/**
 * 
 */
package nl.idgis.commons.jobexecutor;



/**
 * @author Rob
 *
 */
public interface JobMail {
	/**
	 * Send an email to parties concerned, about the result of the job execution.<br>
	 * Attributes from a specific Job implementation can be used in the email message. 
	 * @param job
	 * @throws Exception
	 */
	void sendMail(Job job) throws Exception;
}
