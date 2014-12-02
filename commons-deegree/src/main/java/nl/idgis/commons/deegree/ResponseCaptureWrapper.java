package nl.idgis.commons.deegree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.io.output.ByteArrayOutputStream;

class ResponseCaptureWrapper extends HttpServletResponseWrapper  {
	
	final ByteArrayOutputStream serviceResponseBuffer = new ByteArrayOutputStream();

	public ResponseCaptureWrapper(HttpServletResponse response) {
		super(response);
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {					
		return new ServletOutputStream() {

			@Override
			public void write(int i) throws IOException {							
				serviceResponseBuffer.write(i);
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				serviceResponseBuffer.write(b, off, len);
			}

			@Override
			public void write(byte[] b) throws IOException {
				serviceResponseBuffer.write(b);
			}

			@Override
			public void close() throws IOException {
				serviceResponseBuffer.close();
			}

			@Override
			public void flush() throws IOException {							
				serviceResponseBuffer.flush();
			}
		};
	}
	
	InputStream getCapturedContent() {
		return new ByteArrayInputStream(serviceResponseBuffer.toByteArray());
	}
	
	int getCapturedContentSize() {
		return serviceResponseBuffer.size();
	}
}
