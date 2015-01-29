package org.adligo.fabricate.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * intended to parse out manifest attributes
 * from a .jar file,
 * generally I am printing/using Specification-Title, Implementation-Vendor
 * for what and Implementation-Version for uniqueness.  
 * @author scott
 *
 */
public class ManifestParser {
	public static final String IMPLEMENTATION_VENDOR = "Implementation-Vendor";
	/**
	 * Note Adligo uses this for the public version
	 */
	public static final String IMPLEMENTATION_VERSION = "Implementation-Version";
	public static final String IMPLEMENTATION_TITLE = "Implementation-Title";
	public static final String SPECIFICATION_VENDOR = "Specification-Vendor";
	/**
	 * Note Adligo uses this for the compile date.
	 */
	public static final String SPECIFICATION_VERSION = "Specification-Version";
	public static final String SPECIFICATION_TITLE = "Specification-Title";
	private Map<String,String> attributes = new HashMap<String,String>();
	
	public ManifestParser() {}
	
	
	private void parse(String p) {
		StringBuilder sb = new StringBuilder();
		char [] chars = p.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			char c = chars[i];
			if (c == '\r') {
				//new line
				String line = sb.toString();
				char [] lineChars = line.toCharArray();
				StringBuilder keyBuilder = new StringBuilder();
				StringBuilder valueBuilder = new StringBuilder();
				
				boolean foundColon = false;
				for (int j = 0; j < lineChars.length; j++) {
					char l = lineChars[j];
					if (!foundColon) {
						if (l == ':') {
							foundColon = true;
						} else {
							
							keyBuilder.append(l);
						}
					} else {
						if (valueBuilder.length() == 0) {
							if (l != ' ') {
								valueBuilder.append(l);
							}
						} else {
							valueBuilder.append(l);
						}
					}
				}
				String key = keyBuilder.toString();
				String value = valueBuilder.toString();
				
				attributes.put(key, value);
				sb = new StringBuilder();
			} else {
				if (c != '\n') {
					sb.append(c);
				}
			}
		}
	}
	
	public String get(String key) {
		return attributes.get(key);
	}
	

	public void readManifest(String jarFileName) {
		File inFile = new File(jarFileName);
		
		BufferedOutputStream out = null;
 		FileInputStream fis = null;
 		BufferedInputStream bis = null;
 		ZipInputStream in = null;
 		
        try
        {
        	 fis = new FileInputStream(inFile);
        	 bis = new BufferedInputStream(fis);
             in = new ZipInputStream(bis);
             
             ZipEntry entry;
             while((entry = in.getNextEntry()) != null)
             {
            	 String name = entry.getName();
            	 if (name.indexOf("MANIFEST.MF") != -1) {
            		 long size = entry.getSize();
            		 try {
            			 int sizeInt = (int) size;
	            		 byte [] b = new byte[sizeInt];
		        		 in.read(b);
		        		 //it doen't even give a charset recommendation in the oracle specification
		        		 // hmm some times I wonder why I picked java
		        		 // http://docs.oracle.com/javase/7/docs/technotes/guides/jar/jar.html#Manifest-Overview
		        		 // lol
		        		 // also the whole reason for this class is the following 
		        		 // don't seem to work to get the MANIFEST.MF attributes
		        		 // Package pkg
		        		 //  pkg.getImplementationTitle()
		        		 // nor from the Manifest class loaded with a url loader
		        		 // (loads my other attributes but no the mains wtf)
		        		 char [] chars = new char[sizeInt];
		        		 for (int i = 0; i < b.length; i++) {
							byte byt = b[i];
							chars[i] = (char) byt;
						 }
		        		 String asString = new String(chars);
		        		 parse(asString);
            		 } catch (ClassCastException x) {
            			 throw new IllegalStateException("MANIFEST.MF file is to big to read?");
            		 }
            	 }
             }
        }
        catch(IOException e)
        {
        	System.err.println("error with inFile '"+ inFile);
             e.printStackTrace();
        } finally {
        	if (out != null) {
        		try {
        			out.close();
        		} catch (IOException x) {
        			x.printStackTrace();
        		}
        	}
        	if (in != null) {
        		try {
        			in.close();
        		} catch (IOException x) {
        			x.printStackTrace();
        		}
        	}
        	if (bis != null) {
        		try {
        			bis.close();
        		} catch (IOException x) {
        			x.printStackTrace();
        		}
        	}
        	if (fis != null) {
        		try {
        			fis.close();
        		} catch (IOException x) {
        			x.printStackTrace();
        		}
        	}
        }
	}
}
