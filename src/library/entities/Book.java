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
    private int iD;
    
    private ILoan loan;
    private EBookState state;
    
    private final String NOT_AVALIABLE = "The book is not avaliable";
    private final String NOT_ON_LOAN = "The book is not currently out on loan";
    private final String NOT_DAMAGED = "The book is not currently Damaged";
    private final String CANNOT_DISPOSE = "Cannot dispose book";
    
    public Book(String author, String title, String callNumber, int bookID) 
           throws IllegalArgumentException {
     
        this.validateConstructorObject(author);
        this.validateConstructorObject(title);
        this.validateConstructorObject(callNumber);
        this.validateConstructorObject(bookID);
        
        this.author = author;
        this.title = title;
        this.callNumber = callNumber;
        this.iD = bookID;
        this.state = EBookState.AVAILABLE;
        
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
    public void borrow(ILoan loan) throws RuntimeException {
        if(this.state == EBookState.AVAILABLE) {
            this.loan = loan;
        } else {
            throw new RuntimeException(this.NOT_AVALIABLE);
        }
    }



    @Override
    public ILoan getLoan() {
        if(this.state == EBookState.ON_LOAN) {
            return this.loan;
        } else {
            return null;
        }
    }



    @Override
    public void returnBook(boolean damaged) throws RuntimeException {
        if(this.state == EBookState.ON_LOAN) {
            this.loan = null;
            if(damaged) {
                this.state = EBookState.DAMAGED;
            } else {
                this.state = EBookState.AVAILABLE;
            }
        } else {
            throw new RuntimeException(this.NOT_ON_LOAN);
        }
    }



    @Override
    public void lose() throws RuntimeException {
        if(this.state == EBookState.ON_LOAN) {
            this.state = EBookState.LOST;
        } else {
            throw new RuntimeException(this.NOT_ON_LOAN);
        }
    }



    @Override
    public void repair() throws RuntimeException {
        if(this.state == EBookState.DAMAGED) {
            this.state = EBookState.AVAILABLE;
        } else {
            throw new RuntimeException(this.NOT_DAMAGED);
        }
    }



    @Override
    public void dispose() throws RuntimeException {
        if(this.state == EBookState.AVAILABLE ||
           this.state == EBookState.DAMAGED ||
           this.state == EBookState.LOST) {
            this.state = EBookState.DISPOSED;
        } else {
            throw new RuntimeException(this.CANNOT_DISPOSE);
        }
    }



    @Override
    public EBookState getState() {
        return this.state;
    }



    @Override
    public String getAuthor() {
        return this.author;
    }



    @Override
    public String getTitle() {
        return this.getTitle();
    }



    @Override
    public String getCallNumber() {
        return this.callNumber;
    }



    @Override
    public int getID() {
        return this.iD;
    }

}
