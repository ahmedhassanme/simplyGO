
import java.awt.GridLayout;

import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import cytoscape.plugin.CytoscapePlugin;
import cytoscape.view.cytopanels.CytoPanelImp;
import cytoscape.Cytoscape;

public class Main extends CytoscapePlugin 
{
	private Structures structures;
	
    public Main() 
    {	
    	structures = new Structures();
		CytoPanelImp ctrlPanel = (CytoPanelImp) Cytoscape.getDesktop().getCytoPanel(SwingConstants.WEST); 
		
		ColorPanel colorPanel = new ColorPanel(structures);
		colorPanel.setPreferredSize(new java.awt.Dimension(250, 500));
		colorPanel.setLayout(new GridLayout(9, 0));
		JScrollPane colorScroller = new JScrollPane(colorPanel);
		ctrlPanel.add("simplyGO : Color", colorScroller);
		int indexInCytoPanel = ctrlPanel.indexOfComponent("simplyGO : Color");
		ctrlPanel.setSelectedIndex(indexInCytoPanel);
		
		CalculationPanel distancePanel = new CalculationPanel(structures);
		distancePanel.setPreferredSize(new java.awt.Dimension(250, 500));
		distancePanel.setLayout(new GridLayout(8, 0));
		JScrollPane distanceScroller = new JScrollPane(distancePanel);
		ctrlPanel.add("simplyGO : SPA", distanceScroller);
		
		OverrepPanel overrepPanel = new OverrepPanel(structures);
		overrepPanel.setPreferredSize(new java.awt.Dimension(250, 500));
		overrepPanel.setLayout(new GridLayout(7, 0));
		JScrollPane overrepScroller = new JScrollPane(overrepPanel);
		ctrlPanel.add("simplyGO : Semantic similarity", overrepScroller);
    }
}