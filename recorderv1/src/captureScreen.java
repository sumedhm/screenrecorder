/**
**vimbueRecorder
**
**Class : captureScreen
**
**Date : 18/06/2013
**
**@author : Sumedh Masulkar
**
**/

package src;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.*;

public class captureScreen extends Object{
    
        public captureScreen(String size, Rectangle rect, String name) {

			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle screenRectangle = rect;
			String filename;
            filename = "./inFiles/" + name + ".jpeg";
			System.out.println("Frame - " + name);
            try{Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(screenRectangle);
            
			/*JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			int option = chooser.showSaveDialog(null);
			if(option == JFileChooser.APPROVE_OPTION){
				 System.out.println(chooser.getSelectedFile().getAbsolutePath());
			}
			*/
			Image cursor = ImageIO.read(new File("./cursor.gif"));
			int x = MouseInfo.getPointerInfo().getLocation().x - rect.getLocation().x ;
			int y = MouseInfo.getPointerInfo().getLocation().y - rect.getLocation().y ;

			Graphics2D graphics2D = image.createGraphics();
			graphics2D.drawImage(cursor, x, y, 16, 16, null); // cursor.gif is 16x16 size.
            ImageIO.write(image, "jpeg", new File(filename));
			}
            catch(Exception e)
            {
                System.out.println("Error"+e);
            }
        }
        
        public static void main(String args, Rectangle rect, String name){
            new captureScreen(args,rect,name);
        }

    }
