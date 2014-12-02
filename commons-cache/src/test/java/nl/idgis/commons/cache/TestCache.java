/**
 * 
 */
package nl.idgis.commons.cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Rob
 *
 */
public class TestCache {
	private static final Log logger = LogFactory.getLog(TestCache.class);
	private static final String testText1 = "Hello 1!\n";
	private static final String testText2 = "Hello World 2!\n";

	private static String testDir;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		testDir = System.getProperty("user.home");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testZipCache(){
		ZippedCache zippedCache = new ZippedCache(testDir);
		FileIdentity id = new UUIDIdentity(".zip");
		logger.info("testZipCache");
		try {
			//Make zip file and Write to it
			OutputStream os = zippedCache.newFile(id);
			zippedCache.nextItem(id, "testText1.txt" );	
			byte[] bufW = testText1.getBytes();         
            os.write(bufW, 0, bufW.length);
			zippedCache.nextItem(id, "testText2.txt" );	
			bufW = testText2.getBytes();         
            os.write(bufW, 0, bufW.length);
            os.close();
            
            // Read from zip file
            byte[] bufR ; 
            InputStream is = zippedCache.getInputStream(id, "testText1.txt");
            bufR = getStreamAsByteArray(is);
            is.close();
            String r = new String(bufR);
            logger.info("read " + r);
            logger.info("org  " + testText1);
            Assert.assertTrue(r.equals(testText1));

            is = zippedCache.getInputStream(id, "testText2.txt");
            bufR = getStreamAsByteArray(is);
            is.close();
            r = new String(bufR); 
            logger.info("read " + r);
            logger.info("org  " + testText2);
            Assert.assertTrue(r.equals(testText2));
            
            // Give some file details and delete it
            logger.info("File path   : " + zippedCache.getFilePath(id));
            logger.info("File date   : " + new Date(zippedCache.getFileDate(id)));
            logger.info("File entries: " + zippedCache.nrOfItems(id));
            Assert.assertTrue(zippedCache.nrOfItems(id) == 2);
            Assert.assertTrue(zippedCache.rmFile(id));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		
	}
	
	@Test
	public void testPlainCache(){
		FileCache plainCache = new FileCache(testDir);
		FileIdentity id = new UUIDIdentity(".gml");
		logger.info("testPlainCache");
		try {
			//Make text file and Write to it
			OutputStream os = plainCache.newFile(id);
			plainCache.nextItem(id, null);	
			byte[] bufW = testText1.getBytes();         
            os.write(bufW, 0, bufW.length);
			plainCache.nextItem(id, null);	
			bufW = testText2.getBytes();         
            os.write(bufW, 0, bufW.length);
            os.close();
            
            // Read from text file
            byte[] bufR = new byte[1024]; 
            InputStream is = plainCache.getInputStream(id, null);
            bufR = getStreamAsByteArray(is);
            is.close();
            String r = new String(bufR); 
            logger.info("read " + r);
            logger.info("org  " + testText1 + testText2);
            Assert.assertTrue(r.equals(testText1 + testText2));
            
            // Give some file details and delete it
            logger.info("File path   :" + plainCache.getFilePath(id));
            logger.info("File date   :" + new Date(plainCache.getFileDate(id)));
            logger.info("File entries: " + plainCache.nrOfItems(id));
            Assert.assertTrue(plainCache.nrOfItems(id) == 1);
            Assert.assertTrue(plainCache.rmFile(id));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
		
	}
    /**
         * Returns the contents of the input stream as byte array.
         * @param stream the <code>InputStream</code>
         * @return the stream content as byte array
         */
        public static byte[] getStreamAsByteArray(InputStream stream)
        {
            return getStreamAsByteArray(stream, -1);
        }
        
        /**
         * Returns the contents of the input stream as byte array.
         * @param stream the <code>InputStream</code>
         * @param length the number of bytes to copy, if length < 0,
         *        the number is unlimited
         * @return the stream content as byte array
         */
        public static byte[] getStreamAsByteArray(InputStream stream, int length)
        {
            if(length == 0) return new byte[0];
            boolean checkLength = true;
            if(length < 0)
            {
                length = Integer.MAX_VALUE;
                checkLength = false;
            }
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            try
            {
                int nextValue = stream.read();
                if(checkLength) length--;
                while(-1 != nextValue && length >= 0)
                {
                    byteStream.write(nextValue);
                    nextValue = stream.read();
                    if(checkLength) length--;
                }
                byteStream.flush();
            }
            catch(IOException exc)
            {
                logger.error("Exception while reading from stream", exc);
            }
            return byteStream.toByteArray();
        }
    
}
