package m19.app.main;

import m19.core.LibraryManager;
import pt.tecnico.po.ui.Input;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import m19.app.exception.*;
import m19.core.exception.MissingFileAssociationException;

import pt.tecnico.po.ui.Command;

// FIXME import other core concepts
// FIXME import other ui concepts

/**
 * 4.1.1. Save to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<LibraryManager> {
  
  private Input<String> _fileName;

  /**
   * @param receiver
   */
  public DoSave(LibraryManager receiver) {
    super(Label.SAVE, receiver);
    _fileName = _form.addStringInput(Message.newSaveAs());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws FileOpenFailedException{
    try{
      _receiver.save();
    }catch(MissingFileAssociationException | IOException m){
      _form.parse();
      try{
        _receiver.saveAs(_fileName.value());
      }catch(MissingFileAssociationException | IOException e) {
        e.printStackTrace();
      }
    }
  }
}
