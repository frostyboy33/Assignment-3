package library.testers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import library.BorrowUC_CTL;
import library.entities.BookDAO;
import library.entities.BookHelper;
import library.entities.LoanHelper;
import library.entities.LoanMapDAO;
import library.entities.MemberHelper;
import library.entities.MemberMapDAO;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;
import library.panels.borrow.ABorrowPanel;

public class IntegrationTestBorrowUC_CTL {
    
    ICardReader cardReader;
    IScanner scanner;
    IPrinter printer;
    IDisplay display;
    IBookDAO bookDAO;
    ILoanDAO loanDAO;
    IMemberDAO memberDAO;
    IBorrowUI borrowUI;
    List<ILoan> pendingLoanList;
    
    BorrowUC_CTL borrowControl;
    
    private final Integer MEMBER_CLEAN = 1;
    private final Integer MEMBER_OVERDUE = 2;
    private final Integer MEMBER_MAXED_FINES = 3;
    private final Integer MEMBER_MAXED_LOANS = 4;
    private final Integer MEMBER_ACCEPTABLE_FINES = 5;
    private final Integer MEMBER_ACCEPTABLE_LOANS = 6;
    
    

    @Before
    public void setUp() throws Exception {
        
        this.cardReader = Mockito.mock(ICardReader.class);
        this.scanner = Mockito.mock(IScanner.class);
        this.printer = Mockito.mock(IPrinter.class);
        this.display = Mockito.mock(IDisplay.class);
        
        
        this.bookDAO = new BookDAO(new BookHelper());      
        this.memberDAO = new MemberMapDAO(new MemberHelper());      
        this.loanDAO = new LoanMapDAO(new LoanHelper());
        this.setupTestData();
        
        this.borrowUI = Mockito.mock(ABorrowPanel.class);
        this.pendingLoanList = new ArrayList<ILoan>();
        
        this.borrowControl = new BorrowUC_CTL(this.cardReader,
                                              this.scanner,
                                              this.printer,
                                              this.display,
                                              this.bookDAO,
                                              this.loanDAO,
                                              this.memberDAO,
                                              this.borrowUI,
                                              this.pendingLoanList);
        
        
    }
    
    
    
    private void setupTestData() {
        IBook[] book = new IBook[15];
        IMember[] member = new IMember[6];
        
        book[0]  = bookDAO.addBook("author1", "title1", "callNo1");
        book[1]  = bookDAO.addBook("author1", "title2", "callNo2");
        book[2]  = bookDAO.addBook("author1", "title3", "callNo3");
        book[3]  = bookDAO.addBook("author1", "title4", "callNo4");
        book[4]  = bookDAO.addBook("author2", "title5", "callNo5");
        book[5]  = bookDAO.addBook("author2", "title6", "callNo6");
        book[6]  = bookDAO.addBook("author2", "title7", "callNo7");
        book[7]  = bookDAO.addBook("author2", "title8", "callNo8");
        book[8]  = bookDAO.addBook("author3", "title9", "callNo9");
        book[9]  = bookDAO.addBook("author3", "title10", "callNo10");
        book[10] = bookDAO.addBook("author4", "title11", "callNo11");
        book[11] = bookDAO.addBook("author4", "title12", "callNo12");
        book[12] = bookDAO.addBook("author5", "title13", "callNo13");
        book[13] = bookDAO.addBook("author5", "title14", "callNo14");
        book[14] = bookDAO.addBook("author5", "title15", "callNo15");
        
        member[0] = memberDAO.addMember("fName0", "lName0", "0001", "email0");
        member[1] = memberDAO.addMember("fName1", "lName1", "0002", "email1");
        member[2] = memberDAO.addMember("fName2", "lName2", "0003", "email2");
        member[3] = memberDAO.addMember("fName3", "lName3", "0004", "email3");
        member[4] = memberDAO.addMember("fName4", "lName4", "0005", "email4");
        member[5] = memberDAO.addMember("fName5", "lName5", "0006", "email5");
        
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
                
        //create a member with overdue loans        
        for (int i=0; i<2; i++) {
            ILoan loan = loanDAO.createLoan(member[1], book[i]);
            loanDAO.commitLoan(loan);
        }
        cal.setTime(now);
        cal.add(Calendar.DATE, ILoan.LOAN_PERIOD + 1);
        Date checkDate = cal.getTime();     
        loanDAO.updateOverDueStatus(checkDate);
        
        //create a member with maxed out unpaid fines
        member[2].addFine(10.0f);
        
        //create a member with maxed out loans
        for (int i=2; i<7; i++) {
            ILoan loan = loanDAO.createLoan(member[3], book[i]);
            loanDAO.commitLoan(loan);
        }
        
        //a member with a fine, but not over the limit
        member[4].addFine(5.0f);
        
        //a member with a couple of loans but not over the limit
        for (int i=7; i<9; i++) {
            ILoan loan = loanDAO.createLoan(member[5], book[i]);
            loanDAO.commitLoan(loan);
        }
    }



    @After
    public void tearDown() throws Exception {
        this.cardReader = null;
        this.scanner = null;
        this.printer = null;
        this.display = null;
        
        this.bookDAO = null;
        this.loanDAO = null;
        this.memberDAO = null;
        
        
        this.borrowUI = null;
        this.pendingLoanList = null;
        
        this.borrowControl = null;
    }


    

    @Test
    public void testInitialise() {
        this.borrowControl.initialise();
        assertEquals(EBorrowState.INITIALIZED, this.borrowControl.getState());
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testInitialiseRuntimeException() {
        this.borrowControl.initialise();
        this.borrowControl.initialise();
    }

    
    

    @Test
    public void testCardSwiped() {
        final Integer memberID = this.MEMBER_CLEAN;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(true);
        Mockito.verify(this.borrowUI).setState(EBorrowState.SCANNING_BOOKS);
                
        assertEquals(EBorrowState.SCANNING_BOOKS, this.borrowControl.getState());
        
        assertFalse(this.memberDAO.getMemberByID(memberID).hasOverDueLoans());
        assertFalse(this.memberDAO.getMemberByID(memberID).hasReachedFineLimit());
        assertFalse(this.memberDAO.getMemberByID(memberID).hasReachedLoanLimit());
        assertFalse(this.memberDAO.getMemberByID(memberID).hasFinesPayable());
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testCardSwipedRuntimeException() {
        this.borrowControl.cardSwiped(this.MEMBER_CLEAN);
    }
    
    
    
    @Test
    public void testCardSwipedOverdue() {
        final Integer memberID = this.MEMBER_OVERDUE;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(false);
        Mockito.verify(this.borrowUI).setState(EBorrowState.BORROWING_RESTRICTED);
        Mockito.verify(this.borrowUI).displayOverDueMessage();
        
        assertEquals(EBorrowState.BORROWING_RESTRICTED, this.borrowControl.getState());
        assertTrue(this.memberDAO.getMemberByID(memberID).hasOverDueLoans());
    }
    
    
    
    @Test
    public void testCardSwipedMaxedFines() {
        final Integer memberID = this.MEMBER_MAXED_FINES;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(false);
        Mockito.verify(this.borrowUI).setState(EBorrowState.BORROWING_RESTRICTED);
        Mockito.verify(this.borrowUI).displayOverFineLimitMessage(
                                      this.memberDAO.getMemberByID(memberID).getFineAmount());
        
        assertEquals(EBorrowState.BORROWING_RESTRICTED, this.borrowControl.getState());
        assertTrue(this.memberDAO.getMemberByID(memberID).hasReachedFineLimit());
    }
    
    @Test
    public void testCardSwipedMaxedLoans() {
        final Integer memberID = this.MEMBER_MAXED_LOANS;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(false);
        Mockito.verify(this.borrowUI).setState(EBorrowState.BORROWING_RESTRICTED);
        Mockito.verify(this.borrowUI).displayAtLoanLimitMessage();
        
        assertEquals(EBorrowState.BORROWING_RESTRICTED, this.borrowControl.getState());
        assertTrue(this.memberDAO.getMemberByID(memberID).hasReachedLoanLimit());
    }
    
    @Test
    public void testCardSwipedAcceptableFines() {
        final Integer memberID = this.MEMBER_ACCEPTABLE_FINES;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(true);
        Mockito.verify(this.borrowUI).setState(EBorrowState.SCANNING_BOOKS);
        Mockito.verify(this.borrowUI).displayOutstandingFineMessage(
                                      this.memberDAO.getMemberByID(memberID).getFineAmount());
        
        assertEquals(EBorrowState.SCANNING_BOOKS, this.borrowControl.getState());
        assertTrue(this.memberDAO.getMemberByID(memberID).hasFinesPayable());
    }
    
    @Test
    public void testCardSwipedAcceptableLoans() {
        final Integer memberID = this.MEMBER_ACCEPTABLE_LOANS;
        this.borrowControl.initialise();
        
        Mockito.reset(this.scanner);
        
        this.borrowControl.cardSwiped(memberID);
        
        Mockito.verify(this.cardReader).setEnabled(false);
        Mockito.verify(this.scanner).setEnabled(true);
        Mockito.verify(this.borrowUI).setState(EBorrowState.SCANNING_BOOKS);
        
        assertEquals(EBorrowState.SCANNING_BOOKS, this.borrowControl.getState());
        assertNotNull(this.memberDAO.getMemberByID(memberID).getLoans());
    }

}
