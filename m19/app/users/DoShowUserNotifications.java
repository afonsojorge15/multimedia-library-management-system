package m19.app.users;

import m19.core.LibraryManager;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import m19.app.exception.NoSuchUserException;
import pt.tecnico.po.ui.Input;
import m19.app.requests.Message;

/**
 * 4.2.3. Show notifications of a specific user.
 */
public class DoShowUserNotifications extends Command<LibraryManager> {

  public Input<Integer> _usr;

  /**
   * @param receiver
   */
  public DoShowUserNotifications(LibraryManager receiver) {
    super(Label.SHOW_USER_NOTIFICATIONS, receiver);
    _usr  = _form.addIntegerInput(Message.requestUserId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try{
      _display.addLine(_receiver.doNotifications(_usr.value()));
      _display.display();
    }
    catch(NoSuchUserException e){
      throw new NoSuchUserException(_usr.value());
    }
  }

}
