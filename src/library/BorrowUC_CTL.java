package library;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.management.RuntimeErrorException;
import javax.swing.JPanel;

import library.entities.BookDAO;
import library.entities.BookHelper;
import library.entities.LoanHelper;
import library.entities.LoanMapDAO;
import library.entities.MemberHelper;
import library.entities.MemberMapDAO;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.IBorrowUIListener;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.daos.IMemberHelper;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.EMemberState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.ICardReaderListener;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.interfaces.hardware.IScannerListener;

public class BorrowUC_CTL implements ICardReaderListener, 
									 IScannerListener, 
									 IBorrowUIListener {
	
	private ICardReader reader;
	private IScanner scanner; 
	private IPrinter printer; 
	private IDisplay display;
	//private String state;
	private int scanCount = 0;
	private IBorrowUI ui;
	private EBorrowState state; 
	private IBookDAO bookDAO;
	private IMemberDAO memberDAO;
	private ILoanDAO loanDAO;
	
	private List<IBook> bookList;
	private List<ILoan> loanList;
	private List<ILoan> pendingLoanList;
	private IMember borrower;
	
	private JPanel previous;


	public BorrowUC_CTL(ICardReader reader, IScanner scanner, 
			IPrinter printer, IDisplay display,
			IBookDAO bookDAO, ILoanDAO loanDAO, IMemberDAO memberDAO ) {

	    this.reader = reader;
	    this.scanner = scanner;
	    this.printer = printer;
	    this.bookDAO = bookDAO;
	    this.loanDAO = loanDAO;
	    this.memberDAO = memberDAO;
	    
//	    try {
//            this.bookDAO = (IBookDAO) 
//                           this.validateConstructorDAO(bookDAO, 
//                                                       BookDAO.class,
//                                                       IBookHelper.class,
//                                                       BookHelper.class);
//            this.loanDAO = (ILoanDAO) 
//                           this.validateConstructorDAO(bookDAO, 
//                                                       LoanMapDAO.class,
//                                                       ILoanHelper.class,
//                                                       LoanHelper.class);
//            this.memberDAO = (IMemberDAO) 
//                              this.validateConstructorDAO(bookDAO, 
//                                                          MemberMapDAO.class,
//                                                          IMemberHelper.class,
//                                                          MemberHelper.class);
//        }
//	    catch (Exception e) {
//	        e.printStackTrace();
//	    }
	    
	    this.reader.addListener(this);
	    this.scanner.addListener(this);
	    this.pendingLoanList = new ArrayList<ILoan>();
		this.display = display;
		this.ui = new BorrowUC_UI(this);
		state = EBorrowState.CREATED;
	}
	
	
	
	private Object validateConstructorDAO(Object incomming, 
	                                      Class<?> backup,
	                                      Class<?> backupParameter,
	                                      Class<?> backupHelper) 
	        throws Exception {
	    Object returnObject = null;
	    if(incomming == null) {
	        returnObject = backup.getConstructor(backupParameter).newInstance(backupHelper.newInstance());
	    } else {
	        returnObject = incomming;
	    }
	    
	    return returnObject;
	}
	
	
	
	public void initialise() {
	    if(this.state == EBorrowState.CREATED) {
    		previous = display.getDisplay();
    		display.setDisplay((JPanel) ui, "Borrow UI");
    		this.reader.setEnabled(true);
    		this.scanner.setEnabled(false);
    		this.state = EBorrowState.INITIALIZED;
	    } 
	    else {
	        throw new RuntimeException();
	    }
	}
	
	public void close() {
		display.setDisplay(previous, "Main Menu");
		this.reader.setEnabled(false);
		this.scanner.setEnabled(false);
		this.setState(EBorrowState.CREATED);
		this.ui.setState(EBorrowState.CANCELLED);
		this.scanCount = 0;
		this.pendingLoanList = null;
		this.pendingLoanList = new ArrayList<ILoan>();
	}

	@Override
	public void cardSwiped(int memberID) {
	    if(this.state == EBorrowState.INITIALIZED) {
	        this.borrower = this.memberDAO.getMemberByID(memberID);
	        if(this.borrower != null){
	            // member exists
	            
	            boolean overdue = this.borrower.hasOverDueLoans();
	            boolean atLoanLimit = this.borrower.hasReachedLoanLimit();
	            boolean hasFines = this.borrower.hasFinesPayable();
	            boolean overFineLimit = this.borrower.hasReachedFineLimit();
	            if(overdue || atLoanLimit || hasFines || overFineLimit){
	                this.setState(EBorrowState.BORROWING_RESTRICTED);
	                this.reader.setEnabled(false);
	                this.scanner.setEnabled(false);
	                this.ui.setState(EBorrowState.BORROWING_RESTRICTED);
	                if(hasFines) {
	                    this.ui.displayOutstandingFineMessage(this.borrower.
	                                                          getFineAmount());
	                }
	                if(overdue){
	                    this.ui.displayOverDueMessage();
	                }
	                if(atLoanLimit){
	                    this.ui.displayAtLoanLimitMessage();
	                }
	                if(overFineLimit){
	                    this.ui.displayOverFineLimitMessage(this.borrower.
	                                                        getFineAmount());
	                }
	            }
	            else {
	                this.setState(EBorrowState.SCANNING_BOOKS);
	                this.reader.setEnabled(false);
	                this.scanner.setEnabled(true);
	                this.ui.setState(EBorrowState.SCANNING_BOOKS);
	                this.ui.displayScannedBookDetails("");
	                this.ui.displayPendingLoan("");   
	            }
	            this.loanList = this.borrower.getLoans();
	            this.scanCount = this.loanList.size();
                this.ui.displayExistingLoan(this.buildLoanListDisplay(this.loanList));
                this.ui.displayMemberDetails(this.borrower.getID(),
                                             this.borrower.getFirstName() + " "
                                             + this.borrower.getLastName(),
                                             this.borrower.getContactPhone());
	        }
	    }
	    else {
	        throw new RuntimeException();
	    }
	}
	
	
	
	@Override
	public void bookScanned(int barcode) {
	    if(this.state == EBorrowState.SCANNING_BOOKS){
	        this.ui.displayErrorMessage("");
	        IBook book = this.bookDAO.getBookByID(barcode);
	        if(book != null){
	            EBookState bookState = book.getState();
	            if(bookState == EBookState.AVAILABLE) {
	                boolean isNotPending = true;
	                for (Iterator<ILoan> iterator = this.pendingLoanList.iterator(); iterator.hasNext();) {
                        ILoan loan_item = iterator.next();
                        if(loan_item.getBook().getID() == barcode){
                            isNotPending = false;
                        }
                    }
	                if(isNotPending){
	                    if(this.scanCount < IMember.LOAN_LIMIT){
	                        this.scanCount++;
        	                ILoan loan = this.loanDAO.createLoan(this.borrower, book);
        	                this.pendingLoanList.add(loan);
        	                this.ui.displayScannedBookDetails(book.toString());	                
        	                this.ui.displayPendingLoan(this.buildLoanListDisplay(this.pendingLoanList));
	                    }
	                    if(this.scanCount == IMember.LOAN_LIMIT) {
	                        this.scansCompleted();
	                    }
	                }
	                else {
	                    this.ui.displayErrorMessage("Book already scanned");
	                }
	            }
	            else {
	                this.ui.displayErrorMessage("Book not avaliable");
	            }
	        }
	        else {
	            this.ui.displayErrorMessage("Book not found");
	        }
	        
	    }
	    else {
	        throw new RuntimeException();
	    }
		
	}

	
	private void setState(EBorrowState state) {
		this.state = state;
	}

	@Override
	public void cancelled() {
		close();
	}
	
	@Override
	public void scansCompleted() {
	    if(this.state == EBorrowState.SCANNING_BOOKS){
	        this.setState(EBorrowState.CONFIRMING_LOANS);
	        this.ui.setState(EBorrowState.CONFIRMING_LOANS);
	        this.reader.setEnabled(false);
	        this.scanner.setEnabled(false);
	        this.ui.displayConfirmingLoan(this.buildLoanListDisplay(this.pendingLoanList));
	    }
	    else {
	        throw new RuntimeException();
	    }
		
	}

	@Override
	public void loansConfirmed() {
	    if(this.state == EBorrowState.CONFIRMING_LOANS){
	        for (ILoan loan : this.pendingLoanList) {
                this.loanDAO.commitLoan(loan);
            }
	        List<ILoan> completeLoanList = new ArrayList<ILoan>();
	        completeLoanList.addAll(this.loanList);
	        // Note no need to add the pending loan list as the loanList is
	        // a pointer to the loanDAO internal Map. So therefore when
	        // commitLoan is called above it this.loanList has the compelte
	        // list of all loans
	        
	        this.printer.print(this.buildLoanListDisplay(completeLoanList));
	        
	        this.close();
	    }
	    else {
	        throw new RuntimeException("Not implemented yet");    
	    }
	}

	@Override
	public void loansRejected() {
	    if(this.state == EBorrowState.CONFIRMING_LOANS){
	        
	    }
	    else {
	        throw new RuntimeException("Not implemented yet");    
	    }
		
	}

	private String buildLoanListDisplay(List<ILoan> loans) {
		StringBuilder bld = new StringBuilder();
		for (ILoan ln : loans) {
			if (bld.length() > 0) bld.append("\n\n");
			bld.append(ln.toString());
		}
		return bld.toString();		
	}

}
