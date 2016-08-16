package jdumper.ui;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jdumper.JDCaptor;
import jdumper.JDStatisticsTakerLoader;
import jdumper.JpcapDumper;
import jdumper.stat.JDStatisticsTaker;

public class JDFrame extends JFrame implements ActionListener
{
	public JDCaptor captor;
	
	JLabel statusLabel;
	JMenuItem openMenu,saveMenu,captureMenu,stopMenu;
	JMenu statMenu;
	JButton openButton,saveButton,captureButton,stopButton;
	
	public JDTablePane tablePane;

	public static JDFrame openNewWindow(JDCaptor captor){
		JDFrame frame=new JDFrame(captor);
		frame.setVisible(true);
		
		return frame;
	}

	public JDFrame(JDCaptor captor){
		this.captor=captor;
		tablePane=new JDTablePane(captor);
		captor.setJDFrame(this);
		
		setTitle("JpcapDumper Main Window");

		// Create Menu
		JMenuBar menuBar=new JMenuBar();
		setJMenuBar(menuBar);
		
		//System Menu
		JMenu menu=new JMenu("System");
		menuBar.add(menu);
		JMenuItem item=new JMenuItem("New Window");
		item.setActionCommand("NewWin");
		item.addActionListener(this);
		menu.add(item);
		item=new JMenuItem("Exit");
		item.setActionCommand("Exit");
		item.addActionListener(this);
		menu.add(item);
		
		//File Menu
		menu=new JMenu("File");
		menuBar.add(menu);
		openMenu=new JMenuItem("Open");
		openMenu.setIcon(getImageIcon("/image/open.gif"));
		openMenu.setActionCommand("Open");
		openMenu.addActionListener(this);
		menu.add(openMenu);
		saveMenu=new JMenuItem("Save");
		saveMenu.setIcon(getImageIcon("/image/save.gif"));
		saveMenu.setActionCommand("Save");
		saveMenu.addActionListener(this);
		saveMenu.setEnabled(false);
		menu.add(saveMenu);

		//Capture Menu
		menu=new JMenu("Capture");
		menuBar.add(menu);
		captureMenu=new JMenuItem("Start");
		captureMenu.setIcon(getImageIcon("/image/capture.gif"));
		captureMenu.setActionCommand("Start");
		captureMenu.addActionListener(this);
		menu.add(captureMenu);
		stopMenu=new JMenuItem("Stop");
		stopMenu.setIcon(getImageIcon("/image/stopcap.gif"));
		stopMenu.setActionCommand("Stop");
		stopMenu.addActionListener(this);
		stopMenu.setEnabled(false);
		menu.add(stopMenu);
		
		//Stat Menu
		statMenu=new JMenu("Statistics");
		menuBar.add(statMenu);
		menu=new JMenu("Cumulative");
		statMenu.add(menu);
		java.util.List<JDStatisticsTaker> stakers=JDStatisticsTakerLoader.getStatisticsTakers();
		for(int i=0;i<stakers.size();i++){
			item=new JMenuItem(stakers.get(i).getName());
			item.setActionCommand("CUMSTAT"+i);
			item.addActionListener(this);
			menu.add(item);
		}
		menu=new JMenu("Continuous");
		statMenu.add(menu);
		for(int i=0;i<stakers.size();i++){
			item=new JMenuItem(stakers.get(i).getName());
			item.setActionCommand("CONSTAT"+i);
			item.addActionListener(this);
			menu.add(item);
		}

		//View menu
		menu=new JMenu("View");
		menuBar.add(menu);
		tablePane.setTableViewMenu(menu);
		
		//L&F Menu
		/*menu=new JMenu("Look&Feel");
		menuBar.add(menu);
		item=createLaFMenuItem("Metal","javax.swing.plaf.metal.MetalLookAndFeel");
		menu.add(item);
		item.setSelected(true);
		menu.add(createLaFMenuItem("Windows","com.sun.java.swing.plaf.windows.WindowsLookAndFeel"));
		menu.add(createLaFMenuItem("Motif","com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
		menu.add(createLaFMenuItem("Mac","com.sun.java.swing.plaf.mac.MacLookAndFeel"));*/
		
		
		//Create Toolbar
		JToolBar toolbar=new JToolBar();
		toolbar.setFloatable(false);
		openButton=new JButton(getImageIcon("/image/open.gif"));
		openButton.setActionCommand("Open");
		openButton.addActionListener(this);
		toolbar.add(openButton);
		saveButton=new JButton(getImageIcon("/image/save.gif"));
		saveButton.setActionCommand("Save");
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
		toolbar.add(saveButton);
		toolbar.addSeparator();
		captureButton=new JButton(getImageIcon("/image/capture.gif"));
		captureButton.setActionCommand("Start");
		captureButton.addActionListener(this);
		toolbar.add(captureButton);
		stopButton=new JButton(getImageIcon("/image/stopcap.gif"));
		stopButton.setActionCommand("Stop");
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		toolbar.add(stopButton);
		
		statusLabel=new JLabel("JpcapDumper started.");
		
		getContentPane().setLayout(new BorderLayout());
		//getContentPane().add(desktop,BorderLayout.CENTER);
		getContentPane().add(statusLabel,BorderLayout.SOUTH);
		getContentPane().add(tablePane,BorderLayout.CENTER);
		getContentPane().add(toolbar,BorderLayout.NORTH);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent evt){
				saveProperty();
				JpcapDumper.closeWindow((JDFrame)evt.getSource());
			}
		});
		
		loadProperty();
		//pack();
	}
	
	public void actionPerformed(ActionEvent evt){
		String cmd=evt.getActionCommand();
		
		if(cmd.equals("Open")){
			captor.loadPacketsFromFile();
		}else if(cmd.equals("Save")){
			captor.saveToFile();
		}else if(cmd.equals("NewWin")){
			JpcapDumper.openNewWindow();
		}else if(cmd.equals("Exit")){
			saveProperty();
			System.exit(0);
		}else if(cmd.equals("Start")){
			captor.capturePacketsFromDevice();
		}else if(cmd.equals("Stop")){
			captor.stopCapture();
		}else if(cmd.startsWith("CUMSTAT")){
			int index=Integer.parseInt(cmd.substring(7));
			captor.addCumulativeStatFrame(JDStatisticsTakerLoader.getStatisticsTakerAt(index));
		}else if(cmd.startsWith("CONSTAT")){
			int index=Integer.parseInt(cmd.substring(7));
			captor.addContinuousStatFrame(JDStatisticsTakerLoader.getStatisticsTakerAt(index));
		/*}else if(cmd.startsWith("LaF")){
			try{
				UIManager.setLookAndFeel(cmd.substring(3));
				SwingUtilities.updateComponentTreeUI(this);
				SwingUtilities.updateComponentTreeUI(JpcapDumper.chooser);
			}catch(Exception e){}*/
		}
	}
	
	public void clear(){
		tablePane.clear();
	}
	/*void initInternalFrames(){
		packets.removeAllElements();
		totalPacketCount=0;
		tableFrame.clear();

		if(sframes!=null)
			for(int i=0;i<sframes.length;i++)
				if(sframes[i]!=null) sframes[i].clear();
	}*/

	public void startUpdating(){
		JDFrameUpdater.setRepeats(true);
		JDFrameUpdater.start();
	}
	
	public void stopUpdating(){
		JDFrameUpdater.stop();
		JDFrameUpdater.setRepeats(false);
		JDFrameUpdater.start();
	}

	javax.swing.Timer JDFrameUpdater=new javax.swing.Timer(500,new ActionListener(){
		public void actionPerformed(ActionEvent evt){
			tablePane.fireTableChanged();
			statusLabel.setText("Captured "+captor.getPackets().size()+" packets.");

			repaint();
		}
	});

	void loadProperty(){
		setSize(Integer.parseInt(JpcapDumper.preferences.get("WinWidth","640")),
		        Integer.parseInt(JpcapDumper.preferences.get("WinHeight","480")));
		setLocation(Integer.parseInt(JpcapDumper.preferences.get("WinX","0")),
			Integer.parseInt(JpcapDumper.preferences.get("WinY","0")));
	}
	
	void saveProperty(){
		//JpcapDumper.JDProperty.setProperty("WinWidth",String.valueOf(getBounds().width));
		//JpcapDumper.JDProperty.setProperty("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.preferences.put("WinWidth",String.valueOf(getBounds().width));
		JpcapDumper.preferences.put("WinHeight",String.valueOf(getBounds().height));
		JpcapDumper.preferences.put("WinX",String.valueOf(getBounds().x));
		JpcapDumper.preferences.put("WinY",String.valueOf(getBounds().y));
		
		tablePane.saveProperty();
		
		JpcapDumper.saveProperty();
	}
	
	public void enableCapture(){
		openMenu.setEnabled(true);
		openButton.setEnabled(true);
		saveMenu.setEnabled(true);
		saveButton.setEnabled(true);
		captureMenu.setEnabled(true);
		captureButton.setEnabled(true);
		stopMenu.setEnabled(false);
		stopButton.setEnabled(false);
	}
	
	public void disableCapture(){
		openMenu.setEnabled(false);
		openButton.setEnabled(false);
		captureMenu.setEnabled(false);
		captureButton.setEnabled(false);
		saveMenu.setEnabled(true);
		saveButton.setEnabled(true);
		stopMenu.setEnabled(true);
		stopButton.setEnabled(true);
	}
	
	private ImageIcon getImageIcon(String path){
		return new ImageIcon(this.getClass().getResource(path));
	}
	
	/*ButtonGroup lafGroup=new ButtonGroup();
	private JRadioButtonMenuItem createLaFMenuItem(String name,String lafName){
		JRadioButtonMenuItem item=new JRadioButtonMenuItem(name);
		item.setActionCommand("LaF"+lafName);
		item.addActionListener(this);
		lafGroup.add(item);
		
		try {
			Class lnfClass = Class.forName(lafName);
			LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
			if(!newLAF.isSupportedLookAndFeel()) item.setEnabled(false);
		} catch(Exception e) {
			item.setEnabled(false);
		}
		
		return item;
	}*/
}
