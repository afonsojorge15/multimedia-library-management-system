package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class RAllRequested extends Rule implements Serializable{   
    private int _id;

    public RAllRequested(int id){
        _id = id;
    }

    public boolean checkRule(Work work, User user){
        if(work.getRemainingCopies()==0){
            return false;
        }else{
            return true;
        }
    }
}