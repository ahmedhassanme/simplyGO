
import java.awt.Color;

import javax.swing.table.DefaultTableModel;


public class FunctionTableModel extends DefaultTableModel
{
	//Constructor - define the model for the function table
	public FunctionTableModel()
	{
		this.addColumn("Function");
		this.addColumn("Color");
		this.setRowCount(1);
		this.setValueAt("", 0, 0);
		this.setValueAt(Color.white, 0, 1);
	}
	
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
	
	public Class getColumnClass(int c) 
	{
        return getValueAt(0, c).getClass();
    }
}
