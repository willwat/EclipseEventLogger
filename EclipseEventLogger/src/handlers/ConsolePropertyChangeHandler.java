package handlers;

import java.sql.SQLException;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IOConsole;

import database.DBUtils;
import utilities.EclipseTools;
import utilities.GeneralUtils;

public class ConsolePropertyChangeHandler implements IPropertyChangeListener {
	
	private final int runtimeEventTypeID = 1;

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		IOConsole console = (IOConsole)event.getSource();
		IHyperlink[] links = console.getHyperlinks();
		
		String userMacAddress = "";
		String eventMessage = "";
		String classFileName = "";
		
		for(IHyperlink link : links) {
			if(link.getClass().getSimpleName().equals("JavaExceptionHyperLink")) {
				eventMessage = GeneralUtils.getHyperLinkTextFromConsole(link, console);
				userMacAddress = GeneralUtils.getUserMACAddress();
				classFileName = EclipseTools.getCurrentPageActiveEditor().getEditorInput().getName();

				
				try {
					//DBUtils.addRecordToDB(userMacAddress, eventMessage, runtimeEventTypeID);
					DBUtils.addRecordToDB(userMacAddress, eventMessage, runtimeEventTypeID, classFileName);
				} catch (SQLException e) {
					e.printStackTrace();
					return;
				}
			}
		}		
		
	}
}
