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
import marvin.util.MarvinAttributes;

public abstract class MarvinAbstractPluginImage implements MarvinPluginImage
{
	//private Marvin marvinApplication;
	private MarvinAttributes marvinAttributes;
	private MarvinImagePanel imagePanel;

	/**
	 * Empty constructor 
	 */
	protected MarvinAbstractPluginImage(){
		marvinAttributes = new MarvinAttributes();
	}

	/**
	 * @return MarvinAttributes object associated with this plug-in
	 */
	public MarvinAttributes getAttributes(){
		return marvinAttributes;
	}
	
	/**
	 * Set an attribute
	 * @param a_attrName	- attribute큦 name
	 * @param a_value		- attribute큦 value
	 **/
	public void setAttribute(String a_label, Object a_value){
		marvinAttributes.set(a_label, a_value);
	}
	
	/**
	 * @param a_attrName	- atribute큦 name
	 * @return the attribute큦 value
	 */
	public Object getAttribute(String a_label){
		return marvinAttributes.get(a_label);
	}
	
	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param a_imagePanel	- reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel a_imagePanel){
		imagePanel = a_imagePanel;
	}
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel(){
		return imagePanel;
	}
}