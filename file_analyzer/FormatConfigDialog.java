package file_analyzer;

import gui.DNDList;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FormatConfigDialog extends JDialog {
	private JLabel example_match_;

	private JTextField match_field_;

	private DNDList match_list_;

	private JButton add_match_;

	private JButton remove_match_;

	private JButton ok_;

	private JButton cancel_;

	public FormatConfigDialog() {

		JPanel match_manage = new JPanel(new FlowLayout(FlowLayout.LEFT));
		match_field_ = new JTextField(25);
		match_field_.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				System.out.println(e.getKeyChar());
			}
		});

		add_match_ = new JButton("Add");
		add_match_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				match_list_.getModel().addElement(match_field_.getText());
			}
		});
		remove_match_ = new JButton("Remove");
		remove_match_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selected = match_list_.getSelectedIndex();
				if (selected > -1) {
					match_list_.getModel().removeElementAt(selected);
				}
			}
		});

		match_manage.add(remove_match_);
		match_manage.add(new JSeparator());
		match_manage.add(match_field_);
		match_manage.add(add_match_);

		JPanel match_panel = new JPanel(new BorderLayout());
		match_panel.add(new JScrollPane(match_list_ = new DNDList()));
		for(String format:FileAnalyzerSettings.FORMATS)
		{
			match_list_.getModel().addElement(format);
		}
		match_list_.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
			}
		});
		match_panel.add(match_manage, BorderLayout.SOUTH);

		JPanel control_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ok_ = new JButton("Ok");
		ok_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Enumeration matches = match_list_.getModel().elements();
				ArrayList<String> formats = new ArrayList<String>();
				while (matches.hasMoreElements()) {
					formats.add((String) matches.nextElement());
				}
				FileAnalyzerSettings.FORMATS = formats;
				FormatConfigDialog.this.dispose();
			}
		});

		cancel_ = new JButton("Cancel");
		cancel_.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormatConfigDialog.this.dispose();
			}
		});

		control_panel.add(ok_);
		control_panel.add(cancel_);

		example_match_ = new JLabel("Example:");

		// this.add(example_match_, BorderLayout.NORTH);
		this.add(match_panel, BorderLayout.CENTER);
		this.add(control_panel, BorderLayout.SOUTH);
		this.setTitle("Configure Song Formats");
		this.pack();
		this.setVisible(true);
	}

}
