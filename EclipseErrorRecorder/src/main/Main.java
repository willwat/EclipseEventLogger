/**
 * 
 */
package main;

import org.eclipse.ui.IStartup;

import utilities.ProjectSetup;


/**
 * @author William
 *
 */
public class Main implements IStartup {

	@Override
	public void earlyStartup() {
		ProjectSetup.setupListeningForSyntaxErrors();
		ProjectSetup.setupListeningForRuntimeErrors();
	}

}
