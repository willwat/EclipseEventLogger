package handlers;

import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

public class AnnotationModelChangedHandler implements IAnnotationModelListenerExtension, IAnnotationModelListener {

	@Override
	public void modelChanged(AnnotationModelEvent event) {
		Annotation[] addedAnnotations = event.getAddedAnnotations();

		for(Annotation ann : addedAnnotations) {
			if(ann.getType().equals("org.eclipse.jdt.ui.error") && !ann.getText().contains("chkd")) {
				System.out.println(ann.getText() + " Type: " + ann.getType());
				ann.setText(ann.getText() + "chkd");
			}
		}
		System.out.println();		
	}

	@Override
	public void modelChanged(IAnnotationModel model) {
		// TODO Auto-generated method stub
		
	}

}
