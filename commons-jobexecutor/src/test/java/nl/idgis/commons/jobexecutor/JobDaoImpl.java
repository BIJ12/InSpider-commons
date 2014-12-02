package nl.idgis.commons.jobexecutor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import nl.idgis.commons.jobexecutor.Job.Status;
import nl.idgis.commons.jobexecutor.JobLogger.LogLevel;

public class JobDaoImpl implements JobDao, JobCollector, JobCreator {
	private static Long pk;
	private Map<Long, Job> jobs ;
	private Job currJob = null;
	private Iterator<Job> jobIterator ;
	private Properties logitems;
	
	public JobDaoImpl(){
		pk = 0L;
		jobs = new HashMap<Long, Job>();
	}
	
	@Override
	public void create(Job job) {
		pk++;
		job.setId(pk);
		jobs.put(job.getId(), job);
	}

	@Override
	public Job getJob(Long pk) {
		return jobs.get(pk);
	}

	@Override
	public void update(Job job) {
		jobs.put(job.getId(), job);
	}

	@Override
	public void delete(Job job) {
		jobs.remove(job.getId());
	}

	@Override
	public void putJob(Job job) {
		create(job);
	}
	
	@Override
	public Job nextJob() {
		if (jobIterator==null) jobIterator = jobs.values().iterator() ;
		if (jobIterator.hasNext()) {
			currJob = jobIterator.next();
			currJob.setStatus (Status.PREPARED);
		}else{
			currJob = null;
		}
		
		return currJob;
	}

	public int nrOfJobs(){
		return jobs.size();
	}

	@Override
	public void putLogItem(Job job, String msg, String key, LogLevel level, String context) {
		logitems.put(job.getId()+key, msg);
	}
}
