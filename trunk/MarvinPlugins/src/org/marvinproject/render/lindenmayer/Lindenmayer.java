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

import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;

public class Lindenmayer extends MarvinAbstractImagePlugin{

	private MarvinAttributes attributes;
	
	private Grammar 		grammar;
	private TurtleGraphics	turtle;
	
	private String RULES = 	"G->F[+FFG][G]-FG\n"+
							"F->FF\n";
	
	
	@Override
	public void load() {
		attributes = getAttributes();
		attributes.set("angle", 22.5);
		attributes.set("rules", "");
		attributes.set("initialText", "G");
		
		grammar = new Grammar();
		turtle = new TurtleGraphics();
	}

	@Override
	public void process
	(
		MarvinImage in, 
		MarvinImage out, 
		MarvinAttributes out2,
		MarvinImageMask a_mask, 
		boolean mode
	) {
		
		String rules[] = ((String)(attributes.get("rules"))).split("\n");
		
		for(int i=0; i<rules.length; i++){
			addRule(rules[i]);
		}
		
		turtle.setStartPosition(300, 300, 0);
	}

	@Override
	public void show() {
		MarvinFilterWindow filterWindow = new MarvinFilterWindow("Lindenmayer", 500,600, getImagePanel(), this);
		filterWindow.addTextArea("txtRules","rules", 8, 40, attributes);
		filterWindow.setVisible(true);
	}
	
	private void addRule(String rule){
		String r[] = rule.split("->");
		grammar.addRule(r[0], r[1]);
	}
	

}
