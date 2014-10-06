import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
//store finger print and calculate hamming distance
class SimHashTable {
	//define the length of hashbits and the length of key in hashmap
	int hashbits = 64;
	int keyLen = 16;
	int numOfSplit = 4;
	HashMap<Integer, LinkedList<BigInteger>> hash;
	//setting the threshold for the hammingDistance if the distance is smaller than the threshold then it is a near duplicate
	private int threshold = 7;
	
	public SimHashTable(){
		hash = new HashMap<Integer, LinkedList<BigInteger>>();
	}
	
	public boolean contains(BigInteger a){
		//Convert the input number to binary format;
		String str = a.toString(2);
		//System.out.println(str + " length: " + str.length());
		int diff = this.hashbits - str.length();
		if (diff > 0){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < diff; i++){
				sb.append("0");
			}
			sb.append(str);
			str = sb.toString();
		}
		//Split it to 4 substring, each has 16bit length;
		int[] keys = new int[this.numOfSplit];
		for (int i = 0; i < this.numOfSplit; i++) {
			int keyVal = Integer.parseInt(str.substring(i * keyLen, (i + 1) * keyLen), 2);
			keys[i] = keyVal;
			if (hash.containsKey(keyVal)) {
				for (int j = 0; j < hash.get(keyVal).size(); j++){
					if (hammingDistance(hash.get(keyVal).get(j), a) < this.threshold){
						return true;
					}
				}
			}
		}
		for (int i = 0; i < this.numOfSplit; i++){
			if (!hash.containsKey(keys[i])){
				hash.put(keys[i], new LinkedList<BigInteger>());
			}
			hash.get(keys[i]).add(a);
		}
		return false;
		
		
		
		/*
		String[] strs = new String[4];
		int index = 0;
		for (int i = 0; i < hashbits; i += keyLen) {
			strs[index] = str.substring(i, i + keyLen);
			index++;
		}
		for (String temp : strs) {
			int keyVal = Integer.parseInt(temp, 2);
			if (hash.containsKey(keyVal)) {
				LinkedList<BigInteger> list = hash.get(keyVal);
				for (BigInteger i : list) {
					//if we found a near duplicate in Linkedlist
					if (hammingDistance(i, a) < threshold) {
						return true;
					}
				}
			}
		}
		//no near duplicates found, add BigInteger to the hashmap
		for (String temp : strs) {
			int keyVal = Integer.parseInt(temp, 2);
			if (hash.containsKey(keyVal)) {
				hash.get(keyVal).add(a);
			} else {
				LinkedList<BigInteger> newList = new LinkedList<BigInteger>();
				newList.add(a);
				hash.put(keyVal, newList);
			}
		}
		return false;*/
	}
	
	//Calculating the Hamming Distance
	public int hammingDistance(BigInteger a, BigInteger b) {
		BigInteger m = new BigInteger("1").shiftLeft(this.hashbits).subtract(
				new BigInteger("1"));
		BigInteger x = a.xor(b).and(m);
		int tot = 0;
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}
}

