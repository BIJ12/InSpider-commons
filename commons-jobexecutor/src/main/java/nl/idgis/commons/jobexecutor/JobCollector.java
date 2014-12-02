/**
 * 
 */
package nl.idgis.commons.jobexecutor;

/**
 * Responsible for retrieving jobs in the right order to be processed by the JobExecutor.<br>
 * This could mean using a dao to get new jobs from a table,<br>
 * or using several dao's and deciding which has highest priority. 
 * @author Rob
 *
 */
public interface JobCollector {
	
	/**
	 * Retrieve the next job to be processed.
	 * Job status must be atomically updated to PREPARED when fetching the next job.
	 * 
	 * @return
	 */
	Job nextJob();
}
