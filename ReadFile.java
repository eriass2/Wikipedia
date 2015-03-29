import java.io.*;



public class ReadFile {
	public static void main(String[] args){
		
		try{
			// Open the file
			
			FileInputStream fstream = new FileInputStream("/home/erik/Google Drive/Wikipedia/wikipedia-master/svwiki-latest-pages-meta-current.xml");
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));
			
			
			String strLine;
	
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
			  System.out.println (strLine);
			}
	
			//Close the input stream
			br.close();
		
		}catch(FileNotFoundException e){
			System.err.println("File is very missing.");
			System.exit(0);
		}catch(IOException e){
			System.err.println("File is very error.");
			System.exit(0);
		}
		
	}
}