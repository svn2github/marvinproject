/**
MARVIN Project

Initial version by:
Danilo Rosetto Muñoz
Fábio Andrijauskas
Gabriel Ambrósio Archanjo

site: http://marvin.incubadora.fapesp.br/

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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

/**
 * Store all plug-ins used on an image. A strip image with all steps can be exported.
 * @version 1.0 02/13/08
 * @author Fabio Andrijauskas
 */
public class MarvinPluginHistory extends JFrame
{
	// Constants
	private final static int TOP_MARGIN = 50;
	private final static int BOTTOM_MARGIN = 10;
	private final static int ATTRIBUTES_MARGIN = 200;
	private final static String STORE_SUCCESS = "History exported sucessfully";
	private final static String STORE_FAILED = "Error while exporting the history";

	// Interface Components
	JPanel jpBtnHistoric;
	JButton btnExportHistoric;
	JPanel panelImg;
	JScrollPane scroll;

	private LinkedList<String> listPluginName;
	private LinkedList<MarvinImage> listMarvinImage;
	private LinkedList<MarvinAttributes> listMarvinAttributes;

	private JFrame frmhist;

	/**
	 * Constructs a new PluginHistory.
	 */
	public MarvinPluginHistory()
	{
		frmhist = this;

		//setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Plug-ins history");
		setSize(600, 250);
		setLayout(new GridLayout(2,1));

		listMarvinImage = new LinkedList<MarvinImage>();
		listMarvinAttributes = new LinkedList<MarvinAttributes>();
		listPluginName = new LinkedList<String>();
		
		jpBtnHistoric = new JPanel();

		btnExportHistoric = new JButton("Export");
		btnExportHistoric.addActionListener(new ExportButtonHandler());
		btnExportHistoric.setMnemonic('E');

		jpBtnHistoric.add(btnExportHistoric);

		this.add(jpBtnHistoric);

		panelImg = new JPanel();
		scroll = new JScrollPane(panelImg);
		panelImg.setLayout(new GridLayout(1,0));
		scroll.setLayout(new ScrollPaneLayout());
		add(scroll);
	}

	/**
	 * Add a new entry to the history.
	 *
	 * @param a_pluginName 	- Effect's name
	 * @param a_image 		- The modified {@code MarvinImage}
	 * @param a_attributes 	- The {@code MarvinAttributes} applied
	 * @see MarvinImage
	 * @see MarvinAttributes
	 */
	public void addEntry(String a_pluginName, MarvinImage a_image, MarvinAttributes a_attributes){
		listPluginName.add(a_pluginName);
		
		MarvinImage l_image=null;
		MarvinAttributes l_attributes=null;
		
		if(a_image != null){
			l_image = a_image.clone();
		}
		
		if(a_attributes != null){
			l_attributes = a_attributes.clone();
		}
		
		listMarvinImage.add(l_image);
		listMarvinAttributes.add(l_attributes);
	}
	
	/**
	 * @return the last image entry
	 */
	public MarvinImage getLastImageEntry(){
		return (MarvinImage)listMarvinImage.getLast();
	}

	/**
	 * Shows the history dialog.
	 */
	public void showThumbnailHistory(){
		panelImg.removeAll();
		for(MarvinImage l_image : listMarvinImage){
			panelImg.add(new JLabel(new ImageIcon(l_image.getImage(100, 100))));
			if(listMarvinImage.size() > 1)
			{
				panelImg.add(new JLabel(""));
			}
		}
		setVisible(true);
	}

	/**
	 * Clear the history.
	 */
	public void clear(){
		listPluginName = new LinkedList<String>();
		listMarvinImage = new LinkedList<MarvinImage>();
		listMarvinAttributes = new LinkedList<MarvinAttributes>();
	}

	/**
	 *	Invoked when export button is performed.
	 */
	private class ExportButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				String l_pluginName;
				MarvinImage l_image;
				MarvinAttributes l_attributes;
				String[] l_attributesAsString=null;
				int l_pos;
				int l_imagePx = 5;
				int l_attributePy=0;
				int width=0;
				int height=0;
				int i;


				// Calc the history image width and height
				for(MarvinImage img : listMarvinImage){
					width+=img.getWidth()+ATTRIBUTES_MARGIN;

					// The image height will be the biggest height from the listMarvinImage
					if(img.getHeight() > height){
						height = img.getHeight();
					}
				}
				height+=TOP_MARGIN+BOTTOM_MARGIN;

				String arq = MarvinFileChooser.select(frmhist, false, MarvinFileChooser.SAVE_DIALOG);

				if (arq != null) {
					BufferedImage bufExport = new BufferedImage(width+5,height, listMarvinImage.get(0).getType());
					Graphics2D g = bufExport.createGraphics();
					height =  listMarvinImage.get(0).getHeight();
					
					for(l_pos=0; l_pos<listMarvinImage.size(); l_pos++){
						l_pluginName = listPluginName.get(l_pos);
						l_image = listMarvinImage.get(l_pos);
						l_attributes = listMarvinAttributes.get(l_pos);

						if(l_attributes != null){
							l_attributesAsString = l_attributes.toStringArray();
						}

						g.drawImage(l_image.getBufferedImage(), null, l_imagePx, TOP_MARGIN);
						g.setColor(Color.white);
						g.drawRect(l_imagePx+l_image.getWidth()+5, TOP_MARGIN, ATTRIBUTES_MARGIN-10, l_image.getHeight());

						if(l_pluginName.lastIndexOf('.') != -1){
							g.drawString(l_pluginName.substring(l_pluginName.lastIndexOf('.')+1, l_pluginName.length()), l_imagePx+l_image.getWidth()+10, TOP_MARGIN+15);
						}
						else{
							g.drawString(l_pluginName, l_imagePx+l_image.getWidth()+10, TOP_MARGIN+15);
						}

						g.drawString("ATTRIBUTES:", l_imagePx+l_image.getWidth()+10, TOP_MARGIN+45);
						l_attributePy = TOP_MARGIN+30+45;

						if(l_attributes != null){
							g.setColor(Color.white);
							for(i=0; i<l_attributesAsString.length; i+=2){
								// Attribute name
								g.drawString(l_attributesAsString[i]+": "+l_attributesAsString[i+1], l_imagePx+l_image.getWidth()+10, l_attributePy);
								l_attributePy+=15;
							}
						}

						l_imagePx+=l_image.getWidth()+ATTRIBUTES_MARGIN;
					}

					// Draw Header
					g.setFont(new Font("Courier",Font.BOLD, 30));
					g.drawString("Marvin 1.2 - Plug-ins history", 5, 30);
					g.dispose();

					MarvinImage teste = new MarvinImage(bufExport);
					MarvinImageIO.saveImage(teste, arq);
					//if(ImageIO.write(bufExport, "jpg", new File(arq)))
					//{
					//	JOptionPane.showMessageDialog(frmhist, STORE_SUCCESS);
					//}else
					//{
					//	JOptionPane.showMessageDialog(frmhist, STORE_FAILED);
					//}
					frmhist.setVisible(false);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}


