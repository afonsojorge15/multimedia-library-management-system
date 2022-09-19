package m19.app.requests;

import m19.core.LibraryManager;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Form;
import m19.app.requests.Message;
import m19.app.exception.WorkNotBorrowedByUserException;
import m19.app.exception.NoSuchUserException;
import m19.app.exception.NoSuchWorkException;

/**
 * 4.4.2. Return a work.
 */
public class DoReturnWork extends Command<LibraryManager> {

  public Input<Integer> _usr;
  public Input<Integer> _work;
  public Input<String> _str;

  /**
   * @param receiver
   */
  public DoReturnWork(LibraryManager receiver) {
    super(Label.RETURN_WORK, receiver);
    _usr  = _form.addIntegerInput(Message.requestUserId());
    _work  = _form.addIntegerInput(Message.requestWorkId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    Form newForm = new Form();
    _form.parse();
    int pay;
    try{
      pay = _receiver.getPayValue(_usr.value(), _work.value());
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(_usr.value());
    }catch(NoSuchWorkException e){
      throw new NoSuchWorkException(_work.value());
    }catch(WorkNotBorrowedByUserException e){
      throw new WorkNotBorrowedByUserException(_work.value(), _usr.value());
    }
    if(pay>0){
      _display.addLine("O utente "+_usr.value()+" deve pagar uma multa de EUR "+pay+".");
      _display.display();
      _str = newForm.addStringInput(Message.requestFinePaymentChoice());
      newForm.parse();
      if(_str.value().toLowerCase().equals("s")){
        _receiver.doPayReturn(_usr.value());
      }else{
        _receiver.fineUpdate(_usr.value(), pay);
      }
    }
      _receiver.doReturn(_work.value(), _usr.value());
  }
}
