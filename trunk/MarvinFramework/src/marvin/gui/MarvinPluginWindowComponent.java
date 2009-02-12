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

package marvin.gui;

import javax.swing.JComponent;

import marvin.util.MarvinAttributes;

/**
 * Generic component for PluginWindow.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginWindowComponent
{
	
	public enum ComponentType{
		COMPONENT_TEXTFIELD, COMPONENT_SLIDER, COMPONENT_COMBOBOX, 
		COMPONENT_LABEL, COMPONENT_IMAGE, COMPONENT_TEXTAREA, COMPONENT_CHECKBOX
	};
	
	protected String id;
	protected String attributeID;
	protected MarvinAttributes attributes;
	protected JComponent component;
	ComponentType type;
	
	/**
	 * Constructs a new {@link MarvinPluginWindowComponent}
	 * @param a_id
	 * @param a_attributeID 
	 * @param a_attributes {@link MarvinAttributes}
	 * @param a_component {@link JComponent}
	 * @param a_type
	 */
	public MarvinPluginWindowComponent(String a_id, String a_attributeID, MarvinAttributes a_attributes, JComponent a_component, ComponentType a_type){
		id = a_id;
		attributeID = a_attributeID;
		attributes = a_attributes;
		component = a_component;
		type = a_type;
	}

	/**
	 * @return the component´s ID
	 */
	public String getID(){
		return id;
	}

	/**
	 * @return the ID of the attribute associated with the component.
	 */
	public String getAttributeID(){
		return attributeID;
	}

	/**
	 * 
	 * @return atribute object´s reference.
	 */
	public MarvinAttributes getAttributes(){
		return attributes;
	}

	/**
	 * @return the Swing component
	 */
	public JComponent getComponent(){
		return component;
	}

	/**
	 * @return the component´s type.
	 */
	public ComponentType getType(){
		return type;
	}
}