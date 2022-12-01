import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.time.LocalDate;

import net.sourceforge.jdatepicker.impl.*;

import function.rotation_f;
import struct.rotation;

public class admin extends JPanel {
	final int MENU_NUM = 2; 
	public int selected;
	public static searchP search = new searchP();
	public static enrollP enroll = new enrollP();

	public admin() {
		setBackground(Color.ORANGE);
		setBounds(700, 0, 300, 700);
		setLayout(new FlowLayout());
		
		JButton[] btn_list = new JButton[MENU_NUM];
		
		btn_list[0] = new JButton("새로운 로테이션 등록");
		btn_list[1] = new JButton("로테이션 기록");
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton btn = (JButton)e.getSource();
					
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							selected = j;
						}
					}
					setVisible(false);
					
					if(selected == 0) {
						Main.c.add(enroll);
					} else {
						Main.c.add(search);
					}
				}
			});
			add(btn_list[i]);
		}
	}
	
	public static class enrollP extends JPanel {
		public enrollP() {
			setBackground(Color.orange);
			setBounds(700, 0, 300, 700);
			setLayout(new FlowLayout());
			
			JLabel la1 = new JLabel("로테이션 시작일");
			JLabel la2 = new JLabel("로테이션 종료일");
			String s = "시작일, 종료일 모두를 지정하지 않는 경우 자동 설정 됩니다.";
			JLabel la3 = new JLabel(s);
			JDatePickerImpl datePicker1 = createDatePicker();
			JDatePickerImpl datePicker2 = createDatePicker();
			JButton btn = new JButton("등록하기");
			
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					java.util.Date temp_start = (java.util.Date) datePicker1.getModel().getValue();
					java.util.Date temp_end = (java.util.Date) datePicker2.getModel().getValue();
					
					int result = 0;
					if(temp_start == null && temp_end == null) 
						result = rotation_f.setRotation();
					else if (temp_start == null)
						la3.setText("종료일을 입력하세요.");
					else if (temp_end == null) 
						la3.setText("시작일을 입력하세요.");
					else  {
						LocalDate start = new java.sql.Date(temp_start.getTime()).toLocalDate();
						LocalDate end = new java.sql.Date(temp_end.getTime()).toLocalDate();
						result = rotation_f.setRotation(start, end);
					}

					String message;
					if(result == rotation_f.SUCCESS)
						message = "등록 성공";
					else if (result == rotation_f.DB_FALSE)
						message = "저장 실패";
					else
						message = "새로운 로테이션 시작일은 마지막 로테이션의 종료일보다 앞설 수 없습니다.";
					
					JOptionPane.showMessageDialog(Main.c, message);
				}
			});
			
			add(la1); add(datePicker1);
			add(la2); add(datePicker2);
			add(la3);
			add(btn);
		}
		
		public JDatePickerImpl createDatePicker() {
			UtilDateModel model = new UtilDateModel();
			JDatePanelImpl datePanel = new JDatePanelImpl(model);
			JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
			return datePicker;
		}
	}
	
	public static class searchP extends JPanel {
		public searchP() {
			setBackground(Color.ORANGE);
			setBounds(70, 0, 930, 700);
			setPreferredSize(new Dimension(930, 700));
			setLayout(null);
			
			JLabel la = new JLabel("로테이션 목록");
			la.setBounds(30, 30, 200, 30);
			add(la);
			
			JPanel pan = new JPanel();
			pan.setBounds(30, 60, 850, 570);
			pan.setLayout(null);

			String header[] = {"로테이션 회차", "로테이션 시작일", "로테이션 종료일", "팀 구분", "구성 멤버"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			r_list.getColumn("로테이션 회차").setPreferredWidth(100);
			r_list.getColumn("로테이션 시작일").setPreferredWidth(190);
			r_list.getColumn("로테이션 종료일").setPreferredWidth(190);
			r_list.getColumn("팀 구분").setPreferredWidth(70);
			r_list.getColumn("구성 멤버").setPreferredWidth(297);
			r_list.getTableHeader().setReorderingAllowed(false);
			r_list.getTableHeader().setResizingAllowed(false);
			
			ArrayList<rotation> rt = rotation_f.getAllRotation();
			int rt_num = rt.size();
			
			ArrayList<String[]> strs = new ArrayList<String[]>();
			ArrayList<String[]> test = rt.get(0).getColumn();

			for(int i = 0; i < rt_num; i++) {
				strs.addAll(rt.get(i).getColumn());
			}
			
			for(int i = 0; i < strs.size(); i++) {
				String[] line = strs.get(i);
				model.addRow(line);
			}
			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(850, 570));
			list.setSize(850, 570);
			
			pan.add(list);
			add(pan);
		}
	}
	

}

