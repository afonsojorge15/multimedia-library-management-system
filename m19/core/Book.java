package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class Book extends Work implements Serializable{         //class is a type of work, hence it extends it
    private String _author;                                     //implements serializable to be able to be stored in files  
    private String _isbn;

    public Book(String title, String author, int price, Category cat, String isbn, int numberOfCopies){
        super(price, title, cat, "Book", numberOfCopies);
        _isbn = isbn;
        _author = author;
    }

    public String getWriter(){
        return _author;
    }

    public String printDescription(){                   //returns Dvd description with appropriate formating
        String cat = "";
        if(getCategory() == Category.FICTION){
            cat = "Ficção";
        }else if(getCategory() == Category.SCITECH){
            cat = "Técnica e Científica";
        }else{
            cat = "Referência";
        }
        return getId()+" - "+getRemainingCopies()+" de  "+getNumberOfCopies()+" - Livro - "+getTitle()+" - "+getPrice()+" - "+cat+ " - "+_author+" - "+_isbn;
    }

    public String getAuthor(){
        return _author;
    }

    public String getIsbn(){
        return _isbn;
    }
}
