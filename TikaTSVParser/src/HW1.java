import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class HW1 {
	//----------------user needs to set below parameters--------------------------------------------------
	private String fileD = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment1\\employment\\";;
	private String jobEntryFile = "C:\\Users\\chen\\Desktop\\教材们\\2014 fall\\572\\assignment2\\json\\";
	//----------------user needs to set above parameters--------------------------------------------------
	private String uniqueFiles = "unique.txt";
	private String bufFileRoute = "fileNameBuf.txt";
	private String bufXHTML = "bufXHTML.html";
	private String[] header = {"postedDate","department","location","title","id","salary","start","duration","jobtype","applications","company","contactPerson","phoneNumber","faxNumber","Area","latitude","longitude","firstSeenData","url","lastSeenData"};
	private int[] weight = {20, 15, 30, 5, 5, 5, 5, 15, 15, 15, 15, 5, 5};
	private String[] chosenField = {"department","location","title","salary","start","duration","jobtype","applications","company","contactPerson","Area","latitude","longitude"};
	private int numOfThread = 8;
	private int numOfJobEntry;
	
	public static void main(String[] args){
		HW1 hw1 = new HW1(args);
	}
	//-------------------------use need to set parameter of TSVToJSON and crawFile---------------------------------
	public HW1(String[] args){
		if (args.length == 2){
			this.fileD = args[0];
			this.jobEntryFile = args[1];
		}
		try{
			//deleteFile();			
			
			long start = System.currentTimeMillis();
			
			//read the input file and list the file names in it
			readInputFile();       
			
			//-------------------------use need to set parameter of TSVToJSON---------------------------------
			//convert tsv to json jon entries. 
			//The first parameter is to enable the de-duplication of same job postings
			//The second parameter is the start date of input file, such as computrabajo-ar-20121106.tsv, the start data is 20121106
			
			TSVToJSON(true, "20121106");    
			System.out.println("Parse TSV and generate JSON takes " + (System.currentTimeMillis() - start) + " ms");	
			//-------------------------use need to set parameter of TSVToJSON---------------------------------
				
			
			//-------------------------use need to set parameter of crawlFile---------------------------------
			//crawl the created job entries, the parameter is to enable the identify the near duplicates.
			//crawlFile(true);
			//-------------------------use need to set parameter of crawlFile---------------------------------
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void deleteFile(){
		int count = 1300000;
		String prefix = this.jobEntryFile;
		try{
			while (true){
				String route = prefix + count + ".json";
				File file = new File(route);
				if (file.isFile()){
					file.delete();
				} else {
					break;
				}
				count++;
				if (count % 10000 == 0){
					System.out.println(count + ".json is deleted");
				}
			}
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
	public void TSVToJSON(boolean flag, String start) throws FileNotFoundException, TransformerConfigurationException, IOException, SAXException, TikaException, ParserConfigurationException{
		TestTika t = new TestTika(fileD, bufFileRoute, bufXHTML, jobEntryFile, header, flag, start);
		this.numOfJobEntry = t.getJobEntryNumber();
	}	
	
	public void crawlFile(boolean flag){
		if (!flag){
			System.out.println(this.numOfJobEntry + " job entries");
		} else {
			try {
				crawlFileDedulplication();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void crawlFileDedulplication() throws IOException{
		int number = ((this.numOfJobEntry / 10000) + 1) * 10000;
		//int number = 273629;
		int section = number / this.numOfThread;
		List<List<BigInteger>> finger = new ArrayList<List<BigInteger>>();
		for (int i = 0; i < this.numOfThread; i++){
			finger.add(new ArrayList<BigInteger>());
		}
		CountDownLatch latch = new CountDownLatch(this.numOfThread);
		for (int i = 0; i < this.numOfThread; i++){
			int st = i * section;
			int ed = (i + 1) * section;
			if (i == this.numOfThread - 1){
				ed = this.numOfJobEntry;
				//ed = 273629;
			}
			Crawler crawler = new Crawler(jobEntryFile, weight, chosenField, st, ed, finger.get(i), i, latch);
		}
		try {
			latch.await();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		
		System.out.println("Start de-duplication");
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		int uniqueFileCounter = 0;
		Deduplication dedulp = new Deduplication(this.uniqueFiles);
		List<BigInteger> join = new ArrayList<BigInteger>();
		for (int i = 0; i < this.numOfThread; i++){			
			join.addAll(finger.get(i));
		}		
		/*
		PrintWriter writer = new PrintWriter(this.simHashFile);
		for (int i = 0; i < join.size(); i++){
			writer.println(join.get(i));
		}
		writer.close();*/
		
		uniqueFileCounter += dedulp.runDeduplication(join);
		System.out.println("Unique file number is " + uniqueFileCounter);
		
		endTime = System.currentTimeMillis();
		System.out.println("Finish de-duplication");
		System.out.println("De-Dulplication takes " + (endTime - startTime) + " ms");
	}
}
