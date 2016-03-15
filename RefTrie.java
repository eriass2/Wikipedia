import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.stream.Stream;




import trie.StringRadixTreeMap;

public class RefTrie {

	private TreeMap<String, Integer> articles = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> missing = new TreeMap<String, Integer>();
	private ArrayList<String> finalSet = new ArrayList<String>();
	private int anr = 0;

	private String lang;

	public ArrayList<String> getRedLinks(String path, String l)
			throws InterruptedException {

		lang = l;

		readArticle(path, lang);
		RedLink.area.append("Articles found :" + articles.size()
				+ "\n Missing articles :" + missing.size() + "\r\n");
		articles.clear();
		saveListToFile();
		Collections.reverse(finalSet);
		return finalSet;
	}

	public void linkMissing(String link) {
		if (link.length() > 0 && link.charAt(link.length() - 1) == ' ') {// Removes
			// last
			// empty
			// space.
			link = link.substring(0, link.length() - 1);
		}
		if (link.startsWith(" ")) {// Removes first empty space
			link = link.substring(1);
		}

		if (link.contains("\u2013")) {
			link = link.replace("\u2013", "-");
		}
                if (link.contains("\u00a0")){
                    link = link.replace("\u00a0", "");
                }

		if (!articles.containsKey(link.toLowerCase())) {
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
		link = link.replace('_', ' ');// Replace underscore with blank space
		articles.put(link.toLowerCase(), 1);
		System.out.println(anr++);
	}

	/**
	 * Reads the target file to find links inside the articles, which are then
	 * sent to be verified by fetchLinks()
	 * 
	 * @param path
	 * @param lang
	 */
	public void readArticle(String path, String lang) {

		try {
			// Open file
			FileInputStream fstream = new FileInputStream(path);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
					fstream, "UTF8"))) {
				String strLine;
				boolean nameSpace = false;
				boolean newArticle = false;
				String title = "";

				// Read File Line By Line
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
							fetchLinks(strLine, lang);
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

	public void fetchLinks(String link, String lang) {

		String temp = "";
		boolean addChar = false;
		ValidLinks vd = new ValidLinks(lang);

		for (int i = 0; i < link.length(); i++) {

			if (link.charAt(i) == '[') {
				addChar = true;
			} else if (link.charAt(i) == ']' || link.charAt(i) == '|') {
				addChar = false;

				if (vd.checkLink(temp.toLowerCase())) {
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

	public void saveListToFile() {
		System.out.println("Fixar text");

		Map temp = sortByValue(missing);
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
		Stream<Entry<K, V>> st = map.entrySet().stream();

		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(
				e -> result.put(e.getKey(), e.getValue()));

		return result;
	}
}
