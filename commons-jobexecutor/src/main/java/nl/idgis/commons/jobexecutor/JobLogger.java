package nl.idgis.commons.jobexecutor;

import java.util.Map;

/**
 * Use for logging during job processing.
 * @author Rob
 *
 */
public interface JobLogger {

	public enum LogLevel {
		ERROR,
		WARNING
	}
	
	void logString (Job job, String key, LogLevel logLevel, String message);
	/**
	 * Log a message during job processing.
	 * @param job the job that is being processed
	 * @param key a key that explains the message
	 * @param logLevel level of the log
	 * @param message the logmessage
	 * @param context a map of primitives to store extra information with the message 
	 */
	void logString (Job job, String key, LogLevel logLevel, String message, Map<String,Object> context);
}
