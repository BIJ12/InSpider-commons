package nl.idgis.commons.jobexecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestProcess implements Process<TestJob> {
	private static final Log log = LogFactory.getLog(TestProcess.class);
	
	@Override
	public boolean process(final TestJob job, final JobLogger logger) throws Exception {
		Thread.sleep(1000);
		if (Math.random() < 0.25) throw new Exception("some exception thrown");
		log.info("Processed job: (" + job + ") by process for jobtype " + JobTypeIntrospector.getJobTypeName (job));
		return false;
	}

	@Override
	public Class<? extends Job> getJobType () {
		return TestJob.class;
	}
}
