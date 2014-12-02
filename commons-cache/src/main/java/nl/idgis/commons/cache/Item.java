/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Item is an item or entry in a cache.<br>
 * This can be a file within a container (e.g. zipfile). <br>
 * or a file within a directory.<br>
 * These methods will do nothing (i.e. have no meaning) in case of a single file cache. 
 * 
 * @author Rob
 *
 */
public interface Item {
	/**
	 * Make a new entry.<br> 
	 * @param id identifier of a (container) file or directory in the cache
	 * @param name Name of the new entry in the (container) file or directory
	 * @return the outputStream of the next item
	 * @throws IOException 
	 */
	OutputStream nextItem(FileIdentity id,String name) throws IOException;
	/**
	 * Make a new entry.<br> 
	 * @param os OutputStream of the current (container) file in the cache
	 * @param name Name of the new entry in the (container) file 
	 * @return the outputStream of the next item
	 * @throws IOException
	 */
	OutputStream nextItem(OutputStream os,String name) throws IOException;
	/**
	 * Get the number of entries.
	 * @param id identifier of a (container) file or directory in the cache.
	 * @return nr of entries in the (container) file or directory, 0 if empty, -1 if unknown
	 */
	int nrOfItems(FileIdentity id) ;

}
