package m19.app.works;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;
import m19.app.exception.NoSuchWorkException;
import pt.tecnico.po.ui.Input;



import m19.core.LibraryManager;
// FIXME import other core concepts
// FIXME import ui concepts

/**
 * 4.3.1. Display work.
 */
public class DoDisplayWork extends Command<LibraryManager> {

  private Input<Integer> _id;

  /**
   * @param receiver
   */
  public DoDisplayWork(LibraryManager receiver) {
    super(Label.SHOW_WORK, receiver);
    _id  = _form.addIntegerInput(Message.requestWorkId());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try{
      _display.addLine(_receiver.showWork(_id.value()));
      _display.display();
    }
    catch(NoSuchWorkException e){
      throw new NoSuchWorkException(_id.value());
    }
  }

}