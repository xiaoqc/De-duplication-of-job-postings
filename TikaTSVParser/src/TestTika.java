import java.io.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;




import org.apache.tika.exception.TikaException; 
import org.apache.tika.metadata.Metadata; 
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser; 
import org.apache.tika.parser.ParseContext; 
import org.apache.tika.sax.BodyContentHandler; 
import org.xml.sax.ContentHandler; 

//TestTika read tsv file and output json files, one json file per job entry
public class TestTika {	  	  
	public TestTika(String inputF, String outputF, String jobEntryDirectory) throws FileNotFoundException, IOException, org.xml.sax.SAXException, TikaException, TransformerConfigurationException {			 
		File file = new File(inputF);
	    InputStream is = new FileInputStream(file);
	    OutputStream output = new FileOutputStream(new File(outputF));
	    Metadata metadata = new Metadata();		    
	    ContentHandler ch = new BodyContentHandler(output);
	    Parser parser = new TSVParser();	
	   
	    
	    
	    //StringWriter sw = new StringWriter();
	    SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
	    TransformerHandler handler = factory.newTransformerHandler();
	    handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
	    handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
	    handler.setResult(new StreamResult(output));
	    //System.out.println(sw.toString());
	    
	    
	    try {
	    	parser.parse(is, handler, metadata, new ParseContext());	    	
	    } finally {
	    	is.close();
	    	output.close();
	    }
	    
	    try{
	    	JsonTableContentHandler jsonch = new JsonTableContentHandler(jobEntryDirectory);
			SAXParserFactory saxF = SAXParserFactory.newInstance();
			SAXParser saxP = saxF.newSAXParser();
			saxP.parse(new File(outputF), jsonch);
		} catch (Exception e){
			e.printStackTrace();
		}	    
	}	 	
}