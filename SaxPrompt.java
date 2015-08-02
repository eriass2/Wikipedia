import javax.swing.*;

public class SaxPrompt extends JPanel{

	private JTextField u = new JTextField(10);
	private JPasswordField p = new JPasswordField(10);
	
	public SaxPrompt(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel rad1 = new JPanel();
			rad1.add(new JLabel("Username: "));
			rad1.add(u);
		add(rad1);
		
		JPanel rad2 = new JPanel();
			rad2.add(new JLabel("Password: "));
			rad2.add(p);
		add(rad2);
	}
	
	public String getUser(){
		return u.getText();
	}
	
	public char[] getPass(){
		return p.getPassword();
	}
}