package utilities;

import java.net.InetAddress;
import java.net.NetworkInterface;

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
	
}
