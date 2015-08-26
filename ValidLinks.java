import java.util.NoSuchElementException;

public class ValidLinks {

	//private String user, userT, template, templateT, category, image, file, special, wikipediaT, project, module, help;
	
	private String lang;
	private String[] namespaces_sv = {"diskussion", "anv�ndare", "anv�ndardiskussion","wikipedia", "wikipediadiskussion", "fil", "fildiskussion", "mediawiki", "mediawiki-diskussion", "mall", "malldiskussion", "hj�lp", "hj�lpdiskussion", "kategori", "kategoridiskussion","portal", "portaldiskussion", "utbildningsprogram","utbildningsprogramsdiskussion", "modul", "moduldiskussion", "special", "media", "project", "file", "image"};
	private String[] namespaces_en = {"talk", "user", "user talk", "wikipedia talk", "wikipedia", "file", "file talk", "mediawiki", "mediawiki talk","template", "template talk", "help", "help talk", "category", "category talk", "portal", "portal talk", "book", "book talk", "draft", "draft talk", "education program", "education program talk", "timedtext", "timedtext talk", "module", "module talk", "topic", "special", "media", "project", "tools:", "file", "image", "wt"};
	
	
    /**
     *
     * @param L
     */
    public ValidLinks(String L){
		lang = L;
		
	}

    public boolean checkLink(String link) {
        if(link.equals(""))
            return false;
        
        if ((link.contains("#")) || (link.contains("%23")) || (link.contains("//")) || (link.contains("{{")) || (link.equals(" ")) || (link.startsWith("[[")) || (link.startsWith(":"))|| (link.startsWith("\\b"))|| (link.startsWith("edit="))|| (link.startsWith("move="))){
            return false;
        } else if ((link.startsWith("commons:"))||(link.startsWith("c:"))|| (link.startsWith("wp:")) || (link.startsWith("wikipedia:"))|| (link.startsWith("wiktionary:")) || (link.startsWith("wikt:"))) {
            return false;
        } else if ((link.startsWith("en:"))||(link.startsWith("d:"))||(link.startsWith("w:"))||(link.startsWith("s:"))) {
            return false;
        } else {
        	
        	switch(lang){
			case "sv":
				
				for(int i = 0; i<namespaces_sv.length;i++){
					if(link.startsWith(namespaces_sv[i]+":")){
						return false;
					}
				}
				
			break;
			case "en":                                
				
				for(int i = 0; i<namespaces_en.length;i++){
					if(link.startsWith(namespaces_en[i]+":")){
						return false;
					}
				}
				
			break;
			default:
				throw new NoSuchElementException("Language not supported.");
			
        	}
		//Switch
        }
        return true;
    }
}