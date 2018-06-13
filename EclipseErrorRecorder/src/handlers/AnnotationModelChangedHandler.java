package handlers;


import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

import database.DBUtils;
import utilities.GeneralUtils;

public class AnnotationModelChangedHandler implements IAnnotationModelListenerExtension, IAnnotationModelListener {

	ArrayList<ArrayList<Object>> checkedAnnotationInfo;
	private final int syntaxErrorTypeID = 2;
	
	public AnnotationModelChangedHandler() {
		checkedAnnotationInfo = new ArrayList<ArrayList<Object>>();
	}
	
	@Override
	public void modelChanged(AnnotationModelEvent event) {
		Annotation[] addedAnnotations = event.getAddedAnnotations();
		
		for(Annotation ann : addedAnnotations) {
			Position annPos = event.getAnnotationModel().getPosition(ann);
			ArrayList<Object> annotationInfo = new ArrayList<Object>();
			annotationInfo.add(ann.getText());
			annotationInfo.add(annPos.getOffset());
			
			if(ann.getType().equals("org.eclipse.jdt.ui.error") && !checkedAnnotationInfo.contains(annotationInfo)) {
				try {
					DBUtils.addRecordToDB(GeneralUtils.getUserMACAddress(), ann.getText(), syntaxErrorTypeID);
				} catch (SQLException e) {
					return;
				}
			}
		}
	}

	@Override
	public void modelChanged(IAnnotationModel model) {
		
	}

}
