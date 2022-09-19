package m19.core;
import java.util.*;
import java.io.*;

import java.io.Serializable;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;
import java.util.Locale;

enum Behave{NORMAL, CUMPRIDOR, FALTOSO;}

public class User implements Comparable<User>, Serializable{        //implements serializable to be able to be stored in files  
    private int _id;                                                //implements comparable to use in later sorts
    private boolean _isActive;
    private String _name;
    private String _email;
    private Behave _behaviour;
    private ArrayList<Request> _requestList;
    private ArrayList<Notification> _notifList;
    private int _fineVal; 
    private int _streak;

    public User(String name, String email){
        _name = name;
        _email = email;
        _isActive = true;
        _behaviour = Behave.NORMAL;                                 //behaviour defaults to NORMAL upon creation
        _requestList = new ArrayList<Request>();
        _notifList = new ArrayList<Notification>();
        _fineVal = 0;
        _streak = 0;
    }

    public void addNotif(Work work, boolean request_return){
        String st = "";
        if(request_return){
            st = "ENTREGA: "+work.printDescription();;
        }else{
            st = "REQUISICAO: "+work.printDescription();;
        }
        Notification a = new Notification(st);
        _notifList.add(a);
    }

    public String printNotifs(){
        String ret = "";
        for(Notification notif: _notifList){
            ret += notif.getText()+"\n";
        }
        _notifList.clear();
        return ret;
    }

    public void updateStreak(boolean onTime){
        if(onTime==true){
            _streak = (_streak>-1) ? _streak+1 : 1;
        }else{
            _streak = (_streak<1) ? _streak-1 : -1;
        }
        if(_streak>2){
            _behaviour = Behave.NORMAL;
        }
        if(_streak>4){
            _behaviour = Behave.CUMPRIDOR;
        }else if(_streak < -2){
            _behaviour = Behave.FALTOSO;
        }
    }

    public int getFine(){
        return _fineVal;
    }

    public void setFine(int fine){
        _fineVal = fine;
        return;
    }

    public void addReq(Request req){
        _requestList.add(req);
    }

    public void setActive(boolean active){
        _isActive = active;
    }

    public boolean isActive(){
        return _isActive;
    }

    public String getDescription(){                                 //returns User description with appropriate formating
        String active;
        if(_isActive){
            active = "ACTIVO";
            return _id +" - " + _name + " - " + _email +" - " + _behaviour + " - " + active; 
        } else {
            active = "SUSPENSO";
            return _id +" - " + _name + " - " + _email +" - " + _behaviour + " - " + active + " - EUR "+_fineVal; 
        }
    }

    public String getName(){
        return _name;
    }

    public void setNormal(){
        _behaviour = Behave.NORMAL;
    }

    public String getEMail(){
        return _email;
    }

    public int getId(){
        return _id;
    }

    public ArrayList<Request> getReqList(){
        return _requestList;
    }

    public Behave getBehave(){
        return _behaviour;
    }

    public void payFine(){
        _fineVal = 0;
    }

    public void delReq(int i){
        _requestList.remove(i);
    }

    public boolean getActive(){
        return _isActive;
    }

    public void setId(int id){
        _id = id;
    }

    public void addNotif(String text){
        Notification a = new Notification(text);
        _notifList.add(a);
    }

    public String getAllNotifs(){
        String ret = "";
        for(Notification a: _notifList){
            ret += a.getText() + "\n";
        }
        return ret;
    }

    public int compareTo(User a){                             //compareTo function belonging to comparable first compares name (not case sensitive). If equal, compares Id
        if(this._name.compareTo(a.getName())!=0){
            return this._name.toLowerCase().compareTo(a.getName().toLowerCase());
        }else{
            return _id-a.getId();
        }
    }
}

