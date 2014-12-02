/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/**
 * A ZippedCache can store more than 1 entries in a zip file.<br>
 * The nextEntry() method closes the current zipEntry and opens the next.<br>
 * This implementation does not provide for subdirectories between the main path and the file itself.
 * @author Rob
 *
 */
public class ZippedCache extends AbstractCache {
	private Map<FileIdentity,OutputStream> outputStreams;
	private Map<FileIdentity,ZipFile> zipfiles;
	
	/**
	 * Makes a zippedCache in the path given
	 * @param path which must already exist.
	 */
	public ZippedCache (String path){
		super();
		this.path = path;
		files = new HashMap<FileIdentity, File>();
		zipfiles = new HashMap<FileIdentity, ZipFile>();
		outputStreams = new HashMap<FileIdentity, OutputStream>();
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#mkCache(java.lang.Object)
	 */
	@Override
	public OutputStream newFile(FileIdentity id) throws IOException {
		String name = id.toString();
		File f = new File(path+File.separator+name);
		files.put(id,f);
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
		outputStreams.put(id, zos);
		return zos;
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#rmCache(java.lang.Object)
	 */
	@Override
	public boolean rmFile(FileIdentity id) throws IOException {
		OutputStream os = outputStreams.remove(id);
		os.close();
		ZipFile zf = zipfiles.remove(id);
		zf.close();
		boolean result = super.rmFile(id);
		return result;
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#addToCache(java.lang.Object, java.lang.String)
	 */
	@Override
	public OutputStream nextItem(FileIdentity id, String name) throws IOException {
		ZipOutputStream zos = (ZipOutputStream) outputStreams.get(id);
		return nextItem(zos,name);
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#addToCache(java.lang.Object, java.lang.String)
	 */
	@Override
	public OutputStream nextItem(OutputStream os, String name) throws IOException {
		ZipOutputStream zos = (ZipOutputStream) os;
		zos.closeEntry();
		if (name !=null){
			ZipEntry z = new ZipEntry(name);
			zos.putNextEntry(z);
		}
		return os;
	}

	/* (non-Javadoc)
	 * @see nl.ipo.cds.utils.cache.Cache#getStream(java.lang.Object)
	 */
	@Override
	public InputStream getInputStream(FileIdentity id, String name) throws IOException {
		File f = files.get(id);
		ZipFile z = zipfiles.get(id);
		if (z==null){
			z = new ZipFile(f);
		}
		zipfiles.put(id, z);
		ZipEntry ze = null ;
		if (name==null || name.isEmpty()){
			// if no name is given look for the first entry
			Enumeration<? extends ZipEntry> e = z.entries();
			if (e.hasMoreElements()){
				ze = e.nextElement();
			}
		}else{
			ze = z.getEntry(name);			
		}		
		return ze==null?null:z.getInputStream(ze);
	}

	@Override
	public int nrOfItems(FileIdentity id) {
		int count = 0;
		try {
			ZipFile z = new ZipFile(new File(getFilePath(id)));
			 for (Enumeration<? extends ZipEntry> e = z.entries(); e.hasMoreElements();e.nextElement()){
				 count++;
			 }
			 z.close();
		} catch (ZipException e) {
			e.printStackTrace();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return count;
	}


}
