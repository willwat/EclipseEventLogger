package utilities;

import handlers.AnnotationModelChangedHandler;
import handlers.ConsoleAddedHandler;
import handlers.PagePartActivatedHandler;

public class ProjectSetup {
	
	public static void setupListeningForSyntaxEvents() {
		if(EclipseTools.getCurrentPageActiveEditor() != null) {
			EclipseTools.getEditorSourceViewer(EclipseTools.getCurrentPageActiveEditor()).getAnnotationModel().addAnnotationModelListener(new AnnotationModelChangedHandler());
		}
		
		EclipseTools.getWorkbench().getWorkbenchWindows()[0].getPages()[0].addPartListener(new PagePartActivatedHandler());
	}
	
	public static void setupListeningForRuntimeEvents() {
		EclipseTools.getConsoleManager().addConsoleListener(new ConsoleAddedHandler());
	}
	
}
