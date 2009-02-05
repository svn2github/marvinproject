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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import marvin.gui.MarvinPluginWindowComponent.ComponentType;
import marvin.util.MarvinAttributes;

/**
 * Generic Window for plug-ins. This window supports integrating Swing components 
 * with MarvinAttributes. Components events are automatically handled.
 *
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginWindow extends JFrame
{
	// Components Collections
	protected Hashtable<String,MarvinPluginWindowComponent> hashComponents;
	protected Enumeration <MarvinPluginWindowComponent> enumComponents;

	// Interface
	protected GridLayout dynamicPanelLayout;
	protected Container container;
	protected JPanel	panelDynamicComponents, 
							panelCenter,
							panelCurrent;
	/**
	 * Constructs an {@link MarvinPluginWindow}
	 * @param a_strName Window name
	 * @param a_width width
	 * @param a_height height
	 */
	public MarvinPluginWindow
	(
		String a_strName, 
		int a_width, 
		int a_height
	){
		super(a_strName);
		
		// Parameters
		setSize(a_width,a_height);
		//setLocation(a_marvinApplication.getLocation());

		hashComponents = new Hashtable<String, MarvinPluginWindowComponent>(10,5);
		container = getContentPane();
		container.setLayout(new FlowLayout());
		
		// Dynamic Components
		panelCenter = new JPanel();
		panelCenter.setLayout(new FlowLayout());
		dynamicPanelLayout = new GridLayout(0,1);
		panelDynamicComponents = new JPanel();
		panelDynamicComponents.setLayout(dynamicPanelLayout);
		panelCenter.add(panelDynamicComponents);
		addPanelBelow();
		
		//Window Layout
		container.setLayout(new BorderLayout());
		container.add(panelCenter, BorderLayout.CENTER);
	}

	/**
	 * Adds new component Line
	 */
	public void addPanelBelow(){
		JPanel l_panel = new JPanel();
		l_panel.setLayout(new FlowLayout());
		panelCurrent = l_panel;
		panelDynamicComponents.add(l_panel);
		dynamicPanelLayout.setRows(dynamicPanelLayout.getRows()+1);
		setSize(getWidth(), getHeight()+30);
	}
	
	/**
	 * This method is useful if the developer need to add an external component.
	 * @return current panel.
	 */
	public JPanel getCurrentPanel(){
		return panelCurrent;
	}

	/**
	 * Adds new component
	 * @param a_id
	 * @param a_component
	 * @param a_attributeID
	 * @param a_attributes
	 * @param a_type
	 */
	public void plugComponent(String a_id, JComponent a_component, String a_attributeID, MarvinAttributes a_attributes, ComponentType a_type){
		panelCurrent.add(a_component);
		hashComponents.put(a_id, new MarvinPluginWindowComponent(a_id, a_attributeID, a_attributes, a_component, a_type));
	}
	
	/**
	 * Gets Component
	 * @param a_id
	 */	
	public MarvinPluginWindowComponent getComponent(String a_strComponentID){
		return hashComponents.get(a_strComponentID);
	}
	
	/**
	 * Adds label
	 * @param a_id
	 * @param a_label
	 */
	public void addLabel(String a_id, String a_label){
		JComponent l_component = new JLabel(a_label);
		panelCurrent.add(l_component);
		plugComponent(a_id, l_component, null, null, ComponentType.COMPONENT_LABEL);
	}
	
	/**
	 * Adds image
	 * @param a_id
	 * @param a_image
	 */
	public void addImage(String a_id, BufferedImage a_image){
		JComponent l_component = new JLabel(new ImageIcon(a_image));
		panelCurrent.add(l_component);
		plugComponent(a_id, l_component, null, null, ComponentType.COMPONENT_IMAGE);
	}
	
	/**
	 * Adds TextField
	 * @param a_id
	 * @param a_attributeID
	 * @param a_attributes
	 */
	public void addTextField(String a_id, String a_attributeID, MarvinAttributes a_attributes)
	{
		JComponent l_component = new JTextField(5);
		((JTextField)(l_component)).setText(a_attributes.get(a_attributeID).toString());
		plugComponent(a_id, l_component, a_attributeID, a_attributes, ComponentType.COMPONENT_TEXTFIELD);
	}
	
	/**
	 * 
	 * @param a_id				- component큦 id.
	 * @param a_attributeID		- attribute큦 id.
	 * @param a_lines			- number of lines.
	 * @param a_columns			- number of columns
	 * @param a_attributes		- MarivnAttributes Object
	 */
	public void addTextArea(String a_id, String a_attributeID, int a_lines, int a_columns, MarvinAttributes a_attributes)
	{
		JComponent l_component = new JTextArea(a_lines, a_columns);
		JScrollPane l_scrollPane = new JScrollPane(l_component);
		((JTextArea)(l_component)).setText(a_attributes.get(a_attributeID).toString());
		
		// plug manually
		panelCurrent.add(l_scrollPane);
		hashComponents.put(a_id, new MarvinPluginWindowComponent(a_id, a_attributeID, a_attributes, l_component, ComponentType.COMPONENT_TEXTAREA));
		
		//plugComponent(a_id, l_scrollPane, a_attributeID, a_attributes, ComponentType.COMPONENT_TEXTAREA);
	}
	
	/**
	 * Add ComboBox
	 * @param a_id				- component큦 id.
	 * @param a_attributeID		- attribute id.
	 * @param a_arrItems		- items array.
	 * @param a_attributes		- MarvinAttributes object.
	 */
	public void addComboBox(String a_id, String a_attributeID, Object[] a_arrItems, MarvinAttributes a_attributes){
		JComponent l_component = new JComboBox(a_arrItems);
		plugComponent(a_id, l_component, a_attributeID, a_attributes, ComponentType.COMPONENT_COMBOBOX);
	}
	
	/**
	 * Add Slider
	 * @param a_id				- component큦 id.
	 * @param a_attributeID		- attribute큦 id.
	 * @param a_orientation		- slider orientation
	 * @param a_min				- minimum value.
	 * @param a_max				- maximum value.
	 * @param a_value			- initial value.
	 * @param a_attributes		- MarvinAttributes object
	 */
	protected void addSlider(String a_id, String a_attributeID, int a_orientation, int a_min, int a_max, int a_value, MarvinAttributes a_attributes){
		JComponent l_component = new JSlider(a_orientation, a_min, a_max, a_value);
		plugComponent(a_id, l_component, a_attributeID, a_attributes, ComponentType.COMPONENT_SLIDER);
	}

	/**
	 * Add HorizontalSlider
	 * @param a_id			- component큦 ID.
	 * @param a_attributeID	- attribute큦 ID.
	 * @param a_min			- minimum value.
	 * @param a_max			- maximum value.
	 * @param a_value		- initial value.
	 * @param a_attributes	- MarvinAttributes object
	 */
	public void addHorizontalSlider(String a_id, String a_attributeID, int a_min, int a_max, int a_value, MarvinAttributes a_attributes){
		addSlider(a_id, a_attributeID, SwingConstants.HORIZONTAL, a_min, a_max, a_value, a_attributes);
	}
	
	/**
	 * Add VerticalSlider
	 * @param a_id			- component큦 ID.
	 * @param a_attributeID	- attribute큦 ID.
	 * @param a_min			- minimum value.
	 * @param a_max			- maximum value.
	 * @param a_value		- initial value.
	 * @param a_attributes	- MarvinAttributes object
	 */
	public void addVerticalSlider(String a_id, String a_attributeID, int a_min, int a_max, int a_value, MarvinAttributes a_attributes){
		addSlider(a_id, a_attributeID, SwingConstants.VERTICAL, a_min, a_max, a_value, a_attributes);
	}

	/**
	 * Update the attributes� value based on the associated components.
	 */
	public void applyValues(){
		MarvinPluginWindowComponent l_filterComponent;
		enumComponents = hashComponents.elements();
		while(enumComponents.hasMoreElements()){
			l_filterComponent = enumComponents.nextElement();
			if(l_filterComponent.getAttributes() != null){
				l_filterComponent.getAttributes().set(l_filterComponent.getAttributeID(), getValue(l_filterComponent));
			}
		}
	}

	/**
	 * Converts a string to the attribute type.
	 * @param a_value	- attribute큦 value.
	 * @param a_type	- attribute큦 type.
	 * @return value as the specified type.
	 */
	public Object stringToType(String a_value, Object a_type){
		Class<?> l_class = a_type.getClass();
		if(l_class == Double.class){
			return Double.parseDouble(a_value);
		}
		else if(l_class == Float.class){
			return Float.parseFloat(a_value);
		}
		else if(l_class == Integer.class){
			return Integer.parseInt(a_value);
		}
		else if(l_class == String.class){
			return a_value.toString();
		}
		return null;
	}

	/**
	 * @param a_pluginComponent - graphical component;
	 * @return the value associated with the specified component
	 */
	public Object getValue(MarvinPluginWindowComponent a_pluginComponent){
		String l_id = a_pluginComponent.getAttributeID();
		MarvinAttributes l_attributes = a_pluginComponent.getAttributes();
		JComponent l_component = a_pluginComponent.getComponent();
		
		switch(a_pluginComponent.getType()){
			case COMPONENT_TEXTFIELD:
				return stringToType( ((JTextField)l_component).getText(), l_attributes.get(l_id));
			case COMPONENT_COMBOBOX:
				return ( ((JComboBox)l_component).getSelectedItem());
			case COMPONENT_SLIDER:
				return ( ((JSlider)l_component).getValue());
			case COMPONENT_TEXTAREA:
				return ( ((JTextArea)l_component).getText());
		}
		return null;		
	}
}
