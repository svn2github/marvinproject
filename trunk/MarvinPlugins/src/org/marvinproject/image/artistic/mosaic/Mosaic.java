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

package org.marvinproject.image.artistic.mosaic;


import java.awt.Color;
import java.awt.Graphics;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

/**
 * Mosaic implementation
 * @author Gabriel Ambrósio Archanjo
 * @version 1.0 03/01/2008
 */
public class Mosaic extends MarvinAbstractImagePlugin
{
	private final static String SQUARES = "squares";
	private final static String TRIANGLES = "triangles";

	private int width;
	private String shape;
	private boolean border;

	private MarvinAttributes attributes;
	private MarvinPerformanceMeter performanceMeter;

	public void load()
	{
		attributes = getAttributes();
		attributes.set("width", 6);
		attributes.set("shape", SQUARES);
		attributes.set("border", true);
		performanceMeter = new MarvinPerformanceMeter();
	}

	public void show(){
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Halftone - Circles", 420,350, getImagePanel(), this);
		l_filterWindow.addLabel("lblWidth", "Tile witdh:");
		l_filterWindow.addTextField("txtwidth", "width", attributes);
		l_filterWindow.newComponentRow();
		l_filterWindow.addLabel("lblWidth", "Format:");
		l_filterWindow.addComboBox("combShape", "shape", new Object[]{SQUARES, TRIANGLES}, attributes);
		l_filterWindow.newComponentRow();
		l_filterWindow.addLabel("lblWidth", "Edge:");
		l_filterWindow.addComboBox("combBorder", "border", new Object[]{true, false}, attributes);
		l_filterWindow.setVisible(true);		
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
		width = (Integer)attributes.get("width");
		shape = (String)attributes.get("shape");
		border = (Boolean)attributes.get("border");

		Graphics l_graphics = a_imageOut.getBufferedImage().getGraphics();

		performanceMeter.enableProgressBar("Mosaic", a_imageIn.getHeight()*width);

		if(shape.equals(SQUARES)){
			squaresMosaic(width, border, l_graphics, a_imageIn);
		}
		else if(shape.equals(TRIANGLES)){
			trianglesMosaic(width, border, l_graphics, a_imageIn);
		}

		a_imageOut.updateColorArray();
		performanceMeter.finish();
	}

	private void squaresMosaic(int a_width, boolean a_border, Graphics a_graphics, MarvinImage a_image){
		Color l_color;

		for (int y = 0; y < a_image.getHeight(); y+=a_width) {
			for (int x = 0; x < a_image.getWidth(); x+=a_width) {			
				l_color = getSquareColor(x,y,a_image);
				a_graphics.setColor(l_color);
				a_graphics.fillRect((int)(x), (int)(y), (int)((a_width)), (int)((a_width)));

				if(a_border){
					a_graphics.setColor(Color.black);
					a_graphics.drawRect((int)(x), (int)(y), (int)((a_width)), (int)((a_width)));
				}
			}
			performanceMeter.stepsFinished(a_image.getWidth());
		}
	}

	private void trianglesMosaic(int a_width, boolean a_border, Graphics a_graphics, MarvinImage a_image){
		Color l_colorT1;
		Color l_colorT2;
		int t=-1;
		boolean l_aux=true;

		if
		(
			((a_image.getWidth()/a_width)%2 == 0 && a_image.getWidth()%a_width==0) ||
			((a_image.getWidth()/a_width)%2 == 1 && a_image.getWidth()%a_width!=0)
		)
		{
			l_aux=false;
		}
		
		for (int y = 0; y < a_image.getHeight(); y+=a_width) {
			for (int x = 0; x < a_image.getWidth(); x+=a_width) {
				if(t ==-1)
				{
					l_colorT1 = getTriangleColor(x,y,0, a_image);
					l_colorT2 = getTriangleColor(x,y,1, a_image);

					a_graphics.setColor(l_colorT1);
					a_graphics.fillPolygon(new int[]{x,x+a_width,x}, new int[]{y,y,y+a_width},3);
					if(a_border){
						a_graphics.setColor(Color.black);
						a_graphics.drawPolygon(new int[]{x,x+a_width,x}, new int[]{y,y,y+a_width},3);
					}


					a_graphics.setColor(l_colorT2);
					a_graphics.fillPolygon(new int[]{x+a_width,x+a_width,x}, new int[]{y,y+a_width,y+a_width},3);
					if(a_border){
						a_graphics.setColor(Color.black);
						a_graphics.drawPolygon(new int[]{x+a_width,x+a_width,x}, new int[]{y,y+a_width,y+a_width},3);
					}
				}
				else{
					l_colorT1 = getTriangleColor(x,y,2, a_image);
					l_colorT2 = getTriangleColor(x,y,3, a_image);


					a_graphics.setColor(l_colorT1);
					a_graphics.fillPolygon(new int[]{x,x+a_width,x+a_width}, new int[]{y,y,y+a_width},3);
					if(a_border){
						a_graphics.setColor(Color.black);
						a_graphics.drawPolygon(new int[]{x,x+a_width,x+a_width}, new int[]{y,y,y+a_width},3);
					}	



					a_graphics.setColor(l_colorT2);
					a_graphics.fillPolygon(new int[]{x, x+a_width,x}, new int[]{y,y+a_width,y+a_width},3);
					if(a_border){
						a_graphics.setColor(Color.black);
						a_graphics.drawPolygon(new int[]{x, x+a_width,x}, new int[]{y,y+a_width,y+a_width},3);
					}
				}
				performanceMeter.stepsFinished(a_image.getWidth());	
				t*=-1;
			}
			if(l_aux){
				t*=-1;
			}
		}
	}

	private Color getSquareColor(int a_x, int a_y, MarvinImage image){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		for(int y=0; y<width; y++){
			for(int x=0; x<width; x++)
			{
				if(a_x+x > 0 && a_x+x < image.getWidth() &&  a_y+y> 0 && a_y+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(a_x+x,a_y+y);
						l_green = image.getIntComponent1(a_x+x,a_y+y);
						l_blue = image.getIntComponent2(a_x+x,a_y+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(a_x+x,a_y+y))/2;
						l_green = (l_green+image.getIntComponent1(a_x+x,a_y+y))/2;
						l_blue = (l_blue+image.getIntComponent2(a_x+x,a_y+y))/2;
					}
				}
			}
		}
		return new Color(l_red,l_green,l_blue);
	}

	private Color getTriangleColor(int a_x, int a_y, int a_tringlePos, MarvinImage image){
		int l_red=-1;
		int l_green=-1;
		int l_blue=-1;

		int l_xInitial=0;
		int l_xOffSet=0;
		int l_xOffSetInc=0;
		int l_xInitalInc=0;

		switch(a_tringlePos){
			case 0:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=-1;
				l_xInitalInc = 0;
				break;
			case 1:
				l_xInitial = width-1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = -1;
				break;
			case 2:
				l_xInitial=1;
				l_xOffSet = width;
				l_xOffSetInc=0;
				l_xInitalInc = 1;
				break;
			case 3:
				l_xInitial = 1;
				l_xOffSet = 1;
				l_xOffSetInc=1;
				l_xInitalInc = 0;
				break;
			
		}
		
		int x = l_xInitial;
		int y = 0;

		for(int w=0; w< width-1; w++){ 
			while(x < l_xOffSet){
				if(a_x+x > 0 && a_x+x < image.getWidth() &&  a_y+y> 0 && a_y+y < image.getHeight()){
					if(l_red == -1){
						l_red = image.getIntComponent0(a_x+x,a_y+y);
						l_green = image.getIntComponent1(a_x+x,a_y+y);
						l_blue = image.getIntComponent2(a_x+x,a_y+y);
					}
					else{
						l_red = (l_red+image.getIntComponent0(a_x+x,a_y+y))/2;
						l_green = (l_green+image.getIntComponent1(a_x+x,a_y+y))/2;
						l_blue = (l_blue+image.getIntComponent2(a_x+x,a_y+y))/2;
					}
				}
				x++;
			}
			l_xInitial+=l_xInitalInc;
			l_xOffSet+=l_xOffSetInc;
			x = l_xInitial;
			y++;
			
		}
		if(l_red == -1) l_red = 0;
		if(l_green == -1) l_green = 0;
		if(l_blue == -1) l_blue = 0;
		return new Color(l_red,l_green,l_blue);
	}
}