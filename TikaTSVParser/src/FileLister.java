

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//list the directory's file names
public class FileLister {
	private String directory_path;
	private String filename;
	private File folder;
	private File[] list;
	private File out;
	
	/*@param path  the directory that contains files*/
	public FileLister(String path, String filename){
		this.filename = filename;
		this.directory_path = path;
		folder = new File(directory_path);
		list = folder.listFiles();
	}
	
	/*@param filename  output file name that contains filenames
	 *@return output file*/
	public File getFileNameFile() throws IOException{
		out = new File(filename);
		FileWriter writer = new FileWriter(out);
		for(File f : list){
			if(f.isFile()){
				writer.write(f.getName() + "\n");
			}
		}
		writer.close();
		return out;
	}
	
	/*Print all names of files*/
	public void print(){
		for(int i = 0; i < list.length; i++){
			if(list[i].isFile()){
				System.out.println(list[i].getName());
			}
		}
	}
	
	/*Print all names of files and directories*/
	public void printAll(){
		for(File f : list){
			System.out.println(f.getName());
		}
	}
	
	public int size(){
		return list.length;
	}
	
}
