package framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import project.ProjectMonitor;
import project.ProjectWizard;
import framework.ApolloFramework;

public class FrameworkImplWindow extends Window {
	private CollectionTable table_;

	private JTabbedPane tabs_;

	private FrameworkImplWindowToolBar toolbar_;

	public FrameworkImplWindow() {
		super();
		this.setIconImage(new ImageIcon(this.getClass()
				.getResource("music.png")).getImage());
		this.setJMenuBar(new FrameworkImplWindowMenuBar());
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				ApolloFramework.instance().close();
			}
		});
		toolbar_ = new FrameworkImplWindowToolBar();
		this.add(toolbar_, BorderLayout.NORTH);

		WindowStatusBar status = new WindowStatusBar();
		status.add(new TaskPoolMonitor());
		status.add(new ProjectMonitor());
		this.setWindowStatusBar(status);

		WindowMenuBar wmb = this.getWindowMenuBar();
		JMenuItem project_options = new JMenuItem("Configure Project...");
		project_options.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectWizard wiz = new ProjectWizard();
			}
		});
		wmb.getOptionsMenu().add(project_options);

		JMenuItem about = new JMenuItem("About Apollo Framework...");
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		wmb.getHelpMenu().add(about);

		tabs_ = new JTabbedPane() {
			public void addTab(String s, Component c) {
				super.addTab(s, c);
				JLabel vlabel = new JLabel(s);
				vlabel.setBorder(new EmptyBorder(3, 3, 3, 3));
				vlabel.setUI(new VerticalLabelUI(false));
				this.setTabComponentAt(this.getTabCount() - 1, vlabel);
			}
		};
		tabs_.setTabPlacement(JTabbedPane.LEFT);

		WelcomeTab welcome_ = new WelcomeTab();
		tabs_.add("Welcome", welcome_);

		table_ = new CollectionTable();
		tabs_.addTab("Overview", table_);

		this.add(tabs_, BorderLayout.CENTER);

		Window.setInstance(this);
	}

	public CollectionTable getTable() {
		return table_;
	}

	public FrameworkImplWindowToolBar getWindowToolBar() {
		return toolbar_;
	}

	public JTabbedPane getTabbedPane() {
		return this.tabs_;
	}
}
