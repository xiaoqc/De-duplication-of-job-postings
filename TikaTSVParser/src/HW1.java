public class HW1 {
	public static void main(String[] args){
		HW1 hw1 = new HW1();
	}
	public HW1(){
		try {
			//first parameter is inputFile and second one is outputFile
			//the output file should delete the title in order to run in broswer
			TestTika t = new TestTika("C:\\Users\\chen\\Desktop\\xx.tsv", "C:\\Users\\chen\\Desktop\\xx.html");
		} catch(Exception e){
			
		}
	}
}
