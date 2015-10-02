package library.testers;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.entities.Loan;
import library.entities.LoanHelper;
import library.entities.LoanMapDAO;
import library.entities.Member;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.ILoanHelper;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;

public class IntegrationTestBook {
    
    IBook book;
    ILoanDAO loanDAO;
    ILoan loan;
    IMember member;
    
    private final String AUTHOR = "author";
    private final String TITLE = "title";
    private final String CALL_NUMBER = "11AC";
    private final Integer BOOK_ID = 1;
    
    private final String FIRST_NAME = "John";
    private final String LAST_NAME = "Smith";
    private final String CONTACT_NUMBER = "6345789";
    private final String EMAIL = "john.smith@example.com";
    private final Integer MEMBER_ID = 1; 

    @Before
    public void setUp() throws Exception {
        this.book = new Book(this.AUTHOR, 
                             this.TITLE, 
                             this.CALL_NUMBER, 
                             this.BOOK_ID);
        
        this.member = new Member(this.FIRST_NAME,
                                 this.LAST_NAME,
                                 this.CONTACT_NUMBER,
                                 this.EMAIL,
                                 this.MEMBER_ID);
        
        this.loanDAO = new LoanMapDAO(new LoanHelper());
        
        this.loan = this.loanDAO.createLoan(this.member, this.book);
    }



    @After
    public void tearDown() throws Exception {
        this.book = null;
        this.loan = null;
    }



    @Test
    public void testBorrow() {
        this.book.borrow(this.loan);
        assertEquals(this.book.getLoan(), this.loan);
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testBorrowRuntimeException() {
        this.book.borrow(this.loan);
        this.book.borrow(this.loan);
    }

}
