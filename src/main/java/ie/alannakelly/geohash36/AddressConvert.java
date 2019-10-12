package ie.alannakelly.geohash36;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

public class AddressConvert{
	public String address;
	
	public static ArrayList<String> convertAddress(String address) throws Exception{
		address = address.replaceAll(" ", "+");
		Properties prop = new Properties();
		ArrayList<String> list = new ArrayList<String>();
		
		try(InputStream input = new FileInputStream("properties.config")){
			prop.load(input);
		
			URL google = new URL(prop.getProperty("url") + address + prop.getProperty("apikey"));
			BufferedReader in = new BufferedReader(new InputStreamReader(google.openStream()));
			String inputLine;
			
			while((inputLine = in.readLine()) != null){
				if(inputLine.contains("lat") || inputLine.contains("lng")){
					inputLine = inputLine.replace(inputLine.substring(0, inputLine.indexOf("l")), "");
					inputLine = inputLine.replace(inputLine.substring(0, 7), "");
			            	
					if(inputLine.contains(",")){
						inputLine = inputLine.replace(",", "");
					}
					list.add(inputLine);
				}
			}
			in.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return list;
	}
}