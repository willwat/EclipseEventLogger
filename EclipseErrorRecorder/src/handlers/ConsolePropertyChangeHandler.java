package handlers;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IOConsole;

import utilities.GeneralUtils;

public class ConsolePropertyChangeHandler implements IPropertyChangeListener {

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		IOConsole console = (IOConsole)event.getSource();
		
		IHyperlink[] links = console.getHyperlinks();
		String errorInfo = "";
		
		for(IHyperlink link : links) {
			if(link.getClass().getSimpleName().equals("JavaExceptionHyperLink")) {
				errorInfo += GeneralUtils.getHyperLinkTextFromConsole(link, console) + "\n";
			}
		}
		
		System.out.println(errorInfo);
	}

}
