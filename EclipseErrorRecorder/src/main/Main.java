/**
 * 
 */
package main;


import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IStartup;

import handlers.AnnotationModelChangedHandler;
import handlers.PagePartActivatedHandler;
import utilities.EclipseTools;

/**
 * @author William
 *
 */
public class Main implements IStartup {

	@Override
	public void earlyStartup() {
		if(EclipseTools.getCurrentPageActiveEditor() != null) {
			ISourceViewer sourceView = (ISourceViewer)EclipseTools.getCurrentPageActiveEditor().getAdapter(ITextOperationTarget.class);
			sourceView.getAnnotationModel().addAnnotationModelListener((IAnnotationModelListener) new AnnotationModelChangedHandler());
		}
		
		EclipseTools.getWorkbench().getWorkbenchWindows()[0].getPages()[0].addPartListener(new PagePartActivatedHandler());
	}

}
