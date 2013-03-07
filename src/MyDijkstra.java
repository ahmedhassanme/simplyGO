
import giny.model.Edge;
import giny.model.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;

//Based on the code found at http://renaud.waldura.com/doc/java/dijkstra/

public class MyDijkstra
{
	//private static final Float INFINITE_DISTANCE = Float.MAX_VALUE;
	private static final int INITIAL_CAPACITY = 8;
	float result = 0;
	float theResult;
	Node allEnd;
	ArrayList<Node> route;
	
	private CyNetwork currentNetwork;
	private CyAttributes cyEdgeAttrs;
	
	//Constructor
	public MyDijkstra(CyNetwork theNetwork)
	{
		currentNetwork = theNetwork;
		//cyEdgeAttrs = Cytoscape.getEdgeAttributes();
	}
	
	//Get the route
	public ArrayList<Node> getRoute()
	{
		return route;
	}
	
	//Find the shortest path between proteins start and end
	public float findPath(Node start, Node end)
	{
		//currentNetwork = Cytoscape.getCurrentNetwork();
		cyEdgeAttrs = Cytoscape.getEdgeAttributes();
		
		settledNodes.clear();
        unsettledNodes.clear();       
        shortestDistances.clear();
        predecessors.clear();

        unsettledNodes.add(start);
		setShortestDistance(start, 0);
		
		while (!unsettledNodes.isEmpty())
	    {
	        // get the node with the shortest distance
	        Node u = extractMin();

	        // destination reached, stop
	        if (u == end) break;

	        settledNodes.add(u);
	        relaxNeighbors(u);
	    }
		
		route = new ArrayList<Node>();

		 for (Node protein = end; protein != null; protein = getPredecessor(protein))
		 {
		     route.add(protein);
		 }

		 Collections.reverse(route);

		 for(int x = 0; x<route.size();x=x+1)
		 {		 
			 result = getShortestDistance(route.get(x));
			 //System.out.println(route.get(x).getName()+ " : "+result);
		 }
		 
		 return result;
	}

	//Get the distance
	public float getResult()
	{
		return theResult;
	}
	
	//Relax neighbours
	public void relaxNeighbors(Node protein)
	{
		//Arrays.asList(currentNetwork.neighborsArray(protein.getRootGraphIndex()));
		//currentNetwork.edgesList(protein, currentNetwork.getNode(adjacent[x])).get(0);
		System.out.println("node: "+protein.getIdentifier());
		System.out.println(protein.getRootGraphIndex());
		int[] adjacent = currentNetwork.neighborsArray(protein.getRootGraphIndex());
		System.out.println(currentNetwork.getNode(adjacent[0]));
		for(int x = 0; x<adjacent.length; x = x + 1)
		{
			if(!isSettled(currentNetwork.getNode(adjacent[x])))
			{
				System.out.println("the egde: "+currentNetwork.edgesList(protein, currentNetwork.getNode(adjacent[x])));
				System.out.println("the egde: "+currentNetwork.edgesList(currentNetwork.getNode(adjacent[x]), protein));
				
				if(currentNetwork.edgeExists(currentNetwork.getNode(adjacent[x]), protein))
				{
					Edge theEdge = (Edge)currentNetwork.edgesList(currentNetwork.getNode(adjacent[x]), protein).get(0);
					
					if(getShortestDistance(currentNetwork.getNode(adjacent[x])) > (getShortestDistance(protein) + Float.parseFloat((String)(cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))))
							|| getShortestDistance(currentNetwork.getNode(adjacent[x]))==0)
					{				
						setShortestDistance(currentNetwork.getNode(adjacent[x]), (getShortestDistance(protein) + Float.parseFloat((String)cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))));					
						//For all nodes
						setAllShortestDistance(currentNetwork.getNode(adjacent[x]), (getShortestDistance(protein) + Float.parseFloat((String)cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))));
						setPredecessor(currentNetwork.getNode(adjacent[x]), protein);
						unsettledNodes.add(currentNetwork.getNode(adjacent[x]));
					}
				}
				else
				{
					Edge theEdge = (Edge)currentNetwork.edgesList(protein, currentNetwork.getNode(adjacent[x])).get(0);
					
					if(getShortestDistance(currentNetwork.getNode(adjacent[x])) > (getShortestDistance(protein) + Float.parseFloat((String)(cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))))
							|| getShortestDistance(currentNetwork.getNode(adjacent[x]))==0)
					{				
						setShortestDistance(currentNetwork.getNode(adjacent[x]), (getShortestDistance(protein) + Float.parseFloat((String)cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))));					
						//For all nodes
						setAllShortestDistance(currentNetwork.getNode(adjacent[x]), (getShortestDistance(protein) + Float.parseFloat((String)cyEdgeAttrs.getAttribute(theEdge.getIdentifier(), "interaction"))));					
						setPredecessor(currentNetwork.getNode(adjacent[x]), protein);
						unsettledNodes.add(currentNetwork.getNode(adjacent[x]));
					}
				}
			}
		}
	}

	
	//						 //
//////Set of SETTLED PROTEINS//////
	//                       //
	private final Set<Node> settledNodes = new HashSet<Node>();

	private boolean isSettled(Node p)
	{
	    return settledNodes.contains(p);
	}

	//						   //
//////Map of SHORTEST DISTANCES//////
	//                         //
	private final Map<Node, Float> shortestDistances = new HashMap<Node, Float>();

	private void setShortestDistance(Node protein, float distance)
	{	
	    shortestDistances.put(protein, distance);
	}

	public Float getShortestDistance(Node protein)
	{
		//Float check = INFINITE_DISTANCE;
		Float check = 0f;
	    if(shortestDistances.get(protein)!=null)
	    {
	    	check = shortestDistances.get(protein);
	    }
	    
	    return check;
	}
	
	//						   //
//////Overall Map of SHORTEST DISTANCES//////
	//                         //
	private final Map<Node, Float> allShortestDistances = new HashMap<Node, Float>();

	private void setAllShortestDistance(Node protein, float distance)
	{	
	    allShortestDistances.put(protein, distance);
	}

	public Float getAllShortestDistance(Node protein)
	{
		//Float check = INFINITE_DISTANCE;
		Float check = 0f;
	    if(allShortestDistances.get(protein)!=null)
	    {
	    	check = allShortestDistances.get(protein);
	    }
	    
	    return check;
	}


	//						     //
//////Map of PREDECESSOR VERTICES//////
	//                           //
	private final Map<Node, Node> predecessors = new HashMap<Node, Node>();

	private void setPredecessor(Node a, Node b)
	{
	    predecessors.put(a, b);
	}

	public Node getPredecessor(Node protein)
	{
	    return predecessors.get(protein);
	}

	//						                  //
//////Queue of UNSETTELED NODES and COMPARATOR//////
	//                                        //
	private final Comparator<Node> shortestDistanceComparator = new Comparator<Node>()
    {
        public int compare(Node left, Node right)
        {
            float shortestDistanceLeft = getShortestDistance(left);
            float shortestDistanceRight = getShortestDistance(right);

            	if (shortestDistanceLeft > shortestDistanceRight)
                {
                    return +1;
                }
                else if (shortestDistanceLeft < shortestDistanceRight)
                {
                    return -1;
                }
                else // equal
                {
                    //return left.compareTo(right);
                	return 0;
                }                    
        }
    };

    private final PriorityQueue<Node> unsettledNodes = new PriorityQueue<Node>(INITIAL_CAPACITY, shortestDistanceComparator);

	private Node extractMin()
	{
		return unsettledNodes.poll();
	}
}