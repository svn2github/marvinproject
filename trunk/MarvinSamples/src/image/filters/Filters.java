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

package image.filters;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinPluginImage;
import marvin.util.MarvinPluginLoader;

/**
 * Filter application sample
 * @author Gabriel Ambrosio Archanjo
 */
public class Filters extends JFrame
{
	MarvinImagePanel imagePanel;
	MarvinImage image, backupImage;
	JPanel panelBottom;
	JButton buttonGray, buttonSepia, buttonInvert, buttonReset;
	
	public Filters()
	{
		super("Filters Sample");
		
		// Create Graphical Interface
		ButtonHandler buttonHandler = new ButtonHandler();
		buttonGray = new JButton("Gray");
		buttonGray.addActionListener(buttonHandler);
		buttonSepia = new JButton("Sepia");
		buttonSepia.addActionListener(buttonHandler);
		buttonInvert = new JButton("Invert");
		buttonInvert.addActionListener(buttonHandler);
		buttonReset = new JButton("Reset");
		buttonReset.addActionListener(buttonHandler);
		
		panelBottom = new JPanel();
		panelBottom.add(buttonGray);
		panelBottom.add(buttonSepia);
		panelBottom.add(buttonInvert);
		panelBottom.add(buttonReset);
		
		// ImagePanel
		imagePanel = new MarvinImagePanel();
		
		Container l_c = getContentPane();
		l_c.setLayout(new BorderLayout());
		l_c.add(imagePanel, BorderLayout.NORTH);
		l_c.add(panelBottom, BorderLayout.SOUTH);
		
		// Load image
		loadImage();
		
		imagePanel.setImage(image);
		
		setSize(320,600);
		setVisible(true);	
	}
	
	private void loadImage(){
		image = MarvinImageIO.loadImage("./res/arara.jpg");
		backupImage = image.clone();		
	}
	
	public static void main(String args[]){
		Filters t = new Filters();
		t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent a_event){
			image = backupImage.clone();
			if(a_event.getSource() == buttonGray){
				MarvinPluginImage l_pluginImage = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.grayScale.jar");
				l_pluginImage.process(image, image, null, MarvinImageMask.NULL_MASK, false);
			}
			else if(a_event.getSource() == buttonSepia){
				MarvinPluginImage l_pluginImage = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.sepia.jar");
				l_pluginImage.setAttribute("hsIntensidade", 50);				
				l_pluginImage.process(image, image, null, MarvinImageMask.NULL_MASK, false);
			}
			else if(a_event.getSource() == buttonInvert){
				MarvinPluginImage l_pluginImage = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.invert.jar");
				l_pluginImage.process(image, image, null, MarvinImageMask.NULL_MASK, false);
			}
			image.update();
			imagePanel.setImage(image);
		}
	}
}