package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class Request implements Serializable{   
    private int _deadline;  
    private int _workId;                      

    public Request(int deadline, int workId){
        _workId = workId;
        _deadline = deadline;
    }         

    public int getWorkId(){
        return _workId;
    }

    public int getDeadline(){
        return _deadline;
    }
}