import java.io.*;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;
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
	public TestTika(String prefix, String tsvFile, String outputF, String jobEntryDirectory, String[] s) throws FileNotFoundException, IOException, org.xml.sax.SAXException, TikaException, TransformerConfigurationException, ParserConfigurationException {			 
		List<String> route = getRoute(prefix, tsvFile);
		//System.out.println(route);
		
	    
	    Metadata metadata = new Metadata();		    
	    //ContentHandler ch = new BodyContentHandler(output);
	    Parser parser = new TSVParser();	
	    
	    
	    //StringWriter sw = new StringWriter();
	    SAXTransformerFactory factory = (SAXTransformerFactory)SAXTransformerFactory.newInstance();
	    TransformerHandler handler = factory.newTransformerHandler();
	    handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
	    handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no");
	    
	    //System.out.println(sw.toString());
	    
	    JsonTableContentHandler jsonch = new JsonTableContentHandler(jobEntryDirectory, s);
		SAXParserFactory saxF = SAXParserFactory.newInstance();
		SAXParser saxP = saxF.newSAXParser();
	    

    	FileWriter clearWriter = new FileWriter(outputF);
    	BufferedWriter clearBufferedWriter = new BufferedWriter(clearWriter);
	    try{
		    for (int i = 0; i < route.size(); i++){
		    	clearBufferedWriter.write("");
		    	OutputStream output = new FileOutputStream(new File(outputF));
		    	handler.setResult(new StreamResult(output));
		    	
		    	
		    	String inputF = route.get(i);
		    	File file = new File(inputF);
			    InputStream is = new FileInputStream(file);
			    parser.parse(is, handler, metadata, new ParseContext());
			    is.close();
			    saxP.parse(new File(outputF), jsonch);
			    System.out.println("job " + i + " is finished.");
			    output.close();
		    }
	    } catch(Exception e){  
	    	e.printStackTrace();
	    }
	}	
	public List<String> getRoute(String prefix, String fileName) throws IOException{
		List<String> rst = new ArrayList<String>();
		File file = new File(fileName);
		InputStream input = new FileInputStream(file);
		String line = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		while ((line = reader.readLine()) != null){		
			//System.out.println(line);
			/*String tmp = new String(line);
			String[] s = tmp.split(".");
			if (s[s.length - 1].equals("tsv")){
				rst.add (prefix + line);
			}*/
			rst.add (prefix + line);
		}
		return rst;
	}
}