
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import cytoscape.Cytoscape;


public class OverrepPanel extends JPanel
{
	private Structures structures;
	
	private JTextField numberTestField;
	private JTextField numberDrawnField;
	private JTextField goField;
	private JTextField pField;
	
	private JLabel numberTestLabel;
	private JLabel numberDrawnLabel;
	private JLabel goLabel;
	private JLabel inLabel;
	private JLabel overrepLabel;
	
	private JButton pSelectedButton;
	private JButton pPathButton;
	private JButton findFunctionsButton;
	private JButton calculateSSButton;
	private JButton resetGraphButton;
	
	private JButton edgeButton;
	private Dimension buttonSize;
	
	public OverrepPanel(Structures struct)
	{
		structures = struct;
		buttonSize = new Dimension(100,100);
		
		//overrepLabel = new JLabel("GO Overrepresentation Calculation");
		
		findFunctionsButton = new JButton("Find Functions of Selected Nodes");
		FindFunctionsListener findFunctionsListener = new FindFunctionsListener(this, structures);
		findFunctionsButton.setSize(buttonSize);
		findFunctionsButton.addActionListener(findFunctionsListener);
		
		calculateSSButton = new JButton("Semantic Similarity of Selected Nodes");
		SemanticSimilarityListener ssListener = new SemanticSimilarityListener(struct, this);
		calculateSSButton.addActionListener(ssListener);
		
		/*numberTestLabel = new JLabel("Probablity that:");
		numberDrawnLabel = new JLabel("Of:");
		inLabel = new JLabel("    come under the following GO label");
		goLabel = new JLabel("GO:");
		
		numberTestField = new JTextField("enter number");
		numberTestField.setPreferredSize(new Dimension(150,20));
		numberDrawnField = new JTextField("enter number");
		numberDrawnField.setPreferredSize(new Dimension(200,20));
		goField  = new JTextField("enter GO label");
		goField.setPreferredSize(new Dimension(150,20));
		pField = new JTextField();
		pField.setPreferredSize(new Dimension(200,20));
		pField.setEditable(false);
		
		pSelectedButton = new JButton("Selected Nodes");
		pListener theListener = new pListener(numberDrawnField, numberTestField, goField, structures, pField);
		pSelectedButton.addActionListener(theListener);
		
		pPathButton = new JButton("Last Found Path");
		pPathListener theOtherListener = new pPathListener(numberDrawnField, numberTestField, goField, structures, pField);
		pPathButton.addActionListener(theOtherListener);*/
		
		

		this.add(findFunctionsButton);
		this.add(calculateSSButton);
		
		resetGraphButton = new JButton("Reset Graph");
		ResetListener resetListener = new ResetListener();
		resetGraphButton.addActionListener(resetListener);
		this.add(resetGraphButton);
		//this.add(edgeButton);
		//this.add(overrepLabel);
		//this.add(numberTestLabel);
		//this.add(numberTestField);
		//this.add(numberDrawnLabel);
		//this.add(numberDrawnField);
		//this.add(inLabel);
		//this.add(goLabel);
		//this.add(goField);
		//this.add(pSelectedButton);
		//this.add(pPathButton);
		//this.add(pField);
	}

}
