/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;


/**
 * A Cache is a location where one or more files can be stored.<br>
 * Between location and the file itself a number of subdirectories can exist, <br>
 * depending on the actual implementation.<br>
 * Each File can itself have one or more entries.<br> 
 * A File is being identified by an Identity Object, <br>
 * which also provides the (unique) File name.<br>
 * @author Rob
 *
 */
public interface Cache extends Item{
	/**
	 * Make a new file in the cache and give the stream to write to the file.
	 * @param id identifier of a specific file in the cache.
	 * @return stream to write to the file.
	 * @throws IOException
	 */
	OutputStream newFile(FileIdentity id) throws IOException;
	/**
	 * Remove the file from cache
	 * @param id identifier of a specific file in the cache.
	 * @return true if removal was succesfull
	 * @throws IOException
	 */
	boolean rmFile(FileIdentity id) throws IOException;
	
	//TODO ADD void closeFile (FileIdentity id) throws IOException;
	
	/**
	 * Get a stream to read from the file.
	 * @param id identifier of a specific file in the cache.
	 * @param name name of a specific item in the file.
	 * @return stream
	 * @throws IOException when there is no file.
	 */
	InputStream getInputStream(FileIdentity id, String name) throws IOException;
	/**
	 * Get the time in milliseconds (since 1/1/1970 00:00) of the File in cache.<br>
	 * This can be used to see if the file is invalid and therefore should be rewritten.
	 * @param id identifier of a specific file in the cache.
	 * @return time in milliseconds of the file, 0 if there is no file or date is unknown.
	 */
	long getFileDate(FileIdentity id);
	/**
	 * Get the path for this file.
	 * @param id identifier of a specific file in the cache.
	 * @return String that points to the location of the file.
	 * @throws IOException 
	 */
	String getFilePath(FileIdentity id) throws IOException;
}
