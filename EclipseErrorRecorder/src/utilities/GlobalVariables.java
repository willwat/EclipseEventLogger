package utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsoleManager;

public class GlobalVariables {
		
	public static String getUserMACAddress(){
		
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			byte[] mac = network.getHardwareAddress();
			
			StringBuilder macAddress = new StringBuilder();
			String macFormatStr = "%02X%s";
			
			for (int i = 0; i < mac.length; i++) {
				boolean ifNotOnLastItem = i < mac.length - 1;
				macAddress.append(String.format(macFormatStr, mac[i], (ifNotOnLastItem) ? "-" : ""));		
			}
			
			return macAddress.toString();
		}
		catch(Exception e) {
			return "No MAC address found";
		}
	}
	
	public static IConsoleManager getConsoleManager() {
		return ConsolePlugin.getDefault().getConsoleManager();
	}
	
	public static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}
}
