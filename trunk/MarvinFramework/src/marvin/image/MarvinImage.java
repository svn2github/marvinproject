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

package marvin.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Image object with many operations. This class is the image
 * representation used for the other classes in the framework.
 *
 * @version 1.0 02/13/08
 * @author Danilo Roseto Munoz
 * @author Fabio Andrijaukas
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImage implements Cloneable {
	// Definitions
	public final static int PROPORTIONAL = 0;	

	// Image
	protected BufferedImage image;

	// File path
	protected String filePath;
	
	// Array Color
	protected int[] arrColor;
	
	// Colors
	protected int rgb, r, b, g;
	protected Color color;

	// Format
	protected String formatName;
	
	
	// Dimension
	int width;
	int height;
	
	/**
	 * Constructor using a image in memory
	 * @param img Image
	 */
	public MarvinImage(BufferedImage a_image){		
		this.image =  a_image;
		filePath = "";
		formatName = "JPG";
		width = a_image.getWidth();
		height = a_image.getHeight();
		updateColorArray();
	}
	
	/**
	 * Constructor using a image in memory
	 * @param img Image
	 * @param a_formatName Image format name
	 */
	public MarvinImage(BufferedImage a_image, String a_formatName){		
		this.image =  a_image;
		formatName = a_formatName;
		filePath = "";
		
		width = a_image.getWidth();
		height = a_image.getHeight();
		
		updateColorArray();
	}

	/**
	 * Constructor to blank image, passing the size of image
	 * @param int width
	 * @param int height
	 */
	public MarvinImage(int a_width, int a_height){
		image = new BufferedImage(a_width, a_height, BufferedImage.TYPE_INT_RGB);
		filePath = "";
		formatName = "JPG";
		setDimension(a_width, a_height);		
	}
	
	public MarvinImage crop(int x,int y,int altura, int largura){		 
		return(new MarvinImage(image.getSubimage(x, y, altura, largura)));
	}
	
	public void updateColorArray(){
		arrColor = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
	}
	
	public void update(){
		int l_width = image.getWidth();		
		image.setRGB(0, 0, image.getWidth(), image.getHeight(), arrColor,0,l_width);
	}
	
	/**
	 * Gets the type
	 */
	public int getType(){		
		return image.getType();
	}
	
	//@todo remove ambiguity between Type and FormatName
	/*
	 * @return image format name
	 */
	public String getFormatName(){
		return formatName;
	}
	
	/**
	 * @param a_filePath : file path to this MarvinImage
	 */
	public void setFilePath(String a_filePath){
		filePath = a_filePath;
	}
	
	/**
	 * @return file path associated to this MarvinImage, if exists.
	 */
	public String getFilePath(){
		return filePath;
	}
	
	public void setDimension(int a_width, int a_height){
		image = new BufferedImage(a_width, a_height, BufferedImage.TYPE_INT_RGB);
		width = a_width;
		height = a_height;
		arrColor = new int[width*height];		
	}

	/**
	 * @return RGB array for the entire image
	 */
	public int[] getRGBArray(){
		//return image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, 0);
		return arrColor;
	}
	
	/**
	 *	Set the RGB array for the entire image 
	 **/
	public void setRGBArray(int[] a_arrRGB){
		//image.setRGB(0, 0, image.getWidth(), image.getHeight(), a_arrRGB,0,0);
		arrColor = a_arrRGB;
	}
	
	/**
	 * 
	 */
	public static void copyRGBArray(MarvinImage a_imageSource, MarvinImage a_imageDestine){
		System.arraycopy(a_imageSource.getRGBArray(), 0, a_imageDestine.getRGBArray(), 0, a_imageSource.getWidth()*a_imageSource.getHeight());
	}
	
	
	/**
	 * Gets the rgb for x, y position
	 * @param int - x 
	 * @param int - y
	 * @return int rgb
	 */
	public int getRGB(int x, int y){
		return arrColor[y*width+x];
	}
	
	
	/**
	 * Gets the red component in the x and y position
	 * @param int - x 
	 * @param int - y
	 * @return int red color
	 */	
	public int getRed(int x, int y){
		return (arrColor[((y*width+x))]& 0x00FF0000) >>> 16;
	}

	/**
	 * Gets the green component in the x and y position
	 * @param int - x 
	 * @param int - y
	 * @return int green color
	 */	
	public int getGreen(int x, int y){
		return (arrColor[((y*width+x))]& 0x0000FF00) >>> 8;
	}

	/**
	 * Gets the blue component in the x and y position
	 * @param int - x 
	 * @param int - y
	 * @return int blue color
	 */	
	public int getBlue(int x, int y){
		return (arrColor[((y*width+x))] & 0x000000FF);
	}

	/**
	 * Returns the width 
	 * @return int - width
	 */
	public int getWidth(){
		return(image.getWidth());  	
	}

	/**
	 * Returns the height 
	 * @return int - height
	 */
	public int getHeight(){
		return(image.getHeight());  
	}

	/**
	 * Sets the RGB in X an Y position
	 * @param x position
	 * @param y position
	 * @param rgb RGB value
	 */
	public void setRGB(int x, int y, int rgb){
		arrColor[((y*image.getWidth()+x))] = rgb;
	}

	/**
	 * Sets the RGB in X an Y position
	 * @param x position
	 * @param y position
	 * @param r Red color
	 * @param g Green color
	 * @param b Blue Color
	 */
	public void setRGB(int x, int y, int r, int g, int b){
		arrColor[((y*image.getWidth()+x))] = (255 << 24)+
		(r << 16)+
		(g << 8)+
		b;
	}

	/**
	 * Sets a new image 
	 * @param BufferedImage imagem
	 */
	public void setBufferedImage(BufferedImage a_image){		
		image = a_image;
		width = a_image.getWidth();
		height = a_image.getHeight();
		updateColorArray();		
	}

	/**
	 * @returns a BufferedImage associated with the MarvinImage 
	 */
	public BufferedImage getBufferedImage(){
		return image;
	}
	
	/**
	 * Set the value of RGB in the 0 to 255
	 * @return int - the RGB value
	 */
	public int limitRgb(int rgb){

		if(rgb > 255){
			rgb = 255;
			return(rgb);
		}

		if(rgb < 0){
			rgb = 0;
			return(rgb);
		}
		return rgb;
	}

	/**
	 * Convolution operator
	 * @return int[]
	 */
	public int[] Multi8p(int x, int y,int masc[][]){
		//a b c
		//d e f
		//g h i
		int aR = getRed(x-1,y-1);   int bR = getRed(x-1,y);  int cR = getRed(x-1,y+1);
		int aG = getGreen(x-1,y-1);   int bG = getGreen(x-1,y);  int cG = getGreen(x-1,y+1);
		int aB = getGreen(x-1,y-1);   int bB = getGreen(x-1,y);  int cB = getGreen(x-1,y+1);


		int dR = getRed(x,y-1);      int eR = getRed(x,y);   int fR = getRed(x,y+1);
		int dG = getGreen(x,y-1);      int eG = getGreen(x,y);   int fG = getGreen(x,y+1);
		int dB = getGreen(x,y-1);      int eB = getGreen(x,y);   int fB = getGreen(x,y+1);


		int gR = getRed(x+1,y-1);    int hR = getRed(x+1,y);   int iR = getRed(x+1,y+1);
		int gG = getGreen(x+1,y-1);    int hG = getGreen(x+1,y);   int iG = getGreen(x+1,y+1);
		int gB = getGreen(x+1,y-1);    int hB = getGreen(x+1,y);   int iB = getGreen(x+1,y+1);

		int rgb[] = new int[3];

		rgb[0] = ( (aR * masc[0][0]) + (bR * masc[0][1]) +(cR * masc[0][2])+
				(dR * masc[1][0]) + (eR * masc[1][1]) +(fR * masc[1][2])+
				(gR * masc[2][0]) + (hR * masc[2][1]) +(iR * masc[2][2]) ); 

		rgb[1] = ( (aG * masc[0][0]) + (bG * masc[0][1]) +(cG * masc[0][2])+
				(dG * masc[1][0]) + (eG * masc[1][1]) +(fG * masc[1][2])+
				(gG * masc[2][0]) + (hG * masc[2][1]) +(iG * masc[2][2]) ); 

		rgb[2] = ( (aB * masc[0][0]) + (bB * masc[0][1]) +(cB * masc[0][2])+
				(dB * masc[1][0]) + (eB * masc[1][1]) +(fB * masc[1][2])+
				(gB * masc[2][0]) + (hB * masc[2][1]) +(iB * masc[2][2]) ); 

		// return the value for all channel
		return(rgb);

	}

	/**
	 * Return a new instance of the BufferedImage
	 * @return BufferedImage
	 */
	public BufferedImage getNewImageInstance(){
		BufferedImage buf = new BufferedImage(image.getWidth(),image.getHeight(), image.getType());
		buf.setData(image.getData());
		return buf;
	}

	/**
	 * Resize and return the image passing the new height and width
	 * @param height
	 * @param width
	 * @return
	 */
	public BufferedImage getBufferedImage(int width, int height)
	{
		// using the new approach of Java 2D API 
		BufferedImage buf = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D) buf.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(image,0,0,width,height,null);
		g2d.dispose();
		return(buf);
	}
	
	/**
	 * Resize and return the image passing the new height and width, but maintains width/height factor
	 * @param height
	 * @param width
	 * @return
	 */
	 public BufferedImage getBufferedImage(int width, int height, int type){
		 int	l_widthDif, 
				l_heightDif,				
				l_finalWidth = 0, 
				l_finalHeight = 0;

		 double	l_imageWidth,
					l_imageHeight;

		 double l_factor;
		 l_imageWidth = image.getWidth();
		 l_imageHeight = image.getHeight();
		

		 switch(type)
		 {
			 case PROPORTIONAL:
				 l_widthDif = (int)l_imageWidth - width;
				 l_heightDif = (int)l_imageHeight - height;
				 if(l_widthDif > l_heightDif){
					 l_factor = width/l_imageWidth;
				 }
				 else{
					 l_factor = height/l_imageHeight;
				 }
				 l_finalWidth = (int)Math.floor(l_imageWidth*l_factor);
				 l_finalHeight = (int)Math.floor(l_imageHeight*l_factor);
				 break;
		 }
		 return getBufferedImage(l_finalWidth, l_finalHeight);
	 }
	/**
	 * Resize the image passing the new height and width
	 * @param height
	 * @param width
	 * @return
	 */
	public void resize(int a_width, int a_height)
	{

		// using the new approach of Java 2D API 
		BufferedImage buf = new BufferedImage(a_width,a_height, image.getType());
		Graphics2D g2d = (Graphics2D) buf.getGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.drawImage(image,0,0,a_height,a_width,null);
		g2d.dispose();
		image = buf;
		width = a_width;
		height = a_height;
		updateColorArray();
	}
	
	/**
	 * Clones the {@link MarvinImage}
	 */
	public MarvinImage clone() {
		try {
			MarvinImage newMarvinImg = (MarvinImage)super.clone();
			BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			newMarvinImg.setBufferedImage(newImage);
			MarvinImage.copyRGBArray(this, newMarvinImg);
			newMarvinImg.update();
			return newMarvinImg;

		}
		catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	/**
	 * Multiple of gradient windwos per masc relation of x y 
	 * @return int[]
	 */
	public double Multi8p(int x, int y,double masc){
		int aR = getRed(x-1,y-1);     int bR = getRed(x-1,y);    int cR = getRed(x-1,y+1);
		int aG = getGreen(x-1,y-1);   int bG = getGreen(x-1,y);  int cG = getGreen(x-1,y+1);
		int aB = getGreen(x-1,y-1);   int bB = getGreen(x-1,y);  int cB = getGreen(x-1,y+1);

		
		int dR = getRed(x,y-1);        int eR = getRed(x,y);     int fR = getRed(x,y+1);
		int dG = getGreen(x,y-1);      int eG = getGreen(x,y);   int fG = getGreen(x,y+1);
		int dB = getGreen(x,y-1);      int eB = getGreen(x,y);   int fB = getGreen(x,y+1);

		
		int gR = getRed(x+1,y-1);      int hR = getRed(x+1,y);     int iR = getRed(x+1,y+1);
		int gG = getGreen(x+1,y-1);    int hG = getGreen(x+1,y);   int iG = getGreen(x+1,y+1);
		int gB = getGreen(x+1,y-1);    int hB = getGreen(x+1,y);   int iB = getGreen(x+1,y+1);

		double rgb = 0;

		rgb = ( (aR * masc) + (bR * masc) +(cR * masc)+
				(dR * masc) + (eR * masc) +(fR * masc)+
				(gR * masc) + (hR * masc) +(iR * masc) ); 

		return(rgb);

	}
	public int boundRGB(int rgb){

		if(rgb > 255){
			rgb = 255;
			return(rgb);
		}

		if(rgb < 0){
			rgb = 0;
			return(rgb);
		}
		return rgb;
	}
	
	/**
	 * Draws a rectangle in the image. It큦 useful for debugging purposes.
	 * @param a_x		- rect큦 start position in x-axis
	 * @param a_y		- rect큦 start positioj in y-axis
	 * @param a_width	- rect큦 width
	 * @param a_height	- rect큦 height
	 * @param a_color	- rect큦 color
	 */
	public void drawRect(int a_x, int a_y, int a_width, int a_height, Color a_color){
		int l_colorRGB = a_color.getRGB();
		for(int i=a_x; i<a_x+a_width; i++){
			setRGB(i, a_y, l_colorRGB);
			setRGB(i, a_y+(a_height-1), l_colorRGB);
		}
		
		for(int i=a_y; i<a_y+a_height; i++){
			setRGB(a_x, i, l_colorRGB);
			setRGB(a_x+(a_width-1), i, l_colorRGB);
		}
	}
	
	/**
	 * Fills a rectangle in the image.
	 * @param a_x		- rect큦 start position in x-axis
	 * @param a_y		- rect큦 start positioj in y-axis
	 * @param a_width	- rect큦 width
	 * @param a_height	- rect큦 height
	 * @param a_color	- rect큦 color
	 */
	public void fillRect(int a_x, int a_y, int a_width, int a_height, Color a_color){
		int l_colorRGB = a_color.getRGB();
		for(int l_x=a_x; l_x<a_x+a_width; l_x++){
			for(int l_y=a_y; l_y<a_y+a_height; l_y++){
				setRGB(l_x,l_y,l_colorRGB);
			}
		}
	}
	
	/**
	 * Compare two MarvinImage objects
	 * @param a_object	- object to be compared. MarvinImage object is expected.
	 */
	public boolean equals(Object a_object){
		MarvinImage l_image = (MarvinImage) a_object;
		int[] l_arrColor = l_image.getRGBArray();
		
		if(getWidth() != l_image.getWidth() || getHeight() != l_image.getHeight()){
			return false;
		}
		
		for(int l_cont=0; l_cont<getHeight(); l_cont++){
			if(arrColor[l_cont] != l_arrColor[l_cont]){
				return false;
			}
		}	
		return true;
	}
}