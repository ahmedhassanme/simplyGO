
import jas.util.ErrorBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GenerateGoListener implements ActionListener
{
	private File selectedFile;
	private File saveFile;
	private BufferedReader reader;
	private String line;
	private JPanel parent;
	
	private String goID;
	private String name;
	private String namespace;
	private ArrayList<GoObject> goObjects;
	
	//Constructor
	public GenerateGoListener(JPanel top)
	{
		parent = top;
		goObjects = new ArrayList<GoObject>();
	}
	
	//Read the GO Tree file and produce the smaller file
	public void actionPerformed(ActionEvent e) 
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
        		reader = new BufferedReader(new FileReader(selectedFile));
        		
        		line = reader.readLine();
        		while(!line.equals("[Typedef]"))
        		{
        			if(line.equals("[Term]"))
        			{
            			ArrayList<String> parents = new ArrayList<String>();
        				goID = reader.readLine().substring(4);
        				name = reader.readLine().substring(6);
        				namespace = reader.readLine().substring(11);
        			
        				while(line.length()>0)
        				{
        					if(line.substring(0,4).equals("is_a"))
        					{
        						parents.add(line.substring(19));
        					}
        					line = reader.readLine();
        				}
        				goObjects.add(new GoObject(goID, name, namespace, parents));
        			}
    				line = reader.readLine();
        		}
        		reader.close();
        		
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
                	BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
                	for(int x = 0; x<goObjects.size(); x=x+1)
                	{
                		if(x==goObjects.size()-1)
                		{
                			writer.write("Object");
                			writer.newLine();
                			writer.write(goObjects.get(x).getName());
                			writer.newLine();
                			writer.write(goObjects.get(x).getNamespace());
                			writer.newLine();
                			writer.write(goObjects.get(x).getID());
        			
                			for(int y = 0; y<goObjects.get(x).getParents().size(); y=y+1)
                			{
                				writer.newLine();
                				writer.write(goObjects.get(x).getParents().get(y));
                			}
                		}
                		else
                		{
                			writer.write("Object");
                			writer.newLine();
                			writer.write(goObjects.get(x).getName());
                			writer.newLine();
                			writer.write(goObjects.get(x).getNamespace());
                			writer.newLine();
                			writer.write(goObjects.get(x).getID());
                			writer.newLine();
        			
                			for(int y = 0; y<goObjects.get(x).getParents().size(); y=y+1)
                			{
                				writer.write(goObjects.get(x).getParents().get(y));
                				writer.newLine();
                			}
                		}
                	}
                	writer.close();
                }
        	} 
        	catch (Exception e1) 
        	{
        		e1.printStackTrace();
        	}
        }
	}
}
