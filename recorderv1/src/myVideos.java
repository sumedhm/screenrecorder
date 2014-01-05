package src;

import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.event.*;



/******************This class will show all videos made or edited by this application in grid view ***********************/

public class myVideos implements ActionListener{

	JFrame frame = new JFrame("Your recorded Videos");
	JPanel videoPanel = new JPanel();
	File allfiles = new File("./allfiles/");
	File[] fileLst = allfiles.listFiles();
	String name = new String();
	
		public myVideos(){
			frame.setLayout(new BorderLayout());
			frame.setDefaultLookAndFeelDecorated(false);
			JLabel head = new JLabel("<html><br>Showing All Videos<br><br></html>", JLabel.CENTER);
			head.setFont(new Font("Verdana",1,20));
			frame.add(head, BorderLayout.NORTH);
			JPanel[] panel = new JPanel[fileLst.length];
			JLabel[] label = new JLabel[fileLst.length];
			JButton[] button = new JButton[fileLst.length];
			for(int i=0;i<fileLst.length;i++){
				panel[i] = new JPanel();
				label[i] = new JLabel();
				button[i] = new JButton("Delete");
			}
			videoPanel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(20, 40, 20, 40), new EmptyBorder(20,20,20,20)));
			videoPanel.setLayout(new GridLayout((fileLst.length/4)+1, 4, 15, 15));
			videoPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			if((fileLst.length)>0){
				for (int i = fileLst.length - 1; i >= 0; i--) {
					panel[i].setLayout(new BoxLayout(panel[i], BoxLayout.Y_AXIS));
					panel[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					panel[i].setSize(new Dimension(120,120));
					panel[i].setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 0, 0), new EtchedBorder()));
					name = new String(fileLst[i].getName().replaceFirst("[.][^.]+$", ""));
					System.out.println(name);
					panel[i].setName(fileLst[i].getName());
					panel[i].addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent e){
								String file = ((JPanel)e.getSource()).getName();
								new playWindow(file);
								frame.dispose();
							}
					});
					button[i].addActionListener(this);
					File img = new File("thumbnails/"+name+".jpg");
					if(!img.exists()){
						try{Runtime.getRuntime().exec(new String[]{"\"lib/ffmpeg\"", "-i", "allfiles/"+name+".mov", "-ss", "00:00:01.435", "-f", "image2", "-vframes", "1", "-s", "320x240", "thumbnails/"+name+".jpg"});}
						catch(Exception ex){System.out.println("Thumbnail could not be created for "+ name);}
					}
					panel[i].add(new JLabel("",new ImageIcon(new ImageIcon("thumbnails/"+name+".jpg").getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH)), JLabel.CENTER));
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					label[i].setText("<html><br>"+fileLst[i].getName()+"<br>"+sdf.format(fileLst[i].lastModified())+"</html>");
					panel[i].add(label[i]);
					panel[i].add(button[i]);
					videoPanel.add(panel[i]);
				}
			} else {head.setText("No Videos Found");}
			frame.add(videoPanel, BorderLayout.CENTER);
			JScrollPane scroll = new JScrollPane(videoPanel);
			frame.add(scroll);
			frame.setLocation(10,10);
			frame.setSize(1200,700);
			frame.setIconImage(new ImageIcon("./icon.jpg").getImage());
			frame.setDefaultLookAndFeelDecorated(false);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		}
		public void actionPerformed(ActionEvent e) {
                String file = (((JButton)e.getSource()).getParent()).getName();
				new File("allfiles/"+file).delete();
				file = file.replaceFirst("[.][^.]+$", "");
				new File("thumbnails/"+ file +".jpg").delete();
				System.out.println("thumbnails/"+ file +".jpg"+ " got deleted");
				new myVideos();
				frame.dispose();
        }
		
		public static void main(String[] args){
			new myVideos();
		}


}//class myVideos ends here..