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

/**
MARVIN Project

Initial version by: 
Danilo Rosetto Mu�oz
F�bio Andrijauskas
Gabriel Ambr�sio Archanjo

site: http://marvin.incubadora.fapesp.br/

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

package org.marvinproject.image.color.skinColorDetection;


import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;


/**
 * Locates skin color pixels in an image.
 * @author Barry McCullagh
 * @version 08/05/2009
 */
public class SkinColorDetection extends MarvinAbstractImagePlugin
{
	
	MarvinAttributes attributes;

	public void load(){
		attributes = getAttributes();
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Skin Detection", 400,350, getImagePanel(), this);
	
		l_filterWindow.setVisible(true);
	}

	/**
	 * Initiate the process of finding skin
	 * @param MarvinImage - the image to be rotated
	 * @param boolean - to display a preview
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
		MarvinImage l_hsvImage = new MarvinImage(a_imageIn.getWidth(), a_imageIn.getHeight());
		ColorSpaceConverter l_colorspaceConverter = new ColorSpaceConverter();
		l_colorspaceConverter.process(a_imageIn, l_hsvImage, a_attributesOut, a_mask, false);
		
		
		/*The first step is to determine regions of the scene which
		 * have appropriate skin tone.
		 */
		findSkinColorPixels(a_imageIn, l_hsvImage, a_imageOut);
		
		
		
		
	}
	/**
	 * Using heuristics, identify any pixels which may be skin colored.
	 * @param a_imageIn
	 * @param a_imageOut
	 */
	private void findSkinColorPixels(MarvinImage a_imageIn, MarvinImage a_hsvImage, MarvinImage a_imageOut)
	{
		int l_imageHeight = a_imageIn.getHeight();
		int l_imageWidth = a_imageIn.getWidth();
		
		
		boolean[] l_rules = {false, false, false};
		
		for(int xx = 0; xx < l_imageWidth; xx++)
		{
			for(int yy = 0; yy < l_imageHeight; yy++)
			{
				int l_currentHue = a_hsvImage.getIntComponent0(xx, yy);
				int l_currentSat = a_hsvImage.getIntComponent1(xx, yy);
				int l_currentVar = a_hsvImage.getIntComponent2(xx, yy);
				
				if(l_currentHue > 0 && l_currentHue < 23)
				{
					a_imageOut.setIntColor(xx, yy, a_imageIn.getIntColor(xx, yy));
				}
				else
				{
					a_imageOut.setIntColor(xx, yy, 0);
				}
			/*	checkRule0(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				//checkRule1(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				checkRule2(l_currentRed, l_currentGreen, l_currentBlue, l_rules);
				if(l_rules[0] == true && l_rules[2] == true)
				{
					a_imageOut.setRGB(xx, yy, a_imageIn.getRGB(xx, yy));
				}
				else
				{
					a_imageOut.setRGB(xx, yy, 0);
				}
				*/
			}
		}
	}
	
	//rules from http://graphics.cs.msu.ru/en/publications/text/gc2003vsa.pdf
	protected void checkRule2(int a_currentRed, int a_currentGreen, int a_currentBlue, boolean[] a_rules)
	{
		int l_maxCoords = max3nums(a_currentRed, a_currentGreen, a_currentBlue);
		int l_minCoords = min3nums(a_currentRed, a_currentGreen, a_currentBlue);
		if(a_currentRed > 95 && a_currentGreen >40 && a_currentBlue > 20
				&& (l_maxCoords - l_minCoords) > 15
				&& Math.abs(a_currentRed - a_currentGreen) > 15
				&& a_currentRed > a_currentGreen
				&& a_currentRed > a_currentBlue)
		{
			a_rules[2] = true;
		}
		else
		{
			a_rules[2] = false;
		}
	}
	public int max3nums(int a_red, int a_green, int a_blue)
	{
		int l_max = 0;
		if(a_red > a_green)
		{
			l_max = a_red;
		}
		else
		{
			l_max = a_green;
			
		}
		if(l_max < a_blue)
		{
			l_max = a_blue;
		}
		
		return l_max;
	}
	public int min3nums(int a_red, int a_green, int a_blue)
	{
		int l_min = 255;
		if(a_red < a_green)
		{
			l_min = a_red;
		}
		else
		{
			l_min = a_green;
		}
		if(l_min < a_blue)
		{
			return l_min;
		}
		else
		{
			return a_blue;
		}
	}
	
	//rules from http://lrv.fri.uni-lj.si/~peterp/publications/eurocon03.pdf
	protected void checkRule1(int a_currentRed, int a_currentGreen, int a_currentBlue, boolean[] a_rules)
	{
		if(a_currentRed > 220 
				&& a_currentGreen > 210 
				&& a_currentBlue > 170 
				&& Math.abs(a_currentRed - a_currentGreen) <= 15 
				&& a_currentRed > a_currentBlue 
				&& a_currentGreen > a_currentBlue)
		{
			a_rules[1] = true;
		}
		else
		{
			a_rules[1] = false;
		}
	}
	
	//rules from http://cs-people.bu.edu/ringb/CS585/PA1/source/ImageFunct.html
	protected void checkRule0(int a_currentRed, int a_currentGreen, int a_currentBlue, boolean[] a_rules)
	{
		int[] l_currentSkintone = new int[2];
		l_currentSkintone[0] = a_currentRed;
		l_currentSkintone[1] = a_currentGreen;
		//check the red and green component of the current pixel
		//If it does not fall in between the required values set as black
		if(a_currentRed < 40  || a_currentGreen < 40)
		{
			//a_imageOut.setRGB(xx, yy, 0, 0, 0);
			a_rules[0] = false;
			//System.out.println(a_rule1);
		}
		else
		{
			int[] l_VectorSkintoneA = new int[2];
			int[] l_VectorSkintoneB = new int[2];
			l_VectorSkintoneA[0] = 225;
			l_VectorSkintoneA[1] = 165;
			l_VectorSkintoneB[0] = 125;
			l_VectorSkintoneB[1] = 50;
			double l_angleCurrentSkinVectorA = calculateVectorAngle(l_VectorSkintoneA, l_currentSkintone);
			double l_angleCurrentSkinVectorB = calculateVectorAngle(l_VectorSkintoneB, l_currentSkintone);
			
			if(l_angleCurrentSkinVectorA < .995 && l_angleCurrentSkinVectorB < .995)
			{
				//a_imageOut.setRGB(xx, yy, 0, 0, 0);
				a_rules[0] = false;
				
			}
			else
			{
				a_rules[0] = true;
				//System.out.println(a_rule1);
				//a_imageOut.setRGB(xx, yy, a_imageIn.getRGB(xx,yy));
			}
		}
	}
	/**
	 * Find the minimum and maximum component of all the R, G and B values in an image.
	 * @param a_imageIn
	 * @param a_minMax
	 */
	protected void findMinMax(MarvinImage a_imageIn, int[] a_minMax)
	{
		for(int l_currentX = 0; l_currentX < a_imageIn.getWidth(); l_currentX++)
		{
			for(int l_currentY = 0; l_currentY < a_imageIn.getHeight(); l_currentY++)
			{
				int l_currentRed = a_imageIn.getIntComponent0(l_currentX, l_currentY);
				int l_currentGreen = a_imageIn.getIntComponent1(l_currentX, l_currentY);
				int l_currentBlue = a_imageIn.getIntComponent2(l_currentX, l_currentY);
				//check red
				if(l_currentRed < a_minMax[0])
				{
					a_minMax[0] = l_currentRed;
				}
				if(l_currentRed > a_minMax[1])
				{
					a_minMax[1] = l_currentRed;
				}
				//check green
				if(l_currentGreen < a_minMax[0])
				{
					a_minMax[0] = l_currentGreen;
				}
				if(l_currentGreen > a_minMax[1])
				{
					a_minMax[1] = l_currentGreen;
				}
				//check blue
				if(l_currentBlue < a_minMax[0])
				{
					a_minMax[0] = l_currentBlue;
				}
				if(l_currentBlue > a_minMax[1])
				{
					a_minMax[1] = l_currentBlue;
				}
			}
		}
	}
	/**
	 * The borders may not be continuous so this method examines the pixels
	 * around the current pixel of interest. If there is no border pixel neighbouring
	 * it, it searches within a 16 connected connected neighbourhood to find a bordering pixel
	 * @param l_borderedRegions
	 */
	
/**
 * Get the angle between two, two dimensional vectors
 * @param a_Vector1, a_Vector2 - the vectors who's angle of difference needs to be calculated
 */
	protected double calculateVectorAngle(int[] a_Vector1, int[] a_Vector2)
	{
		if (a_Vector1.length != a_Vector2.length)
		{
			return 0;
		}
		double l_magVector1 = findMagnitude(a_Vector1);
		double l_magVector2 = findMagnitude(a_Vector2);
		double l_dotProduct = dotProduct(a_Vector1, a_Vector2);
		
		return l_dotProduct / (l_magVector1 * l_magVector2);

	}
	/**
	 * Find the magnitude of a 2 element 'vector'
	 * @param a_Vector
	 * @return
	 */
	protected double findMagnitude(int[] a_Vector)  
	{
		if (a_Vector.length != 2)
		{
			return 0;
		}
		else
		{
			return Math.sqrt(a_Vector[0]*a_Vector[0] + a_Vector[1]*a_Vector[1] );
		}
	}
	
	//Static Method that returns the Dot Product of two Vectors
	private double dotProduct(int[] a_Vector1, int[] a_Vector2) 
	{
		if (a_Vector1.length != a_Vector2.length && a_Vector1.length != 2)
		{
			return 0;
		}
	    return a_Vector1[0]*a_Vector2[0] + a_Vector1[1]*a_Vector2[1];
	}
	
	

	
}
