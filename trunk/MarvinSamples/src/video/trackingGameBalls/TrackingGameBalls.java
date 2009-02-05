package video.trackingGameBalls;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.video.MarvinVideoManager;

public class TrackingGameBalls extends JFrame implements Runnable{

	private MarvinVideoManager	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private Thread 				thread;
	
	private MarvinImage 		imageIn, 
								imageOut,
								imageHat,
								imageBall;
							
	
	private JPanel				panelSlider;
	
	private JSlider				sliderSensibility;
	
	private JLabel				labelSlider;
	
	private boolean				objectCaptured;
	
	private int					objectPx,
								objectPy,
								objectWidth,
								objectHeight;
	
	private int[]				arrObjectPixels,
								arrRegionPixels;
	
	private int					patternWidth=60;
	private int					patternHeight=60;
	
	private int					sensibility=30;
	
	private boolean				regionSelected=false;
	private int[]				firstClick;
	
	private Ball[]				arrBall;
	
	private int					screenWidth,
								screenHeight;
	
	private int					maskPx,
								maskPy,
								maskWidth=120,
								maskHeight=60;
	
	
	public TrackingGameBalls(){
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
		
		imageHat = MarvinImageIO.loadImage("hat.png");
		imageBall = MarvinImageIO.loadImage("ball.png");
		loadGUI();
		
		arrBall = new Ball[5]; 
		for(int l_i=0; l_i<5; l_i++){
			arrBall[l_i] = new Ball();
			arrBall[l_i].used = false;
		}
		
		screenWidth = videoManager.getCameraWidth();
		screenHeight = videoManager.getCameraHeight();
		/*
		objectPx = 300;
		objectPy = 200;
		objectWidth = 120;
		objectHeight = 180;
		*/
				
		objectCaptured = false;
		
		thread = new Thread(this);
		thread.start();
	}
	
	private void loadGUI(){	
		
		videoPanel.addMouseListener(new MouseHandler());
		
		sliderSensibility = new JSlider(JSlider.HORIZONTAL, 0, 60, 30);
		sliderSensibility.setMinorTickSpacing(2);
		sliderSensibility.setPaintTicks(true);
		sliderSensibility.addChangeListener(new SliderHandler());
		
		labelSlider = new JLabel("Sensibility");
		
		panelSlider = new JPanel();
		panelSlider.add(labelSlider);
		panelSlider.add(sliderSensibility);
		
		Container l_container = getContentPane();
		l_container.setLayout(new BorderLayout());
		l_container.add(videoPanel, BorderLayout.NORTH);
		l_container.add(panelSlider, BorderLayout.SOUTH);
		
		setSize(1000,700);
		setVisible(true);
	}
	
	private void initPattern(int a_x, int a_y, int a_width, int a_height){
		objectPx = a_x;
		objectPy = a_y;
		objectWidth = a_width;
		objectHeight = a_height;
		
		patternWidth = a_width/2;
		patternHeight = a_height/2;
		
		arrObjectPixels = new int[patternWidth*patternHeight];
		arrRegionPixels = new int[patternWidth*patternHeight];
		
		captureObject(objectPx, objectPy, objectWidth, objectHeight);
	}
	
	private void captureObject(int a_x, int a_y, int a_width, int a_height){
		double l_xFactor = (double)a_width/patternWidth;
		double l_yFactor = (double)a_height/patternHeight;
		
		double l_dX=a_x,l_dY=a_y;
		int l_iX,l_iY;
		
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			l_dY+=l_yFactor;
			for(int l_w=0; l_w<patternWidth; l_w++){
				l_dX+=l_xFactor;
				
				l_iX = (int)l_dX;
				l_iY = (int)l_dY;
				
				arrObjectPixels[((l_h)*patternWidth)+l_w] = imageOut.getRGB(l_iX, l_iY);
			}
			l_dX=a_x;
		}
		objectCaptured = true;
	}
	
	private void process(){		
		
		int l_arrRegion[] = new int[]{objectPx, objectPy, objectWidth, objectHeight};
		
		newRegion(l_arrRegion, objectWidth/10,0,4);
		newRegion(l_arrRegion, objectWidth/20,0,5);
		newRegion(l_arrRegion, 1,0,20);
		newRegionSize(l_arrRegion, 0,8);
		
		objectPx = l_arrRegion[0];
		objectPy = l_arrRegion[1];
		objectWidth = l_arrRegion[2];
		objectHeight = l_arrRegion[3];
	}
	
	private void newRegionSize(int[] a_arrRegion, int a_depth, int a_maxDepth){
		double l_arrMatch[] = new double[3];
		double l_tempMatch;
		double l_betterMatch=0;
		int l_betterIndex=0;
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 0;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]-3, a_arrRegion[1]-3, a_arrRegion[2]+6, a_arrRegion[3]+6);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 1;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+3, a_arrRegion[1]+3, a_arrRegion[2]-6, a_arrRegion[3]-6);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 2;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch >= l_betterMatch){
			
			return;
		}
		
		switch(l_betterIndex){
			case 1: 	
				a_arrRegion[0]-=3;	
				a_arrRegion[1]-=3;
				a_arrRegion[2]+=6;	
				a_arrRegion[3]+=6;
				break;
			case 2: 	
				a_arrRegion[0]+=3;	
				a_arrRegion[1]+=3;
				a_arrRegion[2]-=6;	
				a_arrRegion[3]-=6;				
				break;
		}
		
		if(a_depth < a_maxDepth){
			newRegionSize(a_arrRegion, a_depth+1, a_maxDepth);
		}	
		
		
		
	}
	
	private void newRegion(int[] a_arrRegion, int a_pixelShift, int a_depth, int a_maxDepth){
		double l_arrMatch[] = new double[8];
		double l_tempMatch;
		double l_betterMatch=0;
		int l_betterIndex=0;
		
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 0;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 1;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 2;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1]+a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 3;
		}
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 4;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]-a_pixelShift, a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 5;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1]-a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 6;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0]+a_pixelShift, a_arrRegion[1]+a_pixelShift, a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch > l_betterMatch){
			l_betterMatch = l_tempMatch;
			l_betterIndex = 7;
		}
		
		l_tempMatch = matchRegion(a_arrRegion[0], a_arrRegion[1], a_arrRegion[2], a_arrRegion[3]);
		if(l_tempMatch >= l_betterMatch){
			return;
		}
		
		
		switch(l_betterIndex){
			case 0: 	a_arrRegion[0]-=a_pixelShift;									break;
			case 1: 	a_arrRegion[0]+=a_pixelShift;									break;
			case 2: 									a_arrRegion[1]-=a_pixelShift;	break;
			case 3: 									a_arrRegion[1]+=a_pixelShift;	break;
			case 4: 	a_arrRegion[0]-=a_pixelShift;	a_arrRegion[1]-=a_pixelShift;	break;
			case 5: 	a_arrRegion[0]-=a_pixelShift;	a_arrRegion[1]+=a_pixelShift;	break;
			case 6: 	a_arrRegion[0]+=a_pixelShift;	a_arrRegion[1]-=a_pixelShift;	break;
			case 7: 	a_arrRegion[0]+=a_pixelShift;	a_arrRegion[1]+=a_pixelShift;	break;
		}
		
		if(a_depth < a_maxDepth){
			newRegion(a_arrRegion, a_pixelShift, a_depth+1, a_maxDepth);
		}
	}	
	
	private double matchRegion(int a_x, int a_y, int a_width, int a_height){
		double l_xFactor = (double)a_width/patternWidth;
		double l_yFactor = (double)a_height/patternHeight;
		
		double l_dX=a_x,l_dY=a_y;
		int l_iX,l_iY;
		
		if
		(
			a_x < 0 ||
			a_y < 0 ||
			a_x+a_width+1 > videoManager.getCameraWidth() || 
			a_y+a_height+1 > videoManager.getCameraHeight()
		){
			return 0;
		}
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			l_dY+=l_yFactor;
			for(int l_w=0; l_w<patternWidth; l_w++){
				l_dX+=l_xFactor;
				
				l_iX = (int)l_dX;
				l_iY = (int)l_dY;
				
				arrRegionPixels[((l_h)*patternWidth)+l_w] = imageOut.getRGB(l_iX, l_iY);
			}
			l_dX=a_x;
		}
		
		int l_diffPixels=0,
			l_redA,
			l_redB,
			l_greenA,
			l_greenB,
			l_blueA,
			l_blueB;
		
		for(int l_h=0; l_h<patternHeight; l_h++){
			for(int l_w=0; l_w<patternWidth; l_w++){
		
				l_redA 		= (arrObjectPixels[(l_h*patternWidth)+l_w] 	& 0x00FF0000) >>> 16;
				l_redB 		= (arrRegionPixels[(l_h*patternWidth)+l_w] 	& 0x00FF0000) >>> 16;
				l_greenA 	= (arrObjectPixels[(l_h*patternWidth)+l_w] 	& 0x0000FF00) >>> 8;
				l_greenB 	= (arrRegionPixels[(l_h*patternWidth)+l_w] 	& 0x0000FF00) >>> 8;
				l_blueA		= (arrObjectPixels[(l_h*patternWidth)+l_w] 	& 0x000000FF);
				l_blueB		= (arrRegionPixels[(l_h*patternWidth)+l_w] 	& 0x000000FF);
				
				if
				(
					Math.abs(l_redA-l_redB) > sensibility ||
					Math.abs(l_greenA-l_greenB) > sensibility ||
					Math.abs(l_blueA-l_blueB) > sensibility
				){
					l_diffPixels++;
				}
				
			}
		}
		
		return 100-(((double)l_diffPixels/(patternWidth*patternHeight))*100);		
	}
	
	private void combineImage(MarvinImage a_image, int a_x, int a_y){
		int l_rgb;
		int l_width = a_image.getWidth();
		int l_height = a_image.getHeight();
		
		for(int l_y=0; l_y<l_height; l_y++){
			for(int l_x=0; l_x<l_width; l_x++){
				if
				(
					l_x+a_x > 0 && l_x+a_x < screenWidth &&
					l_y+a_y > 0 && l_y+a_y < screenHeight
				)
				{
					l_rgb=a_image.getRGB(l_x, l_y);				
					if(l_rgb != 0xFFFFFFFF){
						imageOut.setRGB(l_x+a_x, l_y+a_y, l_rgb);
					}
				}
			}
		}		
	}
	
	private long lastBall=0;
	private final static int BALL_DELAY = 1000;
	private long currentTime;
	private void updateBalls(){
				
		currentTime = System.currentTimeMillis();
		if(currentTime - lastBall > BALL_DELAY){
			launchBall();
			lastBall = currentTime;
		}
		
		for(int l_i=0; l_i<5; l_i++){
			if(arrBall[l_i].used){
				arrBall[l_i].py *= 1.1;
				combineImage(imageBall,(int)arrBall[l_i].px,(int)arrBall[l_i].py);
			}
		}
		
	}
	
	private void launchBall(){
		for(int l_i=0; l_i<5; l_i++){
			if(!arrBall[l_i].used){
				arrBall[l_i].py = 1;
				arrBall[l_i].px = (Math.random()*(screenWidth-300))+150;
				System.out.println("arrBall[l_i].px:"+arrBall[l_i].px);
				arrBall[l_i].used = true;
				return;
			}
		}
	}
	
	public void run(){
		long time = System.currentTimeMillis();
		int ticks=0;
		
		while(true){
			
			ticks++;
			if(System.currentTimeMillis() - time > 1000){
				System.out.println("FPS: "+ticks+"       ");
				ticks=0;
				time = System.currentTimeMillis();					
			}
			
			imageIn = videoManager.getCapturedImage();
			imageOut = videoManager.getResultImage();
						
			MarvinImage.copyRGBArray(imageIn, imageOut);
			
			if(objectCaptured){
				process();
				gameLoop();
				
			}
			
			videoManager.updatePanel();
		}
	}
	
	private void gameLoop(){
		combineImage(imageHat,objectPx-30,objectPy-60);
		
		maskPx = objectPx-12;
		maskPy = objectPy-58;
		
		/*
		imageOut.fillRect
		(
			maskPx,
			maskPy,
			maskWidth, 
			maskHeight,
			Color.black
		);
		*/
		
		updateBalls();
		collisionDetection();
	}
	
	public void collisionDetection(){
		for(int l_i=0; l_i<5; l_i++){
			if(arrBall[l_i].used){
				collisionBallMask(arrBall[l_i]);
				collisionBallScreen(arrBall[l_i]);
			}
		}
	}
	
	private void collisionBallScreen(Ball a_ball){
		if(a_ball.py > screenHeight){
			a_ball.used = false;
		}
	}
	
	private void collisionBallMask(Ball a_ball){
		if
		(
			(
				a_ball.px >= maskPx && a_ball.px <= maskPx+maskWidth ||
				a_ball.px <= maskPx && a_ball.px+a_ball.width >= maskPx
			)
			&&
			(
				a_ball.py >= maskPy && a_ball.py <= maskPy+maskHeight ||
				a_ball.py <= maskPy && a_ball.py+a_ball.height >= maskPy
			)
		)
		{
			a_ball.used = false;
		}
			
	}
	
	public static void main(String args[]){
		TrackingGameBalls l_trackingBalls = new TrackingGameBalls();
		l_trackingBalls.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private class SliderHandler implements ChangeListener{
		public void stateChanged(ChangeEvent a_event){
			sensibility = (60-sliderSensibility.getValue());
		}
	}
	
	private class MouseHandler implements MouseListener{
		public void mouseEntered(MouseEvent a_event){}
		public void mouseExited(MouseEvent a_event){}
		public void mousePressed(MouseEvent a_event){}
		public void mouseClicked(MouseEvent a_event){}
		
		public void mouseReleased(MouseEvent a_event){
			if(!regionSelected){
				if(firstClick == null){
					firstClick = new int[]{a_event.getX(), a_event.getY()};
				}
				else{
					initPattern(firstClick[0], firstClick[1], a_event.getX()-firstClick[0], a_event.getY()-firstClick[1]);
					regionSelected = true;
				}	
			}
		}		
	}
	
	private class Ball{
		public boolean used;
		public double px,py;
		public int width,height;
	}
}
