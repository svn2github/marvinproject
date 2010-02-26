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

package org.marvinproject.image.artistic.television;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Represent an image using red, green and blue lines.
 * @author Gabriel Ambrosio Archanjo
 * @version 1.0 02/28/2008
 */
public class Television extends MarvinAbstractImagePlugin
{
	public void load(){}

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
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		int r,g,b;
		for (int x = 0; x < a_imageIn.getWidth(); x++) {			
			for (int y = 0; y < a_imageIn.getHeight(); y+=3) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				r=0;
				g=0;
				b=0;
					
				for(int w=0; w<3; w++){
					if(y+w < a_imageIn.getHeight() ){
						r += (a_imageIn.getIntComponent0(x, y+w))/2;
						g += (a_imageIn.getIntComponent1(x, y+w))/2;
						b += (a_imageIn.getIntComponent2(x, y+w))/2;						
					}
				}
				r = getValidInterval(r);
				g = getValidInterval(g);
				b = getValidInterval(b);
						
				for(int w=0; w<3; w++){
					if(y+w < a_imageOut.getHeight()){
						if(w == 0){
							a_imageOut.setIntColor(x,y+w,r,0,0);
						}
						else if(w ==1){
							a_imageOut.setIntColor(x,y+w,0,g,0);
						}
						else if(w==2){
							a_imageOut.setIntColor(x,y+w,0,0,b);
						}
					}
				}				
			}
		}
	}

	public int getValidInterval(int a_value){
		if(a_value < 0) return 0;
		if(a_value > 255) return 255;
		return a_value;
	}
}