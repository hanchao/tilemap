package com.sogou.tilemap;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
public class MainFrame extends Frame {
	
	MapView mapView = new MapView();
	Point pntDown;
	
    public static void main(String args[]) { new MainFrame(); }
    public MainFrame() {
        setSize(800,600);
        setVisible(true);
        mapView.setCenter(117,39);
        mapView.init(this);
        mapView.setZoom(1);
        
        addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				mapView.exit();
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        addMouseWheelListener(new MouseWheelListener(){

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getWheelRotation()<0){
					mapView.setZoom(mapView.getZoom()+1);
				}else{
					mapView.setZoom(mapView.getZoom()-1);
				}
				invalidate();
				repaint();
			}
        	
        });
        addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				pntDown = arg0.getPoint();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Point pntUp = arg0.getPoint();
				mapView.move(pntDown.x - pntUp.x,pntDown.y - pntUp.y);
				invalidate();
				repaint();
			}
        	
        });      
    }
    public void paint(Graphics g) {
		Image bufferImage = createImage(getWidth(),getHeight());
		Graphics bufferGraphics = bufferImage.getGraphics();
    	mapView.paint(bufferGraphics);
    	g.drawImage(bufferImage,0,0,null);
    	
    }
}
