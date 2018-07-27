package handlers;

import java.sql.SQLException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IOConsole;

import com.mysql.cj.util.StringUtils;

import database.DBUtils;
import utilities.EclipseTools;
import utilities.GeneralUtils;

public class ConsolePropertyChangeHandler implements IPropertyChangeListener {
	
	private final int runtimeEventTypeID = 1;

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		IOConsole console = (IOConsole)event.getSource();
		IHyperlink[] links = console.getHyperlinks();
		IDocument doc = EclipseTools.getEditorSourceViewer(EclipseTools.getCurrentPageActiveEditor()).getDocument();

		String userMacAddress = "";
		String eventMessage = "";
		String classFileName = "";
		String problemCode = "";
		int lineNumber = 0;
		
			for(IHyperlink link : links) {
				String linkText = GeneralUtils.getHyperLinkTextFromConsole(link, console);
				try {
					if(link.getClass().getSimpleName().equals("JavaExceptionHyperLink")) {
						eventMessage = linkText;
						userMacAddress = GeneralUtils.getUserMACAddress();
						classFileName = EclipseTools.getCurrentPageActiveEditor().getEditorInput().getName();
						continue;
					} 
					else if(link.getClass().getSimpleName().equals("JavaStackTraceHyperlink") && linkText.contains(classFileName)) {
						String[] splitLinkText = GeneralUtils.getHyperLinkTextFromConsole(link, console).split(":");
						if(StringUtils.isStrictlyNumeric(splitLinkText[splitLinkText.length - 1])) {
							lineNumber = Integer.parseInt(splitLinkText[splitLinkText.length - 1]);
							IRegion lineInfo = doc.getLineInformation(lineNumber-1);
							problemCode = doc.get(lineInfo.getOffset(), lineInfo.getLength()).trim();
						}
						else {
							lineNumber = 0;
							problemCode = "";
						}
					}
					else {
						continue;
					}
					
					//DBUtils.addRecordToDB(userMacAddress, eventMessage, runtimeEventTypeID);
					DBUtils.addRecordToDB(userMacAddress, eventMessage, runtimeEventTypeID, classFileName, problemCode, lineNumber);
				} 
				catch (SQLException e) {
					e.printStackTrace();
					return;
				} catch (BadLocationException e) {
					continue;
				}
			
		}	
		
	}
}
