import java.math.BigInteger;
import java.util.StringTokenizer;

public class SimHash {
	private int hashbits = 64;
	private int[] weight;

	public SimHash(int[] w) {
		weight = w;
	}
	
	public SimHash(int hashbits, int[] w) {
		weight = w;
		this.hashbits = hashbits;
	}
	
	public BigInteger getFingerPrint(String str){
		return simHash(str);
	}

	public BigInteger simHash(String str) {
		int[] v = new int[this.hashbits];
		String[] s = str.split("!@#");
		for (int i = 0; i < s.length; i++){
			String temp = s[i];
			int weightTemp = weight[i];
			BigInteger t = this.hash(temp);
			//System.out.println("temp = " + temp+" £º " + t);
			for (int j = 0; j < hashbits; j++) {
				BigInteger bitmask = new BigInteger("1").shiftLeft(j);
				if (t.and(bitmask).signum() != 0) {
					v[j] += weightTemp;
				} else {
					v[j] -= weightTemp;
				}
			}
		}
		BigInteger fingerprint = new BigInteger("0");
		for (int i = 0; i < hashbits; i++) {
			if (v[i] >= 0) {
				fingerprint = fingerprint.add(new BigInteger("1").shiftLeft(i));
			}
		}
		System.out.println(fingerprint);
		return fingerprint;
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
	/*
	public static void main(String[] args) {
		String s = "China people's Republic of China Chinese China people's Republic of China People's Republic of China";
		SimHash hash1 = new SimHash(s, 128);
		System.out.println(hash1.strSimHash + "  "
				+ hash1.strSimHash.bitLength());

		s = "China people's Republic of China Chinese China people's Republic of China";
		SimHash hash2 = new SimHash(s, 128);
		System.out.println(hash2.strSimHash + "  "
				+ hash2.strSimHash.bitCount());

		s = "China people's Republic";
		SimHash hash3 = new SimHash(s, 128);
		System.out.println(hash3.strSimHash + "  "
				+ hash3.strSimHash.bitCount());

		System.out.println("============================");
		System.out.println(hash1.hammingDistance(hash2));
		System.out.println(hash1.hammingDistance(hash3));
	}
	*/
}