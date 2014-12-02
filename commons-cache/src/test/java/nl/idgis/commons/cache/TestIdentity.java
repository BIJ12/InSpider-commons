/**
 * 
 */
package nl.idgis.commons.cache;

import org.junit.Assert;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Rob
 *
 */
public class TestIdentity {
	UUID uuid;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		uuid = UUID.randomUUID();
	}

	@Test
	public void test1() {
		FileIdentity id1 = new UUIDIdentity(".zip");
		Assert.assertTrue(id1.getName().startsWith(id1.getIdStr()));
	}

	@Test
	public void test2() {		
		FileIdentity id2 = new UUIDIdentity(uuid,".zip");
		Assert.assertTrue(id2.getIdStr().equals(uuid.toString()));
	}

	
}
