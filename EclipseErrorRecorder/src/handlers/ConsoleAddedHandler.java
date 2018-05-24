package handlers;

import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleListener;
import org.eclipse.ui.console.IOConsole;

public class ConsoleAddedHandler implements IConsoleListener {

	@Override
	public void consolesAdded(IConsole[] consoles) {
		IOConsole lastConsole = (IOConsole)consoles[consoles.length-1];
		lastConsole.addPropertyChangeListener(new ConsolePropertyChangeHandler());
	}

	@Override
	public void consolesRemoved(IConsole[] consoles) {
		// TODO Auto-generated method stub
		
	}

}
