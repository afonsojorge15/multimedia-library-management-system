package m19.app.main;

import m19.core.LibraryManager;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import m19.app.exception.FileOpenFailedException;
import pt.tecnico.po.ui.Input;



// FIXME import other core concepts
// FIXME import other ui concepts

/**
 * 4.1.1. Open existing document.
 */
public class DoOpen extends Command<LibraryManager> {

  private Input<String> _fileName;

  /**
   * @param receiver
   */
  public DoOpen(LibraryManager receiver) {
    super(Label.OPEN, receiver);
    _fileName = _form.addStringInput(Message.openFile());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    _form.parse();
    try {
      _receiver.load(_fileName.value());
    } catch (FileNotFoundException fnfe) {
      throw new FileOpenFailedException(_fileName.value());
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
  }

}
