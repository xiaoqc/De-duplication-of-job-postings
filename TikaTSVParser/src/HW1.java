public class HW1 {
	public static void main(String[] args){
		HW1 hw1 = new HW1();
	}
	public HW1(){
		try {
			//first parameter is tsv inputFile and second one is outputFile of XHTML, third parameter is the directory for json files, one file per job entery
			//the output XHTML file should delete the title tag in order to run in browser
			TestTika t = new TestTika("C:\\Users\\chen\\Desktop\\computrabajo-ar-20121106.tsv", "C:\\Users\\chen\\Desktop\\bufXHTML.html", "C:\\Users\\chen\\Desktop\\�̲���\\2014 fall\\572\\assignment1\\jobEntry\\");
		} catch(Exception e){
			
		}
	}
}
