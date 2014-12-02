/**
 * 
 */
package nl.idgis.commons.jobexecutor;


/**
 * @author Rob
 *
 */
@JobType ("TEST")
public class TestJob extends AbstractJob {
	
	public TestJob (final int priority) {
		super(priority);
	}
	

}
