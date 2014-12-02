/**
 * 
 */
package nl.idgis.commons.jobexecutor;

import nl.idgis.commons.jobexecutor.JobLogger.LogLevel;

/**
 *  
 * @author Rob
 *
 */
public interface JobDao {
	
	/**
	 * Create a job e.g. persist in a database table.<br>
	 * When this method succeeds the job has an id (table pk)
	 * @param job
	 */
	void create(Job job);

	/**
	 * Get a job from a job table.
	 * @param pk primary key in job table
	 * @return job or null if not found in job table
	 */
	Job getJob(Long pk);

	/**
	 * Renew the job data in the job table
	 * @param job to be updated
	 */
	void update(Job job);

	/**
	 * Remove the job from the job table.
	 * @param job to be deleted
	 */
	void delete(Job job);
	
	/**
	 * Put a new log item in the joblog table.
	 * @param job the job that the log item belongs to
	 * @param msg the log message
	 * @param key optional key to classify the message 
	 * @param level optional log level (e.g. INFO, DEBUG, ERROR)
	 * @param context optional string to add context information (e.g. x,y coordinate, line nr where error occurred,.. )
	 */
	void putLogItem(Job job, String msg, String key, LogLevel level, String context);

}
