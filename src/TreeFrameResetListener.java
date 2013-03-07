
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTree;


public class TreeFrameResetListener implements ActionListener
{
	private JCheckBox[] checkBoxes;
	private FunctionTableModel tableModel;
	private JTree bioTree;
	private JTree molTree;
	private JTree cellTree;
	
	public TreeFrameResetListener(JCheckBox[] boxes, FunctionTableModel model, JTree bio, JTree mol, JTree cell)
	{
		checkBoxes = boxes;
		tableModel = model;
		bioTree = bio;
		molTree = mol;
		cellTree = cell;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		for(int x=0; x<checkBoxes.length; x=x+1)
		{
			checkBoxes[x].setSelected(false);
		}
		
		tableModel.setRowCount(1);
		tableModel.setValueAt("", 0, 0);
		tableModel.setValueAt(Color.WHITE, 0, 1);
		
		int row = bioTree.getRowCount()-1;
		while (row >= 0) 
		{
		      bioTree.collapseRow(row);
		      row = row-1;
		}
		
		row = molTree.getRowCount()-1;
		while (row >= 0) 
		{
		      molTree.collapseRow(row);
		      row = row-1;
		}
		
		row = cellTree.getRowCount()-1;
		while (row >= 0) 
		{
		      cellTree.collapseRow(row);
		      row = row-1;
		}
	}
}
