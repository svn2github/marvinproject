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

package net.sourceforge.marvinproject.edge.edgeDetector;

import java.awt.Color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;

/**
 * Edge detection plug-in. Reference: {@link http://www.cs.princeton.edu/introcs/31datatype/EdgeDetector.java.html}
 * @author Danilo Rosetto Muñoz
 * @version 1.0 02/28/2008
 */
public class EdgeDetector extends MarvinAbstractPluginImage{

	private MarvinPerformanceMeter performanceMeter;
	private MarvinAttributes attributes;

	public void load(){
		//@old version performanceMeter = getApplication().getPerformanceMeter();
		performanceMeter = new MarvinPerformanceMeter();
		attributes = getAttributes();
	}

	public void show()
	{
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("EdgeDetector", 400,350, getImagePanel(), this);
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
		//Sobel filters gx e gy
		int[][] filter1 = { {-1,  0,  1 },
				{-2,  0,  2 },
				{-1,  0,  1 }};
		int[][] filter2 = { { 1,  2,  1 },
				{ 0,  0,  0 },
				{-1, -2, -1 }};

		//Image size
		int width    = a_imageIn.getWidth();
		int height   = a_imageIn.getHeight();
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		performanceMeter.enableProgressBar("Edge Detector", ((height-2)*(width-2)));

		for (int y = 1; y < height - 1; y++) {
			for (int x = 1; x < width - 1; x++) {
				
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				// Get the neighbors 
				int[][] gray = new int[3][3];

				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						int px = x-1+i;
						int py = y-1+j;

						gray[i][j] = (int) luminance(a_imageIn.getRed(px, py), a_imageIn.getGreen(px, py), a_imageIn.getBlue(px, py));
					}
				}

				// Aply the filter, use the 3x3 mask on the image and the center point gets the sum of multiplication of each point through the mask
				int gray1 = 0, gray2 = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						gray1 += gray[i][j] * filter1[i][j];
						gray2 += gray[i][j] * filter2[i][j];
					}
				}
				//Magnitudes sum
				int magnitude = 255 - truncate(Math.abs(gray1) + Math.abs(gray2));

				//int magnitude = 255 - truncate((int) Math.sqrt(gray1*gray1 + gray2*gray2));
				Color grayscale = new Color(magnitude, magnitude, magnitude);

				//Apply the color into a new image
				a_imageOut.setRGB(x, y, grayscale.getRGB());
			}
			performanceMeter.incProgressBar(width-2);
		}
		performanceMeter.finish();
	}

	/**
	 * Sets the RGB between 0 and 255
	 * @param a
	 * @return
	 */
	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	
	/**
	 * Apply the luminance
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	private int luminance(int r, int g, int b){
		//Y = 0.299 r + 0.587g + 0.114b
		return (int)((0.299*r) + (0.58*g) + (0.11*b));			
	}
}