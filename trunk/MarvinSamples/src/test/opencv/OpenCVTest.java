/**
Marvin Project <2007-2013>
http://www.marvinproject.org

License information:
http://marvinproject.sourceforge.net/en/license.html

Discussion group:
https://groups.google.com/forum/#!forum/marvin-project
*/

package test.opencv;

import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class OpenCVTest {

	public static void main(String[] args) {
		try{
			for(int i=0; i<5; i++){
				long time = System.currentTimeMillis();
				IplImage image = cvLoadImage("./res/passage.jpg");
				System.out.println("time to load:"+(System.currentTimeMillis()-time));
				time = System.currentTimeMillis();
				cvSaveImage("./res/passage_2.jpg", image, new int[]{com.googlecode.javacv.cpp.opencv_highgui.CV_IMWRITE_JPEG_QUALITY, 30, 0});
				
				System.out.println("time to save:"+(System.currentTimeMillis()-time));
			}
//			System.out.println("------------------------------------------");
//			for(int i=0; i<5; i++){
//				long time = System.currentTimeMillis();
//				byte[] bytes = MwImageManager.loadImageAsByteArray(new File("./res/passage.jpg"));
//				BufferedImage image = MwImageManager.decoderJPG(bytes);
//				System.out.println("time to load:"+(System.currentTimeMillis()-time));
//				time = System.currentTimeMillis();
//				bytes = MwImageManager.convertBufferedImageJPGToByteArray(image);
//				MwImageManager.saveByteArrayImage(bytes, new File("./res/passage_3.jpg"));
//				System.out.println("time to save:"+(System.currentTimeMillis()-time));
//			}
//			System.out.println("------------------------------------------");
//			for(int i=0; i<5; i++){
//				byte[] bytes = MwImageManager.loadImageAsByteArray(new File("./res/passage.jpg"));
//				BufferedImage image2 = MwImageManager.decoderJPG(bytes);
//				long time = System.currentTimeMillis();
//				//IplImage image = IplImage.createFrom(image2); 
//				IplImage image = cvDecodeImage(com.googlecode.javacv.cpp.opencv_core.cvMat(1, bytes.length, com.googlecode.javacv.cpp.opencv_core.CV_8UC1, new BytePointer(bytes)));
//				cvSaveImage("./res/passage_5.jpg", image, new int[]{com.googlecode.javacv.cpp.opencv_highgui.CV_IMWRITE_JPEG_QUALITY, 30, 0});
//				System.out.println("time to save:"+(System.currentTimeMillis()-time));
//			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
