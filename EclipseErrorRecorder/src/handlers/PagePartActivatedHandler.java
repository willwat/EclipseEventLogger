package handlers;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

public class PagePartActivatedHandler implements IPartListener {

	@Override
	public void partActivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub
		if(part instanceof ITextEditor) {
			ITextEditor editor = (ITextEditor)part;
			ISourceViewer sourceView = (ISourceViewer)((ITextEditor)editor).getAdapter(ITextOperationTarget.class);
			sourceView.getAnnotationModel().addAnnotationModelListener((IAnnotationModelListener) new AnnotationModelChangedHandler());
			
		}
	}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partOpened(IWorkbenchPart part) {
		// TODO Auto-generated method stub

	}

}
