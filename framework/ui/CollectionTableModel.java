package framework.ui;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import collection.CollectionInfo;
import framework.ApolloFramework;

public class CollectionTableModel extends AbstractTableModel {
	private CollectionInfo info_;

	private ArrayList<CollectionTableColumn> columns_;

	public CollectionTableModel() {
		columns_ = new ArrayList<CollectionTableColumn>();
	}

	public int getColumnCount() {
		return columns_.size();
	}

	public int getRowCount() {
		if (info_ == null)
			return 0;
		return info_.getAlbumCount();
	}
	
	public Object getValueAt(int r, int c) {
		if (c >= columns_.size())
			return null;
		return columns_.get(c).getItem(r);
	}

	public void setValueAt(Object o, int r, int c) {
		if (c >= columns_.size())
			return;
		columns_.get(c).setItem(o, r);
		this.fireTableDataChanged();
	}

	public boolean isCellEditable(int r, int c) {
		return true;
	}

	public String getColumnName(int col) {
		if(col >= columns_.size())
			return null;
		return columns_.get(col).getName();
	}
	
	public Class<?> getColumnClass(int i)
	{
		return columns_.get(i).getColumnClass();
	}

	public int getColumnIndex(String name) {
		for (int i = 0; i < columns_.size(); i++) {
			if (columns_.get(i).getName().equals(name))
				return i;
		}
		return -1;
	}

	public CollectionTableColumn getColumnByName(String name) {
		int i = this.getColumnIndex(name);
		if (i < 0)
			return null;
		return columns_.get(getColumnIndex(name));
	}

	public void addColumn(CollectionTableColumn c) {
		columns_.add(c);
	}

	public void replaceColumn(CollectionTableColumn c) {
		columns_.remove(c);
		columns_.add(c);
	}

	public void update() {
		info_ = ApolloFramework.instance().getProject().getCollectionInfo();
		if (info_ == null)
			return;
		this.fireTableStructureChanged();
	}

}
