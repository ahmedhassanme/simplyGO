
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class LoadGoTreeListener implements ActionListener
{
	private JPanel parent;
	private ArrayList<GoObject> goObjects;
	private BufferedReader reader;
	
	private String name;
	private String namespace;
	private String goID;
	private String identifier;
	private JTextField goTreeTF;
	
	//Constructor
	public LoadGoTreeListener(JPanel top, ArrayList<GoObject> objects)
	{
		parent = top;
		goObjects = objects;
	}
	
	//Read the GO Tree file and load it into memory
	public void actionPerformed(ActionEvent arg0) 
	{
		try 
		{
			JFileChooser choose = new JFileChooser();
			int returnValue = choose.showOpenDialog(null);
			File selectedFile = new File("");
	        if (returnValue == JFileChooser.APPROVE_OPTION) 
	        {
	          selectedFile = choose.getSelectedFile();
	        }
	        if(!selectedFile.getName().endsWith(".txt"))
        	{
	        	JOptionPane.showMessageDialog(parent, "Selected file is not a text file.","File Type Error", JOptionPane.ERROR_MESSAGE);
        	}
        	else
        	{
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
        				JOptionPane.showMessageDialog(parent, "Contents of file are invalid.","File Type Error", JOptionPane.ERROR_MESSAGE);
        				break;
        			}
        		}
        		/*if(check)
        		{
        			goTreeTF.setText(selectedFile.getName());
        			goTreeTF.setBackground(Color.green);
        		}*/
        	}
		} 
		catch (Exception e) 
		{
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