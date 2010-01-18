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


package org.marvinproject.interfaceTest;

import javax.swing.JCheckBox;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Marvin GUI test plug-in. This plug-in do not affect the image.
 * @version 02/28/08
 * @author Gabriel Ambrósio Archanjo
 */
public class InterfaceTest extends MarvinAbstractImagePlugin
{
	MarvinAttributes attributes;
	public void load(){
		attributes = getAttributes();
		attributes.set("red", 10);
		attributes.set("green", 50);
		attributes.set("blue", 125);
		attributes.set("filter", "option 1");
		attributes.set("intensity", "option 1");
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Interface Test", 500,350, getImagePanel(), this);
		l_filterWindow.addLabel("labelRed", "Red:");
		l_filterWindow.addTextField("textRed", "red", attributes);

		l_filterWindow.addLabel("labelGreen", "Green:");
		l_filterWindow.addTextField("textGreen", "green", attributes);

		l_filterWindow.addLabel("labelBlue", "Red:");
		l_filterWindow.addTextField("textBlue", "blue", attributes);

		l_filterWindow.addPanelBelow();
		l_filterWindow.addComboBox("comboFilter", "filter", new Object[]{"option 1", "option 2", "option 3"}, attributes);

		l_filterWindow.addPanelBelow();
		l_filterWindow.addLabel("labelIntensity", "Intensity:");
		l_filterWindow.addHorizontalSlider("sliderIntensity", "intensity", 0,100,0, attributes);
		
		l_filterWindow.addPanelBelow();
		l_filterWindow.addCheckBox("checkboxTest", "Check Me!", "checkbox1", attributes);
		
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
		System.out.println("Values received from GUI:");
		System.out.println("red:"+(Integer)attributes.get("red"));
		System.out.println("green:"+(Integer)attributes.get("green"));
		System.out.println("blue:"+(Integer)attributes.get("blue"));
		System.out.println("filter:"+(String)attributes.get("filter"));
		System.out.println("intensity:"+(Integer)attributes.get("intensity"));
		System.out.println("checkbox checked:"+(Boolean)attributes.get("checkbox1"));
		
		a_imageOut.setIntColorArray(a_imageIn.getIntColorArray());
	}
}