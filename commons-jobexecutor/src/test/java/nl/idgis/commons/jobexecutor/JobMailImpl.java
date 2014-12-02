package nl.idgis.commons.jobexecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import nl.idgis.commons.jobexecutor.JobMail;

public class JobMailImpl implements JobMail {
	private static final Log log = LogFactory.getLog(JobMailImpl.class);

	@Override
	public void sendMail(Job job) throws Exception {
		log.info("Send mail job: (" + job + ")");
	}
}
