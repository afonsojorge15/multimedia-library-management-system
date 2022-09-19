package m19.core;
import java.io.Serializable;

public class Date implements Serializable{          //implements serializable to be able to be stored in files  
    private int _currentDate;

    public Date(){
        _currentDate = 0;
    }

    protected int getCurrentDate(){
        return _currentDate;
    }

    protected void advanceDays(int nDays){
        if(nDays > 0){
            _currentDate += nDays;
        }
    }
}

