/**
**Class : playWindow
**
**Date : 25/07/2013
**
**@author : Sumedh Masulkar
**
**@version : 1.0 by Sumedh Masulkar
**
**/
package src;

import java.awt.Dimension;
import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.io.File;
import java.net.URI;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.net.MalformedURLException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.Arrays;
import javax.swing.filechooser.FileFilter;
import javax.media.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.awt.GraphicsDevice.WindowTranslucency.*;


/**
*	This class is the controller of whole application.
**/


public class playWindow extends JFrame implements ActionListener{
	
	/*
	Variables needed in whole class...
	*/
	JFrame frame = new JFrame("Vimbue's Editing Tool");				//Open window.
	
	//Panels
	JPanel pnlBttns = new JPanel();
	JPanel editPanel = new JPanel();
	JPanel cutPanel = new JPanel();
	JPanel trimPanel = new JPanel();
	JPanel audioPanel = new JPanel();
	JPanel uploadPanel = new JPanel();
	JPanel welcome = new JPanel();
	JPanel f = new JPanel();
	JPanel head = new JPanel();
	JPanel info = new JPanel();
	
	//Buttons
	JButton button1 = new JButton("Edit Video", new ImageIcon(new ImageIcon("images/edit_video.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton button2 = new JButton("Upload Video", new ImageIcon(new ImageIcon("images/upload_video.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton button3 = new JButton("My Videos");
	JButton cut = new JButton("Cut", new ImageIcon(new ImageIcon("images/cut.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton cut1 = new JButton("Cut Now", new ImageIcon(new ImageIcon("images/cut.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton trim1 = new JButton("Trim Now", new ImageIcon(new ImageIcon("images/trim.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton change1 = new JButton("Change Duration2");
	JButton change2 = new JButton("Change Duration2");
	JButton trim = new JButton("Trim", new ImageIcon(new ImageIcon("images/trim.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton audio = new JButton("Audio", new ImageIcon(new ImageIcon("images/audio.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton done = new JButton("Done", new ImageIcon(new ImageIcon("images/done.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton done1 = new JButton("Go Back", new ImageIcon(new ImageIcon("images/done.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton done2 = new JButton("Go Back", new ImageIcon(new ImageIcon("images/done.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton done3 = new JButton("Go Back", new ImageIcon(new ImageIcon("images/done.png").getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH)));
	JButton play = new JButton();
	JButton stop = new JButton();
	JButton insert = new JButton("Insert Audio"); //Unimplemented and not added to panel.
	JButton replace = new JButton("Replace Audio");
	JButton record = new JButton("Record Audio");
	
	//Labels
	JLabel jlabel = new JLabel("Showing last recorded video");
	JLabel duration1 = new JLabel();
	JLabel duration2 = new JLabel();
	JLabel duration3 = new JLabel();
	JLabel duration4 = new JLabel();
	ImageIcon image = new ImageIcon(new ImageIcon("images/info.png").getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
	JLabel info_img = new JLabel("", image, JLabel.CENTER);
	JLabel info_text = new JLabel("<html>Cut will keep <br>the video between<br> selected durations,<br> Trim will <br>discard the video<br>between selected<br>durations.");
	JLabel temp_label = new JLabel();
	JLabel temp_label2 = new JLabel();
	JLabel temp_label3 = new JLabel();
	
	Player player;
	//Seekbar
	JProgressBar progressBar = new JProgressBar(0, 100);
	
	//Additional Variables
	int sec, minutes, hour;
	String duration = new String("00:00");
	String time = new String("00:00");
	String state = new String("wait");
	String get = new String("d1");
	String string1, string2, name;
	String filename;
	String action = new String("play");
	double d1=0.0,d2=0.0,d3=0.0,d4=0.0,temp_double;
	long total;
	long current;
	int index;
	
	
	/*   End Declaration of variables   */
	
	/////////////
	
	ActionListener updateSeekBar = new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				if(state == "wait"){/////////////////////
					//Do nothing
				} else if(state == "play"){/////////////
					sec = (int) player.getMediaTime().getSeconds();
					current = (long) player.getMediaTime().getNanoseconds();
					time = secToString(sec);
					progressBar.setString(time+"/"+duration);
					index = (int) ((current*100)/total);
					progressBar.setValue(index);
					if(index==100){
						progressBar.setValue(0);
						progressBar.setString("0:0/"+duration);
						player.setMediaTime(new Time(0));
						play.setToolTipText("Start Playing Video"); 
						play.setActionCommand("play");
						ImageIcon pause = new ImageIcon(new ImageIcon("images/play_button.gif").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
						play.setIcon(pause);
					}
					//update
				} else if(state == "pause"){
					play.setToolTipText("Click this button to play video.");
					ImageIcon pause = new ImageIcon(new ImageIcon("images/play_button.gif").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
					play.setIcon(pause);
					player.stop();
					play.setActionCommand("play");					//Change buttons action to pause
				} else if(state == "stop"){
					//
				} 
			}
		};
	
	Timer timer = new Timer(50 ,updateSeekBar);         //Initiate taskPerformer every 50ms.
	
	//////////////////////////////
	
    public playWindow(String args){
		//Customize Window to record.
		frame.setLayout(new BorderLayout());
		frame.setLocation(10,10);
        frame.setSize(1200,700);
		frame.setIconImage(new ImageIcon("./icon.jpg").getImage());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	// ****On closing window event
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, 
					"Are you sure you want to exit the program?\nAll unsaved changes may be lost.", "Confirm Action",
					JOptionPane.YES_NO_OPTION);

					if (confirmed == JOptionPane.YES_OPTION) {
					  System.exit(0);
					}
			}
		});		
		// *****Window close event handled above.
		
		//Paddings
		editPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(30, 10, 30, 10), new EmptyBorder(0,0,0,0)));
		cutPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(30, 10, 90, 10), new EtchedBorder()));
		trimPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(30, 10, 90, 10), new EtchedBorder()));
		audioPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(30, 10, 90, 10), new EtchedBorder()));
		info.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), new EtchedBorder()));
		uploadPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(30, 10, 30, 10), new EmptyBorder(0,0,0,0)));
		pnlBttns.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(80, 10, 340, 10), new EmptyBorder(0,0,0,0)));
		jlabel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(10, 10, 0, 10), new EmptyBorder(0,0,0,0)));
		
		//Set commands for Buttons 
		pnlBttns.setLayout(new BoxLayout(pnlBttns, BoxLayout.Y_AXIS));
		button1.setToolTipText("Start Editing Video"); 
		button1.setActionCommand("edit");
		button1.addActionListener(this);
		button2.setToolTipText("Upload this Video"); 
		button2.setActionCommand("upload");
		button2.addActionListener(this);
		button3.setToolTipText("Browse all your videos"); 
		button3.setActionCommand("videos");
		button3.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				new myVideos();
				frame.dispose();
			} 
		});
		pnlBttns.add(button1);
		pnlBttns.add(Box.createRigidArea(new Dimension(0, 60)));
		pnlBttns.add(button2);
		pnlBttns.add(Box.createRigidArea(new Dimension(0, 60)));
		pnlBttns.add(button3);
		frame.add(pnlBttns, BorderLayout.EAST);
		
		//create edit panel
		editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
		done.setToolTipText("Go back");
		done.setActionCommand("done");
		done.addActionListener(this);
		cut.setToolTipText("Cut Video into parts");
		cut.setActionCommand("cut");
		cut.addActionListener(this);
		trim.setToolTipText("Remove parts of Video");
		trim.setActionCommand("trim");
		trim.addActionListener(this);
		audio.setToolTipText("Edit audio of Video");
		audio.setActionCommand("audio");
		audio.addActionListener(this);
		editPanel.add(audio);
		editPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		editPanel.add(cut);
		editPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		editPanel.add(trim);
		editPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		editPanel.add(done);
		editPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
		info.add(info_img);
		info.add(info_text);
		editPanel.add(info);
		
		//Create cut Panel.
		cutPanel.setLayout(new BoxLayout(cutPanel, BoxLayout.Y_AXIS));
		cutPanel.add(duration1);
		cutPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		cutPanel.add(duration2);
		duration1.setText("Duration 1:  00.00                     ");
		duration2.setText("Duration 2:                            ");
		change1.setToolTipText("Change duration2");
		change1.setActionCommand("duration2");
		change1.addActionListener(this);
		cutPanel.add(Box.createRigidArea(new Dimension(0, 60)));
		cutPanel.add(change1);
		cutPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		cut1.setToolTipText("Cut this video between above mentioned durations");
		cut1.setActionCommand("cut1");
		cut1.addActionListener(this);
		cutPanel.add(cut1);
		cutPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		done1.setToolTipText("Go back");
		done1.setActionCommand("done1");
		done1.addActionListener(this);
		cutPanel.add(done1);
		cutPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		cutPanel.add(temp_label);
		
		//Create trim Panel.
		trimPanel.setLayout(new BoxLayout(trimPanel, BoxLayout.Y_AXIS));
		trimPanel.add(duration3);
		trimPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		trimPanel.add(duration4);
		duration3.setText("Duration 1: 00.00                      ");
		duration4.setText("Duration 2:                            ");
		change2.setToolTipText("Change duration2");
		change2.setActionCommand("duration4");
		change2.addActionListener(this);
		trimPanel.add(Box.createRigidArea(new Dimension(0, 60)));
		trimPanel.add(change2);
		trimPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		trim1.setToolTipText("Trim this video between above mentioned durations");
		trim1.setActionCommand("trim1");
		trim1.addActionListener(this);
		trimPanel.add(trim1);
		trimPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		done2.setToolTipText("Go back");
		done2.setActionCommand("done2");
		done2.addActionListener(this);
		trimPanel.add(done2);
		trimPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		trimPanel.add(temp_label2);
		
		//Create trim Panel.
		audioPanel.setLayout(new BoxLayout(audioPanel, BoxLayout.Y_AXIS));
//		audioPanel.add(insert);
		audioPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		audioPanel.add(replace);
		audioPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		audioPanel.add(record);
		audioPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		// insert.setToolTipText("Insert new audio between some durations");
		// insert.setActionCommand("insert");
		// insert.addActionListener(this);
		replace.setToolTipText("Replace audio of the entire video");
		replace.setActionCommand("replace");
		replace.addActionListener(this);
		record.setToolTipText("Record a new audio file to insert or replace.");
		record.setActionCommand("record");
		record.addActionListener(this);
		done3.setToolTipText("Go back");
		done3.setActionCommand("done3");
		done3.addActionListener(this);
		audioPanel.add(done3);
		audioPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		audioPanel.add(temp_label3);
		
		//create upload panel.
		/*Nothing*/
		
		addVideo(args);
	}

	public void addVideo(String args)
        {	head.setLayout(new GridBagLayout());
			
			jlabel.setFont(new Font("Verdana",1,20));
			head.add(jlabel, new GridBagConstraints());
            f.setLayout(new BorderLayout());
			f.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			//file you want to play
            try{
			
				filename = new File("allfiles/"+args).getAbsolutePath();
				System.out.println("file:///"+filename);
				MediaLocator mediaURL = new MediaLocator("file:///"+filename);
				Player mediaPlayer = Manager.createRealizedPlayer(mediaURL);
				jlabel.setText("Playing "+filename);
				
				/*************** Add Video Panel *********************/
				
				//get components for video and playback controls
				Component video = mediaPlayer.getVisualComponent();
				minutes = 0;
				hour = 0;
				sec = (int) mediaPlayer.getDuration().getSeconds();
				total = (long) mediaPlayer.getDuration().getNanoseconds();
				if(sec>=60){
					minutes = (int) sec/60;
					sec = sec % 60;
				}
				if(minutes>=60){
					hour = (int) minutes/60;
					minutes = minutes % 60;
				}
				if(hour>0){
					duration = Integer.toString(hour)+":"+Integer.toString(minutes)+":"+Integer.toString(sec);
				} else{
					duration = Integer.toString(minutes)+":"+Integer.toString(sec);
				}
				mediaPlayer.setStopTime(mediaPlayer.getDuration());
				
				/**************** Controller Panel ********************/
				JPanel controls = new JPanel(new FlowLayout());
				ImageIcon play2 = new ImageIcon(new ImageIcon("images/play.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
				ImageIcon play1 = new ImageIcon(new ImageIcon("images/play_button.gif").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
				
				//Play button
				play = new JButton(play2);
				play.setToolTipText("Start Playing Video"); 
				play.setActionCommand("play");
				play.addActionListener(this);
				play.setPreferredSize(new Dimension(35,30));
				play.setIcon(play1);
				play.setDisabledIcon(play2);
				play.setPressedIcon(play2);
				play.setSelectedIcon(play2);
				
				
				//Stop button
				stop = new JButton(new ImageIcon(new ImageIcon("images/stop.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
				stop.setToolTipText("Stop Playing Video"); 
				stop.setActionCommand("stop");
				stop.addActionListener(this);
				stop.setPreferredSize(new Dimension(35,30));
				
				//ProgressBar
				progressBar.setStringPainted(true);
				progressBar.setString("0:0/"+duration);
				progressBar.setPreferredSize(new Dimension(600,30));
				progressBar.addMouseListener(new MouseAdapter() {            
												   public void mouseClicked(MouseEvent e) {
														int mouseX = e.getX();//Computes how far along the mouse is relative to the component width
														int val = (int)Math.round(((double)mouseX / (double)progressBar.getWidth()) * progressBar.getMaximum());
														progressBar.setValue(val);
														double tim = val * player.getDuration().getSeconds()/100;
														player.setMediaTime(new Time(tim));
														if(action=="cut" || action =="trim"){
															state = "pause";
														}
														if(get=="d1"){
															d1 = tim;
															tim = (Math.round(tim*1000))/1000.0;
															duration1.setText("Duration1: "+tim+"    ");
														} else if(get=="d2"){
															d2 = tim;
															tim = (Math.round(tim*1000))/1000.0;
															duration2.setText("Duration2: "+tim+"    ");
														} else if(get=="d3"){
															d3 = tim;
															tim = (Math.round(tim*1000))/1000.0;
															duration3.setText("Duration1: "+tim+"    ");
														} else if(get=="d4"){
															d4 = tim;
															tim = (Math.round(tim*1000))/1000.0;
															duration4.setText("Duration2: "+tim+"    ");
														}
														sec = (int) tim;
														time = secToString(sec);
														progressBar.setString(time+"/"+duration);
												   }
												});
		
				player = mediaPlayer;
				controls.add(play);
				controls.add(stop);
				controls.add(progressBar);
				progressBar.setValue(0);
				f.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 10, 20, 10), new EtchedBorder()));
				f.add(video,BorderLayout.CENTER);
				f.add(controls,BorderLayout.SOUTH);
			} catch (Exception e) {
				Component[] com = pnlBttns.getComponents();  
				for (int a = 0; a < com.length; a++) {  
					 com[a].setEnabled(false);  
				} 
				jlabel.setText("Video not compatible");
				button3.setEnabled(true);
			}
			//////
			frame.add(f, BorderLayout.CENTER);
			frame.add(head, BorderLayout.PAGE_START);
			frame.setDefaultLookAndFeelDecorated(false);
			frame.setVisible(true);
        }

	
	public String secToString(int sec){
		String time = new String("a");
	    if(sec>=60){
					minutes = (int) sec/60;
					sec = sec % 60;
		}
		if(minutes>=60){
					hour = (int) minutes/60;
					minutes = minutes % 60;
		}
		if(hour>0){
					time = Integer.toString(hour)+":"+Integer.toString(minutes)+":"+Integer.toString(sec);
		} else{
					time = Integer.toString(minutes)+":"+Integer.toString(sec);
		}
		return time;
	}
	
	/************************MAIN FUNCTION HERE**********************************************/
	
	
    public static void main(String[] args){
        new playWindow(args[0]);
    }
	
	
	/**^^^****************************************END OF MAIN()****************************************************/
	
	/**
	**Following method actionPerformed() handles all the events triggered on using buttons of the whole window...
	**All actions related to buttons can be found here..
	**/
	
	public void actionPerformed(ActionEvent e){
	
	
		if ("edit".equals(e.getActionCommand())) {
		
			state = "edit";
			frame.remove(pnlBttns);
			frame.add(editPanel, BorderLayout.EAST);
			frame.revalidate();
			frame.repaint();
		
		} else if ("upload".equals(e.getActionCommand())) {	
		
			state = "upload";
		try{	URI uri = new URI("http://vimbue.com/sumedh/coach/video/upload/");
			Desktop.getDesktop().browse(uri.resolve(uri));} catch(Exception ex){}
		
		} else if ("play".equals(e.getActionCommand())) {			//Given Sized screenshot.
		
			play.setToolTipText("Click this button to pause video.");
			ImageIcon pause = new ImageIcon(new ImageIcon("images/pause.png").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
			play.setIcon(pause);
			player.start();
			state = "play";
			play.setActionCommand("pause");					//Change buttons action to pause
			System.out.println("Video Started");
			timer.start();
			
		} else if ("pause".equals(e.getActionCommand())) {			//Given Sized screenshot.
		
			state = "pause";
			System.out.println("Video Paused");
			
		} else if ("stop".equals(e.getActionCommand())) {			//Given Sized screenshot.
				
				state = "stop";
				System.out.println("Video Stopped");
				progressBar.setValue(0);
				player.stop();
				progressBar.setString("0:0/"+duration);
				player.setMediaTime(new Time(0));
				play.setToolTipText("Start Playing Video"); 
				play.setActionCommand("play");
				ImageIcon pause = new ImageIcon(new ImageIcon("images/play_button.gif").getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH));
				play.setIcon(pause);
				
		} else if ("done".equals(e.getActionCommand())){
		
				frame.remove(editPanel);
				frame.add(pnlBttns, BorderLayout.EAST);
				frame.revalidate();
				frame.repaint();
		
		}  else if ("cut".equals(e.getActionCommand()) || "trim".equals(e.getActionCommand()) ||"audio".equals(e.getActionCommand())){
		
				progressBar.setCursor(new Cursor(Cursor.TEXT_CURSOR));
				frame.remove(editPanel);
				if("cut".equals(e.getActionCommand())){
					frame.add(cutPanel, BorderLayout.EAST);
					action = "cut";
					get = "d1";
				} else if("trim".equals(e.getActionCommand())) {
					frame.add(trimPanel, BorderLayout.EAST);
					action = "trim";
					get = "d3";
				} else {
					frame.add(audioPanel, BorderLayout.EAST);
					action = "audio";
					get = "d5";
				}
				frame.revalidate();
				frame.repaint();
			
		} else if ("cut1".equals(e.getActionCommand())){
		
				System.out.println("cutting");
				try{
					if(d1<60){
						string1 = new String("00:00:"+d1);
					} else{
						string1 = new String(""+secToString((int)d1)+(d1-(int)d1));
					}
					if(d2<60){
						string2 = new String("00:00:"+d2);
					}
					name = new String(new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
					Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "\""+filename+"\"", "-ss", string1, "-t", string2, "-vcodec", "copy", "-acodec", "copy", "allfiles/"+name+".mov"});
					System.out.println("A new command for ffmpeg ------>ffmpeg -i \""+filename+"\" -ss "+string1+" -t "+string2+" -vcodec copy -acodec copy allfiles/"+name+".mov");
					File file = new File("./allfiles/"+name+".mov");
					while(!file.canWrite()) {
						// windows still working on it.
						try{Thread.sleep(100);}catch(InterruptedException ex){}
					}
					Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "allfiles/"+name+".mov", "allfiles/"+name+".mov"});
					temp_label.setText("<html>New Video<br>saved as:  "+name+".mov</html>");
					Runtime.getRuntime().exec("lib/ffmpeg -i allfiles/"+name+".mov -ss "+(player.getDuration().getSeconds()/2.0)+" -f image2 -vframes 1 -s 320x240 thumbnails/"+name+".jpg");
				}
				catch (Exception ex){System.out.println("Cannot cut: "+ex);}
				System.out.println("cutting done");
				
				
		}  else if ("trim1".equals(e.getActionCommand())){
		
				System.out.println("trimming");
				try{
					if(d1<60){
						string1 = new String("00:00:"+d3);
						if(d3==0.0){string1 = "00:00.01";}
					} else{
						string1 = new String(""+secToString((int)d3)+(d3-(int)d3));
					}
					if(d2<60){
						string2 = new String("00:00:"+d4);
					}
					name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
					temp_double = player.getDuration().getSeconds();
					Runtime.getRuntime().exec(new String[]{"\"lib/ffmpeg\"", "-i", "\""+filename+"\"", "-ss", "00:00:00", "-t", string1, "-vcodec", "copy", "-acodec", "copy", "allfiles/temp1.mov"});
					System.out.println("Forming temp1 :" +Arrays.toString(new String[]{"\"lib/ffmpeg\"", "-i", "\""+filename+"\"", "-ss", "00:00:00", "-t", string1, "-vcodec", "copy", "-acodec", "copy", "allfiles/temp1.mov"}));
			
					File file = new File("./allfiles/temp1.mov");
					while(!file.canWrite()) {
						// windows still working on it.
						try{Thread.sleep(100);}catch(InterruptedException ex){}
					}
					Runtime.getRuntime().exec(new String[]{"\"lib/ffmpeg\"", "-i", "\""+filename+"\"", "-ss", string2, "-t", temp_double+"", "-vcodec", "copy", "-acodec", "copy", "allfiles/temp2.mov"});
					System.out.println("Forming temp2: "+Arrays.toString(new String[]{"\"lib/ffmpeg\"", "-i", "\""+filename+"\"", "-ss", string2, "-t", temp_double+"", "-vcodec", "copy", "-acodec", "copy", "allfiles/temp2.mov"}));
					file = new File("./allfiles/temp2.mov");
					while(!file.canWrite()) {
						// windows still working on it.
						try{Thread.sleep(100);}catch(InterruptedException ex){}
					}
					Runtime.getRuntime().exec(new String[]{"\"lib/ffmpeg\"", "-f", "concat", "-i", "concat.txt", "-c", "copy", "allfiles/"+name+".mov"});
					System.out.println("A new command for ffmpeg to trim------>ffmpeg -f concat -i concat.txt -c copy allfiles/"+name+".mov");
					file = new File("./allfiles/"+name+".mov");
					while(!file.canWrite()) {
						// windows still working on it.
						try{Thread.sleep(100);}catch(InterruptedException ex){}
					}
					Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "allfiles/" +name+".mov", "allfiles/"+name+".mov"});
					new File("allfiles/temp1.mov").delete();
					new File("allfiles/temp2.mov").delete();
					temp_label2.setText("<html>New Video<br>saved as:  "+name+".mov</html>");
					Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "allfiles/"+name+".mov", "-ss", ""+(player.getDuration().getSeconds()/2.0), "-f", "image2", "-vframes", "1", "-s", "320x240", "thumbnails/"+name+".jpg"});
				}
				catch (Exception ex){System.out.println("Cannot trim: "+ex);}
				System.out.println("trimming done");
				
				
		} else if ("done1".equals(e.getActionCommand()) || "done2".equals(e.getActionCommand()) ||"done3".equals(e.getActionCommand())){
				
				action = "play";
				state = "edit";
				progressBar.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				if("done1".equals(e.getActionCommand())){
					frame.remove(cutPanel);
				} else if("done2".equals(e.getActionCommand())){
					frame.remove(trimPanel);
				} else {
					frame.remove(audioPanel);
				}
				frame.add(editPanel, BorderLayout.EAST);
				frame.revalidate();
				frame.repaint();
			
		} else if ("duration1".equals(e.getActionCommand())){
			get = "d1";
			change1.setToolTipText("Change duration2");
			change1.setActionCommand("duration2");
			change1.addActionListener(this);
			change1.setText("Change Duration2");
		
		} else if ("duration2".equals(e.getActionCommand())){
			get = "d2";
			change1.setToolTipText("Change duration1");
			change1.setActionCommand("duration1");
			change1.addActionListener(this);
			change1.setText("Change Duration1");
		} else if ("duration3".equals(e.getActionCommand())){
			get = "d3";
			change2.setToolTipText("Change duration1");
			change2.setActionCommand("duration4");
			change2.addActionListener(this);
			change2.setText("Change Duration2");
		} else if ("duration4".equals(e.getActionCommand())){
			get = "d4";
			change2.setToolTipText("Change duration1");
			change2.setActionCommand("duration3");
			change2.addActionListener(this);
			change2.setText("Change Duration1");
		} else if ("replace".equals(e.getActionCommand())){
			//
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.setDialogTitle("Please select an audio file");
			fc.setFileFilter(new fileFilter());
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if( fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
			{
				string1 = fc.getSelectedFile().getAbsolutePath();
			}
			if(string1.endsWith(".wav") || string1.endsWith(".mp3")){
				try{
				name = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
				Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "\""+filename+"\"", "-i", "\""+string1+"\"", "-map", "0", "-map", "1", "-codec", "copy", "-shortest", "./allfiles/"+name+".mov"});
				System.out.println("newcommand--->ffmpeg -i \""+filename+"\" -i "+string1+" -map 0 -map 1 -codec copy -shortest ./allfiles/"+name+".mov");
				File file = new File("./allfiles/"+name+".mov");
				System.out.println(file.getAbsolutePath());
					while(!file.canWrite()) {
						// windows still working on it.
						try{Thread.sleep(100);}catch(InterruptedException ex){}
					}
				Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "allfiles/" +name+".mov", "allfiles/"+name+".mov"});
				temp_label3.setText("<html>New Video<br>saved as:  "+name+".mov</html>");
				Runtime.getRuntime().exec(new String[]{"lib/ffmpeg", "-i", "allfiles/"+name+".mov", "-ss", ""+(player.getDuration().getSeconds()/10.0), "-f", "image2", "-vframes", "1", "-s", "320x240", "thumbnails/"+name+".jpg"});
				} catch(Exception ex){
					temp_label3.setText("<html>Java Exception<br>Error occured,<br>Check if audio<br>file is valid,<br>or try again.</html>");
					System.out.println("Error:   "+ex);
				}
			} else {
				temp_label3.setText("<html>Invalid file,<br>cannot be used:<br>"+ new File(string1).getName());
			}
			
			
		} else if ("record".equals(e.getActionCommand())){
			//
			name = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
			new audioRecorder(name, player.getDuration().getSeconds());
			
		}
		
	}
	
	

	
}	//End of class editingWindow() here.

class fileFilter extends FileFilter{
	
	public boolean accept(File f){
		return (f.isDirectory() || f.getName().toLowerCase().endsWith(".wav") || f.getName().toLowerCase().endsWith(".mp3"));
	}
	public String getDescription(){
		return "Please select .wav or .mp3 files only";
	}
	
}
/********************************* END OF FILE******************************************************************************/
