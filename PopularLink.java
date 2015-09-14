

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import trie.StringRadixTreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Assar
 */
public class PopularLink {

    private StringRadixTreeMap<Integer> catagory = new StringRadixTreeMap<>();
    private ArrayList<String> finalSet = new ArrayList<>();
    private String lang;
    private String target ="Korea";
    private String ns;
    private int anr = 0;

    public ArrayList<String> getRedLinks(String path, String l)
            throws InterruptedException {

        lang = l;
        
        if(null != lang)switch (lang) {
            case "en":
                ns = "[[Category:";
                break;
            case "sv":
                ns = "[[Kategori:";
                break;
        }

        readArticle(path, lang);

        saveListToFile();

        Collections.reverse(finalSet);
        return finalSet;
    }

    private void readArticle(String path, String lang) {
        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    fstream, "UTF8"))) {
                String strLine;
                boolean nameSpace = false;
                String title = "";

                // Read File Line By Line
                while ((strLine = br.readLine()) != null) {

                    if (strLine.startsWith("    <title>")) {
                        title = strLine.toLowerCase();
                        strLine = br.readLine().replaceAll("\\s+", "");
                        nameSpace = strLine.equals("<ns>0</ns>");
                    }

                    if (nameSpace) {
                        if (strLine.startsWith(ns + target)) {
                            if (strLine.startsWith(ns + target + "]]") || strLine.startsWith(ns + target + "| ]]")) {
                                addArticle(title);
                            }
                        }
                    }

                }
                // Close the input stream
            }

        } catch (FileNotFoundException e) {
            System.err.println("File is very missing.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("File is very error.");
            System.exit(0);
        }
        System.out.println("Artiklar klara, l�nkar b�rjar");
        try {
            // Open file
            FileInputStream fstream = new FileInputStream(path);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    fstream, "UTF8"))) {
                String strLine;
                boolean nameSpace = false;

                // Read File Line By Line
                while ((strLine = br.readLine()) != null) {

                    if (strLine.startsWith("    <title>")) {
                        strLine = br.readLine().replaceAll("\\s+", "");
                        nameSpace = strLine.equals("<ns>0</ns>");
                    }

                    if (nameSpace) {
                        // Check if line contains link
                        if (strLine.contains("[[")) // If line contains link,
                        // add to list
                        {
                            fetchLinks(strLine.toLowerCase(), lang);
                        }

                    }

                }
                // Close the input stream
            }

        } catch (FileNotFoundException e) {
            System.err.println("File is very missing.");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("File is very error.");
            System.exit(0);
        }
    }

    private void addArticle(String link) {
        link = link.replace("    <title>", "");
        link = link.replace("</title>", "");
        link = link.replace('_', ' ');// Replace underscore with blank space
        catagory.put(link.toLowerCase(), 0);
        System.out.println(anr++ + link);

    }

    private void fetchLinks(String link, String lang) {
        String temp = "";
        boolean addChar = false;
        ValidLinks vd = new ValidLinks(lang);

        for (int i = 0; i < link.length(); i++) {

            if (link.charAt(i) == '[') {
                addChar = true;
            } else if (link.charAt(i) == ']' || link.charAt(i) == '|') {
                addChar = false;

//                if (vd.checkLink(temp.toLowerCase())) {
//                    //linkMissing(temp);
//                }
                if (catagory.containsKey(temp)) {
                    int g = catagory.get(temp);
                    catagory.replace(temp, g, g + 1);
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
    
    private void saveListToFile() {
        		System.out.println("Fixar text");

		Map temp = sortByValue(catagory);
		Iterator it = temp.entrySet().iterator();

		int l = temp.size();
		int i = (l - 1000);
		int p = 0;

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			if (p >= i) {
				String text = ("#[[" + pair.getKey() + "]] :" + pair.getValue());
				finalSet.add(text);
			}
			it.remove();
			p++;
		}
    }

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		Map<K, V> result = new LinkedHashMap<>();
		Stream<Map.Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(
				e -> result.put(e.getKey(), e.getValue()));

		return result;
	}
}
