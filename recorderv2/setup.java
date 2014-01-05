

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import org.apache.commons.io.FileUtils;
import java.io.File;

import javax.swing.JComponent;
import java.io.File;

public class setup{

		public static void main(String[] args){
			
			JFrame frame = new JFrame("Setting up Recorder");
			JProgressBar bar = new JProgressBar(0, 100);
			frame.add(bar);
			bar.setValue(0);
			String dir = new getDir();
			while(dir!=null){
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File("."));
				fc.setDialogTitle("Please select an audio file");
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if( fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
				{
					dir = fc.getSelectedFile().getAbsolutePath();
				}
			
			}
			bar.setValue(0);
			File file = new File(dir+"Vimbue-Recorder/");
			while(!file.exists()){
				File trgDir = new File("./Vimbue-Recorder/");
				File srcDir = new File(dir);
				FileUtils.copyDirectory(srcDir, trgDir);
			}
			dir = dir+"Vimbue-Recorder/";
			bar.setValue(60);
			file = new File(dir+"/allfiles/");
			if(!file.exists()){new File(dir+"allfiles/").mkdirs();}
			bar.setValue(70);
			file = new File(dir+"/audios/");
			if(!file.exists()){new File(dir+"audios/").mkdirs();}
			bar.setValue(80);
			file = new File(dir+"/inFiles/");
			if(!file.exists()){new File(dir+"inFiles/").mkdirs();}
			bar.setValue(90);
			file = new File(dir+"/thumbnails/");
			if(!file.exists()){new File(dir+"thumbnails/").mkdirs();}
			bar.setValue(100);
			Process p = Runtime.getRuntime().exec(new String[]{"java", "-jar", dir+"/rec.jar"});
			
		}
		
}