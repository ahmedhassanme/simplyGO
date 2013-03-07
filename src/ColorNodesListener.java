

import giny.model.Node;
import giny.view.NodeView;

import java.awt.Color;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.render.stateful.CustomGraphic;
import cytoscape.visual.NodeAppearanceCalculator;
import cytoscape.visual.NodeShape;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualPropertyType;
import cytoscape.visual.VisualStyle;
import cytoscape.visual.calculators.BasicCalculator;
import cytoscape.visual.calculators.Calculator;
import cytoscape.visual.mappings.DiscreteMapping;
import cytoscape.visual.mappings.ObjectMapping;
import ding.view.DNodeView;

public class ColorNodesListener implements ActionListener
{		
	private JPanel parent;
	private CyNetwork currentNetwork;
	
	private FunctionTableModel chosenFunctionTable;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<String> evidence;
	private boolean check;
	private JTextField assocTF;
	
	private ArrayList<Color> theColors;
	private VisualMappingManager vmm;
	private VisualStyle currentStyle;
	private CyAttributes nodeAttrib;
	NodeAppearanceCalculator nodeAC;
	private int type;
	
	//Constructor
	public ColorNodesListener(ArrayList<AssociationObject> objects, ArrayList<String> evidenceList, FunctionTableModel model, JPanel top, int check)
	{
		assocObjects = objects;
		chosenFunctionTable = model;
		parent = top;
		evidence = evidenceList;
		type = check;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "a","Selection Error", JOptionPane.ERROR_MESSAGE);
		currentNetwork = Cytoscape.getCurrentNetwork();
		CyAttributes cyNodeAttrs = Cytoscape.getNodeAttributes();
		theColors = new ArrayList<Color>();
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "b","Selection Error", JOptionPane.ERROR_MESSAGE);
		Color[] colors = new Color[chosenFunctionTable.getRowCount()+1];
		for(int col = 0; col<chosenFunctionTable.getRowCount(); col=col+1)
		{
			colors[col] = (Color)chosenFunctionTable.getValueAt(col, 1);
		}
		colors[colors.length-1] = Color.WHITE;
		addColors(colors);
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "c","Selection Error", JOptionPane.ERROR_MESSAGE);
		ArrayList<ArrayList<String>> proteinLists = new ArrayList<ArrayList<String>>();
		ArrayList<String> goLabels = new ArrayList<String>();
		boolean check=false;
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "d","Selection Error", JOptionPane.ERROR_MESSAGE);
		try {
			for(int y=0; y<chosenFunctionTable.getRowCount()-1; y=y+1)
			{
				String function = ""+chosenFunctionTable.getValueAt(y, 0);
				function = function.substring(0,10);
				goLabels.add(function);
				proteinLists.add(new ArrayList<String>());
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), e1.toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "e","Selection Error", JOptionPane.ERROR_MESSAGE);
		//Add proteins related to GO name to protein list
		for(int assoc=0; assoc<assocObjects.size(); assoc = assoc + 1)
		{
			if(evidence.contains(assocObjects.get(assoc).getEvidence()) && goLabels.contains(assocObjects.get(assoc).getGO()))
			{
				if(assocObjects.get(assoc).getProtein().contains("|"))
				{
					List<String> prots = Arrays.asList(assocObjects.get(assoc).getProtein().split("\\|"));
					for(int x=0; x<prots.size(); x=x+1)
					{
						proteinLists.get(goLabels.indexOf(assocObjects.get(assoc).getGO())).add(prots.get(x));
					}
				}
				else
				{
					proteinLists.get(goLabels.indexOf(assocObjects.get(assoc).getGO())).add(assocObjects.get(assoc).getProtein());
				}
			}
		}
		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "f","Selection Error", JOptionPane.ERROR_MESSAGE);
		if(type==0)
		{
			//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "1","Selection Error", JOptionPane.ERROR_MESSAGE);
			boolean fillExist = false;
	 		String[] names = cyNodeAttrs.getAttributeNames();
	 		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "2","Selection Error", JOptionPane.ERROR_MESSAGE);
	 		for (String name: names)
	 		{
				if (name.equalsIgnoreCase(VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName())) 
				{
					fillExist = true;
					break;
				}
			}
	 		//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "3","Selection Error", JOptionPane.ERROR_MESSAGE);
			for(int x = 0; x<currentNetwork.nodesList().size(); x=x+1)
			{
				if(fillExist)
				{
					cyNodeAttrs.deleteAttribute(currentNetwork.nodesList().get(x).toString(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName());
				}
				Node aNode = (Node)currentNetwork.nodesList().get(x);
				NodeView nv = Cytoscape.getCurrentNetworkView().getNodeView(aNode);
				DNodeView dnv = (DNodeView) nv;
				while (dnv.getNumCustomGraphics() != 0) 
				{
					CustomGraphic custom = dnv.customGraphicIterator().next();
					dnv.removeCustomGraphic(custom);
					custom = null;
				}
			}
			//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "4","Selection Error", JOptionPane.ERROR_MESSAGE);
			//Color the nodes
			for(int node=0; node<currentNetwork.nodesList().size(); node=node+1)
			{
				check = false;
				ArrayList<String> colorList = new ArrayList<String>();
				for(int list=0; list<proteinLists.size(); list=list+1)
				{
					if(proteinLists.get(list).contains(currentNetwork.nodesList().get(node).toString()))
					{
						cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+(Color)chosenFunctionTable.getValueAt(list,1));
						//cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), VisualPropertyType.NODE_SHAPE.getBypassAttrName(), ""+NodeShape.RECT);
						Color temp = (Color)chosenFunctionTable.getValueAt(list,1);
						colorList.add(""+temp.getRGB());
						//cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), "color"+list, ""+(Color)chosenFunctionTable.getValueAt(list,1));
						check = true;
						//break;
					}
				}
				cyNodeAttrs.setListAttribute(currentNetwork.nodesList().get(node).toString(), "list", colorList);
				//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "5","Selection Error", JOptionPane.ERROR_MESSAGE);
				if(colorList.size()>1)
				{
					//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "6","Selection Error", JOptionPane.ERROR_MESSAGE);
					PieChart pie = new PieChart(colorList, node);
					
					Node aNode = (Node)currentNetwork.nodesList().get(node);
					NodeView nv = Cytoscape.getCurrentNetworkView().getNodeView(aNode);
					
					if(nv!=null)
					{
						DNodeView dnv = (ding.view.DNodeView) nv;
						Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(-5.0, -25.0, 50, 50);
						
						String userDir = System.getProperty("user.dir");
						java.awt.Paint paint;
						try 
						{
							String file = userDir+"/"+node+"image.png";
							paint = new java.awt.TexturePaint(ImageIO.read(new File(file)), rect);
							
							if(dnv!=null)
							{
								dnv.addCustomGraphic(rect, paint, cytoscape.render.stateful.NodeDetails.ANCHOR_WEST);
							}
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}				
					}
				}
				if(check==false)
				{
					cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+Color.WHITE);
				}
			}
			//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "7","Selection Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			boolean fillExist = false;
	 		String[] names = cyNodeAttrs.getAttributeNames();
	 		for (String name: names)
	 		{
				if (name.equalsIgnoreCase(VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName())) 
				{
					fillExist = true;
					break;
				}
			}
			for(int x = 0; x<currentNetwork.nodesList().size(); x=x+1)
			{
				if(fillExist)
				{
					cyNodeAttrs.deleteAttribute(currentNetwork.nodesList().get(x).toString(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName());
				}
				
				Node aNode = (Node)currentNetwork.nodesList().get(x);
				NodeView nv = Cytoscape.getCurrentNetworkView().getNodeView(aNode);
				DNodeView dnv = (DNodeView) nv;
				while (dnv.getNumCustomGraphics() != 0) 
				{
					CustomGraphic custom = dnv.customGraphicIterator().next();
					dnv.removeCustomGraphic(custom);
					custom = null;
				}
			}
			
			Iterator<Node> it = currentNetwork.getSelectedNodes().iterator();
			
			if(currentNetwork.getSelectedNodes().isEmpty())
		 	{
		 		JOptionPane.showMessageDialog(parent, "No nodes selected.","Selection Error", JOptionPane.ERROR_MESSAGE);
		 	}
			else
			{
				while (it.hasNext()) 
			    {
			    	Node aNode = (Node) it.next();
			    	check = false;
					ArrayList<String> colorList = new ArrayList<String>();
					for(int list=0; list<proteinLists.size(); list=list+1)
					{
						if(proteinLists.get(list).contains(aNode.getIdentifier()))
						{
							cyNodeAttrs.setAttribute(aNode.getIdentifier(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+(Color)chosenFunctionTable.getValueAt(list,1));
							//cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), VisualPropertyType.NODE_SHAPE.getBypassAttrName(), ""+NodeShape.RECT);
							Color temp = (Color)chosenFunctionTable.getValueAt(list,1);
							colorList.add(""+temp.getRGB());
							//cyNodeAttrs.setAttribute(currentNetwork.nodesList().get(node).toString(), "color"+list, ""+(Color)chosenFunctionTable.getValueAt(list,1));
							check = true;
							//break;
						}
					}
					cyNodeAttrs.setListAttribute(aNode.getIdentifier(), "list", colorList);
					
					if(colorList.size()>1)
					{
						PieChart pie = new PieChart(colorList, aNode.getRootGraphIndex());
						
						NodeView nv = Cytoscape.getCurrentNetworkView().getNodeView(aNode);
						
						if(nv!=null)
						{
							DNodeView dnv = (ding.view.DNodeView) nv;
							Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(-5.0, -25.0, 50, 50);
							
							String userDir = System.getProperty("user.dir");
							java.awt.Paint paint;
							try 
							{
								String file = userDir+"/"+aNode.getRootGraphIndex()+"image.png";
								paint = new java.awt.TexturePaint(ImageIO.read(new File(file)), rect);
								
								if(dnv!=null)
								{
									dnv.addCustomGraphic(rect, paint, cytoscape.render.stateful.NodeDetails.ANCHOR_WEST);
								}
							} 
							catch (IOException e1) 
							{
								e1.printStackTrace();
							}				
						}
					}
					if(check==false)
					{
						cyNodeAttrs.setAttribute(aNode.getIdentifier(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+Color.WHITE);
					}
			    }
			}
		 	/*else
		 	{			
			    while (it.hasNext()) 
			    {
			    	Node aNode = (Node) it.next();
			    	check = false;
					for(int list=0; list<proteinLists.size(); list=list+1)
					{
						if(proteinLists.get(list).contains(aNode.getIdentifier()))
						{
							cyNodeAttrs.setAttribute(aNode.getIdentifier(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+(Color)chosenFunctionTable.getValueAt(list,1));
							check = true;
							break;
						}
					}
					if(check==false)
					{
						cyNodeAttrs.setAttribute(aNode.getIdentifier(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+Color.WHITE);
					}
			    }
		 	}*/
		}
		Cytoscape.getCurrentNetworkView().redrawGraph(true,true);
		vmm.applyNodeAppearances();
	}
	
	public void addColors(Color[] colors)
	{	
		vmm = new VisualMappingManager(Cytoscape.getCurrentNetworkView());
		currentStyle = vmm.getVisualStyle();
		NodeAppearanceCalculator nodeAC = currentStyle.getNodeAppearanceCalculator();
		DiscreteMapping colorMapping = new DiscreteMapping(Color.WHITE, ObjectMapping.NODE_MAPPING);
	    colorMapping.setControllingAttributeName(VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), currentNetwork, false);
	
	    for(int x = 0; x<colors.length; x = x + 1)
	    {
	    	theColors.add(colors[x]);
	    }
	    
		for(int x = 0; x<theColors.size(); x = x + 1)
	    {
	    	colorMapping.putMapValue(""+theColors.get(x), theColors.get(x));
	    }
		Calculator nodeColorCalculator = new BasicCalculator("C3", colorMapping, VisualPropertyType.NODE_FILL_COLOR);
		nodeAC.setCalculator(nodeColorCalculator);
		
		currentStyle.setNodeAppearanceCalculator(nodeAC);				
		Cytoscape.getCurrentNetworkView().setVisualStyle(currentStyle.getName());
		vmm.setVisualStyle(currentStyle);
	}
}
