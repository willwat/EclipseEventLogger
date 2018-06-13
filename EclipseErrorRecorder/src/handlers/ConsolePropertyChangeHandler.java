package handlers;

import java.sql.SQLException;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IOConsole;

import database.DBUtils;
import utilities.GeneralUtils;

public class ConsolePropertyChangeHandler implements IPropertyChangeListener {
	
	private final int runtimeErrorTypeID = 1;

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		IOConsole console = (IOConsole)event.getSource();
		IHyperlink[] links = console.getHyperlinks();
		
		String userMacAddress = "";
		String errorMessage = "";
		
		for(IHyperlink link : links) {
			if(link.getClass().getSimpleName().equals("JavaExceptionHyperLink")) {
				errorMessage = GeneralUtils.getHyperLinkTextFromConsole(link, console);
				userMacAddress = GeneralUtils.getUserMACAddress();
				
				try {
					DBUtils.addRecordToDB(userMacAddress, errorMessage, runtimeErrorTypeID);
				} catch (SQLException e) {
					return;
				}
			}
		}		
		
	}
}
