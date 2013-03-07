

public class AssociationObject 
{
	//Information stored in an association object
	private String goID;
	private String protein;
	private String evidence;
	
	//Constructor
	public AssociationObject(String go, String prot, String evi)
	{
		goID = go;
		protein = prot;
		evidence = evi;
	}
	
	//Get the the GO ID
	public String getGO()
	{
		return goID;
	}
	
	//Get the protein
	public String getProtein()
	{
		return protein;
	}
	
	//Get the evidence code
	public String getEvidence()
	{
		return evidence;
	}
}
