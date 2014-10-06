import org.json.*;

import java.io.*;
import java.math.BigInteger;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXTransformerFactory;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.ContentHandlerDecorator;
import org.apache.tika.sax.XHTMLContentHandler;
import org.xml.sax.*;

//JsonTableContentHandler transforms XHTML to json files, one file per job entry
public class JsonTableContentHandler extends ContentHandlerDecorator {
	private int jobEntryIndex = 0;
	private String routePrefix;
	private JSONObject json;
	private String[] s;
	private int columnNameIndex;
	private boolean writeValue = false;
	
	public JsonTableContentHandler(String prefix, String[] header){
		s = header;
		routePrefix = new String(prefix);
	}
	
	public int getNumberOfJobEntry(){
		return this.jobEntryIndex;
	}
	
	//@override
	public void startElement(String uri, String localName, String name, Attributes atts) throws SAXException{
		if (name.equals("tr")){			
			json = new JSONObject();
			columnNameIndex = 0;
		} else if (name.equals("td")){
			writeValue = true;
		}
		super.startElement(uri, localName, name, atts);
	}
	
	//@override
	public void characters(char[] ch, int start, int length) throws SAXException{
		if (writeValue){
			String tmp = new String(ch, start, length);
			try {
				if (columnNameIndex == 20){
					columnNameIndex = 0;
				}
				if (s[columnNameIndex].equals("index")){
					tmp = "" + jobEntryIndex;
				}
				json.append(s[columnNameIndex], tmp);
				columnNameIndex++;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.characters(ch, start, length);
	}

	//@override
	public void endElement(String uri, String localName, String name) throws SAXException{
		if (name.equals("tr")){
			String route = routePrefix + jobEntryIndex + ".json";
			jobEntryIndex++;
			try {
				PrintWriter writer = new PrintWriter(route, "UTF-8");
				writer.println(json.toString());
				writer.close();
				//generateSimHash();
			}
			catch (Exception e){
				e.printStackTrace();
			}
		} else if (name.equals("td")){
			writeValue = false;
		}
		super.endElement(uri, localName, name);
	}
}
