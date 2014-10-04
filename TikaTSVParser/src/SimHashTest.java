import java.math.BigInteger;
import java.util.StringTokenizer;

public class SimHashTest {

	private String tokens;
	private BigInteger strSimHash;
	private int hashbits = 128;

	public SimHashTest(String tokens) {
		this.tokens = tokens;
		this.strSimHash = this.simHash();
	}
	
	public SimHashTest(String tokens, int hashbits) {
		this.tokens = tokens;
		this.hashbits = hashbits;
		this.strSimHash = this.simHash();
	}

	public BigInteger simHash() {
		int[] v = new int[this.hashbits];
		StringTokenizer stringTokens = new StringTokenizer(this.tokens);
		while (stringTokens.hasMoreTokens()) {
			String temp = stringTokens.nextToken();
			BigInteger t = this.hash(temp);
			System.out.println("temp = " + temp+" £º " + t);
			for (int i = 0; i < this.hashbits; i++) {
				BigInteger bitmask = new BigInteger("1").shiftLeft(i);
				if (t.and(bitmask).signum() != 0) {
					v[i] += 1;
				} else {
					v[i] -= 1;
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

	public int hammingDistance(SimHashTest other) {
		BigInteger m = new BigInteger("1").shiftLeft(this.hashbits).subtract(
				new BigInteger("1"));
		BigInteger x = this.strSimHash.xor(other.strSimHash).and(m);
		int tot = 0;
		while (x.signum() != 0) {
			tot += 1;
			x = x.and(x.subtract(new BigInteger("1")));
		}
		return tot;
	}

	public static void main(String[] args) {
		String s = "China people's Republic of China Chinese China people's Republic of China People's Republic of China";
		SimHashTest hash1 = new SimHashTest(s, 128);
		System.out.println(hash1.strSimHash + "  "
				+ hash1.strSimHash.bitLength());

		s = "China people's Republic of China Chinese China people's Republic of China";
		SimHashTest hash2 = new SimHashTest(s, 128);
		System.out.println(hash2.strSimHash + "  "
				+ hash2.strSimHash.bitCount());

		s = "China people's Republic";
		SimHashTest hash3 = new SimHashTest(s, 128);
		System.out.println(hash3.strSimHash + "  "
				+ hash3.strSimHash.bitCount());

		System.out.println("============================");
		System.out.println(hash1.hammingDistance(hash2));
		System.out.println(hash1.hammingDistance(hash3));
		
		System.out.println("============================");
		System.out.println("============================");
		System.out.println("============================");
		String s4 = "2012-10-23	Capital Federal	Capital Federal	Desarrollador plataforma SalesForce CRM.!!!A convenir	Inmediato	Indeterminada	Tiempo Completo	Enviar Cv con Ref Desarrollador SalesForce CRM	Softtek	Belen Lavinia!!!	Buenos Aires, Argentina	-34.6037232	-58.3815931	2012-10-29	http://www.computrabajo.com.ar/bt-ofrd-softtek-21444.htm	2012-11-06";
		String s5 = "2012-10-24	Capital Federal	Capital Federal	Desarrollador plataforma SalesForce CRM.!!!A convenir	Inmediato	Indeterminada	Tiempo Completo	Enviar Cv con Ref Desarrollador SalesForce CRM	Softtek	Belen Lavinia!!!	Buenos Aires, Argentina	-34.6037232	-58.3815931	2012-10-29	http://www.computrabajo.com.ar/bt-ofrd-softtek-21444.htm	2012-11-06";
		SimHashTest hashs4 = new SimHashTest(s, 64);
		SimHashTest hashs5 = new SimHashTest(s, 64);
		System.out.println(hashs4.strSimHash + "  "+ hashs4.strSimHash.bitCount());
		System.out.println(hashs5.strSimHash + "  "+ hashs5.strSimHash.bitCount());
		System.out.println("============================");
		System.out.println(hashs4.hammingDistance(hashs5));
	}
}