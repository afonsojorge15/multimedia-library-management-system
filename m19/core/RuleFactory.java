package m19.core;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;

public class RuleFactory implements Serializable{   

    public Rule createRule(int id){
        if(id == 1){
            return new RReqTwice(id);
        }else if(id == 2){
            return new RSuspendedUser(id);
        }else if(id == 3){
            return new RAllRequested(id);
        }else if(id == 4){
            return new RMaxRequested(id);
        }else if(id == 5){
            return new RReference(id);
        }else{
            return new RBigPrice(id);
        }
    }
}