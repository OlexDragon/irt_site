package irt.table;

import java.util.ArrayList;
import java.util.List;

public class RowAttribute {

	private List<Boolean> show ;
	private int count;
	private boolean isCount;

	public RowAttribute() {
		show = new ArrayList<>();
		count = 0;
		isCount = true;
	}

	public void set(Row row) {
		if (row != null && row.getRow()!=null) {
			List<Boolean> show = new ArrayList<>();
			List<Field> r = row.getRow();

			for (Field field : r)
				if (field.isEmpty())
					show.add(false);
				else
					show.add(true);

			if (!show.isEmpty())
				for (int i = 0; i < show.size(); i++)
					if (this.show.size()<=i)
						this.show.add(show.get(i));
					else
						if(show.get(i))
							this.show.set(i, true);
		}
	}

	public List<Boolean> getShow() {
		return ((show!=null) ? show : new ArrayList<Boolean>());
	}

	public void setShow(List<Boolean> show) {
		this.show = show;
	}
	
	public void setShow(int index, boolean isShow){
		if(index<getShow().size())
			getShow().set(index, isShow);
	}

	public int getCount() {
		return count;
	}

	public int columnCount() { return show.size();	}

	/**
	 * @return count++
	 */
	public int count() {
		return count++;
	}

	public boolean isCount() {
		return isCount;
	}

	public void setCount(boolean isCount) {
		this.isCount = isCount;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int size() {
		return show!=null && !show.isEmpty()
				? show.size()
						: 0;
	}

	public boolean get(int index) {
		return index<size()
				? show.get(index)
						: true;
	}

	@Override
	public String toString() {
		return "RowAttribute [show=" + show + ", count=" + count + ", isCount="
				+ isCount + "]";
	}
}
