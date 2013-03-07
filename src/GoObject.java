
import java.util.ArrayList;


public class GoObject 
{
	//Information representing each go object
	private String goID;
	private String name;
	private String namespace;
	private ArrayList<String> isA;
	
	private ArrayList<GoObject> parents;
	
	//Constructor
	public GoObject(String id, String theName, String theNamespace, ArrayList<String> childOf)
	{
		goID = id;
		name = theName;
		namespace = theNamespace;
		isA = childOf;
	}
	
	//Set a parent for this object
	public void setParent(GoObject aParent)
	{
		parents.add(aParent);
	}
	
	//Get the GO ID
	public String getID()
	{
		return goID;
	}
	
	//Get the name
	public String getName()
	{
		return name;
	}
	
	//Get the namespace
	public String getNamespace()
	{
		return namespace;
	}
	
	//Get the parents
	public ArrayList<String> getParents()
	{
		return isA;
	}
}
