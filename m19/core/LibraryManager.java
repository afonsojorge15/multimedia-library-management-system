package m19.core;

import java.io.IOException;
import java.io.FileNotFoundException;

import m19.core.exception.MissingFileAssociationException;
import m19.core.exception.BadEntrySpecificationException;
import m19.core.exception.ImportFileException;
import m19.app.exception.UserRegistrationFailedException;
import m19.app.exception.NoSuchUserException;
import m19.app.exception.NoSuchWorkException;
import m19.app.exception.UserIsActiveException;
import m19.app.exception.WorkNotBorrowedByUserException;
import m19.app.exception.RuleFailedException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;

import java.io.File;


public class LibraryManager{

  private Library _library = new Library();             //creates new library

  private String _fileName = "";

  public User addUser(String name, String email) throws UserRegistrationFailedException{
    if(name.equals("") | email.equals("")){
      throw new UserRegistrationFailedException(name, email);
    }else{
      User a = new User(name, email);
      _library.addUserLib(a);
      return a;
    }
  }

  public String showUsers(){
    return _library.allUsers();
  }

  public String showUser(int id) throws NoSuchUserException{        //fazer igual em todo exception
    String r = "";
    try{
      r = _library.printUser(id);
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(id);
    }
    return r;
  }

  public String doNotifications(int id) throws NoSuchUserException{
    String r = "";
    try{
      r = _library.showNotifs(id);
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(id);
    }
    return r;
  }

  public String showWorks(){
    return _library.allWorks();
  }

  public String showWork(int id) throws NoSuchWorkException{
    String r = "";
    try{
      r = _library.printWork(id);
    }catch(NoSuchWorkException e){
      throw new NoSuchWorkException(id);
    }
    return r;
  }

  public String searchTerm(String str){                 //commented out searchTerm function, as it is only required for next delivery
    return _library.searchWord(str);
  }

  public void advDays(int days){
    _library.addDays(days);
  }

  public int showDate(){
    return _library.dispDate();
  }

  public int doRequest(int wk, int usr) throws NoSuchWorkException, NoSuchUserException{
    int r;
    try{
      r = _library.checkRules(wk, usr);
    }catch(NoSuchWorkException e){
      throw new NoSuchWorkException(wk);
    }catch(NoSuchUserException f){
      throw new NoSuchUserException(usr);
    }
    return r;
  }

  public void addOrder(int wk, int usr){
    _library.newOrder(wk, usr);
    return;
  }

  public void doReturn(int wk, int usr) throws WorkNotBorrowedByUserException{
    String r = "";
    try{
      _library.performReturn(wk, usr);
    }catch(WorkNotBorrowedByUserException e){
      throw new WorkNotBorrowedByUserException(wk, usr);
    }
    return;
  }

  public void fineUpdate(int usr, int fine){
      _library.upFine(usr, fine);
  }

  public void doPay(int usr) throws UserIsActiveException, NoSuchUserException{
    try{
      _library.performPay(usr);
    }catch(UserIsActiveException e){
      throw new UserIsActiveException(usr);
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(usr);
    }
    return;
  }

  public void doPayReturn(int usr){
      _library.performPayReturn(usr);
  }
  
  public int getPayValue(int usr, int work) throws WorkNotBorrowedByUserException, NoSuchWorkException, NoSuchUserException{
    int ret;
    try{
      ret = _library.getFineValue(usr, work);
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(usr);
    }catch(NoSuchWorkException e){
      throw new NoSuchWorkException(work);
    }catch(WorkNotBorrowedByUserException e){
      throw new WorkNotBorrowedByUserException(work, usr);
    }
    return ret;
  }

  /**
   * Serialize the persistent state of this application.
   * 
   * @throws MissingFileAssociationException if the name of the file to store the persistent
   *         state has not been set yet.
   * @throws IOException if some error happen during the serialization of the persistent state

   */
  public void save() throws MissingFileAssociationException, IOException {          
    if(_fileName.equals("")){
      throw new MissingFileAssociationException();
    }
    try(
      FileOutputStream out = new FileOutputStream(_fileName);
      ObjectOutputStream outobj = new ObjectOutputStream(out)){
      outobj.writeObject(_library);
    }
  }

  /**
   * Serialize the persistent state of this application into the specified file.
   * 
   * @param filename the name of the target file
   *
   * @throws MissingFileAssociationException if the name of the file to store the persistent
   *         is not a valid one.
   * @throws IOException if some error happen during the serialization of the persistent state
   */
  public void saveAs(String filename) throws MissingFileAssociationException, IOException {
    _fileName = filename;
    save();
  }

  /**
   * Recover the previously serialized persitent state of this application.
   * 
   * @param filename the name of the file containing the perssitente state to recover
   *
   * @throws IOException if there is a reading error while processing the file
   * @throws FileNotFoundException if the file does not exist
   * @throws ClassNotFoundException 
   */
  public void load(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {       
    FileInputStream inp = new FileInputStream(fileName);
    ObjectInputStream inpObj = new ObjectInputStream(new BufferedInputStream(inp));
    inp.close();
    _library = (Library) inpObj.readObject();
    inpObj.close();
    _fileName = fileName;
  }

  /**
   * Set the state of this application from a textual representation stored into a file.
   * 
   * @param datafile the filename of the file with the textual represntation of the state of this application.
   * @throws ImportFileException if it happens some error during the parsing of the textual representation.
   */
  public void importFile(String datafile) throws ImportFileException {           
    try {
      _library.importFile(datafile);
    } catch (IOException | BadEntrySpecificationException e) {
      throw new ImportFileException(e);
    }
  }
}
