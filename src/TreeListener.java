
import giny.model.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;


public class TreeListener implements TreeWillExpandListener
{
	private DefaultTreeModel treeModel;
	private ArrayList<GoObject> goObjects;
	private int start = 0;
	private int type;
	
	private Structures structures;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<String> goIDS;
	private ArrayList<ArrayList<String>> parents;
	private CyNetwork currentNetwork;
	
	private ArrayList<GoObject> bioObjects;
	private ArrayList<GoObject> molObjects;
	private ArrayList<GoObject> cellObjects;
	private ArrayList<GoObject> bioIDS;
	private ArrayList<GoObject> molIDS;
	private ArrayList<GoObject> cellIDS;
	private int bioCount = 0;
	private JFrame parent;
	private Set selectedNodes;
	
	//Constructor
	public TreeListener(Structures data, DefaultTreeModel tree, int kind, JFrame top)
	{
		structures = data;
		treeModel = tree;
		type = kind;
		parent = top;
		assocObjects = structures.getAssocObjects();
		goObjects = structures.getGoObjects();
		bioObjects = new ArrayList<GoObject>();
		molObjects = new ArrayList<GoObject>();
		cellObjects = new ArrayList<GoObject>();
		bioIDS = new ArrayList<GoObject>();
		molIDS = new ArrayList<GoObject>();
		cellIDS = new ArrayList<GoObject>();
		
		for(int x = 0; x<goObjects.size(); x=x+1)
		{
			if(goObjects.get(x).getNamespace().equals("biological_process"))
			{
				bioObjects.add(goObjects.get(x));
			}
			else
			{
				if(goObjects.get(x).getNamespace().equals("molecular_function"))
				{
					molObjects.add(goObjects.get(x));
				}
				else
				{
					cellObjects.add(goObjects.get(x));
				}
			}
		}
	}
	
	public void treeWillCollapse(TreeExpansionEvent arg0) throws ExpandVetoException 
	{

	}
	
	//Deal with expansion
	public void treeWillExpand(TreeExpansionEvent evt) throws ExpandVetoException 
	{
		//goObjects = structures.getGoObjects();
		//assocObjects = structures.getAssocObjects();
		goIDS = new ArrayList<String>();
		currentNetwork = Cytoscape.getCurrentNetwork();

 		currentNetwork.selectAllNodes();
 		selectedNodes = currentNetwork.getSelectedNodes();
	 	Iterator<Node> it = selectedNodes.iterator();
	 	
	 	if(selectedNodes.isEmpty())
	 	{
	 		JOptionPane.showMessageDialog(parent, "No nodes selected.","Selection Error", JOptionPane.ERROR_MESSAGE);
	 	}
	 	else
	 	{			
		    while (it.hasNext()) 
		    {
	            Node aNode = (Node) it.next();
		
				for(int y = 0; y<assocObjects.size(); y = y + 1)
				{			
					if(assocObjects.get(y).getProtein().contains(aNode.getIdentifier()))
					{
						if(!goIDS.contains(assocObjects.get(y).getGO()))
						{
							goIDS.add(assocObjects.get(y).getGO());
						}
					}
				}
		    }
	 	}
			
		if(type==1)
		{	 
			for(int x = 0; x<bioObjects.size(); x=x+1)
			{
				for(int y = 0; y<goIDS.size(); y=y+1)
				{
					if(bioObjects.get(x).getID().equals(goIDS.get(y)))
					{
						bioIDS.add(bioObjects.get(x));
					}
				}
			}
			
			DefaultMutableTreeNode current = (DefaultMutableTreeNode)evt.getPath().getLastPathComponent();
			//DefaultMutableTreeNode child = new DefaultMutableTreeNode("test");
			
			if(!(current.getChildCount()>1))
			{
				for(int x = 0; x<bioIDS.size(); x=x+1)
				{
					String temp = bioIDS.get(x).getID() + " - " + bioIDS.get(x).getName();
					DefaultMutableTreeNode theChild = new DefaultMutableTreeNode(temp);
					treeModel.insertNodeInto(theChild, current, current.getChildCount());
					//treeModel.insertNodeInto(new DefaultMutableTreeNode(bioIDS.get(x)), theChild, theChild.getChildCount());
				}
				current.remove(0);
				//treeModel.insertNodeInto(new DefaultMutableTreeNode("new level"), child, child.getChildCount());
			}
		}
		else
		{
			if(type==2)
			{
				for(int x = 0; x<molObjects.size(); x=x+1)
				{
					for(int y = 0; y<goIDS.size(); y=y+1)
					{
						if(molObjects.get(x).getID().equals(goIDS.get(y)))
						{
							molIDS.add(molObjects.get(x));
						}
					}
				}
				
				DefaultMutableTreeNode current = (DefaultMutableTreeNode)evt.getPath().getLastPathComponent();
				//DefaultMutableTreeNode child = new DefaultMutableTreeNode("test");
				
				if(!(current.getChildCount()>1))
				{
					for(int x = 0; x<molIDS.size(); x=x+1)
					{
						String temp = molIDS.get(x).getID() + " - " + molIDS.get(x).getName();
						DefaultMutableTreeNode theChild = new DefaultMutableTreeNode(temp);
						treeModel.insertNodeInto(theChild, current, current.getChildCount());
						//treeModel.insertNodeInto(new DefaultMutableTreeNode(bioIDS.get(x)), theChild, theChild.getChildCount());
					}
					current.remove(0);
					//treeModel.insertNodeInto(new DefaultMutableTreeNode("new level"), child, child.getChildCount());
				}
			}
			else
			{
				for(int x = 0; x<cellObjects.size(); x=x+1)
				{
					for(int y = 0; y<goIDS.size(); y=y+1)
					{
						if(cellObjects.get(x).getID().equals(goIDS.get(y)))
						{
							cellIDS.add(cellObjects.get(x));
						}
					}
				}
				
				DefaultMutableTreeNode current = (DefaultMutableTreeNode)evt.getPath().getLastPathComponent();
				//DefaultMutableTreeNode child = new DefaultMutableTreeNode("test");
				
				if(!(current.getChildCount()>1))
				{
					for(int x = 0; x<cellIDS.size(); x=x+1)
					{
						String temp = cellIDS.get(x).getID() + " - " + cellIDS.get(x).getName();
						DefaultMutableTreeNode theChild = new DefaultMutableTreeNode(temp);
						treeModel.insertNodeInto(theChild, current, current.getChildCount());
						//treeModel.insertNodeInto(new DefaultMutableTreeNode(bioIDS.get(x)), theChild, theChild.getChildCount());
					}
					current.remove(0);
					//treeModel.insertNodeInto(new DefaultMutableTreeNode("new level"), child, child.getChildCount());
				}
			}
		}
		currentNetwork.unselectAllNodes();
	}	
}