/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;


/**
 * A PlainTextCache can store 1 entry per file.<br>
 * The nextEntry() method does nothing.<br>
 * This implementation does not provide for subdirectories between the main path and the file itself.
 * @author Rob
 *
 */
public class FileCache extends AbstractCache {
	
	/**
	 * Makes a PlainTextCache in the path given
	 * @param path which must already exist.
	 */
	public FileCache(String path){
		super();
		this.path = path;
		files = new HashMap<FileIdentity, File>();
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#newFile(nl.ipo.cds.utils.Identity)
	 */
	@Override
	public OutputStream newFile(FileIdentity id) throws IOException {
		String name = id.toString();
		File f = new File(path+File.separator+name);
		files.put(id,f);
		return new FileOutputStream(f);
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#nrOfEntries(nl.ipo.cds.utils.Identity)
	 */
	@Override
	public int nrOfItems(FileIdentity id) {
		return 1;
	}


	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#getInputStream(nl.ipo.cds.utils.Identity, java.lang.String)
	 */
	@Override
	public InputStream getInputStream(FileIdentity id, String name)
			throws IOException {
		File f = files.get(id);
		FileInputStream fis = new FileInputStream(f);
		return fis;
	}

}
