
import giny.model.Edge;
import giny.model.Node;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;
import cytoscape.visual.EdgeAppearanceCalculator;
import cytoscape.visual.NodeAppearanceCalculator;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualPropertyType;
import cytoscape.visual.VisualStyle;
import cytoscape.visual.calculators.BasicCalculator;
import cytoscape.visual.calculators.Calculator;
import cytoscape.visual.mappings.DiscreteMapping;
import cytoscape.visual.mappings.ObjectMapping;


public class ShortestPathListener implements ActionListener
{
	private JPanel parent;
	private JTextField field;
	private JTextArea area;
	private CyNetwork currentNetwork;
	private JComboBox startNodeComboBox;
	private JComboBox endNodeComboBox;
	private int startNodeIndex;
	private int endNodeIndex;
	private JTextField startNode;
	private JTextField endNode;
	
	private VisualMappingManager vmm;
	private VisualStyle currentStyle;
	private CyAttributes cyNodeAttrs;
	private CyAttributes cyEdgeAttrs;
	
	private Structures structures;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<GoObject> goObjects;
	
	private int type = 0;
	
	public ShortestPathListener(JPanel top, JTextField testField, JComboBox start, JComboBox end)
	{
		parent = top;
		field = testField;
		startNodeComboBox = start;
		endNodeComboBox = end;
		type = 1;
	}
	
	public ShortestPathListener(JPanel top, JTextArea testField, JTextField start, JTextField end, Structures struct)
	{
		parent = top;
		area = testField;
		startNode = start;
		endNode = end;
		structures = struct;
		assocObjects = structures.getAssocObjects();
		type = 2;
	}
	
	public ShortestPathListener(JPanel top, JTextField testField)
	{
		parent = top;
		field = testField;
	}
	
	public ShortestPathListener(JPanel top, JTextArea testField, Structures struct)
	{
		parent = top;
		area = testField;
		structures  = struct;
		assocObjects = structures.getAssocObjects();
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		float result=0;
		int sizeCheck = 0;
		ArrayList<Node> path = new ArrayList<Node>();
		currentNetwork = Cytoscape.getCurrentNetwork();
		MyDijkstra lookFor = new MyDijkstra(currentNetwork);
		
		if(type==1)
		{
			startNodeIndex = startNodeComboBox.getSelectedIndex();
			endNodeIndex = endNodeComboBox.getSelectedIndex();
			result = lookFor.findPath((Node)currentNetwork.nodesList().get(startNodeIndex), (Node)currentNetwork.nodesList().get(endNodeIndex));
		}
		
		if(type==2)
		{
			int startIndex=-1;
			int endIndex=-1;
			
			if(startNode.getText().isEmpty()||endNode.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(parent, "No node(s) entered.","Input Error", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				int firstCheck=0;
				int secondCheck=0;
				
				for(int x=0; x<currentNetwork.nodesList().size(); x=x+1)
				{
					if(currentNetwork.nodesList().get(x).toString().equalsIgnoreCase(startNode.getText().trim()))
					{
						startIndex = x;
						firstCheck = 1;
						break;
					}
				}
				if(firstCheck==0)
				{
					JOptionPane.showMessageDialog(parent, "First node couldn't be matched.","Node Error", JOptionPane.ERROR_MESSAGE);
				}
				
				for(int y=0; y<currentNetwork.nodesList().size(); y=y+1)
				{
					if(currentNetwork.nodesList().get(y).toString().equalsIgnoreCase(endNode.getText().trim()))
					{
						endIndex = y;
						secondCheck = 1;
						break;
					}
				}
				if(secondCheck==0)
				{
					JOptionPane.showMessageDialog(parent, "Second node couldn't be matched.","Node Error", JOptionPane.ERROR_MESSAGE);
				}
				result = lookFor.findPath((Node)currentNetwork.nodesList().get(startIndex), (Node)currentNetwork.nodesList().get(endIndex));		
			}
		}
		
		else
		{			
			if(currentNetwork.getSelectedNodes().size()<2)
		 	{
		 		JOptionPane.showMessageDialog(parent, "Not enough nodes selected.","Selection Error", JOptionPane.ERROR_MESSAGE);
		 		sizeCheck = 1;
		 	}
			else
			{
				Object[] pair = currentNetwork.getSelectedNodes().toArray();
				result = lookFor.findPath((Node)pair[0], (Node)pair[1]);
			}
		}
		
		if(result==0 && sizeCheck==0)
		{
			JOptionPane.showMessageDialog(parent, "Nodes are not connected.", "Selection Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
				//structures = new Structures();
				//ArrayList<AssociationObject> objects = structures.getAssocObjects();
				//JOptionPane.showMessageDialog(parent, "in.", "Selection Error", JOptionPane.ERROR_MESSAGE);
				path = lookFor.getRoute();		
				if(!path.isEmpty())
				{
					structures.setPath(path);
					area.setText("Path:");
					area.append("\n");
					for(int x = 0; x<path.size(); x=x+1)
					{
						area.append(""+x+"-"+path.get(x));
						area.append(""+'\n');
						//area.append("(");
						//area.append(")");
					}			
					area.append(""+result);
					area.append("\n");
					area.append("\n");
					area.append("GO Associations:");
					area.append("\n");
					
					for(int x = 0; x<path.size(); x=x+1)
					{
						area.append(""+path.get(x)+":");
						area.append("\n");
						for(int y = 0; y<assocObjects.size(); y = y + 1)
						{			
							if(assocObjects.get(y).getProtein().contains(""+path.get(x)))
							{
								//area.setText(area.getText()+assocObjects.get(y).getGO());
								area.append(assocObjects.get(y).getGO());
								area.append("\n");
							}
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(parent, "path is empty.", "Selection Error", JOptionPane.ERROR_MESSAGE);
				}
			
			cyNodeAttrs = Cytoscape.getNodeAttributes();
			cyEdgeAttrs = Cytoscape.getEdgeAttributes();
		   // JOptionPane.showMessageDialog(parent, "test a","Selection Error", JOptionPane.ERROR_MESSAGE);

			for(int x=0; x<currentNetwork.nodesList().size(); x=x+1)
			{
				if(path.contains(currentNetwork.nodesList().get(x)))
				{
					cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(x).toString(), VisualPropertyType.NODE_OPACITY.getBypassAttrName(), ""+50);	
					}
				else
				{
					cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(x).toString(), VisualPropertyType.NODE_OPACITY.getBypassAttrName(), ""+255);
				}
				
			}
			/*Iterator<Node> it = Cytoscape.getCurrentNetwork().edgesIterator();
			while (it.hasNext()) 
			{
				Edge edge = (Edge) it.next();
				cyEdgeAttrs.setAttribute(edge.getIdentifier(), VisualPropertyType.EDGE_LINE_WIDTH.getBypassAttrName(), ""+30);
			}*/
		   // JOptionPane.showMessageDialog(parent, "test b","Selection Error", JOptionPane.ERROR_MESSAGE);

			//applyEdges();
			
		   // JOptionPane.showMessageDialog(parent, "test c","Selection Error", JOptionPane.ERROR_MESSAGE);
		    
			/*for(int x = 0; x<currentNetwork.edgesList().size(); x=x+1)
			{
				cyEdgeAttrs.setAttribute(currentNetwork.edgesList().get(x).toString(), VisualPropertyType.EDGE_COLOR.getBypassAttrName(), ""+Color.YELLOW);
			}*/
			
		   // JOptionPane.showMessageDialog(parent, "test d","Selection Error", JOptionPane.ERROR_MESSAGE);

		}
		
	   // JOptionPane.showMessageDialog(parent, "test e","Selection Error", JOptionPane.ERROR_MESSAGE);

		
		/*ArrayList<CyEdge> edges = new ArrayList<CyEdge>();
		for(int y=0; y<currentNetwork.edgesList().size(); y=y+1)
		{
			//edges.add((CyEdge)currentNetwork.getEdge(y));
			cyEdgeAttrs.setAttribute(currentNetwork.getEdge(y).getIdentifier(), VisualPropertyType.EDGE_LINE_WIDTH.getBypassAttrName(), "0");
			//cyEdgeAttrs.setAttribute(Cytoscape.getCurrentNetwork().edgesList().get(y).toString(), VisualPropertyType.EDGE_LINE_WIDTH.getBypassAttrName(), 10);
			//JOptionPane.showMessageDialog(parent, cyEdgeAttrs.getAttribute(Cytoscape.getCurrentNetwork().edgesList().get(y).toString(), VisualPropertyType.EDGE_LINE_WIDTH.getBypassAttrName()), "Selection Error", JOptionPane.ERROR_MESSAGE);
		}*/
		//JOptionPane.showMessageDialog(parent, ""+currentNetwork.edgesList().get(0).toString(), "Selection Error", JOptionPane.ERROR_MESSAGE);

		//Cytoscape.getCurrentNetworkView().redrawGraph(true,true);
		//Cytoscape.getVisualMappingManager().applyAppearances();
		//Cytoscape.getVisualMappingManager().applyNodeAppearances();
		//Cytoscape.getVisualMappingManager().applyEdgeAppearances();
		//Cytoscape.getVisualMappingManager().applyEdgeAppearances();
		//vmm.applyNodeAppearances();
		//vmm.applyEdgeAppearances();
	}
	
	public void applyEdges()
	{
		vmm = new VisualMappingManager(Cytoscape.getCurrentNetworkView());
		currentStyle = vmm.getVisualStyle();
		EdgeAppearanceCalculator edgeAC = currentStyle.getEdgeAppearanceCalculator();
		DiscreteMapping colorMapping = new DiscreteMapping(Color.WHITE, ObjectMapping.EDGE_MAPPING);
	    colorMapping.setControllingAttributeName(VisualPropertyType.EDGE_COLOR.getBypassAttrName(), currentNetwork, false);

	    JOptionPane.showMessageDialog(parent, "test3","Selection Error", JOptionPane.ERROR_MESSAGE);
	    
	    colorMapping.putMapValue(""+Color.WHITE, Color.WHITE);
	    colorMapping.putMapValue(""+Color.YELLOW, Color.YELLOW);
	    
		Calculator edgeColorCalculator = new BasicCalculator("E3", colorMapping, VisualPropertyType.EDGE_COLOR);
		edgeAC.setCalculator(edgeColorCalculator);

		JOptionPane.showMessageDialog(parent, "test4","Selection Error", JOptionPane.ERROR_MESSAGE);
	
		currentStyle.setEdgeAppearanceCalculator(edgeAC);				
		Cytoscape.getCurrentNetworkView().setVisualStyle(currentStyle.getName());
		vmm.setVisualStyle(currentStyle);
	}

}
