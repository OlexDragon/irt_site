package irt.table;

public class FieldThread extends Thread {

	private int value;
	private Field field;

	public FieldThread(Field field, int value){
		this.field = field;
		this.value = value;
		start();
	}

	@Override
	public void run() {
		field.setValue(""+value);
	}
}
