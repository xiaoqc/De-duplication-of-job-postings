import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.*;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

//TSVParser transfroms tsv file to XHTML file
//for each entry, it represents as a row in the table
public class TSVParser extends AbstractParser {
		private int jobCounter = 0;
		private boolean removeSame = false;
		private int beginDate;
		private Map<String, Boolean> map = new HashMap<String, Boolean>();
		
        private Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
        public static final String TSV_MIME_TYPE = "application/tsv";        
        public Set<MediaType> getSupportedTypes(ParseContext context) {
        	return SUPPORTED_TYPES;
        }
        
        public TSVParser(boolean flag, String start){
        	this.removeSame = flag;
        	this.beginDate = Integer.parseInt(start);
        }
        //parse tsv file and generate XHTML output
        public void parse(InputStream stream, ContentHandler handler, Metadata metadata, ParseContext context) throws IOException, SAXException, TikaException {
            metadata.set(Metadata.CONTENT_TYPE, "application/xhtml+xml; charset=UTF-8");
            XHTMLContentHandler X = new XHTMLContentHandler(handler, metadata);
            X.startDocument();
            //X.element("title", "xx");
            X.startElement("table", "border", "1px");
            
            /*//add column name
            X.startElement("tr");
            for (int i = 0; i < s.length; i++){
            	X.element("th", s[i]);
            }
            X.endElement("tr");*/
                
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
            String line;
			while ((line = reader.readLine()) != null){				
				this.jobCounter++;
				/*StringBuilder sb = new StringBuilder();				
				List<String> buf = new ArrayList<String>();				
				for (int i = 0; i < line.length(); i++){
					char c = line.charAt(i);
					if (c == '\t'){
						String tmp = sb.toString();
	    	    		sb = new StringBuilder();
	    	    		buf.add(tmp);	    	    		
	    	    	} else {
	    	    		sb.append(c);
	    	    	}
				}
				buf.add(sb.toString());*/
				String[] words = line.split("\t");				
				if (words.length != 20){
					System.out.println("wrong job entry");
					continue;
				}
				//words[14] = words[14].replace(',', ' ');
				/*if (jobCounter - 1 == 193){
					for (int i = 0; i < words.length; i++){
						System.out.println(i + " " + words[i]);
					}
				}*/
				if (this.removeSame && isSame(words[words.length - 3], words[words.length - 1], words[words.length - 2])){
					continue;
				}				
				
				X.startElement("tr");
				for (int i = 0; i < words.length; i++){
					String tmp = words[i];
					if (tmp.equals("")){
						tmp = " ";
					}
			    	X.element("td", tmp);					
				}
				X.endElement("tr");
			}
               
			X.endElement("table");
            X.endDocument();	
        }
        
        //identify the exactly same file
        public boolean isSame(String start, String end, String url){        	
        	StringBuilder sb = new StringBuilder();
        	/*String[] startA = start.split("-");
        	for (int i = 0; i < startA.length; i++){        		
        		sb.append(startA[i]);
        	}
        	int startVal = -1;
        	try {
        		startVal = Integer.parseInt(sb.toString());
        	} catch (Exception e){
        		System.out.println("wrong job entry");
        		return false;
        	}
        	if (startVal < this.beginDate){
        		if (this.map.containsKey(url)){
        			return true;
        		} else {
        			this.map.put(url, true);
        			return false;
        		}
        	}        	
        	sb = new StringBuilder();*/
        	String[] endA = end.split("-");
        	for (int i = 0; i < endA.length; i++){
        		sb.append(endA[i]);
        	}
        	int endVal = -1;
        	try {
        		endVal = Integer.parseInt(sb.toString());
        	} catch (Exception e){
        		System.out.println("wrong job entry");
        		return false;
        	}
        	if (map.containsKey(url)){
        		return true;
        	} else {
        		this.map.put(url, true);
    			return false;
        	}
        	//return startVal < endVal;
        }
        
        public int getNumberOfJobs(){
        	return this.jobCounter;
        }
	}  