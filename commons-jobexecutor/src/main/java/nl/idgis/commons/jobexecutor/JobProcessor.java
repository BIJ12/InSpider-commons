package nl.idgis.commons.jobexecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For each job a JobProcessor must be created. 
 * The JobProcessor finds the proper Process for the job to be processed 
 * and determines which actions around the processing occur.<br>
 * This JobProcessor updates job statuses before and after processing by the Job Process,
 * swallows exceptions raised by the Process and sends mail afterwards. 
 * @author Rob
 *
 */
public class JobProcessor {

	private static final Log log = LogFactory.getLog(JobProcessor.class);
	
	private final Map<Class<?>, Process<?>> processes;
	
	private final JobDao jobDao;
	private final JobMail jobMail;
	private final JobLogger jobLogger;
	
	public JobProcessor (final Map<Class<?>, Process<?>> processes, final JobDao jobDao, final JobMail jobMail, final JobLogger jobLogger) {
		this.processes = new HashMap<Class<?>, Process<?>> (processes);
		this.jobDao = jobDao;
		this.jobMail = jobMail;
		this.jobLogger = jobLogger;
	}
	
	public Map<Class<?>, Process<?>> getProcesses () {
		return Collections.unmodifiableMap (processes);
	}
	
	public JobDao getJobDao () {
		return jobDao;
	}
	
	public JobMail getJobMail () {
		return jobMail;
	}
	
	public JobLogger getJobLogger () {
		return jobLogger;
	}

	public Callable<Boolean> createJobProcessor (final Job job) {
		if (job == null) {
			throw new NullPointerException ("job is null");
		}
		
		@SuppressWarnings("unchecked")
		final Process<Job> jobProcess = (Process<Job>)processes.get (job.getClass ());
		if (jobProcess == null) {
			log.error("No process found for job type: " + JobTypeIntrospector.getJobTypeName (job));
			return null;
		}
		
		return new Callable<Boolean> () {
			@Override
			public Boolean call () throws Exception {
				boolean error = false;
				try {
					job.setStatus(Job.Status.STARTED);
					jobDao.update(job);
					error = jobProcess.process(job, jobLogger);
					job.setStatus(Job.Status.FINISHED);
				} catch (Exception e) {
					// Log and swallow Exception. An Exception in a process may not
					// cause the JobExecutor to crash
					error = true;
					handleException(job, e);
					jobDao.update(job);
				} finally {
					try {
						if (jobMail==null){
							log.error("Couldn't send e-mail: jobMail is NULL");
						}else{
							jobMail.sendMail(job);
						}
					} catch (Exception e) {
						log.error("Couldn't send e-mail: ", e);
					}
					jobDao.update(job);
				}
				return error;
				
			}
		};
	}
	
	/**
	 * Write a nice error-message to the job
	 * 
	 * @param job
	 * @param e
	 * @return
	 */
	private void handleException(Job job, Exception e) {
		/*
		 * In case of checked exception, don't write an error to the log, just a
		 * warning. After that write the error to the Job. Don't propagate the
		 * Exception.
		 */
		String message = "job (id="
				+ (job != null && job.getId() != null ? job.getId() : null)
				+ ") aborted. Reason: " + ExceptionUtils.getRootCauseMessage(e);
		log.warn(message, e);
		// mark job as aborted
		job.setStatus(Job.Status.ABORTED);
		job.setResult(ExceptionUtils.getRootCauseMessage(e));
	}
}
