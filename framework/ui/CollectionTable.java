package framework.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.RowFilter;
import javax.swing.border.BevelBorder;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableRowSorter;

import project.Project;
import project.ProjectListener;
import framework.ApolloFramework;
import framework.ApolloFrameworkListener;

public class CollectionTable extends JPanel implements ProjectListener,
		ApolloFrameworkListener, TableModelListener, RowSorterListener {
	private CollectionTableModel model_;

	private JTable table_;

	private ArrayList<RowFilter<CollectionTableModel, Integer>> filters_;

	private JPanel filters_panel_;

	private JToolBar info_bar_;

	private JLabel table_info_;

	private JPanel info_panel_;

	public CollectionTable() {
		ApolloFramework.instance().addListener(this);
		table_ = new JTable();
		model_ = new CollectionTableModel();
		model_.addTableModelListener(table_);
		table_.setModel(model_);
		table_.setRowSorter(new TableRowSorter(model_));
		table_.getRowSorter().addRowSorterListener(this);
		model_.addTableModelListener(this);
		filters_ = new ArrayList<RowFilter<CollectionTableModel, Integer>>();
		info_bar_ = new JToolBar();
		info_bar_.setPreferredSize(new Dimension(-1, 25));
		info_bar_.setFloatable(false);
		table_info_ = new JLabel("Showing "
				+ table_.getRowSorter().getViewRowCount() + " of "
				+ table_.getRowCount());
		info_bar_.add(table_info_);

		filters_panel_ = this.createFiltersPanel();

		info_panel_ = new JPanel();
		info_panel_.setLayout(new BoxLayout(info_panel_, BoxLayout.Y_AXIS));
		info_panel_.add(info_bar_);

		JScrollPane table_view = new JScrollPane(table_);
		table_view
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table_view
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BorderLayout());
		this.add(table_view, BorderLayout.CENTER);
		this.add(filters_panel_, BorderLayout.NORTH);
		this.add(info_panel_, BorderLayout.SOUTH);
	}

	public CollectionTableModel getTableModel() {
		return (CollectionTableModel) table_.getModel();
	}

	@SuppressWarnings("unchecked")
	public void addFilter(RowFilter f) {
		filters_.add(f);
		((TableRowSorter) table_.getRowSorter()).setRowFilter(RowFilter
				.andFilter(filters_));
	}

	@SuppressWarnings("unchecked")
	public void removeFilter(RowFilter f) {
		filters_.remove(f);
		((TableRowSorter) table_.getRowSorter()).setRowFilter(RowFilter
				.andFilter(filters_));
	}

	public JPanel getFiltersPanel() {
		return filters_panel_;
	}

	public JPanel getInfoPanel() {
		return info_panel_;
	}

	public JToolBar getInfoBar() {
		return info_bar_;
	}

	public JTable getTable() {
		return table_;
	}

	public void projectChanged(Project p) {
		model_.update();
		p.addListener(this);
	}

	public void projectBuilderChanged() {
		model_.update();
	}

	public void projectCollectionChanged() {
		// TODO Auto-generated method stub

	}

	public void projectValidatorChanged() {
		// TODO Auto-generated method stub

	}

	public void frameworkOpened() {
		// TODO Auto-generated method stub

	}

	public void frameworkOpening() {
		// TODO Auto-generated method stub

	}

	private JPanel createFiltersPanel() {
		JPanel tools = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tools.setBorder(new BevelBorder(BevelBorder.RAISED));
		tools.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		tools.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				for (int i = 1; i < filters_panel_.getComponentCount(); i++) {
					Component c = filters_panel_.getComponent(i);
					c.setVisible(!c.isVisible());
				}
				filters_panel_.validate();
			}
		});

		JPanel filters = new JPanel();
		filters.setLayout(new BoxLayout(filters, BoxLayout.Y_AXIS));

		JLabel filter_label = new JLabel("<html><b>Filters</b></html>");
		tools.add(filter_label);

		filters.add(tools);
		return filters;
	}

	public void tableChanged(TableModelEvent arg0) {
		table_info_.setText("Showing "
				+ table_.getRowSorter().getViewRowCount() + " of "
				+ table_.getRowSorter().getModelRowCount());

	}

	public void sorterChanged(RowSorterEvent arg0) {
		tableChanged(null);
	}

}
