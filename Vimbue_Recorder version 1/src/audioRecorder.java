package src;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class audioRecorder implements ActionListener{

	JFrame frame = new JFrame("Recording audio");
	JPanel panel = new JPanel();
	JPanel bttnPanel = new JPanel();
	JLabel label = new JLabel("");
	JButton start = new JButton("Record");
	JButton pause = new JButton("Pause");
	JButton stop = new JButton("Finish");
	JButton restart = new JButton("Restart");
	JProgressBar bar = new JProgressBar(0, 100);
	String state = new String();
	String name = new String();
	int takes = 0;
	public static BufferedWriter out;   //Writing to text file
	JavaSoundRecorder sound = new JavaSoundRecorder();
	double ms = 0; //microseconds
	double duration;
	String finalname = new String();
	
	
	/*******************Timer***************************/
	ActionListener updateSeekBar = new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(state == "record"){
					//
					ms += 50;
				} else if(state == "pause"){
					//
				} else if(state == "stop"){
					//
				}
				if((ms/1000) >= duration){
					stop.doClick();
					bar.setValue(100);
				} else{
					bar.setValue((int) ((ms/10)/duration));
				}
			}
		};
	
	Timer timer = new Timer(50 ,updateSeekBar);         //Initiate taskPerformer every 50ms.
	
	
	/*********************Frame*****************/
	public audioRecorder(String filename, double target){
	finalname = filename;
	duration = target;
	File file = new File("audios/");
		String[] myFiles;
			if(file.isDirectory()){
				myFiles = file.list();
				for(int i=0;i<myFiles.length;i++){
					File myFile = new File(file,myFiles[i]);
					myFile.delete();
				}
			}
	
	try{
		out = new BufferedWriter(new FileWriter("audios/audios.txt"));
	}catch(IOException ex){}
	
		panel.setLayout(new BorderLayout());
		frame.setLocation(10,10);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setSize(460,280);
		bttnPanel.setLayout(new GridLayout(2, 2, 15, 15));
		panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 40, 20, 40), new EmptyBorder(0,0,0,0)));
		bttnPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 40, 20, 40), new EmptyBorder(20,20,20,20)));
		bttnPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		start.setToolTipText("Start Recording Audio"); 
		start.setActionCommand("start");
		start.addActionListener(this);
		restart.setToolTipText("Restart Recording Audio"); 
		restart.setActionCommand("restart");
		restart.addActionListener(this);
		stop.setToolTipText("Finish Recording Audio"); 
		stop.setActionCommand("stop");
		stop.addActionListener(this);
		pause.setToolTipText("Pause Recording Audio"); 
		pause.setActionCommand("pause");
		pause.addActionListener(this);
		pause.setEnabled(false);
		bar.setValue(0);
		panel.add(bar, BorderLayout.NORTH);
		bttnPanel.add(start);
		bttnPanel.add(pause);
		bttnPanel.add(stop);
		bttnPanel.add(restart);
		panel.add(bttnPanel, BorderLayout.CENTER);
		panel.add(label, BorderLayout.SOUTH);
		panel.add(new JLabel(new ImageIcon(new ImageIcon("images/mic.png").getImage().getScaledInstance(30, 40, java.awt.Image.SCALE_SMOOTH))), BorderLayout.EAST);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.add(panel);
		frame.setVisible(true);
		
	}
	
	public static void main(String[] args){
		new audioRecorder(args[0], Long.parseLong(args[1]));
	}
	
	/**
	**Following method actionPerformed() handles all the events triggered on using buttons of the whole window...
	**All actions related to buttons can be found here..
	**/
	public void actionPerformed(ActionEvent e){
	
	
		if ("start".equals(e.getActionCommand())) {
			takes++;
			if(takes==1){
				timer.start();
			}
			name = new String("audio"+takes);
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					sound.start("audios/"+name+".wav");
				}
			});
			stopper.start();
			state = "record";
			pause.setEnabled(true);
			label.setText("Recording....");
			try{
				System.out.println(""+name+".wav");
				out.write("file './"+name+".wav'\n");
			}catch(IOException ex){System.out.println("Error: "+ex);}
			
		
		} else if ("restart".equals(e.getActionCommand())){
		
			sound.finish();
			timer.stop();
			state = "null";
			pause.setEnabled(true);
			stop.setEnabled(true);
			start.setEnabled(true);
			timer = new Timer(50 , updateSeekBar);         //Initiate taskPerformer every 50ms.
			takes = 0;
			ms = 0;
			bar.setValue(0);
			label.setText("Recording Discarded...");
			File file = new File("audios/");
			String[] myFiles;
			if(file.isDirectory()){
				myFiles = file.list();
				for(int i=0;i<myFiles.length;i++){
					File myFile = new File(file,myFiles[i]);
					myFile.delete();
				}
			}
			try{
				out = new BufferedWriter(new FileWriter("audios/audios.txt"));
			}catch(IOException ex){}
			timer.start();
		
		} else if ("pause".equals(e.getActionCommand())) {
			state = "pause";
			sound.finish();
			pause.setEnabled(false);
			label.setText("Recording Paused....");
		
		} else if ("stop".equals(e.getActionCommand())) {
			
			state = "stop";
			sound.finish();
			pause.setEnabled(false);
			start.setEnabled(false);
			stop.setEnabled(false);
			try{
			out.close();
			Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-f", "concat", "-i", "audios/audios.txt", "-c", "copy", "allfiles/"+finalname+".wav"});
			label.setText("Recording Saved as "+finalname+".wav....");
			} catch (Exception ex){
				System.out.println("Error :"+ex);
			}
			timer.stop();   //Stop the timer.
			
		}
		
	}//End of action listener
	
}//end of class audioRecorder