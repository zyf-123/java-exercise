package abc;

public class Test {
	String id;
	int card;
	int balance;
	
	Test(String n,int b,int c)
	{
		id=n;
		card=b;
		balance=c;
	}
	int Add(int c)
	{
		balance=balance+c;
		return balance;
	}
	int Reduce(int d)
	{
		balance=balance-d;
		return balance;
	}
}