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

package marvin.util;

import java.util.Hashtable;
import java.util.LinkedList;

/**
 * This class stores and manages plug-ins attributes and either 
 * integrates them with Marvin features, such as FilterHistory,
 * Statistics and MarvinPluginWindow.
 * 
 * @version 02/13/08
 */
public class MarvinAttributes
{
	protected Hashtable<String, Object> hashAttributes;
	protected LinkedList<String> listAttributesOrder;

	/**
	 * Constructor
	 */
	public MarvinAttributes(){
		hashAttributes = new Hashtable<String, Object>();
		listAttributesOrder = new LinkedList<String>();
	}

	/**
	 * Set an attribute.
	 * @param name		attribute name.
	 * @param value		attribute value.
	 */
	public void set(String name, Object value){
		if(!hashAttributes.containsKey(name)){
			listAttributesOrder.add(name);
		}
		hashAttributes.put(name, value);		
	}

	/**
	 * Get an attribute by its name.
	 * @param name		attribute´s name.
	 * @return the specified attribute as an Object.
	 */
	public Object get(String name){
		return hashAttributes.get(name);
	}

	/**
	 * Returns all attributes´ name and value as a String array.
	 * @return string array with all attributes´ name and value.
	 */
	public String[] toStringArray(){
		String key;
		String attrs[] = new String[hashAttributes.size()*2];
		Object[] l_keysInOrder = listAttributesOrder.toArray();
		for(int x=0; x<l_keysInOrder.length; x++){
			key = l_keysInOrder[x].toString();
			attrs[(x*2)] = key;
			attrs[(x*2)+1] = ""+hashAttributes.get(key);
		}
		return attrs;
	}
	
	/**
	 * Clones a MarvinAttributes Object.
	 */
	public MarvinAttributes clone(){
		MarvinAttributes attrs = new MarvinAttributes();
		String key;
		Object[] keysInOrder = listAttributesOrder.toArray();
		for(int x=0; x<keysInOrder.length; x++){
			key = keysInOrder[x].toString();
			attrs.set(key, hashAttributes.get(key));
		}		
		return attrs;
	}
}