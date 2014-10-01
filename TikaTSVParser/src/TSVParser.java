import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Set;

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
        private Set<MediaType> SUPPORTED_TYPES = Collections.singleton(MediaType.application("tsv"));
        public static final String TSV_MIME_TYPE = "application/tsv";        
        public Set<MediaType> getSupportedTypes(ParseContext context) {
        	return SUPPORTED_TYPES;
        }

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