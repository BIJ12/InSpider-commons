/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


/**
 * This implementation does not provide for subdirectories between the main path and the file itself.<br>
 * Thus these files must have unique names.
 * @author Rob
 *
 */
public abstract class AbstractCache implements Cache {
	protected String path;
	protected Map<FileIdentity,File> files;

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#nextEntry(nl.ipo.cds.utils.Identity, java.lang.String)
	 */
	@Override
	public OutputStream nextItem(FileIdentity id, String name) throws IOException {
		// do nothing
		return null;
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#nextEntry(nl.ipo.cds.utils.Identity, java.lang.String)
	 */
	@Override
	public OutputStream nextItem(OutputStream os, String name) throws IOException {
		// do nothing
		return os;
	}


	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#rmFile(nl.ipo.cds.utils.Identity)
	 */
	@Override
	public boolean rmFile(FileIdentity id) throws IOException {
		File f = files.remove(id);
//		f.delete();
//		java.nio.file.Files.delete(f.toPath());
		return f==null?false:f.delete();
}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#getFileDate(nl.ipo.cds.utils.Identity)
	 */
	@Override
	public long getFileDate(FileIdentity id) {
		File f = files.get(id);
		if (f==null){
			String name = id.toString();
			f = new File(path+File.separator+name);
		}
		return f.lastModified();
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#getFilePath(nl.ipo.cds.utils.Identity)
	 */
	@Override
	public String getFilePath(FileIdentity id) throws IOException {
		File f = files.get(id);
		return f==null?null:f.getCanonicalPath();
	}

}
