package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.TextConsole;

public class GeneralUtils {
	
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
	
	public static void writeToFile(String filePath, String text) {
		FileWriter fileWriter = null;
		PrintWriter printWriter = null;
		
		try {
			fileWriter = new FileWriter(filePath);
			printWriter = new PrintWriter(fileWriter);
		    printWriter.print(text);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    printWriter.close();
		}
	    
	}
	
	public static String getHyperLinkTextFromConsole(IHyperlink link, TextConsole console) {
		IRegion linkRegion = console.getRegion(link);
		try {
			return console.getDocument().get(linkRegion.getOffset(), linkRegion.getLength());
		} catch (BadLocationException e) {
			return "Hyperlink not found in given console.";
		}
	}
	
}
