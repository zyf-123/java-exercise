package tongji;

public class tongji {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a[][]={{66,58,71},{46,51,80},{39,42,53},{65,71,89}};
		int sum=0;
		for(int i=0;i<4;i++)
		{
			for(int j=0;j<3;j++)
			{
				sum+=a[i][j];
			}
		}

	System.out.println("×ÜºÍÊÇ£º"+sum);
	
	}
}
