
import giny.model.Node;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

import browser.AttributeBrowserPlugin;
import browser.AttributeBrowser;


public class FindFunctionsListener implements ActionListener
{
	private JPanel parent;
	private CyNetwork currentNetwork;
	private Set selectedNodes;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<GoObject> goObjects;
	
	private AttributeBrowser attBrowser;
	public static int max = 0;
	
	/*public FindFunctionsListener(JPanel top, ArrayList<AssociationObject> assoc, ArrayList<GoObject> go)
	{
		parent = top;
		assocObjects = assoc;
		goObjects = go;
	}*/
	
	public FindFunctionsListener(JPanel top, Structures struct)
	{
		parent = top;
		assocObjects = struct.getAssocObjects();
		goObjects = struct.getGoObjects();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		attBrowser = AttributeBrowserPlugin.getAttributeBrowser(browser.DataObjectType.NODES);
		currentNetwork = Cytoscape.getCurrentNetwork();
		
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
	            CyAttributes cyNodeAttrs = Cytoscape.getNodeAttributes();
			
				int inDegree = Cytoscape.getCurrentNetwork().getInDegree(aNode);
				int outDegree = Cytoscape.getCurrentNetwork().getOutDegree(aNode);
				cyNodeAttrs.setAttribute(aNode.getIdentifier(), "InDegree", inDegree);
				cyNodeAttrs.setAttribute(aNode.getIdentifier(), "OutDegree", outDegree);
				
				int col = 1;
				List<String> list = new ArrayList<String>();
				list.add("InDegree");
				list.add("OutDegree");
				
				for(int y = 0; y<assocObjects.size(); y = y + 1)
				{			
					if(assocObjects.get(y).getProtein().contains(aNode.getIdentifier()))
					{
						cyNodeAttrs.setAttribute(aNode.getIdentifier(), "GO"+col, assocObjects.get(y).getGO() +"("+assocObjects.get(y).getEvidence()+")");
						//cyNodeAttrs.setAttribute(aNode.getIdentifier(), "test"+col, assocObjects.get(y).getGO() +"("+assocObjects.get(y).getEvidence()+")");
						list.add("GO"+col);
						col=col+1;
					}
				}
				/*for(int y = 0; y<assocObjects.size(); y = y + 1)
				{			
					if(assocObjects.get(y).getProtein().contains(aNode.getIdentifier()))
					{
						for(int go = 0; go<goObjects.size(); go = go + 1)
						{
							if(goObjects.get(go).getID().equals(assocObjects.get(y).getGO()))
							{
								cyNodeAttrs.setAttribute(aNode.getIdentifier(), "GO"+col, assocObjects.get(y).getGO() +"("+assocObjects.get(y).getEvidence()+")");
								list.add("GO"+col);
								col=col+1;
								break;
							}
						}
					}
				}*/
				
				if(list.size()>max)
				{
					attBrowser.setSelectedAttributes(list);
					max = list.size();
				}
				else
				{
					attBrowser.setSelectedAttributes(attBrowser.getSelectedAttributes());
				}
		    }	
	 	}
	}
}