package eu.wisebed.wiseui.server.util;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.Random;

public abstract class URLUtil {
	/** Converts a URL (http://example.com:8989/path to http://0.0.0.0:8989/path
	 *  
	 */
	public static URL convertHostToZeros(URL url) throws MalformedURLException {
		return new URL(url.getProtocol(), "0.0.0.0", url.getPort(), url.getFile());
	}

	/** Converts a URL (http://example.com:8989/path to http://0.0.0.0:8989/path
	 *
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
    
    private static int port;
	private static final int MIN_PORT_NUMBER = 8089;
	private static final int MAX_PORT_NUMBER = 9000;
    
	/**
	 * Return the next available port for an instance controller to bind on.
	 *
	 * @return port the next available port.
	 */
	public static int getNextAvailablePort(){
		if(port < 0) port = 8089;
		while(!portAvailability(port)){
			port++;
		}
		return port;
	}
    
	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port the port to check for availability
	 */
	private static boolean portAvailability(int port) 
		throws IllegalArgumentException  {
	    if (port < MIN_PORT_NUMBER || port > MAX_PORT_NUMBER){
	    	port = MIN_PORT_NUMBER; 
	    }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
}
