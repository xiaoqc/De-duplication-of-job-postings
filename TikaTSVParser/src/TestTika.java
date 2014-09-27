import org.json.*;

import java.io.*;
import java.util.*;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.tika.Tika; 
import org.apache.tika.exception.TikaException; 
import org.apache.tika.metadata.Metadata; 
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.AutoDetectParser; 
import org.apache.tika.parser.Parser; 
import org.apache.tika.parser.ParseContext; 
import org.apache.tika.sax.BodyContentHandler; 
import org.apache.tika.sax.ContentHandlerDecorator;
import org.xml.sax.ContentHandler; 
import org.xml.sax.SAXException; 
import org.xml.sax.helpers.DefaultHandler;
import org.apache.tika.sax.XHTMLContentHandler;
import org.apache.tika.mime.MediaType;

public class TestTika {	  	  
	  public TestTika(String inputF, String outputF) throws FileNotFoundException, IOException, org.xml.sax.SAXException, TikaException, TransformerConfigurationException {			 
		  	File file = new File(inputF);
		    InputStream is = new FileInputStream(file);
		    OutputStream output = new FileOutputStream(new File(outputF));
		    Metadata metadata = new Metadata();		    
		    //ContentHandler ch = new BodyContentHandler(output);
		    ContentHandler ch = new BodyContentHandler();
		    Parser parser = new TSVParser();		
		    
		    
		    StringWriter sw = new StringWriter();
		    SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
		    TransformerHandler handler = factory.newTransformerHandler();
		    handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
		    handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
		    handler.setResult(new StreamResult(output));
		    
		    
		    try {
		    	parser.parse(is, handler, metadata, new ParseContext());
		    } finally {
		    	is.close();
		    }
		    //System.out.println(sw.toString());
	  }	  
	  
	  public class TSVParser extends AbstractParser {
	        private Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
	        public static final String TSV_MIME_TYPE = "application/tsv";
	        
	        public Set<MediaType> getSupportedTypes(ParseContext context) {
	                return SUPPORTED_TYPES;
	        }

	        public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
	                metadata.set(Metadata.CONTENT_TYPE, "application/xhtml+xml; charset=UTF-8");
	                XHTMLContentHandler X = new XHTMLContentHandler(handler, metadata);
	                String[] s = {"postedDate","location","department","title","salary","start","duration","jobtype","applications","company","contactPerson","phoneNumber","faxNumber","emptyColumn","location","latitude","longitude","firstSeenData","url","lastSeenData"};
	                X.startDocument();
	                X.startElement("table", "border", "1px");	             
	                for (int i = 0; i < s.length; i++){
	                	X.element("td", s[i]);
	                }
	                
	                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
	                String line;
					while ((line = reader.readLine()) != null){
						X.startElement("tr");
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < line.length(); i++){
							char c = line.charAt(i);
							if (c == '\t'){
								String tmp = sb.toString();
								if (tmp.equals("")){
									tmp = " ";
								}
		    		    		X.element("td", tmp);
		    		    		sb.delete(0, sb.length());
		    		    	} else {
		    		    		sb.append(c);
		    		    	}
						}
						String tmp = sb.toString();
						if (tmp.equals("")){
							tmp = " ";
						}
    		    		X.element("td", tmp);
						X.endElement("tr");
					}
	                
					X.endElement("table");
	                X.endDocument();	
	        }
	  }
	  
	  public class jsonTableContentHandler extends ContentHandlerDecorator{
		  
	  }
}