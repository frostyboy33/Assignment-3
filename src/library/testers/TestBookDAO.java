package library.testers;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import library.entities.BookDAO;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class TestBookDAO {

    private IBookDAO bookDAO;
    private IBookHelper bookHelper;
    private IBook book;
    

    private final String AUTHOR = "author";
    private final String TITLE = "title";
    private final String CALL_NUMBER = "11AC";
    private final Integer BOOK_ID = 1;
    
    @Before
    public void setUp() throws Exception {
        this.book = Mockito.mock(IBook.class);
        Mockito.when(this.book.getAuthor()).thenReturn(this.AUTHOR);
        Mockito.when(this.book.getTitle()).thenReturn(this.TITLE);
        Mockito.when(this.book.getID()).thenReturn(this.BOOK_ID);
        
        this.bookHelper = Mockito.mock(IBookHelper.class);
        Mockito.when(this.bookHelper.makeBook(this.AUTHOR, 
                     this.TITLE,
                     this.CALL_NUMBER,
                     this.BOOK_ID)).thenReturn(this.book);
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
        IBook book = this.bookDAO.addBook(this.AUTHOR, 
                                          this.TITLE, 
                                          this.CALL_NUMBER);
        assertNotNull(book);
    }



    @Test
    public void testGetBookByID() {
        IBook book = this.bookDAO.addBook(this.AUTHOR, 
                                          this.TITLE, 
                                          this.CALL_NUMBER);
        IBook book2 = this.bookDAO.getBookByID(book.getID());
        assertEquals(book,book2);
    }
    
    
    
    @Test
    public void testGetBookByIDNull() {
        IBook book = this.bookDAO.getBookByID(1);
        assertEquals(book, null);
    }



    @Test
    public void testListBooks() {
        List<IBook> list = this.bookDAO.listBooks();
        assertNotNull(list);
    }



    @Test
    public void testFindBooksByAuthor() {
        this.bookDAO.addBook(this.AUTHOR, 
                             this.TITLE, 
                             this.CALL_NUMBER);
        List<IBook> list = this.bookDAO.findBooksByAuthor(this.AUTHOR);
        if(list.get(0).getAuthor().equals(this.AUTHOR)) {
            assertNotNull(list);
        } else {
            fail("List did not have correct author");
        }     
    }
    
    
    
    @Test
    public void testFindBooksByAuthorEmpty() {
        List<IBook> list = this.bookDAO.findBooksByAuthor(null);
        assertNotNull(list);
    }



    @Test
    public void testFindBooksByTitle() {
        this.bookDAO.addBook(this.AUTHOR, 
                             this.TITLE, 
                             this.CALL_NUMBER);
        List<IBook> list = this.bookDAO.findBooksByTitle(this.TITLE);
        if(list.get(0).getTitle().equals(this.TITLE)) {
            assertNotNull(list);
        } else {
            fail("List did not have correct title");
        } 
    }
    
    
    
    @Test
    public void testFindBooksByTitleEmpty() {
        List<IBook> list = this.bookDAO.findBooksByTitle(null);
        assertNotNull(list);
    }



    @Test
    public void testFindBooksByAuthorTitle() {
        this.bookDAO.addBook(this.AUTHOR, 
                             this.TITLE, 
                             this.CALL_NUMBER);
        List<IBook> list = this.bookDAO.findBooksByAuthorTitle(this.AUTHOR, this.TITLE);
        if(list.get(0).getAuthor().equals(this.AUTHOR) && 
           list.get(0).getTitle().equals(this.TITLE)) {
            assertNotNull(list);
        } else {
            fail("List did not have correct title and author");
        } 
    }
    
    
    
    @Test
    public void testFindBooksByAuthorTitleEmpty() {
        List<IBook> list = this.bookDAO.findBooksByAuthorTitle(null,null);
        assertNotNull(list);
    }
}
