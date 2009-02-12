package video.objectTracking;

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
import marvin.image.MarvinImageMask;
import marvin.plugin.MarvinPluginImage;
import marvin.util.MarvinAttributes;
import marvin.util.MarvinPluginLoader;
import marvin.video.MarvinVideoManager;

/**
 * Object tracking by color pattern
 * @author Gabriel Ambrosio Archanjo
 */

public class ObjectTracking extends JFrame implements Runnable{

	private MarvinVideoManager	videoManager;
	private MarvinImagePanel 	videoPanel;
	
	private Thread 				thread;
	
	private MarvinImage 		imageIn, 
								imageOut;
	
	private JPanel				panelSlider;
	
	private JSlider				sliderSensibility;
	
	private JLabel				labelSlider;
	
	private MarvinPluginImage	pluginImage;
	private MarvinAttributes	attributesOut;
	
	private int					sensibility=30;
	
	private boolean				regionSelected=false;
	private int[]				arrInitialRegion;
	
	public ObjectTracking(){
		videoPanel = new MarvinImagePanel();
		videoManager = new MarvinVideoManager(videoPanel);	
		videoManager.connect();
		
		loadGUI();
		
		pluginImage = MarvinPluginLoader.loadPluginImage("net.sourceforge.marvinproject.pattern.findColorPattern.jar");
		
		attributesOut = new MarvinAttributes();

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
			
			if(regionSelected){
				pluginImage.setAttribute("differenceColorRange", sensibility);
				pluginImage.process(imageIn, imageOut, attributesOut, MarvinImageMask.NULL_MASK, false);
				
				imageOut.drawRect
				(
					(Integer)attributesOut.get("regionPx"), 
					(Integer)attributesOut.get("regionPy"),
					(Integer)attributesOut.get("regionWidth"),
					(Integer)attributesOut.get("regionHeight"),
					Color.red
				);
			}
			videoManager.updatePanel();
		}
	}
	
	public static void main(String args[]){
		ObjectTracking l_objectTracking = new ObjectTracking();
		l_objectTracking.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				if(arrInitialRegion == null){
					arrInitialRegion = new int[]{a_event.getX(), a_event.getY(),0,0};
				}
				else{
					arrInitialRegion[2] = a_event.getX()-arrInitialRegion[0];
					arrInitialRegion[3] = a_event.getY()-arrInitialRegion[1];
					
					pluginImage.setAttribute("regionPx", arrInitialRegion[0]);
					pluginImage.setAttribute("regionPy", arrInitialRegion[1]);
					pluginImage.setAttribute("regionWidth", arrInitialRegion[2]);
					pluginImage.setAttribute("regionHeight", arrInitialRegion[3]);
					regionSelected = true;
				}	
			}
		}		
	}
}
