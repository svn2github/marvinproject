package marvin;

import java.awt.Color;
import java.util.List;

import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

public class MarvinPluginCollection {

	private static MarvinImagePlugin	blackAndWhite,
										brightnessAndContrast,
										colorChannel,
										combineByMask,
										combineByTransparency,
										convolution,
										determineSceneBackground,
										emboss,
										gaussianBlur,
										grayScale,
										invertColors,
										mergePhotos,
										moravec,
										pixelize,
										sepia,
										skinColorDetection,
										television,
										thresholding;
	
	/*==============================================================================================
	  | BLACK AND WHITE
	  ==============================================================================================*/
	public static void blackAndWhite(MarvinImage image, int level){
		blackAndWhite(image, image, level);
	}
	
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level){
		blackAndWhite(imageIn, imageOut,  level, MarvinImageMask.NULL_MASK);
	}
	
	public static void blackAndWhite(MarvinImage imageIn, MarvinImage imageOut, int level, MarvinImageMask mask){
		blackAndWhite = checkAndLoadImagePlugin(blackAndWhite, "org.marvinproject.image.color.blackAndWhite");
		blackAndWhite.setAttribute("level", level);
		blackAndWhite.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | BRIGHTNESS AND CONTRAST
	  ==============================================================================================*/
	public static void brightnessAndContrast(MarvinImage image, int brightness, int contrast){
		brightnessAndContrast(image, image, brightness, contrast);
	}
	
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast){
		brightnessAndContrast(imageIn, imageOut,  brightness, contrast, MarvinImageMask.NULL_MASK);
	}
	
	public static void brightnessAndContrast(MarvinImage imageIn, MarvinImage imageOut, int brightness, int contrast, MarvinImageMask mask){
		brightnessAndContrast = checkAndLoadImagePlugin(brightnessAndContrast, "org.marvinproject.image.color.brightnessAndContrast");
		brightnessAndContrast.setAttribute("brightness", brightness);
		brightnessAndContrast.setAttribute("contrast", contrast);
		brightnessAndContrast.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | COMBINE BY MASK
	  ==============================================================================================*/
	public static void combineByMask(MarvinImage imageA, MarvinImage imageB, MarvinImage imageOut, int x, int y, Color colorMask){
		combineByMask = checkAndLoadImagePlugin(combineByMask, "org.marvinproject.image.combine.combineByMask");
		colorChannel.setAttribute("xi", x);
		colorChannel.setAttribute("yi", y);
		colorChannel.setAttribute("combinationImage", imageB);
		colorChannel.setAttribute("colorMask", colorMask);
		colorChannel.process(imageA, imageOut);
	}
	
	/*==============================================================================================
	  | COMBINE BY TRANSPARENCY
	  ==============================================================================================*/
	public static void combineByTransparency(MarvinImage imageA, MarvinImage imageB, MarvinImage imageOut, int x, int y, int transparency){
		combineByTransparency = checkAndLoadImagePlugin(combineByMask, "org.marvinproject.image.combine.combineByTransparency");
		combineByTransparency.setAttribute("xi", x);
		combineByTransparency.setAttribute("yi", y);
		combineByTransparency.setAttribute("combinationImage", imageB);
		combineByTransparency.setAttribute("transparency", transparency);
		combineByTransparency.process(imageA, imageOut);
	}
	
	/*==============================================================================================
	  | CONVOLUTION
	  ==============================================================================================*/
	public static void convolution(MarvinImage imageIn, MarvinImage imageOut, double[][] matrix){
		convolution = checkAndLoadImagePlugin(convolution, "org.marvinproject.image.convolution");
		convolution.setAttribute("matrix", matrix);
		convolution.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | COLOR CHANNEL
	  ==============================================================================================*/
	public static void colorChannel(MarvinImage image, int red, int green, int blue){
		colorChannel(image, image, red, green, blue);
	}
	
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue){
		colorChannel(imageIn, imageOut, red, green, blue, MarvinImageMask.NULL_MASK);
	}
	
	public static void colorChannel(MarvinImage imageIn, MarvinImage imageOut, int red, int green, int blue, MarvinImageMask mask){
		colorChannel = checkAndLoadImagePlugin(colorChannel, "org.marvinproject.image.color.colorChannel");
		colorChannel.setAttribute("red", red);
		colorChannel.setAttribute("green", green);
		colorChannel.setAttribute("blue", blue);
		colorChannel.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | DETERMINE SCENE BACKGROUND
	  ==============================================================================================*/
	public static void determineSceneBackground(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		determineSceneBackground = checkAndLoadImagePlugin(determineSceneBackground, "org.marvinproject.image.background.determineSceneBackground");
		determineSceneBackground.setAttribute("threshold", threshold);
		determineSceneBackground.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | EMBOSS
	  ==============================================================================================*/
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut){
		emboss(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void emboss(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		emboss = checkAndLoadImagePlugin(emboss, "org.marvinproject.image.color.emboss");
		emboss.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | GAUSSIAN BLUR
	  ==============================================================================================*/
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius){
		gaussianBlur(imageIn, imageOut, radius, MarvinImageMask.NULL_MASK);
	}
	
	public static void gaussianBlur(MarvinImage imageIn, MarvinImage imageOut, int radius, MarvinImageMask mask){
		gaussianBlur = checkAndLoadImagePlugin(gaussianBlur, "org.marvinproject.image.blur.gaussianBlur");
		gaussianBlur.setAttribute("radius", radius);
		gaussianBlur.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | GRAY SCALE
	  ==============================================================================================*/
	public static void grayScale(MarvinImage image){
		grayScale(image, image);
	}
	
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut){
		grayScale(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void grayScale(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		grayScale = checkAndLoadImagePlugin(grayScale, "org.marvinproject.image.color.grayScale");
		grayScale.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | INVERT COLORS
	  ==============================================================================================*/
	public static void invertColors(MarvinImage image){
		invertColors(image, image);
	}
	
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut){
		invertColors(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void invertColors(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		invertColors = checkAndLoadImagePlugin(invertColors, "org.marvinproject.image.color.invert");
		invertColors.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | MERGE PHOTOS
	  ==============================================================================================*/
	public static void mergePhotos(List<MarvinImage> images, MarvinImage imageOut, int threshold){
		mergePhotos = checkAndLoadImagePlugin(mergePhotos, "org.marvinproject.image.combine.mergePhotos");
		mergePhotos.setAttribute("threshold", threshold);
		mergePhotos.process(images, imageOut);	
	}
	
	/*==============================================================================================
	  | MORAVEC
	  ==============================================================================================*/
	public static void moravec(MarvinImage imageIn, MarvinImage imageOut, int matrixSize, int threshold){
		moravec = checkAndLoadImagePlugin(moravec, "org.marvinproject.image.corner.moravec");
		moravec.setAttribute("matrixSize", matrixSize);
		moravec.setAttribute("threshold", threshold);
		moravec.process(imageIn, imageOut);
	}
	
	/*==============================================================================================
	  | PIXELIZE
	  ==============================================================================================*/
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide){
		pixelize(imageIn, imageOut, squareSide, MarvinImageMask.NULL_MASK);
	}
	
	public static void pixelize(MarvinImage imageIn, MarvinImage imageOut, int squareSide, MarvinImageMask mask){
		pixelize = checkAndLoadImagePlugin(pixelize, "org.marvinproject.image.blur.pixelize");
		pixelize.setAttribute("squareSide", squareSide);
		pixelize.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SEPIA
	  ==============================================================================================*/
	public static void sepia(MarvinImage image, int intensity){
		sepia(image, image, intensity);
	}
	
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity){
		sepia(imageIn, imageOut, intensity, MarvinImageMask.NULL_MASK);
	}
	
	public static void sepia(MarvinImage imageIn, MarvinImage imageOut, int intensity, MarvinImageMask mask){
		sepia = checkAndLoadImagePlugin(sepia, "org.marvinproject.image.color.sepia");
		sepia.setAttribute("intensity", intensity);
		sepia.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | SKIN COLOR DETECTION
	  ==============================================================================================*/
	public static void skinColorDetection(MarvinImage image){
		skinColorDetection(image, image);
	}
	
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut){
		skinColorDetection(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void skinColorDetection(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		skinColorDetection = checkAndLoadImagePlugin(skinColorDetection, "org.marvinproject.image.color.skinColorDetection");
		skinColorDetection.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | TELEVISION
	  ==============================================================================================*/
	public static void television(MarvinImage image){
		television(image, image);
	}
	
	public static void television(MarvinImage imageIn, MarvinImage imageOut){
		television(imageIn, imageOut, MarvinImageMask.NULL_MASK);
	}
	
	public static void television(MarvinImage imageIn, MarvinImage imageOut, MarvinImageMask mask){
		television = checkAndLoadImagePlugin(television, "org.marvinproject.image.artistic.television");
		television.process(imageIn, imageOut, mask);
	}
	
	/*==============================================================================================
	  | THRESHOLDING
	  ==============================================================================================*/
	public static void thresholding(MarvinImage image, int threshold){
		thresholding(image, image, threshold);
	}
	
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold){
		thresholding(imageIn, imageOut, threshold, MarvinImageMask.NULL_MASK);
	}
	
	public static void thresholding(MarvinImage imageIn, MarvinImage imageOut, int threshold, MarvinImageMask mask){
		thresholding = checkAndLoadImagePlugin(thresholding, "org.marvinproject.image.color.thresholding");
		thresholding.process(imageIn, imageOut, mask);
	}
	
	
	private static MarvinImagePlugin checkAndLoadImagePlugin(MarvinImagePlugin ref, String pluginCanonicalName){
		// Plug-in already loaded
		if(ref != null){
			return ref;
		}
		return MarvinPluginLoader.loadImagePlugin(pluginCanonicalName);
	}
	
}
