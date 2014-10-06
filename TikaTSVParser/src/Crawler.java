import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;


public class Crawler extends Thread{
	private String[] chosen;
	private SimHash sim;
	private String dir;
	private List<BigInteger> FP;
	private Thread thread;
	private int start;
	private int end;
	private int id;
	private CountDownLatch cdl;
	
	public Crawler(String dir, int[] weight, String[] chosenField, int start, int end, List<BigInteger> fingerSet, int id, CountDownLatch latch){
		this.cdl = latch;
		this.FP = fingerSet;
		this.start = start;
		this.end = end;
		this.id = id;
		this.dir = dir;
		this.chosen = chosenField;
		this.thread = new Thread(this);
		List<Integer> w = new ArrayList<Integer>();
		for (int i = 0; i < weight.length; i++){
			w.add(weight[i]);
		}
		sim = new SimHash(w);
		thread.start();
		System.out.println("Thread " + this.id + " starts");
	}
	
	public void writeFingerPrint(String jsonString){
		try {
			JSONObject json = new JSONObject(jsonString);
			String spliter = "$%^";
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < chosen.length; i++){
				if (i > 0){
					sb.append(spliter);
				}
				sb.append(json.get(chosen[i]).toString());
			}
			BigInteger bi = sim.getFingerPrint(sb.toString());
			FP.add(bi);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void begin() throws IOException{
		String prefix = dir;
		long startTime = 0;
		long endTime = 0;
		startTime = System.currentTimeMillis();
		while(this.start < this.end){
			String content = readFile(prefix + this.start + ".json", StandardCharsets.UTF_8);
			writeFingerPrint(content);
			this.start++;
		}
		endTime = System.currentTimeMillis();
		System.out.println("Thread " + this.id + " finish generate finger print");
		System.out.println("Generate finger print takes " + ((endTime - startTime) / 1000) + " seconds");			
	}
	
	 public String readFile(String path, Charset encoding) throws IOException {
		 byte[] encoded = Files.readAllBytes(Paths.get(path));
		 return new String(encoded, encoding);
	 } 
	 
	 public void run(){
		 try{
			 begin();
		 } catch (Exception e){
			 e.printStackTrace();
		 }
		 cdl.countDown();
	 }
}
