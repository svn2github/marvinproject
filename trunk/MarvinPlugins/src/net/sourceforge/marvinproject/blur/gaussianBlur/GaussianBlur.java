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

package net.sourceforge.marvinproject.blur.gaussianBlur;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;

/**
 * Gaussian blur implementation. This algorithm can be optimized in many ways.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/13/2008
 */
public class GaussianBlur extends MarvinAbstractPluginImage
{
	private final static int RED = 0;
	private final static int GREEN = 1;
	private final static int BLUE = 2;

	MarvinAttributes attributes;
	MarvinPerformanceMeter performanceMeter;

	double kernelMatrix[][];
	double resultMatrix[][][];
	int radius;
	public void load()
	{
		attributes = getAttributes();
		attributes.set("radius",3);		
		performanceMeter = new MarvinPerformanceMeter();
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Gaussian Blur", 400,350, getImagePanel(), this);
		l_filterWindow.addLabel("lblWidth", "Radius:");
		l_filterWindow.addTextField("txtRadius", "radius", attributes);
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
		radius = (Integer)attributes.get("radius");

		int l_imageWidth = a_imageIn.getWidth();
		int l_imageHeight = a_imageIn.getHeight();

		performanceMeter.start("Gaussian Blur");
		performanceMeter.enableProgressBar("Gaussian Blur", (l_imageWidth*l_imageHeight)+l_imageWidth);

		int l_pixelColor;
		kernelMatrix = getGaussianKernel();
		resultMatrix = new double[l_imageWidth][l_imageHeight][3];
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		performanceMeter.startEvent("Apply Kernel");
		for (int x = 0; x < l_imageWidth; x++) {
			for (int y = 0; y < l_imageHeight; y++) {	
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				l_pixelColor = a_imageIn.getIntColor(x,y);
				applyKernel(x,y,l_pixelColor,a_imageOut);
			}
			performanceMeter.incProgressBar(l_imageHeight);
		}
		performanceMeter.finishEvent();
		
		
		performanceMeter.startEvent("Apply result");
		for (int x = 0; x < l_imageWidth; x++) {
			for (int y = 0; y < l_imageHeight; y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				resultMatrix[x][y][RED] = (short)(resultMatrix[x][y][0]%256);
				resultMatrix[x][y][GREEN] = (short)(resultMatrix[x][y][1]%256);
				resultMatrix[x][y][BLUE] = (short)(resultMatrix[x][y][2]%256);
				a_imageOut.setIntColor(x,y,(int)resultMatrix[x][y][0], (int)resultMatrix[x][y][1], (int)resultMatrix[x][y][2]);
			}
			performanceMeter.incProgressBar();
			performanceMeter.stepsFinished(l_imageHeight);			
		}
		performanceMeter.finishEvent();
		
		performanceMeter.finish();
	}

	/*
	 * Calc Gaussian Matrix.
	 */
	private double[][] getGaussianKernel(){
		double l_matrix[][] = new double[(radius*2)+1][(radius*2)+1];
		double l_q=radius/3.0;
		double l_distance;
		double l_x;
		double l_y;
		
		performanceMeter.startEvent("Generate Gaussian Kernel");
		for(int x=1; x<=(radius*2)+1; x++){
			for(int y=1; y<=(radius*2)+1; y++){
				l_x = Math.abs(x-(radius+1));
				l_y = Math.abs(y-(radius+1));
				l_distance = Math.sqrt((l_x*l_x)+(l_y*l_y));
				l_matrix[y-1][x-1] = ( (1.0/(2.0*Math.PI*l_q*l_q))* Math.exp( (-(l_distance*l_distance)) / (2.0*l_q*l_q) ) );
			}
			performanceMeter.stepsFinished(radius*2);
		}
		performanceMeter.finishEvent();
		return l_matrix;
	}

	/*
	 * Apply the blur matrix on a image region. 
	 */
	private void applyKernel(int a_centerPixel_X, int a_centerPixel_Y, int a_pixelColor, MarvinImage a_image)
	{
		for(int y=a_centerPixel_Y; y<a_centerPixel_Y+(radius*2); y++){
			for(int x=a_centerPixel_X; x<a_centerPixel_X+(radius*2); x++){
				if(x-radius >= 0 && x-radius < a_image.getWidth() && y-radius >=0 && y-radius < a_image.getHeight()){
					resultMatrix[x-radius][y-radius][RED]+= (((a_pixelColor & 0x00FF0000) >>> 16)*kernelMatrix[x-a_centerPixel_X][y-a_centerPixel_Y]);
					resultMatrix[x-radius][y-radius][GREEN]+= (((a_pixelColor & 0x0000FF00) >>> 8)*kernelMatrix[x-a_centerPixel_X][y-a_centerPixel_Y]);
					resultMatrix[x-radius][y-radius][BLUE]+= ((a_pixelColor & 0x000000FF)*kernelMatrix[x-a_centerPixel_X][y-a_centerPixel_Y]);
				}
			}
			performanceMeter.stepsFinished(radius*2);
		}
	}
}