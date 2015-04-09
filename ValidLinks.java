public class ValidLinks {

	String user, uderD, teamplate, teamplateD;

	public ValidLinks(String L){
		//Switch
		user = "Användare";
		userD = "Användare Diskution";
		teamplate = "mall:";
		teamplateD = "Mall Diskution:";
	}

	public boolean cheakLink(String link){
		return (link.startsWith(teamplate)||link.startsWith(teamplateD)||link.equals(" ")||link.contains("{{"));
	}

}