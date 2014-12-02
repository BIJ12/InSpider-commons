/**
 * 
 */
package nl.idgis.commons.cache;

/**
 * Represents a cache object that can be uniquely indentified in the context used.<br>
 * The cache identity provides a (subdirectory) path and a name for a file in cache. 
 * @author robs
 * 
 */
public interface FileIdentity {

	/**
	 * The unique identifier to represent the identity
	 * @return Long identifier 
	 */
	public Long getId();
	
	/**
	 * Unique identifier as a string e.g. file Path+Name
	 * @return String identifier
	 */
	public String getIdStr();
	
	/**
	 * Path of the file
	 * @return Path part of the file location
	 */
	public String getPath();

	/** 
	 * Name of the file
	 * @return Name part of the file location
	 */
	public String getName();

}
