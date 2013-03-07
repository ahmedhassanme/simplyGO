
import jas.util.ErrorBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GenerateAssociationListener implements ActionListener
{
	private JPanel parent;
	private File selectedFile;
	private File saveFile;
	private ArrayList<AssociationObject> assocObjects;
	
	private String line;
	private String gene = "";
	private String go = "";
	private String evidence = "";
	private int tabCount = 0;
	private int evidenceLocStart = 0;
	private int evidenceLocEnd = 0;
	private int geneStart = 0;
	private int geneEnd = 0;
	
	//Constructor
	public GenerateAssociationListener(JPanel top, ArrayList<AssociationObject> objects)
	{
		parent = top;
		assocObjects = objects;
	}
	
	//Read association file and produce smaller file
	public void actionPerformed(ActionEvent arg0) 
	{
		JFileChooser choose = new JFileChooser();
		int returnValue = choose.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) 
        {
        	selectedFile = choose.getSelectedFile();
        }
        if(!selectedFile.getName().endsWith(".txt"))
        {
			JOptionPane.showMessageDialog(parent, "File must be of .txt type.","File Type Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
        	try 
        	{			        		  
        		File file = new File(selectedFile.getAbsolutePath());
        		BufferedReader reader = new BufferedReader(new FileReader(file));
        		assocObjects.clear();  
        		line = reader.readLine();
        		  
        		while(line.charAt(0)=='!')
        		{
        			line = reader.readLine();
        		}
        		  
        		while(line!=null)
        		{
        			gene = "";
        			go = "";
        			evidence = "";
        			tabCount = 0;
        			evidenceLocStart = 0;
    				evidenceLocEnd = 0;
    				geneStart = 0;
    				geneEnd = 0;
        			  
        			//Store GO ID
        			int startGo = line.indexOf("GO");
        			go = line.substring(startGo,startGo+10);
        			  
        			for(int tab = 0; tabCount!=10; tab = tab + 1)
        			{
        				if(line.charAt(tab)=='\t')
        				{
        					tabCount = tabCount + 1;
        				}
        				if(tabCount==5)
        				{
        					evidenceLocStart = tab + 2;
        					evidenceLocEnd = evidenceLocStart;
        					  
        					for(int z = evidenceLocStart+1; line.charAt(z)!='\t'; z=z+1)
	        				{
	        					evidenceLocEnd = evidenceLocEnd + 1;
	        				}
        					  
        					evidence = line.substring(evidenceLocStart, evidenceLocEnd+1);
        				}
        				if(tabCount==9)
        				{
        					geneStart = tab + 2;
        					geneEnd = geneStart;
        					  
        					for(int z = geneStart+1; line.charAt(z)!='\t'; z=z+1)
	        				{
	        					geneEnd = geneEnd + 1;
	        				}
        					  
        					gene = line.substring(geneStart, geneEnd+1);
        				}
        			}
        			 assocObjects.add(new AssociationObject(go, gene, evidence));
        			 line = reader.readLine();
        		}
        		reader.close();
        	}
        	catch(Exception e)
        	{
        		  
        	}
        	 
        	returnValue = choose.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
            	saveFile = choose.getSelectedFile();
            }
            if(!saveFile.getName().endsWith(".txt"))
            {
    			JOptionPane.showMessageDialog(parent, "File must be of .txt type.","File Type Error", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
            	try 
            	{
            		BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
				
            		for(int x = 0; x<assocObjects.size(); x = x+1)
            		{
            			if(x==assocObjects.size()-1)
            			{
            				bw.write("OBJECT");
            				bw.newLine();
            				bw.write(assocObjects.get(x).getGO());
            				bw.newLine();
            				bw.write(assocObjects.get(x).getProtein());
            				bw.newLine();
            				bw.write(assocObjects.get(x).getEvidence());
            			}
            			else
            			{
            				bw.write("OBJECT");
            				bw.newLine();
            				bw.write(assocObjects.get(x).getGO());
            				bw.newLine();
            				bw.write(assocObjects.get(x).getProtein());
            				bw.newLine();
            				bw.write(assocObjects.get(x).getEvidence());
            				bw.newLine();
            			}
            		}
            		bw.close();
            	}
            	catch (IOException e) 
            	{
            		e.printStackTrace();
            	}
            }
        }
    }
}
