/**
 * 
 */
package main;


import org.eclipse.ui.IStartup;

import utilities.GlobalVariables;

/**
 * @author William
 *
 */
public class Main implements IStartup {

	@Override
	public void earlyStartup() {
		System.out.println(GlobalVariables.getUserMACAddress());
		System.out.println(GlobalVariables.getConsoleManager());
		System.out.println(GlobalVariables.getWorkbench());
	}

}
