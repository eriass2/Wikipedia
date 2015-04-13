package wikipedia;

import java.util.NoSuchElementException;

public class ValidLinks {

	String user, userT, template, templateT, category, image, file, special, wikipediaT, project, module, help;

    /**
     *
     * @param L
     */
    public ValidLinks(String L){
		switch(L){
			case "sv":
				user = "användare:";
				userT = "användare diskussion:";
				template = "mall:";
				templateT = "mall diskussion:";
                                category = "kategori:";
			break;
			case "en":                                
                                special = "special:";
				user = "user:";
				userT = "user talk:";
				template = "template:";
				templateT = "template talk:";
                                category = "category:";
                                image = "image:";
                                file = "file:";                                
                                wikipediaT = "wikipedia talk:";
                                project = "project:";
                                module = "module:";
                                help = "help:";
			break;
			default:
				throw new NoSuchElementException("No such language exists.");
			
		}
		//Switch
		
	}

    public boolean checkLink(String link) {
        if(link.equals(""))
            return false;
        
        if ((link.contains("#")) || (link.contains("%23")) || (link.contains("//")) || (link.contains("{{")) || (link.equals(" ")) || (link.startsWith("[[")) || (link.startsWith(":"))|| (link.startsWith("\\b"))) {
            return false;
        } else if ((link.startsWith("commons:"))||(link.startsWith("c:"))|| (link.startsWith("wp:")) || (link.startsWith("wikipedia:"))|| (link.startsWith("wiktionary:")) || (link.startsWith("wikt:"))) {
            return false;
        } else if ((link.startsWith("en:"))||(link.startsWith("d:"))||(link.startsWith("w:"))||(link.startsWith("s:"))) {
            return false;
        } else {
            return !(link.startsWith(template) || link.startsWith(templateT) || link.startsWith(user) || link.startsWith(userT) || link.startsWith(category) || link.startsWith(image) || link.startsWith(file)|| link.startsWith(special)|| link.startsWith(wikipediaT)|| link.startsWith(module)|| link.startsWith(help));
        }
    }

}