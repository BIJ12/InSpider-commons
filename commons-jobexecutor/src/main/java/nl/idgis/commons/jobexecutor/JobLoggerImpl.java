package nl.idgis.commons.jobexecutor;

import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

public class JobLoggerImpl implements JobLogger {

	private final JobDao jobDao;
	
	public JobLoggerImpl (final JobDao jobDao) {
		this.jobDao = jobDao;  
	}

	@Override
	public void logString(Job job, String key, LogLevel logLevel, String message) {
		jobDao.putLogItem (job, message, key, logLevel, null);
	}

	@Override
	public void logString(Job job, String key, LogLevel logLevel, String message,  Map<String,Object> context) {
		final ObjectMapper mapper = new ObjectMapper ();
		String contextStr;
		try {
			contextStr = mapper.writeValueAsString(context);
		} catch (Exception e) {
			contextStr = e.getMessage();
			e.printStackTrace();
		}
		jobDao.putLogItem (job, message, key, logLevel, contextStr);
	}
}
