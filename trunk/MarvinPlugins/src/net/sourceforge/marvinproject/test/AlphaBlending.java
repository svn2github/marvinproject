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

package net.sourceforge.marvinproject.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinErrorHandler;
import marvin.util.MarvinFileChooser;

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

/**
 * Alpha-Blending plug-in.
 * 
 * @author Alan Thiago do Prado
 * @version 1.0 29/01/2009
 */
public class AlphaBlending extends MarvinAbstractPluginImage {
	final String PERCENT = "Erro: %Image 1 + %Image 2 ultrapassa 100%";
	final String OUTOFBOUNDS="Erro: xi ou yi maior que o maximo tamanho permitido";
	private MarvinAttributes attributes;
	private int xi = 0;
	private int yi = 0;
	private int percent_image1 = 50;
	private int percent_image2 = 50;
	MarvinPerformanceMeter performanceMeter;
	MarvinFileChooser fileChooser;

	public int verify(int a) {
		if (a>0)
			return a;
		else
			return -a;
	}

	public boolean verify(int a, int b) {
	if(a+b>100)
		return true;
	else 
		return false;
	}

	@Override
	public void load() {
		attributes = getAttributes();
		attributes.set("xi", xi);
		attributes.set("yi", yi);
		attributes.set("percent_image1", percent_image1);
		attributes.set("percent_image2", percent_image2);
		performanceMeter = new MarvinPerformanceMeter();
	}

	@Override
	public void process
	(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		System.out.println("PROCESS");
		// Selects the other image to apply AlphaBlending
		boolean erro = false;
		String file = null;
		BufferedImage buffImage2 = null;
		
		try {
			// Open the file browser dialog
			file = MarvinFileChooser.select(null, true,
					MarvinFileChooser.OPEN_DIALOG);
		} catch (Exception e) {
			MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_CHOOSE, e);
			return;
		}
		
		System.out.println("file:"+file);

		if (file == null)
			return;

		// Loads the image to the memory and creates an MarvinImage
		try {
			buffImage2 = ImageIO.read(new File(file));
		} catch (IOException ioe) {
			MarvinErrorHandler.handle(MarvinErrorHandler.TYPE.ERROR_FILE_OPEN, ioe);
			return;
		}

		MarvinImage image2 = new MarvinImage(buffImage2);
		
		int maxH = 0;
		int maxW = 0;
		int minH = 0;
		int minW = 0;

		if (image2.getHeight() >= a_imageIn.getHeight()) {
			maxH = image2.getHeight();
			minH = a_imageIn.getHeight();
		} else {
			maxH = a_imageIn.getHeight();
			minH = image2.getHeight();
		}

		if (image2.getWidth() >= a_imageIn.getWidth()) {
			maxW = image2.getWidth();
			minW = a_imageIn.getWidth();
		} else {
			maxW = a_imageIn.getWidth();
			minW = image2.getWidth();
		}

		//Create a MarvinImage with maxWidht and maxHeignt
		//MarvinImage image3 = new MarvinImage(maxW, maxH);
		a_imageOut.setDimension(maxW, maxH);
		
		//Get MarvinAttributes
		int xi = (Integer) attributes.get("xi");
		int yi = (Integer) attributes.get("yi");
		int p1 = verify((Integer) attributes.get("percent_image1"));
		int p2 = verify((Integer) attributes.get("percent_image2"));
		
		if (xi>maxH){
			MarvinErrorHandler.handle(OUTOFBOUNDS);
			erro=true;
		}
		else{
			if (yi>maxH){
				MarvinErrorHandler.handle(OUTOFBOUNDS);
				erro=true;
			}
			else{
				if (verify(p1, p2)) {
					MarvinErrorHandler.handle(PERCENT);
					erro=true;				
				}
			}
		}
						
		int l_r1 = 0;
		int l_r2 = 0;
		int l_g1 = 0;
		int l_g2 = 0;
		int l_b1 = 0;
		int l_b2 = 0;

		if(erro == false){
		for (int y = 0; y < maxH; y++) {
			for (int x = 0; x < maxW; x++) {
				//Prints White Image
				l_r1 = 255;
				l_g1 = 255;
				l_b1 = 255;
				a_imageOut.setRGB(x, y, l_r1, l_g1, l_b1);
				//Prints a_image
				if (y < a_imageIn.getHeight() && x < a_imageIn.getWidth()) {
					l_r1 = a_imageIn.getRed(x, y);
					l_g1 = a_imageIn.getGreen(x, y);
					l_b1 = a_imageIn.getBlue(x, y);
					a_imageOut.setRGB(x, y, l_r1, l_g1, l_b1);
				}
				else{
				//Prints image2
				if (y < image2.getHeight() && x < image2.getWidth()) {
					l_r1 = image2.getRed(x, y);
					l_g1 = image2.getGreen(x, y);
					l_b1 = image2.getBlue(x, y);
					if((x+xi)<maxW&&(y+yi)<maxH)
						a_imageOut.setRGB(x+xi, y+yi, l_r1, l_g1, l_b1);
				}
				}
			}
		}
		
		//Apply Alpha-Blending => (p1*a_image+p2*image2)/100
		for (int y = 0; y < minH; y++) {
			for (int x = 0; x < minW; x++) {
				//Get the percent color p1 for each pixel from a_image
				if (y < a_imageIn.getHeight() && x < a_imageIn.getWidth()) {					
					if((x+xi)<minW&&(y+yi)<minH){
						if( (x+xi >= 0) && (x+xi <= a_imageIn.getWidth()) && (y+yi >= 0) && (y+yi <= a_imageIn.getHeight())){						
							l_r1 = a_imageIn.getRed  (x+xi, y+yi) * p1;
							l_g1 = a_imageIn.getGreen(x+xi, y+yi) * p1;
							l_b1 = a_imageIn.getBlue (x+xi, y+yi) * p1;
						}
					}
				}
				//Get the percent color p2 for each pixel from image2
				if (y < image2.getHeight() && x < image2.getWidth()) {
					l_r2 = image2.getRed  (x, y) * p2;
					l_g2 = image2.getGreen(x, y) * p2;
					l_b2 = image2.getBlue (x, y) * p2;
				}
				//Combine a_image with image2 into image3
				if((x+xi)<maxW&&(y+yi)<maxH){
					if( (x+xi >= 0) && (x+xi <= a_imageIn.getWidth()) && (y+yi >= 0) && (y+yi <= a_imageIn.getHeight())){
						a_imageOut.setRGB(x+xi, y+yi, 
								(l_r1 + l_r2) / 100, 
								(l_g1 + l_g2) / 100,
								(l_b1 + l_b2) / 100);
					}
				}
			}
		}
		//Set image3
		//a_imageIn.setBufferedImage(image3.getBufferedImage());
		}
		System.out.println("AQUI");
	}

	public void show() {
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow(
		"Alpha-Blending", 420, 350, getImagePanel(),	this);
		l_filterWindow.addLabel("lblxi", "xi:");
		l_filterWindow.addTextField("txtxi", "xi", attributes);
		l_filterWindow.addLabel("lblyi", "yi:");
		l_filterWindow.addTextField("txtyi", "yi", attributes);
		l_filterWindow.addPanelBelow();
		l_filterWindow.addLabel("lbpercent_image1", "%Image 1:");
		l_filterWindow.addTextField("txtpercent_image1","percent_image1",attributes);
		l_filterWindow.addLabel("lbpercent_image2", "%Image 2:");
		l_filterWindow.addTextField("txtpercent_image2", "percent_image2",attributes);
		l_filterWindow.setVisible(true);
	}
}
