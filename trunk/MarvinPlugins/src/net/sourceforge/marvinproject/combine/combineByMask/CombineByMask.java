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

package net.sourceforge.marvinproject.combine.combineByMask;

import java.awt.Color;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;

/**
 * Combine two images using a mask color.
 * @author Gabriel Ambrosio Archanjo
 */
public class CombineByMask extends MarvinAbstractPluginImage{
	
	MarvinAttributes attributes;
	
	MarvinImage imageToCombine;
	Color colorMask;
	
	public void load(){
		attributes = getAttributes();
	}
	
	public void show(){
		
	}
	
	public void process(MarvinImage a_imageIn, MarvinImage a_imageOut, MarvinAttributes a_attributesOut, MarvinImageMask a_mask, boolean a_previewMode){
		imageToCombine = (MarvinImage)attributes.get("imageToCombine");
		colorMask = (Color)attributes.get("colorMask");
			
		for(int y=0; y<a_imageIn.getHeight(); y++){
    		for(int x=0; x<a_imageIn.getWidth(); x++){
    			if(a_imageIn.getRGB(x, y) == colorMask.getRGB()){
    				a_imageOut.setRGB(x, y, imageToCombine.getRGB(x, y));
    			}
    			else{
    				a_imageOut.setRGB(x, y, a_imageIn.getRGB(x, y));
    			}
    		}
		}
	}
}
