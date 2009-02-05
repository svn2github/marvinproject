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

package net.sourceforge.marvinproject.transform.rotate;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;


/**
 * Rotate the image by a user-defined angle.
 * @author Barry McCullagh
 * @version 02/04/2009
 */
public class Rotate extends MarvinAbstractPluginImage
{
	private final static String CLOCKWISE90 = "clockwise90";
	private final static String ACLOCKWISE90 = "anticlockwise90";
	private final static String OTHER = "Other";

	MarvinAttributes attributes;

	public void load(){
		attributes = getAttributes();
		attributes.set("rotate", "angle");
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Rotate", 400,350, getImagePanel(), this);
		
		l_filterWindow.addLabel("labelRotate", "Options:");
		l_filterWindow.addComboBox("combpRotate", "rotate", new Object[]{CLOCKWISE90, ACLOCKWISE90, OTHER}, attributes);
		l_filterWindow.addPanelBelow();
		
		l_filterWindow.addLabel("lblRotateAngle", "Angle of Rotation");
		l_filterWindow.addHorizontalSlider("sliderRotateAngle", "RotateAngle", -45, 45, 0, attributes);
		l_filterWindow.addPanelBelow();

	//	l_filterWindow.addLabel("lblRotateAngle", "Rotate Angle:");
	//	l_filterWindow.addTextField("textFieldRotateAngle", "rotateTextField", attributes);
	//	l_filterWindow.addPanelBelow();
		
	//	l_filterWindow.addHorizontalSlider("sliderRotateAngle", "RotateAngle", -180, 180, 0, attributes);
	//	l_filterWindow.addPanelBelow();
		
		l_filterWindow.setVisible(true);
	}

	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		int l_aImageHeight = a_imageIn.getHeight();
		int l_aImageWidth = a_imageIn.getWidth();
	//	int l_halfAImageHeight = (int)(l_aImageHeight)/2;
	//	int l_halfAImageWidth = (int)(l_aImageWidth)/2;
		int l_rotateAngle;
		double l_rotateAngleRadians = 0;
		//MarvinImage l_image = (MarvinImage)a_imageIn.clone();
		
		
		
		//for speed, check if the user wants a simple rotate by 90 degrees
		String userOption = (String)attributes.get("rotate"); 
		attributes.set("rotateTextField", userOption);
		if(userOption.intern() == CLOCKWISE90.intern())
		{
			//this is an image transpose so swap the width and height
			System.out.println("90clockwise");
			int l_newHeight = l_aImageWidth;
			int l_newWidth = l_aImageHeight;
			
			a_imageOut.setDimension(l_newHeight, l_newWidth);
			//a_image.resizeCurrentImg(l_newWidth, l_newHeight);
			for(int xx = 0; xx < l_aImageWidth; xx ++)
			{
				for(int yy =l_aImageHeight-1; yy >= 0; yy--)
				{
					a_imageOut.setRGB(l_aImageHeight-1 - yy, xx, a_imageIn.getRGB(xx, yy));
				}
			}
		}
		else if(userOption.intern() == ACLOCKWISE90.intern())
		{
			//this is an image transpose so swap the width and height
			System.out.println("90clockwise");
			int l_newHeight = l_aImageWidth;
			int l_newWidth = l_aImageHeight;
			//a_image.resizeCurrentImg(l_newWidth, l_newHeight);
			a_imageOut.setDimension(l_newHeight, l_newWidth);
			for(int xx = l_aImageWidth-1; xx > 0; xx --)
			{
				for(int yy = 0; yy < l_aImageHeight; yy++)
				{
					a_imageOut.setRGB(yy, l_aImageWidth -1 - xx, a_imageIn.getRGB(xx, yy));
				}
			}
		}
		else
		{
			//first, calculate the new height and width
			l_rotateAngle = (Integer)attributes.get("RotateAngle");
			l_rotateAngleRadians = Math.toRadians(l_rotateAngle);
			//System.out.println("Angle: " + l_rotateAngleRadians + " tan: " + Math.tan(l_rotateAngleRadians));
			//MarvinImage l_finalStage;
			if(l_rotateAngleRadians > 0)
			{
				rotateClockwise(a_imageIn, a_imageOut, l_rotateAngleRadians);
			}
			else
			{
				rotateAntiClockwise(a_imageIn, a_imageOut, l_rotateAngleRadians);
			}
			
			//due to the floating point/integer error we need to interpolate the image
			//to avoid having pixels with no information.
			//int l_neighbourhood = 4;
			//interpolateImage(a_image, l_neighbourhood);

		}
	}

	/**
	 * Use the information from the original image to interpolate and fill in
	 * gaps in the rotated image
	 * @param MarvinImage a_image - the image to be modified
	 * @param MarvinImage a_originalImage - the image to be used as the source of the interpolation
	 * @param double a_rotateAngle - the angle of rotation
	 */
	private void interpolateImage(MarvinImage a_image, MarvinImage a_originalImage, int[][][] a_LookUpArray, double a_rotateAngle, int a_initialisationValue)
	{
		//get image dimensions
		int l_rotatedImageWidth = a_image.getWidth();
		int l_rotatedImageHeight = a_image.getHeight();
		
		int counter = 0;
		for(int xx = 1; xx < l_rotatedImageWidth-1; xx++)
		{
			for(int yy = 1; yy < l_rotatedImageHeight-1; yy++)
			{
				//if the pixel value has been assigned, in future change this to check for null
				if(a_LookUpArray[xx][yy][2] == a_initialisationValue)
				{
					int l_leftValue = a_LookUpArray[xx-1][yy][2];
					int l_rightValue = a_LookUpArray[xx-1][yy][2];
					int l_difference = l_rightValue - l_leftValue;
					//System.out.println(a_image.getRGB(xx, yy));
					a_image.setRGB(xx, yy, l_leftValue + (l_difference+2));
					
					if(xx==1 && yy < 40){
					   System.out.println("value:"+(l_leftValue + (l_difference+2)));
					   System.out.println("l_differene:"+l_difference);
					}
					
					//System.out.println();
					//System.out.println(a_image.getRGB(xx, yy) + "-------");
				}
				
			}
		}
	}

	public void initialiseLookUpArray(int[][][] a_LookUpArray, int[] dimensions, int a_InitialisationValue)
	{
		for(int xx = 0; xx < dimensions[0]; xx++)
		{
			for(int yy = 0; yy < dimensions[1]; yy++)
			{
				a_LookUpArray[xx][yy][2] = a_InitialisationValue;
			}				
		}
	}
	
	private MarvinImage rotateClockwise(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_rotateAngle)
	{
		//Get the image dimensions
		int l_aimageHeight = a_imageIn.getHeight();
		int l_aimageWidth = a_imageIn.getWidth();
		
		//Calculate the size of the rotated image
		double l_absRotateAngle = Math.abs(a_rotateAngle);
		int l_newHeight = (int)(Math.ceil(l_aimageWidth * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageHeight * Math.cos(l_absRotateAngle))));
		int l_newWidth = (int)(Math.ceil(l_aimageHeight * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageWidth * Math.cos(l_absRotateAngle))));

		//The look up array used to interpolate later.
		//Each location in the array will contain
		// the x and y coordinates of the original pixel, and its RGB value
		int[][][] l_LookUpArray = new int [l_newWidth][l_newHeight][3];
		int l_initialisationValue = 654321;
		int[] l_dimensions = {l_newWidth, l_newHeight, 3};
		initialiseLookUpArray(l_LookUpArray, l_dimensions, l_initialisationValue);
		//Create a local copy of the image
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		//erase the contents of a_image
		
		/*
		for(int xx = 0; xx < l_aimageWidth; xx++)
		{
			for(int yy = 0; yy < l_aimageHeight; yy++)
			{
				a_imageOut.setRGB(xx, yy, 0000);
			}
		}
		*/
		
		//@@@ a_image.resizeCurrentImg(Math.abs(l_newWidth), Math.abs(l_newHeight));
		a_imageOut.setDimension(Math.abs(l_newWidth), Math.abs(l_newHeight));
		
		for(int xx = 0; xx < l_aimageWidth; xx++)
		{
			for(int yy = 0; yy < l_aimageHeight; yy++)
			{
				int l_newXCoordinate = (int)( (Math.cos(a_rotateAngle)* (xx - (l_aimageWidth/2))) - ((Math.sin(a_rotateAngle) * (yy - (l_aimageHeight/2)))) + (l_newWidth/2));
				int l_newYCoordinate = (int)( (Math.sin(a_rotateAngle)* (xx - (l_aimageWidth/2))) + ((Math.cos(a_rotateAngle) * (yy - (l_aimageHeight/2)))) + (l_newHeight/2));
				
				try
				{
					a_imageOut.setRGB(l_newXCoordinate, l_newYCoordinate, a_imageIn.getRGB(xx, yy));
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][0] = xx;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][1] = yy;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][2] = a_imageIn.getRGB(xx, yy);
				}
				catch(Exception e)
				{
					System.out.println(l_newXCoordinate + " " + l_newYCoordinate);
				}
			}
		}
		interpolateImage(a_imageOut, a_imageIn, l_LookUpArray, a_rotateAngle, l_initialisationValue);
		return a_imageOut; 
	}

	private MarvinImage rotateAntiClockwise(MarvinImage a_imageIn, MarvinImage a_imageOut, double a_rotateAngle)
	{
		//Get the image dimensions
		int l_aimageHeight = a_imageIn.getHeight();
		int l_aimageWidth = a_imageIn.getWidth();
		
		//Calculate the size of the rotated image
		double l_absRotateAngle = Math.abs(a_rotateAngle);
		int l_newHeight = (int)(Math.ceil(l_aimageWidth * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageHeight * Math.cos(l_absRotateAngle))));
		int l_newWidth = (int)(Math.ceil(l_aimageHeight * Math.sin(l_absRotateAngle) + Math.ceil(l_aimageWidth * Math.cos(l_absRotateAngle))));

		//The look up array used to interpolate later.
		//Each location in the array will contain
		// the x and y coordinates of the original pixel, and its RGB value
		int[][][] l_LookUpArray = new int [l_newWidth][l_newHeight][3];
		//int l_initialisationValue = 654321;
		int l_initialisationValue = 32000;
		int[] l_dimensions = {l_newWidth, l_newHeight, 3};
		initialiseLookUpArray(l_LookUpArray, l_dimensions, l_initialisationValue);
	
		//Create a local copy of the image
		//MarvinImage l_image = (MarvinImage)a_image.clone();
		//erase the contents of a_image
		/*
		for(int xx = 0; xx < l_aimageWidth; xx++)
		{
			for(int yy = 0; yy < l_aimageHeight; yy++)
			{
				a_image.setRGB(xx, yy, 0000);
			}
		}
		*/
		
		//a_image.resizeCurrentImg(Math.abs(l_newWidth), Math.abs(l_newHeight));
		a_imageOut.setDimension(Math.abs(l_newWidth), Math.abs(l_newHeight));
		
		for(int xx = 0; xx < l_aimageWidth-0; xx++)
		{
			for(int yy = 0; yy < l_aimageHeight-0; yy++)
			{
				int l_newXCoordinate = (int)( Math.floor(Math.cos(a_rotateAngle)* (xx - (l_aimageWidth/2))) - (Math.floor(Math.sin(a_rotateAngle) * (yy - (l_aimageHeight/2)))) + (l_newWidth/2));
				int l_newYCoordinate = (int)( Math.floor(Math.sin(a_rotateAngle)* (xx - (l_aimageWidth/2))) + (Math.floor(Math.cos(a_rotateAngle) * (yy - (l_aimageHeight/2)))) + (l_newHeight/2));
				try
				{
					a_imageOut.setRGB(l_newXCoordinate, l_newYCoordinate, a_imageIn.getRGB(xx, yy));
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][0] = xx;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][1] = yy;
					l_LookUpArray[l_newXCoordinate][l_newYCoordinate][2] = a_imageIn.getRGB(xx, yy);
				}
				catch(Exception e)
				{
					System.out.println(l_newXCoordinate + " " + l_newYCoordinate);
				}
				//a_image.setRGB(l_newXCoordinate, l_newYCoordinate, l_image.getRGB(xx, yy));
			}
		}
		interpolateImage(a_imageOut, a_imageIn, l_LookUpArray, a_rotateAngle, l_initialisationValue);
		return a_imageOut; 
	}
	/**
	 * Perform a vertical skew of the input image
	 * @param MarvinImage a_image
	 * @param double skew_angle_rad
	 */
	private MarvinImage skewVertical(MarvinImage a_originalImage, MarvinImage a_copyImage, double a_skewAngleRad)
	{
		int r, g, b;
		int l_newYcoordinate;
		int l_originalImageWidth = a_originalImage.getWidth();
		int l_originalImageHeight = a_originalImage.getHeight();
		int l_extraHeight = (int)Math.ceil(l_originalImageWidth * Math.tan(a_skewAngleRad));
		int l_newHeight = l_originalImageHeight + Math.abs(l_extraHeight);
		
		a_copyImage = (MarvinImage)(a_originalImage.clone());
		
		a_copyImage.resizeCurrentImg(l_originalImageWidth, l_newHeight);
		for(int xx = 0; xx < l_originalImageWidth; xx++)
		{
			for(int yy = 0; yy < l_newHeight; yy++)
			{
				a_copyImage.setRGB(xx, yy, 000000);
			}
		}
		for (int x = 0; x < l_originalImageWidth; x++) {
			//System.out.println("x: " + x);
			for (int y = 0; y < l_originalImageHeight; y++){
				
				//The position of the pixel in the top left hand corner of the original image will
				//be different depending on the value of the skew angle
				if (l_extraHeight > 0)
				{
					l_newYcoordinate = y + (int)((x) * Math.tan(a_skewAngleRad));
				}
				else
				{
					l_newYcoordinate = (l_newHeight) -(l_originalImageHeight) + y - (int)Math.abs(((x) * Math.tan(a_skewAngleRad)));
				}
			
				r = a_originalImage.getRed(x, y);
				g = a_originalImage.getGreen(x, y);
				b = a_originalImage.getBlue(x, y);
				
				a_copyImage.setRGB(x, l_newYcoordinate, r, g, b);	
			}
		}
		return a_copyImage;
		//a_image.setImage((Image)(b_image));
	}
	
	}
