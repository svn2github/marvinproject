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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;

/**
 * Generic Window for filters. This window includes thumbnail, preview and reset support.
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinFilterWindow extends MarvinPluginWindow
{
	protected JPanel 	panelFixedComponents, panelImage;

	protected JButton 	buttonPreview, buttonApply, buttonReset;
	protected JLabel 	labelImage;

	MarvinImagePanel	imagePanel;
	MarvinImage 		imageThumbnail;
	MarvinImage 		imageResetBuffer;
	MarvinImage			imageOut;
	MarvinImagePlugin 	plugin;	

	// ActionHandler
	protected ActionHandler actionHandler;

	/**
	 * Constructs a {@link MarvinFilterWindow}
	 * @param a_strName Window name
	 * @param a_width width
	 * @param a_height height
	 * @param a_image {@link MarvinImage}
	 * @param a_filter {@link MarvinFilter}
	 */
	public MarvinFilterWindow
	(
		String a_strName, 
		int a_width, 
		int a_height,
		MarvinImagePanel a_imagePanel,
		MarvinImagePlugin a_filter
	)
	{
		this(a_strName, a_width, a_height, 250, 250, a_imagePanel, a_filter);
	}

	/**
	 * Constructs a {@link MarvinFilterWindow}
	 * @param a_strName Window name
	 * @param a_width width
	 * @param a_height height
	 * @param a_thumbnailWidth thumbnail with
	 * @param a_thumbnailHeight thumbnail height
	 * @param a_image {@link MarvinImage}
	 * @param a_filter {@link MarvinFilter}
	 */
	public MarvinFilterWindow
	(
		String a_strName, 
		int a_width, 
		int a_height,
		int a_thumbnailWidth,
		int a_thumbnailHeight,
		MarvinImagePanel a_imagePanel,
		MarvinImagePlugin a_plugin
	)
	{
		super(a_strName, a_width, a_height);
		imagePanel = a_imagePanel;
		plugin = a_plugin;
		//Buttons
		actionHandler = new ActionHandler();
		buttonPreview = new JButton("Preview");
		buttonReset = new JButton("Reset");
		buttonApply = new JButton("Apply");
		
		buttonPreview.setMnemonic('P');
		buttonReset.setMnemonic('R');
		buttonApply.setMnemonic('A');

		buttonPreview.addActionListener(actionHandler);
		buttonReset.addActionListener(actionHandler);
		buttonApply.addActionListener(actionHandler);

		// Fixed Components
		panelFixedComponents = new JPanel();
		panelFixedComponents.setLayout(new FlowLayout());
		panelFixedComponents.add(buttonPreview);
		panelFixedComponents.add(buttonReset);
		panelFixedComponents.add(buttonApply);

		// Image Panel
		panelImage = new JPanel();
		panelImage.setLayout(new FlowLayout());

		// Image
		if(a_thumbnailWidth > 0 && a_thumbnailHeight > 0){
			imageThumbnail = new MarvinImage(imagePanel.getImage().getBufferedImage(a_thumbnailWidth, a_thumbnailHeight, MarvinImage.PROPORTIONAL));			
			imageResetBuffer = imageThumbnail.clone();
			labelImage = new JLabel(new ImageIcon(imageThumbnail.getBufferedImage()));
			panelImage.add(labelImage);
		}
		
		imageOut = new MarvinImage(imagePanel.getImage().getWidth(), imagePanel.getImage().getHeight());
		
		container.add(panelImage, BorderLayout.NORTH);
		container.add(panelCenter, BorderLayout.CENTER);
		container.add(panelFixedComponents, BorderLayout.SOUTH);

	}

	/**
	 * Disables preview function.
	 */
	public void disablePreview(){
		panelFixedComponents.remove(buttonPreview);
		panelFixedComponents.remove(buttonReset);
		panelImage.remove(labelImage);
	}
	
	/**
	 * Returns the reference to "Apply" button.
	 * @return a reference to "Apply" button
	 */
	public JButton getApplyButton(){
		return buttonApply;
	}
	
	/**
	 * Preview the plug-in application
	 */
	public void preview(){
		try{
			//marvinApplication.getPerformanceMeter().disable();
			imageThumbnail = imageResetBuffer.clone();
			MarvinImage l_imageOut = new MarvinImage(imageThumbnail.getWidth(), imageThumbnail.getHeight());
			plugin.process(imageThumbnail, l_imageOut, null, MarvinImageMask.NULL_MASK, true);
			l_imageOut.update();
			imageThumbnail = l_imageOut.clone();
			//marvinApplication.getPerformanceMeter().enable();
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		labelImage.setIcon(new ImageIcon(imageThumbnail.getBufferedImage()));
	}
	
	/**
	 * Reset to the original state
	 */
	public void reset(){
		imageThumbnail = new MarvinImage(imageResetBuffer.getNewImageInstance());
		labelImage.setIcon(new ImageIcon(imageThumbnail.getBufferedImage()));
	}

	/**
	 * Apply the plug-in
	 */
	public void apply(){		
		dispose();
		plugin.process(imagePanel.getImage(), imageOut, null, MarvinImageMask.NULL_MASK, false);
		
		if(imagePanel.isHistoryEnabled()){
			imagePanel.getHistory().addEntry(plugin.getClass().getSimpleName(), imageOut, plugin.getAttributes());
		}
		imageOut.update();
		imagePanel.setImage(imageOut);
	}

	/**
	 * Event handler class
	 */
	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e){
			if(e.getSource() == buttonApply){
				applyValues();
				apply();
			}			
			else if (e.getSource() == buttonReset){
				reset();	
			}
			else if(e.getSource() == buttonPreview){
				applyValues();
				preview();
			}
		}
	}
}