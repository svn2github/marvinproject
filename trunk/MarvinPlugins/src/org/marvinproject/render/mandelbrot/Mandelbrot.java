/**
Marvin Project <2007-2010>

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
package org.marvinproject.render.mandelbrot;

import java.awt.Color;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Mandelbrot extends MarvinAbstractImagePlugin{

	double xCenter = -0.75;
	double yCenter = -0.1;
	int width;
	int height;
	double factor = 0.02;
	int iterations = 500;
	int cm = 0;
	
	public void load() {
	}

	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes out2,
		MarvinImageMask a_mask, 
		boolean previewMode
	)
	{
		if(imageOut.getWidth() > imageOut.getHeight()){
			height = imageOut.getHeight();
			width = height;
		}
		else{
			width = imageOut.getWidth();
			height = width;
		}
		
		double xc   = xCenter;
		double yc   = yCenter;

		int iter;

		double nx;
		double ny;
		double nx1;
		double ny1;

		for (int i=0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double x0 = xc - factor/2 + factor*i/width;
				double y0 = yc - factor/2 + factor*j/height;

				nx=x0;
				ny=y0;

				iter=0;
				while(Math.sqrt(nx*nx + ny*ny) < 2.0 && iter < iterations){
					nx1 = nx*nx-ny*ny;
					ny1 = nx*ny+ny*nx;

					nx = nx1+x0;
					ny = ny1+y0;						 
					iter++;
				}
				imageOut.setIntColor(i,height-1-j, getColor(iter, iterations, cm));
			}
		}
	}
	
	private int getColor(int iter, int max, int colorModel){
		if(colorModel == 0){
			return getColor0(iter, max);
		}
		return getColor1(iter, max);
	}
	
	 private int getColor0(int iter, int max){
		 double f = 0x00FFFFFF/max;
		 int i = (int)(iter*f);
		 int blue = (i&0xFF0000)>>16;
		 int green = (i&0x00FF00)>>8;
		 int red = (i&0x0000FF);
		 return blue + (green << 8) + (red << 16);
	 }
	 
	 private int getColor1(int iter, int max) {
			int red = (int) ((Math.cos(iter / 10.0f) + 1.0f) * 127.0f);
			int green = (int) ((Math.cos(iter / 20.0f) + 1.0f) * 127.0f);
	        int blue = (int) ((Math.cos(iter / 300.0f) + 1.0f) * 127.0f);
	        return blue + (green << 8) + (red << 16);
		}

	public void show() {
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Mandelbrot", 500,600, getImagePanel(), this);
		filterWindow.setVisible(true);
	}

}
