
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

public class ReadFile {

    /**
     *
     */
    public static TreeSet<String> articles = new TreeSet<>();
    public static HashMap<String, Integer> missing = new HashMap<>();

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        //Gui med fil väljare
        //Progression feedback
        //String som samma objekt, cool grej som kan lösa minne problemt
        getArticles("D:enwiki-latest-pages-meta-current.xml");
        readArticle("D:enwiki-latest-pages-meta-current.xml", 0x5f5e100);
        //getArticles("G:Google Drive/Wikipedia/wikipedia-master/svwiki-latest-pages-meta-current.xml");
        //readArticle("G:Google Drive/Wikipedia/wikipedia-master/svwiki-latest-pages-meta-current.xml", 0x5f5e100);
        saveListToFile();
        //readArticle2();

        //saveListToFile(links);
    }

    public static void linkMissing(String link) {
        if (link.length() > 0 && link.charAt(link.length() - 1) == ' ') {
            link = link.substring(0, link.length() - 1);
        }
        if (link.startsWith(" ")) {
            link = link.substring(1);
        }
        if (!articles.contains(link)) {
            if (missing.containsKey(link)) {
                int g = missing.get(link);
                missing.replace(link, g, g + 1);
            } else {
                missing.put(link, 1);
            }
        }

    }

    public static void readArticle(String path, double stopAtRow) {

        double currentRow = 0;

        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"))) {
                String strLine;
                boolean artikelrymden = false;
                
                //Read File Line By Line
                while ((strLine = br.readLine()) != null && currentRow < stopAtRow) {
                    String temp = strLine;
                                        
                    if (strLine.startsWith("    <title>")) {                        
                        strLine = br.readLine().replaceAll("\\s+", "");
                        artikelrymden = strLine.equals("<ns>0</ns>");
                    }

                    if (artikelrymden) {
                        //Check if line contains link
                        if (temp.contains("[[")) //If line contains link, add to list
                        {

                            fetchLinks(temp.toLowerCase());
                        }

//                        currentRow++;
//                        if (currentRow % 100000 == 0) {
//                            System.out.println(currentRow / 100000 + " " + strLine);
//                        }
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

    public static void getArticles(String path) throws InterruptedException {
//läs artikel
        //http
        //
        String tempRow = "";
        String strLine = "";
        double currentRow = 0;
        double articalAdded = 0;
        int oneHundredK =0;

        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            //Read File Line By Line
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"),20000)) {
                //Read File Line By Line
                while ((strLine = br.readLine()) != null) {
                    
                    //Check if line contains name of article (<title>)
                    if (strLine.startsWith("    <title>")) {
                        tempRow = strLine;

                        strLine = br.readLine().replaceAll("\\s+", "");

                        if (strLine.equals("<ns>0</ns>")) {
                            tempRow = tempRow.replace("    <title>", "");
                            tempRow = tempRow.replace("</title>", "");

                            tempRow = tempRow.replace('_', ' ');                            
                            articles.add(tempRow.toLowerCase());
                            articalAdded++;
                        }
                    }
//                    oneHundredK++;
//                    if (oneHundredK == 100000) {
//                        oneHundredK = 0;
//                        currentRow+=100000;
//                        System.out.println(currentRow / 100000 + " " + articalAdded );
//                    }
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

        System.out.println("Antal hittade artiklar :" + articles.size() + "\n Antal rader lästa :" + currentRow);
        Thread.sleep(30000);
    }

    public static void saveListToFile() {

        int fileVersion = 0;
        boolean fileExists = true;

        try {

            File theFile;

            // if file doesnt exists, then create it
            while (fileExists) {

                theFile = new File("newList" + fileVersion + ".txt");

                if (theFile.exists()) {
                    fileVersion++;
                } else {

                    fileExists = false;

                    theFile.createNewFile();
                    int articlesSize = articles.size();
                    articles.clear();
                    FileWriter fw = new FileWriter(theFile.getAbsoluteFile());
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        missing = (HashMap<String, Integer>) sortByComparator(missing, false);
                        Iterator it = missing.entrySet().iterator();
                        bw.write("Antal hittade artiklar :" + articlesSize + "\n Antal saknade artiklar :" + missing.size() + "\r\n");
                        int i = 0;
                        while (it.hasNext()&& i<=1000) {
                            Map.Entry pair = (Map.Entry) it.next();
                            String text = ("#[[" + pair.getKey() + "]] :" + pair.getValue());                            
                            bw.write(text + "\r\n");
                            it.remove();
                            i++;
                        }
                    }

                    System.out.println("File saved.");

                }
            }

        } catch (IOException e) {
            System.err.println("Save is very error.");
        }

    }

    public static void fetchLinks(String link) {

        String temp = "";
        boolean addChar = false;
        

        for (int i = 0; i < link.length(); i++) {

            if (link.charAt(i) == '[') {
                addChar = true;
            } else if (link.charAt(i) == ']' || link.charAt(i) == '|') {
                addChar = false;

                if (validLink(temp)) {
                    linkMissing(temp);
                }
                temp = "";
            }

            if (addChar) {
                if (link.charAt(i) != '[' && link.charAt(i) != '_') {
                    temp += link.charAt(i);
                }else if(link.charAt(i) == '_'){
                    temp += ' ';
                }               
            }
            
        }
    }
    
     public static void sampelCreater() {

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

            // if file doesnt exists, then create it
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

    public static boolean validLink(String link) {
        return !templateLink(link) && !containsHashtag(link) && !languishLink(link) && link != null && !link.isEmpty();
    }


    
    public static boolean templateLink(String link) {
        return (link.startsWith("mall:")||link.equals(" ")||link.contains("{{"));
    }
    
    public static boolean languishLink(String link) {
        return (link.contains(":")|| link.contains("\\") ||link.contains("&") || link.contains("''") || link.contains("!") || link.contains("=")); 
    }
    
    public static boolean colonLink(String link) {
        return (link.startsWith(":"));
    } 
    
    public static boolean containsHashtag(String link) {
        return ((link.contains("#"))||(link.contains("%23")));
    }

//http://stackoverflow.com/questions/8119366/sorting-hashmap-by-values
private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (Entry<String, Integer> o1, Entry<String, Integer> o2) -> {
            if (order)
            {
                return o1.getValue().compareTo(o2.getValue());
            }
            else
            {
                return o2.getValue().compareTo(o1.getValue());
                
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public static void printMap(Map<String, Integer> map)
    {
        for (Entry<String, Integer> entry : map.entrySet())
        {
            System.out.println("Key : " + entry.getKey() + " Value : "+ entry.getValue());
        }
    }
}
