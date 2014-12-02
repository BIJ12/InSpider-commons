package nl.idgis.commons.jobexecutor;

import java.util.HashMap;
import java.util.Map;


public class TestJobExecutor {
	private static Map<Class<?>, Process<?>> processes;
	private static JobProcessor jobProcessor;
	private static JobMail mail ;
	private static JobDaoImpl dao ;
	private static JobLogger jobLogger;

	/**
	 * Shows a possible use of the JobExecutor with different Queue implementations.
	 * @param args
	 */
	public static void main(String[] args) {
		// make a JobDao
		dao = new JobDaoImpl();
		// make a JobGenerator, the dao does the work
		JobCreator generator = dao;
		// make a JobCollector, the dao does the work
		JobCollector collector = dao;
		// make JobMail
		mail = new JobMailImpl();
		// make JobLogger
		jobLogger = new JobLoggerImpl (dao);
		// make Process
		processes = new HashMap<Class<?>, Process<?>>();
		Process<?> process = new TestProcess();
		processes.put(TestJob.class, process);
		jobProcessor = new JobProcessor(processes, dao, mail, jobLogger);
		// make Jobs
		generator.putJob(new TestJob(20));
		generator.putJob(new TestJob( 5));
		generator.putJob(new TestJob(10));
		generator.putJob(new TestJob(30));
		generator.putJob(new TestJob( 5));
		generator.putJob(new TestJob(20));
		System.err.println("NrOfJobs: " + dao.nrOfJobs());
		
		// make the Job Executor, it only needs a collector and the available processes
		processes = new HashMap<Class<?>, Process<?>>();
		processes.put(TestJob.class, process);
		jobProcessor = new JobProcessor(processes, dao, mail, jobLogger);
		JobExecutor exec = new JobExecutor(jobProcessor);
		exec.setJobCollector(collector);
		
		try {
			Thread t = new Thread(exec);
			t.setPriority(Thread.MIN_PRIORITY);
			t.start();
			System.err.println("running");
			// let it run for some time
			Thread.sleep(10000);
			System.err.println("stopping");
			exec.stop();
			System.err.println("stopped");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
