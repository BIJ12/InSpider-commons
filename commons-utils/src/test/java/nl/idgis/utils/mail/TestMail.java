/**
 * 
 */
package nl.idgis.utils.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import nl.idgis.commons.utils.Mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author Rob
 *
 */
public class TestMail {
	private static final Log logger = LogFactory.getLog(TestMail.class);
	
	@Test
	public void testBuildMsg() {
		HashMap<String,Object> placeholders = new HashMap<String,Object>() ;
		String file = new String("file.zip");
		String[] keys = {"mail","me", "file"};
		Object[] values = {"email","Robert", file};
		String subject = "Here is a $mail from $me\n You can download $file";
		int len = subject.length();
		for (int i=0;i<keys.length;i++){
			placeholders.put(keys[i], values[i]);
			len = len - keys[i].length() - 1;
			len = len + values[i].toString().length();
		}
		String msg = Mail.createMsg(placeholders, subject);
		assertNotNull(msg);
		logger.info("template: " + subject);
		logger.info("result  : " + msg);
		assertTrue(msg.length() == len);
	}

}
