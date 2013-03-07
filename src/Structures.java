
import giny.model.Node;

import java.util.ArrayList;


public class Structures 
{
	private LoadAssociations loadAssociations;
	private LoadGoTree loadGoTree;
	private ArrayList<AssociationObject> assocObjects;
	private ArrayList<GoObject> goObjects;
	ArrayList<Node> path;
	
	public Structures()
	{
		assocObjects = new ArrayList<AssociationObject>();
		loadAssociations  = new LoadAssociations(assocObjects);
		loadAssociations.load();
		
		goObjects = new ArrayList<GoObject>();
		loadGoTree = new LoadGoTree(goObjects);
		loadGoTree.load();
	}	
	
	public ArrayList<AssociationObject> getAssocObjects()
	{
		return assocObjects;
	}
	
	public ArrayList<GoObject> getGoObjects()
	{
		return goObjects;
	}
	
	public void setPath(ArrayList<Node> thePath)
	{
		path = thePath;
	}
	
	public ArrayList<Node> getPath()
	{
		return path;
	}
}
