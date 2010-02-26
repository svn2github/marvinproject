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

package org.marvinproject.image.color.thresholding;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Thresholding
 * @author Gabriel Ambrosio Archanjo
 */
public class Thresholding extends MarvinAbstractImagePlugin{

	private MarvinAttributes attributes;
	private int threshold;
	private MarvinImagePlugin pluginGray;
	
	public void load(){
		
		attributes = getAttributes();
		attributes.set("threshold", 125);
		pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.color.grayScale.jar");
	}
	
	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Thresholding", 400,350, getImagePanel(), this);
		l_filterWindow.addLabel("lblThreshold", "Threshold");
		l_filterWindow.addTextField("txtThreshold", "threshold", attributes);		
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
		threshold = (Integer)attributes.get("threshold");
		
		pluginGray.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for(int y=0; y<a_imageIn.getHeight(); y++){
			for(int x=0; x<a_imageIn.getWidth(); x++){
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				if(a_imageIn.getIntComponent0(x,y) < threshold){
					a_imageOut.setIntColor(x, y, 0,0,0);
				}
				else{
					a_imageOut.setIntColor(x, y, 255,255,255);
				}				
			}
		}		
	}
}
