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

package marvin.util;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * File Chooser.
 * @version 1.0 02/13/08
 * @author Danilo Rosetto Muñoz
 */
public class MarvinFileChooser extends JDialog{	
	// Constants
	public final static int OPEN_DIALOG = JFileChooser.OPEN_DIALOG;
	public final static int SAVE_DIALOG = JFileChooser.SAVE_DIALOG;

	//Creates an instance to the JFileChooser and use static to maintain the last folder used.
	static JFileChooser chooser = null;
	
	// Static file filter objects
	public final static FileNameExtensionFilter[] JPG = new FileNameExtensionFilter[]{new FileNameExtensionFilter("JPEG", "jpg", "jpg")};
	public final static FileNameExtensionFilter[] PNG = new FileNameExtensionFilter[]{new FileNameExtensionFilter("PNG", "png")};
	public final static FileNameExtensionFilter[] AllSupportedImages = new FileNameExtensionFilter[]{JPG[0], PNG[0]};
	
	private static void init(){
		chooser = new JFileChooser("./img");
		chooser.setAcceptAllFileFilterUsed(false);
	}
	/**
	 * Select the file 
	 * @param parent The parent component of the dialog, can be <code>null</code>.
	 * @param forceExistingFile Indicates when the file should exists or not. If file doesn't exists, a FileNotFoundException will be thrown.
	 * @return Selected file
	 * @throws IOException 
	 */
	public static String select(Component a_parent, boolean a_forceExistingFile, int a_dialogType, FileNameExtensionFilter[] a_arrExtensions) throws IOException{
		int l_result=0;
		
		if(chooser == null)
		{
			init();
		}
		
		if(a_arrExtensions == null){
			chooser.resetChoosableFileFilters();
		}
		else{
			for(int i=0; i<a_arrExtensions.length; i++){
				chooser.addChoosableFileFilter(a_arrExtensions[i]);
			}
		}
		
		//Open the image chooser dialog		
		switch(a_dialogType){
			case OPEN_DIALOG:
				l_result = chooser.showOpenDialog(a_parent); break;
			case SAVE_DIALOG:
				l_result = chooser.showSaveDialog(a_parent); break;
		}
		
		//If user cancel the operation, return null
		if(l_result == JFileChooser.CANCEL_OPTION){
			// to quick show in the next selection
			init();
			return null;
		}

		if (chooser.getSelectedFile() == null) throw new FileNotFoundException();
		
		//Verify if file exists. If doesn't exists, throw the FileNotFoundException
		String path = chooser.getSelectedFile().getCanonicalPath();
		
		if(a_dialogType == SAVE_DIALOG)
		{
			int l_lastIndex;
			l_lastIndex = path.lastIndexOf('.');
			if(l_lastIndex == -1)
			{
				path+="."+chooser.getFileFilter().getDescription().toLowerCase();
			}
		}
		
		if (path != null){
			if(a_forceExistingFile && !chooser.getSelectedFile().exists()){
				throw new FileNotFoundException();
			}else{
				// to quick show in the next selection
				init();
				return path;
			}
		}else{
			throw new FileNotFoundException();
		}
	}

	public static String select(Component parent, boolean forceExistingFile, int dialogType) throws IOException{
		return select(parent, forceExistingFile, dialogType, AllSupportedImages);
	}
}
