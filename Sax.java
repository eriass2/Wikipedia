import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.TreeSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;


public class Sax  extends DefaultHandler{

	private String tempVal;
	private String XMLpath;
	private String lang;
	private TreeSet<String> articles = new TreeSet<String>();
	private ArrayList<String> finalSet;
	private ArrayList<String> linkBatch;
	private Connection c = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String tempStr;




	public ArrayList<String> getArticles(String path, String u, String p, String l){
		lang = l;
		doDBConnection(u,p);
		XMLpath = path;
		parseDocument();
		
		
		try{
			c.close();
			stmt.close();
			rs.close();
		}catch(Exception e){}
		
		return finalSet;
	}

	private void parseDocument() {

		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the file and also register this class for call backs
			sp.parse(XMLpath, this);

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	/**
	 * Iterate through the list and print
	 * the contents
	 */


	//Event Handlers
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		//reset
		tempVal = "";
		
		if(qName.equalsIgnoreCase("title")){

		}
		
		if(qName.equalsIgnoreCase("text")){

			}
	
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
		tempStr+=tempVal;
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {

		if(qName.equalsIgnoreCase("title")){
			try{
				stmt = c.createStatement();

				String sql = "INSERT INTO Wiki VALUES ('" + tempVal +"')";
				stmt.executeUpdate(sql);

			}catch(Exception e){
				RedLink.area.append(e + "\n");
			}
		}
		if(qName.equalsIgnoreCase("text")){
			String link = tempStr;

			String temp = "";
			boolean addChar = false;
			ValidLinks vd = new ValidLinks(lang);

			for (int j = 0; j < link.length(); j++) {

				if (link.charAt(j) == '[') {
					addChar = true;
				} else if (link.charAt(j) == ']' || link.charAt(j) == '|') {
					addChar = false;

					if (vd.checkLink(temp.toLowerCase())) {
						addLinkInDB(temp);
					}
					temp = "";
				}

				if (addChar) {
					if (link.charAt(j) != '[' && link.charAt(j) != '_') {
						temp += link.charAt(j);
					} else if (link.charAt(j) == '_') {
						temp += ' ';
					}
				}
			}
		}

	}

	public void addLinks(){

	}

	public void doDBConnection(String user, String pwd){
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager
					.getConnection("jdbc:postgresql://localhost:5432/Test",
							user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
			RedLink.area.append(e.getClass().getName()+": "+e.getMessage()+"\n");
		}
		RedLink.area.append("Opened database successfully!\n");
		//Fånga PSQLException om man skriver fel lösenord
	}
	
	public void addLinkToBatch(String link){
		if(linkBatch.size()==100){
			addBatchInDB();
		}
		
		linkBatch.add("INSERT INTO wikilinks (name) VALUES ('" + link + "')");
	}
	
	public void addBatchInDB(){
		try{
			stmt = c.createStatement();
					
			for (String query : linkBatch) {
				stmt.addBatch(query);
			}
			stmt.executeBatch();
			linkBatch.clear();
			
			stmt.close();
		}catch(Exception e){}
	}
	
	public void addLinkInDB(String link){
		try{
			stmt = c.createStatement();
			stmt.executeUpdate("INSERT INTO Wikilinks VALUES ('" + link + "')");
			
			/*
			//Checks if temp exists in table, and if so, add 1 to it's occurrence value
			int rows = 0;
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT COUNT (*) AS rowcount FROM Wikilinks WHERE name='" + link + "'");
			
			if (rs.next())
		        rows = rs.getInt("rowcount");
			
			if(rows<1){
				stmt = c.createStatement();
				stmt.executeUpdate("INSERT INTO Wikilinks VALUES ('" + link + "',1)");
			}
			else{
				int occ = 0;
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT occurrence FROM Wikilinks WHERE name='" + link + "'");
				rs.next();
				occ = rs.getInt("occurrence") + 1;
				
				stmt.executeUpdate("UPDATE Wikilinks SET occurrence=" + occ + " WHERE name='" + link +"'");
				
			}
			*/
			
		}catch(Exception e){}
	}
	
	
	public void findMissingArticles(){
		try{
			stmt = c.createStatement();
			rs = stmt.executeQuery("SELECT wikilinks.name, COUNT('name') AS 'occurrence' FROM wikilinks GROUP BY 'name' ORDER BY 'occurrence' ASC");
			
			while(rs.next()){
				stmt.executeUpdate("INSERT INTO freqlinks VALUES ('" + rs.getString("name") + "', " + rs.getInt("occurrence"));
			}
			
			/*
		stmt = c.createStatement();
		rs = stmt.executeQuery("SELECT * FROM Wikilinks ORDER BY 'Occurrence' ASC");
		
		while(rs.next()){
			stmt.executeUpdate("INSERT INTO freqlinks VALUES ('" + rs.getString("name") + "', " + rs.getInt("occurrence"));
		}
		
		rs = stmt.executeQuery("SELECT * FROM freqlinks WHERE 'name' NOT IN (SELECT 'name' FROM articles)");
		
		finalSet = new ArrayList<String>(articles);
		
		while(rs.next()){
			finalSet.add(rs.getString("name") + "  "  + rs.getInt("occurrence"));
		}
		*/
		}catch(Exception e){}
		
			/*Jämför sen med artikeltabellen och ta bort värden som förekommer i båda
			
			SELECT  (SELECT * 
			FROM    Call
			WHERE   phone_number NOT IN (SELECT phone_number FROM Phone_book))
			
			FROM    Call
			WHERE   phone_number NOT IN (SELECT phone_number FROM Phone_book)
			
			Stoppa värdena som är kvar i en arraylist och skicka till print.
		*/
	}

}