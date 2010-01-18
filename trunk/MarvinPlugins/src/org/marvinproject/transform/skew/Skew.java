/**
Marvin Project <2007-2009>

Initial version by:

Danilo Rosetto Munoz
Fabio Andrijauskas
Gabriel Ambrosio Archanjo

site: http://marvinproject.sourceforge.net

GPL
Copyright (C) <2007>  

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package org.marvinproject.transform.skew;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Perform a vertical OR horizontal skew of an image.
 * @author Barry McCullagh
 * @version 01/30/2009
 */
public class Skew extends MarvinAbstractImagePlugin
{
	private final static String HORIZONTAL = "Horizontal";
	private final static String VERTICAL = "Vertical";
	private int SELECTEDANGLE = 0;

	MarvinAttributes attributes;

	public void load(){
		attributes = getAttributes();
		attributes.set("skew", "horizontal");
		attributes.set("selected", String.valueOf(SELECTEDANGLE));
	}

	/**
	 * Displays the interface to the Skew plug-in.
	 * Adds a horizontal/vertical combo box so the direction can be selected.
	 * Adds a slider so the magnitude of the angle can be selected.
	 * 
	 * @param - none.
	 * @return - void.
	 */
	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Skew", 400,350, getImagePanel(), this);
	
		l_filterWindow.addLabel("labelSkew", "Skew:");
		l_filterWindow.addComboBox("combpSkew", "skew", new Object[]{HORIZONTAL, VERTICAL}, attributes);
		l_filterWindow.addPanelBelow();
		
		l_filterWindow.addLabel("lblSkewAngle", "SkewAngle");
		l_filterWindow.addHorizontalSlider("sliderSkewAngle", "SkewAngle", -89, 89, 0, attributes);
		l_filterWindow.addPanelBelow();
		
		/*
		//In future versions it would be nice to be able to tell the user the selected
		//skew angle, but at present, I can't set the value for a text field added here
		//in other methods
		l_filterWindow.addLabel("lblSelectedAngle", "Selected Angle: ");
		l_filterWindow.addTextField("txtfieldSelectedAngle", "selected", attributes);
		*/
		
		l_filterWindow.setVisible(true);
	}

	/**
	 * Handles the 'Process' button being pressed. 
	 * Determines the angle to skew and if the user wants
	 * a horizontal or vertical skew.
	 * 
	 * @param MarvinImage - the image to be skewed
	 * @param boolean - if a preview is being performed
	 * @return - void
	 */
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//find the desired direction
		String l_operation = (String)attributes.get("skew");
		//find the desired angle and convert from degrees to rads
		int s_angle = (Integer)attributes.get("SkewAngle");		
		double s_angle_rad = (double)(Math.toRadians(s_angle));
		
		if(l_operation.equals(HORIZONTAL))
		{
			skewHorizontal(a_imageIn, a_imageOut, s_angle_rad);
		}
		else
		{
			skewVertical(a_imageIn, a_imageOut, s_angle_rad);
		}

	}
	/**
	 * Perform a horizontal skew of the input image (a_image). The maximum skew in each direction is 89 degrees.
	 * 
	 * @param a_image The input image to be skewed.
	 * @param skew_angle_rad The amount of skew to be performed.
	 * @return void
	 * @see MarvinImage
	 */
	private void skewHorizontal(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_skewAngleRad)
	{
		
		int r,g,b;
		//get image dimensions
		int l_aHeight = a_imageIn.getHeight();
		int l_aWidth = a_imageIn.getWidth();
		//a local copy of the image, used to store a copy of the original image.
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		
		//Calculate the width of the new image. 
		//l_extraWidth is not made absolute as it tells us if the skew is to be performed
		//to the left or right.
		int l_extraWidth = (int)(Math.ceil(l_aHeight * Math.tan(a_skewAngleRad)));
		int l_newWidth = l_aWidth + Math.abs(l_extraWidth); 
		
		//The modifications must be performed on a_image so erase the contents
		//of a_image so the skewed image can be put there.
		for(int xx = 0; xx < l_aWidth; xx++)
		{
			for(int yy = 0; yy < l_aHeight; yy++)
			{
				a_imageOut.setIntColor(xx, yy, 000000);
				
			}
				
		}
		a_imageOut.resize(l_newWidth, l_aHeight);
		
		//The top of the image is being moved to the right.
		//The amount each row moves depends on its height, calculated as l_newXcoordinate.
		if(l_extraWidth > 0)
		{
			for (int x = 0; x < l_aWidth; x++) {
				for (int y = 0; y < l_aHeight; y++){
					int l_newXcoordinate = x + (int)(Math.abs((y - l_aHeight) * Math.tan(a_skewAngleRad)));
					r = a_imageIn.getIntComponent0(x, y);
					g = a_imageIn.getIntComponent1(x, y);
					b = a_imageIn.getIntComponent2(x, y);
				
					a_imageOut.setIntColor(l_newXcoordinate, y, r, g, b);	
				}
			}
		}
		//The top of the image is being moved to the left.
		//The amount each row moves depends on its height, calculated as l_newXcoordinate
		else
		{
			for (int x = 0; x < l_aWidth; x++) {
				for (int y = l_aHeight-1; y >= 0; y--){
					int new_xcoordinate = x + (int)(Math.abs((y) * Math.tan(a_skewAngleRad)));

					r = a_imageIn.getIntComponent0(x, y);
					g = a_imageIn.getIntComponent1(x, y);
					b = a_imageIn.getIntComponent2(x, y);

					a_imageOut.setIntColor(new_xcoordinate, y, r, g, b);	
				}
			}
		}
	}
	
	/**
	 * Perform a vertical skew of the input image
	 * @param MarvinImage a_image
	 * @param double skew_angle_rad
	 * @return void
	 * @see MarvinImage
	 */
	private void skewVertical(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_skewAngleRad)
	{
		//The modifications must be applied to the image passed to the function (a_image).
		//Make a local copy of the image to store the data and modify the passed image.

		//MarvinImage l_image = (MarvinImage)(a_image.clone());
		int r, g, b;
		int l_newYcoordinate;
		//Get the image dimensions
		int l_aWidth = a_imageIn.getWidth();
		int l_aHeight = a_imageIn.getHeight();
		
		//Calculate the dimensions of the modified image then resize a_image so that 
		//can store the skewed image. The contents are also empties. 
		int l_extraHeight = (int)Math.ceil(l_aWidth * Math.tan(a_skewAngleRad));		
		int l_newHeight = l_aHeight + Math.abs(l_extraHeight);		
		for(int xx = 0; xx < l_aWidth; xx++)
		{
			for(int yy = 0; yy < l_aHeight; yy++)
			{
				a_imageOut.setIntColor(xx, yy, 000000);
			}
		}
		a_imageOut.resize(l_aWidth, l_newHeight);
		
		
		//Calculate the new coordinate of each pixel. If the image is being skewed
		//up or down (relative to the left hand column of pixels) a different calculation
		//must be performed
		for (int x = 0; x < l_aWidth; x++) {
			for (int y = 0; y < l_aHeight; y++){
				
				//for a skew 'down'
				if (l_extraHeight > 0)
				{
					l_newYcoordinate = y + (int)((x) * Math.tan(a_skewAngleRad));
				}
				//for a skew 'up'
				else
				{
					l_newYcoordinate = (l_newHeight) -(l_aHeight) + y - (int)Math.abs(((x) * Math.tan(a_skewAngleRad)));
				}
			
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);
				
				a_imageOut.setIntColor(x, l_newYcoordinate, r, g, b);	
			}
		}
		
	
	}
}