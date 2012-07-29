package framework.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import project.ProjectWizard;
import framework.ApolloFramework;

public class FrameworkImplWindowToolBar extends JToolBar {
	public FrameworkImplWindowToolBar() {
		JButton new_project = new JButton(new ImageIcon(this.getClass()
				.getResource("document-new.png")));
		new_project.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectWizard wiz = new ProjectWizard();
			}
		});
		JButton save = new JButton(new ImageIcon(this.getClass().getResource(
				"media-floppy.png")));
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApolloFramework.instance().getProject().save();
			}
		});

		JButton load = new JButton(new ImageIcon(this.getClass().getResource(
				"document-open.png")));
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApolloFramework.instance().openProject();
			}
		});

		this.setFloatable(false);
		this.add(Box.createHorizontalStrut(5));
		this.add(new_project);
		this.addSeparator();
		this.add(save);
		this.add(load);
		this.addSeparator();
	}
}
