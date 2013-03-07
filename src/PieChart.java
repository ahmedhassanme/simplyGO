
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

//Based on code found at http://www.exampledepot.com/egs/java.awt/Pie.html

public class PieChart 
{
	private PieValue[] slices;
	private int imageNumber;

    public PieChart(ArrayList<String> things, int number) 
    {
    	imageNumber = number;
    	slices = new PieValue[things.size()];
    	for(int x = 0; x<slices.length; x=x+1)
    	{
    		Color theColor = new Color(Integer.parseInt(things.get(x)));
    		slices[x] = new PieValue(20, theColor);
    	}
        
        //BufferedImage bi = new BufferedImage(300, 180, BufferedImage.TYPE_INT_RGB);
        BufferedImage bi = new BufferedImage(300, 180, BufferedImage.TYPE_4BYTE_ABGR);

        
	    Graphics2D g = bi.createGraphics();
	    
	 // Get total value of all slices
	    double total = 0.0D;
	    for (int i=0; i<slices.length; i++) 
	    {
	        total += slices[i].value;
	    }
	
	    // Draw each pie slice
	    double curValue = 0.0D;
	    int startAngle = 0;
	    for (int i=0; i<slices.length; i++) 
	    {
	        // Compute the start and stop angles
	        startAngle = (int)(curValue * 360 / total);
	        int arcAngle = (int)(slices[i].value * 360 / total);
	
	        // Ensure that rounding errors do not leave a gap between the first and last slice
	        if (i == slices.length-1) {
	            arcAngle = 360 - startAngle;
	        }
	
	        // Set the color and draw a filled arc
	        g.setColor(slices[i].color);
	        g.fillArc(0, 0, 300, 178, startAngle, arcAngle);
	        
	        curValue += slices[i].value;
	    }
	    
	    try 
	    {
	    	String userDir = System.getProperty("user.dir");
	    	String file = userDir+"/"+imageNumber+"image.png";
			ImageIO.write(bi, "PNG", new File(file));
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
			System.out.println("no");
		}
    }
}
