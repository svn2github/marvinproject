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

package marvin.plugin;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.util.MarvinAttributes;

public interface MarvinImagePlugin extends MarvinPlugin
{
	/**
	 * Initializes objects and attributes.
	 */
	public void load();
	
	/**
	 * Shows a graphical interface 
	 */
	public void show();
	
	/**
	 * Executes the algorithm.
	 * @param a_imageIn			- input image.
	 * @param a_imageOut		- output image.
	 * @param a_attributesOut	- output attributes.
	 * @param a_mask			- mask containing what pixels should be considered.
	 * @param a_previewMode		- it is or isn´t on preview mode.
	 */
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut, 
		MarvinAttributes a_attributesOut, 
		MarvinImageMask a_mask, 
		boolean a_previewMode
	);
	
	/**
	 * @return MarvinAttributes object associated with this plug-in
	 */
	public MarvinAttributes getAttributes();
	
	/**
	 * Set an attribute
	 * @param a_attrName	- attribute´s name
	 * @param a_value		- attribute´s value
	 **/
	public void setAttribute(String a_attrName, Object a_value);
	
	/**
	 * @param a_attrName	- atribute´s name
	 * @return the attribute´s value
	 */
	public Object getAttribute(String a_attrName);
	
	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param a_imagePanel	- reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel a_imagePanel);
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel();
}