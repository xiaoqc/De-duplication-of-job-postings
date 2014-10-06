import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
//generate finger print for json files
public class SimHash {
	private int hashbits = 64;
	private List<Integer> weight;
	private MessageDigest md;
	private MurmurHash murmur;

	public SimHash(List<Integer> w) {
		murmur = new MurmurHash();
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		weight = new ArrayList<Integer>(w);
	}
	
	public SimHash() {
	}
	

	public BigInteger getFingerPrint(String str) {
		int[] v = new int[this.hashbits];
		
		String spliter = "$%^";
		String[] sArray = str.split("$%^");
		for (int i = 0; i < sArray.length; i++){
			String temp = sArray[i];
			int tempWeight = weight.get(i);
			
			long val = murmur.hash64(temp);
			BigInteger t = BigInteger.valueOf(val);
			//BigInteger t = this.hash(temp);
			//BigInteger t = MD5Hash(temp);
			//System.out.println("temp = " + temp+" £º " + t);
			
			
			for (int j = 0; j < this.hashbits; j++) {
				BigInteger bitmask = new BigInteger("1").shiftLeft(j);
				if (t.and(bitmask).signum() != 0) {
					v[j] += tempWeight;
					//v[j] += 1;
				} else {
					v[j] -= tempWeight;
					//v[j] -= 1;
				}
			}
		}
		BigInteger fingerprint = new BigInteger("0");
		for (int i = 0; i < this.hashbits; i++) {
			if (v[i] >= 0) {
				fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
			}
		}
		return fingerprint;
	}
	
	public BigInteger MD5Hash(String source){
		BigInteger rst = null;
		try {
			byte[] buf = source.getBytes("utf-8");			
			byte[] digest = md.digest(buf);
			rst = new BigInteger(digest);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return rst;
	}

	private BigInteger hash(String source) {
		if (source == null || source.length() == 0) {
			return new BigInteger("0");
		} else {
			char[] sourceArray = source.toCharArray();
			BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);
			BigInteger m = new BigInteger("1000003");
			BigInteger mask = new BigInteger("2").pow(this.hashbits).subtract(
					new BigInteger("1"));
			for (char item : sourceArray) {
				BigInteger temp = BigInteger.valueOf((long) item);
				x = x.multiply(m).xor(temp).and(mask);
			}
			x = x.xor(new BigInteger(String.valueOf(source.length())));
			if (x.equals(new BigInteger("-1"))) {
				x = new BigInteger("-2");
			}
			return x;
		}
	}

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