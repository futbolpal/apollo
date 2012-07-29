package framework.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import framework.ImageLoader;

public class WelcomeTab extends JPanel {
	public WelcomeTab() {
		JLabel banner = new JLabel(
				"Welcome to the Apollo Music Collection Analyzer");
		banner.setBackground(Color.BLACK);
		banner.setForeground(Color.WHITE);
		banner.setOpaque(true);
		banner.setHorizontalTextPosition(JLabel.RIGHT);
		Font f;
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, this.getClass()
					.getResourceAsStream("ALBA.ttf"));
			f = f.deriveFont(20f);
			banner.setFont(f);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel instr = new JLabel(
				"<html><b>Organize.  Correct.  Explore.</b><br>"
						+ "1.  Create a new project" + "<br>"
						+ "2.  Import collection data" + "<br>"
						+ "3.  Explore collection" + "<br>" + "</html>");
		instr.setFont(instr.getFont().deriveFont(16f));
		instr.setVerticalTextPosition(SwingConstants.TOP);

		this.setLayout(new BorderLayout());
		this.add(banner, BorderLayout.NORTH);
		this.add(instr, BorderLayout.CENTER);

	}
}
