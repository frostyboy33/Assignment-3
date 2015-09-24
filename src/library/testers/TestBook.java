package library.testers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import library.entities.Book;
import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;

public class TestBook {

    IBook book;
    ILoan loan;
    
    private final String AUTHOR = "author";
    private final String TITLE = "title";
    private final String CALL_NUMBER = "11AC";
    private final Integer BOOK_ID = 1; 
    
    
    
    @Before
    public void setUp() throws Exception {
        this.book = new Book(this.AUTHOR, 
                             this.TITLE, 
                             this.CALL_NUMBER, 
                             this.BOOK_ID);
        this.loan = Mockito.mock(ILoan.class);
    }



    @After
    public void tearDown() throws Exception {
        this.book = null;
        this.loan = null;
    }


    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionAuthorNull() {
        this.book = null;
        this.book = new Book(null, this.TITLE, this.CALL_NUMBER, this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionTitleNull() {
        this.book = null;
        this.book = new Book(this.AUTHOR, null, this.CALL_NUMBER, this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionCallNumberNull() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, null, this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionBookIDZero() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, this.CALL_NUMBER, 0);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionAuthorEmpty() {
        this.book = null;
        this.book = new Book("", this.TITLE, this.CALL_NUMBER, this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionTitleEmpty() {
        this.book = null;
        this.book = new Book(this.AUTHOR, "", this.CALL_NUMBER, this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionCallNumberEmpty() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, "", this.BOOK_ID);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionBookIDNegative() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, this.CALL_NUMBER, -1);
    }
    
    
    
    @Test
    public void testBook() {
        assertNotNull(this.book);
    }

    

    @Test(expected = RuntimeException.class)
    public void testBorrowRuntimeException() {
        this.book.borrow(loan);
        this.book.borrow(loan);
    }
    
    
    
    @Test
    public void testBorrow() {
        this.book.borrow(this.loan);
        assertEquals(this.book.getLoan(), this.loan);
    }



    @Test
    public void testGetLoan() {
        this.book.borrow(this.loan);
        assertEquals(this.book.getLoan(), this.loan);
    }
    
    
    
    @Test
    public void testGetLoanNull() {
        assertEquals(this.book.getLoan(), null);
    }



    @Test
    public void testReturnBookDamaged() {
        this.book.borrow(this.loan);
        this.book.returnBook(true);
        assertEquals(this.book.getState(), EBookState.DAMAGED);
    }
    
    
    
    @Test
    public void testReturnBookNotDamaged() {
        this.book.borrow(this.loan);
        this.book.returnBook(false);
        assertEquals(this.book.getState(), EBookState.AVAILABLE);
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testReturnBookNotOnLoan() {
        this.book.returnBook(true);
    }



    @Test
    public void testLose() {
        this.book.borrow(this.loan);
        this.book.lose();
        assertEquals(this.book.getState(), EBookState.LOST);
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testLoseNotOnLoan() {
        this.book.lose();
    }



    @Test
    public void testRepair() {
        this.book.borrow(this.loan);
        this.book.returnBook(true);
        this.book.repair();
        assertEquals(this.book.getState(), EBookState.AVAILABLE);
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testRepairNotDamaged() {
        this.book.repair();
    }



    @Test
    public void testDispose() {
        this.book.dispose();
        assertEquals(this.book.getState(), EBookState.DISPOSED);
    }
    
    
    
    @Test(expected = RuntimeException.class)
    public void testDisposeOnLoan() {
        this.book.borrow(loan);
        this.book.dispose();
    }



    @Test
    public void testGetState() {
        assertEquals(this.book.getState(), EBookState.AVAILABLE);
    }



    @Test
    public void testGetAuthor() {
        assertEquals(this.book.getAuthor(), this.AUTHOR);
    }



    @Test
    public void testGetTitle() {
        assertEquals(this.book.getTitle(), this.TITLE);
    }



    @Test
    public void testGetCallNumber() {
        assertEquals(this.book.getCallNumber(), this.CALL_NUMBER);
    }



    @Test
    public void testGetID() {
        assertEquals((Integer)this.book.getID(), (Integer)this.BOOK_ID);
    }
}
