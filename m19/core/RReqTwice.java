package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import m19.app.exception.FileOpenFailedException;

public class RReqTwice extends Rule implements Serializable{   
    private int _id;

    public RReqTwice(int id){
        _id = id;
    }

    public boolean checkRule(Work work, User user){
        ArrayList<Request> a = user.getReqList();
        int id = work.getId();
        for(Request req: a){
            if(req.getWorkId() == id){
                return false;
            }
        }
        return true;
    }

}