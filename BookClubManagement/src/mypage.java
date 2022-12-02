import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import function.rotation_f;
import function.session;
import struct.member;
import struct.rotation;

public class mypage extends JPanel {
	final int MENU_NUM = 2;
	private int selected;
	public static myRtnPane myRtn;
	public static changePWPane changePW;
	
	public mypage() {
		setBackground(Color.ORANGE);
		setBounds(700, 0, 300, 700);
		setLayout(new FlowLayout());
		
		myRtnPane myRtn = new myRtnPane();
		changePWPane changePW = new changePWPane();
		
		JButton[] btn_list = new JButton[MENU_NUM];
		
		btn_list[0] = new JButton("참여 기록");
		btn_list[1] = new JButton("비밀번호 변경");
		
		for(int i = 0; i < MENU_NUM; i++) {
			btn_list[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JButton btn = (JButton)e.getSource();
					
					for(int j = 0; j < MENU_NUM; j++) {
						if(btn.equals(btn_list[j])) {
							selected = j;
						}
					}
					setVisible(false);
					
					if(selected == 0) {
						Main.c.add(myRtn);
					} else {
						Main.c.add(changePW);
					}
				}
				
			});
			add(btn_list[i]);
		}
	}
	
	public static class myRtnPane extends JPanel {
		public myRtnPane() {
			setBackground(Color.ORANGE);
			setBounds(70, 0, 930, 700);
			setPreferredSize(new Dimension(930, 700));
			setLayout(null);
			
			JLabel la = new JLabel("참여한 로테이션 목록");
			la.setBounds(30, 30, 200, 30);
			add(la);
			
			JPanel pan = new JPanel();
			pan.setBounds(30, 60, 850, 570);
			pan.setLayout(null);
			
			String header[] = {"로테이션 회차", "팀 구분", "로테이션 시작일", "로테이션 종료일", "구성 멤버"};
			DefaultTableModel model = new DefaultTableModel(header, 0) {
				public boolean isCellEditable(int rowIndex, int ColIndex) {
					return false;
				}
			};
			
			JTable r_list = new JTable(model);
			r_list.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			r_list.getColumn("로테이션 회차").setPreferredWidth(100);
			r_list.getColumn("팀 구분").setPreferredWidth(70);
			r_list.getColumn("로테이션 시작일").setPreferredWidth(190);
			r_list.getColumn("로테이션 종료일").setPreferredWidth(190);
			r_list.getColumn("구성 멤버").setPreferredWidth(296);
			r_list.getTableHeader().setReorderingAllowed(false);
			r_list.getTableHeader().setResizingAllowed(false);


			ArrayList<String> myRL = member.getMyRotation(session.login_member.getNum());
			for(int i = 0; i < myRL.size(); i++) {
				String[] line = myRL.get(i).split("/");
				model.addRow(line);
			}

			
			JScrollPane list = new JScrollPane(r_list);
			list.setPreferredSize(new Dimension(850, 570));
			list.setSize(850, 570);
			
			pan.add(list);
			add(pan);
		}
	}

	public static class changePWPane extends JPanel {
		private JLabel error = new JLabel();
		private JPasswordField[] field = new JPasswordField[3];
		
		public changePWPane() {
			setBackground(Color.ORANGE);
			setBounds(700, 0, 300, 700);
			setLayout(new FlowLayout());
			
			JLabel la1 = new JLabel("현재 비밀번호");
			JLabel la2 = new JLabel("새 비밀번호");
			JLabel la3 = new JLabel("새 비밀번호 확인");
			JButton btn = new JButton("변경하기");
			
			btn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String now_pw = String.valueOf(field[0].getPassword());
					String new_pw = String.valueOf(field[1].getPassword());
					String check_pw = String.valueOf(field[2].getPassword());
					
					if(now_pw.equals("")) {
						error.setText("현재 비밀번호를 입력하세요.");
						field[0].requestFocus();
					} else if(new_pw.equals("")) {
						error.setText("새 비밀번호를 입력하세요.");
						field[1].requestFocus();
					} else if(check_pw.equals("")) {
						error.setText("비밀번호 확인이 필요합니다.");
						field[2].requestFocus();
					} else if(!new_pw.equals(check_pw)) {
						error.setText("새 비밀번호가 일치하지 않습니다.");
						field[2].requestFocus();
					} else {
						int result = session.login_member.changePass(now_pw, new_pw);
						
						String message = "";
						if(result == member.SUCCESS)
							message = "변경되었습니다.";
						else if (result == member.DISMATCH)
							message = "등록된 비밀번호가 아닙니다.";
						else
							message = "변경에 실패하였습니다.";
						error.setText("");
						JOptionPane.showMessageDialog(Main.c, message);
					}
				}
				
			});
			
			
			for(int i = 0; i < 3; i++)
				field[i] = new JPasswordField(15);
			
			
			add(la1); add(field[0]);
			add(la2); add(field[1]);
			add(la3); add(field[2]);
			add(error);
			add(btn);
		}
		
	}
}
