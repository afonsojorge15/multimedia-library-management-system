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

public class Order implements Serializable{
    private int _userId;
    private int _workId;

    public Order(int wk, int usr){
        _userId = usr;
        _workId = wk;
    }

    public int getUserId(){
        return _userId;
    }

    public int getWorkId(){
        return _workId;
    }
}