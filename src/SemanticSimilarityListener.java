
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;


public class SemanticSimilarityListener implements ActionListener
{
	private Structures structures;
	private Resnik rensick;
	
	public SemanticSimilarityListener(Structures data, JPanel top)
	{
		structures = data;
		rensick = new Resnik(structures, top);
	}

	public void actionPerformed(ActionEvent e) 
	{
		rensick.calculate();
	}

}
