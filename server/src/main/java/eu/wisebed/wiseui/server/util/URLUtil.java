/**
 * Copyright (C) 2011 Universität zu Lübeck, Institut für Telematik (ITM),
 *                             Research Academic Computer Technology Institute (RACTI)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.wisebed.wiseui.server.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

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
    	Integer millis = (int) now.getTime();
    	return (millis.toString() + key);
    }

	public static int getPort() {
		return PORT;
	}
}
