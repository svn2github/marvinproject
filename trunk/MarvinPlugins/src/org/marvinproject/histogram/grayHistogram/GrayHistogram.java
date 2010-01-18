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

package org.marvinproject.histogram.grayHistogram;

import java.awt.Color;

import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.statistic.MarvinHistogram;
import marvin.statistic.MarvinHistogramEntry;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;

/**
 * Gray histogram is a representation of the gray scale distribution.
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 02/13/2008
 */
public class GrayHistogram extends MarvinAbstractImagePlugin
{
	MarvinImagePlugin pluginGray;
    public void load(){
    	pluginGray = MarvinPluginLoader.loadImagePlugin("org.marvinproject.color.grayScale.jar");
    }

    public void show(){
    	MarvinImage l_image = getImagePanel().getImage();
    	process(l_image, new MarvinImage(l_image.getWidth(), l_image.getHeight()), null, MarvinImageMask.NULL_MASK, false);
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
    	pluginGray.process(a_imageIn, a_imageOut, a_attributesOut, a_mask, a_previewMode);
    	
        MarvinPluginWindow l_pluginWindow = new MarvinPluginWindow("Gray Histogram", 440,195);

        MarvinHistogram l_histoGray = new MarvinHistogram("Gray Intensity");
        l_histoGray.setBarWidth(1);

        int l_arrGray[] = new int[256];
        
        for (int x = 0; x < a_imageOut.getWidth(); x++) {
            for (int y = 0; y < a_imageOut.getHeight(); y++) {
                l_arrGray[a_imageOut.getIntComponent0(x, y)]++;
            }
        }

        for(int x=0; x<256; x++){
            l_histoGray.addEntry(new MarvinHistogramEntry(x, l_arrGray[x], new Color(x, x, x)));
        }

        l_pluginWindow.addImage("histoGray", l_histoGray.getImage(400,200));
        l_pluginWindow.addPanelBelow();
        l_pluginWindow.setVisible(true);
    }
}