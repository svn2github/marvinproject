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

package org.marvinproject.render.iteratedFunctionSystem;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class IteratedFunctionSystem extends MarvinAbstractImagePlugin{

	private MarvinAttributes attributes;
	
	private List<Rule> rules;
	
	/* Testing String
	private final static String TESTING_STRING = 	"0,0,0,0.16,0,0,0.01\n"+
											"0.85,0.04,-0.04,0.85,0,1.6,0.85\n"+
											"0.2,-0.26,0.23,0.22,0,1.6,0.07\n"+
											"-0.15,0.28,0.26,0.24,0,0.44,0.07\n";
	
	0,0,0,0.16,0,0,0.01
	0.85,0.04,-0.04,0.85,0,1.6,0.85
	0.2,-0.26,0.23,0.22,0,1.6,0.07
	-0.15,0.28,0.26,0.24,0,0.44,0.07
	*/
	
	@Override
	public void load() {
		attributes = getAttributes();
		attributes.set("rules", "");
		
		rules = new ArrayList<Rule>();
	}

	@Override
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut, 
		MarvinAttributes out2,
		MarvinImageMask a_mask, 
		boolean previewMode
	){
		loadRules();
		
		double x0 = 0;
		double y0 = 0;
		int x,y;
		int startX = 300;
		int startY = 300;
		double factor = 50;
		double iterations = 100000;
		
		double minX=999999999,minY=999999999,maxX=-999999999,maxY=-99999999;
		
		Rule tempRule;
		double point[] = {x0,y0};
		
		imageOut.fillRect(0, 0, imageOut.getWidth(), imageOut.getHeight(), Color.white);
		
		for(int i=0; i<iterations; i++){
			tempRule = getRule();
			applyRule(point, tempRule);
			
			//g.drawLine((int)(point[0]*factor)+startX, -(int)(point[1]*factor)+startY, (int)(point[0]*factor)+startX, -(int)(point[1]*factor)+startY);
			x = (int)(point[0]);
			y = (int)(point[1]);
			
			if(x < minX){	minX = x;	};
			if(x > maxX){	maxX = x;	};
			if(y < minY){	minY = y;	};
			if(y > maxY){	maxY = y;	};
		
		}	
		
		if(Math.abs(minX-maxX) > Math.abs(minY-maxY)){
			factor = imageOut.getWidth()/Math.abs(minX-maxX);
		}
		else{
			factor = imageOut.getHeight()/Math.abs(minY-maxY);
		}
		
		System.out.println("deltaX:"+Math.abs(minX-maxX));
		System.out.println("deltaY:"+Math.abs(minY-maxY));
		System.out.println("factor:"+factor);
		
		
		for(int i=0; i<iterations; i++){
			tempRule = getRule();
			applyRule(point, tempRule);
			
			//g.drawLine((int)(point[0]*factor)+startX, -(int)(point[1]*factor)+startY, (int)(point[0]*factor)+startX, -(int)(point[1]*factor)+startY);
			x = (int)(point[0]*factor)+startX;
			y = startY-(int)(point[1]*factor);
			
			
			if(x >= 0 && x<imageOut.getWidth() && y >= 0 && y < imageOut.getHeight()){
				imageOut.setIntColor(x,y , 0);
			}
		}	
	}
	

	@Override
	public void show() {
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Tile Texture", 500,600, getImagePanel(), this);
		filterWindow.addLabel("lblRules","Rules:");
		filterWindow.addPanelBelow();
		filterWindow.addTextArea("txtRules","rules", 8, 40, attributes);
		filterWindow.setVisible(true);
	}
	
	private void loadRules(){
		String r[] = ((String)(attributes.get("rules"))).split("\n");
		
		for(int i=0; i<r.length; i++){
			addRule(r[i]);
		}
	}

	private void addRule(String rule){
		rule = rule.replace(" ", "");
		String attr[] = rule.split(",");
		Rule r = new Rule
		(
			Double.parseDouble(attr[0]),
			Double.parseDouble(attr[1]),
			Double.parseDouble(attr[2]),
			Double.parseDouble(attr[3]),
			Double.parseDouble(attr[4]),
			Double.parseDouble(attr[5]),
			Double.parseDouble(attr[6])
		);
		
		rules.add(r);
	}
	
	private Rule getRule(){
		double random = Math.random();
		double sum=0;
		int i;
		for(i=0; i<rules.size(); i++){
			sum += rules.get(i).probability;
			if(random < sum){
				return rules.get(i);
			}
		}
		return rules.get(i-1);
	}
	
	private void applyRule(double point[], Rule rule){
		double nx = rule.a*point[0] + rule.b*point[1]+rule.e;
		double ny = rule.c*point[0] + rule.d*point[1]+rule.f;
		point[0] = nx;
		point[1] = ny;
	}
}

class Rule {
	
	public double	a,
					b,
					c,
					d,
					e,
					f,
					probability;
	
	public Rule
	(
		double a,
		double b,
		double c,
		double d,
		double e,
		double f,
		double probability
	)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.f = f;
		this.probability = probability;
	}
}
