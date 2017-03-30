import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class MethodTabModel extends AbstractTableModel {
	private ArrayList<InfoBean> content = new ArrayList<InfoBean>();
	private String[] columns = new String[] { "id", "Time(us)", "Method" };

	public MethodTabModel() {

	}

	@Override
	public int getRowCount() {// ����
		return content.size();
	}

	@Override
	public int getColumnCount() {// ����
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		InfoBean bean = this.content.get(rowIndex);
		switch (columnIndex) {// ÿ������ʾ�����ݶ���
		case 0:
			return bean.gettId();
		case 1:
			if (bean.getXitTime() == 0) {
				return "unknown";
			} else {
				return bean.getXitTime() - bean.getEntTime();
			}
		case 2: {
			return bean.getKey().replaceAll("[.]{2,}+", "");

		}
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	public void setContent(List<InfoBean> content) {
		this.content.clear();
		this.content.addAll(content);
		fireTableDataChanged();
	}

}
