package library.entities;

import java.util.ArrayList;
import java.util.HashMap;
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
            this.bookMap = new HashMap<Integer, IBook>();
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
        List<IBook> list = new ArrayList<IBook>(this.bookMap.values());
        return list;
    }



    @Override
    public List<IBook> findBooksByAuthor(String author) {
        List<IBook> books = new ArrayList<IBook>();
        for (IBook book : this.bookMap.values()) {
            if(book.getAuthor().equals(author)) {
                books.add(book);
            }
        }
        return books;
    }



    @Override
    public List<IBook> findBooksByTitle(String title) {
        List<IBook> books = new ArrayList<IBook>();
        for (IBook book : this.bookMap.values()) {
            if(book.getTitle().equals(title)) {
                books.add(book);
            }
        }
        return books;
    }



    @Override
    public List<IBook> findBooksByAuthorTitle(String author, String title) {
        List<IBook> books = new ArrayList<IBook>();
        for (IBook book : this.bookMap.values()) {
            if(book.getAuthor().equals(author) && 
               book.getTitle().equals(title)) {
                books.add(book);
            }
        }
        return books;
    }
}
