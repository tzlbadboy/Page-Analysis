package Config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Config {

	private static PropertiesConfiguration config = null;
	static {
			try {
				config = new PropertiesConfiguration("./conf/config.properties");
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
	}

	public static String getValue(String key) {
		return config.getString(key,"unknown");
	}
    	
	public static void setValue(String key, Object value) {
		config.setProperty(key, value);
	}
    
	public static void save(){
		try {
			config.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] agrs) {
		//Config.setValue("lastIndexTime","6,7,8,9,10,11,12,13,14,15,16");
		System.err.println(Config.getValue("lastIndexTime"));
	}

}