

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoadAssociationsListener implements ActionListener 
{
	private JPanel parent;
	private ArrayList<AssociationObject> assocObjects;
	
	private String line;
	private String go;
	private String protein;
	private String evidence;
	private JTextField associationFileTF;
	private File selectedFile;
	private BufferedReader reader;
	
	//Constructor
	public LoadAssociationsListener(JPanel top, ArrayList<AssociationObject> objects)
	{
		parent = top;
		assocObjects = objects;
	}
	
	//Read through the associations file and load them into memory
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
        		assocObjects.clear();
        		boolean check = true;
        		reader = new BufferedReader(new FileReader(selectedFile));
        		  
        		line = reader.readLine();
        		  
        		while(line!=null)
        		{
        			if(line.equals("OBJECT"))
        			{
        				go = reader.readLine();
        				protein = reader.readLine();
        				evidence = reader.readLine();
        				AssociationObject object = new AssociationObject(go, protein, evidence);
        				assocObjects.add(object);
               			line = reader.readLine();
        			}
        			else
        			{
        				//In case incorrect file is loaded
        				assocObjects.clear();
        				check = false;
        				JOptionPane.showMessageDialog(parent, "Contents of file are invalid.","File Contents Error", JOptionPane.ERROR_MESSAGE);
        				break;
        			}

        		}
        	}
        	catch(Exception e)
        	{
        		  
        	}
        	finally
        	{        		
        		try 
        		{
					reader.close();
				} 
        		catch (IOException e) 
        		{
					e.printStackTrace();
				}
        	}
        }
    }
}
