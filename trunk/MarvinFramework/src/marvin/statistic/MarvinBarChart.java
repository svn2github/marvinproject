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
import java.util.LinkedList;

/**
 * Bar Chart.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinBarChart
{
	// Constants
	public final static int ORIGINAL_BAR_COLOR = 0;
	public final static int SEQUENTIAL_BAR_COLOR = 1;

	private final static int BAR_REFERENCE_HEIGHT = 15;
	private final static int CHARACTER_WIDTH = 8;
	private final static int CHARACTER_HEIGHT = 15;


	private final static Color[] BAR_COLORS = {	Color.blue, Color.red, new Color(0,125,0), 
																Color.orange, Color.green, Color.pink, 
																Color.cyan, Color.yellow
															};

	private String description;
	private int barsColorType;
	private double maxValue;
	private double maxHeight;	

	private LinkedList<MarvinBarChartEntry>listEntries;

	public MarvinBarChart(String a_description){
		description = a_description;
		barsColorType = ORIGINAL_BAR_COLOR;
		maxValue = 0;
		maxHeight=0;
		listEntries = new LinkedList<MarvinBarChartEntry>();
	}

	public void addEntry(MarvinBarChartEntry a_entry){
		listEntries.add(a_entry);

		if(a_entry.getValue() > maxValue){
			maxValue = a_entry.getValue();
			maxHeight = a_entry.getValue()-CHARACTER_HEIGHT;			
		}
	}

	public void setBarsColor(int a_type){
		barsColorType = a_type;
	}

	private Color getBarColor(MarvinBarChartEntry a_entry, int a_barIndex){
		switch(barsColorType){
			case ORIGINAL_BAR_COLOR:
				return a_entry.getColor();
			case SEQUENTIAL_BAR_COLOR:
				return BAR_COLORS[a_barIndex%(BAR_COLORS.length)];
		}
		return null;
	}

	public void draw(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		int l_chartLeftDistance = ((""+maxValue).length()*CHARACTER_WIDTH)+5;

		// Fill white rect with the chart dimension
		a_graphics.setColor(Color.white);
		a_graphics.fillRect(a_px, a_py, a_width, a_height);
		// write the description
		a_graphics.setColor(Color.black);
		a_graphics.drawString(description, l_chartLeftDistance, 12);
		// draw chart
		drawChart(a_px+(int)(l_chartLeftDistance), a_py+(int)(a_height*0.1), (int)(a_width-l_chartLeftDistance), (int)(a_height*0.60), a_graphics);
		drawBarReference(a_px+(int)(l_chartLeftDistance), (int)(a_height*0.75), (int)(a_width-l_chartLeftDistance), (int)(a_height*0.25), a_graphics);
		drawIntervals(a_px+(int)(l_chartLeftDistance), a_py+(int)(a_height*0.1), (int)(a_width-l_chartLeftDistance), (int)(a_height*0.60), a_graphics);	
	}

	private void drawChart(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		Object[] l_arrEntries;
		MarvinBarChartEntry l_entry; 
		int l_px, l_py;
		int l_height;
		int l_numEntries;
		int barWidth;
		int barDistance;
		
		// draw chart Lines
		a_graphics.setColor(Color.black);
		a_graphics.drawLine(a_px, a_py+a_height,a_px+a_width, a_py+a_height);
		a_graphics.drawLine(a_px, a_py, a_px, a_py+a_height);

		l_arrEntries = listEntries.toArray();
		l_numEntries = listEntries.size();
		// Chart design Attributes		
		barWidth = (int) ((a_width*0.7)/l_numEntries);
		barDistance = (int) ((a_width*0.3)/(l_numEntries+1));

		for(int i=0; i<l_numEntries; i++){
			l_entry = (MarvinBarChartEntry)l_arrEntries[i];

			a_graphics.setColor(getBarColor(l_entry,i));

			l_height = (int)(a_height*(l_entry.getValue()/maxHeight));
			if(l_height == 0 && l_entry.getValue() > 0){
				l_height = 1;
			}

			l_px = a_px+(barDistance+((barDistance+barWidth)*i));
			l_py = a_py+(a_height-l_height);
			// render bar
			a_graphics.fillRect(l_px, l_py, barWidth, l_height);
			a_graphics.setColor(Color.black);
		}
	}

	private void drawBarReference(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		Object[] l_arrEntries;
		MarvinBarChartEntry l_entry;
		int l_numEntries;
		int l_px, l_py;
		int l_barReferenceWidth;
		int l_barReferenceStringLength;

		l_py = a_py;
		l_px = a_px;
		l_barReferenceWidth = a_width/3;
		l_barReferenceStringLength = (l_barReferenceWidth-12)/CHARACTER_WIDTH;
		l_arrEntries = listEntries.toArray();
		l_numEntries = listEntries.size();
		for(int i=0; i<l_numEntries; i++){
			l_entry = (MarvinBarChartEntry)l_arrEntries[i];

			l_px=a_px+(i*l_barReferenceWidth)%a_width;
			l_py=a_py+(((i*l_barReferenceWidth)/a_width)*BAR_REFERENCE_HEIGHT);
			
			a_graphics.setColor(getBarColor(l_entry,i));
			a_graphics.fillRect(l_px, l_py, 10,10);

			a_graphics.setColor(Color.black);

			if(l_entry.getName().length() > l_barReferenceStringLength){
				a_graphics.drawString(l_entry.getName().substring(0,l_barReferenceStringLength)+"." , l_px+12,l_py+10);
			}
			else{
				a_graphics.drawString(l_entry.getName(), l_px+12,l_py+10);
			}
		}
	}

	/*
		Draw from px to px-(width of the value as String)
	*/
	private void drawIntervals(int a_px, int a_py, int a_width, int a_height, Graphics a_graphics){
		int l_intervalMaxValue = (int)(maxValue/10)*10;
		double l_numIntervals = (a_height)/(CHARACTER_HEIGHT*2);
		double l_intervalHeight = (a_height/l_numIntervals);
		int l_intervalValue = (int)(l_intervalMaxValue/l_numIntervals);
		int l_value;
		int l_py;

		l_value = l_intervalMaxValue;
		a_graphics.setColor(Color.black);
		for(int i=0; i<l_numIntervals+1; i++){

			if(i == l_numIntervals){
				l_py = a_py+a_height;
				l_value = 0;
			}
			else{
				l_py = a_py+(int)(i*l_intervalHeight);
			}

			a_graphics.drawLine(a_px, l_py, a_px-5, l_py);
			a_graphics.drawString((""+l_value), a_px-5-((""+l_value).length()*CHARACTER_WIDTH), l_py);
			l_value = l_value - l_intervalValue;
		}
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
}