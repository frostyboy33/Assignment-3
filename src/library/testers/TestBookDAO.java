package library.testers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import library.entities.BookDAO;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;

public class TestBookDAO {

    private IBookDAO bookDAO;
    private IBookHelper bookHelper;
    
    @Before
    public void setUp() throws Exception {
        this.bookHelper = Mockito.mock(IBookHelper.class);
        this.bookDAO = new BookDAO(this.bookHelper);
    }



    @After
    public void tearDown() throws Exception {
        this.bookDAO = null;
        this.bookHelper = null;
    }



    @Test
    public void testBookDAO() {
        assertNotNull(this.bookDAO);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBookDAONull() {
        this.bookDAO = null;
        this.bookDAO = new BookDAO(null);
    }



    @Test
    public void testAddBook() {
        fail("Not yet implemented");
    }



    @Test
    public void testGetBookByID() {
        fail("Not yet implemented");
    }



    @Test
    public void testListBooks() {
        fail("Not yet implemented");
    }



    @Test
    public void testFindBooksByAuthor() {
        fail("Not yet implemented");
    }



    @Test
    public void testFindBooksByTitle() {
        fail("Not yet implemented");
    }



    @Test
    public void testFindBooksByAuthorTitle() {
        fail("Not yet implemented");
    }

}
