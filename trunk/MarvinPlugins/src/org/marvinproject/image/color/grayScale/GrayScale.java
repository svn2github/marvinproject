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

package org.marvinproject.image.color.grayScale;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Represents an image in gray scale.
 * @author Fábio Andrijauskas
 * @version 1.0 02/28/2008
 */
public class GrayScale extends MarvinAbstractImagePlugin
{
	MarvinPerformanceMeter performanceMeter;
	MarvinAttributes attributes;
	public void load(){
		performanceMeter = new MarvinPerformanceMeter();
		attributes = getAttributes();
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Gray", 400,350, getImagePanel(), this);
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
		int l_width = a_imageIn.getWidth();
		
		// Mask
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		performanceMeter.start("Gray");
		performanceMeter.startEvent("Gray");
		int r,g,b,corfinal;
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				//Red - 30% / Blue - 59% / Green - 11%
				r = a_imageIn.getIntComponent0(x, y);
				g = a_imageIn.getIntComponent1(x, y);
				b = a_imageIn.getIntComponent2(x, y);
				corfinal = (int)((r*0.3)+(b*0.59)+(g*0.11));
				a_imageOut.setIntColor(x,y,corfinal,corfinal,corfinal);
								
			}
			performanceMeter.stepsFinished(a_imageIn.getHeight());
		}
		performanceMeter.finishEvent();
		performanceMeter.finish();
	}
}

