package library.entities;

import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookHelper implements IBookHelper {

    @Override
    public IBook makeBook(String author, String title, String callNumber,
                          int id) {
        IBook book = new Book(author, title, callNumber, id);
        return book;
    }

}
