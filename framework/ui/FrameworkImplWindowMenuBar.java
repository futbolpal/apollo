package framework.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import project.Project;
import project.ProjectWizard;
import framework.ApolloFramework;

public class FrameworkImplWindowMenuBar extends WindowMenuBar {
	public FrameworkImplWindowMenuBar() {
		JMenuItem new_project = new JMenuItem("New...");
		new_project.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApolloFramework.instance().newProject();
				ProjectWizard wiz = new ProjectWizard();
			}
		});
		JMenuItem close_project = new JMenuItem("Close");
		close_project.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ApolloFramework.instance().close();
			}
		});
		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		JMenuItem open = new JMenuItem("Open...");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});

		this.getFileMenu().add(new JSeparator(), 0);
		this.getFileMenu().add(save, 0);
		this.getFileMenu().add(close_project, 0);
		this.getFileMenu().add(new JSeparator(), 0);
		this.getFileMenu().add(open, 0);
		this.getFileMenu().add(new_project, 0);
	}
}
