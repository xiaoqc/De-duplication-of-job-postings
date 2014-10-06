import java.io.*;
import java.math.BigInteger;
import java.util.*;
//take finger print of files and check the near duplicates
public class Deduplication {
	private PrintWriter writer;
	private int uniqueFileCounter = 0;
	private SimHashTable map = new SimHashTable();
	
	public Deduplication(String out){
		try{
			writer = new PrintWriter(out);
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	public int runDeduplication(List<BigInteger> FP){
		int fingerPrintCount = 0;
		try {
			for (int i = 0; i < FP.size(); i++){
				BigInteger finger = FP.get(i);				
				
				if (!map.contains(finger)){
					this.uniqueFileCounter++;
					writer.println(fingerPrintCount + ".json");
				}
				fingerPrintCount++;
				if (i % 100000 == 0){
					System.out.println(i + "job entries are compared");
				}
			}
			writer.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		return this.uniqueFileCounter;
	}
}
