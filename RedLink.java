import javax.swing.border.TitledBorder;
import javax.swing.*;
import javax.swing.filechooser.*;

import java.awt.*;
import java.awt.event.*;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static javax.swing.JOptionPane.*;

public class RedLink extends JFrame {

	private String filePath, savePath, caseAlt;
	private JFileChooser fc, fo;
	private File theFile;
	private JButton chooseBtn, saveBtn, runBtn;
	private JComboBox<String> caseCh, langCh;
	private String[] caseLabels = {"", "Red links - most common", "Red links - by category", "10000 first lines"};
	//private String[] caseLabels = {"","Test", "First 10000 rows", "Find red links", "SAX", "Trie","Cat"};		//Note: If adding or removing cases, you must manually change the switch in RunListener to match them.
	private String[] langLabels = {"","English", "Swedish"};
	private String[] languages = {"en", "sv"};
	private JTextField pathField, savePathField;
	private int caseVal, langVal;
	private boolean fileExists;
	private ArrayList<String> testSet = new ArrayList<>();
	public static JTextArea area;
	public static TreeSet<String> articles = new TreeSet<>();

	public RedLink() {
		super("RedLink District 1.6.2");
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
		
	
		
		populate();
		
		TitledBorder tit1 = new TitledBorder("1. -  Choose XML-file");
		TitledBorder tit2 = new TitledBorder("2. -  Choose target folder for generated file");
		TitledBorder tit3 = new TitledBorder("3. -  Choose algorithm");
		TitledBorder tit4 = new TitledBorder("4. -  Choose database language");
		TitledBorder tit5 = new TitledBorder("5. -  Run");

		JPanel norr = new JPanel();
		norr.setLayout(new BoxLayout(norr, BoxLayout.Y_AXIS));

		JPanel row1 = new JPanel();
		row1.setBorder(tit1);

		row1.add(pathField = new JTextField(60));
		pathField.setEditable(false);
		row1.add(chooseBtn = new JButton("Browse"));
		chooseBtn.addActionListener(new OpenFileListener());
		norr.add(row1);

		JPanel row2 = new JPanel();
		row2.setBorder(tit2);

		row2.add(savePathField = new JTextField(60));
		savePathField.setEditable(false);
		row2.add(saveBtn = new JButton("Browse"));
		saveBtn.addActionListener(new ChoosePathListener());
		saveBtn.setEnabled(false);
		norr.add(row2);
		
		JPanel row3 = new JPanel();
		row3.setBorder(tit3);
		row3.add(caseCh = new JComboBox<String>(caseLabels));
		caseCh.addActionListener(new CaseChoiceListener());
		caseCh.setEnabled(false);

		norr.add(row3);
		
		JPanel row4 = new JPanel();
		row4.setBorder(tit4);
		row4.add(langCh = new JComboBox<String>(langLabels));
		langCh.addActionListener(new LangChoiceListener());
		langCh.setEnabled(false);
		norr.add(row4);
		
		JPanel row5 = new JPanel();
		row5.setBorder(tit5);
		row5.add(runBtn = new JButton("Run"));
		runBtn.addActionListener(new RunListener());
		runBtn.setEnabled(false);

		norr.add(row5);

		add(norr, BorderLayout.NORTH);

		area = new JTextArea();
		add(new JScrollPane(area), BorderLayout.CENTER);
		area.setEditable(false);
		area.setText("Hello!");
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new ExitListener());
		setResizable(false);
		setSize(600, 450);
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	
	//		Listener used to choose an xml-file, and then unlocking step 2

	public class OpenFileListener implements ActionListener {
                @Override
		public void actionPerformed(ActionEvent eve) {
			
			fc = new JFileChooser();
			fc.setCurrentDirectory(new File(System.getProperty("user.home")));
			fc.addChoosableFileFilter(new FileNameExtensionFilter("XML files", "xml"));
			fc.setAcceptAllFileFilterUsed(false);

			if (fc.showOpenDialog(RedLink.this) == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fc.getSelectedFile();
				filePath = selectedFile.getPath();
				pathField.setText(filePath);
				saveBtn.setEnabled(true);
			}
		}
	}
	
//	Listener used to choose a target folder for the generated txt-file, and then unlocking step 3

	public class ChoosePathListener implements ActionListener {
                @Override
		public void actionPerformed(ActionEvent eve) {
			
			fo = new JFileChooser();
			fo.setCurrentDirectory(new File(System.getProperty("user.home")));
			fo.setAcceptAllFileFilterUsed(false);
			fo.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (fo.showOpenDialog(RedLink.this) == JFileChooser.APPROVE_OPTION) {
				savePath = fo.getSelectedFile().getPath();
				savePathField.setText(savePath);
				
				caseCh.setEnabled(true);
			}
		}
	}
	
//	Listener used to choose algorithm for browsing xml-file, and then unlocking step 4
	
	public class CaseChoiceListener implements ActionListener{
                @Override
		public void actionPerformed(ActionEvent eve){
			if(caseCh.getSelectedIndex() > 0){
				caseVal = caseCh.getSelectedIndex();
				langCh.setEnabled(true);
			}
			else{
				langCh.setEnabled(false);
			}
		}
	}
	
//	Listener used to enable language options for xml-search
	
	public class LangChoiceListener implements ActionListener{
                @Override
		public void actionPerformed(ActionEvent eve){
			if(langCh.getSelectedIndex() > 0){
				langVal = langCh.getSelectedIndex()-1;
				runBtn.setEnabled(true);
			}
			else{
				runBtn.setEnabled(false);
			}
		}
	}
	
//	Listener used to start algorithm
	
	public class RunListener implements ActionListener {
                @Override
		public void actionPerformed(ActionEvent eve) {
			chooseBtn.setEnabled(false);
			saveBtn.setEnabled(false);
			caseCh.setEnabled(false);
			langCh.setEnabled(false);
			runBtn.setEnabled(false);
			runBtn.setText("Please wait...");
			fileExists = true;
			area.setText("yay!\n");
			area.append(filePath + "\n");
			area.append(savePath + "\n");
			
			try{
				Thread.sleep(3000);
			}catch(InterruptedException e){
				showErrorMessage(e.getMessage());
			}
			
			StartSearch();
			
		}
	}
	
//	Listener used to alert user on exit
	
	public class ExitListener extends WindowAdapter {
                @Override
		public void windowClosing(WindowEvent we) {
			if (showConfirmDialog(RedLink.this, "Exit? Really?!", "Message",
					YES_NO_OPTION) == YES_OPTION) {
				System.exit(0);
			} else {
			}
		}
	}

//	Listener used to show information about application and copyrights.
	
	public class AboutListener implements ActionListener {
                @Override
		public void actionPerformed(ActionEvent eve) {
			showMessageDialog(RedLink.this, "yada yada Assar och Jacke 2015",
					"About", INFORMATION_MESSAGE);
		}
	}
	
//	Method that runs the selected algorithm with preferred language
	
	public void StartSearch(){
		
		switch(caseVal){
		case 0:
		break;
		
		case 1:
			WikiTrie wt = new WikiTrie();
			try{
				print(wt.getRedLinks(filePath, languages[langVal]));
			}catch(InterruptedException | NoSuchElementException e){
				showErrorMessage(e.getMessage());
			}
		break;
		
		case 2:
                    	PopularLink pl = new PopularLink();
			try{
				print(pl.getRedLinks(filePath, languages[langVal]));
			}catch(InterruptedException | NoSuchElementException e){
				showErrorMessage(e.getMessage());
			}
		break;
		
		
		case 3:
			XMLReader xr = new XMLReader();
			try{
				print(xr.getArticles(filePath));
			}catch(InterruptedException e){
				showErrorMessage(e.getMessage());
			}
		break;
		
		/*
		
		case 4:
			print(testSet);
		break;
		
		
		
		case 5:
			FindLinks vl = new FindLinks();
			try{
				print(vl.getRedLinks(filePath, languages[langVal]));
			}catch(InterruptedException e){
				showErrorMessage(e.getMessage());
			}catch(NoSuchElementException e){
				showErrorMessage(e.getMessage());
			}
			
		break;
		
		case 6:
			SaxPrompt sp = new SaxPrompt();
			if(showConfirmDialog(RedLink.this, sp, "Login to database",OK_CANCEL_OPTION)== OK_OPTION){
				Sax sxml = new Sax();
				String temp = "";
				for(int i=0; i<sp.getPass().length;i++){
					temp+=sp.getPass()[i];
					sp.getPass()[i] = '0';
				}
				print(sxml.getArticles(filePath,sp.getUser(),temp, languages[langVal]));
			}
			
		break;
		*/
		
		default:
		break;
		}

	}
	
//	Method that saves results from chosen algorithm to file

	public void print(ArrayList<String> data){
		
		int fileVersion = 0;

        try {
            
            while (fileExists) {

                theFile = new File(savePath + "\\newList" + fileVersion + ".txt");

                if (theFile.exists()) {
                    fileVersion++;
                }
                else {

                    fileExists = false;

                    theFile.createNewFile();

                    FileWriter fw = new FileWriter(theFile.getAbsoluteFile());
                    try (BufferedWriter bw = new BufferedWriter(fw)) {
                        for (String s : data) {
                            bw.write(s + "\r\n");
                        }
                    }

                    area.append("File saved.");

                }
            }

        } catch (IOException e) {
            area.append("Save is very error.");
        }
    
        showMessageDialog(RedLink.this,"Successfully created file:\n" + theFile.getAbsolutePath() + ".\nProgram will now terminate.","Message",INFORMATION_MESSAGE);
        System.exit(0);
	        
	}
	
//	Test methods. May be removed.
	
	public void populate(){
		if(testSet.isEmpty()){
			testSet.add("Banan");
			testSet.add("Äpple");
			testSet.add("Meloner");
			testSet.add("Gurka");
			testSet.add("Fetaost & oliver");
		}
	}
	
	public class SAXTest implements ActionListener{
                @Override
		public void actionPerformed(ActionEvent eve){
				Sax sxml = new Sax();
				
				sxml.getArticles("path", "postgres", "1", languages[1]);
			
		}
	}
	
//	Main method. Do not remove!
	
	
	public void showErrorMessage(String error){
		showMessageDialog(RedLink.this,error,"Error",ERROR_MESSAGE);
	}
	
	public static void main(String[] args) {
		
		new RedLink();
	}
}