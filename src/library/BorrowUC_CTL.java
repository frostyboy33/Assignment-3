package library;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	                //this.ui.displayScannedBookDetails("");
	                //this.ui.displayPendingLoan("");
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
	            String loanDetails = "";
                for (Iterator<ILoan> loan = this.loanList.iterator(); loan.hasNext();) {
                    loanDetails += loan.next().toString();
                }
                this.ui.displayExistingLoan(loanDetails);
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
		throw new RuntimeException("Not implemented yet");
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
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansConfirmed() {
		throw new RuntimeException("Not implemented yet");
	}

	@Override
	public void loansRejected() {
		throw new RuntimeException("Not implemented yet");
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
