package com.toep;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Utils {
	
	/* Returns a instance of InputStream for the resource */
	public static InputStream getResourceAsStream(String resource) 
	                                                   throws FileNotFoundException {
	    String stripped = resource.startsWith("/")?resource.substring(1):resource;
	    InputStream stream = null;
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    if (classLoader != null) {
	        stream = classLoader.getResourceAsStream(stripped);
	    }
	    if (stream == null) {
	        stream = Utils.class.getResourceAsStream(resource);
	    }
	    if (stream == null) {
	        stream = Utils.class.getClassLoader().getResourceAsStream(stripped);
	    }
	    if (stream == null) {
	    	
	        throw new FileNotFoundException("Resource not found: " + resource);
	    }
	    return stream;
	}
}
