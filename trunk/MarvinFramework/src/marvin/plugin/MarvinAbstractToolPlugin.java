package marvin.plugin;

import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;

import marvin.gui.MarvinImagePanel;
import marvin.gui.MarvinPluginWindow;

public abstract class MarvinAbstractToolPlugin implements MarvinPluginTool{

	private MarvinImagePanel	imagePanel;
	
	private ImageIcon 			imageIcon;
	private Image				cursorImage;
	
	private Point				cursorHotSpot;
	
	/**
	 * Protected Contructor
	 */
	protected void loadIcon
	(
		String a_pathIcon
	){
		imageIcon = new ImageIcon(loadImage(a_pathIcon));
	}
	
	protected void loadCursor(String a_pathCursorImage){
		loadCursor(a_pathCursorImage, new Point(0,0));
	}
	
	protected void loadCursor(String a_pathCursorImage,Point a_cursorHotSpot){
		cursorImage = loadImage(a_pathCursorImage);
		cursorHotSpot = a_cursorHotSpot;
	}

	
	/**
	 * Load image
	 * @param a_path
	 * @return
	 */
	protected Image loadImage(String a_path){
		URL l_url =null;
		Image l_image=null;
		File l_file;
		try{
			l_url = new URL((this.getClass().getResource("").toString()+a_path));
			l_image = new ImageIcon(l_url).getImage();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return l_image;
	}
	
	public ImageIcon getIcon(){
		return imageIcon;
	}
	
	public Image getCursorImage(){
		return cursorImage;
	}
	
	public Point getCursorHotSpot(){
		return cursorHotSpot;
	}
}
