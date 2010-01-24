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

package pluginTester;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.plugin.MarvinPlugin;

import org.marvinproject.render.iteratedFunctionSystem.IteratedFunctionSystem;
import org.marvinproject.render.lindenmayer.Lindenmayer;

/**
 * Test plug-ins and generate .jar files
 * @author Gabriel Ambrosio Archanjo
 */
public class PluginTester extends JFrame{
	
	// Definitions
	private final static String PACKAGE_NET_FOLDER = "./bin/org/";
	private final static String INITIAL_IMAGE = "./res/tucano.jpg";
	
	// Attributes
	private JButton				buttonReset,
								buttonLoadPlugin,
								buttonGenerateJarFiles,
								buttonBenchmark;
	
	private MarvinImage 		originalImage,
								newImage;
	
	private MarvinImagePanel	imagePanel;			
	
	private Benchmark			benchmark;
	
	/*
	 * @Load plug-in to test
	 */
	private void loadPlugin(){
		HashMap<Object,Object> test = new HashMap<Object,Object>();
		test.put("key", null);
		
		MarvinImagePlugin l_plugin = new IteratedFunctionSystem();
		
		l_plugin.setImagePanel(imagePanel);
		MarvinImage i=null; 
		try{
			i = MarvinImageIO.loadImage("./res/tile02.png");
		}
		catch(Exception e){
			
		}
		System.out.println("continue running");
		
		l_plugin.load();
		l_plugin.setAttribute("tile", i);
		l_plugin.show();
		
		
		
		/*
		l_plugin.load();
		l_plugin.show();
		*/
	}
	
	public PluginTester(){
		super("Plug-in Tester");
		
		// GUI
		ButtonHandler l_buttonHandler = new ButtonHandler();
		buttonLoadPlugin = new JButton("Load Plugin");
		buttonReset = new JButton("Reset");	
		buttonGenerateJarFiles = new JButton("Generate Jar Files");
		buttonBenchmark = new JButton("Benchmark");
		
		buttonLoadPlugin.addActionListener(l_buttonHandler);
		buttonReset.addActionListener(l_buttonHandler);
		buttonGenerateJarFiles.addActionListener(l_buttonHandler);
		buttonBenchmark.addActionListener(l_buttonHandler);
		
		imagePanel = new MarvinImagePanel();
		
		JPanel panelBottom = new JPanel();
		panelBottom.add(buttonLoadPlugin);
		panelBottom.add(buttonReset);
		panelBottom.add(buttonGenerateJarFiles);		
		panelBottom.add(buttonBenchmark);
		
		// Container
		Container l_con = getContentPane();
		l_con.setLayout(new BorderLayout());
		
		l_con.add(imagePanel, BorderLayout.NORTH);
		l_con.add(panelBottom, BorderLayout.SOUTH);
		
		
		// Load image
		originalImage = MarvinImageIO.loadImage(INITIAL_IMAGE);
		newImage = originalImage.clone();
		imagePanel.setImage(newImage);
		
		// Benchmark
		benchmark = new Benchmark(); 
		
		int width;
		if(originalImage.getWidth() < 460){
			width = 460;
		}
		else{
			width = originalImage.getWidth();
		}
		setSize(width,originalImage.getHeight()+70);
		
		setVisible(true);
	}
	
	public void generateJarFiles(File a_directory){
		String l_directoryName;
		File[] l_arrFiles;
		String l_path;
		
		if (a_directory.exists() && !a_directory.getName().equals(".svn")){
			l_arrFiles = a_directory.listFiles();

			if (l_arrFiles.length>0){
				for (File l_file : l_arrFiles) {
					if (l_file.isDirectory()){
						l_directoryName = l_file.getName();
						l_directoryName = l_directoryName.substring(0, 1).toUpperCase() + l_directoryName.substring(1).toLowerCase();  
						
						generateJarFiles(l_file);
					}
					else{
						if(isMarvinPlugin(l_file)){
							try{
								l_path = l_file.getParent();
								l_path = l_path.replace("/bin", "");
								l_path = l_path.replace("\\bin", "");
																
								Runtime.getRuntime().exec
								(
									"jar -cvf ../jar/"+getCanonicalName(l_file)+".jar"+" "+l_path,
									null,
									new File("./bin")
								);
							}
							catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}				
			}
		}
	}
	
	/**
	 * Check if the specified file is a class that implements MarvinPlugin interface
	 * @param a_file supposed MarvinPlugin file
	 * @return true if the file is associated to a MarvinPlugin class
	 */
	private boolean isMarvinPlugin(File a_file){
		Class<?> l_class=null;
		String l_classPath;
		l_classPath = a_file.getPath();
		l_classPath = l_classPath.substring(l_classPath.indexOf("bin")+4);
		l_classPath = l_classPath.replace("/", ".");
		l_classPath = l_classPath.replace("\\", ".");
		l_classPath = l_classPath.replace(".class", "");
		
		// Check if the class file implements MarvinPlugin
		try{
			l_class = Class.forName(l_classPath);							
		}
		catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		
		if(MarvinPlugin.class.isAssignableFrom(l_class)){
			return true;
		}
		else{
			return false;
		}
	}
	
	private String getCanonicalName(File a_marvinPlugin){
		String l_canonicalName = a_marvinPlugin.getParent();
		l_canonicalName = l_canonicalName.substring(l_canonicalName.indexOf("bin")+4);
		l_canonicalName = l_canonicalName.replace("/", ".");
		l_canonicalName = l_canonicalName.replace("\\", ".");
		return l_canonicalName;
	}
	
	
	public static void main(String[] args) {
		PluginTester pt = new PluginTester();
		pt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent a_event){
			if(a_event.getSource() == buttonReset){
				newImage = originalImage.clone();
				imagePanel.setImage(newImage);
			}
			else if(a_event.getSource() == buttonLoadPlugin){
				loadPlugin();
			}
			else if(a_event.getSource() == buttonGenerateJarFiles){
				generateJarFiles(new File(PACKAGE_NET_FOLDER));
			}
			else if(a_event.getSource() == buttonBenchmark){
				benchmark.process(originalImage);
			}
		}
	}
}
