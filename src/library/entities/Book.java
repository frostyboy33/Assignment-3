package library.entities;

import java.util.Date;

import library.interfaces.entities.EBookState;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
 

public class Book implements IBook {

    private String author;
    private String title;
    private String callNumber;
    private int bookID;
    
    
    public Book(String author, String title, String callNumber, int bookID) {
     
        this.validateConstructorObject(author);
        this.validateConstructorObject(title);
        this.validateConstructorObject(callNumber);
        this.validateConstructorObject(bookID);
        
        this.author = author;
        this.title = title;
        this.callNumber = callNumber;
        this.bookID = bookID;
        
    }
    
    private void validateConstructorObject(Object object) throws IllegalArgumentException {
        if(object instanceof String) {
            String object_string = (String)object;
            if(object_string == null || object_string.isEmpty()) {
                throw new IllegalArgumentException("String cannot be null or empty");
            }   
        }
        else if(object instanceof Integer) {
            Integer object_integer = (Integer)object;
            if(object_integer <= 0) {
                throw new IllegalArgumentException("Integer cannot be less than or equal to 0");
            }
        }
    }
    
    @Override
    public void borrow(ILoan loan) {
        // TODO Auto-generated method stub

    }



    @Override
    public ILoan getLoan() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public void returnBook(boolean damaged) {
        // TODO Auto-generated method stub

    }



    @Override
    public void lose() {
        // TODO Auto-generated method stub

    }



    @Override
    public void repair() {
        // TODO Auto-generated method stub

    }



    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }



    @Override
    public EBookState getState() {
        return null;
    }



    @Override
    public String getAuthor() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String getCallNumber() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public int getID() {
        // TODO Auto-generated method stub
        return 0;
    }

}
