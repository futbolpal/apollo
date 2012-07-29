package project;

import java.awt.Image;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;

import system.CollectionBuilder;
import system.CollectionValidator;
import framework.ApolloFramework;
import framework.ApolloFrameworkListener;
import framework.ImageLoader;

public class ProjectMonitor extends JToolBar implements ProjectListener,
		ApolloFrameworkListener {
	private JLabel album_count_;

	private JLabel song_count_;

	private JLabel builder_;

	private JLabel validator_;

	private Project project_;

	public ProjectMonitor() {
		ApolloFramework.instance().addListener(this);
		ImageIcon album_img = new ImageIcon(ImageLoader.instance()
				.getImageIcon(this.getClass(), "cd.png").getImage()
				.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		album_count_ = new JLabel("0", album_img, JLabel.LEFT);
		ImageIcon song_img = new ImageIcon(ImageLoader.instance().getImageIcon(
				this.getClass(), "song.png").getImage().getScaledInstance(20,
				20, Image.SCALE_SMOOTH));
		song_count_ = new JLabel("0", song_img, JLabel.LEFT);
		builder_ = new JLabel();
		validator_ = new JLabel();
		this.setOpaque(false);
		this.setFloatable(false);
		this.add(album_count_);
		this.add(Box.createHorizontalStrut(5));
		this.add(song_count_);
		this.add(Box.createHorizontalStrut(5));
		this.add(new JSeparator(JSeparator.VERTICAL));
		this.add(Box.createHorizontalStrut(5));
		this.add(builder_);
		this.add(validator_);
		this.setVisible(true);
	}

	public void projectBuilderChanged() {
		CollectionBuilder cb = project_.getCollectionBuilder();
		if (cb != null) {
			ImageIcon i = (ImageIcon) cb.getIcon();
			Image sized = i.getImage().getScaledInstance(20, 20,
					Image.SCALE_SMOOTH);
			builder_.setText("");
			builder_.setIcon(new ImageIcon(sized));
		} else {
			builder_.setText("");
			builder_.setIcon(null);
		}

	}

	public void projectCollectionChanged() {
		if (project_ != null) {
			album_count_.setText(""
					+ project_.getCollectionInfo().getAlbumCount());
			song_count_.setText(""
					+ project_.getCollectionInfo().getSongCount());
		}
	}

	public void projectValidatorChanged() {
		CollectionValidator cv = project_.getCollectionValidator();
		if (cv != null) {
			ImageIcon i = (ImageIcon) cv.getIcon();
			Image sized = i.getImage().getScaledInstance(20, 20,
					Image.SCALE_SMOOTH);
			validator_.setText("");
			validator_.setIcon(new ImageIcon(sized));
		} else {
			validator_.setText("");
			validator_.setIcon(null);
		}
	}

	public void frameworkOpened() {
	}

	public void frameworkOpening() {
	}

	public void projectChanged(Project p) {
		project_ = p;
		project_.addListener(this);
	}
}