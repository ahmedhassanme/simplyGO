
import giny.model.Node;
import giny.view.NodeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.render.stateful.CustomGraphic;
import cytoscape.visual.VisualPropertyType;
import ding.view.DNodeView;



public class ResetListener implements ActionListener
{
	private CyNetwork currentNetwork;
	private CyAttributes cyNodeAttrs = Cytoscape.getNodeAttributes();
	
	
	public void actionPerformed(ActionEvent e) 
	{
		currentNetwork = Cytoscape.getCurrentNetwork();
		
		for(int x = 0; x<currentNetwork.nodesList().size(); x=x+1)
		{
			cyNodeAttrs.deleteAttribute(currentNetwork.nodesList().get(x).toString(), VisualPropertyType.NODE_FILL_COLOR.getBypassAttrName());
			
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
		Cytoscape.getCurrentNetworkView().redrawGraph(true,true);
	}
}
