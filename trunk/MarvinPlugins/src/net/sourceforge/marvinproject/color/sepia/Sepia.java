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

package net.sourceforge.marvinproject.color.sepia;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinFilterWindow;
import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.performance.MarvinPerformanceMeter;
import marvin.plugin.MarvinAbstractPluginImage;
import marvin.util.MarvinAttributes;
/**
 * Sepia plug-in. Reference: {@link http://forum.java.sun.com/thread.jspa?threadID=728795&messageID=4195478}
 * @author Hugo Henrique Slepicka
 * @version 1.0.2 04/10/2008
 */
public class Sepia extends MarvinAbstractPluginImage implements ChangeListener, KeyListener{

	private MarvinAttributes attributes;
	private MarvinPerformanceMeter performanceMeter;
	private MarvinPluginWindow Tela;
	
	@Override
	public void load() {
		attributes = getAttributes();
		attributes.set("txtValue", "20");
		attributes.set("hsIntensidade", 20);
		//@old versionperformanceMeter = getApplication().getPerformanceMeter();
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
		int r, g, b, depth, corfinal;
		
		//Define a intensidade do filtro...
		depth = Integer.parseInt(attributes.get("hsIntensidade").toString());
		
		int width    = a_imageIn.getWidth();
		int height   = a_imageIn.getHeight();
		
		//@old versionperformanceMeter.enableProgressBar("Filtro de Teste", ((height-2)*(width-2)));
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for (int x = 0; x < a_imageIn.getWidth(); x++) {
			for (int y = 0; y < a_imageIn.getHeight(); y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				//Captura o RGB do ponto...
				r = a_imageIn.getRed(x, y);
				g = a_imageIn.getGreen(x, y);
				b = a_imageIn.getBlue(x, y);
				
				//Define a cor como a média aritmética do pixel...
				corfinal = (r + g + b) / 3;
				r = g = b = corfinal;
				 
				r = truncate(r + (depth * 2));
				g = truncate(g + depth);
			
				//Define a nova cor do ponto...
				a_imageOut.setRGB(x, y, r, g, b);
			}
			//@old versionperformanceMeter.incProgressBar(width-2);
		}
		//@old versionperformanceMeter.finish();
	}

	/**
	 * Sets the RGB between 0 and 255
	 * @param a
	 * @return
	 */
	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	
	public void show() { 
		MarvinFilterWindow l_filterWindow = new MarvinFilterWindow("Sepia", 400,350, getImagePanel(), this);
		l_filterWindow.addLabel("lblIntensidade", "Intensidade do Filtro");
		l_filterWindow.addHorizontalSlider("hsIntensidade", "hsIntensidade", 0, 100, 20, attributes);
		l_filterWindow.addPanelBelow();
		l_filterWindow.addTextField("txtValue", "txtValue",attributes);
		
		Tela = l_filterWindow;
			
		JTextField txtValue = (JTextField)(l_filterWindow.getComponent("txtValue").getComponent());
		JSlider slider = (JSlider)(l_filterWindow.getComponent("hsIntensidade").getComponent());
		
		slider.addChangeListener(this);
		txtValue.addKeyListener(this);
		
		l_filterWindow.setVisible(true);
	}
	
	//Manipula as alterações da Horizontal Bar
	//Handles the Horizontal Bar changes
	public void stateChanged(ChangeEvent e) {
		JSlider barra = (JSlider) (e.getSource());
		JTextField lbl = (JTextField)(Tela.getComponent("txtValue").getComponent());
		lbl.setText(""+barra.getValue());
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		JTextField txtValor = (JTextField) (Tela.getComponent("txtValue").getComponent());
		JSlider barra = (JSlider) (Tela.getComponent("hsIntensidade").getComponent());
		try {
			barra.setValue(Integer.parseInt(txtValor.getText().trim()));	
		} catch (Exception exception) {
			barra.setValue(0);
			txtValor.setText("0");
		}
			
	}

	public void keyTyped(KeyEvent e) {
	
	}
	
}
