package library.entities;

import java.util.List;
import java.util.Map;

import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookDAO implements IBookDAO {

    private int nextID;
    private Map<Integer, IBook> bookMap;
    private IBookHelper helper;
    
    public BookDAO(IBookHelper helper) {
        if(helper == null) {
            throw new IllegalArgumentException("Helper cannot be null");
        } else {
            this.helper = helper;
        }
    }
    
    
    
    @Override
    public IBook addBook(String author, String title, String callNo) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public IBook getBookByID(int id) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public List<IBook> listBooks() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public List<IBook> findBooksByAuthor(String author) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public List<IBook> findBooksByTitle(String title) {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public List<IBook> findBooksByAuthorTitle(String author, String title) {
        // TODO Auto-generated method stub
        return null;
    }

}
