package project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import module_system.ModuleSystem;
import system.CollectionBuilder;
import system.CollectionValidator;
import basic_gui.BasicWizard;
import basic_gui.BasicWizardFrame;
import basic_gui.LinkButton;
import collection.CollectionInfo;
import framework.ApolloFramework;
import framework.ModuleSystemLoader;
import framework.tasks.TaskPool;
import framework.ui.FrameworkImplWindow;
import framework.ui.TaskMonitor;

public class ProjectWizard extends BasicWizard {
	private CollectionBuilder builder_;

	private CollectionValidator validator_;

	private Project project_;

	public ProjectWizard() {
		super("Project Setup");
		this.project_ = ApolloFramework.instance().getProject();
		this.builder_ = project_.getCollectionBuilder();
		this.validator_ = project_.getCollectionValidator();
		this.addFrame(this.createWelcomeFrame());
		this.addFrame(this.createBuilderFrame());
		this.addFrame(this.createValidatorFrame());
		this.addFrame(this.createRunFrame());
		this.addFrame(this.createSummaryFrame());
		this.display();
	}

	private BasicWizardFrame createWelcomeFrame() {
		BasicWizardFrame welcome = new BasicWizardFrame();
		welcome.setLayout(new BorderLayout());
		String welcome_str;
		welcome_str = "<html>Welcome to the project wizard.<br><br>  Use this wizard to configure";
		welcome_str += "your project by setting an appropriate builder and validator of your choice.";
		welcome_str += "<br><br> - A builder is used to import your existing collection into the collection into the program.";
		welcome_str += "<br><br> - A validator is an external service used to compare your albums to other databases.";
		welcome_str += "<br><br>Press 'Next' to continue.</html>";
		JLabel welcome_label = new JLabel(welcome_str);
		welcome_label.setBorder(new EmptyBorder(0, 10, 0, 10));
		welcome.add(welcome_label, BorderLayout.CENTER);
		return welcome;
	}

	private BasicWizardFrame createBuilderFrame() {
		String instructions = "Select which type of builder you wish to use<BR> "
				+ "to import your music collection";
		BasicWizardFrame build = new BasicWizardFrame();
		build.setLayout(new BoxLayout(build, BoxLayout.Y_AXIS));
		build.setInstructions(instructions);
		build.setNextAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (builder_ == null) {
					setValid(false);
					JOptionPane.showMessageDialog(ProjectWizard.this,
							"You must select a builder", "Collection Builder",
							JOptionPane.ERROR_MESSAGE);
				} else {
					project_.setCollectionBuilder(builder_);
				}
			}
		});
		ModuleSystem<CollectionBuilder> builder_children = (ModuleSystem<CollectionBuilder>) ModuleSystemLoader
				.instance().getChild(
						ModuleSystemLoader.instance().getJarFor(
								"system.CollectionBuilderLoader"));
		build.add(Box.createVerticalStrut(10));
		JLabel title = new JLabel("Available Builders");
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setFont(new Font(title.getFont().getFamily(), Font.BOLD, title
				.getFont().getSize()));
		build.add(title);
		Collection<CollectionBuilder> builders = builder_children.getChildren();
		ButtonGroup bg = new ButtonGroup();
		for (final CollectionBuilder cb : builders) {
			final JRadioButton select = new JRadioButton(cb.getName());
			select.setSelected(cb == builder_);
			select.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (select.isSelected()) {
						builder_ = cb;
					}

				}
			});
			bg.add(select);
			build.add(select);
		}
		return build;
	}

	private BasicWizardFrame createValidatorFrame() {
		String instructions = "Select which type of validator you wish to use<BR> "
				+ "to check your music collection";
		BasicWizardFrame build = new BasicWizardFrame();
		build.setLayout(new BoxLayout(build, BoxLayout.Y_AXIS));
		build.setInstructions(instructions);
		build.setNextAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (validator_ == null) {
					setValid(false);
					JOptionPane.showMessageDialog(ProjectWizard.this,
							"You must select a validator",
							"Collection Validator", JOptionPane.ERROR_MESSAGE);
				} else {
					project_.setCollectionValidator(validator_);
				}
			}
		});
		ModuleSystem<CollectionValidator> builder_children = (ModuleSystem<CollectionValidator>) ModuleSystemLoader
				.instance().getChild(
						ModuleSystemLoader.instance().getJarFor(
								"system.CollectionValidatorLoader"));
		build.add(Box.createVerticalStrut(10));
		JLabel title = new JLabel("Available Validators");
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setFont(new Font(title.getFont().getFamily(), Font.BOLD, title
				.getFont().getSize()));
		build.add(title);
		Collection<CollectionValidator> validators = builder_children
				.getChildren();
		ButtonGroup bg = new ButtonGroup();
		for (final CollectionValidator cv : validators) {
			final JRadioButton select = new JRadioButton(cv.getName());
			select.setSelected(cv == validator_);
			select.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (select.isSelected()) {
						validator_ = cv;
					}

				}
			});
			bg.add(select);
			build.add(select);
		}
		return build;
	}

	private BasicWizardFrame createRunFrame() {
		final BasicWizardFrame start = new BasicWizardFrame();
		start.setLayout(new BoxLayout(start, BoxLayout.Y_AXIS));
		start.setInstructions("Time to get started.");
		start.setNextAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (TaskPool.instance().getTaskCount() > 0)
					JOptionPane.showMessageDialog(null,
							"The process will continue in the background",
							"Project Wizard", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		final TaskMonitor build_monitor = new TaskMonitor(null);
		build_monitor.setBorder(new EmptyBorder(0, 50, 0, 0));
		build_monitor.setMaximumSize(new Dimension(450, 25));
		build_monitor.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		final LinkButton build = new LinkButton("Step 1. Build collection");
		build.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CollectionInfo newinfo = new CollectionInfo();
				project_.setCollectionInfo(newinfo);
				builder_.setCollectionInfo(newinfo);
				build.setEnabled(false);
				build_monitor.setTask(builder_);
				build_monitor.start();
				TaskPool.instance().addTask(builder_);
				new Thread(new Runnable() {
					public void run() {
						while (!builder_.isDone()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						build_monitor.done();
					}
				}).start();
			}
		});
		if (builder_ != null) {
			build.setEnabled(!builder_.isDone());
		}

		final TaskMonitor validator_monitor = new TaskMonitor(null);
		validator_monitor.setBorder(new EmptyBorder(0, 50, 0, 0));
		validator_monitor.setMaximumSize(new Dimension(450, 25));
		validator_monitor.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		final LinkButton validate = new LinkButton(
				"Step 2. Validate collection (optional)");
		validate.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				CollectionInfo newinfo = new CollectionInfo();
				project_.setCollectionInfo(newinfo);
				validator_.setCollectionInfo(newinfo);
				build.setEnabled(false);
				validator_monitor.setTask(validator_);
				validator_monitor.start();
				TaskPool.instance().addTask(validator_);
				new Thread(new Runnable() {
					public void run() {
						while (!validator_.isDone()) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						validator_monitor.done();
					}
				}).start();
			}
		});
		if (validator_ != null) {
			validate.setEnabled(!validator_.isDone());
		}

		JLabel title = new JLabel("Launch:");
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		title.setFont(new Font(title.getFont().getFamily(), Font.BOLD, title
				.getFont().getSize()));

		start.add(title);
		start.add(build);
		start.add(validate);
		start.add(Box.createVerticalStrut(30));
		start.add(new JLabel("Build Monitor"));
		start.add(build_monitor);
		start.add(new JLabel("Validator Monitor"));
		start.add(validator_monitor);
		return start;

	}

	private BasicWizardFrame createSummaryFrame() {
		BasicWizardFrame summary = new BasicWizardFrame();
		summary.setLayout(new BorderLayout());
		String summary_str;
		summary_str = "<html>Your project has been created.<br><br>";
		JLabel summary_label = new JLabel(summary_str);
		summary_label.setBorder(new EmptyBorder(0, 10, 0, 10));
		summary.add(summary_label, BorderLayout.CENTER);
		return summary;
	}
}
