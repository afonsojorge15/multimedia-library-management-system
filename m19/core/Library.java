package m19.core;

import java.io.Serializable;
import java.io.IOException;

import m19.core.exception.MissingFileAssociationException;
import m19.core.exception.BadEntrySpecificationException;
import m19.app.exception.WorkNotBorrowedByUserException;
import m19.app.exception.RuleFailedException;
import m19.app.exception.UserIsActiveException;
import m19.app.exception.NoSuchUserException;
import m19.app.exception.NoSuchWorkException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import m19.core.Parser;


/**
 * Class that represents the library as a whole.
 */
public class Library implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 201901101348L;

  ArrayList<Rule> _listRules = new ArrayList<Rule>();

  ArrayList<Order> _listOrders = new ArrayList<Order>();

  private Map<Integer, User> _userMap = new HashMap<Integer, User>();
  private Map<Integer, Work> _workMap = new HashMap<Integer, Work>();

  private RuleFactory _factory = new RuleFactory();     //instantiates RuleFactory object

  private Integer _idUser;
  private Integer _idWork;

  Date date = new Date();       //instantiates date 

  public Library(){
    _idWork = 0;                                  //keeps trace of work and user id's to use in next instance
    _idUser = 0;
    for(int i = 1; i<7; i++){                     //creates an object corresponding to each rule, using the RuleFactory's createRule function. Different rule is going to be created depending on "i" (rule id)          
      Rule a = _factory.createRule(i);
      _listRules.add(a);
    }
  }

  /**
   * Checks request rules. In case of failure, returns the failed rule's id. In case of success returns the number corresponding to the dealine (negative number, will be parsed into positive in doRequestWork())
   * 
   * @param userId id of the user who is trying a new request
   * @param workId id of the work that user wants to request
   *
   * @throws NoSuchWorkException if work id is not registred or if it is invalid (<0)
   * @throws NoSuchUserException if user id is not registred or if it is invalid (<0)
   */
  protected int checkRules(int workId, int userId) throws NoSuchWorkException, NoSuchUserException{
    if(workId>_workMap.size()-1 | workId<0){
      throw new NoSuchWorkException(workId);
    }
    if(userId>_userMap.size()-1 | userId<0){                                           
      throw new NoSuchUserException(userId);
    }
    User user = _userMap.get(userId);
    Work work = _workMap.get(workId);
    for(int i = 0; i<6; i++){
      if(_listRules.get(i).checkRule(work, user) == false){
        return i+1;
      }
    }
    int a = performRequest(workId, userId);
    return -a;  
  }

  /**
   * Creates a new order for notifications after request failed rule 3 and stores it in the order list
   * 
   * @param user id of the user who tried the request
   * @param work id of the work that user wanted to request
   *
   */
  protected void newOrder(int work, int user){
    for(Order ord: _listOrders){
      if((ord.getUserId()==user) && (ord.getWorkId()==work)){
        return;
      }
    }
    Order a = new Order(work, user);
    _listOrders.add(a);
  }

  /**
   * Checks if recent action (request/return) implies adding a new notification to any user. If so, a new notification is going to be added to the user's list
   * 
   * @param request_return marks if action is a request or return. request->0, return->1
   * @param work work that has been requested/returned
   *
   */
  protected void checkNewNotification(int work, boolean request_return){
    for(Order ord: _listOrders){
      if(ord.getWorkId()==work){
        _userMap.get(ord.getUserId()).addNotif(_workMap.get(work), request_return);
      }
    }
  }

  /**
   * Performs a request after the rules have been checked
   * 
   * @param usr id of the user that wants to file a request
   * @param wk id of the work the user wants to request
   *
   */
  protected int performRequest(int wk, int usr){
    int deadline;
    User user = _userMap.get(usr);
    Work work = _workMap.get(wk);
    if(user.getBehave()==Behave.FALTOSO){
      deadline = date.getCurrentDate()+2;
    }else if((user.getBehave()==Behave.NORMAL & work.getNumberOfCopies()<6 & work.getNumberOfCopies()>1) | (user.getBehave()==Behave.CUMPRIDOR & work.getNumberOfCopies()==1)){
      deadline = date.getCurrentDate()+8;
    }else if((user.getBehave()==Behave.CUMPRIDOR & work.getNumberOfCopies()<6 & work.getNumberOfCopies()>1) | (user.getBehave()==Behave.NORMAL & work.getNumberOfCopies()>5)){
      deadline = date.getCurrentDate()+15;
    }else if(user.getBehave()==Behave.NORMAL & work.getNumberOfCopies()==1){
      deadline = date.getCurrentDate()+3;
    }else{
      deadline = date.getCurrentDate()+30;
    }
    Request nova = new Request(deadline, wk);
    _userMap.get(usr).addReq(nova);
    work.changeAvailable(-1);
    checkNewNotification(wk, false);
    return deadline;
  }

  /**
   * Returns the work borrowed by user. Throws exception if request wasnt made
   * 
   * @param usr id of the user that returns the work
   * @param wk id of the work that is going to be returned by user
   *
   * @throws WorkNotBorrowedByUserException if the specified work hasnt been borrowed by specified user
   */
  protected void performReturn(int wk, int usr) throws WorkNotBorrowedByUserException{
    ArrayList<Request> a = _userMap.get(usr).getReqList();
    for(int i = 0; i<a.size(); i++){
      if(a.get(i).getWorkId() == wk){
        if(a.get(i).getDeadline()>=date.getCurrentDate()){
          _userMap.get(usr).updateStreak(true);
        }else{
          _userMap.get(usr).updateStreak(false);
          if(_userMap.get(usr).getBehave()==Behave.CUMPRIDOR){
            _userMap.get(usr).setNormal();
          }
        }
        _userMap.get(usr).delReq(i);
        Work work = _workMap.get(wk);
        work.changeAvailable(1);
        checkSuspended(_userMap.get(usr));
        checkNewNotification(wk, true);
        return;
      }
    }
    throw new WorkNotBorrowedByUserException(wk, usr);
  }

  /**
   * Checks if the user needs to be suspended and does so if needed
   * 
   * @param user user on which the verification is going to be made
   *
   */
  protected void checkSuspended(User user){
    if(user.getFine()==0){
      for(Request req: user.getReqList()){
        if(req.getDeadline()<date.getCurrentDate()){
          user.setActive(false);
          return;
        }
      }
      user.setActive(true);
      return;
    }else{
      user.setActive(false);
      return;
    }
  }

  /**
   * Sets the user fine if he/she refused to pay upon return
   * 
   * @param usr id of the user whose fine is going to be set
   * @param fine value of the fine to be atributed
   *
   */
  protected void upFine(int usr, int fine){
    _userMap.get(usr).setFine(fine);
  }

  /**
   * pays fine related to specific user
   * 
   * @param usr id of the user whose fine is going to be payed
   *
   * @throws UserIsActiveException if user is active, there's no fine to pay
   * @throws NoSuchUserException if user id is not registred or if it is invalid (<0)
   */
  protected void performPay(int usr) throws UserIsActiveException, NoSuchUserException{
    if(usr>_userMap.size()-1 | usr<0){                                           
      throw new NoSuchUserException(usr);
    }
    if(_userMap.get(usr).isActive() == false){
      _userMap.get(usr).payFine();
      checkSuspended(_userMap.get(usr));
    }else{
      throw new UserIsActiveException(usr);
    }
    return;
  }

  /**
   * Pays the user fine upon return of overdue request
   * 
   * @param usr user whose fine is going to be payed
   *
   */
  protected void performPayReturn(int usr){
      _userMap.get(usr).payFine();
  }

  /**
   * Checks if the user request for the work is overdue, in which case it returns the fine to be payed. Otherwise returns 0
   * 
   * @param usr id of the user whose request might be overdue
   * @param work id of the work whose request might be overdue
   *
   * @throws WorkNotBorrowedByUserException if the specified work hasnt been borrowed by specified user
   * @throws NoSuchWorkException if work id is not registred or if it is invalid (<0)
   * @throws NoSuchUserException if user id is not registred or if it is invalid (<0)
   */
  protected int getFineValue(int usr, int work) throws WorkNotBorrowedByUserException, NoSuchWorkException, NoSuchUserException{
    if(usr>_userMap.size()-1 | usr<0){                                           
      throw new NoSuchUserException(usr);
    }
    if(work>_workMap.size()-1 | work<0){                                           
      throw new NoSuchWorkException(work);
    }
    ArrayList<Request> a = _userMap.get(usr).getReqList();
    for(int i = 0; i<a.size(); i++){
      if(a.get(i).getWorkId() == work){
        if(a.get(i).getDeadline()<date.getCurrentDate()){
          return (date.getCurrentDate()-a.get(i).getDeadline())*5+_userMap.get(usr).getFine();
        }else{
          return 0;
        }
      }
    }
    throw new WorkNotBorrowedByUserException(work, usr);
  }

  /**
   * returns notifications of the specified user
   * 
   * @param usr id of the user whose notifications are going to be returned
   *
   * @throws NoSuchUserException if user id is not registred or if it is invalid (<0)
   */
  protected String showNotifs(int usr) throws NoSuchUserException{
    if(usr>_userMap.size()-1 | usr<0){                                           
      throw new NoSuchUserException(usr);
    }
    User user = _userMap.get(usr);
    return user.printNotifs();
  }

  /**
   * Sets user id and adds it to user list.
   * 
   * @param utilizador the User named utilizador.
   */
  protected void addUserLib(User utilizador){         
    utilizador.setId(_idUser);
    _userMap.put(_idUser, utilizador);  
    _idUser++;
  }
  
   /**
   * Sets work id and adds it to work list.
   * 
   * @param trabalho the Work named trabalho.
   */
  protected void addWorkLib(Work trabalho){           
    trabalho.setId(_idWork);
    _workMap.put(_idWork, trabalho);  
    _idWork++;
  }

  /**
   * Iterates through user list and returns a string with all user descriptions.
   */
  protected String allUsers(){                        
    String all = "";
    List<User> a = new ArrayList<User>(_userMap.values());
    Collections.sort(a);
    for(User b : a){
      all += b.getDescription()+"\n";
    }
    return all;
  }

  /**
   * Looks in user list for user with this id and prints its description
   * 
   * @param id an int thats the id for the user list.
   * @throws NoSuchUserException throws exception if no user has this id.
   */
  protected String printUser(int id) throws NoSuchUserException{        
    if(id>_userMap.size()-1 | id<0){                                           
      throw new NoSuchUserException(id);
    }
    String si = "";
    for(int i=0; i<_userMap.size(); i++){
      if(_userMap.get(i).getId() == id){
        si = _userMap.get(i).getDescription();
        break;
      }
    }
    return si;
  }

  /**
  * Iterates through work list and returns a string with all work descriptions
  */
  String allWorks(){                                  
    String all = "";
    for(int i=0; i<_workMap.size(); i++){
      all += _workMap.get(i).printDescription()+"\n";
    }  
    return all;
  }

  /**
   * Looks in work list for work with this id and prints its description
   * 
   * @param id an int thats the id for the work list.
   * @throws NoSuchUserException throws exception if no work has this id
   */
  String printWork(int id) throws NoSuchWorkException{                 
    String si = "";                                                    
    if(id>_workMap.size()-1 | id<0){
      throw new NoSuchWorkException(id);
    }
    for(int i=0; i<_workMap.size(); i++){
      if(_workMap.get(i).getId() == id){
        si = _workMap.get(i).printDescription();
        break;
      }
    }
    return si;
  }

  /**
   * Searches for keyword in work descriptions. Returns a string containing the descriptions of the matching works
   * 
   * @param str keyword to search in work titles and authors/directors
   *
   */
  protected String searchWord(String str){                          
    String writer, title, strLower, ret;
    ret = "";
    strLower = str.toLowerCase();
    for(int i=0; i<_workMap.size(); i++){
      writer = _workMap.get(i).getWriter().toLowerCase();
      title = _workMap.get(i).getTitle().toLowerCase();
      if(writer.contains(strLower) || title.contains(strLower)){
        ret += _workMap.get(i).printDescription() +"\n";
      }
    } 
    return ret; 
  }

  /**
   * Checks if any users need suspending based on overdue requests
   * 
   * @param days days advanced by client
   *
   */
  protected void updateSuspendedDaily(int days){
    int curr = date.getCurrentDate();
    int a;
    boolean suspend = false;
    for(User user: _userMap.values()){
      for(Request req: user.getReqList()){
        if(req.getDeadline()<curr){
          user.setActive(false);
        }
      }
    }
    return;
  }

  /**
  * Adds "days" amount of days to date and run function to check if any users need suspending
  * 
  * @param days an int thats the amount of days to to add.
  */
  protected void addDays(int days){  
    int oldDay = date.getCurrentDate();                              
    date.advanceDays(days);
    updateSuspendedDaily(days);
  }

  /**
  * Displays current date
  */
  protected int dispDate(){                                           
    return date.getCurrentDate();
  }

  /**
   * Read the text input file at the beginning of the program and populates the
   * instances of the various possible types (books, DVDs, users).
   * 
   * @param filename
   *          name of the file to load
   * @throws BadEntrySpecificationException
   * @throws IOException
   */
  void importFile(String filename) throws BadEntrySpecificationException, IOException{      //imports import file specified in command line
    Parser p = new Parser(this);
    p.parseFile(filename);
  }

}
