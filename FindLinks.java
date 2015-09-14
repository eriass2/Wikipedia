import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

public class FindLinks {

    /**
     *
     */
    public TreeSet<String> articles = new TreeSet<>();
    public HashMap<String, Integer> missing = new HashMap<>();
    private ArrayList<String> finalSet = new ArrayList<>();
    
    private String lang;
    
    
    public ArrayList<String> getRedLinks(String path, String l) throws InterruptedException {
    	
    	lang = l;
    	
    	readArticle(path, lang);
        saveListToFile();
    	
    	return finalSet;
    }
    

    /**
     * Takes a link and checks if it already exists in article, otherwise it is
     * deemed missing and checked if it exists in list "missing" thereafter
     * added to list "missing" and amount increased by 1.
     * @param link
     */
    public void linkMissing(String link) {
        if (link.length() > 0 && link.charAt(link.length() - 1) == ' ') {//Removes last empty space.
            link = link.substring(0, link.length() - 1);
        }
        if (link.startsWith(" ")) {//Removes first empty space
            link = link.substring(1);
        }
//        if(link.contains("&amp;")){
//            link = link.replace("&amp;", "&");
//        }
        if(link.contains("&ndash;")){
            link = link.replace("&ndash;", "-");
        }
        link = link.replace("&ndash", "-");
        if (!articles.contains(link)) {
            if (missing.containsKey(link)) {
                int g = missing.get(link);
                missing.replace(link, g, g + 1);
            } else {
                missing.put(link, 1);
            }
        }

    }

    public void addArticle(String link) {
        link = link.replace("    <title>", "");
        link = link.replace("</title>", "");
        link = link.replace('_', ' ');//Replace underscore with blank space
        articles.add(link.toLowerCase());//Changes article names to only contain lower case letters
        if (missing.containsKey(link)) {
            missing.remove(link);
        }
    }

    /**
     * Reads the target file to find links inside the articles, which are then
     * sent to be verified by fetchLinks()
     * @param path
     * @param lang
     */
    public void readArticle(String path, String lang) {

        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"))) {
                String strLine;
                boolean nameSpace = false;
                boolean newArticle = false;
                String title = "";

                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {

                    if (strLine.startsWith("    <title>")) {
                        title = strLine.toLowerCase();
                        strLine = br.readLine().replaceAll("\\s+", "");
                        nameSpace = strLine.equals("<ns>0</ns>");
                        newArticle = true;
                    }

                    if (nameSpace) {
                        if (newArticle) {
                            newArticle = false;
                            addArticle(title);
                        }
                        //Check if line contains link
                        if (strLine.contains("[[")) //If line contains link, add to list
                        {
                            fetchLinks(strLine.toLowerCase(), lang);
                        }

                    }

                }
                //Close the input stream
            }

        } catch (FileNotFoundException e) {
            System.err.println("File is very missing.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("File is very error.");
            System.exit(0);
        }

    }

     /*
     * Saves sorted list of wikityped text to file
     */
    public void saveListToFile() {
    	
    	missing = (HashMap<String, Integer>) sortByComparator(missing, false);
        Iterator it = missing.entrySet().iterator();
        RedLink.area.append("Articles found :" + articles.size() + "\n Missing articles :" + missing.size() + "\r\n");
        int i = 0;
        while (it.hasNext() && i <= 1000) {
            Map.Entry pair = (Map.Entry) it.next();
            String text = ("#[[" + pair.getKey() + "]] :" + pair.getValue());
            finalSet.add(text);
            it.remove();
            i++;
        }
    	
    }

    /*
     *Validates links
     */
    public void fetchLinks(String link, String lang) {

        String temp = "";
        boolean addChar = false;
        ValidLinks vd = new ValidLinks(lang);

        for (int i = 0; i < link.length(); i++) {

            if (link.charAt(i) == '[') {
                addChar = true;
            } else if (link.charAt(i) == ']' || link.charAt(i) == '|') {
                addChar = false;

                if (vd.checkLink(temp)) {
                    linkMissing(temp);
                }
                temp = "";
            }

            if (addChar) {
                if (link.charAt(i) != '[' && link.charAt(i) != '_') {
                    temp += link.charAt(i);
                } else if (link.charAt(i) == '_') {
                    temp += ' ';
                }
            }

        }
    }

    /**
     * Creates a short copy of source file for debug.
     */
    public void sampleCreator() {

        double currentRow = 0;
        double target = 100000;
        ArrayList<String> articles2 = new ArrayList<>();

        try {
            // Open file
            FileInputStream fstream = new FileInputStream("D:enwiki-latest-pages-meta-current.xml");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"))) {
                String strLine = null;

                //Read File Line By Line
                while (currentRow < target) {

                    articles2.add(br.readLine());

                    currentRow++;
                    if (currentRow % 100000 == 0) {
                        System.out.println(currentRow / 100000 + " " + strLine);
                    }
                }
            }

                //Close the input stream
        } catch (FileNotFoundException e) {
            System.err.println("File is very missing.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("File is very error.");
            System.exit(0);
        }

        int fileVersion = 0;
        boolean fileExists = true;

        try {

            File theFile;

            // if file doesn't exists, then create it
            while (fileExists) {

                theFile = new File("newList" + fileVersion + ".txt");

                if (theFile.exists()) {
                    fileVersion++;
                } else {

                    fileExists = false;

                    theFile.createNewFile();

                    FileWriter fw = new FileWriter(theFile.getAbsoluteFile());
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        for (String s : articles2) {
                            bw.write(s + "\r\n");
                        }
                    }

                    System.out.println("File saved.");

                }
            }

        } catch (IOException e) {
            System.err.println("Save is very error.");
        }

    }

//http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
    private Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {

        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (Entry<String, Integer> o1, Entry<String, Integer> o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());

            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
