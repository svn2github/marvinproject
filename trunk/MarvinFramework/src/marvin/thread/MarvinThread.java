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

package marvin.thread;

import com.sun.xml.internal.bind.v2.TODO;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinPlugin;
import marvin.plugin.MarvinImagePlugin;

/**
 * Thread to process a segment or an entire image.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinThread implements Runnable{
	
	private enum PluginType{
		PLUGIN_IMAGE
	}
	
	private MarvinThreadListener 	listener;
	private Thread 					thread;
	private MarvinPlugin 			plugin;
	private MarvinImage 			imageIn,
									imageOut;
	private MarvinImageMask 		imageMask;
	PluginType 						eType;
	
	/**
	 * Constructor.
	 * @param a_plugin		- plugin associated with this thread.
	 * @param a_imageIn		- plug-in´s input image.
	 * @param a_imageOut	- plug-in´s output image.
	 * @param a_mask		- image mask.
	 */
	public MarvinThread
	(
		MarvinImagePlugin a_plugin,
		MarvinImage a_imageIn,
		MarvinImage a_imageOut,
		MarvinImageMask a_mask
	)
	{
		plugin = a_plugin;
		imageIn = a_imageIn;
		imageOut = a_imageOut;
		imageMask = a_mask;
		eType = PluginType.PLUGIN_IMAGE;
		thread = new Thread(this);
	}
	
	/**
	 * Starts the thread.
	 */
	public void start(){
		thread.start();
	}
	
	/**
	 * Set a thread listener.
	 * {@link TODO} 			- Support a list of listeners.
	 * @param a_listener		- listener object.
	 */
	public void addThreadListener(MarvinThreadListener a_listener){
		listener = a_listener;
	}
	
	/**
	 * Thread´s run method.
	 */
	public void run(){
		switch(eType){
			case PLUGIN_IMAGE:
				((MarvinImagePlugin)plugin).process(imageIn, imageOut, null, imageMask, false);
				if(listener != null){
					listener.threadFinished(new MarvinThreadEvent(plugin));
				}
				break;
		}
	}
}
