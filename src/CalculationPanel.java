
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CalculationPanel extends JPanel
{
	private Structures structures;
	
	//TEXT FIELDS
	private JTextField testField;
	private JTextField proteinOneField;
	private JTextField proteinTwoField;
	
	//BUTTONS
	private JButton clusteringCoefficientButton;
	private JButton shortestPathButton;
	private JButton shortestSelectedButton;
	private JButton savePathButton;
	private JButton spaceButton;
	private JButton resetGraphButton;
	
	private JList list;
	private JPanel listPanel;
	
	private JTextArea listField;
	private JScrollPane listPane;
	
	private JButton findFunctionsButton;
	private JLabel pathLabel;
	
	public CalculationPanel(Structures data)
	{
		structures = data;
	
		pathLabel = new JLabel("Shortest Path Information");
		
		listField = new JTextArea();
		listPane = new JScrollPane(listField);
		listPane.setPreferredSize(new Dimension(250, 250));
		//listField.setPreferredSize(new Dimension(250, 250));
		listField.setEditable(false);
		//this.add(listField);
		
		testField = new JTextField();
		testField.setPreferredSize(new Dimension(250, 42));
		//this.add(testField);
		
		proteinOneField = new JTextField();
    	proteinTwoField = new JTextField();
    	proteinOneField.setPreferredSize(new java.awt.Dimension(200, 20));
    	proteinTwoField.setPreferredSize(new java.awt.Dimension(200, 20));
		
		shortestPathButton = new JButton("Shortest Path Between Nodes:");
    	ShortestPathListener shortestPathListener = new ShortestPathListener(this, listField, proteinOneField, proteinTwoField, structures);
    	shortestPathButton.addActionListener(shortestPathListener);
		
		shortestSelectedButton = new JButton("Shortest Path Between Selected Nodes");
    	ShortestPathListener shortestSelectedPathListener = new ShortestPathListener(this, listField, structures);
    	shortestSelectedButton.addActionListener(shortestSelectedPathListener);
    	
    	findFunctionsButton = new JButton("Functions of Selected Nodes");
    	FindFunctionsListener findFunctionsListener = new FindFunctionsListener(this, structures);
    	findFunctionsButton.addActionListener(findFunctionsListener);
    	
    	savePathButton = new JButton("Save Path Information");
    	SavePathListener savePathListener = new SavePathListener(this, listField);
    	savePathButton.addActionListener(savePathListener);
    	
    	this.add(shortestSelectedButton);
    	this.add(shortestPathButton);
    	this.add(proteinOneField);
    	this.add(proteinTwoField);
    	this.add(pathLabel);
		this.add(listPane);
    	//this.add(findFunctionsButton);
       	//this.add(clusteringCoefficientButton);
       	this.add(savePathButton);
       	
       	resetGraphButton = new JButton("Reset Graph");
		ResetListener resetListener = new ResetListener();
		resetGraphButton.addActionListener(resetListener);
		this.add(resetGraphButton);
	}
}
