
import jas.util.ErrorBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SavePathListener implements ActionListener
{
	private File saveFile;
	private JPanel parent;
	private JTextArea data;
	
	public SavePathListener(JPanel top, JTextArea area)
	{
		parent  = top;
		data = area;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		JFileChooser choose = new JFileChooser();
		int returnValue = choose.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) 
        {
          saveFile = choose.getSelectedFile();
        }
        if(!saveFile.getName().endsWith(".txt"))
        {
			JOptionPane.showMessageDialog(parent, "Invalid file type!", "File Type Error", JOptionPane.ERROR_MESSAGE);

        }
        else
        {
        	try 
        	{
				BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile));
				writer.write(data.getText());
				writer.close();
			} 
        	catch (IOException e1) 
        	{
				e1.printStackTrace();
			}
        }
	}
}
