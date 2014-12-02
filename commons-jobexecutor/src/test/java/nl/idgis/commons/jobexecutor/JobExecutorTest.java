package nl.idgis.commons.jobexecutor;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class JobExecutorTest{

	private static JobDaoImpl dao;
	private static JobMail mail;
	private static JobLogger jobLogger;
	private static Map<Class<?>, Process<?>> processes;
	private static JobProcessor jobProcessor;
	
	@BeforeClass
	public static void testInitialization(){
		// make a JobDao
		dao = new JobDaoImpl();
		// make JobMail
		mail = new JobMailImpl();
		// make JobLogger
		jobLogger = new JobLoggerImpl (dao);
	}
	
	@After
	public void tearDown() throws Exception {
		// nothing to do
	}
	
	@Test
	public void testProcessSuccessfullJob() {
    	TestJob job = createJob();
    	
		Process<TestJob> process = new TestProcess() {
			@Override
			public boolean process(final TestJob job, final JobLogger logger) throws Exception {
				// Do NOT simulate a RuntimeException
				return false;
			}
		};
		
		JobExecutor jobExecutor = createJobExecutor(process);
		Assert.assertNotNull(jobExecutor);
    	jobExecutor.processJob(job);
    	
    	Assert.assertEquals(Job.Status.FINISHED, job.getStatus());
	}

	@Test
	public void testProcessTechnicalExceptionJob() {
    	TestJob job = createJob();
    	
		Process<TestJob> process = new TestProcess() {
			@Override
			public boolean process(final TestJob job, final JobLogger logger) throws Exception {
				// Do simulate a RuntimeException
				throw new Exception("A severe error occurred in process");
			}
		};
		
		JobExecutor jobExecutor = createJobExecutor(process);
		Assert.assertNotNull(jobExecutor);
    	jobExecutor.processJob(job);
    	
    	Assert.assertEquals(Job.Status.ABORTED, job.getStatus());
	}

	@Test
	public void testProcessFunctionalExceptionJob() {
    	TestJob job = createJob();
    	
		Process<TestJob> process = new TestProcess() {
			@Override
			public boolean process(final TestJob job, final JobLogger logger) throws Exception {
				// Do simulate a functional Exception
				return true;
			}
		};

		JobExecutor jobExecutor = createJobExecutor(process);
		Assert.assertNotNull(jobExecutor);
    	jobExecutor.processJob(job);
    	
    	Assert.assertEquals(Job.Status.FINISHED, job.getStatus());
	}

	@Test
	public void testNoProcessForJob() {
    	OtherTestJob job = new OtherTestJob();
		dao.create(job);
    	
		// make a Process with a different type as job
		Process<TestJob> process = new TestProcess() {
			@Override
			public boolean process(final TestJob job, final JobLogger logger) throws Exception {
				// Do NOT simulate an Exception
				return false;
			}
		};
		
		JobExecutor jobExecutor = createJobExecutor(process);
		Assert.assertNotNull(jobExecutor);
    	jobExecutor.processJob(job);
    	
    	Assert.assertEquals(Job.Status.CREATED, job.getStatus());
	}

	private JobExecutor createJobExecutor(Process<TestJob> process) {
		// make ProcessJobType
		processes = new HashMap<Class<?>, Process<?>>();
		processes.put(TestJob.class, process);
		jobProcessor = new JobProcessor(processes, dao, mail, jobLogger);
		return new JobExecutor(jobProcessor);
	}

	private TestJob createJob () {
		TestJob job = new TestJob(1);
		dao.create(job);
		return job;
	}

}
