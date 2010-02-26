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

package org.marvinproject.image.combine.combineByTransparency;

import java.awt.Color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine by transparency plug-in.
 * 
 * @author Alan Thiago do Prado
 * @version 1.0 29/01/2009
 */
public class CombineByTransparency extends MarvinAbstractImagePlugin {
	
	private MarvinPerformanceMeter 	performanceMeter;
	private MarvinAttributes 		attributes;
	
	private int 					xi=0,
									yi=0;
	
	private int 					transparency=10;
	
	private MarvinImage 			combinationImage;
	
	
	public void load() {
		attributes = getAttributes();
		attributes.set("xi", xi);
		attributes.set("yi", yi);
		attributes.set("transparency", transparency);
		performanceMeter = new MarvinPerformanceMeter();
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
		xi = (Integer)attributes.get("xi");
		yi = (Integer)attributes.get("yi");		
		combinationImage = (MarvinImage)attributes.get("combinationImage");
		transparency = (Integer)attributes.get("transparency");
		
		double l_factor = (100-transparency)/100.0;
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();
		
		double 	l_redA,
				l_redB,
				l_greenA,
				l_greenB,
				l_blueA,
				l_blueB;
		
			
		for(int l_y=0; l_y<a_imageIn.getHeight(); l_y++){
    		for(int l_x=0; l_x<a_imageIn.getWidth(); l_x++){
    			
    			l_xCI = l_x-xi;
    			l_yCI = l_y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    			
    				l_redA = a_imageIn.getIntComponent0(l_x, l_y);
    				l_greenA = a_imageIn.getIntComponent1(l_x, l_y);
    				l_blueA = a_imageIn.getIntComponent2(l_x, l_y);
    				
    				l_redB = combinationImage.getIntComponent0(l_xCI, l_yCI);
    				l_greenB = combinationImage.getIntComponent1(l_xCI, l_yCI);
    				l_blueB = combinationImage.getIntComponent2(l_xCI, l_yCI);
    				
    				a_imageOut.setIntColor
    				(
    					l_x,
    					l_y,
    					(int)((l_redA*(1.0-l_factor))+(l_redB*l_factor)),
    					(int)((l_greenA*(1.0-l_factor))+(l_greenB*l_factor)),
    					(int)((l_blueA*(1.0-l_factor))+(l_blueB*l_factor))
    				);
    			}
    			else{
    				a_imageOut.setIntColor(l_x, l_y, a_imageIn.getIntColor(l_x, l_y));
    			}
    		}
		}
	}
}