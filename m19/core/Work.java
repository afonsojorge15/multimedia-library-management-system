package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

enum Category{REFERENCE, FICTION, SCITECH}            //work can only be one of these 3 categories, hence enum

public abstract class Work implements Serializable{   //work is an abstract class. allows for greater flexibility in Dvd and Work
    private int _id;                                  //implements serializable to be able to be stored in files  
    private int _numberOfCopies;
    private int _remainingCopies;
    private int _price;
    private String _title;
    private Category _category;
    protected String _type;

    public Work(){}

    public Work(int price, String title, Category cat, String type, int numberOfCopies){
        _price = price;
        _title = title;
        _category = cat;
        _type = type;
        _numberOfCopies = numberOfCopies;
        _remainingCopies = numberOfCopies;
    }

    public abstract String printDescription();      //abstract because it accesses different fields in Book and Dvd

    public abstract String getWriter();             //abstract because it returns different fields in Book and Dvd

    public void changeAvailable(int i){
        //System.out.println("change: "+i);
        _remainingCopies += i;
        return;
    }

    public int getPrice(){
        return _price;
    }

    public String getTitle(){
        return _title;
    }

    public int getNumberOfCopies(){
        return _numberOfCopies;
    }

    public int getRemainingCopies(){
        return _remainingCopies;
    }

    public int getId(){
        return _id;
    }

    public int getNumCopy(){
        return _numberOfCopies;
    }

    public Category getCategory(){
        return _category;
    }

    public void setId(int id){                  //sets work id
        _id = id;
    }
}

