package video.mediaFile;

import javax.swing.JFrame;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.video.MarvinJavaCVAdapter;
import marvin.video.MarvinVideoInterface;
import marvin.video.MarvinVideoInterfaceException;

public class MediaFileExample extends JFrame implements Runnable{

	private MarvinVideoInterface 	videoAdapter;
	private MarvinImage				image;
	private MarvinImagePanel 		videoPanel;
	
	public MediaFileExample(){
		super("Media File Example");
	
		try{
			// Create the VideoAdapter to load the video file
			videoAdapter = new MarvinJavaCVAdapter();
			videoAdapter.loadResource("C:\\Users\\Gabriel\\Desktop\\intro_01.wmv");
			
			// Create VideoPanel
			videoPanel = new MarvinImagePanel();
			add(videoPanel);
			
			// Start the thread for requesting the video frames 
			new Thread(this).start();
			
			setSize(videoAdapter.getImageWidth()+10,videoAdapter.getImageHeight()+80);
			setVisible(true);
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MediaFileExample m = new MediaFileExample();
		m.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void run() {
		try{
			while(true){
				// Request a video frame and set into the VideoPanel
				image = videoAdapter.getFrame();
				videoPanel.setImage(image);
			}
		}
		catch(MarvinVideoInterfaceException e){
			e.printStackTrace();
		}
	}
}
