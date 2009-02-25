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

import marvin.MarvinDefinitions;
import marvin.plugin.MarvinPlugin;
import marvin.plugin.MarvinPluginImage;

/**
 * Load plug-ins via MarvinJarLoader
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginLoader {
	
	/**
	 * Loads a MarvinPluginImage via MarvinJarLoader
	 * @param a_pluginPath	- plug-in´s jar file path.
	 * @return a loaded MarvinPluginImage.
	 */
	public static MarvinPluginImage loadPluginImage(String a_pluginPath){
		MarvinPluginImage l_plugin;
		String l_className = a_pluginPath.replace(".jar", "");
		if(l_className.lastIndexOf(".") != -1){
			l_className = l_className.substring(l_className.lastIndexOf(".")+1);
		}
		l_className = l_className.substring(0,1).toUpperCase()+l_className.substring(1);
		
		l_plugin = (MarvinPluginImage)loadPlugin(MarvinDefinitions.PLUGIN_IMAGE_PATH+a_pluginPath, l_className);
		l_plugin.load();
		return l_plugin;
	}
	
	/**
	 * Loads a MarvinPlugin via MarvinJarLoader
	 * @param a_pluginPath	- plug-in´s jar file path.
	 * @param l_className	- plug-in´s class name.
	 * @return
	 */
	private static MarvinPlugin loadPlugin(String a_pluginPath, String l_className){
		MarvinPlugin l_plugin;
		MarvinJarLoader l_loader = new MarvinJarLoader(a_pluginPath);
		l_plugin = (MarvinPlugin)l_loader.getObject(l_className);
		return l_plugin;
	}
}
