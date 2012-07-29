package framework.ui;

public class CollectionTableColumn {
	protected Object[] data_;

	private String name_;

	private Class<?> class_;

	public CollectionTableColumn(String name, Object[] data, Class clazz) {
		name_ = name;
		data_ = data;
		class_ = clazz;
	}

	public Object getItem(int r) {
		return data_[r];
	}

	public void setItem(Object o, int r) {
		data_[r] = o;
	}

	public String getName() {
		return name_;
	}

	public Object[] getData() {
		return data_;
	}

	public void setData(Object[] data) {
		data_ = data;
	}

	public Class<?> getColumnClass() {
		return class_;
	}

	public boolean equals(Object o) {
		if (o instanceof CollectionTableColumn) {
			return ((CollectionTableColumn) o).getName().equals(name_);
		} else if (o instanceof String) {
			return o.equals(name_);
		}
		return false;
	}
}