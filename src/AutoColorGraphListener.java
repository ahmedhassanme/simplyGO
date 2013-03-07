
import giny.model.Node;
import giny.view.NodeView;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.render.stateful.CustomGraphic;
import cytoscape.visual.NodeAppearanceCalculator;
import cytoscape.visual.VisualMappingManager;
import cytoscape.visual.VisualPropertyType;
import cytoscape.visual.VisualStyle;
import cytoscape.visual.calculators.BasicCalculator;
import cytoscape.visual.calculators.Calculator;
import cytoscape.visual.mappings.DiscreteMapping;
import cytoscape.visual.mappings.ObjectMapping;
import ding.view.DNodeView;


public class AutoColorGraphListener implements ActionListener
{
	private JPanel parent;
	private CyNetwork currentNetwork;
	private ArrayList<String> goFunctions;
	private ArrayList<Color> colors;
	private Random r;
	private HashMap<String, Color> goToColorMap;
	private HashMap<String, String> functionsToNamesMap;
	
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
	private CyAttributes cyNodeAttrs;
	private Set selectedNodes;
	private Structures structures;
	private ArrayList<GoObject> goObjects;
	private JFrame legendFrame;
	private FunctionTableModel legendModel;
	
	public AutoColorGraphListener(Structures data, JPanel top, FunctionTableModel model, JFrame legend, FunctionTableModel legendM)
	{
		structures = data;
		parent = top;
		chosenFunctionTable = model;
		legendFrame = legend;
		legendModel = legendM;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		currentNetwork = Cytoscape.getCurrentNetwork();
		cyNodeAttrs = Cytoscape.getNodeAttributes();
		assocObjects = structures.getAssocObjects();
		goObjects = structures.getGoObjects();
		currentNetwork.selectAllNodes();
		selectedNodes = currentNetwork.getSelectedNodes();
	 	Iterator<Node> it = selectedNodes.iterator();
	 	
	 	r = new Random();
	 	theColors = new ArrayList<Color>();
		goFunctions = new ArrayList<String>();
		colors = new ArrayList<Color>();
	 	
	 	if(selectedNodes.isEmpty())
	 	{
	 		JOptionPane.showMessageDialog(parent, "No nodes selected.","Selection Error", JOptionPane.ERROR_MESSAGE);
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
	 		
	 		HashMap<String, ArrayList<String>> geneToFunctionsMap = new HashMap<String, ArrayList<String>>();
	 		functionsToNamesMap = new HashMap<String, String>();
	 		while (it.hasNext()) 
		    {
	            Node aNode = (Node) it.next();
	            ArrayList<String> tempFunctions = new ArrayList<String>();
	            
	            for(int x = 0; x<assocObjects.size(); x = x + 1)
				{			
					if(assocObjects.get(x).getProtein().contains(aNode.getIdentifier()))
					{
						if(!goFunctions.contains(assocObjects.get(x).getGO()))
						{
							goFunctions.add(assocObjects.get(x).getGO());
						}
						if(!tempFunctions.contains(assocObjects.get(x).getGO()))
						{
							tempFunctions.add(assocObjects.get(x).getGO());						
						}
					}
				}
	            geneToFunctionsMap.put(aNode.getIdentifier(), tempFunctions);
		    }
	 		
	 		for(int x=0; x<goObjects.size(); x=x+1)
	 		{
	 			for(int y = 0; y<goFunctions.size(); y=y+1)
	 			{
		 			if(goObjects.get(x).getID().equals(goFunctions.get(y)))
		 			{
		 				functionsToNamesMap.put(goFunctions.get(y), goObjects.get(x).getName());
		 				break;
		 			}
	 			}
	 		}
	 		
	 		goToColorMap = new HashMap<String, Color>();	 		
	 		for(int x = 0; x<goFunctions.size(); x=x+1)
	 		{
	 			Color tempColor = new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256));
	 			while(goToColorMap.containsValue(tempColor))
	 			{
	 				tempColor = new Color(r.nextInt(256),r.nextInt(256),r.nextInt(256));
	 			}
	 			goToColorMap.put(goFunctions.get(x), tempColor);
	 			colors.add(tempColor);
	 		}
	 		
	 		Color[] colorArray = new Color[colors.size()];
	 		for(int x = 0; x<colorArray.length; x=x+1)
	 		{
	 			colorArray[x] = colors.get(x);
	 		}
	 		addColors(colorArray);
	 		
	 		currentNetwork.unselectAllNodes();
	 		currentNetwork.selectAllNodes();
	 		selectedNodes = currentNetwork.getSelectedNodes();
		 	it = selectedNodes.iterator();
		 	//while (it.hasNext()) 
		 	for(int z = 0; z<currentNetwork.nodesList().size(); z=z+1)
		    {
	            //Node aNode = (Node) it.next();
		 		Node aNode = (Node)currentNetwork.nodesList().get(z);
	            ArrayList<String> associatedColorList = new ArrayList<String>();
	            ArrayList<String> associatedFunctions = geneToFunctionsMap.get(aNode.getIdentifier());
	            for(int x = 0; x<associatedFunctions.size(); x=x+1)
	            {
	            	associatedColorList.add(""+goToColorMap.get(associatedFunctions.get(x)).getRGB());
	            }
				cyNodeAttrs.setListAttribute(aNode.getIdentifier(), "list", associatedColorList);
				
				cyNodeAttrs.setAttribute(aNode.getIdentifier(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName(), ""+Color.WHITE);
				if(associatedColorList.size()>1)
				{
					PieChart pie = new PieChart(associatedColorList, aNode.getRootGraphIndex());
					
					NodeView nv = Cytoscape.getCurrentNetworkView().getNodeView(aNode);
					
					if(nv!=null)
					{
						//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "3","Selection Error", JOptionPane.ERROR_MESSAGE);

						DNodeView dnv = (ding.view.DNodeView) nv;
						Rectangle2D rect = new java.awt.geom.Rectangle2D.Double(-5.0, -25.0, 50, 50);
						//Ellipse2D rect = new Ellipse2D.Float(-11.0f, -20.0f, 50.0f, 50.0f);
						//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "4","Selection Error", JOptionPane.ERROR_MESSAGE);

						
						String userDir = System.getProperty("user.dir");
						java.awt.Paint paint;
						try 
						{
							//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "5","Selection Error", JOptionPane.ERROR_MESSAGE);

							String file = userDir+"/"+aNode.getRootGraphIndex()+"image.png";
							paint = new java.awt.TexturePaint(ImageIO.read(new File(file)), rect);
							//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "6","Selection Error", JOptionPane.ERROR_MESSAGE);

							if(dnv!=null)
							{
								//JOptionPane.showMessageDialog(Cytoscape.getDesktop(), "7","Selection Error", JOptionPane.ERROR_MESSAGE);
								dnv.addCustomGraphic(rect, paint, cytoscape.render.stateful.NodeDetails.ANCHOR_WEST);
							}
						} 
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}				
					}
				}
		    }
	 	}
	 	
	 	for(int x = 0; x<goFunctions.size(); x=x+1)
	 	{
	 		String title = goFunctions.get(x) + " - " + functionsToNamesMap.get(goFunctions.get(x));
	 		chosenFunctionTable.setValueAt(title, chosenFunctionTable.getRowCount()-1, 0);
	 		chosenFunctionTable.setValueAt(goToColorMap.get(goFunctions.get(x)), chosenFunctionTable.getRowCount()-1, 1);
	 		chosenFunctionTable.setRowCount(chosenFunctionTable.getRowCount()+1);
	 	}
	 	chosenFunctionTable.setRowCount(chosenFunctionTable.getRowCount()-1);
	 	
	 	for(int x = 0; x<goFunctions.size(); x=x+1)
	 	{
	 		String title = goFunctions.get(x) + " - " + functionsToNamesMap.get(goFunctions.get(x));
	 		legendModel.setValueAt(title, legendModel.getRowCount()-1, 0);
	 		legendModel.setValueAt(goToColorMap.get(goFunctions.get(x)), legendModel.getRowCount()-1, 1);
	 		legendModel.setRowCount(legendModel.getRowCount()+1);
	 	}
	 	chosenFunctionTable.setRowCount(chosenFunctionTable.getRowCount()-1);
	 	
	 	legendFrame.setVisible(true);
	 	currentNetwork.unselectAllNodes();
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
	    if(!theColors.contains(Color.WHITE))
	    {
	    	theColors.add(Color.WHITE);
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
