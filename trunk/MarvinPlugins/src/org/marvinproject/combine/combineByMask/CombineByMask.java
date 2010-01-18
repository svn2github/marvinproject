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

package org.marvinproject.combine.combineByMask;

import java.awt.Color;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Combine two images using a mask color.
 * @author Gabriel Ambrosio Archanjo
 */
public class CombineByMask extends MarvinAbstractImagePlugin{
	
	MarvinAttributes 	attributes;
	
	MarvinImage 		combinationImage;
	
	Color 				colorMask;
	
	private int 		xi=0,
						yi=0;
	
	public void load(){
		attributes = getAttributes();
		attributes.set("xi", xi);
		attributes.set("yi", yi);
	}
	
	public void show(){
		
	}
	
	public void process(MarvinImage a_imageIn, MarvinImage a_imageOut, MarvinAttributes a_attributesOut, MarvinImageMask a_mask, boolean a_previewMode){
		xi = (Integer)attributes.get("xi");
		yi = (Integer)attributes.get("yi");		
		colorMask = (Color)attributes.get("colorMask");
		combinationImage = (MarvinImage)attributes.get("combinationImage");
		
		int l_xCI,
			l_yCI;
		
		int l_widthCI = combinationImage.getWidth(),
			l_heightCI = combinationImage.getHeight();
			
			
		for(int l_y=0; l_y<a_imageIn.getHeight(); l_y++){
    		for(int l_x=0; l_x<a_imageIn.getWidth(); l_x++){
    			
    			l_xCI = l_x-xi;
    			l_yCI = l_y-yi;
    			
    			if(l_xCI >= 0 && l_xCI < l_widthCI && l_yCI >= 0 && l_yCI < l_heightCI){
    				if(a_imageIn.getIntColor(l_x, l_y) == colorMask.getRGB()){
    					a_imageOut.setIntColor(l_x, l_y, combinationImage.getIntColor(l_x, l_y));
    				}
    				else{
    					a_imageOut.setIntColor(l_x, l_y, a_imageIn.getIntColor(l_x, l_y));
    				}
    			}
    			else{
    				a_imageOut.setIntColor(l_x, l_y, a_imageIn.getIntColor(l_x, l_y));
    			}
    		}
		}
	}
}
