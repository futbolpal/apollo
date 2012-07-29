package framework.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class DNDList extends JList {

	private Object source_;

	private int temp_index_ = -1;

	public DNDList() {
		super(new DefaultListModel());
		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.removeMouseMotionListener(this.getMouseMotionListeners()[0]);

		this.removeMouseListener(this.getMouseListeners()[0]);
		this.addMouseListener(new MouseAdapter() {

			public void mouseReleased(MouseEvent e) {
				if (source_ != null) {
					if (DNDList.this.getModel().contains(""))
						DNDList.this.getModel().removeElement("");
					
					int i = DNDList.this.locationToIndex(e.getPoint());
					i = Math.max(i, 0);
					i = Math.min(DNDList.this.getModel().getSize() - 1, i);
					DNDList.this.getModel().removeElement(source_);
					DNDList.this.getModel().add(i, source_);
					DNDList.this.setSelectedIndex(i);
					source_ = null;
					temp_index_ = -1;
				}
			}

		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (source_ == null) {
					temp_index_ = DNDList.this.locationToIndex(e.getPoint());
					source_ = DNDList.this.getModel().elementAt(temp_index_);
				} else {
					int index = DNDList.this.locationToIndex(e.getPoint());
					if (index > 0 && index != temp_index_) {
						if (DNDList.this.getModel().contains(""))
							DNDList.this.getModel().removeElement("");
						DNDList.this.getModel().add(index, new String());

					}
					temp_index_ = index;
				}

			}
		});
	}

	public DefaultListModel getModel() {
		return ((DefaultListModel) super.getModel());
	}
}
