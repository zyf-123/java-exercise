package abc;

public class Test {
	private String id;
	private int card;
	private int balance;

	public void setid(String id)
	{
		this.id=id;
	}	
	public String getid()
	{
		return id;
	}
	public void setcard(int card)
	{
		this.card=card;
	}	
	public int getcard()
	{
		return card;
	}
	public void setbalance(int balance)
	{
		this.balance=balance;
	}	
	public int getbalance()
	{
		return balance;
	}
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
		if(d>balance)
			{
			System.out.println("Óà¶î²»×ã");
			return 0;
			}
		
			
			balance=balance-d;
			
			return balance;
			
	}
}