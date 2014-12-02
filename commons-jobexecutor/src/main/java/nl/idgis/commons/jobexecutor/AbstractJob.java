package nl.idgis.commons.jobexecutor;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import nl.idgis.commons.utils.DateTimeUtils;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * This is the base class of all specific jobs.
 * It provides access to all common fields of a job.<br> 
 * It has been JPA annotated to be able to get a single database table 
 * for the common fields (in this class) and special fields (in subclasses). 
 * @author Rob
 */
@Entity
@Inheritance (strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn (name = "job_type", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table (name = "job")
public abstract class AbstractJob implements Job {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column( name = "job_type", insertable=false,  updatable=false)
	private String jobType;
	
	@Column(columnDefinition="text")
	private String result;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@NotNull
	private Integer priority;
	
	@NotNull
	private Timestamp createTime;
	
	private Timestamp startTime;
	
	private Timestamp finishTime;
	
	protected AbstractJob () {
		this(0);
	}
	
	public AbstractJob (final int priority) {
		this.priority = priority;
		setStatus(Status.CREATED);
	}
	
	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getId()
	 */
	@Override
	public Long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setId(java.lang.Long)
	 */
	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getType(){
		return jobType;
	}
	
	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getPriority()
	 */
	@Override
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setResult(java.lang.String)
	 */
	@Override
	public void setResult(String message) {
		this.result = message;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getStatus()
	 */
	@Override
	public Status getStatus() {
		return status;
	}
	
	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setStatus(nl.idgis.commons.jobexecutor.DefaultJob.Status)
	 */
	@Override
	public void setStatus(Status status) {
		this.status = status;
		switch (status) {
		case CREATED:
			createTime = DateTimeUtils.now();
			break;
		case STARTED:
			startTime = DateTimeUtils.now();
			break;
		case ABORTED:
		case FINISHED:
			finishTime = DateTimeUtils.now();
			break;
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getResultaat()
	 */
	@Override
	public String getResult() {
		return result;
	}
	
	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getCreateTime()
	 */
	@Override
	public Timestamp getCreateTime() {
		return createTime;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setCreateTime(java.sql.Timestamp)
	 */
	@Override
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getStartTime()
	 */
	@Override
	public Timestamp getStartTime() {
		return startTime;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setStartTime(java.sql.Timestamp)
	 */
	@Override
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#getFinishTime()
	 */
	@Override
	public Timestamp getFinishTime() {
		return finishTime;
	}

	/* (non-Javadoc)
	 * @see nl.idgis.commons.jobexecutor.Job#setFinishTime(java.sql.Timestamp)
	 */
	@Override
	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	@Override
	public String toString(){
		return "Id: "+id+", type: "+ getClass ().getCanonicalName () +", status: "+ status;
	}
	
	@Override  
	public boolean equals(Object obj)  
	{  
		if (obj instanceof AbstractJob == false)  
		{  
			return false;  
		}  
		if (this == obj)  
		{  
			return true;  
		}  
		final AbstractJob otherObject = (AbstractJob) obj;  

		return new EqualsBuilder()  
		.append(this.id, otherObject.id)  
		.isEquals();  
	}

	@Override  
	public int hashCode()  
	{  
		return new HashCodeBuilder()  
		.append(this.id)  
		.toHashCode();  
	}
}