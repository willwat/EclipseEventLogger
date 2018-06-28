package utilities;

import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.texteditor.ITextEditor;

public class EclipseTools {
	
	public static IConsoleManager getConsoleManager() {
		return ConsolePlugin.getDefault().getConsoleManager();
	}
	
	public static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}
	
	public static IWorkbenchPage getCurrentWorkbenchPage() {
		return getWorkbench().getWorkbenchWindows()[0].getPages()[0];
	}
	
	public static ITextEditor getCurrentPageActiveEditor() {
		return (ITextEditor) getCurrentWorkbenchPage().getActiveEditor();
	}
	
	public static ISourceViewer getEditorSourceViewer(ITextEditor editor) {
		return (ISourceViewer)editor.getAdapter(ITextOperationTarget.class);
	}
	
}
