package handlers;


import java.util.ArrayList;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

public class AnnotationModelChangedHandler implements IAnnotationModelListenerExtension, IAnnotationModelListener {

	ArrayList<ArrayList<Object>> checkedAnnotationInfo;
	
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
				System.out.println("Annotation Info:\t" + "Text: " + ann.getText());
				System.out.println("Annotation Position Info:\t" + "Length: " + annPos.getLength() + "\t" + "Offset: " + annPos.getOffset());
				System.out.println();
				checkedAnnotationInfo.add(annotationInfo);
			}
		}
	}

	@Override
	public void modelChanged(IAnnotationModel model) {
		
	}

}
