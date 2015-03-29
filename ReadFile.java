import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class ReadFile {
	
	public static ArrayList<String> linkRad = new ArrayList<String>();
	public static ArrayList<String> links = new ArrayList<String>();
	public static HashMap<String, Integer> articles = new HashMap<String, Integer>();
	
	
	public static void main(String[] args){
		
		//readArticle("mälaren.txt",34);
		getArticles("svwiki-latest-pages-meta-current.xml");
		
		//saveListToFile(links);
		
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
	
	public static void getArticles(String path){
		
		String tempRow = "";
		String strLine = "";
		
		try{
			// Open file
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));
			
			//Read File Line By Line
			while ((strLine = br.readLine().replaceAll("\\s+","")) != null)   {
				
				//Check if line contains name of article (<title>)
				if(strLine.startsWith("<title>")){
					tempRow = strLine;
				
					strLine = br.readLine().replaceAll("\\s+","");
					
					if(strLine.equals("<ns>0</ns>")){
						tempRow = tempRow.replace("<title>", "");
						tempRow = tempRow.replace("</title>", "");
						
						if(articles.containsKey(tempRow) && tempRow != null){
							articles.put(tempRow, articles.get(tempRow)+1);
						}
						else{
							articles.put(tempRow, 1);
						}
						
					}
				}
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
		
		for(Entry<String, Integer> entry : articles.entrySet()) {
		    System.out.println(entry.getKey() + " " + entry.getValue());

		    // do what you have to do here
		    // In your case, an other loop.
		}
		
	}
	
	public static void saveListToFile(ArrayList<String> myList){
		
		int fileVersion = 0;
		boolean fileExists = true;
		
		
		try {
			
			File theFile;
 
			// if file doesnt exists, then create it
			while(fileExists){
				
				theFile = new File("newList" + fileVersion + ".txt");
				
				if(theFile.exists()){
					fileVersion++;
				}
				else{
					
					fileExists = false;
					
					theFile.createNewFile();
					
					
					FileWriter fw = new FileWriter(theFile.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					
					for(String s : myList){
						bw.write(s + "\r\n");
					}
					
					bw.close();
					
					System.out.println("File saved.");
					
				}
			}
 
 
		} catch (IOException e) {
			System.err.println("Save is very error.");
		}
		
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