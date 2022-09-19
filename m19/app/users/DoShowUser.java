package m19.app.users;

import m19.core.LibraryManager;
import m19.app.exception.NoSuchUserException;
import m19.core.User;
import m19.app.users.Message;
import pt.tecnico.po.ui.*;

/**
 * 4.2.2. Show specific user.
 */
public class DoShowUser extends Command<LibraryManager> {

  private Input<Integer> _id;

  /**
   * @param receiver
   */
  public DoShowUser(LibraryManager receiver) {
    super(Label.SHOW_USER, receiver);
    _id  = _form.addIntegerInput(Message.requestUserId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try{
      _display.addLine(_receiver.showUser(_id.value()));
      _display.display();
    }
    catch(NoSuchUserException e){
      throw new NoSuchUserException(_id.value());
    }
  }


}
