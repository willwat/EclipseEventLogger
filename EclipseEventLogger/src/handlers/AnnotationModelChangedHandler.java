package handlers;


import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

import database.DBUtils;
import utilities.EclipseTools;
import utilities.GeneralUtils;

public class AnnotationModelChangedHandler implements IAnnotationModelListenerExtension, IAnnotationModelListener {

	private ArrayList<ArrayList<Object>> checkedAnnotationInfo;
	private final int syntaxEventTypeID = 2;
	
	public AnnotationModelChangedHandler() {
		checkedAnnotationInfo = new ArrayList<ArrayList<Object>>();
	}
	
	@Override
	public void modelChanged(AnnotationModelEvent event) {
		Annotation[] addedAnnotations = event.getAddedAnnotations();
		IDocument doc = EclipseTools.getEditorSourceViewer(EclipseTools.getCurrentPageActiveEditor()).getDocument();
		
		for(Annotation ann : addedAnnotations) {
			Position annPos = event.getAnnotationModel().getPosition(ann);
			ArrayList<Object> annotationInfo = new ArrayList<Object>();
			annotationInfo.add(ann.getText());
			annotationInfo.add(annPos.getOffset());
			
			if(ann.getType().equals("org.eclipse.jdt.ui.error") && !checkedAnnotationInfo.contains(annotationInfo)) {
				try {
					int lineNumber = doc.getLineOfOffset(annPos.getOffset()) + 1;
					IRegion lineInfo = doc.getLineInformation(lineNumber - 1);
					String classFileName = EclipseTools.getCurrentPageActiveEditor().getEditorInput().getName();
					String problemCode = doc.get(lineInfo.getOffset(), lineInfo.getLength()).trim();
					
					DBUtils.addRecordToDB(GeneralUtils.getUserMACAddress(), ann.getText(), syntaxEventTypeID, classFileName, problemCode, lineNumber);
					checkedAnnotationInfo.add(annotationInfo);					
				} 
				catch (SQLException | BadLocationException e) {
					e.printStackTrace();
					return;
				}
			}
		}		
	}

	// Required method implementation that is not used.
	@Override
	public void modelChanged(IAnnotationModel model) {
		
	}

}
