package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class Dvd extends Work implements Serializable{      //class is a type of work, hence it extends it
    private String _director;                               //implements serializable to be able to be stored in files  
    private String _igac;

    public Dvd(String title, String director, int price, Category cat, String igac, int numberOfCopies){
        super(price, title, cat, "Dvd", numberOfCopies);
        _igac = igac;
        _director = director;
    }

    public String getWriter(){
        return _director;
    }

    public String printDescription(){               //returns Dvd description with appropriate formating
        String cat = "";
        if(getCategory() == Category.FICTION){
            cat = "Ficção";
        }else if(getCategory() == Category.SCITECH){
            cat = "Técnica e Científica";
        }else{
            cat = "Referência";
        }
        return getId()+" - "+getRemainingCopies()+" de  "+getNumberOfCopies()+"- DVD -"+getTitle()+" - "+getPrice()+" - "+cat+" - "+_director+ " - "+_igac;
    }

    public String getDirector(){
        return _director;
    }

    public String getIgac(){
        return _igac;
    }
}

