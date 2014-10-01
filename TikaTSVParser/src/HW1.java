import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class HW1 {
	//String fileD = "C:\\Users\\chen\\Dropbox\\TSV\\TSV\\";
	private String fileD = "C:\\Users\\chen\\Desktop\\New folder\\";
	private String bufFileRoute = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment1\\fileNameBuf.txt";
	private String bufXHTML = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment1\\bufXHTML.html";
	private String jobEntryFile = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment1\\jobEntry\\";
	private String simHashFile = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment1\\simHash.txt";
	private String[] header = {"postedDate","location","department","title","salary","start","duration","jobtype","applications","company","contactPerson","phoneNumber","faxNumber","index","Area","latitude","longitude","firstSeenData","url","lastSeenData"};
	private int[] weight = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 20};
	private String[] chosenField = {"postedDate","location","department","title","salary","start","duration","jobtype","applications","company","contactPerson","phoneNumber","faxNumber","Area","latitude","longitude","url"};
	
	public static void main(String[] args){
		HW1 hw1 = new HW1();
	}
	public HW1(){
		try{
			readInputFile();
			TSVToJSON();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//(fireDirectory, bufferedFileRoute), FileLister gets the route of input file directory.
	public void readInputFile() throws IOException{					
		FileLister fList = new FileLister(fileD, bufFileRoute);
		fList.getFileNameFile();
	}
	
	//(route prefix, tsv file name, buffer XHTML output, job entry), TestTika transforms all tsv files to json job entries.		
	public void TSVToJSON() throws FileNotFoundException, TransformerConfigurationException, IOException, SAXException, TikaException, ParserConfigurationException{
		TestTika t = new TestTika(fileD, bufFileRoute, bufXHTML, jobEntryFile, simHashFile, header, weight, chosenField);
	}	
}
