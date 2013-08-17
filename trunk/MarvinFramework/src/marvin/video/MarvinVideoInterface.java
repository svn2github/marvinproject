package marvin.video;

import marvin.image.MarvinImage;

import com.googlecode.javacv.FrameGrabber;

public interface MarvinVideoInterface {

	// Connection
	void connect(int deviceIndex) 							throws MarvinVideoInterfaceException;
	void connect(int deviceIndex, int width, int height) 	throws MarvinVideoInterfaceException;
	void loadResource(String path) 							throws MarvinVideoInterfaceException;
	void disconnect() 										throws MarvinVideoInterfaceException;
	
	// Image Width / Height
	int getImageWidth();
	int getImageHeight();
	
	// Frame request
	MarvinImage getFrame() 									throws MarvinVideoInterfaceException;
}
