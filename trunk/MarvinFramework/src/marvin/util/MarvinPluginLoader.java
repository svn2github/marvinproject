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
