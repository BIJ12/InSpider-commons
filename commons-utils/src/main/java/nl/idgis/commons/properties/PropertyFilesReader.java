package nl.idgis.commons.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertyFilesReader {


	public static Properties readPropertyFiles(File file) throws FileNotFoundException, IOException {
		return readPropertyFiles(file, new Properties());
	}
	
	public static Properties readPropertyFiles(File file, Properties allProps) throws FileNotFoundException, IOException {

		if(file.isDirectory()){
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				readPropertyFiles(files[i], allProps);
			}
		} else if(file.getAbsolutePath().endsWith(".properties")){
			Properties props = new Properties();

			
			props.load(new FileInputStream(file));

			allProps.putAll(props);
		} else {
		}
		
		return allProps;
	}
}
