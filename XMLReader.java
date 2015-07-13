import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class XMLReader {
	
	private ArrayList<String> finalSet = new ArrayList<String>();
	
	public ArrayList<String> getArticles(String path) throws InterruptedException {
		
        String tempRow = "";
        String strLine = "";
        double currentRow = 0;

        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            //Read File Line By Line
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"), 10000)) {	//Kolla upp vad denna buffert g�r
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    
                    //Check if line contains name of article (<title>)
                    if (strLine.startsWith("    <title>")) {
                        tempRow = strLine;

                        strLine = br.readLine().replaceAll("\\s+", "");

                        if (strLine.equals("<ns>0</ns>")) {
                            tempRow = tempRow.replace("    <title>", "");
                            tempRow = tempRow.replace("</title>", "");
							
							//Replace underscore with blank space
                            tempRow = tempRow.replace('_', ' ');
							
							//Changes article names to only contain lower case letters
                            finalSet.add(tempRow.toLowerCase());
                        }
                    }
                    currentRow++;
                }
                //Close the input stream
            }

        } catch (FileNotFoundException e) {
            System.err.println("File could not be found.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Error while reading file.");
            System.exit(0);
        }
        
        
        RedLink.area.append("Articles found:" + RedLink.articles.size() + "\n Scanned rows :" + currentRow);	//Message of amount of articles in namespace
        return finalSet;
    }

}
