import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class Sax  extends DefaultHandler{
	
	private String tempVal;

	
	
	
	public Sax(){
		
	}
	
	public void runExample() {
		parseDocument();

	}

	private void parseDocument() {
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {
		
			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			//parse the file and also register this class for call backs
			sp.parse("C:/Users/jacob_000/Google Drive/wiki/wikipedia-master/svwiki-latest-pages-meta-current.xml", this);
			
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
		//if(qName.equalsIgnoreCase("title")) {
			//create a new instance of employee
			
			for(int i = 0; i<attributes.getLength(); i++){
				if(attributes.getQName(i).equalsIgnoreCase("title")){
					System.out.println(attributes.getValue(i));
				}
				
				// System.out.println(attributes.getQName(i) + "q");
				// System.out.println(attributes.getValue(i) + "v");
				// System.out.println(attributes.getURI(i) + "u");
				// System.out.println(attributes.getLocalName(i) + "l");
				
			}
		//}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		tempVal = new String(ch,start,length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {

		
		
	}
	
	public static void main(String[] args){
		Sax spe = new Sax();
		spe.runExample();
	}

}
