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

package org.marvinproject.steganography;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import marvin.gui.MarvinPluginWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinFileChooser;

/**
 * Steganography plug-in
 * @author Hugo Henrique Slepicka
 */
public class Steganography extends MarvinAbstractImagePlugin implements ActionListener{
	
	private enum Action{
		MODE_READ, MODE_WRITE
	};

	private enum Type{
		TYPE_TEXT, TYPE_FILE
	};
	
	private final int numArgs = 3;

	private final int RED   = 0;
	private final int GREEN = 1;
	private final int BLUE  = 2;

	
	private int currentColor = RED;
	private int currentPixel = 0;
	
	private MarvinAttributes attributes;	
	private MarvinPluginWindow pluginWindow;
	private JButton buttonOK;
	private byte[] put;
	private byte[] get;
	
	private Type tipo;
	private long pixels;
	private String ext;
	private int xH, yH = 0;
	
	public void load() {
		attributes = new MarvinAttributes();
		attributes.set("txtLines", "");
		attributes.set("cbSelecao", "Gravar na Imagem");
		attributes.set("cbTipo", "Gravar Arquivo");
	}
		
	public Action getAction(){
		String s = attributes.get("cbSelecao").toString();
		if(s.equals("Gravar na Imagem")){
			return (Action.MODE_WRITE);
		}else{
			return (Action.MODE_READ);
		}
	}
	
	public Type getType(){
		String s = attributes.get("cbTipo").toString();
		if(s.equals("Gravar Arquivo")){
			return (Type.TYPE_FILE);
		}else{
			return (Type.TYPE_TEXT);
		}
	}
		
	public void getHEADER(MarvinImage a_image){
		//Cabe�alho do arquivo no seguinte formato:
		//Tipo_do_armazenamento/Extensao/Qtde_Pixels
		//F/docx/12345
		int cont = 0;
		String binpar = "";
		String pixaux = "";
		ext = "";
		tipo = null;
		pixels = 0L;
		
		currentColor = RED;
		currentPixel = 0;
		
		while(cont != numArgs){
			binpar += readBit(a_image);	
			if(binpar.length()==8){				
				//Se for igual a / entao incrementa o CONT
				if(binpar.equals("00101111")){
					cont++;
				}else{		

					switch(cont){
					//File Type
					case 0:
						if(binpar.equals("01010100")){
							tipo = Type.TYPE_TEXT;
						}else{
							tipo = Type.TYPE_FILE;
						}
						break;
						//File Extension
					case 1:
						ext += (char) Integer.parseInt(binpar,2); 
						break;
						//Pixels
					case 2:
						pixaux += (char) Integer.parseInt(binpar,2);
						break;						
					}
				}	
				binpar = "";
			}
		}
		xH = currentPixel/a_image.getHeight();
		yH = currentPixel%a_image.getHeight();		
		pixels = Long.valueOf(pixaux);			
	}
	
	public String setHEADER(File fileOrigin){
		
		//Cabe�alho do arquivo no seguinte formato:
		//Tipo_do_armazenamento/Extensao/Qtde_Pixels
		//F/docx/12345
		String rec = "";
		switch(getType()){
		  case TYPE_FILE:
			  rec += "F";
		  break;
		  case TYPE_TEXT:
			  rec += "T";
		  break;				 
		}
		
		rec += "/";
		rec += fileOrigin.getName().substring(fileOrigin.getName().lastIndexOf('.')+1);
		rec += "/";
		rec += fileOrigin.length()*8;
		rec += "/";
		
		return rec;
	}

	public String setHEADER(String Message){
		
		//Cabe�alho do arquivo no seguinte formato:
		//Tipo_do_armazenamento/Extensao/Qtde_Pixels/
		//F/docx/12345/
		String rec = "";
		switch(getType()){
		  case TYPE_FILE:
			  rec += "F";
		  break;
		  case TYPE_TEXT:
			  rec += "T";
		  break;				 
		}
		
		rec += "/";
		rec += "txt";
		rec += "/";
		rec += Message.length()*8;
		rec += "/";
		
		return rec;
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
		int l_result = 0;
		String name = "";		
		
		//Get the action - Write File/Read File
		switch(getAction()){
			case MODE_WRITE:
				//Get the Type of record to store in image...
				switch(getType()){
					case TYPE_FILE:
						try{
							JOptionPane.showMessageDialog(null, "Selecione um arquivo para armazenar na Imagem!");
							JFileChooser jfc = new JFileChooser("./img");
							jfc.setAcceptAllFileFilterUsed(true);
							l_result = jfc.showOpenDialog(pluginWindow);					
		
							if(l_result != JFileChooser.CANCEL_OPTION){
								name = jfc.getSelectedFile().getCanonicalPath();
							}
						} catch(IOException ex){
							ex.printStackTrace();
						}
						if((l_result != JFileChooser.CANCEL_OPTION)&&(name != null)&&(name.length() > 0)){
							File arq = new File(name);
							PrepareFile(arq);					
						}else{
							JOptionPane.showMessageDialog(null, "Nome de Arquivo inv�lido para armazenar na Imagem!");
							return ;
						}
						break;
					case TYPE_TEXT:
						PrepareFile(((JTextArea) pluginWindow.getComponent("txtLines").getComponent()).getText());
						break;
					}				
				if(Gravar_Imagem(a_imageIn, put))
					JOptionPane.showMessageDialog(null, "Imagem modificada com Sucesso!");
				break;	
			case MODE_READ:				
				if(isPng(a_imageIn)){					
					switch(StorageType(a_imageIn)){
						case TYPE_TEXT:
							JOptionPane.showMessageDialog(null, "TEM TEXTO");
							ReadText(a_imageIn);
							break;
						case TYPE_FILE:
							JOptionPane.showMessageDialog(null, "TEM ARQUIVO");
							ReadFile(a_imageIn);
							break;
						default:
							JOptionPane.showMessageDialog(null, "Tipo de armazenamento n�o reconhecido.");
					}
				}else{
					JOptionPane.showMessageDialog(null, "A imagem deve ser do tipo PNG para ler.");
					return ;
				}				
				break;
		}
	}
	
	public void show() {
		pluginWindow = new MarvinPluginWindow("Steganografia",500,200);
		
		//Create the objects to receive the text that will be masked on the image...				
		pluginWindow.newComponentRow();
		pluginWindow.addComboBox("cbSelecao", "cbSelecao", new String[] {"Ler Imagem","Gravar na Imagem"}, attributes);
		pluginWindow.newComponentRow();
		pluginWindow.addComboBox("cbTipo", "cbTipo", new String[] {"Gravar Arquivo","Gravar Texto"}, attributes);
		pluginWindow.newComponentRow();
		pluginWindow.addLabel("lblTexto", "Digite abaixo o Texto para Mascarar na Imagem:");
		pluginWindow.newComponentRow();
		pluginWindow.addTextArea("txtLines", "txtLines", 2, 40, attributes);
		
		pluginWindow.newComponentRow();
		
		buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
		pluginWindow.getCurrentPanel().add(buttonOK);
		
		JComboBox cbSel = (JComboBox) pluginWindow.getComponent("cbSelecao").getComponent();	
		cbSel.addActionListener(this);
		
		JComboBox cbTipo = (JComboBox) pluginWindow.getComponent("cbTipo").getComponent();
		cbTipo.setVisible(false);
		pluginWindow.setVisible(true);
	}
		
	public boolean Gravar_Imagem(MarvinImage a_image, byte[] Put){
		
//		if((Put.length*8) > (a_image.getWidth()*a_image.getHeight()*3)){
//			JOptionPane.showMessageDialog(null, "Espa�o insulficiente na imagem para armazenar informa��es solicitadas.","Marvin", JOptionPane.ERROR_MESSAGE);
//			return false;
//		}else{
			for (int l_pos = 0; l_pos < put.length; l_pos++){				
				for (int l_bit = 0; l_bit < 8; l_bit++){
					storeBit(a_image, put[l_pos], 7-l_bit);				
				}					
			}				
//		}		
		return true;				
	}
	
	private Type StorageType(MarvinImage a_image){
		getHEADER(a_image);
		return tipo;
	}
	
	private void ReadText(MarvinImage a_image){
		get = new byte[(int) (pixels/8)];
		String msgbin = "";
		String msg = "";
		int cont = 0;
		
		for(int i = 0; i < pixels; i++){
			msgbin += readBit(a_image);
			if(msgbin.length() == 8){
				get[cont] = (byte) Integer.parseInt(msgbin,2);
				msgbin = "";
				cont++;
			}
		}
		
		for(int i = 0; i < get.length; i++){
			msg += (char) get[i];
		}
		
		((JTextArea) pluginWindow.getComponent("txtLines").getComponent()).setText(msg);
	}
	
	private void ReadFile(MarvinImage a_image){
		get = new byte[(int) (pixels/8)];
		String msgbin = "";
		int cont = 0;
		
		for(int i = 0; i < pixels; i++){
			msgbin += readBit(a_image);
			if(msgbin.length() == 8){
				get[cont] = (byte) Integer.parseInt(msgbin,2);
				msgbin = "";
				cont++;
			}
		}
		
		try {			
			String name = "";
			JOptionPane.showMessageDialog(null, "Selecione um local para salvar o arquivo extra�do da imagem.");
			FileNameExtensionFilter[] vExt = new FileNameExtensionFilter[]{new FileNameExtensionFilter("File - *."+ext,ext)};
			name = MarvinFileChooser.select(pluginWindow,false,MarvinFileChooser.SAVE_DIALOG,vExt);			

			if((name != null)&&(name.length() > 0)){
				File fileOutput = new File(name);

				FileOutputStream out = new FileOutputStream(fileOutput);
				BufferedOutputStream on = new BufferedOutputStream(out);

				for(int i = 0; i < get.length; i++){
					on.write((int) get[i]);
				}
				on.close();
			}else{
				JOptionPane.showMessageDialog(null, "Nome de arquivo inv�lido para Salvar.");
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
	}
	
	private void PrepareFile(String Message){

		String header = "";
		int cont = 0;
		
		header = setHEADER(Message);
						
		put = new byte[header.length()+Message.length()];
		
		for (int j = 0; j < header.length(); j++){
			put[cont] = (byte) header.charAt(j);
			cont++;
		}
			
		//Armazena os binarios de 3 em 3 bits em um array...
		for (int j = 0; j < Message.length(); j++){
			put[cont] = (byte) Message.charAt(j);
			cont++;			
		}				
	}
	
	private void PrepareFile(File FileName){
		String header = "";
		int cont = 0;
		
		try{
			File fileOrigin = FileName;

			
			FileInputStream i = new FileInputStream(fileOrigin);
			BufferedInputStream in = new BufferedInputStream(i);			
			byte[] arq = new byte[(int) fileOrigin.length()];	
			
			
			in.read(arq);

			header = setHEADER(FileName);
		
			put = new byte[header.length() + arq.length];
		
			for (int j = 0; j < header.length(); j++){
				put[cont] = (byte) header.charAt(j);
				cont++;
			}
					
			for(int r=0;r<arq.length;r++){
				put[cont] = arq[r];
				cont++;
			}									
			
			in.close();						
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == buttonOK){
			pluginWindow.applyValues();
			process(getImagePanel().getImage(), null, null, MarvinImageMask.NULL_MASK, false);
		}
		else{
			JComboBox cbSel = (JComboBox) pluginWindow.getComponent("cbSelecao").getComponent();
			JComboBox cbTipo = (JComboBox) pluginWindow.getComponent("cbTipo").getComponent();
	
			cbTipo.setVisible(cbSel.getSelectedItem() == "Gravar na Imagem");
		}
	}

	private boolean isPng(MarvinImage a_image){
		String ext = a_image.getFormatName();		
        if((ext != null)&&(ext.equals("png"))){
			return true;
		}else{
			return false;
		}
	}

	private int getBit(int dado, int bit){			
		  return ((dado & (0x1 << bit)) >> bit);
	}
	
	private void storeBit(MarvinImage image, byte dado, int bit){
		int currX, currY;
		int vbit;
		int r,g,b;

		currX = currentPixel/image.getHeight();
		currY = currentPixel%image.getHeight();
		 		
		vbit = getBit(dado, bit);

		r = image.getIntComponent0(currX, currY);
		g = image.getIntComponent1(currX, currY);
		b = image.getIntComponent2(currX, currY);		
		
		switch(currentColor){
		    case RED:
		       if(vbit == 0){
					//Se g for �mpar...
					if(r % 2 !=0){
						//Se g+1 for menor que 255...
						if(r + 1 <255){ r += 1; }else{ r -= 1; }
					}
					//Se o bit for 1...
				}else{
					//Se r for par...
					if(r % 2 ==0){
						//Se r+1 for menor que 255...
						if(r + 1 < 255){ r += 1; }else{ r -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);
		       currentColor++;
		       break;
		   
		   case GREEN:
		       if(vbit == 0){
					//Se g for �mpar...
					if(g % 2 !=0){
						//Se g+1 for menor que 255...
						if(g + 1 <255){ g += 1; }else{ g -= 1; }
					}
					//Se o bit for 1...
				}else{
					//Se r for par...
					if(g % 2 ==0){
						//Se r+1 for menor que 255...
						if(g + 1 < 255){ g += 1; }else{ g -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);

		       currentColor++;
		       break;
		   
		   case BLUE:
		       if(vbit == 0){
					//Se g for �mpar...
					if(b % 2 !=0){
						//Se g+1 for menor que 255...
						if(b + 1 <255){ b += 1; }else{ b -= 1; }
					}
					//Se o bit for 1...
				}else{
					//Se r for par...
					if(b % 2 ==0){
						//Se r+1 for menor que 255...
						if(b + 1 < 255){ b += 1; }else{ b -= 1; }
					}
			   }
		       		       
		       image.setIntColor(currX, currY, r, g, b);
		       currentColor = RED;
		       currentPixel++;
		       break;
		  
		 }
	}
	
	private char readBit(MarvinImage image){
		int currX, currY;
		char result = '0';
		int r,g,b;
		

		currX = currentPixel/image.getHeight();
		currY = currentPixel%image.getHeight();		 				

		r = image.getIntComponent0(currX, currY);
		g = image.getIntComponent1(currX, currY);
		b = image.getIntComponent2(currX, currY);		
		
		switch(currentColor){
		    case RED:
				if(r%2==0)
				    result = '0';
				else
					result = '1';				
		       currentColor++;
		       break;
		   
		   case GREEN:
			   if(g%2==0)
				    result = '0';
				else
					result = '1';
			   currentColor++;
		       break;
		   
		   case BLUE:
			   if(b%2==0)
				    result = '0';
				else
					result = '1';
			   currentColor = RED;
		       currentPixel++;
		       break;		 
		 }
		return result;
	}
}