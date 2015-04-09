public class ValidLinks {

	String user, userD, template, templateD;

	public ValidLinks(String L){
		switch(L){
			case "sv":
				user = "Användare";
				userD = "Användare Diskussion";
				template = "mall:";
				templateD = "Mall Diskussion:";
			break;
			case "en":
				user = "User";
				userD = "User Discussion";
				template = "template:";
				templateD = "Template Discussion:";
			break;
			case default:
				throw NoSuchElementException("No such language exists.");
			break;
		}
		//Switch
		
	}

	public boolean checkLink(String link){
		return (link.startsWith(teamplate)||link.startsWith(teamplateD)||link.equals(" ")||link.contains("{{"));
	}

}