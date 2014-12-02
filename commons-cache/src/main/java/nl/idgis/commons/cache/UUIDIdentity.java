package nl.idgis.commons.cache;

import java.io.File;
import java.util.UUID;


/**
 * Implementation of fileIdentity that assumes no subdirectory structure and 
 * @author Rob
 *
 */
public class UUIDIdentity implements FileIdentity{
	private UUID uuid;
	private String path="", ext;
	
	public UUIDIdentity(UUID uuid, String ext){
		this.uuid = uuid;
		this.ext = ext;
	}
	
	public UUIDIdentity(String ext){
		this.uuid = UUID.randomUUID();
		this.ext = ext;
	}
	
	@Override
	public Long getId() {
		return uuid.getMostSignificantBits();
	}
	
	@Override
	public String getIdStr() {
		return uuid.toString();
	}

	@Override
	public String getPath(){
		return path;
	}
	
	@Override
	public String getName(){
		return uuid.toString() + ext;
	}
	
	public String toString(){
		if (path.endsWith(File.separator)){
			return getPath()+getName();
		}else{
			return getPath()+File.separator+getName();
		}
	}

}

