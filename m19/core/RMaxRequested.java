package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class RMaxRequested extends Rule implements Serializable{   
    private int _id;

    public RMaxRequested(int id){
        _id = id;
    }

    public boolean checkRule(Work work, User user){
        if(user.getBehave() == Behave.NORMAL){
            if(user.getReqList().size()>2){
                return false;
            }
        }else if(user.getBehave() == Behave.FALTOSO){
            if(user.getReqList().size()>0){
                return false;
            }
        }else{
            if(user.getReqList().size()>4){
                return false;
            }
        }
        return true;
    }
}