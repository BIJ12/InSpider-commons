package nl.idgis.commons.deegree;

import static org.slf4j.LoggerFactory.getLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

class DocumentTransformer {
	
	private static final Logger LOG = getLogger(DocumentTransformer.class);
	
	private final Map<QName, Document> transformerDocuments;
	
	DocumentTransformer(final Map<QName, String> transformerDocumentRefs) throws ParserConfigurationException, SAXException, IOException {
		transformerDocuments = new HashMap<QName, Document>();
		
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		final ClassLoader classLoader = getClass().getClassLoader();
		for(Map.Entry<QName, String> transformerDocumentRef : transformerDocumentRefs.entrySet()) {
			final String documentRef = transformerDocumentRef.getValue();
			final InputStream documentStream = classLoader.getResourceAsStream(documentRef);
			if(documentStream == null) {
				throw new IllegalArgumentException("Transformer document not found: " + documentRef);
			}
			
			LOG.debug("loading transformer document: {}", documentRef);
			transformerDocuments.put(transformerDocumentRef.getKey(), documentBuilder.parse(documentStream));
		}
	}

	void transform(final InputStream inputStream, final OutputStream outputStream) throws ParserConfigurationException, SAXException, IOException, TransformerException {
		final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);		
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setExpandEntityReferences(false);
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		
		LOG.debug("parsing xml document");
				
		final Document d = documentBuilder.parse(inputStream);
		final Node rootNode = d.getFirstChild();
		
		final String rootNodeLocalName = rootNode.getLocalName();
		final QName rootNodeName;
		if(rootNodeLocalName == null) {
			rootNodeName = new QName(null, rootNode.getNodeName());
		} else {
			rootNodeName = new QName(rootNode.getNamespaceURI(), rootNodeLocalName);
		}
		
		final Transformer transformer;
		final TransformerFactory transformerFactory = TransformerFactory.newInstance();
		
		final Document transformerDocument = transformerDocuments.get(rootNodeName);
		if(transformerDocument == null) {
			LOG.debug("nothing to do for document with root node name: {}", rootNodeName);
			transformer = transformerFactory.newTransformer();
		} else {
			LOG.debug("transforming document with root node name: {}", rootNodeName);			
			transformer = transformerFactory.newTransformer(new DOMSource(transformerDocument));
			final DocumentType docType = d.getDoctype();
			if(docType != null) {
				transformer.setParameter("doctype-system", docType.getSystemId());
			}
		}
		
		transformer.transform(new DOMSource(d), new StreamResult(outputStream));
		
		LOG.debug("transformer finished");
	}
}
