package org.marvinproject.image.edge.prewitt;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinFilterWindow;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinAbstractImagePlugin;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinAttributes;

import org.marvinproject.image.convolution.Convolution;

/**
 * @author Gabriel Ambrósio Archanjo
 */
public class Prewitt extends MarvinAbstractImagePlugin{

	// Definitions
	double[][] matrixPrewittX = new double[][]{
			{1,		0,		-1},
			{1,		0,		-1},
			{1,		0,		-1}
	};
	
	double[][] matrixPrewittY = new double[][]{
			{1,		1,		1},
			{0,		0,		0},
			{-1,	-1,		-1}
	};
	
	private MarvinImagePlugin 	convolution;
	
	public void load(){
		//convolution = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.convolution.jar");
		convolution = new Convolution();
		convolution.load();
	}
	
	public MarvinAttributesPanel getAttributesPanel(){
		return null;
	}
	
	public void process
	(
		MarvinImage imageIn, 
		MarvinImage imageOut,
		MarvinAttributes attrOut,
		MarvinImageMask mask, 
		boolean previewMode
	)
    {
		convolution.setAttribute("matrix", matrixPrewittX);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
		
		convolution.setAttribute("matrix", matrixPrewittY);
		convolution.process(imageIn, imageOut, null, mask, previewMode);
    }
}
