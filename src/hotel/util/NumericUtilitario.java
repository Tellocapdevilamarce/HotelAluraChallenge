package hotel.util;

public class NumericUtilitario {
	
	int num;
	
	public boolean isNumeric(String cadena){
		
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}

}
