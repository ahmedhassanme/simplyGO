
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;


public class AddFunctionListener implements ActionListener 
{
	private FunctionTableModel model;
	private JTree goTree;
	private JPanel parent;
	
	//Constructor
	public AddFunctionListener(FunctionTableModel tableModel, JTree tree, JPanel top)
	{
		model = tableModel;
		goTree = tree;
		parent = top;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		//Get the current selection path
		TreePath[] path = goTree.getSelectionPaths();
		//Handle lack of selection
		if(path==null)
		{
			//ErrorBox box = new ErrorBox(parent, "Select a function from the GO Tree.");
			//box.show();
		}
		else
		{
			//Check if root is elected
			boolean check = false;
			for(int node = 0; node<path.length; node=node+1)
			{
				if(path[node].getLastPathComponent().toString().contains("GO Tree"))
				{
					check = true;
				}
			}
			//Handle selection of root
			if(check)
			{
				//ErrorBox box = new ErrorBox(parent, "Can't add root node of the GO Tree.");
				//box.show();
			}
			else
			{
				//Check if ID is selected instead of name
				boolean goCheck = false;
				for(int node = 0; node<path.length; node=node+1)
				{
					if(path[node].getLastPathComponent().toString().substring(0,2).equals("GO"))
					{
						goCheck = true;
						//JOptionPane.showMessageDialog(parent, "2","Selection Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				//Handle selection of ID instead of name
				//if(goCheck)
				//{
					//ErrorBox box = new ErrorBox(parent, "Cannot add a GO ID. Please select the function name.");
					//box.show();
				//}
				//Get function information and store it in the table
				//else
				//{
					String[] selected = new String[path.length];
	
					/*if(model.getRowCount()==1 && model.getValueAt(0, 0)=="")
					{
						for(int x = 0; x<path.length; x=x+1)
						{
							if(x==0)
							{
								DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[x].getLastPathComponent();
								
								selected[x] = node.getFirstLeaf().toString();
								//selected[x] = selected[x] + " - " +path[x].getLastPathComponent().toString();
								model.setValueAt(selected[x], model.getRowCount()-1, 0);
							}
							else
							{
								DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[x].getLastPathComponent();
	
								selected[x] = node.getFirstLeaf().toString();
								//selected[x] = selected[x] + " - " +path[x].getLastPathComponent().toString();
								model.setRowCount(model.getRowCount()+1);
								model.setValueAt(selected[x], model.getRowCount()-1, 0);
							}
						}
					}
					else
					{*/
						for(int x = 0; x<path.length; x=x+1)
						{
							DefaultMutableTreeNode node = (DefaultMutableTreeNode)path[x].getLastPathComponent();
	
							selected[x] = node.getFirstLeaf().toString();
							//selected[x] = selected[x] + " - " +path[x].getLastPathComponent().toString();
							model.setValueAt(selected[x], model.getRowCount()-1, 0);
							model.setRowCount(model.getRowCount()+1);
						}
					
				//}
			}
		}
	}
}