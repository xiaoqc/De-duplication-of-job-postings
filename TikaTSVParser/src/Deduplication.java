import java.io.*;
import java.math.BigInteger;
import java.util.*;

public class Deduplication {
	private BufferedReader br;
	private PrintWriter writer;
	private int uniqueFileCounter = 0;
	private SimHash sim = new SimHash();
	private Map<Integer, LinkedList<BigInteger>> map;
	private int threshold = 3;
	
	public Deduplication(String in, String out){
		try{
			File file = new File(in);
			InputStream is = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(is));
			writer = new PrintWriter(out);
			map = new HashMap<Integer, LinkedList<BigInteger>>();
		} catch (Exception e){
			e.printStackTrace();
		}		
	}
	
	public int runDeduplication(){
		String line = "";
		int fingerPrintCount = 0;
		try {
			while ((line = br.readLine()) != null){
				BigInteger finger = new BigInteger(line);
				List<Integer> sections = splitFingerPrint(finger);
				if (checkUniqueness(finger, sections)){
					this.uniqueFileCounter++;
					writer.println(fingerPrintCount + ".json");
				}
				fingerPrintCount++;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return this.uniqueFileCounter;
	}
	
	public boolean checkUniqueness(BigInteger finger, List<Integer> sections){
		Set<BigInteger> set = new HashSet<BigInteger>();
		List<LinkedList<BigInteger>> lists = new ArrayList<LinkedList<BigInteger>>();
		List<Integer> key = new ArrayList<Integer>();
		for (int i = 0; i < sections.size(); i++){
			if (map.containsKey(sections.get(i))){
				LinkedList<BigInteger> list = map.get(sections.get(i));
				for (BigInteger bi : list) {
					if (sim.hammingDistance(bi, finger) < this.threshold){
						return false;
					} else {
						set.add(bi);
					}
				} 
				lists.add(list);
			} else {
				key.add(sections.get(i));
			}
		}
		for (int i = 0; i < lists.size(); i++){
			lists.get(i).add(finger);
		}
		for (int i = 0; i < key.size(); i++){
			LinkedList<BigInteger> tmp = new LinkedList<BigInteger>();
			tmp.add(finger);
			map.put(key.get(i), tmp);
		}
		return true;
	}
	
	public List<Integer> splitFingerPrint(BigInteger finger){
		List<Integer> rst = new ArrayList<Integer>();
		String binaryFinger = finger.toString(2);
		for (int i = 0; i < 4; i++){
			String tmp = binaryFinger.substring(i * 4, (i + 1) * 4);
			int val = Integer.parseInt(tmp, 2);
			rst.add(val);
		}
		return rst;
	}
}
