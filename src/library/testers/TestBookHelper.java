package library.testers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import library.entities.BookHelper;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class TestBookHelper {

    private IBookHelper bookHelper;
    
    private final String AUTHOR = "author";
    private final String TITLE = "title";
    private final String CALL_NUMBER = "11AC";
    private final Integer BOOK_ID = 1;
    
    @Before
    public void setUp() throws Exception {
        this.bookHelper = new BookHelper();
    }



    @After
    public void tearDown() throws Exception {
        this.bookHelper = null;
    }



    @Test
    public void testMakeBook() {
        IBook book = this.bookHelper.makeBook(this.AUTHOR,
                                              this.TITLE, 
                                              this.CALL_NUMBER, 
                                              this.BOOK_ID);
        assertNotNull(book);
    }

}
