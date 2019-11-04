package ╣зр╩уб;

import java.util.Scanner;

public class _2 {
	public static double hswd;
	public static int sswd;
	public static	Scanner sysin =new Scanner(System.in);
	

	public  static double js (int n)
	{
		hswd=9.0/5*n+32;
		return hswd;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		sswd= sysin.nextInt();
		System.out.println(js(sswd));
		
	}

}
