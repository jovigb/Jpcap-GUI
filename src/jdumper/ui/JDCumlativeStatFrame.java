package jdumper.ui;
import jpcap.packet.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.Vector;

import jdumper.stat.JDStatisticsTaker;
import jdumper.ui.graph.PieGraph;

public class JDCumlativeStatFrame extends JDStatFrame implements ListSelectionListener
{
	JTable table;
	TableModel model=null;
	PieGraph pieGraph=null;
	
	JDStatisticsTaker staker;
	int statType=0;
	
	public static JDCumlativeStatFrame openWindow(java.util.List<Packet> packets,JDStatisticsTaker staker){
		JDCumlativeStatFrame frame=new JDCumlativeStatFrame(packets,staker);
		frame.setVisible(true);
		return frame;
	}
	
	JDCumlativeStatFrame(java.util.List<Packet> packets,JDStatisticsTaker staker){
		super(staker.getName());
		this.staker=staker;
		staker.analyze(packets);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));

		model=new TableModel();
		table=new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JTableHeader header = table.getTableHeader();
		Dimension dim = header.getPreferredSize();
		dim.height=20;
		header.setPreferredSize(dim);
		JScrollPane tablePane=new JScrollPane(table);
		dim=table.getMinimumSize();
		dim.height+=25;
		tablePane.setPreferredSize(dim);
		
		if(staker.getLabels().length>1){
			pieGraph=new PieGraph(staker.getLabels(),staker.getValues(0));
			JSplitPane splitPane=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			splitPane.setTopComponent(tablePane);
			splitPane.setBottomComponent(pieGraph);
			
			getContentPane().add(splitPane);
			
			table.getSelectionModel().addListSelectionListener(this);
		}else{
			getContentPane().add(tablePane);
		}
		
		setSize(400,400);

		/*addInternalFrameListener(new InternalFrameAdapter(){
			public void internalFrameClosing(InternalFrameEvent evt){
				setVisible(false);
			}
		});*/
		
	}
	
	void fireUpdate(){
		int sel=table.getSelectedRow();
		if(pieGraph!=null) pieGraph.changeValue(staker.getValues(statType));
		if(model!=null) model.update();
		if(sel>=0) table.setRowSelectionInterval(sel,sel);
		repaint();
	}
	
	public void addPacket(Packet p){
		staker.addPacket(p);
	}
	
	public void clear(){
		staker.clear();
		if(pieGraph!=null) pieGraph.changeValue(staker.getValues(statType));
		if(model!=null) model.update();
	}
	
	public void valueChanged(ListSelectionEvent evt){
		if(evt.getValueIsAdjusting()) return;
		
		ListSelectionModel lsm=(ListSelectionModel)evt.getSource();
		if(lsm.isSelectionEmpty()) statType=0;
		else statType=lsm.getMinSelectionIndex();
		pieGraph.changeValue(staker.getValues(statType));
	}
	
	class TableModel extends AbstractTableModel{
		String[] labels;
		Object[][] values;
		TableModel(){
			labels=new String[staker.getLabels().length+1];
			labels[0]=new String();
			System.arraycopy(staker.getLabels(),0,labels,1,staker.getLabels().length);
			
			String[] types=staker.getStatTypes();
			values=new Object[types.length][staker.getLabels().length+1];
			for(int i=0;i<values.length;i++){
				values[i][0]=types[i];
				long[] v=staker.getValues(i);
				for(int j=0;j<v.length;j++)
					values[i][j+1]=new Long(v[j]);
			}
		}
		public String getColumnName(int c){return labels[c];}
		public int getColumnCount(){ return labels.length; }
		public int getRowCount(){ return values.length; }
		public Object getValueAt(int row,int column){ return values[row][column]; }
		void update(){
			String[] types=staker.getStatTypes();
			values=new Object[types.length][staker.getLabels().length+1];
			for(int i=0;i<values.length;i++){
				values[i][0]=types[i];
				long[] v=staker.getValues(i);
				for(int j=0;j<v.length;j++)
					values[i][j+1]=new Long(v[j]);
			}
			fireTableDataChanged();
		}
	}
}
