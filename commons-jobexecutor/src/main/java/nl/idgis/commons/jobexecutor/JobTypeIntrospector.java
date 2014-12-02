package nl.idgis.commons.jobexecutor;

public class JobTypeIntrospector {

	public static String getJobTypeName (final Job job) {
		final JobType jobType = job.getClass ().getAnnotation (JobType.class);
		
		if (jobType == null) {
			return job.getClass ().getSimpleName ();
		}
		
		return jobType.value ();
	}
}
