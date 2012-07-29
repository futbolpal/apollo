package framework;

import java.awt.Image;
import java.util.Hashtable;

import javax.swing.ImageIcon;

public class ImageLoader {
	private static ImageLoader this_;

	private Hashtable<String, ImageIcon> images_;

	protected ImageLoader() {
		images_ = new Hashtable<String, ImageIcon>();
	}

	public boolean containsImageIcon(String reference) {
		return images_.containsKey(reference);
	}

	public void putImageIcon(String reference, ImageIcon icon) {
		images_.put(reference, icon);
	}

	public ImageIcon getImageIcon(String key) {
		return images_.get(key);
	}

	public ImageIcon getImageIcon(Class reference, String relative_path) {
		if (images_.containsKey(reference.getName() + relative_path)) {
			ImageIcon i = images_.get(reference.getName() + relative_path);
			return i;
		} else {
			ImageIcon i = new ImageIcon(reference.getResource(relative_path));
			return i;
		}
	}

	public ImageIcon getImageIcon(Class reference, String relative_path,
			int size) {
		ImageIcon original = this.getImageIcon(reference, relative_path);
		return new ImageIcon(original.getImage().getScaledInstance(size, size,
				Image.SCALE_SMOOTH));
	}

	public static ImageLoader instance() {
		if (this_ == null)
			this_ = new ImageLoader();
		return this_;
	}
}
