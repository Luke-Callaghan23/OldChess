import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class S 
{
	public static int oneWins = 0, twoWins = 0, turnNo = 1;
	public static int[] occuMap = {0,0,0,0,0,0,0,0,0};
	public static String[] posMap = {"1-1","1-2","1-3","2-1","2-2","2-3","3-1","3-2","3-3"};
	public static void main(String[] args)
	{
		System.out.println("(   ) [   ] [   ]\n[   ] [   ] [   ]\n[   ] [   ] [   ]");
		System.out.println("Use WASD\nEnter 'e' to set your piece.\nEnter 'r' to reset.");
		Scanner scAction = new Scanner(System.in);
		boolean valid  = false;
		while(!valid)
		{
			try
			{
				char action = scAction.next(".").charAt(0);
				if(action == 'w' || action == 'a' || action == 's' || action == 'd' || action == 'e' || action == 'r')
				{
					valid  = true;
					String position = null;
					String BackupPosition = null;
					if(action == 'e')
					{
						position = SetPiece("1-1");
					}
					else if(action == 'r')
					{
						System.out.println("Can't reset an empty board!");
						TimeUnit.SECONDS.sleep(1);
						main(args);
					}
					else
					{
						position = MoveCursor("1-1", action);
					}
					if(position != null)
					{
						BackupPosition = position;
					}
					else
					{
						BackupPosition = "1-1";
					}
					while(true)
					{
						if(position != null)
						{
							BackupPosition = position;
							System.out.println("Next move.");
							boolean nValid = false;
							while(!nValid)
							{
								try
								{
									action = scAction.next(".").charAt(0);
									if(action == 'w' || action == 'a' || action == 's' || action == 'd' || action == 'e' || action == 'r')
									{
										nValid = true;
										if(action == 'e')
										{
											position = SetPiece(position);
										}
										else if(action == 'r')
										{
											position = ResetMap();
										}
										else
										{
											position = MoveCursor(position, action);
										}
									}
									else
									{
										System.out.println("Only enter W,A,S,D,or E.");
										TimeUnit.SECONDS.sleep(1);
										scAction = new Scanner(System.in);
									}
								}
								catch(Exception e)
								{
									System.out.println("Only enter one character.");
									TimeUnit.SECONDS.sleep(1);
									scAction = new Scanner(System.in);
								}
							}
						}
						else
						{
							System.out.println("Try another character.");
							boolean nValid = false;
							while(!nValid)
							{
								try
								{
									action = scAction.next(".").charAt(0);
									if(action == 'w' || action == 'a' || action == 's' || action == 'd' || action == 'e' || action == 'r')
									{
										nValid = true;
										if(action == 'e')
										{
											position = SetPiece(BackupPosition);
										}
										if(action == 'r')
										{
											position = ResetMap();
										}
										else
										{
											position = MoveCursor(BackupPosition, action);
										}
									}
									else
									{
										PrintPosition(position);
										System.out.println("Only enter W,A,S,D,E,or R.");
										TimeUnit.SECONDS.sleep(1);
										scAction = new Scanner(System.in);
									}
								}
								catch(Exception e)
								{
									PrintPosition(position);
									System.out.println("Only enter one character.");
									TimeUnit.SECONDS.sleep(1);
									scAction = new Scanner(System.in);
								}
							}
						}
					}
				}
				else
				{
					PrintPosition("1-1");
					System.out.println("Only enter W,A,S,D,E,or R.");
					TimeUnit.SECONDS.sleep(1);
					scAction = new Scanner(System.in);
				}
			}
			catch(Exception e)
			{
				PrintPosition("1-1");
				System.out.println("Only enter one character.");
				try 
				{
					TimeUnit.SECONDS.sleep(1);
				}
				catch(InterruptedException e1) 
				{
					e1.printStackTrace();
				}
				scAction = new Scanner(System.in);
			}
		}
	}
	public static String SetPiece(String position)
	{
		StringBuilder sb = new StringBuilder();
		for(int loop = 0; loop < 9; loop++)
		{
			if(posMap[loop].equals(position))
			{
				if(occuMap[loop] == 0)
				{
					if(turnNo % 2 != 0)
					{
						sb.append("( X )");
						occuMap[loop] = 1;
						System.out.println("set X in " + position);
					}
					else
					{
						sb.append("( O )");
						occuMap[loop] = 2;
						System.out.println("set O in " + position);
					}
				}
				else
				{
					String ret = PrintPosition(position);
					System.out.println("Occupied!");
					return ret;
				}
			}
			else if(occuMap[loop] == 0)
			{
				sb.append("[   ]");
			}
			else if(occuMap[loop] == 1)
			{
				sb.append("[ X ]");
			}
			else if(occuMap[loop] == 2)
			{
				sb.append("[ O ]");
			}
			if(loop == 2 || loop == 5)
			{
				sb.append("\n");
			}
		}
		int check = winCheck();
		if(check == 0)
		{
			turnNo++;
			System.out.println(sb);
			if(turnNo % 10 == 0)
			{
				System.out.println("DRAW!");
				try 
				{
					TimeUnit.SECONDS.sleep(1);
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
				System.out.println("\n");
				return ResetMap();
			}
			return position;
		}
		else
		{
			printWin();
			if(check == 1)
			{
				oneWins++;
				System.out.println("Winner player 1!");
			}
			else
			{
				twoWins++;
				System.out.println("Winner player 2!");
			}
			System.out.println("Score: " + oneWins + "-" + twoWins);
			try 
			{
				TimeUnit.SECONDS.sleep(2);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			return ResetMap();
		}
	}
	public static String MoveCursor(String position, char action)
	{
		int posOne = Integer.valueOf(String.valueOf(position.charAt(0)));
		int posTwo = Integer.valueOf(String.valueOf(position.charAt(2)));
		if(action == 'w')
		{
			if(posOne == 1)
			{
				position = PrintPosition(position);
				System.out.println("Can't go any farther North.");
				return null;
			}
			else
			{
				posOne = posOne - 1;
			}
		}
		else if(action == 'd')
		{
			if(posTwo == 3)
			{
				position = PrintPosition(position);
				System.out.println("Can't go any farther East.");
				return null;
			}
			else
			{
				posTwo = posTwo + 1;
			}
		}
		else if(action == 's')
		{
			if(posOne == 3)
			{
				position = PrintPosition(position);
				System.out.println("Can't go any farther South.");
				return null;
			}
			else
			{	
				posOne = posOne + 1;
			}
		}
		else if(action == 'a')
		{
			if(posTwo == 1)
			{
				position = PrintPosition(position);
				System.out.println("Can't go any farther West.");
				return null;
			}
			else
			{
				posTwo = posTwo - 1;
			}
		}
		return PrintPosition(posOne + "-" + posTwo);
	}
	public static String PrintPosition(String position)
	{
		StringBuilder sb = new StringBuilder();
		for(int loop = 0; loop < 9; loop++)
		{
			if(posMap[loop].equals(position))
			{
				if(occuMap[loop] == 0)
				{
					sb.append("(   )");
				}
				else if(occuMap[loop] == 1)
				{
					sb.append("( X )");
				}
				else if(occuMap[loop] == 2)
				{
					sb.append("( O )");
				}
			}
			else if(occuMap[loop] == 0)
			{
				sb.append("[   ]");
			}
			else if(occuMap[loop] == 1)
			{
				sb.append("[ X ]");
			}
			else if(occuMap[loop] == 2)
			{
				sb.append("[ O ]");
			}
			if(loop == 2 || loop == 5)
			{
				sb.append("\n");
			}
		}
		System.out.println(sb);
		return position;
	}
	public static String ResetMap() 
	{
		for(int loop = 0; loop < 9; loop++)
			occuMap[loop] = 0;
		System.out.println("(   ) [   ] [   ]\n[   ] [   ] [   ]\n[   ] [   ] [   ]");
		System.out.println("Map reset.");
		return "1-1";
	}
	public static int winCheck()
	{
		if(occuMap[0] != 0)
		{
			if(occuMap[0] == occuMap[1] && occuMap[0] == occuMap[2])
			{
				if(occuMap[0] == 1)
				{
					occuMap[0] = 3; occuMap[1] = 3; occuMap[2] = 3;
					return 1;
				}
				else
				{
					occuMap[0] = 3; occuMap[1] = 3; occuMap[2] = 3;
					return 2;
				}
			}
			else if(occuMap[0] == occuMap[3] && occuMap[0] == occuMap[6])
			{
				if(occuMap[0] == 1)
				{
					occuMap[0] = 4; occuMap[3] = 4; occuMap[6] = 4;
					return 1;
				}
				else
				{
					occuMap[0] = 4; occuMap[3] = 4; occuMap[6] = 4;
					return 2;
				}
			}
		}
		if(occuMap[4] != 0)
		{
			if(occuMap[4] == occuMap[3] && occuMap[4] == occuMap[5])
			{
				if(occuMap[4] == 1)
				{
					occuMap[3] = 3; occuMap[4] = 3; occuMap[5] = 3;
					return 1;
				}
				else
				{
					occuMap[3] = 3; occuMap[4] = 3; occuMap[5] = 3;
					return 2;
				}
			}
			else if(occuMap[4] == occuMap[1] && occuMap[4] == occuMap[7])
			{
				if(occuMap[4] == 1)
				{
					occuMap[1] = 4; occuMap[4] = 4; occuMap[7] = 4;
					return 1;
				}
				else
				{
					occuMap[1] = 4; occuMap[4] = 4; occuMap[7] = 4;
					return 2;
				}
			}
			else if(occuMap[4] == occuMap[0] && occuMap[4] == occuMap[8])
			{
				if(occuMap[4] == 1)
				{
					occuMap[0] = 5; occuMap[4] = 5; occuMap[8] = 5;
					return 1;
				}
				else
				{
					occuMap[0] = 5; occuMap[4] = 5; occuMap[8] = 5;
					return 2;
				}
			}
			else if(occuMap[4] == occuMap[2] && occuMap[4] == occuMap[6])
			{
				if(occuMap[4] == 1)
				{
					occuMap[2] = 6; occuMap[4] = 6; occuMap[6] = 6;
					return 1;
				}
				else 
				{
					occuMap[2] = 6; occuMap[4] = 6; occuMap[6] = 6;
					return 2;
				}
			}
		}
		if(occuMap[8] != 0)
		{
			if(occuMap[8] == occuMap[6] && occuMap[8] == occuMap[7])
			{
				if(occuMap[8] == 1)
				{
					occuMap[6] = 3; occuMap[7] = 3; occuMap[8] = 3;
					return 1;
				}
				else
				{
					occuMap[6] = 3; occuMap[7] = 3; occuMap[8] = 3;
					return 2;
				}
			}
			else if(occuMap[8] == occuMap[2] && occuMap[8] == occuMap[5])
			{
				if(occuMap[8] == 1)
				{
					occuMap[2] = 4; occuMap[5] = 4; occuMap[8] = 4;
					return 1;
				}
				else
				{
					occuMap[2] = 4; occuMap[5] = 4; occuMap[8] = 4;
					return 2;
				}
			}
		}
		return 0;
	}
	public static void printWin()
	{
		StringBuilder sb = new StringBuilder();
		for(int loop = 0; loop < 9; loop++)
		{
			if(occuMap[loop] == 0)
			{
				sb.append("[   ]");
			}
			else if(occuMap[loop] == 1)
			{
				sb.append("[ X ]");
			}
			else if(occuMap[loop] == 2)
			{
				sb.append("[ O ]");
			}
			else if(occuMap[loop] == 3)
			{
				sb.append("[ - ]");
			}
			else if(occuMap[loop] == 4)
			{
				sb.append("[ | ]");
			}
			else if(occuMap[loop] == 5)
			{
				sb.append("[ \\ ]");
			}
			else if(occuMap[loop] == 6)
			{
				sb.append("[ / ]");
			}
			if(loop == 2 || loop == 5)
			{
				sb.append("\n");
			}	
		}
		turnNo = 1;
		System.out.println(sb);
	}
}