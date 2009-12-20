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

package net.sourceforge.marvinproject.segmentation.imageSlicer;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinErrorHandler;
import marvin.util.MarvinFileChooser;


/**
 * Image Slicer plug-in.
 * @author Hugo Henrique Slepicka
 * @version 1.1 05/11/2008
 */
public class ImageSlicer extends MarvinAbstractPluginImage {
	private MarvinAttributes attributes;
	private MarvinPerformanceMeter performanceMeter;
	private DecimalFormat df = new DecimalFormat("000");
	
	public void load() {
		attributes = new MarvinAttributes();
		attributes.set("txtLines", 1);
		attributes.set("txtCols", 1);
		performanceMeter = new MarvinPerformanceMeter();
	}

	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{		
		int conti, contx, conty, col, line, r, g, b, lines, cols, limitl, limitc;
		
		//Initialize the variables...
		col = line = conti = contx = conty = 0;
		
		//Get the values from objects...
		lines = Integer.parseInt(attributes.get("txtLines").toString());
		cols = Integer.parseInt(attributes.get("txtCols").toString());
		
		 
		
		//Get the image width and height...
		int width    = a_imageIn.getWidth();
		int height   = a_imageIn.getHeight();
		
		//Set the vertical and horizontal limits of the images
		limitl = (height/lines);
		limitc = (width/cols); 
				
		if(a_previewMode != true){
			//Creates the list to store the marvin Images of the sliced image...
			List<MarvinImage> listaImgs = new ArrayList<MarvinImage>();
			
			//Sets the default image to the images on the list...
			for(int x=0;x<(lines*cols);x++){
				listaImgs.add(new MarvinImage(limitc, limitl));
			}

			//Creates the Performance Meter...
			performanceMeter.enableProgressBar("Image Slicer", (lines*cols*limitc*limitl));
			int x,y=0;
			for(line=0;line<lines;line++){
				for(col=0;col<cols;col++){
					for(x=(line*limitl);x<((line+1)*limitl);x++){
						for(y=(col*limitc);y<((col+1)*limitc);y++){
							r = a_imageIn.getIntComponent0(y, x);
							g = a_imageIn.getIntComponent1(y, x);
							b = a_imageIn.getIntComponent2(y, x);

							listaImgs.get(conti).setIntColor(conty, contx, r, g, b);
							conty++;
							performanceMeter.incProgressBar(limitc);
						}
						conty=0;
						contx++;
						performanceMeter.incProgressBar(limitl);
					}
					listaImgs.get(conti).update();
					contx=conty=0;
					conti++;
				}

				contx=conty=0;
			}
			performanceMeter.finish();
			//Sets the path to save the files...
			String arq = "";
			int foto = 0;
			try {
				arq = MarvinFileChooser.select(null, false, MarvinFileChooser.SAVE_DIALOG);
				//Save the generated Images...
				for(x=0;x<lines;x++){
					for(y=0;y<cols;y++){
						String prefix = arq.replaceAll(".jpg" , "");
						if(prefix != null){
							prefix += df.format(x+1)+"x"+df.format(y+1)+".jpg";
							File grava = new File(prefix);
							ImageIO.write(listaImgs.get(foto).getBufferedImage(), "jpg", grava);
							foto ++;
						}
					}

				}
			} catch (Exception e3) {
				MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_SAVE, e3);
			}
			if(arq!=null){
				JOptionPane.showMessageDialog(null, "Files saved successfully at: "+arq.replaceAll(".jpg" , ""),
						"Marvin", JOptionPane.INFORMATION_MESSAGE);
			}
			a_imageOut.setIntColorArray(a_imageIn.getIntColorArray());
		}else{
			//Print the limits of the image
			performanceMeter.enableProgressBar("Image Slicer", (lines*cols*limitc*limitl));
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				for (int x = 0; x < a_imageIn.getWidth(); x++) {
					if(((x!=0)&&(x%limitc == 0))||((y!=0)&&(y%limitl == 0))){
						a_imageOut.setIntColor(x, y, 255, 0, 0);
					}
					else{
						a_imageOut.setIntColor(x, y, a_imageIn.getIntColor(x,y));
					}
				}	
				performanceMeter.incProgressBar(width-2);
			}
			performanceMeter.finish();
		}
		
	}

	public void show() {
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Image Slicer",400,350,getImagePanel(),this);
		
		//Create the objects to set the number of lines and collumns to slice the image...
		l_filterWindow.addLabel("lblLinhas", "Number of rows:");
		l_filterWindow.addTextField("txtLines", "txtLines", attributes);
		//l_filterWindow.addHorizontalSlider("txtLines", "txtLines", 1, 10, 2, attributes);
		l_filterWindow.addPanelBelow();
		
		l_filterWindow.addLabel("lblCols", "Number of columns:");
		l_filterWindow.addTextField("txtCols", "txtCols", attributes);
		//l_filterWindow.addHorizontalSlider("txtCols", "txtCols", 1, 10, 2, attributes);
		l_filterWindow.addPanelBelow();
		
		l_filterWindow.setVisible(true);
	}
}
