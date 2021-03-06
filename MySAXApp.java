import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import java.io.FileReader;

public class MySAXApp extends DefaultHandler{

	public static void main(String args[]) throws Exception{
		XMLReader xr = XMLReaderFactory.createXMLReader();
		MySAXApp handler = new MySAXApp();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		
            for (String arg : args) {
                FileReader r = new FileReader(arg);
                xr.parse(new InputSource(r));
            }
		
	}
	

	public MySAXApp(){
		super();
	}
	
        @Override
	public void startDocument(){
		System.out.println("Start document");
	}
	
        @Override
	public void endDocument(){
		System.out.println("End document");
	}
	
        @Override
    public void startElement (String uri, String name,
		      String qName, Attributes atts)
	{
	if ("".equals (uri))
	  System.out.println("Start element: " + qName);
	else
	  System.out.println("Start element: {" + uri + "}" + name);
	}
	
	
        @Override
	public void endElement (String uri, String name, String qName)
	{
	if ("".equals (uri))
	  System.out.println("End element: " + qName);
	else
	  System.out.println("End element:   {" + uri + "}" + name);
	}
	
	
        @Override
	public void characters (char ch[], int start, int length){
		System.out.print("Characters:    \"");
		for (int i = start; i < start + length; i++) {
			switch (ch[i]) {
			case '\\':
				System.out.print("\\\\");
			break;
			case '"':
				System.out.print("\\\"");
			break;
			case '\n':
				System.out.print("\\n");
			break;
			case '\r':
				System.out.print("\\r");
			break;
			case '\t':
				System.out.print("\\t");
			break;
			default:
				System.out.print(ch[i]);
			break;
			}
		}
		System.out.print("\"\n");
	}
		
}