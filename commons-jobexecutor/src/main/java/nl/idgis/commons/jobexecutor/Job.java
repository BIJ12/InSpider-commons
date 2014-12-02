package nl.idgis.commons.jobexecutor;

import java.sql.Timestamp;

public interface Job extends Identity {

	/**
	 * The status of a Job.<br>
	 * Normally status goes from:<br>
	 * <code>
	 * CREATED --> PREPARED --> STARTED --> FINISHED<br>
	 * </code>
	 * In error situations the status will be set to <code>ABORTED</code>.
	 * @author Rob
	 *
	 */
	public enum Status {
		CREATED, PREPARED, STARTED, FINISHED, ABORTED;
	}
	
	public abstract void setId(Long id);

	public abstract String getType();

	/**
	 * Returns a priority value for the job.<br>
	 * A higher priority has a lower value.<br>
	 * @return priority value
	 */
	public abstract int getPriority();

	public abstract String getResult();

	/**
	 * Set a result string to describe the outcome of job processing. 
	 * @param message
	 */
	public abstract void setResult(String message);

	public abstract Status getStatus();

	/**
	 * Set the status of the job.
	 * @param status
	 * @see Status
	 */
	public abstract void setStatus(Status status);


	public abstract Timestamp getCreateTime();

	public abstract void setCreateTime(Timestamp createTime);

	public abstract Timestamp getStartTime();

	public abstract void setStartTime(Timestamp startTime);

	public abstract Timestamp getFinishTime();

	public abstract void setFinishTime(Timestamp finishTime);

}