import org.json.*;

import java.io.*;
import java.util.*;

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
	  public static void main(String[] args) throws FileNotFoundException, IOException, SAXException, TikaException{
		  TestTika t = new TestTika();
	  }
	  
	  public TestTika() throws FileNotFoundException, IOException, org.xml.sax.SAXException, TikaException {	
		    File file = new File("computrabajo-ar-20121106.tsv");
		    //InputStream is = new FileInputStream(new File("C:\\Users\\chen\\Desktop\\xx.tsv"));
		    InputStream is = new FileInputStream(file);
		    //OutputStream output = new FileOutputStream(new File("out.xhtml"));
		    Metadata metadata = new Metadata();		    
		    //ContentHandler ch = new BodyContentHandler(output);
		    ContentHandler ch = new BodyContentHandler();
		    Parser parser = new TSVParser();		
		    try {
		    	parser.parse(is, ch, metadata, new ParseContext());
		    } finally {
		    	is.close();
		    }
	  }
	  
	  /*public void buildXHTML(XHTMLContentHandler X, String buf) throws FileNotFoundException, IOException, org.xml.sax.SAXException, TikaException {
		  	StringBuilder sb = new StringBuilder();
		    X.startDocument();
		    X.startElement("table");
		    X.startElement("tr");
		    for (int i = 0; i < buf.length(); i++){
		    	char c = buf.charAt(i);
		    	if (c == '\t'){
		    		X.element("td", sb.toString());
		    		sb.delete(0, sb.length());
		    	} else if (c == '\n'){
		    		X.element("td", sb.toString());
		    		sb.delete(0, sb.length());
		    		X.startElement("tr");
		    		X.endElement("tr");
		    	} else {
		    		sb.append(c);
		    	}
		    }
		    X.endElement("tr");
		    X.endElement("table");
		    X.endDocument();
		    System.out.println(X.toString());
	  }*/
	  
	  public class TSVParser extends AbstractParser {
	        private Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
	        public static final String TSV_MIME_TYPE = "application/tsv";
	        
	        public Set<MediaType> getSupportedTypes(ParseContext context) {
	                return SUPPORTED_TYPES;
	        }

	        public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
	                metadata.set(Metadata.CONTENT_TYPE, "application/xhtml+xml");
	                XHTMLContentHandler X = new XHTMLContentHandler(handler, metadata);
	                X.startDocument();
	                X.startElement("table");	                
	                
	                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
	                String line;
					while ((line = reader.readLine()) != null){
						X.startElement("tr");
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < line.length(); i++){
							char c = line.charAt(i);
							if (c == '\t'){
		    		    		X.element("td", sb.toString());
		    		    		sb.delete(0, sb.length());
		    		    	} else {
		    		    		sb.append(c);
		    		    	}
						}
						X.endElement("tr");
					}
	                
					X.endElement("table");
	                X.endDocument();	    		  
	        }
	  }
	  public class jsonTableContentHandler extends ContentHandlerDecorator{
		  
	  }
}