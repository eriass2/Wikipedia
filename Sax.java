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


public class Sax  extends DefaultHandler{
	
	private String tempVal;
	private String XMLpath;
	private TreeSet<String> articles = new TreeSet<String>();
	private ArrayList<String> finalSet;
	private Connection c = null;
	private Statement stmt = null;

	
	
	
	public ArrayList<String> getArticles(String path, String u, String p){
		doDBConnection(u,p);
		XMLpath = path;
		//parseDocument();
		finalSet = new ArrayList<String>(articles);
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
			
			for(int i = 0; i<attributes.getLength(); i++){
				if(attributes.getQName(i).equalsIgnoreCase("title")){
					//articles.add(attributes.getValue(i));
					
					try{
						stmt = c.createStatement();
						
						String sql = "INSERT INTO Wiki VALUES ('" + attributes.getValue(i) +"')";
						stmt.executeUpdate(sql);
						
					}catch(Exception e){
						RedLink.area.append(e + "\n");
					}
				}
				else if(attributes.getQName(i).equalsIgnoreCase("text")){
					
					/*
					try{
						stmt = c.createStatement();
						
						String sql = "INSERT INTO WikiLinks VALUES ()";
						stmt.executeUpdate(sql);
						
					}catch(Exception e){}
					*/
				}
			}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		
		
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

}