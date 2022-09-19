package m19.app.requests;

import m19.core.LibraryManager;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import pt.tecnico.po.ui.Form;
import m19.app.requests.Message;
import m19.app.exception.RuleFailedException;
import m19.app.exception.NoSuchWorkException;
import m19.app.exception.NoSuchUserException;


/**
 * 4.4.1. Request work.
 */
public class DoRequestWork extends Command<LibraryManager> {

  public Input<Integer> _usr;
  public Input<Integer> _work;
  public Input<String> _str;

  /**
   * @param receiver
   */
  public DoRequestWork(LibraryManager receiver) {
    super(Label.REQUEST_WORK, receiver);
    _usr  = _form.addIntegerInput(Message.requestUserId());
    _work  = _form.addIntegerInput(Message.requestWorkId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    int rule;
    Form newForm = new Form();
    _form.parse();
    try{
      rule = _receiver.doRequest(_work.value(), _usr.value());
    }catch(NoSuchWorkException e){
      throw new NoSuchWorkException(_work.value());
    }catch(NoSuchUserException e){
      throw new NoSuchUserException(_usr.value());
    }
    if(rule!=3 && rule>0){
      throw new RuleFailedException(_usr.value(), _work.value(), rule);
    }else if(rule == 3){
      _str = newForm.addStringInput(Message.requestReturnNotificationPreference());
      newForm.parse();
      if(_str.value().toLowerCase().equals("s")){
        _receiver.addOrder(_work.value(), _usr.value());
      }
    }else if(rule<=0){
      _display.addLine("A obra "+ _work.value() + " deve ser devolvida no dia " +(-rule)+".");
      _display.display();
    }
  }
}
