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

package org.marvinproject.render.lindenmayer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Stack;

import marvin.image.MarvinImage;

public class TurtleGraphics {

	private double walkDistance;
	private double rotationAngle;
	private int startPx;
	private int startPy;
	private double startAngle;
	
	// Current Turtle State
	private double currentPx;
	private double currentPy;
	private double currentAngle;
	
	// State Stack
	private Stack<Double[]> stateStack;
	
	public TurtleGraphics(){
		stateStack = new Stack<Double[]>();		
	}
	
	private void reset(){
		currentPx = 0;
		currentPy = 0;
		currentAngle = startAngle;
		stateStack.clear();
	}
	
	public void setStartPosition(int x, int y, double angle){
		startPx = x;
		startPy = y;
		startAngle = angle;
	}
	
	public void setWalkDistance(double distance){
		walkDistance = distance;
	}
	
	public void setRotationAngle(double angle){
		rotationAngle = angle;
	}
	
	public void render(String text, Grammar grammar, int iterations, MarvinImage image){
		
		reset();
		
		for(int i=0; i<iterations; i++){	
			text = grammar.derive(text);						
		}
		walk(text, image);
	}
	
	Polygon p = new Polygon();
	boolean b = false;
	
	private void walk(String text, MarvinImage image){
		double newPx;
		double newPy;
		
		for(int i=0; i<text.length(); i++){
			switch(text.charAt(i)){
				case 'F':
				case 'G':
				case 'H':
				case 'I':
				case 'J':
				case 'K':
				case 'L':
				case 'M':
				{
					newPx = currentPx+Math.cos(Math.toRadians(currentAngle))*walkDistance;
					newPy = currentPy-Math.sin(Math.toRadians(currentAngle))*walkDistance;
					
					if(!b){
						//g.drawLine((int)currentPx+startPx, (int)currentPy+startPy, (int)newPx+startPx, (int)newPy+startPy);
						drawLine((int)currentPx+startPx, (int)currentPy+startPy, (int)newPx+startPx, (int)newPy+startPy, image);
					}
					else{
						p.addPoint(((int)newPx+startPx), (int)(newPy+startPy));
						
					}
					
					currentPx = newPx;
					currentPy = newPy;
					break;
				}
				case 'g':
				case 'h':
				case 'j':
				case 'k':
				case 'l':
				{
					// do nothing
				}					
				case '+':
				{
					currentAngle -= rotationAngle;
					if(currentAngle < 0){
						currentAngle = 360+currentAngle;
					}
					break;
				}	
				case '-':
				{
					currentAngle = (currentAngle + rotationAngle)%360;
					break;
				}
				case '[':
				{
					stateStack.push(new Double[]{currentPx, currentPy, currentAngle});
					break;
				}
				case ']':
				{
					Double o[] = stateStack.pop();
					currentPx = o[0];
					currentPy = o[1];
					currentAngle = o[2];
					break;
				}
				case '{':
					p = new Polygon();
					b = true;
					break;
				case '}':
					//g.fillPolygon(p);
					b = false;
					//g.setColor(Color.black);
					//System.out.println("black");
					break;
				case '#':
					if(text.charAt(i+1) == '0'){
						//g.setColor(Color.black);
					}
					else if(text.charAt(i+1) == '1'){
						//g.setColor(new Color(0,100,0));
					}
					else if(text.charAt(i+1) == '2'){
						//g.setColor(Color.red);
					}
					else if(text.charAt(i+1) == '3'){
						//g.setColor(Color.yellow);
					}
					else if(text.charAt(i+1) == '4'){
						//g.setColor(Color.blue);
					}
					i++;
					break;
				case '&':
					//g.setColor(new Color((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255)));
					i++;
					break;
			}
		}
	}
	
	private void drawLine(int x0, int y0, int x1, int y1, MarvinImage image){
		boolean steep = (Math.abs(y1 - y0) > Math.abs(x1 - x0));
		int temp;
		if(steep){
			temp = x0;
			x0 = y0;
			y0 = temp;

			temp = x1;
			x1 = y1;
			y1 = temp;
		}
		if(x0 > x1){
			temp = x0;
			x0 = x1;
			x1 = temp;

			temp = y0;
			y0 = y1;
			y1 = temp;
		}

		int deltax = x1 - x0;
		int deltay = Math.abs(y1 - y0);
		int error = deltax / 2;
		int ystep;
		int y = y0;

		if(y0 < y1){
			ystep = 1;
		}
		else{
			ystep = -1;
		}

		for(int x=x0; x<=x1; x++){
         if(steep){
				//g.drawLine(y,x,y,x);
        	 	image.setIntColor(y,x,0,0,0);
			}
			else{
				//g.drawLine(x,y,x,y);
				image.setIntColor(x,y,0,0,0);
			}

         error = error - deltay;
         if (error < 0){
             y = y + ystep;
             error = error + deltax;
			}
		}
	}
}

