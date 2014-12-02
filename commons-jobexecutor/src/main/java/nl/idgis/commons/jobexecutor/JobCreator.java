/**
 * 
 */
package nl.idgis.commons.jobexecutor;

/**
 * JobGenerator puts jobs into a (persistent) storage for later retrieval. 
 * @author Rob
 *
 */
public interface JobCreator {
	
	/**
	 * Put a job for later retrieval.
	 * @param job
	 */
	void putJob(Job job);
}
