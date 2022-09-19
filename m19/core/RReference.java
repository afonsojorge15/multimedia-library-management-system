package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class RReference extends Rule implements Serializable{   
    private int _id;

    public RReference(int id){
        _id = id;
    }

    public boolean checkRule(Work work, User user){
        if(work.getCategory()==Category.REFERENCE){
            return false;
        }
        return true;
    }
}