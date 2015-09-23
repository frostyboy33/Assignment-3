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
            this.nextID = 1;
        }
    }
    
    
    
    @Override
    public IBook addBook(String author, String title, String callNo) {
        IBook book = this.helper.makeBook(author, title, callNo, this.nextID);
        this.bookMap.put(this.nextID, book);
        this.nextID++;
        return book;
    }



    @Override
    public IBook getBookByID(int id) {
        if(bookMap.containsKey(id)) {
            return bookMap.get(id);
        } else {
            return null;
        }
    }



    @Override
    public List<IBook> listBooks() {
        List<IBook> list = (List<IBook>) this.bookMap.values();
        return list;
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
