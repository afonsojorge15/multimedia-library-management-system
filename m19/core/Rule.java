package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public abstract class Rule implements Serializable{   
    private int _id;

    public Rule(){}

    public abstract boolean checkRule(Work work, User user);

    public int getId(){
        return _id;
    }
}