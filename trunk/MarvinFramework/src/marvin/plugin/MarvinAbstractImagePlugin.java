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

public abstract class MarvinAbstractImagePlugin implements MarvinImagePlugin
{
	//private Marvin marvinApplication;
	private MarvinAttributes marvinAttributes;
	private MarvinImagePanel imagePanel;

	/**
	 * Empty constructor 
	 */
	protected MarvinAbstractImagePlugin(){
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
	 * @param a_attrName	attribute큦 name
	 * @param value			attribute큦 value
	 **/
	public void setAttribute(String label, Object value){
		marvinAttributes.set(label, value);
	}
	
	/**
	 * @param a_attrName	atribute큦 name
	 * @return the attribute큦 value
	 */
	public Object getAttribute(String label){
		return marvinAttributes.get(label);
	}
	
	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param imgPanel	reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel imgPanel){
		imagePanel = imgPanel;
	}
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel(){
		return imagePanel;
	}
}