/**
 * 
 */
package nl.idgis.commons.convert.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;

/**
 * @author eshuism
 * 16 mei 2012
 */
public class ShapeFileGenerator {
	private static final Log log = LogFactory.getLog(ShapeFileGenerator.class);
	/**
	 * Creates a set of files that together makes a shapefile in a temporary directory.
	 * 
	 * @param collection
	 * @param shapeFile 
	 * @return
	 */
    public void createShapeFile(SimpleFeatureCollection collection, File shapeFile){
    	assert(collection != null);
    	
    	if(collection.size() == 0){
    		return;
    	}
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();

        Map<String, Serializable> params = new HashMap<String, Serializable>();
        try {
			params.put("url", shapeFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
        params.put("create spatial index", Boolean.TRUE);

        ShapefileDataStore newDataStore = null;
		try {
			newDataStore = (ShapefileDataStore) dataStoreFactory.createNewDataStore(params);
			
			newDataStore.createSchema(collection.getSchema());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

        /*
         * Write the features to the shapefile
         */
        Transaction transaction = new DefaultTransaction("create");

        SimpleFeatureSource featureSource;
		try {
			String typeName = newDataStore.getTypeNames()[0];
			featureSource = newDataStore.getFeatureSource(typeName);

	        if (featureSource instanceof SimpleFeatureStore) {
	            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
	
	            featureStore.setTransaction(transaction);
	            try {
	                featureStore.addFeatures(collection);
	                transaction.commit();
	
	            } catch (Exception problem) {
	                problem.printStackTrace();
	                transaction.rollback();
	
	            } finally {
	                transaction.close();
	            }
	        } else {
				throw new RuntimeException(typeName + " does not support read/write access");
	        }
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    	
	}

	/**
	 * Creates a set of files that together makes a shapefile. Return the set of files as an archive
	 * @param featureCollection with features to create the shapeFile from
	 * @param shapeName 
	 * @param shapeFile is the file that must be created. Extension must be ".shp"
	 * @return archive of the generated files returned as zip
	 */
    public void createZippedShapeFile(SimpleFeatureCollection featureCollection, File shapeFileZip, String shapeName){
    	
    	File parentDir = shapeFileZip.getParentFile();
    	String shapeFileName = shapeName.concat(".shp");
    	File shapeDir = new File(parentDir, "shape");
    	shapeDir.deleteOnExit();
    	boolean createDirSuccess = shapeDir.mkdir();
    	log.debug("Directory shape files: " + shapeDir);
    	assert(createDirSuccess || shapeDir.exists());
    	this.createShapeFile(featureCollection, new File(shapeDir, shapeFileName));

        try {
			this.zipDirectory(shapeDir, shapeFileZip);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
	        File[] shapeFileFiles = shapeDir.listFiles();
	        for (File file : shapeFileFiles) {
	        	file.delete();
			}
        }
        
    }

    private final void zipDirectory( File directory, File zip ) throws IOException {
        ZipOutputStream zos = new ZipOutputStream( new FileOutputStream( zip ) );
        zip( directory, directory, zos );
        zos.close();
      }
     
      private final void zip(File directory, File base,
          ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
       	for (File file : files) {        		
          if (file.isDirectory()) {
            zip(file, base, zos);
          } else {
            FileInputStream in = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(file.getName());
            entry.setSize(file.length());
            log.debug("Zipentry: " + entry.getName() + " ["+entry.getSize()+"]");
            zos.putNextEntry(entry);
            while ((read = in.read(buffer)) > 0) {
              zos.write(buffer, 0, read);
            }
            zos.closeEntry();
            in.close();
          }
        }
      }

}
