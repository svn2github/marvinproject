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

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinPluginImage;
import marvin.util.MarvinPluginLoader;

/**
 * Project�s about dialog.
 * @version 1.0 29/01/09 
 * @author Fabio Andrijaukas
 * @author Gabriel Ambr�sio Archanjo
 * @author Danilo Rosetto Mu�oz
 */
public class AboutDialog extends JDialog implements Runnable{
	private Thread 				thread;
	private MarvinPluginImage[] arrPlugin;
	private MarvinImagePanel 	imagePanel;
	private MarvinImage 		imageIn, 
								imageOut;
	private int 				currentPlugin;
	
	/**
     * Constructs a new AboutDialog with the specified <code>Frame</code> as its owner. 
     * @param owner the <code>Frame</code> from which the dialog is displayed
	 */
	public AboutDialog(Frame a_owner){
		super(a_owner);
		setTitle("MARVIN - About");
		
		imageIn = MarvinImageIO.loadImage("./res/images/about.png");
		imageOut = new MarvinImage(imageIn.getWidth(), imageIn.getHeight());
		
		imagePanel = new MarvinImagePanel();
		imagePanel.setImage(imageIn);
		
		arrPlugin = new MarvinPluginImage[7];
		arrPlugin[0] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.invert.jar");
		arrPlugin[1] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.sepia.jar");
		arrPlugin[2] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.halftone.errorDiffusion.jar");
		arrPlugin[3] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.statistical.Median.jar");
		arrPlugin[4] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.color.grayScale.jar");
		arrPlugin[4].setAttribute("size", 1);
		arrPlugin[5] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.edge.EdgeDetector.jar");
		arrPlugin[6] = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.transform.flip.jar");
		add(imagePanel);
		
		setSize(imageIn.getWidth(),imageIn.getHeight()+40);
		setVisible(true);
		
		thread = new Thread(this);
		thread.start();

		/*
		setSize(550, 360);		
		setLayout(new BorderLayout());
		setLocationRelativeTo(getOwner());
		
		Container container = getContentPane();
		
		//Marvin banner
		lblBannerMarvin = new JLabel();
		lblBannerMarvin.setBackground(container.getBackground());
		
		File l_fileMarvinBanner = new File("./rsc/marvinBanner.png");
		
		if (l_fileMarvinBanner.exists()){
			try {
				lblBannerMarvin.setIcon(new ImageIcon(l_fileMarvinBanner.getCanonicalPath()));
			} catch (IOException e1) {
				//Error while loading the Marvin banner. Let show about dialog without banner
			}
		}				
		//Top text
		String l_strText = "";
		l_strText += "   MARVIN Project\n\n";
		l_strText += "   Initial version by:\n";
		l_strText += "   Danilo Rosetto Mu�oz\n";
		l_strText += "   Fabio Andrijauskas\n";
		l_strText += "   Gabriel Ambr�sio Archanjo\n\n";
				
		l_strText += "   site: http://marvinproject.sourceforge.net\n";		
		l_strText += "   \n\n";
		
		l_strText += "   Marvin Copyright (C) 2007\n";
		l_strText += "   Marvin comes with ABSOLUTELY NO WARRANTY;\n";
		l_strText += "   This is free software, and you are welcome to redistribute it\n";
		l_strText += "   under certain conditions.";
		
		//GPL text
		txtAbout = new JTextArea(l_strText);
		txtAbout.setEditable(false);
		txtAbout.setBackground(container.getBackground());
		txtAbout.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		txtAbout.setAlignmentY(JTextArea.CENTER_ALIGNMENT);
		
		container.add(lblBannerMarvin, BorderLayout.WEST);
		container.add(txtAbout, BorderLayout.CENTER);
		*/			
	}
	
	public void run(){
		while(true){
			
			if(currentPlugin == 0){
				MarvinImage.copyRGBArray(imageIn, imageOut);
			}
			else{
				arrPlugin[currentPlugin-1].process(imageIn,imageOut,null,MarvinImageMask.NULL_MASK, false);
			}
			imageOut.update();
			imagePanel.setImage(imageOut);
			
			currentPlugin++;
			if(currentPlugin == arrPlugin.length+1){
				currentPlugin = 0;
			}
			
			try{
				Thread.sleep(3000);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
