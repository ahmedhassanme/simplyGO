
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoadAssociations
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
	
	public LoadAssociations(ArrayList<AssociationObject> objects)
	{
		assocObjects = objects;
	}
	
	public void load()
	{

        	try 
        	{
        		String userDir = System.getProperty("user.dir");
    			File selectedFile = new File(userDir+"/plugins/associationObjects.txt");
        		
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
        				//ErrorBox box = new ErrorBox(parent, "Contents of file are invalid.");
                		//box.show();
        				break;
        			}

        		}
        		if(check)
        		{
        			associationFileTF.setBackground(Color.green);
        			associationFileTF.setText(selectedFile.getName());
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
