package m19.app.works;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import java.io.IOException;
import java.io.FileNotFoundException;
import m19.app.exception.FileOpenFailedException;
import m19.app.works.Message;
import pt.tecnico.po.ui.Input;


import m19.core.LibraryManager;


/**
 * 4.3.3. Perform search according to miscellaneous criteria.
 */
public class DoPerformSearch extends Command<LibraryManager> {

  private Input<String> _str;

  /**
   * @param m
   */
  public DoPerformSearch(LibraryManager m) {
    super(Label.PERFORM_SEARCH, m);
    _str  = _form.addStringInput(Message.requestSearchTerm());
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() {
    _form.parse();
    _display.addLine(_receiver.searchTerm(_str.value()));
    _display.display();
  }
  
}
