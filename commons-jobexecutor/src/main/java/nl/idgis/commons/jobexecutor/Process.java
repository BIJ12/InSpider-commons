/**
 * 
 */
package nl.idgis.commons.jobexecutor;


/**
 * Abstract class that handles processing of jobs.<br>
 * Subclasses must implement<br>
 * <code>boolean process(Job job) throws Exception;</code> <br>
 * This class sets proper job status, handles exceptions and sends mail
 * @author Rob
 * 
 */
public interface Process<T extends Job> {

	//.. do whatever is needed to process specific job, using specific dao ..
	/**
	 * The real processing takes place here.<br>
	 * Functional errors should be logged. 
	 * @param job
	 * @return true if a functional error occurred, false otherwise
	 * @throws Exception when a technical error occurred
	 */
	boolean process(T job, JobLogger logger) throws Exception;
	
	/**
	 * Returns the type of the corresponding job class.
	 * 
	 * @return The job class.
	 */
	Class<? extends Job> getJobType ();
}
