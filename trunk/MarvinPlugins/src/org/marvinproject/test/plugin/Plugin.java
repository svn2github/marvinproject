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

package org.marvinproject.test.plugin;

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
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
/**
 * Sepia plug-in. Reference: {@link http://forum.java.sun.com/thread.jspa?threadID=728795&messageID=4195478}
 * @author Hugo Henrique Slepicka
 * @version 1.0.2 04/10/2008
 */
public class Plugin extends MarvinAbstractImagePlugin implements ChangeListener, KeyListener{

	private MarvinAttributes attributes;
	private MarvinPluginWindow tela;
		
	public void load() {
		attributes = getAttributes();
		attributes.set("hsR", 0);
		attributes.set("hsG", 0);
		attributes.set("hsB", 0);
		attributes.set("txtR", "0");
		attributes.set("txtG", "0");
		attributes.set("txtB", "0");
	}

	public int truncate(int a) {
		if      (a <   0) return 0;
		else if (a > 255) return 255;
		else              return a;
	}
	public int positive(int a) {
		if      (a <   0) return -a;
		else              return a;
	}
	
	
	public void process(
		MarvinImage a_imageIn, 
		MarvinImage a_imageOut,
		MarvinAttributes a_attributesOut,
		MarvinImageMask a_mask, 
		boolean a_previewMode
	)
	{
		//RGB rgb = new RGB();
		RGB rgbImagem = new RGB();
	
		int l_red = (Integer)attributes.get("hsR");
		int l_green = (Integer)attributes.get("hsG");
		int l_blue = (Integer)attributes.get("hsB");
		
		int width  = a_imageIn.getWidth();
		int height = a_imageIn.getHeight();
		
		boolean[][] l_arrMask = a_mask.getMaskArray();
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if(l_arrMask != null && !l_arrMask[x][y]){
					continue;
				}
				
				rgbImagem.setR(a_imageIn.getIntComponent0(x, y));
				rgbImagem.setG(a_imageIn.getIntComponent1(x, y));
				rgbImagem.setB(a_imageIn.getIntComponent2(x, y));
				
				rgbImagem.setR((int)(rgbImagem.getR()*(255.0/(255.0-l_red))));
				rgbImagem.setG((int)(rgbImagem.getG()*(255.0/(255.0-l_green))));
				rgbImagem.setB((int)(rgbImagem.getB()*(255.0/(255.0-l_blue))));
				
				a_imageOut.setIntColor(x, y, rgbImagem.getR(),rgbImagem.getG(),rgbImagem.getB());
			}
		}
	}

	public void show() { 
		MarvinFilterWindow l_filterWindow = 
		new MarvinFilterWindow("Plugin", 400,350, getImagePanel(), this);
		
		l_filterWindow.addLabel("lblC", "Cyan");
		l_filterWindow.addHorizontalSlider("hsR", "hsR", -100, 100, 0, attributes);
		l_filterWindow.addLabel("lblR", "Red");
		l_filterWindow.addTextField("txtR", "txtR",attributes);
		
		l_filterWindow.newComponentRow();
		l_filterWindow.addLabel("lblM", "Magenta");
		l_filterWindow.addHorizontalSlider("hsG", "hsG", -100, 100, 0, attributes);
		l_filterWindow.addLabel("lblG", "Green");
		l_filterWindow.addTextField("txtG", "txtG",attributes);
		
		l_filterWindow.newComponentRow();
		l_filterWindow.addLabel("lblY", "Yellow");
		l_filterWindow.addHorizontalSlider("hsB", "hsB", -100, 100, 0, attributes);
		l_filterWindow.addLabel("lblB", "Blue");
		l_filterWindow.addTextField("txtB", "txtB",attributes);
		
		l_filterWindow.newComponentRow();
		
		this.tela = l_filterWindow;
			
		JSlider sliderR = (JSlider)(l_filterWindow.getComponent("hsR").getComponent());
		JSlider sliderG = (JSlider)(l_filterWindow.getComponent("hsG").getComponent());
		JSlider sliderB = (JSlider)(l_filterWindow.getComponent("hsB").getComponent());
		
		JTextField txtR = (JTextField)(l_filterWindow.getComponent("txtR").getComponent());
		JTextField txtG = (JTextField)(l_filterWindow.getComponent("txtG").getComponent());
		JTextField txtB = (JTextField)(l_filterWindow.getComponent("txtB").getComponent());
		
		sliderR.addChangeListener(this);
		sliderG.addChangeListener(this);
		sliderB.addChangeListener(this);
		txtR.addKeyListener(this);
		txtG.addKeyListener(this);
		txtB.addKeyListener(this);
		
		l_filterWindow.setVisible(true);
	}
	
	//Manipula as alterações da Horizontal Bar
	//Handles the Horizontal Bar changes
	public void stateChanged(ChangeEvent e) {
		JSlider barra1 = (JSlider) (tela.getComponent("hsR").getComponent());
		JSlider barra2 = (JSlider) (tela.getComponent("hsG").getComponent());
		JSlider barra3 = (JSlider) (tela.getComponent("hsB").getComponent());
		JSlider barra = (JSlider) (e.getSource());
		JTextField lbl = null;
		
		if(e.getSource()==barra1){
				lbl = (JTextField)(tela.getComponent("txtR").getComponent());
			}
			else{ 
				if(e.getSource()==barra2){
					lbl = (JTextField)(tela.getComponent("txtG").getComponent());
				}
				else {
					if(e.getSource()==barra3){
					}
					lbl = (JTextField)(tela.getComponent("txtB").getComponent());
					}
			}
		lbl.setText(""+barra.getValue());
	}

	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent e) {
		JTextField txtR = (JTextField) (tela.getComponent("txtR").getComponent());
		JTextField txtG = (JTextField) (tela.getComponent("txtG").getComponent());
		JTextField txtB = (JTextField) (tela.getComponent("txtB").getComponent());
		
		JSlider barra1 = (JSlider) (tela.getComponent("hsR").getComponent());
		JSlider barra2 = (JSlider) (tela.getComponent("hsG").getComponent());
		JSlider barra3 = (JSlider) (tela.getComponent("hsB").getComponent());
		
		try {
			if(e.getSource()==txtR){
			barra1.setValue(Integer.parseInt(txtR.getText().toString()));
			}
			else{
				if(e.getSource()==txtG){
					barra2.setValue(Integer.parseInt(txtG.getText().toString()));
				}
				else
					if(e.getSource()==txtB){
						barra3.setValue(Integer.parseInt(txtB.getText().toString()));
					}
			}
			} catch (Exception exception) {
				if(e.getSource()==txtR){
					barra1.setValue(0);	
					txtR.setText("0");
					}
				else {
						if(e.getSource()==txtG){
							barra2.setValue(0);
							txtG.setText("0");
						}
						else{ 
								if(e.getSource()==txtB){
									barra3.setValue(0);
									txtB.setText("0");
								}
							}
					}
				}
	}

	public void keyTyped(KeyEvent e) {
	
	}

	private class RGB{
		private int r;
		private int g;
		private int  b;
		public RGB(){
			this.r=0;
			this.g=0;
			this.b=0;
		}
		public RGB(int r, int g, int b){
			this.r=r;
			this.g=g;
			this.b=b;
		}
		public int getR() {
			return r;
		}
		public void setR(int r) {
			this.r = truncate(r);
		}
		public int getG() {
			return g;
		}
		public void setG(int g) {
			this.g = truncate(g);
		}
		public int getB() {
			return b;
		}
		public void setB(int b) {
			this.b = truncate(b);
		}
	}
}
