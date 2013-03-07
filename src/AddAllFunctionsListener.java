
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;


public class AddAllFunctionsListener implements ActionListener
{
	private FunctionTableModel model;
	private JTree goTree;
	private JPanel parent;
	
	public AddAllFunctionsListener(FunctionTableModel tableModel, JTree tree, JPanel top)
	{
		model = tableModel;
		goTree = tree;
		parent = top;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) goTree.getModel().getRoot();
		if(node.getChildCount()==1)
		{
			JOptionPane.showMessageDialog(parent, "Functions not loaded.","Load Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			for(int x = 0; x<node.getChildCount(); x=x+1)
			{
				model.setValueAt(node.getChildAt(x).toString(), model.getRowCount()-1, 0);
				model.setRowCount(model.getRowCount()+1);
			}
		}
	}

}
