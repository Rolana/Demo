

import fontastic.*;
import processing.core.PVector;

public class FontCreation{
	String serv;
	int[][] Data;
	char[] Cs;
	String User;
	int [] w;
	String userID;
	int [] h;
	Fontastic f;	
	
	public FontCreation(int[][] charData, char[] characters, int[] width, int[] height,String ID,String serv)
	{
		this.serv = serv;
		Data = charData;
		Cs = characters;
		w = width;
		h = height;
		userID = ID;
	}
	
	public void makeFont()
	{
		Fontastic font = new Fontastic(userID,serv);  // Create a new Fontastic object

    	font.setAuthor("Loaay Alkherbawy");                  // Set author name - will be saved in TTF file too
    	font.getEngine().setBaseline(0);
    	font.getEngine().setMeanline(5);


    	PVector[] points = {};
    	if(Data.length != 0)
    	{
    		for(int i = 0 ; i < Data.length ; i++)
    		{
    			font.addGlyph(Cs[i]);
    			for(int j = 0 ; j < Data[i].length; j++)
    			{
    				if(Data[i][j] == 0 || Data[i][j] < 200 )
    				{
    	    			points = new PVector[4];
    					points[0] = new PVector((j%w[i]*10)-6, (-1*(j/w[i]*10)-6)+400); 
    					points[1] = new PVector((j%w[i]*10)+6, (-1*(j/w[i]*10)-6)+400); 
    					points[2] = new PVector((j%w[i]*10)+6, (-1*(j/w[i]*10)+6)+400); 
    					points[3] = new PVector((j%w[i]*10)-6, (-1*(j/w[i]*10)+6)+400); 
    					font.getGlyph(Cs[i]).addContour(points);
    				}
    			}
    		}
    	}

    	font.buildFont();  
    	font.cleanup();    
	}

}
