package framework;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import launcher.Launcher;
import module_system.ModuleSystem;
import project.Project;
import basic_gui.FileChooserDialog;
import framework.tasks.Task;
import framework.tasks.TaskPool;
import framework.ui.FrameworkImplWindow;
import framework.ui.SplashScreenConfig;
import framework.ui.Window;

public class ApolloFramework extends Framework {
	private static ApolloFramework this_;

	private Project project_;

	protected ApolloFramework() {
	}

	public Project getProject() {
		if (project_ == null) {
			project_ = new Project();
			this.fireProjectChanged();
		}
		return this.project_;
	}

	public void newProject() {
		this.removeListener(project_);
		project_ = new Project();
	}

	public void fireProjectChanged() {
		Task t = new Task("Project changed...") {
			public void run() {
				Iterator<FrameworkListener> itr = ApolloFramework.this.listeners_
						.iterator();
				while (itr.hasNext()) {
					FrameworkListener l = itr.next();
					if (l instanceof ApolloFrameworkListener)
						((ApolloFrameworkListener) l).projectChanged(project_);
				}
			}

		};
		TaskPool.instance().addTask(t);
	}

	public SplashScreenConfig getSplashConfig() {
		Font f = null;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, this.getClass()
					.getResourceAsStream("ui/ALBA.ttf"));
			f = f.deriveFont(10f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new SplashScreenConfig(f, new ImageIcon(this.getClass()
				.getResource("ui/splash_bg3.png")), true);
	}

	public String getName() {
		return "Apollo Music Collection Analyzer";
	}

	public String getVersion() {
		return "1.0";
	}

	public void postLaunch() {
		super.postLaunch();
		boolean valid = this.validate();
		if (!valid) {
			JOptionPane.showMessageDialog(null,
					"Framework could not be validated", "Missing components!",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		Window.instance().setVisible(true);
	}

	public void preLaunch() {
		super.preLaunch();
		Window.setInstance(new FrameworkImplWindow());
	}

	public boolean validate() {
		Collection<ModuleSystem> systems = ModuleSystemLoader.instance()
				.getChildren();
		boolean has_comparator = false;
		boolean has_builder = false;
		for (ModuleSystem s : systems) {
			if (s.getClass().getName().equals(
					"system.CollectionValidatorLoader")) {
				has_comparator = true;
			}
			if (s.getClass().getName().equals("system.CollectionBuilderLoader")) {
				has_builder = true;
			}
		}
		return has_comparator && has_builder;
	}

	public void openProject() {
		FileChooserDialog jfc = new FileChooserDialog();
		jfc.showDialog(FileChooserDialog.Type.OPEN, false);
		File f = jfc.getSelectedFile();
		if (f != null) {
			try {
				Thread.currentThread().setContextClassLoader(getClassLoader());
				FrameworkObjectInputStream ois = new FrameworkObjectInputStream(
						new FileInputStream(f));
				this.project_ = (Project) ois.readObject();
				ois.close();
				this.fireProjectChanged();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void close() {
		if (project_ != null)
			project_.save();
		super.close();
	}

	public static ApolloFramework instance() {
		if (this_ == null) {
			this_ = new ApolloFramework();
		}
		return this_;
	}

	public static void main(String... args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Launcher.instance().setFramework(ApolloFramework.instance());
		Launcher.instance().run();
	}
}
