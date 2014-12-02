package nl.idgis.commons.deegree;

import static org.slf4j.LoggerFactory.getLogger;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.FileCacheImageOutputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;

import org.slf4j.Logger;

/**
 * A filter to deal with ArcGIS quirks
 * 
 * @author <a href="mailto:reijer.copier@idgis.nl">Reijer Copier</a> 
 * 
 */
public class QuirksFilter implements Filter {
	
	private static final Logger LOG = getLogger(QuirksFilter.class);
	
	private DocumentTransformer wmsCapabilitiesTransformer; 
	
	@Override
	public void init(final FilterConfig filterConfig) throws ServletException {
		try {
			final Map<QName, String> transformerDocumentRefs = new HashMap<QName, String>();
			transformerDocumentRefs.put(new QName("http://www.opengis.net/wms", "WMS_Capabilities"), "nl/idgis/commons/deegree/transform-wms-1.3.0.xsl");
			transformerDocumentRefs.put(new QName("WMT_MS_Capabilities"), "nl/idgis/commons/deegree/transform-wms-1.1.1.xsl");
			
			wmsCapabilitiesTransformer = new DocumentTransformer(transformerDocumentRefs);
			
			LOG.debug("initialized");
		} catch(Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
		try {
			LOG.debug("filter entered");
			
			final HttpServletRequest httpRequest = (HttpServletRequest)request;
			final HttpServletResponse httpResponse = (HttpServletResponse)response;
			final RequestParser requestParser = new RequestParser(httpRequest);
			
			if(requestParser.headerContains("User-Agent", "ArcGIS", "ArcMap")
				&& requestParser.isGetMethod()) {
				
				if(requestParser.parameterEquals("request", "GetCapabilities")
					&& (!requestParser.parameterExists("service") // service parameter is optional for WMS GetCapabilities request
						|| requestParser.parameterEquals("service", "WMS"))) {
				
					LOG.debug("GetCapabilities request originating from ArcGIS detected; transforming response");
					
					final ResponseCaptureWrapper wrapper = new ResponseCaptureWrapper(httpResponse);
					chain.doFilter(request, wrapper);
					
					wmsCapabilitiesTransformer.transform(wrapper.getCapturedContent(), response.getOutputStream());
					
					return;
				} else if(requestParser.parameterEquals("request", "GetLegendGraphic")) {
					
					LOG.debug("GetLegendGraphic request originating from ArcGIS detected; removing alpha layer");
					
					final String format = requestParser.getParameter("format");
					if(format != null) {
						final Iterator<ImageWriter> writers = ImageIO.getImageWritersByMIMEType(format);
						if(writers.hasNext()) {
							final ImageWriter writer = writers.next();
							
							final ResponseCaptureWrapper wrapper = new ResponseCaptureWrapper(httpResponse);
							chain.doFilter(request, wrapper);
							
							final BufferedImage originalImage = ImageIO.read(wrapper.getCapturedContent());
							final int width = originalImage.getWidth();
							final int height = originalImage.getHeight();
							
							LOG.debug("image received from deegree, width: {}, height: {}", width, height);
							
							final BufferedImage opaqueImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);							
							
							final Graphics g = opaqueImage.getGraphics();
							g.setColor(Color.white);
							g.fillRect(0, 0, width, height);
							g.drawImage(originalImage, 0, 0, null);
							
							LOG.debug("writing opaque image");
							
							writer.setOutput(new FileCacheImageOutputStream(response.getOutputStream(), null));						
							writer.write(opaqueImage);
							
							return;
						} else {
							LOG.debug("couldn't find writer for requested format: {}", format);
						}
					} else {
						LOG.debug("format parameter missing");
					}
				}
			} 
			
			LOG.debug("nothing to do");
			chain.doFilter(request, response);
		} catch(Exception e) {
			LOG.error("failure", e);			
			throw new ServletException("Error in QuirksProxy", e);		
		}
	}

	@Override
	public void destroy() {
		
	}
}
