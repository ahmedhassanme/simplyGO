
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

//Based on code found at http://www.exampledepot.com/egs/java.awt/Pie.html
// Class to hold a value for a slice
public class PieValue 
{
    double value;
    Color color;

    public PieValue(double value, Color color) 
    {
        this.value = value;
        this.color = color;
    }
}