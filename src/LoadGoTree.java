
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoadGoTree 
{
	private ArrayList<GoObject> goObjects;
	private JPanel parent;
	private BufferedReader reader;	
	private String name;
	private String namespace;
	private String goID;
	private String identifier;
	private JTextField goTreeTF;
	
	public LoadGoTree(ArrayList<GoObject> objects)
	{
		goObjects = objects;
	}
	
	public void load()
	{
		try 
		{
			String userDir = System.getProperty("user.dir");
			File selectedFile = new File(userDir+"/plugins/goObjects.txt");


        		goObjects.clear();
        		boolean check = true;
        		reader = new BufferedReader(new FileReader(selectedFile));
        		identifier = reader.readLine();
        		while(identifier!=null)
        		{
        			if(identifier.equals("Object"))
        			{					
        				name = reader.readLine();
        				namespace = reader.readLine();
        				goID = reader.readLine();
        				
        				identifier = reader.readLine();
					
        				ArrayList<String> childOf = new ArrayList<String>();
        				while(identifier!=null && !identifier.equals("Object"))
        				{
							childOf.add(identifier);
							identifier = reader.readLine();
        				}					
        				goObjects.add(new GoObject(goID, name, namespace, childOf));
        			}
        			else
        			{
        				//In case incorrect file is loaded
        				goObjects.clear();
        				check = false;
        			//	JOptionPane.showMessageDialog(parent, "Contents of file are invalid.","File Type Error", JOptionPane.ERROR_MESSAGE);
        				break;
        			}
        		}
        		if(check)
        		{
        			goTreeTF.setText(selectedFile.getName());
        			goTreeTF.setBackground(Color.green);
        		}
        	
		} 
		catch (Exception e) 
		{
			//JOptionPane.showMessageDialog(parent, "no","File Type Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
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
