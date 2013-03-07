
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JCheckBox;


public class CheckBoxListener implements ActionListener
{
	private JCheckBox box;
	private ArrayList<String> evidenceList;
	
	//Constructor
	public CheckBoxListener(JCheckBox chosen, ArrayList<String> list)
	{
		box = chosen;
		evidenceList = list;
	}
	
	//Watches for whenever a box is checked or unchecked
	public void actionPerformed(ActionEvent arg0) 
	{		
			if(box.isSelected())
			{
				if(!evidenceList.contains(box.getText()))
				{
					evidenceList.add(box.getText());
				}
			}
			else
			{
				if(evidenceList.contains(box.getText()))
				{
					evidenceList.remove(box.getText());
				}
			}
	}

}
