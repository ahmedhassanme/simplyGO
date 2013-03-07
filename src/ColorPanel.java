
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


public class ColorPanel extends JPanel 
{
	private Structures structures;
	private JFrame treeFrame;
	Dimension buttonSize = new Dimension(200, 20);
	private JButton colorWholeGraphButton;
	private JButton colorSelectedSubgraphButton;
	private JButton openTreeButton;
	private JButton numberColorButton;
	private JButton cycleButton;
	private JButton populateButton;
	private JButton resetGraphButton;
	
	private JScrollPane treeScrollPane;
	private JScrollPane treeScrollPaneBio;
	private JScrollPane treeScrollPaneMol;
	private JScrollPane treeScrollPaneCell;
	private DefaultTreeModel treeModel;
	private DefaultTreeModel treeModelBio;
	private DefaultTreeModel treeModelMol;
	private DefaultTreeModel treeModelCell;
	private JTree goTree;
	
	private JTree goTreeMol;
	private JTree goTreeCell;
	private JTree goTreeBio;
	private DefaultMutableTreeNode bio = new DefaultMutableTreeNode("biological_process");
	private DefaultMutableTreeNode cell = new DefaultMutableTreeNode("cellular_component");
	private DefaultMutableTreeNode mol = new DefaultMutableTreeNode("molecular_function");
	//private DefaultMutableTreeNode top = new DefaultMutableTreeNode("GO Tree");
	private DefaultMutableTreeNode topMol = new DefaultMutableTreeNode("GO Tree Molecular");
	private DefaultMutableTreeNode topBio = new DefaultMutableTreeNode("GO Tree Biological");
	private DefaultMutableTreeNode topCell = new DefaultMutableTreeNode("GO Tree Cellular");
	
	//FUNCTION COLOR TABLE
	private FunctionTableModel chosenFunctionTableModel = new FunctionTableModel();
	private JTable chosenFunctionTable;
	private JScrollPane tableScrollPane;
	private ArrayList<String> evidenceList = new ArrayList<String>();
	private ArrayList<String> checkBoxNames = new ArrayList<String>();
	private JCheckBox[] checkBoxes = new JCheckBox[18];
	
	private JButton colorNodesButton;
	
	private JButton colorGraphButton;
	private JButton colorSubgraphButton;
	private JButton resetButton;
	private JButton generateGoButton;
	private JButton generateAssocButton;
	private JButton loadGoButton;
	private JButton loadAssocButton;
	
	private JLabel goLabel;
	private JLabel assocLabel;
	private JLabel colorLabel;
	
	private JFrame legendFrame;
	private JTable legendTable;
	private FunctionTableModel legendTableModel = new FunctionTableModel();
	private JScrollPane legendTablePane;
	
	
	public ColorPanel(Structures data)
	{
		structures = data;
		treeFrame  = new JFrame();
		legendFrame = new JFrame();
		
		goLabel = new JLabel("GO File Handling");
		assocLabel = new JLabel("Gene Association File Handling");
		colorLabel = new JLabel("Graph Coloring");
		
		generateGoButton = new JButton("Generate GO Tree File");
		GenerateGoListener generateGoListener = new GenerateGoListener(this);
		generateGoButton.addActionListener(generateGoListener);
		
		generateAssocButton = new JButton("Generate Gene Association File");
		GenerateAssociationListener generateAssocListener = new GenerateAssociationListener(this, structures.getAssocObjects());
		generateAssocButton.addActionListener(generateAssocListener);
		
		loadGoButton = new JButton("Load GO Tree File");
		LoadGoTreeListener loadGoListener = new LoadGoTreeListener(this, structures.getGoObjects());
		loadGoButton.addActionListener(loadGoListener);
		
		loadAssocButton = new JButton("Load Gene Association File");
		LoadAssociationsListener loadAssocListener = new LoadAssociationsListener(this, structures.getAssocObjects());
		loadAssocButton.addActionListener(loadAssocListener);
		
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.add(goLabel);
		this.add(generateGoButton);
		this.add(loadGoButton);
		this.add(assocLabel);
		this.add(generateAssocButton);
		this.add(loadAssocButton);
		this.add(colorLabel);
		//this.add(numberColorButton);
		
		colorWholeGraphButton = new JButton("Color Graph");
    	//this.add(colorWholeGraphButton);
    	
    	colorSelectedSubgraphButton = new JButton("Color Selected Subgraph");
    	//this.add(colorSelectedSubgraphButton);
    	
    	openTreeButton = new JButton("Custom Color Graph...");
    	OpenTreeFrameListener openTreeListener = new OpenTreeFrameListener(treeFrame);
    	openTreeButton.addActionListener(openTreeListener);
    	this.add(openTreeButton);
    	
    	/*populateButton = new JButton("Populate Trees");
    	PopulateListener populateListener = new PopulateListener(structures);
    	populateButton.addActionListener(populateListener);
    	treeFrame.add(populateButton);*/
		
		treeModelMol = new DefaultTreeModel(topMol);
    	treeModelBio = new DefaultTreeModel(topBio);
    	treeModelCell = new DefaultTreeModel(topCell);
    	goTreeMol = new JTree(treeModelMol);
    	goTreeCell = new JTree(treeModelCell);
    	goTreeBio = new JTree(treeModelBio);
    	
    	topBio.add(bio);
		//bio.add(new DefaultMutableTreeNode("GO:0008150"));				
		topCell.add(cell);
		//cell.add(new DefaultMutableTreeNode("GO:0005575"));				
		topMol.add(mol);
		//mol.add(new DefaultMutableTreeNode("GO:0003674"));				
		TreeListener treeListenerMol = new TreeListener(structures, treeModelMol, 2, treeFrame);
		TreeListener treeListenerBio = new TreeListener(structures, treeModelBio, 1, treeFrame);
		TreeListener treeListenerCell = new TreeListener(structures, treeModelCell, 3, treeFrame);
		goTreeMol.addTreeWillExpandListener(treeListenerMol);
		goTreeCell.addTreeWillExpandListener(treeListenerCell);
		goTreeBio.addTreeWillExpandListener(treeListenerBio);
		treeScrollPaneBio = new JScrollPane(goTreeBio);
		treeScrollPaneMol = new JScrollPane(goTreeMol);
		treeScrollPaneCell = new JScrollPane(goTreeCell);	
		treeScrollPaneBio.setPreferredSize(new Dimension(300,300));
		treeScrollPaneMol.setPreferredSize(new Dimension(300,300));
		treeScrollPaneCell.setPreferredSize(new Dimension(300,300));
		treeFrame.add(treeScrollPaneBio);
		treeFrame.add(treeScrollPaneMol);
		treeFrame.add(treeScrollPaneCell);		
		treeFrame.setSize(1000,500);
		
		JButton addBioFunctionButton = new JButton("Add Chosen Biological Function");
		AddFunctionListener addFunctionListenerBio = new AddFunctionListener(chosenFunctionTableModel, goTreeBio, this);
		addBioFunctionButton.addActionListener(addFunctionListenerBio);
		addBioFunctionButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addBioFunctionButton);
		
		JButton addMolFunctionButton = new JButton("Add Chosen Molecular Function");
		AddFunctionListener addFunctionListenerMol = new AddFunctionListener(chosenFunctionTableModel, goTreeMol, this);
		addMolFunctionButton.addActionListener(addFunctionListenerMol);
		addMolFunctionButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addMolFunctionButton);
		
		JButton addCellFunctionButton = new JButton("Add Chosen Cellular Function");
		AddFunctionListener addFunctionListenerCell = new AddFunctionListener(chosenFunctionTableModel, goTreeCell, this);
		addCellFunctionButton.addActionListener(addFunctionListenerCell);
		addCellFunctionButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addCellFunctionButton);
		
		JButton addAllBioButton = new JButton("Add All Biological Functions");
		AddAllFunctionsListener addBioFunctionsListener = new AddAllFunctionsListener(chosenFunctionTableModel, goTreeBio, this);
		addAllBioButton.addActionListener(addBioFunctionsListener);
		addAllBioButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addAllBioButton);
		
		JButton addAllMolButton = new JButton("Add All Molecular Functions");
		AddAllFunctionsListener addMolFunctionsListener = new AddAllFunctionsListener(chosenFunctionTableModel, goTreeMol, this);
		addAllMolButton.addActionListener(addMolFunctionsListener);
		addAllMolButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addAllMolButton);
		
		JButton addAllCellButton = new JButton("Add All Cellular Functions");
		AddAllFunctionsListener addCellFunctionsListener = new AddAllFunctionsListener(chosenFunctionTableModel, goTreeCell, this);
		addAllCellButton.addActionListener(addCellFunctionsListener);
		addAllCellButton.setPreferredSize(new Dimension (300,20));
		treeFrame.add(addAllCellButton);

		/*JLabel tableLabel = new JLabel("                 Functions Table                                                   ");
		treeFrame.add(tableLabel);
		
		JLabel evidenceLabel = new JLabel("                                       Evidence Codes");
		treeFrame.add(evidenceLabel);*/
		
		chosenFunctionTable = new JTable(chosenFunctionTableModel);	
		tableScrollPane = new JScrollPane(chosenFunctionTable);
		chosenFunctionTable.setFillsViewportHeight(true);
		tableScrollPane.setPreferredSize(new Dimension(500,100));
		chosenFunctionTable.getColumnModel().getColumn(0).setPreferredWidth(180);	
		ChooseColorListener chooseColorListener = new ChooseColorListener(chosenFunctionTable);
		chosenFunctionTable.addMouseListener(chooseColorListener);
		ColorRenderer rend = new ColorRenderer(false);
		chosenFunctionTable.setDefaultRenderer(Color.class, rend);	
		treeFrame.add(tableScrollPane);
		
		legendTable = new JTable(legendTableModel);	
		legendTablePane = new JScrollPane(legendTable);
		legendTable.setFillsViewportHeight(true);
		legendTablePane.setPreferredSize(new Dimension(500,160));
		legendTable.getColumnModel().getColumn(0).setPreferredWidth(180);	
		ChooseColorListener legendColorListener = new ChooseColorListener(legendTable);
		legendTable.addMouseListener(legendColorListener);
		ColorRenderer legendRend = new ColorRenderer(false);
		legendTable.setDefaultRenderer(Color.class, legendRend);	
		legendFrame.add(legendTablePane);
		legendFrame.setTitle("Legend");
		
		checkBoxNames.add("EXP");
		checkBoxNames.add("IPI");
		checkBoxNames.add("IGI");
		checkBoxNames.add("TAS");
		checkBoxNames.add("IEA");
		checkBoxNames.add("ISS");
		checkBoxNames.add("IDA");
		checkBoxNames.add("IC");
		checkBoxNames.add("IEP");
		checkBoxNames.add("NR");
		checkBoxNames.add("ISO");
		checkBoxNames.add("ISM");
		checkBoxNames.add("IGC");
		checkBoxNames.add("NAS");
		checkBoxNames.add("ISA");
		checkBoxNames.add("IMP");
		checkBoxNames.add("RCA");
		checkBoxNames.add("ND");
		
		JPanel test = new JPanel();
		test.setPreferredSize(new Dimension(400, 100));
		
		for(int x = 0; x<checkBoxes.length; x = x + 1)
		{
			checkBoxes[x] = new JCheckBox(checkBoxNames.get(x));
			checkBoxes[x].setSelected(true);
			evidenceList.add(checkBoxNames.get(x));
			CheckBoxListener radioListener = new CheckBoxListener(checkBoxes[x], evidenceList);
			checkBoxes[x].addActionListener(radioListener);
			//this.add(checkBoxes[x]);
			//treeFrame.add(checkBoxes[x]);
			test.add(checkBoxes[x]);
			checkBoxes[x].setVisible(false);
		}
		treeFrame.add(test);	
		
		numberColorButton = new JButton("Auto Color Graph");
		AutoColorGraphListener autoColorGraphListener = new AutoColorGraphListener(structures, this, chosenFunctionTableModel, legendFrame, legendTableModel);
		numberColorButton.addActionListener(autoColorGraphListener);
		numberColorButton.setPreferredSize(new Dimension (225,40));
		treeFrame.add(numberColorButton);
		
		colorGraphButton = new JButton("Color Whole Graph");
		ColorNodesListener colorNodesListener = new ColorNodesListener(structures.getAssocObjects(), evidenceList, chosenFunctionTableModel, this, 0);
		colorGraphButton.addActionListener(colorNodesListener);
		colorGraphButton.setPreferredSize(new Dimension(225,40));
		treeFrame.add(colorGraphButton);
		
		colorSubgraphButton = new JButton("Color Selected Subgraph");
		ColorNodesListener colorSubgraphListener = new ColorNodesListener(structures.getAssocObjects(), evidenceList, chosenFunctionTableModel, this, 1);
		colorSubgraphButton.addActionListener(colorSubgraphListener);
		colorSubgraphButton.setPreferredSize(new Dimension(225,40));
		treeFrame.add(colorSubgraphButton);
		
		JButton resetButton = new JButton("Reset");
		TreeFrameResetListener treeFrameResetListener = new TreeFrameResetListener(checkBoxes, chosenFunctionTableModel, goTreeBio, goTreeMol, goTreeCell);
		resetButton.addActionListener(treeFrameResetListener);
		resetButton.setPreferredSize(new Dimension(225,40));
		treeFrame.add(resetButton);
		
		legendFrame.setLayout(new FlowLayout());
		legendFrame.setSize(500,200);
		legendFrame.setResizable(false);
		legendFrame.setVisible(false);
		
		treeFrame.setLayout(new FlowLayout());
		treeFrame.setSize(1000, 550);
		treeFrame.setResizable(false);
		treeFrame.setVisible(false);
		
		resetGraphButton = new JButton("Reset Graph");
		ResetListener resetListener = new ResetListener();
		resetGraphButton.addActionListener(resetListener);
		this.add(resetGraphButton);
	}
}