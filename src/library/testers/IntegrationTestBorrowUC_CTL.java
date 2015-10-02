package library.testers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import library.BorrowUC_CTL;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.ILoan;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

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
    

    @Before
    public void setUp() throws Exception {
        
        
        this.borrowUI = Mockito.mock(IBorrowUI.class);
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



    @After
    public void tearDown() throws Exception {
    }



    @Test
    public void testInitialise() {
        fail("Not yet implemented");
    }



    @Test
    public void testCardSwiped() {
        fail("Not yet implemented");
    }

}
