package m19.app.users;

import m19.core.LibraryManager;
import m19.app.exception.UserIsActiveException;
import m19.app.exception.NoSuchUserException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import m19.app.users.Message;

/**
 * 4.2.5. Settle a fine.
 */
public class DoPayFine extends Command<LibraryManager> {

  public Input<Integer> _usr;

  /**
   * @param receiver
   */
  public DoPayFine(LibraryManager receiver) {
    super(Label.PAY_FINE, receiver);
    _usr  = _form.addIntegerInput(Message.requestUserId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try{
      _receiver.doPay(_usr.value());
    }
    catch(NoSuchUserException e){
      throw new NoSuchUserException(_usr.value());
    }catch(UserIsActiveException e){
      throw new UserIsActiveException(_usr.value());
    } 
  }
}
