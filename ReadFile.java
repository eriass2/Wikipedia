import java.io.*;
import java.util.ArrayList;

public class ReadFile {
	
	public static ArrayList<String> linkRad = new ArrayList<String>();
	public static ArrayList<String> links = new ArrayList<String>();
	
	public static void main(String[] args){
		
		//readArticle("mälaren.txt",34);
		readArticle("mälaren.txt");
		
		
		for(String s : links){
			System.out.println(s);
		}
	}
	
	public static void readArticle(String path, double stopAtRow){
		
		double currentRow = 0;
		
		try{
			// Open file
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));
	
			String strLine;
	
			//Read File Line By Line
			while ((strLine = br.readLine()) != null && currentRow<stopAtRow)   {
				
				//Check if line contains link
				if(strLine.contains("[["))
					//If line contains link, add to list
					linkRad.add(strLine);
				
				currentRow++;
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
		
		fetchLinks(linkRad);
		
	}
	
public static void readArticle(String path){
		
		try{
			// Open file
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));
	
			String strLine;
	
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				
				//Check if line contains link
				if(strLine.contains("[["))
					//If line contains link, add to list
					linkRad.add(strLine);
				
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
		
		fetchLinks(linkRad);
		
	}
	
	public static void fetchLinks(ArrayList<String> linkList){
		
		String temp = "";
		boolean addChar = false;
		
		for(String str : linkList){
			
			for(int i=0; i < str.length(); i++){
				
				if(str.charAt(i) == '['){
					addChar = true;
				}
				else if(str.charAt(i) == ']' || str.charAt(i)== '|'){
					addChar = false;
					
					if(validLink(temp)){
						links.add(temp);
					}
					temp = "";
				}
				
				if (addChar){
					if(str.charAt(i) != '[' && str.charAt(i) != ' ' && str.charAt(i) != '_')
						temp += str.charAt(i);
				}
			}
		}
	}
	
	public static boolean validLink(String link){
		if(!picLink(link) && link != null && !link.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static boolean picLink(String link){
		if(link.startsWith("Bild:")){
			return true;
		}
		else{
			return false;
		}
	}
	
}