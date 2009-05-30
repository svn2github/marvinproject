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

/**
 * Mask indicating what pixels must be processed.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImageMask {
	// Static nullMask
	public static MarvinImageMask NULL_MASK = new MarvinImageMask();
	
	private boolean arrMask[][];
	private int		width,
					height;
	
	/**
	 * Constructor for null mask
	 */
	public MarvinImageMask(){
		arrMask = null;
	}
	
	/**
	 * Contructor
	 * @param a_width - 	Width of the image referenced by the mask
	 * @param a_imageHeight -	Height of the image referenced by the mask
	 */
	public MarvinImageMask (int a_width, int a_height){
		width = a_width;
		height = a_height;
		arrMask = new boolean[width][height];
	}
	
	/**
	 * @param a_maskWidth 		- 	Width of the image referenced by the mask
	 * @param a_maskHeight 	-	Height of the image referenced by the mask
	 * @param a_startX			-	Start pixel of the region in x axes
	 * @param a_startY			-	Start pixel of the region in y axes
	 * @param a_regionWidth		- 	Width of the region
	 * @param a_regionHeight	- 	Height of the region
	 */
	public MarvinImageMask
	(
		int a_maskWidth, 
		int a_maskHeight, 
		int a_startX,
		int a_startY,
		int a_regionWidth,
		int a_regionHeight
	)
	{
		this(a_maskWidth, a_maskHeight);
		addRectRegion(a_startX, a_startY, a_regionWidth, a_regionHeight);
	}
	
	/**
	 * Clear the mask for a new selection
	 */
	public void clear(){
		if(arrMask != null){
			for(int l_y=0; l_y<height; l_y++){
				for(int l_x=0; l_x<width; l_x++){
					arrMask[l_x][l_y] = false;
				}
			}
		}
	}
	
	/**
	 * @return	Mask Array.
	 */
	public boolean[][] getMaskArray(){
		return arrMask;
	}
	
	/**
	 * @param a_startX			-	Start pixel of the region in x axes
	 * @param a_startY			-	Start pixel of the region in y axes
	 * @param a_regionWidth		- 	Width of the region
	 * @param a_regionHeight	- 	Height of the region
	 */
	public void addRectRegion
	(
		int a_startX,
		int a_startY,
		int a_regionWidth,
		int a_regionHeight
	)
	{
		for(int x=a_startX; x<a_startX+a_regionWidth; x++){
			for(int y=a_startY; y<a_startY+a_regionHeight; y++){
				arrMask[x][y] = true;
			}
		}
	}
}
