
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class OpenTreeFrameListener implements ActionListener
{
	private JFrame treeFrame;

	public OpenTreeFrameListener(JFrame frame)
	{
		treeFrame = frame;
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		treeFrame.setVisible(true);
	}

}
