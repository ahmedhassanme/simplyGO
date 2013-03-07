
import giny.model.Node;
import cytoscape.task.TaskMonitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;

public class Resnik 
{
	private Structures structures;
	private CyNetwork currentNetwork;
	private Set selectedNodes;
	private JPanel parent;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<GoObject> goObjects;
	private ArrayList<String> goIDS;
	private ArrayList<ArrayList<String>> parents;
	private String[][] micas;
	private String[][] probabilities;
	private String[][] ic;
	private int bioCount = 0;
	private int cellCount = 0;
	private int molCount = 0;
	private ArrayList<GoObject> bioObjects;
	private ArrayList<GoObject> molObjects;
	private ArrayList<GoObject> cellObjects;
	private ArrayList<String> bioCalculated;
	private ArrayList<String> molCalculated;
	private ArrayList<String> cellCalculated;
	private ArrayList<Integer> bioNumbers;
	private ArrayList<Integer> molNumbers;
	private ArrayList<Integer> cellNumbers;
	private File saveFile;
	private ArrayList<String> hmfNames;
	
	private ArrayList<ArrayList<String>> nodeGOS;
	private ArrayList<String> lowestGOS;
	private ArrayList<ArrayList<String>> lowestGOSParents;
	TaskMonitor taskMonitor;
	
	private HashMap<String, ArrayList<String>> functionsToParentsMap;
	
	public Resnik(Structures data, JPanel top)
	{
		structures = data;
		parent = top;
		//taskMonitor = monitor;
	}
	
	public void calculate()
	{	
	 		//JOptionPane.showMessageDialog(parent, "1","Selection Error", JOptionPane.ERROR_MESSAGE);

		nodeGOS = new ArrayList<ArrayList<String>>();
		lowestGOS = new ArrayList<String>();
		lowestGOSParents  = new ArrayList<ArrayList<String>>();
		hmfNames = new ArrayList<String>();
		functionsToParentsMap = new HashMap<String, ArrayList<String>>();
		
		currentNetwork = Cytoscape.getCurrentNetwork();
		assocObjects = structures.getAssocObjects();
		goObjects = structures.getGoObjects();
		goIDS = new ArrayList<String>();
		parents = new ArrayList<ArrayList<String>>();
		bioObjects = new ArrayList<GoObject>();
		molObjects = new ArrayList<GoObject>();
		cellObjects = new ArrayList<GoObject>();
		bioCalculated = new ArrayList<String>(); 
		molCalculated = new ArrayList<String>(); 		
		cellCalculated = new ArrayList<String>(); 
		bioNumbers = new ArrayList<Integer>(); 
		molNumbers = new ArrayList<Integer>(); 		
		cellNumbers = new ArrayList<Integer>(); 
		
		//JOptionPane.showMessageDialog(parent, "2","Selection Error", JOptionPane.ERROR_MESSAGE);
	
		if(bioCount==0)
		{
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
 		//JOptionPane.showMessageDialog(parent, "Size of trees:"+bioObjects.size()+","+molObjects.size()+","+cellObjects.size(),"Selection Error", JOptionPane.ERROR_MESSAGE);

		selectedNodes = currentNetwork.getSelectedNodes();
	 	Iterator<Node> it = selectedNodes.iterator();
	 	
	 	if(selectedNodes.isEmpty())
	 	{
	 		JOptionPane.showMessageDialog(parent, "No nodes selected.","Selection Error", JOptionPane.ERROR_MESSAGE);
	 	}
	 	else
	 	{	
	 		if(selectedNodes.size()==1)
	 		{
		 		JOptionPane.showMessageDialog(parent, "You must choose more than one node.","Selection Error", JOptionPane.ERROR_MESSAGE);
	 		}
	 		else
	 		{
	 			int nodeCount = 0;
	 			while (it.hasNext()) 
	 		    {
	 		 		//JOptionPane.showMessageDialog(parent, "5","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		    	Node aNode = (Node) it.next();
	 		    	hmfNames.add(aNode.getIdentifier());
	 		    	nodeGOS.add(new ArrayList<String>());
	 		 		//JOptionPane.showMessageDialog(parent, "4","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		    	int maxParentLength = 0;
	 		    	int maxParentIndex = 0;
	 		    	ArrayList<String> tempFunctions = new ArrayList<String>();
	 		    	ArrayList<ArrayList<String>> tempParents = new ArrayList<ArrayList<String>>();
	 		    	for(int x = 0; x<assocObjects.size(); x=x+1)
	 		    	{
	 		    		if(assocObjects.get(x).getProtein().contains(aNode.getIdentifier()))
						{
	 		    			if(!tempFunctions.contains(assocObjects.get(x).getGO()))
	 		    			{
	 		    				tempFunctions.add(assocObjects.get(x).getGO());
	 		    			}
						}
	 		    	}
	 		 		//JOptionPane.showMessageDialog(parent, "3","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		    	for(int x = 0; x<tempFunctions.size(); x=x+1)
	 		    	{//JOptionPane.showMessageDialog(parent, ""+tempFunctions.get(x),"Selection Error", JOptionPane.ERROR_MESSAGE);
	 		    		tempParents.add(findAllParents(tempFunctions.get(x)));
	 		    		//JOptionPane.showMessageDialog(parent, "b","Selection Error", JOptionPane.ERROR_MESSAGE);
	 		    		if(tempParents.get(x).size()>maxParentLength)
	 		    		{
	 		    			maxParentLength = tempParents.get(x).size();
	 		    			maxParentIndex = x;
	 		    		}
	 		    		//JOptionPane.showMessageDialog(parent, "c","Selection Error", JOptionPane.ERROR_MESSAGE);
	 		    	}
	 		 	//	JOptionPane.showMessageDialog(parent, "2","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		    	functionsToParentsMap.put(tempFunctions.get(maxParentIndex), tempParents.get(maxParentIndex));
	 		    	lowestGOSParents.add(tempParents.get(maxParentIndex));
	 		    	lowestGOS.add(tempFunctions.get(maxParentIndex));
	 		 		//JOptionPane.showMessageDialog(parent, "1","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		    	///////////////////////
	 		    	/*for(int y = 0; y<assocObjects.size(); y = y + 1)
					{			
						if(assocObjects.get(y).getProtein().contains(aNode.getIdentifier()))
						{
							if(!nodeGOS.get(nodeCount).contains(assocObjects.get(y).getGO()))
							{
								nodeGOS.get(nodeCount).add(assocObjects.get(y).getGO());
							}
						}
					}
	 		 		JOptionPane.showMessageDialog(parent, ""+nodeGOS.get(nodeCount).size(),"Selection Error", JOptionPane.ERROR_MESSAGE);
	 		    	nodeCount = nodeCount + 1;*/
	 		    	////////////////////
	 		    }
	 			
	 			
	 			///////////////////////////////////////////
	 			/*for(int x = 0; x<nodeGOS.size(); x=x+1)
	 			{
	 				for(int y = 0; y<nodeGOS.get(x).size(); y=y+1)
	 				{
	 					parents.add(findAllParents(nodeGOS.get(x).get(y)));
	 		 	 		//JOptionPane.showMessageDialog(parent, ""+y +" of "+nodeGOS.get(x).size()+":"+parents.get(y).toString(),"TEST", JOptionPane.ERROR_MESSAGE);
	 				}
	 			}
	 			
	 			for(int x = 0; x<nodeGOS.size(); x=x+1)
	 			{
	 				int max = 0;
	 				int keeper = 0;
	 				for(int y = 0; y<nodeGOS.get(x).size(); y=y+1)
	 				{
	 					if(parents.get(y).size()>max)
	 					{
	 						max = parents.get(y).size();
	 						keeper = y;
	 					}
	 				}
	 				for(int z = 0; z<goObjects.size(); z=z+1)
	 				{
	 					if(goObjects.get(z).getName().equals(parents.get(keeper).get(0)))
	 					{
	 						lowestGOS.add(goObjects.get(z).getID());
	 					}
	 				}
	 				//lowestGOS.add(parents.get(keeper).get(0));
	 				for(int y = 0; y<nodeGOS.get(x).size(); y=y+1)
	 				{
	 					parents.remove(0);
	 				}
	 			}
		 	 		//JOptionPane.showMessageDialog(parent, "lowestgos:"+lowestGOS.toString(),"TEST", JOptionPane.ERROR_MESSAGE);

	 			for(int x=0; x<lowestGOS.size(); x=x+1)
	 			{
	 				lowestGOSParents.add(findAllParents(lowestGOS.get(x)));
 		 	 		//JOptionPane.showMessageDialog(parent, "parents:"+lowestGOSParents.get(x).toString(),"TEST", JOptionPane.ERROR_MESSAGE);
	 			}*/
	 			//////////////////////////////
	 			micas = new String[hmfNames.size()][hmfNames.size()];
			    probabilities = new String[hmfNames.size()][hmfNames.size()];
			    ic = new String[hmfNames.size()][hmfNames.size()];
			    
			    for(int x = 0; x<lowestGOSParents.size(); x=x+1)
			    {
			    	String[] ref = new String[lowestGOSParents.get(x).size()];
			    	for(int u = 0; u<ref.length; u=u+1)
			    	{
			    		ref[u] = lowestGOSParents.get(x).get(u);
			    	}

			    	for(int y = x+1; y<lowestGOSParents.size(); y=y+1)
			    	{
			    		ArrayList<String> first = new ArrayList<String>();
				    	for(int w = 0; w<ref.length; w=w+1)
				    	{
				    		first.add(ref[w]);
				    	}
			    		ArrayList<String> second = lowestGOSParents.get(y);
	 		 	 		//JOptionPane.showMessageDialog(parent, "first:"+first.toString(),"TEST", JOptionPane.ERROR_MESSAGE);
	 		 	 		//JOptionPane.showMessageDialog(parent, "second:"+second.toString(),"TEST", JOptionPane.ERROR_MESSAGE);

			    		first.retainAll(second);
			    		
	 		 	 		//JOptionPane.showMessageDialog(parent, "MICA:"+first.toString(),"TEST", JOptionPane.ERROR_MESSAGE);
			    		
			    		if(first.isEmpty())
			 	 		{
			 	 			micas[x][y] = "empty";
			 	 			micas[y][x] = "empty";
			 	 		}
			 	 		else
			 	 		{
			 	 			if(lowestGOSParents.get(x).get(lowestGOSParents.get(x).size()-1).equals("biological_process"))
			 	 			{
			 	 				micas[x][y] = "bp-"+first.get(0);	 
				 	 			micas[y][x] = "bp-"+first.get(0);
			 	 			}
			 	 			else
			 	 			{
			 	 				if(lowestGOSParents.get(x).get(lowestGOSParents.get(x).size()-1).equals("molecular_function"))
			 	 				{
			 	 					micas[x][y] = "mp-"+ first.get(0);	 
					 	 			micas[y][x] = "mp-"+first.get(0);
			 	 				}
			 	 				else
			 	 				{
			 	 					micas[x][y] = "cp-"+first.get(0);	 
					 	 			micas[y][x] = "cp-"+first.get(0);
			 	 				}
			 	 			}
			 	 		}
			    	}
			    }
			    
			    for(int x = 0; x<lowestGOSParents.size(); x=x+1)
			    {
			    	micas[x][x] = "NA";
			    }
		 		//JOptionPane.showMessageDialog(parent, "6","Selection Error", JOptionPane.ERROR_MESSAGE);

			    for(int x = 0; x<micas.length; x=x+1)
			    {
					//taskMonitor.setPercentCompleted(x);
			    	for(int y = 0; y<micas[x].length; y=y+1)
			    	{
			    		if(micas[x][y].equals("empty") || micas[x][y].equals("NA"))
			    		{
			    	 		//JOptionPane.showMessageDialog(parent, "in na","Selection Error", JOptionPane.ERROR_MESSAGE);
			    			probabilities[x][y] = "NA";
			    		}
			    		else 
			    		{
			    			if(micas[x][y].startsWith("bp-"))
			    			{
			    				double occu = findCount(micas[x][y]);
		    					double result = occu/bioObjects.size();
				    	 		//JOptionPane.showMessageDialog(parent, "bp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

		    					probabilities[x][y] = ""+result;
			    				//probabilities[x][y] = ""+ (findCount(micas[x][y]) / bioObjects.size());
			    			}
			    			else
			    			{
			    				if(micas[x][y].startsWith("mp-"))
			    				{
			    					double occu = findCount(micas[x][y]);
			    					double result = occu/molObjects.size();
					    	 		//JOptionPane.showMessageDialog(parent, "mp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

			    					probabilities[x][y] = ""+result;
			    					//probabilities[x][y] = ""+ (findCount(micas[x][y]) / molObjects.size());
			    				}
			    				else
			    				{
			    					double occu = findCount(micas[x][y]);
			    					double result = occu/cellObjects.size();
					    	 		//JOptionPane.showMessageDialog(parent, "cp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

			    					probabilities[x][y] = ""+result;
			    					//probabilities[x][y] = ""+ (findCount(micas[x][y]) / cellObjects.size());
			    				}
			    			}	    			
			    		}
			    	}
			    }
			    
			    for(int x = 0; x<ic.length; x=x+1)
		 		{
		 			for(int y=0; y<ic[x].length; y=y+1)
		 			{
		 				if(probabilities[x][y].equals("NA"))
		 				{
		 					ic[x][y] = "NA";
		 				}
		 				else
		 				{
		 					String check = ""+(-1 * Math.log(Double.parseDouble(probabilities[x][y])) / Math.log(2));
		 					if(check.equals("-0.0"))
		 					{
		 						ic[x][y] = "0.0";
		 					}
		 					else
		 					{
		 						ic[x][y] = check;
		 					}
		 					//ic[x][y] = ""+(-1 * Math.log(Double.parseDouble(probabilities[x][y])) / Math.log(2));
		 				}
		 			}
		 		}
			    
			    double[][] data = new double[ic.length][ic[0].length];
		 		for(int x = 0; x<data.length; x=x+1)
		 		{
		 			for(int y=0; y<data[0].length; y=y+1)
		 			{
		 				if(ic[x][y].equals("NA"))
		 				{
		 					data[x][y] = 0.0;
		 				}
		 				else
		 				{
		 					data[x][y] = Double.parseDouble(ic[x][y]);
		 				}
		 			}
		 		}
		 		
		 		HeatMapFrame hmf;
				try {
					hmf = new HeatMapFrame(data, hmfNames);
					//hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			        hmf.setSize(500,500);
			        hmf.setVisible(true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				//ArrayList<>
				//for(int x = 0; x<lowest)
				
		 		JFileChooser choose = new JFileChooser();
		 		int returnValue = choose.showSaveDialog(null);
	            if (returnValue == JFileChooser.APPROVE_OPTION) 
	            {
	            	saveFile = choose.getSelectedFile();
	            }
		 		try 
	        	{
	        		BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
	        		
	        		bw.write("Most Informative Common Ancestor (MICA)");
	        		bw.newLine();
	        		for(int x = 0; x<micas.length; x=x+1)
	        		{
	        			for(int y = 0; y<micas[x].length; y=y+1)
	        			{
	                		bw.write("["+x+"]["+y+"]"+hmfNames.get(x) + "/" +hmfNames.get(y) +": "+micas[x][y]);
	                		bw.newLine();
	        			}
	        		}
	        		
	        		bw.newLine();
	        		bw.write("Probabilities");
	        		bw.newLine();
	        		for(int x = 0; x<probabilities.length; x=x+1)
	        		{
	        			for(int y = 0; y<probabilities[x].length; y=y+1)
	        			{
	                		bw.write("["+x+"]["+y+"]"+hmfNames.get(x) + "/" +hmfNames.get(y) +": "+probabilities[x][y]);
	                		bw.newLine();
	        			}
	        		}
	        		
	        		bw.newLine();
	        		bw.write("Information Content (IC)");
	        		bw.newLine();
	        		for(int x = 0; x<ic.length; x=x+1)
	        		{
	        			for(int y = 0; y<ic[x].length; y=y+1)
	        			{
	                		bw.write("["+x+"]["+y+"]"+hmfNames.get(x) + "/" +hmfNames.get(y) +": "+ic[x][y]);
	                		bw.newLine();
	        			}
	        		}
	        		
	        		bw.newLine();
	        		bw.write("Most Informative Common Ancestor (MICA) Matrix");
	        		bw.newLine();
	        		bw.write("\t");
	        		for(int x = 0; x<micas.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        		}
	        		bw.newLine();
	        		
	        		for(int x = 0; x<micas.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        			for(int y = 0; y<micas[x].length; y=y+1)
	        			{
	        				bw.write(micas[x][y]);
	        				bw.write("\t");
	        			}
	        			bw.newLine();
	        		}
	        		
	        		bw.newLine();
	        		bw.write("Probabilities Matrix");
	        		bw.newLine();
	        		bw.write("\t");
	        		for(int x = 0; x<probabilities.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        		}
	        		bw.newLine();
	        		
	        		for(int x = 0; x<probabilities.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        			for(int y = 0; y<probabilities[x].length; y=y+1)
	        			{
	        				bw.write(probabilities[x][y]);
	        				bw.write("\t");
	        			}
	        			bw.newLine();
	        		}
	        		
	        		bw.newLine();
	        		bw.write("Information Content (IC) Matrix");
	        		bw.newLine();
	        		bw.write("\t");
	        		for(int x = 0; x<ic.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        		}
	        		bw.newLine();
	        		
	        		for(int x = 0; x<ic.length; x=x+1)
	        		{
	        			bw.write(hmfNames.get(x));
	        			bw.write("\t");
	        			for(int y = 0; y<ic[x].length; y=y+1)
	        			{
	        				bw.write(ic[x][y]);
	        				bw.write("\t");
	        			}
	        			bw.newLine();
	        		}
	        		bw.close();
	        	}
		 		catch(Exception e)
		 		{
		 			
		 		}
	 		}
	 	}
	}
	 		/*//START OF SELECTED SEARCH
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
		    micas = new String[goIDS.size()][goIDS.size()];
		    probabilities = new String[goIDS.size()][goIDS.size()];
		    ic = new String[goIDS.size()][goIDS.size()];
		    //END OF SELECTED SEARCH
		    
	 		//JOptionPane.showMessageDialog(parent, "3","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		for(int x = 0; x<goIDS.size(); x=x+1)
		    {
	 			parents.add(findAllParents(goIDS.get(x)));
	 	 		//JOptionPane.showMessageDialog(parent, ""+x +" of "+goIDS.size()+":"+parents.get(x).toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		    }
	 		//JOptionPane.showMessageDialog(parent, "4","Selection Error", JOptionPane.ERROR_MESSAGE);

		    for(int x = 0; x<goIDS.size(); x=x+1)
		    {
		    	String[] ref = new String[parents.get(x).size()];
		    	for(int u = 0; u<ref.length; u=u+1)
		    	{
		    		ref[u] = parents.get(x).get(u);
		    	}

		    	for(int y = x+1; y<goIDS.size(); y=y+1)
		    	{
		 	 		ArrayList<String> first = new ArrayList<String>();
			    	for(int w = 0; w<ref.length; w=w+1)
			    	{
			    		first.add(ref[w]);
			    	}
		    		ArrayList<String> second = parents.get(y);
		    		first.retainAll(second);
		 	 		if(first.isEmpty())
		 	 		{
		 	 			micas[x][y] = "empty";
		 	 			micas[y][x] = "empty";
		 	 		}
		 	 		else
		 	 		{
		 	 			if(parents.get(x).get(parents.get(x).size()-1).equals("biological_process"))
		 	 			{
		 	 				micas[x][y] = "bp-"+first.get(0);	 
			 	 			micas[y][x] = "bp-"+first.get(0);
		 	 			}
		 	 			else
		 	 			{
		 	 				if(parents.get(x).get(parents.get(x).size()-1).equals("molecular_function"))
		 	 				{
		 	 					micas[x][y] = "mp-"+ first.get(0);	 
				 	 			micas[y][x] = "mp-"+first.get(0);
		 	 				}
		 	 				else
		 	 				{
		 	 					micas[x][y] = "cp-"+first.get(0);	 
				 	 			micas[y][x] = "cp-"+first.get(0);
		 	 				}
		 	 			}
		 	 		}
		    	}
		    }
	 		//JOptionPane.showMessageDialog(parent, "5","Selection Error", JOptionPane.ERROR_MESSAGE);

		    for(int x = 0; x<goIDS.size(); x=x+1)
		    {
		    	micas[x][x] = "NA";
		    }
	 		//JOptionPane.showMessageDialog(parent, "6","Selection Error", JOptionPane.ERROR_MESSAGE);

		    for(int x = 0; x<micas.length; x=x+1)
		    {
		    	for(int y = 0; y<micas[x].length; y=y+1)
		    	{
		    		if(micas[x][y].equals("empty") || micas[x][y].equals("NA"))
		    		{
		    	 		//JOptionPane.showMessageDialog(parent, "in na","Selection Error", JOptionPane.ERROR_MESSAGE);
		    			probabilities[x][y] = "NA";
		    		}
		    		else 
		    		{
		    			if(micas[x][y].startsWith("bp-"))
		    			{
		    				double occu = findCount(micas[x][y]);
	    					double result = occu/bioObjects.size();
			    	 		//JOptionPane.showMessageDialog(parent, "bp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

	    					probabilities[x][y] = ""+result;
		    				//probabilities[x][y] = ""+ (findCount(micas[x][y]) / bioObjects.size());
		    			}
		    			else
		    			{
		    				if(micas[x][y].startsWith("mp-"))
		    				{
		    					double occu = findCount(micas[x][y]);
		    					double result = occu/molObjects.size();
				    	 		//JOptionPane.showMessageDialog(parent, "mp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

		    					probabilities[x][y] = ""+result;
		    					//probabilities[x][y] = ""+ (findCount(micas[x][y]) / molObjects.size());
		    				}
		    				else
		    				{
		    					double occu = findCount(micas[x][y]);
		    					double result = occu/cellObjects.size();
				    	 		//JOptionPane.showMessageDialog(parent, "cp children:"+occu,"Selection Error", JOptionPane.ERROR_MESSAGE);

		    					probabilities[x][y] = ""+result;
		    					//probabilities[x][y] = ""+ (findCount(micas[x][y]) / cellObjects.size());
		    				}
		    			}	    			
		    		}
		    	}
		    }
	 		//JOptionPane.showMessageDialog(parent, "7","Selection Error", JOptionPane.ERROR_MESSAGE);

		    for(int z = 0; z<micas.length; z=z+1)
		    {
		    	ArrayList<String> test = new ArrayList<String>();
		    	for(int zz=0; zz<micas[z].length; zz=zz+1)
		    	{
		    		test.add(micas[z][zz]);
		    	}
	 	 		//JOptionPane.showMessageDialog(parent, ""+test.toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		    }
	 		JOptionPane.showMessageDialog(parent, "8","Selection Error", JOptionPane.ERROR_MESSAGE);

		    for(int z = 0; z<probabilities.length; z=z+1)
		    {
		    	ArrayList<String> test = new ArrayList<String>();
		    	for(int zz=0; zz<probabilities[z].length; zz=zz+1)
		    	{
		    		test.add(probabilities[z][zz]);
		    	}
	 	 		//JOptionPane.showMessageDialog(parent, ""+test.toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		    }
	 		JOptionPane.showMessageDialog(parent, "9","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		for(int x = 0; x<ic.length; x=x+1)
	 		{
	 			for(int y=0; y<ic[x].length; y=y+1)
	 			{
	 				if(probabilities[x][y].equals("NA"))
	 				{
	 					ic[x][y] = "NA";
	 				}
	 				else
	 				{
	 					ic[x][y] = ""+(-1 * Math.log(Double.parseDouble(probabilities[x][y])) / Math.log(2));
	 				}
	 			}
	 		}
	 		//JOptionPane.showMessageDialog(parent, "8","Selection Error", JOptionPane.ERROR_MESSAGE);
	 		
	 		double[][] data = new double[ic.length][ic[0].length];
	 		for(int x = 0; x<data.length; x=x+1)
	 		{
	 			for(int y=0; y<data[0].length; y=y+1)
	 			{
	 				if(ic[x][y].equals("NA"))
	 				{
	 					data[x][y] = 0.0;
	 				}
	 				else
	 				{
	 					data[x][y] = Double.parseDouble(ic[x][y]);
	 				}
	 			}
	 		}
	 		
	 		HeatMapFrame hmf;
			try {
				hmf = new HeatMapFrame(data, goIDS);
				//hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        hmf.setSize(500,500);
		        hmf.setVisible(true);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

	 		JFileChooser choose = new JFileChooser();
	 		int returnValue = choose.showSaveDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) 
            {
            	saveFile = choose.getSelectedFile();
            }
	 		try 
        	{
        		BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
        		
        		for(int x = 0; x<micas.length; x=x+1)
        		{
        			for(int y = x+1; y<micas[x].length; y=y+1)
        			{
                		bw.write("["+x+"]["+y+"]"+goIDS.get(x) + "/" +goIDS.get(y) +": "+micas[x][y]);
                		bw.newLine();
        			}
        		}
        		bw.newLine();
        		for(int x = 0; x<probabilities.length; x=x+1)
        		{
        			for(int y = x+1; y<probabilities[x].length; y=y+1)
        			{
                		bw.write("["+x+"]["+y+"]"+goIDS.get(x) + "/" +goIDS.get(y) +": "+probabilities[x][y]);
                		bw.newLine();
        			}
        		}
        		bw.newLine();
        		for(int x = 0; x<ic.length; x=x+1)
        		{
        			for(int y = x+1; y<ic[x].length; y=y+1)
        			{
                		bw.write("["+x+"]["+y+"]"+goIDS.get(x) + "/" +goIDS.get(y) +": "+ic[x][y]);
                		bw.newLine();
        			}
        		}
        		bw.close();
        	}
	 		catch(Exception e)
	 		{
	 			
	 		}

	 		//JOptionPane.showMessageDialog(parent, "9","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		//write.write();
	 		//JOptionPane.showMessageDialog(parent, "10","Selection Error", JOptionPane.ERROR_MESSAGE);

	 		
	 		for(int z = 0; z<ic.length; z=z+1)
		    {
		    	ArrayList<String> test = new ArrayList<String>();
		    	for(int zz=0; zz<ic[z].length; zz=zz+1)
		    	{
		    		test.add(ic[z][zz]);
		    	}
	 	 		JOptionPane.showMessageDialog(parent, ""+test.toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		    }
	 	}
	}*/
	
	
	public int findCount(String go)
	{	
		int count = 1;
		
		if(go.substring(3).equals("biological_process"))
		{
			count = bioObjects.size();
		}
		else
		{
			if(go.substring(3).equals("molecular_function"))
			{
				count = molObjects.size();
			}
			else
			{
				if(go.substring(3).equals("cellular_component"))
				{
					count = cellObjects.size();
				}
				else
				{
					ArrayList<String> children = new ArrayList<String>();
					
					if(go.startsWith("bp-"))
					{
						if(bioCalculated.contains(go.substring(3)))
						{
							count = bioNumbers.get(bioCalculated.indexOf(go.substring(3)));
						}
						else
						{
						//JOptionPane.showMessageDialog(parent, "bp-","Selection Error", JOptionPane.ERROR_MESSAGE);
						for(int x = 0; x<bioObjects.size(); x=x+1)
						{
							if(bioObjects.get(x).getParents().contains(go.substring(3)))
							{
								children.add(bioObjects.get(x).getName());
							}
						}
						//JOptionPane.showMessageDialog(parent, "before children","Selection Error", JOptionPane.ERROR_MESSAGE);
						
						for(int x = 0; x<children.size(); x=x+1)
						{
					 		//JOptionPane.showMessageDialog(parent, go+": child "+x+" of "+children.size(),"Selection Error", JOptionPane.ERROR_MESSAGE);
							for(int y = 0; y<bioObjects.size(); y=y+1)
							{
								if(bioObjects.get(y).getParents().contains(children.get(x)))
								{
									if(!children.contains(bioObjects.get(y).getName()))
									{
										children.add(bioObjects.get(y).getName());
									}
								}
							}
						}
						//JOptionPane.showMessageDialog(parent, "after children","Selection Error", JOptionPane.ERROR_MESSAGE);			
						count =  children.size() + 1;
						bioCalculated.add(go.substring(3));
						bioNumbers.add(count);
						}
					}
					else
					{
						if(go.startsWith("mp-"))
						{
							if(molCalculated.contains(go.substring(3)))
							{
								count = molNumbers.get(molCalculated.indexOf(go.substring(3)));
							}
							else
							{
							//JOptionPane.showMessageDialog(parent, "mp-","Selection Error", JOptionPane.ERROR_MESSAGE);
							for(int x = 0; x<molObjects.size(); x=x+1)
							{
								if(molObjects.get(x).getParents().contains(go.substring(3)))
								{
									children.add(molObjects.get(x).getName());
								}
							}
							//JOptionPane.showMessageDialog(parent, "before children","Selection Error", JOptionPane.ERROR_MESSAGE);
							
							for(int x = 0; x<children.size(); x=x+1)
							{
						 		//JOptionPane.showMessageDialog(parent, go+": child "+x+" of "+children.size(),"Selection Error", JOptionPane.ERROR_MESSAGE);
								for(int y = 0; y<molObjects.size(); y=y+1)
								{
									if(molObjects.get(y).getParents().contains(children.get(x)))
									{
										if(!children.contains(molObjects.get(y).getName()))
										{
											children.add(molObjects.get(y).getName());
										}
									}
								}
							}
							//JOptionPane.showMessageDialog(parent, "after children","Selection Error", JOptionPane.ERROR_MESSAGE);			
							count =  children.size() + 1;
							molCalculated.add(go.substring(3));
							molNumbers.add(count);
							}
						}
						else
						{
							if(cellCalculated.contains(go.substring(3)))
							{
								count = cellNumbers.get(cellCalculated.indexOf(go.substring(3)));
							}
							else
							{
							//JOptionPane.showMessageDialog(parent, "cp-","Selection Error", JOptionPane.ERROR_MESSAGE);
							for(int x = 0; x<cellObjects.size(); x=x+1)
							{
								if(cellObjects.get(x).getParents().contains(go.substring(3)))
								{
									children.add(cellObjects.get(x).getName());
								}
							}
							//JOptionPane.showMessageDialog(parent, "before children","Selection Error", JOptionPane.ERROR_MESSAGE);
							
							for(int x = 0; x<children.size(); x=x+1)
							{
						 		//JOptionPane.showMessageDialog(parent, go+": child "+x+" of "+children.size(),"Selection Error", JOptionPane.ERROR_MESSAGE);
								for(int y = 0; y<cellObjects.size(); y=y+1)
								{
									if(cellObjects.get(y).getParents().contains(children.get(x)))
									{
										if(!children.contains(cellObjects.get(y).getName()))
										{
											children.add(cellObjects.get(y).getName());
										}
									}
								}
							}
							//JOptionPane.showMessageDialog(parent, "after children","Selection Error", JOptionPane.ERROR_MESSAGE);			
							count =  children.size() + 1;
							cellCalculated.add(go.substring(3));
							cellNumbers.add(count);
							}
						}
					}
				}
			}
		}
		
		return count;
	}
	
	public ArrayList<String> findAllParents(String a)
	{
		ArrayList<ArrayList<String>> parentTrees = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> deepestTree = new ArrayList<ArrayList<String>>();
		

		for(int x = 0; x<goObjects.size(); x=x+1)
		{
			if(goObjects.get(x).getID().equals(a))
			{
				if(goObjects.get(x).getName().equals("biological_process")
						||goObjects.get(x).getName().equals("molecular_function")
						||goObjects.get(x).getName().equals("cellular_component"))
				{
					parentTrees.add(new ArrayList<String>());
					parentTrees.get(0).add(goObjects.get(x).getName());
					break;
				}
				//JOptionPane.showMessageDialog(parent, "!"+a,"Selection Error", JOptionPane.ERROR_MESSAGE);
				//JOptionPane.showMessageDialog(parent, "Parent size:"+goObjects.get(x).getParents().get(0),"Selection Error", JOptionPane.ERROR_MESSAGE);
				for(int y=0; y<goObjects.get(x).getParents().size(); y=y+1)
				{
					//JOptionPane.showMessageDialog(parent, "!"+y,"Selection Error", JOptionPane.ERROR_MESSAGE);
					parentTrees.add(new ArrayList<String>());
					parentTrees.get(y).add(goObjects.get(x).getParents().get(y));
				}
				//JOptionPane.showMessageDialog(parent, "!!","Selection Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
		}
		//for(int x = 0; x<parentTrees.size(); x=x+1)
		//{
			//JOptionPane.showMessageDialog(parent, "Tree "+x+"of "+parentTrees.size()+": "+parentTrees.get(x).toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		//}
		//Go through every parent tree
		for(int x = 0; x<parentTrees.size(); x=x+1)
		{
			/*if(a.equals("GO:0035091"))
			{
				JOptionPane.showMessageDialog(parent, "in parent"+x,"Selection Error", JOptionPane.ERROR_MESSAGE);
			}*/
			//JOptionPane.showMessageDialog(parent, "1","Selection Error", JOptionPane.ERROR_MESSAGE);
			for(int y = 0; y<goObjects.size(); y=y+1)
			{
				if(parentTrees.get(x).get(parentTrees.get(x).size()-1).equals("biological_process")
						||parentTrees.get(x).get(parentTrees.get(x).size()-1).equals("molecular_function")
						||parentTrees.get(x).get(parentTrees.get(x).size()-1).equals("cellular_component"))
				{
					break;
				}
				
				//JOptionPane.showMessageDialog(parent, "2","Selection Error", JOptionPane.ERROR_MESSAGE);
				//Check if we've come across the uppermost GO
				if(goObjects.get(y).getName().equals(parentTrees.get(x).get(parentTrees.get(x).size()-1)))
				{	
					//if(goObjects.get(y).getParents()!=null)
					//{
						//Check if GO has more than one parent
						//If so, create new trees accordingly
						if(goObjects.get(y).getParents().size()>1)
						{
							//JOptionPane.showMessageDialog(parent, "4","Selection Error", JOptionPane.ERROR_MESSAGE);
							//Copy tree so far into new tree branch
							ArrayList<String> copy = new ArrayList<String>();
							//Make new tree big enough to take in copies
							for(int c = 0; c<parentTrees.get(x).size(); c=c+1)
							{
								copy.add("");
							}
							Collections.copy(copy, parentTrees.get(x));
							//JOptionPane.showMessageDialog(parent, "5","Selection Error", JOptionPane.ERROR_MESSAGE);
							//Add new branches to tree
							for(int z = 1; z<goObjects.get(y).getParents().size(); z=z+1)
							{
								copy.add(goObjects.get(y).getParents().get(z));
								parentTrees.add(copy);
							}
							//JOptionPane.showMessageDialog(parent, "6","Selection Error", JOptionPane.ERROR_MESSAGE);
							//Add parent for this branch
							parentTrees.get(x).add(goObjects.get(y).getParents().get(0));
							y = 0;
							//JOptionPane.showMessageDialog(parent, ""+parentTrees.get(x).toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
						}
						//If not, just add the one parent and don't create any new trees
						else
						{
							if(goObjects.get(y).getParents().size()==0)
							{
								
							}
							else
							{
								parentTrees.get(x).add(goObjects.get(y).getParents().get(0));
								y = 0;
							}
						}
					//}
				}
			}
		}
		deepestTree.add(parentTrees.get(0));
		for(int x = 1; x<parentTrees.size(); x=x+1)
		{
			if(parentTrees.get(x).size()>deepestTree.get(0).size())
			{
				deepestTree.remove(0);
				deepestTree.add(parentTrees.get(x));
			}
		}
		
		return deepestTree.get(0);
		//JOptionPane.showMessageDialog(parent, "out","Selection Error", JOptionPane.ERROR_MESSAGE);

		/*for(int x = 0; x<parentTrees.size(); x=x+1)
		{
			JOptionPane.showMessageDialog(parent, "Tree "+x+"of "+parentTrees.size()+": "+parentTrees.get(x).toString(),"Selection Error", JOptionPane.ERROR_MESSAGE);
		}*/
		
		
		/*ArrayList<String> aList = new ArrayList<String>();
		
		for(int x = 0; x<goObjects.size(); x=x+1)
		{
			if(goObjects.get(x).getID().equals(a))
			{
				for(int y=0; y<goObjects.get(x).getParents().size(); y=y+1)
				{
					aList.add(goObjects.get(x).getParents().get(y));
				}
				break;
			}
		}
		
		for(int x = 0; x<aList.size(); x=x+1)
		{
			for(int y = 0; y<goObjects.size(); y=y+1)
			{
				if(goObjects.get(y).getName().equals(aList.get(x)))
				{
					for(int z=0; z<goObjects.get(y).getParents().size(); z=z+1)
					{
						aList.add(goObjects.get(y).getParents().get(z));
					}
				}
			}
		}
		
		if(aList.size()==0)
		{
			for(int x = 0; x<goObjects.size(); x=x+1)
			{
				if(goObjects.get(x).getID().equals(a))
				{
					aList.add(goObjects.get(x).getName());
					break;
				}
			}
		}*/
		
		//return aList;
	}
}
