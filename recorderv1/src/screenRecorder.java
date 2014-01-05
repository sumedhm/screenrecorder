/**
**Class : screenRecorder
**
**vimbueRecorder
**
**Date : 20/06/2013
**
**@author : Sumedh Masulkar
**
**@version : 1.0 by Sumedh Masulkar
**
**Revised by : Sumedh Masulkar
**/

package src;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.awt.Rectangle;
import java.awt.*;
import java.io.IOException;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.Vector;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JFrame;
import static java.awt.GraphicsDevice.WindowTranslucency.*;
import javax.sound.sampled.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
*	This class is the controller of whole application, this controls the start of, pause of, terminating the recording,
*	then also order the converter to change images captured to movie format...
**/

public class screenRecorder extends JFrame implements ActionListener{
	
	/*
	Variables needed in whole class...
	*/
	
	
	
	public static BufferedWriter out;
	
	public static String size = new String("sized");
	
	String name = new String("named");
	
	public static Rectangle rect = new Rectangle();
	
	int audioFiles = 1;
	
	JPanel pnlBttns = new JPanel();
	
	String state_Of_Recorder = new String("paused");
					/*Possible states.^^^^^^^
						//paused
						//recording
						//stopped
						//converting
						//finished
						*/
	
	JButton button1 = new JButton("Record this Size");
	
	JButton button2 = new JButton("Record Full Screen");
	JButton button3 = new JButton("My Videos");
	String dir = new String();
	
	int frames = 0;
	
	JavaSoundRecorder sound = new JavaSoundRecorder();
	
	/*   End Declaration of variables   */

		/*****
		Swing timer here..............
		********************************************************************************************************/
		
		ActionListener taskPerformer = new ActionListener(){
			public void actionPerformed(ActionEvent evt){
					if(state_Of_Recorder == "paused"){
					
						/*  Do nothing .... */
					
					} else if(state_Of_Recorder == "recording"){
						frames += 1;
						String next_frame = "img"+Integer.toString(frames);
						captureScreen a = new captureScreen(size,rect,next_frame);		/* Take Given sized screenshots*/
					
					} else if(state_Of_Recorder == "stopped"){
						
						/* Dont know what to do */
						
					} else {
					
						/*  Timer should have been stopped already. */
						System.out.println("Timer hasn't been stopped.....error...");
					}
			}
		};
	
		Timer timer = new Timer(50 ,taskPerformer);         //Initiate taskPerformer every 50ms.
	
		/***************************************
		Timer Finished..
		****************************************/
	
    public screenRecorder() {
	
			//Customize Window to record.
        super("Recording Window");				//Open window.
		setBackground(new Color(0,0,0,0));
        setLayout(new GridBagLayout());
        setSize(500,300);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon("./icon.jpg").getImage());

			//Background to record.
		JPanel jp=new JPanel(){
        public void paintComponent(Graphics g)
        {
            Graphics2D g2=(Graphics2D)g;
            Paint gp=new GradientPaint(0, 0, new Color(100,20,210,105), 0, 200, new Color(80,20,40,105));
            g2.setPaint(gp);
            g2.fillRect(0, 0, getWidth(),getHeight());
        }
    };
	
	//Cursor settings in the application ***everywhere***.
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		pnlBttns.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		jp.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		
	//Lay out settings	
	GridBagConstraints c = new GridBagConstraints();
	
	//Customise JPanel jp(the recording panel).
    c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.LINE_START;
			c.gridwidth = 3;
			c.gridheight = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.gridx = 0;
			c.gridy = 1;
			c.ipadx = 0;
    add(jp,c);
	
	
	/* Adding the buttons at top */
	//Customizing panel buttons.
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridwidth = 1;
			c.gridheight = 1;
			c.weightx = 1;
			c.weighty = 0.0;
			c.gridx = 0;
			c.gridy = 0;
			c.ipadx = 0;
		
		pnlBttns.setBackground(Color.blue);         //Background color.

			//Add a recording button.
			/*
			button2.setVerticalTextPosition(AbstractButton.CENTER);
		button2.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
		button2.setMnemonic(KeyEvent.VK_F);
			*/
				button1.setToolTipText("Resize window to your favourable size before hitting this button to start recording."); 
				button1.setActionCommand("record");
				button1.addActionListener(this);
			pnlBttns.add(button1);

			   //Add a button to record full size.
				button2.setToolTipText("Click this button to start recording full screen."); 
				button2.setActionCommand("full_size");
				button2.addActionListener(this);
			pnlBttns.add(button2);
			
			   //Add a button to go to my videos.
				button3.setToolTipText("See your videos"); 
				button3.setActionCommand("videos");
				button3.addActionListener(this);
			pnlBttns.add(button3);
			
			
		add(pnlBttns,c);			//Panel with buttons
		
					/* Done Adding all top buttons */
					
	// ****On closing window event
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, 
					"Are you sure you want to exit the program?\nAll unsaved recordings will be lost.", "Confirm Action",
					JOptionPane.YES_NO_OPTION);

					if (confirmed == JOptionPane.YES_OPTION) {
					  System.exit(0);
					}
			}
		});		
		// ********Window close event handled above.
	
					
	}

	/************************MAIN FUNCTION HERE**********************************************/
	
	
    public static void main(String[] args) throws IOException{
        // Determine if the GraphicsDevice supports translucency.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        //If translucent windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            System.err.println(
                "Translucency is not supported");
                System.exit(0);
        }
        FileWriter audioFile = new FileWriter("./audios/audios.txt");
		out = new BufferedWriter(audioFile);
	
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                screenRecorder tw = new screenRecorder();
				// Display the window.
                tw.setVisible(true);
            }
        });
		
		
		/*
		*	Delete previously saved files. This snippet may be moved anywhere.
		*/
		File file = new File("./inFiles/");
		String[] myFiles;
			if(file.isDirectory()){
				myFiles = file.list();
				for(int i=0;i<myFiles.length;i++){
					File myFile = new File(file,myFiles[i]);
					myFile.delete();
				}
			}
		//Folder emptied for a new recording.
		file = new File("./audios/");
			if(file.isDirectory()){
				myFiles = file.list();
				for(int i=0;i<myFiles.length;i++){
					File myFile = new File(file,myFiles[i]);
					myFile.delete();
				}
			}
			
		file = new File("./output.avi");
		if(file.exists()){file.delete();}
		//Folder emptied for a new recording.
		
    }
	
	
	/**^^^****************************************END OF MAIN()****************************************************/
	
	/**
	**Following method actionPerformed() handles all the events triggered on using buttons of the whole window...
	**All actions related to buttons can be found here..
	**/
	
	public void actionPerformed(ActionEvent e){
	
	
		if ("record".equals(e.getActionCommand())) {			//Given Sized screenshot.
			
			setState(Frame.ICONIFIED);							//Minimize window(so our window doesn't interfere with screenshot)
			button1.setToolTipText("Click this button to pause recording."); 
			button1.setText("Pause Recording");					//Change button to pause now
			button1.setActionCommand("pause");					//Change buttons action to pause
			button2.setToolTipText("Click this button to stop recording."); 
			button2.setText("Stop Recording");				//Change button to provide stop recording
			button2.setActionCommand("stop");				//Change button action to stop
			
			int w = getContentPane().getWidth();				//Width of rectangle for screenshot
			int h = getContentPane().getHeight();				//Height of rectangle for screenshot
			int r = pnlBttns.getHeight();						//Height of pnlBttns to subtract from content pane
			rect = new Rectangle(getX(), getY()+(2*r), w, h-r);	//Rectangle to be passed
			size = "sized";
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					sound.start("./audios/audio.wav");
				}
			});
			stopper.start();
			
			try{
			out.write("file './audio.wav'" + "\n");
			}catch(IOException ex){}
			state_Of_Recorder = "recording";					//Change State.
			timer.start();										//Start taking frames.
			System.out.println("Recorder Started");
			
		} else if ("full_size".equals(e.getActionCommand())) {			//Full Sized screenshot.
			
			setState(Frame.ICONIFIED);							//Minimize window(doesnt interfere with screenshot)
			button1.setToolTipText("Click this button to pause recording."); 
			button1.setText("Pause Recording");					//Change button to pause now
			button1.setActionCommand("pause");					//Change buttons action to pause
			button2.setToolTipText("Click this button to stop recording.");
			button2.setText("Stop Recording");				//Change button to provide stop recording
			button2.setActionCommand("stop");				//Change button action to stop
			
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
								//Rectangle for screenshot full size, before changing state.
			rect = new Rectangle(screenSize);
			size = "full";
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					sound.start("./audios/audio.wav");
				}
			});
			stopper.start();
			
			try{out.write("file './audio.wav'" + "\n");
			} catch(IOException ex){  System.out.println("Error"+ex);}
			state_Of_Recorder = "recording";					//Change State.
			timer.start();										//Start taking frames
			System.out.println("Recorder Started");
			
		} else if ("pause".equals(e.getActionCommand())) {			//Full Sized screenshot.
			
			button1.setToolTipText("Click this button to resume recording."); 
			button1.setText("Resume Recording");					//Change button to resume
			button1.setActionCommand("resume");					//Change buttons action to resume
			state_Of_Recorder = "paused";						//Change state.	
			System.out.println("Recorder Paused");
			sound.finish();
			
		} else if ("videos".equals(e.getActionCommand())) {		
			
				new myVideos();
				dispose();
			
		} else if ("resume".equals(e.getActionCommand())) {			//Full Sized screenshot.
			
			setState(Frame.ICONIFIED);							//Minimize window(doesnt interfere with screenshot)
			button1.setToolTipText("Click this button to pause recording."); 
			button1.setText("Pause Recording");					//Change button to pause now
			button1.setActionCommand("pause");					//Change buttons action to pause
			
			if (size == "sized"){	//In case the window has been resized or moved. No changes if size was full screen.
				int w = getContentPane().getWidth();				//Width of rectangle for screenshot
				int h = getContentPane().getHeight();				//Height of rectangle for screenshot
				int r = pnlBttns.getHeight();						//Height of pnlBttns to subtract from content pane
				rect = new Rectangle(getX(), getY()+(2*r), w, h-r);	//Rectangle to be passed
			}
			name = "./audio"+audioFiles+".wav";
			Thread stopper = new Thread(new Runnable() {
				public void run() {
					sound.start("./audios/"+name);
					System.out.println(name);
				}
			});
			stopper.start();
			try {out.write("file '"+name + "'\n");
			} catch(IOException ex){}
			audioFiles++;
			state_Of_Recorder = "recording";						//Change state.	
			System.out.println("Recorder Resumed");
			
		} else if ("stop".equals(e.getActionCommand())) {			//Full Sized screenshot.
			state_Of_Recorder = "stopped";						//Change state.	
			timer.stop();										//No more frames.
			System.out.println("Recorder Stopped");
			button1.setActionCommand("edit");					//Change buttons action to garbage
			button2.setActionCommand("upload");					//Change buttons action to garbage
			sound.finish();
			
			try{
				out.close();
				Process process = Runtime.getRuntime().exec("\"./lib/ffmpeg\" -f concat -i ./audios/audios.txt -c copy ./audios/final.wav");
				process.waitFor();
				int exitCode = process.exitValue();
				System.out.println("Exitcode of audios->final :"+exitCode);
		
			} catch (Exception ex){
				System.out.println("Error :"+ex);
			}
			
			
			/*Determining duration of our audio file, so that duration of audio and video are same....*/
			File audiofile = new File("./audios/final.wav");
			double durationInSeconds = 0;
			while(!audiofile.canWrite()) {
					// windows still working on it.
					try{Thread.sleep(100);}catch(InterruptedException ex){}
			}
			
			try{
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audiofile);
				AudioFormat format = audioInputStream.getFormat();
				long frames1 = audioInputStream.getFrameLength();
				durationInSeconds = (frames1+0.0) / format.getFrameRate();
			} catch(IOException ex){} catch(UnsupportedAudioFileException ex){}

			try{
				double framerate =  frames/durationInSeconds;
				System.out.println("size-"+audiofile.length()+" dur-"+durationInSeconds+" rate-"+framerate);
				name = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
				double ss = durationInSeconds/10.0;
				System.out.println("order images->video");	
				Process process = Runtime.getRuntime().exec(new String[]{"\"./lib/ffmpeg\"", "-r", ""+framerate, "-i", "./inFiles/img%d.jpeg", "-i", "./audios/final.wav", "-acodec", "copy", "-b:v", "5000k", "allfiles/"+name+".mov"});
				/*process.waitFor();
				int exitCode = process.exitValue();*/
				System.out.println(Arrays.toString(new String[]{"\"./lib/ffmpeg\"", "-r", ""+framerate, "-i", "./inFiles/img%d.jpeg", "-i", "./audios/final.wav", "-acodec", "copy", "-b:v", "5000k", "allfiles/"+name+".mov"}));
				System.out.println("Exitcode of images->video :  0");
				File file = new File("allfiles/"+name+".mov");
				while(!file.canWrite()) {
					// windows still working on it.
					try{Thread.sleep(100);}catch(InterruptedException ex){}
				}
				process = Runtime.getRuntime().exec(new String[]{"\"./lib/ffmpeg\"", "-i", "allfiles/"+name+".mov", "-ss", "00:00:01.43", "-f", "image2", "-vframes", "1", "-s", "320x240", "thumbnails/"+name+".jpg"});
				System.out.println(Arrays.toString(new String[]{"\"./lib/ffmpeg\"", "-i", "allfiles/"+name+".mov", "-ss", "00:00:01.43", "-f", "image2", "-vframes", "1", "-s", "320x240", "thumbnails/"+name+".jpg"}));
				/*process.waitFor();
				exitCode = process.exitValue();*/
				System.out.println("Exitcode of thumbnail : 0");
			} catch (Exception ex){ System.out.println("Error: "+ex);}
			dispose();
			new myVideos();
		}
	}
	
}	//End of class screenRecorder() here.


/********************************* END OF FILE***********************************************************/
