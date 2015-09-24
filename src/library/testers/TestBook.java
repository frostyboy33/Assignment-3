package library.testers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.Book;
import library.interfaces.entities.IBook;

public class TestBook {

    IBook book;
    
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
    }



    @After
    public void tearDown() throws Exception {
        this.book = null;
    }


    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionAuthor() {
        this.book = null;
        this.book = new Book(null, this.TITLE, this.CALL_NUMBER, this.BOOK_ID);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionTitle() {
        this.book = null;
        this.book = new Book(this.AUTHOR, null, this.CALL_NUMBER, this.BOOK_ID);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionCallNumber() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, null, this.BOOK_ID);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookIllegalArgumentExceptionBookID() {
        this.book = null;
        this.book = new Book(this.AUTHOR, this.TITLE, this.CALL_NUMBER, 0);
    }
    
    @Test
    public void testBook() {
        assertNotNull(this.book);
    }



    @Test
    public void testBorrow() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetLoan() {
        fail("Not yet implemented");
    }



    @Test
    public void testReturnBook() {
        fail("Not yet implemented");
    }



    @Test
    public void testLose() {
        fail("Not yet implemented");
    }



    @Test
    public void testRepair() {
        fail("Not yet implemented");
    }



    @Test
    public void testDispose() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetState() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetAuthor() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetTitle() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetCallNumber() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetID() {
        fail("Not yet implemented");
    }

}
