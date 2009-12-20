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

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinPluginTool;

/**
 * Panel to display and edit MarvinImages.
 * @authors Gabriel Ambrosio Archanjo
 */
public class MarvinEditableImagePanel extends MarvinImagePanel implements Runnable{
	
	private MarvinToolPanel		toolPanel;
	private MarvinImageMask		imageMask;
	
	private MarvinPluginTool	tempTool;
	private Thread				thread;
	
	// Mouse attrbutes
	private MouseEvent			mouseEvent;
	private boolean				pressed;
	
	// Event Handlers
	private MouseHandler 		mouseHandler;
	
	private Point				locationOnScreen;
	/**
	 * Constructor
	 */
	public MarvinEditableImagePanel(){
		super();
		
		imageMask = new MarvinImageMask();
		
		mouseHandler = new MouseHandler();
		
		addMouseListener(mouseHandler);
		
		pressed = false;
	}
	
	public void setImage(MarvinImage a_image){
		super.setImage(a_image);
		imageMask = new MarvinImageMask(a_image.getWidth(), a_image.getHeight());	
	}
	
	/**
	 * Associate a MarvinToolPanel with this MarvinImagePanel.
	 * @param a_toolPanel - MarvinToolPanel Object.
	 */
	public void setToolPanel(MarvinToolPanel a_toolPanel){
		if(toolPanel != a_toolPanel){
			toolPanel = a_toolPanel;
			toolPanel.setImagePanel(this);
			
			if(thread == null){
				thread = new Thread(this);
				thread.start();
			}
		}
	}
	
	/**
	 * Return the associated MarvinToolPanel object.
	 * @return toolPanel reference.
	 */
	public MarvinToolPanel getToolPanel(){
		return toolPanel;
	}
	
	/**
	 * Runnable run() method implementation.
	 */
	int l_mousePx, l_mousePy;
	public void run(){
		
		while(true){
			if(tempTool != null){
				locationOnScreen = getLocationOnScreen();
				l_mousePx = (int)MouseInfo.getPointerInfo().getLocation().getX();
				l_mousePy = (int)MouseInfo.getPointerInfo().getLocation().getY();
				l_mousePx -= locationOnScreen.x;
				l_mousePy -= locationOnScreen.y;
				
				
				if(pressed){	
					tempTool.mousePressed(image, imageMask, l_mousePx, l_mousePy);
					image.update();
				}
								
				update();				
			}			
		}
	}
	
	public void paintComponent(Graphics a_graphics){
		super.paintComponent(a_graphics);

		if(tempTool != null){
			tempTool.update(a_graphics);
		}
		//if(bufferedImage != null){
		//	getGraphics().drawImage(bufferedImage, 0,0,this);
		//}
	}
	
	private void deleteSelected(){
		boolean[][] l_arrMask = imageMask.getMaskArray();
		for(int l_y=0; l_y<image.getHeight(); l_y++){
			for(int l_x=0; l_x<image.getWidth(); l_x++){
				if(!l_arrMask[l_x][l_y]){
					continue;
				}
				image.setIntColor(l_x, l_y, 0xFFFFFFFF);
			}
		}
		
		// Update image and ImagePanel
		image.update();
		update();
		// Clear mask for a new selection
		//imageMask.clear();
		
	}
	
	private class MouseHandler implements MouseListener{
		
		public void mouseClicked(MouseEvent a_event){
			toolPanel.getCurrentTool().mouseClicked(image, imageMask, a_event.getX(), a_event.getY());		
		}

		public void mouseEntered(MouseEvent a_event) {}

		public void mouseExited(MouseEvent a_event) {}

		public void mousePressed(MouseEvent a_event) {
			mouseEvent = a_event;
			if(toolPanel != null){
				tempTool = toolPanel.getCurrentTool();
			}
			pressed = true;
		}

		public void mouseReleased(MouseEvent a_event){
			toolPanel.getCurrentTool().mouseReleased(image, imageMask, l_mousePx, l_mousePy);
			pressed = false;
		}
	}
}
