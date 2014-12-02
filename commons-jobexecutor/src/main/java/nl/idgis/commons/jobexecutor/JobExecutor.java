package nl.idgis.commons.jobexecutor;

import java.util.concurrent.Callable;

import nl.idgis.commons.jobexecutor.Job.Status;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A JobExecutor provides a loop for processing jobs.<br>
 * Explicitly do not catch Exceptions. The Process-es itself are responsible for
 * catching Exceptions and decide if an exception is handable cq recoverable. If
 * not the Process-es propagate RuntimeExceptions which cause the JobExecutor to
 * stop.
 * 
 * @author eshuism, stekelenburgr
 * 
 */
public class JobExecutor implements Runnable {

	private static final long MIN_WAIT_TIME = 1000;
	private long waitTime = 2000;

	private static final Log log = LogFactory.getLog(JobExecutor.class);

	private final JobProcessor jobProcessor;
	private JobCollector jobCollector;//autowire

	private boolean terminate = false;
	private Job runningJob = null;
	private long processedJobs = 0l;

	public JobExecutor (final JobProcessor jobProcessor) {
		this.jobProcessor = jobProcessor;
	}
	
	public Job getRunningJob() {
		return runningJob;
	}

	public void terminate() {
		terminate = true;
	}

	public boolean isTerminating() {
		return terminate;
	}

	public long getProcessedJobs() {
		return processedJobs;
	}

	// configuration items
	
	public void setWaitTime(long sleepMillis){
		this.waitTime = Math.max(MIN_WAIT_TIME, sleepMillis);
	}
	
	public void setJobCollector(JobCollector jobCollector) {
		this.jobCollector = jobCollector;
	}

	public void stop() {
		log.info("JobExecuter stopping");
		terminate();
	}
	
	// main entry
	public void run() {
		log.info("JobExecuter started");
		Job job = null;
		// start a loop that gets jobs and has them processed
		while (!terminate) {
			// subloop: (re)try getting new jobs
			do {
				job = jobCollector.nextJob();
				if (job == null) {
					log.debug("No next job");
					sleep(waitTime);
				}
			} while (!terminate && job == null);
			log.info("Next Job: " + job);
			if (job != null) {
				if (job.getStatus () != Status.PREPARED) {
					log.error (String.format ("Job %d must have status PREPARED", job.getId ()));
					break;
				}
				processJob(job);
			}
			// take some sleep to prevent this loop from taking all CPU
			sleep(MIN_WAIT_TIME);
		}
		log.info("JobExecuter terminated");
	}

	private static void sleep(long sleepMillis) {
		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException e) {
			log.warn("Interrupted while getting jobs");
		}
	}

	/**
	 * Find a Process that can process this job and handle it the job for execution.<br>
	 * 
	 * @param job to be processed
	 */
	protected void processJob(Job job) {
		runningJob = job;
		processedJobs++;

		final String jobTypeName = JobTypeIntrospector.getJobTypeName (job);
		log.debug("Start processing job type: " + jobTypeName);

		final Callable<Boolean> callable = jobProcessor.createJobProcessor (job);
		if (callable != null) {
			try {
				if (callable.call ()) {
					log.error("An error (functional or technical) occurred with job: " + job);
				}
			} catch (Exception e) {
				log.error ("Exception in process", e);
			}
		}
		
		runningJob = null;
	}

}
