package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class Notification implements Serializable{   
    private String _text;

    public Notification(String text){
        _text = text;
    }

    public String getText(){
        return _text;
    }
}