package eu.wisebed.wiseui.server.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.apache.tools.ant.types.resources.selectors.Date;

public abstract class URLUtil {
	
	/** 
	 * Converts a URL (http://example.com:8989/path to http://0.0.0.0:8989/path 
	 */
	public static URL convertHostToZeros(URL url) throws MalformedURLException {
		return new URL(url.getProtocol(), "0.0.0.0", url.getPort(), url.getFile());
	}
	
	/** 
	 * Converts a URL (http://example.com:8989/path to http://0.0.0.0:8989/path
	 */
	public static String convertHostToZeros(String oldUrl) throws MalformedURLException {
		return convertHostToZeros(new URL(oldUrl)).toString();
	}

    private static Random random;

    public static int getRandomUnprivilegedPort() {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt(((int) (Math.pow(2, 16)) - 1) - 1024) + 1024;
    }
    
	private static final int PORT = 8089;
    

    public static String getRandomURLSuffix(final String key){
    	Date now = new Date();
    	Integer millis = new Integer((int)now.getMillis());
    	return (millis.toString() + key);
    }

	public static int getPort() {
		return PORT;
	}
}
