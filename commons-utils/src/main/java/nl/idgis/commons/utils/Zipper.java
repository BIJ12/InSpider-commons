package nl.idgis.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;


public class Zipper
{
	private static final int BUFFER = 2048;

	public static boolean zip(String rootDirName, String zipDirName)
	{
		boolean success = true;

		try
		{
			FileOutputStream dest = new FileOutputStream(rootDirName + File.separator + zipDirName + ".zip");
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

			success = zipDir(out, rootDirName, zipDirName);
			out.close();

			if(!success)
			{
				return false;
			}
		}
		catch(Exception e)
		{
			success = false;
		}

		return success;
	}
	
	

	public static boolean unzip(String destDir, String zipFileName)
	{
		boolean success = true;

		try
		{
			ZipFile zipFile = new ZipFile(zipFileName);
			Enumeration entries = zipFile.entries();

			while(entries.hasMoreElements())
			{
				ZipEntry entry = (ZipEntry)entries.nextElement();

				success = createDirForFile(destDir, entry.getName());
				if(!success)
				{
					return false;
				}

				String test = entry.getName().substring(entry.getName().length()-1);
				if(test.equals(File.separator)){
				//niks
				}else{
					File testFile = new File(destDir + File.separator + entry.getName());
					if(!testFile.isDirectory())
					{
						success = unzipFile(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(destDir + File.separator + entry.getName())));
						if(!success)
						{
							return false;
						}
					}
				}
			}

			zipFile.close();
		}
		catch(IOException ioe)
		{
			success = false;
		}

		return success;
	}
	
	
	public static boolean unzip(String destDir, ZipInputStream zipStream)
	{
		boolean success = true;

		try
		{
			ZipEntry entry = null;
			
			 while((entry = zipStream.getNextEntry())!=null) {
				success = createDirForFile(destDir,entry.getName());
				if(!success)
				{
					return false;
				}

				String test = entry.getName().substring(entry.getName().length()-1);
				if(test.equals(File.separator)){
				//niks
				}else{
					File testFile = new File(destDir + File.separator + entry.getName());
					if(!testFile.isDirectory())
					{
						byte by[] = new byte[(int) entry.getCompressedSize()];
						success = unzipZipStream(zipStream, new BufferedOutputStream(new FileOutputStream(destDir + File.separator + entry.getName())), by);
						if(!success)
						{
							return false;
						}
					}
				}
			}

			zipStream.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace(System.out);	
			success = false;
		}

		return success;
	}
	
	

	private static boolean zipDir(ZipOutputStream out, String rootDirName, String zipDirName)
	{
		boolean success = true;

		try
		{
			byte[] data = new byte[BUFFER];
			System.out.println(rootDirName + File.separator + zipDirName);
			File dir = new File(rootDirName + File.separator + zipDirName);
			
			String[] files = dir.list();

			for (int i = 0; i < files.length; i++)
			{
				String fileName = rootDirName + File.separator + zipDirName + File.separator + files[i];
				File file = new File(fileName);

				if(file.isDirectory())
				{
					success = zipDir(out, rootDirName, zipDirName + File.separator + files[i]);
					if(!success)
					{
						return false;
					}
				}
				else
				{
					FileInputStream fi = new FileInputStream(fileName);
					BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);

					ZipEntry entry = new ZipEntry(zipDirName + File.separator + files[i]);

					out.putNextEntry(entry);

					int count;
					while((count = origin.read(data, 0, BUFFER)) != -1)
					{
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);	
			success = false;
		}

		return success;
	}

	private static boolean createDirForFile(String destDir, String filePath)
	{
		boolean success = true;


		StringTokenizer st = new StringTokenizer(filePath, File.separator);
		while(st.hasMoreTokens())
			
		{
			
			destDir += File.separator + st.nextToken();
			if(st.hasMoreTokens())
			{
				File dirToCreate = new File(destDir);
				if(!dirToCreate.exists())
				{
					
					success = dirToCreate.mkdir();
					if(!success)
					{
						return false;
					}
				}
			}
		}

		return success;
	}

	private static boolean unzipFile(InputStream in, OutputStream out)
	{
		boolean success = true;
		byte[] buffer = new byte[BUFFER];
		int len;

		try
		{
			while((len = in.read(buffer)) >= 0)
			{
				out.write(buffer, 0, len);
			}

			in.close();
			out.close();
		}
		catch(IOException e)
		{
			success = false;
		}

		return success;
	}
	
	private static boolean unzipZipStream(InputStream in, OutputStream out, byte by[])
	{
		boolean success = true;
		try
		{
			for(int index = 0; (index = in.read(by)) > 0;)
			{
				out.write(by, 0, index);
			}
			out.close();
		}
		catch(IOException e)
		{
			
			success = false;
		}

		return success;
	}
	
	
	
}