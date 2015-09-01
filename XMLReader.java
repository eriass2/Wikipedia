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
        int currentRow = 0;

        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            //Read File Line By Line
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"), 10000)) {	//Kolla upp vad denna buffert g�r
                //Read File Line By Line
                while ((tempRow = br.readLine()) != null && currentRow < 10000) {
                	finalSet.add(tempRow);
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
        
        
        RedLink.area.append("Scanned rows :" + currentRow + "\n");	//Message
        return finalSet;
    }

}
