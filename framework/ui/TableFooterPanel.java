package framework.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

public abstract class TableFooterPanel extends JPanel implements
		ListSelectionListener {
	protected JPanel summary_;

	protected JPanel tools_;

	protected JTable table_;

	protected AbstractTableModel model_;

	public TableFooterPanel(JTable table, AbstractTableModel model, String name) {
		model_ = model;
		table_ = table;
		table_.getSelectionModel().addListSelectionListener(this);

		tools_ = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tools_.setBorder(new BevelBorder(BevelBorder.RAISED));
		tools_.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		tools_.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				summary_.setVisible(!summary_.isVisible());
			}
		});

		JLabel summary_label = new JLabel("<html><b>" + name + "</b></html>");
		tools_.add(summary_label);

		summary_ = new JPanel();

		this.setLayout(new BorderLayout());
		this.add(tools_, BorderLayout.NORTH);
		this.add(summary_, BorderLayout.CENTER);
	}

	public abstract void setSummaryInfo(int row);

	public JPanel getSummaryPanel() {
		return summary_;
	}

	public JPanel getToolsPanel() {
		return tools_;
	}

	public void valueChanged(ListSelectionEvent e) {
		DefaultListSelectionModel m = (DefaultListSelectionModel) e.getSource();
		int r = m.getLeadSelectionIndex();
		if (r < 0)
			return;
		r = table_.getRowSorter().convertRowIndexToModel(r);
		if (!m.getValueIsAdjusting()) {
			summary_.removeAll();
			this.setSummaryInfo(r);
			summary_.validate();
		}
	}
}
