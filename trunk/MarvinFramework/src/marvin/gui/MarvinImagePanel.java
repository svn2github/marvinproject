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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import marvin.image.MarvinImage;
import marvin.util.MarvinPluginHistory;

/**
 * Panel to display MarvinImages.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImagePanel extends JPanel{
	
	private MarvinImage 		image;
	private MarvinPluginHistory history;
	private boolean				fitSizeToImage;
	
	/**
	 * Constructor
	 */
	public MarvinImagePanel(){
		super();
		fitSizeToImage = true;
	}
	
	/**
	 * Enable history
	 */
	public void enableHistory(){
		history = new MarvinPluginHistory();
	}
	
	/**
	 * Disable history
	 */
	public void disableHistory(){
		history = null;
	}
	
	/**
	 * @return if the history is enabled.
	 */
	public boolean isHistoryEnabled(){
		return (history != null);
	}
	
	/**
	 * @return MarvinPluginHistory associated with this panel
	 */
	public MarvinPluginHistory getHistory(){
		return history;
	}
	
	/**
	 * Instantiates the MarvinImage object and returns its BufferedImage as off-screen 
	 * drawable image to be used for double buffering. 
	 * @param a_width - image큦 width
	 * @param a_height - image큦 width
	 */
	public Image createImage(int a_width, int a_height){
		image = new MarvinImage(a_width, a_height);		
		setPrefferedSize(new Dimension(a_width, a_height));		
		return image.getBufferedImage();
	}
	
	/**
	 * Set panel큦 preferred size
	 * @param 	a_dimension	- new panel큦 preferred dimensions
	 */
	public void setPrefferedSize(Dimension a_dimension){
		setPreferredSize(a_dimension);
	}
	
	/**
	 * @return panel큦 size
	 */
	public Dimension getSize(){
		return getPreferredSize();
	}
	
	/**
	 * Associates a MarvinImage to the image panel.
	 * @param a_image - image큦 reference to be associated with the image panel.
	 */
	public void setImage(MarvinImage a_image){
		image = a_image;
		if(fitSizeToImage && a_image != null){
			setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		}		
		repaint();
	}
	
	/**
	 * @return the MarvinImage associated with this MarvinImagePanel
	 */
	public MarvinImage getImage(){
		return image;
	}
	
	/**
	 * Overwrite the paint method
	 */
	public void paintComponent(Graphics a_graphics){
		super.paintComponent(a_graphics);
		
		if(image != null){
			a_graphics.drawImage(image.getBufferedImage(), 0,0,this);
		}
	}
	
	/**
	 * Update component큦 graphical representation
	 */
	public void update(){
		repaint();
	}
}
