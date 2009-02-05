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

package marvin.statistic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Histogram chart.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinHistogram
{
	private String description;
	private double maxX;
	private double maxY;
	private int barWidth;

	private Hashtable <Integer, MarvinHistogramEntry> hashEntries;
	private int[] arrPaintedColumns;
	
	public MarvinHistogram(String a_description){
		description = a_description;
		hashEntries = new Hashtable<Integer, MarvinHistogramEntry>();
		maxX = 0;
		maxY = 0;
	}

	public void setBarWidth(int a_barWidth){
		barWidth = a_barWidth;
	}

	public int getBarWidth(){
		return barWidth;
	}

	public void addEntry(MarvinHistogramEntry a_entry){
		hashEntries.put(a_entry.hashCode(), a_entry);

		if(a_entry.getValueX() > maxX){
			maxX = a_entry.getValueX();
		}
		if(a_entry.getValueY() > maxY){
			maxY = a_entry.getValueY();
		}
	}

	public void draw(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		// Fill white rect
		a_graphics.setColor(Color.white);
		a_graphics.fillRect(a_px,a_py,a_width, a_height);
		// write the description
		a_graphics.setColor(Color.black);
		a_graphics.drawString(description, (int)(a_width*0.05), a_py+12);
		
		// draw Histo
		drawHisto(a_px+(int)(a_width*0.05), a_py+(int)(a_height*0.1), (int)(a_width*0.95), (int)(a_height*0.8), a_graphics);

		//drawLines
		a_graphics.setColor(Color.black);
		a_graphics.drawLine(a_px+(int)(a_width*0.05), a_py+(int)(a_height*0.1), a_px+(int)(a_width*0.05),(int)(a_height*0.9));
		a_graphics.drawLine(a_px+(int)(a_width*0.05), (int)(a_height*0.9), a_width,(int)(a_height*0.9));
	}

	private void drawHisto(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		MarvinHistogramEntry l_entry;

		arrPaintedColumns = new int[a_width+1];
		for (Enumeration<MarvinHistogramEntry> e = hashEntries.elements(); e.hasMoreElements();){
			l_entry = e.nextElement();
			drawEntry(a_px,a_py,a_width,a_height,l_entry,a_graphics);			
		}

		for(int i=0; i<a_width; i++){
			if(arrPaintedColumns[i] == 0){
				if(i > 0){
					l_entry = hashEntries.get(arrPaintedColumns[i-1]);
					redrawEntry(a_px,i,a_py,a_height, l_entry,a_graphics);
				}
			}
		}
	}

	private void drawEntry(int a_px, int a_py, int a_width, int a_height, MarvinHistogramEntry a_entry, Graphics a_graphics){
		int l_ePx;
		int l_eHeight;

		if(a_entry.getColor() != null){
			a_graphics.setColor(a_entry.getColor());
		}
		else{
			a_graphics.setColor(Color.black);
		}

		l_ePx = (int)(a_width*(a_entry.getValueX()/maxX));
		l_eHeight = (int)(a_height*(a_entry.getValueY()/maxY));
		a_graphics.fillRect(a_px+l_ePx, (a_py+a_height)-l_eHeight, barWidth, l_eHeight);
		arrPaintedColumns[l_ePx] = a_entry.hashCode();
	}

	// used to resize the histogram
	private void redrawEntry(int a_pxHisto, int a_pxEntry, int a_py, int a_height, MarvinHistogramEntry a_entry, Graphics a_graphics){
		int l_eHeight;

		if(a_entry.getColor() != null){
			a_graphics.setColor(a_entry.getColor());
		}
		else{
			a_graphics.setColor(Color.black);
		}

		l_eHeight = (int)(a_height*(a_entry.getValueY()/maxY));
		a_graphics.fillRect(a_pxHisto+a_pxEntry, (a_py+a_height)-l_eHeight, barWidth, l_eHeight);
		arrPaintedColumns[a_pxEntry] = a_entry.hashCode();
	}

	public BufferedImage getImage(int a_width, int a_height){
		BufferedImage l_buf;
		Graphics2D l_g2d;
		l_buf = new BufferedImage(a_width,a_height, BufferedImage.TYPE_INT_RGB);
		l_g2d = (Graphics2D) l_buf.getGraphics();
		l_g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		draw(0,0,a_width,a_height,l_g2d);
		return l_buf;
	}

};