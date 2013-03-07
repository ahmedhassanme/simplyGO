
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JTable;


public class ChooseColorListener implements MouseListener 
{
	private JButton chooseColorButton;
	private JButton closeButton;
	private JColorChooser colorChooser;
	private JTable table;
	private JFrame frame;
	
	public ChooseColorListener(JTable theTable)
	{
		table = theTable;
	}

	public void mouseClicked(MouseEvent e) 
	{
		if (e.getClickCount() == 2)
		{
			colorChooser = new JColorChooser();
			chooseColorButton = new JButton("Select Chosen Color");
			SelectColorListener selectColorListener = new SelectColorListener();
			chooseColorButton.addActionListener(selectColorListener);
			frame = new JFrame("JColorChooser Sample Popup");
			colorChooser.setPreferredSize(new Dimension(422,324));
			chooseColorButton.setPreferredSize(new Dimension(100,50));
			//frame.setUndecorated(true);
			frame.add(colorChooser, BorderLayout.CENTER);
			frame.add(chooseColorButton, BorderLayout.SOUTH);
			frame.pack();
			frame.setVisible(true);	
			frame.setAlwaysOnTop(true);
		}
	}

	public void mouseEntered(MouseEvent e) 
	{
		
		
	}

	public void mouseExited(MouseEvent e) 
	{
		
		
	}

	public void mousePressed(MouseEvent e) 
	{
		
		
	}

	public void mouseReleased(MouseEvent e) 
	{
		
		
	}
	
	public class SelectColorListener implements ActionListener
	{

		public void actionPerformed(ActionEvent e) 
		{
			table.setValueAt(colorChooser.getColor(), table.getSelectedRow(), 1);
			frame.dispose();
			//tableModel.setValueAt(chooser.getColor(), table.getSelectedRow(), 1);
			//String test = (String)chosenFunctionTable.getValueAt(chosenFunctionTable.getSelectedRow(), 0);
		}
		
	}

}
