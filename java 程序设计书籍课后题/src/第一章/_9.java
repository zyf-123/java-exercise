package µÚÒ»ÕÂ;

public class _9 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int n = 60;
	    
	        System.out.print(n+"=");
	        int i = 2;
	        while (n > 1) {
	            if (n % i == 0) {
	                System.out.print(i);
	                n /= i;
	                if (n != 1) {
	                    System.out.print("x");
	                }
	            } else {
	                i++;
	            }
	        }
	    
	}

}
