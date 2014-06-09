package irt.work;

public class InputTitles {
	private InputTitle[] inputTitles;
	
	public InputTitles(String[] titles,String[] types){
		setInputTitles(titles, types);
	}

	public InputTitle[] getInputTitles() {
		return inputTitles;
	}

	private void setInputTitles(String[] titles, String[] types) {
		inputTitles = new InputTitle[titles.length];
		
		for(int i=0; i<titles.length; i++){
			inputTitles[i] = new InputTitle(titles[i], types[i]);
		}
	}
	
	public InputTitle getInputTitle(int index){
		return (index<inputTitles.length)
				? inputTitles[index]
						: null;
	}

	public String[] toArray(){
		
		String[] returnArray = new String[(inputTitles!=null)
		                                  ? inputTitles.length
		                                		  : 0
		                               ];
		
		if( returnArray.length!=0){
			returnArray[0] = inputTitles[0].getName();
			for(int i=1; i<returnArray.length; i++)
				returnArray[i] = (!inputTitles[i].getName().contains("<br />"))
										? inputTitles[i].getName()
												: inputTitles[i].getName().substring(6);
		}

		return returnArray;
	}
	
	@Override
	public String toString() {
		String returnStr = "";
		
		if(inputTitles!=null && inputTitles.length!=0){
			returnStr = inputTitles[0].getName();
			for(int i=1; i<inputTitles.length; i++)
				returnStr += ","+inputTitles[i].getName();
		}
		
		return returnStr;
	}
}
