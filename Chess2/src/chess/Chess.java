package chess;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;public class Chess extends EditArray
{
	//this is the initial map of all the original pieces in their original positions on the board
	public static final String[][] INITIALOCCUMAP = {
			{"br1","bk1","bb1","b_q","b_k","bb2","bk2","br2"},
			{"bp1","bp2","bp3","bp4","bp5","bp6","bp7","bp8"},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"wp1","wp2","wp3","wp4","wp5","wp6","wp7","wp8"},
			{"wr1","wk1","wb1","w_q","w_k","wb2","wk2","wr2"}};
	//makes a copy of initialOccuMap, but you can edit occuMap wihtout editing initialOccuMap
	public static String[][] occuMap = copyString(INITIALOCCUMAP);
	public static String[][] backupOccuMap;
	//this is the original piecelist of all the default pieces
	public static final String[] INTITIALPIECELIST = {
			"br1","bk1","bb1","b_q","b_k","bb2","bk2","br2",
			"bp1","bp2","bp3","bp4","bp5","bp6","bp7","bp8",
			"wp1","wp2","wp3","wp4","wp5","wp6","wp7","wp8",
			"wr1","wk1","wb1","w_q","w_k","wb2","wk2","wr2"};
	//makes a copy of initialPieceList, but you can edit pieceList wihtout editing initialPieceList
	public static String[] pieceList = copyString(INTITIALPIECELIST);
	public static String[] backupPieceList;
	//this is the original--and final--map of all positions in their String interpretation
	public static final String[][] POSMAP = {
			{"a-8","b-8","c-8","d-8","e-8","f-8","g-8","h-8"},
		    {"a-7","b-7","c-7","d-7","e-7","f-7","g-7","h-7"},
	     	{"a-6","b-6","c-6","d-6","e-6","f-6","g-6","h-6"},
			{"a-5","b-5","c-5","d-5","e-5","f-5","g-5","h-5"},
			{"a-4","b-4","c-4","d-4","e-4","f-4","g-4","h-4"},
			{"a-3","b-3","c-3","d-3","e-3","f-3","g-3","h-3"},
			{"a-2","b-2","c-2","d-2","e-2","f-2","g-2","h-2"},
			{"a-1","b-1","c-1","d-1","e-1","f-1","g-1","h-1"}};
	//these are all the essential booleans for the game to run
	public static boolean pawnMovedTwice = false, deletePawnThatMovedTwice = false, didntMove = false, 
			whiteKingChecked = false, blackKingChecked = false, whiteKingMoved = false, blackKingMoved = false, 
			wr1Moved = false, wr2Moved = false, br1Moved = false, br2Moved = false,
			loadedMap = false, askStaleMate = false;
	//these are all the essential Strings for the game to run
	public static String pawnThatMoved = "", positionThatPawnMovedTo = "";
	//these are all the essential integers required for the game to run
	public static int turns = 1, wWins = 0, bWins = 0, draws = 0, whiteQueens = 1, blackQueens = 1, movesDrawed = 0;
	//these are the essential chars required for the game to run
	public static char enemy = 0, ally = 0;
	//this String is constantly refilled by the users' input
	public static String input;
	//this String is constantly refilled by the users' input splited at the comma
	public static String[] inputSplit;
	//this is the scanner that is constantly called to ask for inputs
	public static Scanner scan = new Scanner(System.in);
	//this is the fill that maps are written into and loaded from
	public static File file = new File("res/Board.txt");
	/*
	//the main method
	//		there is an infinite loop that loops through:
	//			checking to see if there is a map in "res/Board.txt"
	//			if there is:
	//				asks if the user wants to load that board
	//				if yes: loads the board
	//				if no: starts game normally
	//			asking for inputs
	//			checking if inputs are valid
	//			checking if:
	//				input is asking to move a piece or do something else
	//			if moving piece: it attempts to move the piece
	//			if attempting to do something else:
	//				it could:
	//					show options
	//					show the attacking options of any given piece
	//					ask other player for a stalemate
	//					restart game in a forfiet
	//					load a saved map
	//					save the current map
	//			checking if king is checked
	//			checking if king is checkmated
	//			drawing map
	//additionally, since this is an infinite loop, it can infinitely restart the map after a win
	//after each win, it keeps a count of white wins, stalemates, and black wins
	*/
	public static void main(String[] args) 
	{
		//If file exists (presumably meaning there is a save in there), and the user accepts, this will load the data out of there through the loadMap() method
		if(file.exists())
		{
			System.out.println("There is a saved board, would you like to load it?\n   'y'\n   'n'");
			boolean valid = false;
			while(!valid)
			{
				String input = scan.nextLine();
				if(input.equals("y"))
				{
					valid = true;
					loadMap();
					loadedMap = true;
				}
				else if(input.equals("n"))
				{
					valid = true;
				}
				else
				{
					System.out.println("Only enter either 'y' or 'n.'");
				}
			}
		}
		drawMap();
		if(!loadedMap)
		{
			System.out.println("\nWhite goes first!\nFormat:\n   [piece],[place]\n   (Ex: \"wp1,a-3\")");
		}
		else
		{
			System.out.println("Continue!\nEnter 'o' for all options");
			if(turns %2 != 0)
			{
				System.out.println("White's move!");
			}
			else
			{
				System.out.println("Black's move!");
			}
		}
		boolean valid = false;
		while(!valid)
		{
			if(!loadedMap)
			{
				input = scan.nextLine();
				if(turns %2 != 0)
				{
					ally = 'w';
					enemy = 'b';
				}
				else
				{
					ally = 'b';
					enemy = 'w';
				}
				if(validPiece(input))
				{
					String[] options;
					if(input.charAt(0) == enemy)
					{
						options = toPiece(input, "", 1, ally);
					}
					else
					{
						options = toPiece(input, "", 1, enemy);
					}
					drawPieceOptions(options);
					movesDrawed = 2;
				}
				else if(input.equals("return") && movesDrawed > 0)
				{
					drawMap();
					System.out.println("Continue!");
					if(turns %2 != 0)
					{
						System.out.println("White's move!");
					}
					else
					{
						System.out.println("Black's move!");
					}
				}
				else
				{
					if(validate(input))
					{
						valid = true;
						toPiece(inputSplit[0], inputSplit[1], 0, enemy);
						if(!didntMove)
						{
							turns++;
							drawMap();
							System.out.println("Next turn!");
							System.out.println("Enter 'o' for all options");
						}
						else
						{
							didntMove = false;
							drawMap();
							System.out.println("You cannot move there!");
							System.out.println("Enter 'o' for all options");
						}
					}
					else
					{
						System.out.println("Try again.");
					}
				}
			}
			while(true)
			{	
				movesDrawed--;
				if(turns %2 != 0)
				{
					ally = 'w';
					enemy = 'b';
				}
				else
				{
					ally = 'b';
					enemy = 'w';
				}
				if(pawnMovedTwice)
				{
					deletePawnThatMovedTwice = true;
				}
				input = scan.nextLine();
				if(!askStaleMate)
				{
					if(input.equals("o"))
					{
						System.out.println("Format:\n   piece,place\n   (Ex: \"wp1,a-3\")\nEnter 's' to save the map\nEnter 'l' to load saved map\nEnter 'f' to forfeit(this will result in a win for your opponent)\nEnter 'sm' to suggest a stalemate\nEnter a piece to see all its moving options\n   EX: \"wp1\"\n");
						wait(3);
					}
					else if(input.equals("s"))
					{
						valid = true;
						saveMap();
						System.out.println("Map saved!\nContinue!");
					}
					else if(input.equals("l"))
					{
						valid = true;
						loadMap();
						drawMap();
						System.out.println("Map loaded!\nContinue!");
						if(turns %2 != 0)
						{
							System.out.println("White's move!");
						}
						else
						{
							System.out.println("Black's move!");
						}
					}
					else if(input.equals("sm"))
					{
						valid = true;
						askStaleMate = true;
//						resetMap('x');
						System.out.println("Stalemate suggested.  Accept?");
					}
					else if(input.equals("f"))
					{
						valid = true;
						System.out.println(String.valueOf(ally).toUpperCase() + " forfeits!");
						resetMap(enemy);
					}
					else if(input.equals("return") && movesDrawed > 0)
					{
						drawMap();
						System.out.println("Continue!");
						if(turns %2 != 0)
						{
							System.out.println("White's move!");
						}
						else
						{
							System.out.println("Black's move!");
						}
					}
					else if(validPiece(input))
					{
						String[] options;
						if(input.charAt(0) == enemy)
						{
							options = toPiece(input, "", 1, ally);
						}
						else
						{
							options = toPiece(input, "", 1, enemy);
						}
						drawPieceOptions(options);
						movesDrawed = 2;
					}
					else
					{
						if(validate(input))
						{
							toPiece(inputSplit[0], inputSplit[1], 0, enemy);
							if(!didntMove)
							{
								if(enemy == 'b')
								{
									if(checkCheck("b_k", positionOfPiece("b_k")))
									{
										if(checkMate("b_k"))
										{
											System.out.println("GAME OVER!");
											resetMap('w');
										}
										else
										{
											blackKingChecked = true;
										}
									}
									else if(checkCheck("w_k", positionOfPiece("w_k")))
									{
										occuMap = copyString(backupOccuMap);
										pieceList = copyString(backupPieceList);
										turns--;
									}
									else
									{
										whiteKingChecked = false;
									}
								}
								else
								{
									if(checkCheck("w_k", positionOfPiece("w_k")))
									{
										if(checkMate("w_k"))
										{
											drawMap();
											System.out.println("GAME OVER!");
											wait(3);
											resetMap('b');
										}
										else
										{
											whiteKingChecked = true;
										}
									}
									else if(checkCheck("b_k", positionOfPiece("b_k")))
									{
//										loadAll();
										occuMap = copyString(backupOccuMap);
										pieceList = copyString(backupPieceList);
										turns--;
									}
									else
									{
										blackKingChecked = false;
									}
								}
								turns++;
								drawMap();
								if(blackKingChecked)
								{
									System.out.println("Black king checked!");
								}
								else if(whiteKingChecked)
								{
									System.out.println("White king checked!");
								}
								System.out.println("Next turn!");
								System.out.println("Enter 'o' for all options");
								if(deletePawnThatMovedTwice)
								{
									pawnMovedTwice = false;
									deletePawnThatMovedTwice = false;
									pawnThatMoved = "";
									positionThatPawnMovedTo = "";
								}
							}
							else
							{
								didntMove = false;
								drawMap();
								System.out.println("You cannot move there!");
								System.out.println("Enter 'o' for all options");
							}
						
						}
						else
						{
							System.out.println("Try again.");
						}
					}
				}
				else
				{
					if(input.equals("y"))
					{
						System.out.println("Stalemate accepted!  Restarting...");
						wait(1);
						resetMap('x');
						askStaleMate = false;
					}
					else if(input.equals("n"))
					{
						System.out.println("Stalemate denied! Continue!");
						askStaleMate = false;
						wait(1);
						drawMap();
					}
					else
					{
						System.out.println("Please only enter 'y' or 'n' to accept or deny your opponent's stalemate suggestion.");
					}
				}
			}
		}
	}
	//makes sure that the inputed piece uses the correct format and a piece that has not been captured
	//used in the validate() method
	public static boolean validPiece(String piece)
	{
		for(int loop = 0; loop < pieceList.length; loop++)
		{
			if(pieceList[loop].equals(piece))
			{
				return true;
			}
		}
		return false;
	}
	//makes sure that the inputed position is an actual position on the map
	//used in the validate() method
	public static boolean validPosition(String position)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(POSMAP[rows][cols].equals(position))
				{
					return true;
				}
			}
		}
		return false;
	}
	//makes sure 'input' uses the correct format
	//format is:
	//		"[piece],[position]"
	public static boolean validate(String input) 
	{
		if(input.contains(","))
		{
			if(input.indexOf(',') == input.lastIndexOf(','))
			{
				inputSplit = input.split(",");
				if(validPiece(inputSplit[0]))
				{
					if((turns %2 != 0 && inputSplit[0].charAt(0) == 'w') || (turns %2 == 0 && inputSplit[0].charAt(0) == 'b'))
					{
						if(validPosition(inputSplit[1]))
						{
							return true;
						}
						else
						{
							System.out.println("Invalid position!");
						}
					}
					else
					{
						System.out.println("Not your piece!");
					}
				}
				else
				{
					System.out.println("Invalid piece!\nEither you entered a captured piece or misstyped the piece!");
				}
			}
			else
			{
				System.out.println("Make sure you include only ONE comma in your input!");
			}
		}
		else
		{
			System.out.println("Make sure you include a comma in your input!");
		}		
		return false;
	}	
	//draws map
	public static void drawMap()
	{
		StringBuilder sb = new StringBuilder();
		int col = 8;
		sb.append("\ta\tb\tc\td\te\tf\tg\th\n  ").append(col--).append("  ");
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				sb.append("[ ").append(occuMap[rows][cols]).append(" ] ");
			}
			if(rows != 7)
			{
				sb.append("\n  ").append(col--).append("  ");
			}
		}
		System.out.println(sb);
	}
	//resets map
	//used at the end of a game
	//also resets all of the important boolean, integer, and String values that are essential to each new game
	public static void resetMap(char winner)
	{
		occuMap = copyString(INITIALOCCUMAP);
		pieceList = copyString(INTITIALPIECELIST);
		turns = 1;
		pawnThatMoved = "";
		positionThatPawnMovedTo = "";
		pawnMovedTwice = false;
		deletePawnThatMovedTwice = false;
		whiteKingChecked = false;
		blackKingChecked = false;
		whiteKingMoved = false;
		blackKingMoved = false;
		wr1Moved = false;
		wr2Moved = false;
		br1Moved = false;
		br2Moved = false;
		loadedMap = false;
		whiteQueens = 1;
		blackQueens = 1;
		drawMap();
		if(winner == 'w')
		{
			System.out.println("White wins!");
			wWins++;
		}
		else if(winner == 'b')
		{
			System.out.println("Black wins!");
			bWins++;
		}
		else
		{
			System.out.println("Draw!");
			draws++;
		}
		System.out.println("Score: " + wWins + "-" + bWins + "-" + draws);
		wait(1);
		System.out.println("Continue!");
	}	
	//saves map to file
	//also saved all of the important boolean, String, and integer values that should be passed on from game to game
	//it checks if file exists, if it doesn't, it will create the file
	//it checks if the dierectory exists, if it doesn't it will create the directory
	public static void saveMap()
	{
		try
		{
			StringBuilder sb = new StringBuilder();
			if(!file.exists())
			{
				try
				{
					file.createNewFile();
				}
				catch(Exception e)
				{
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
			}
			BufferedWriter write = new BufferedWriter(new FileWriter(file));
			write.write(String.valueOf(turns));
			write.newLine();
			write.write(String.valueOf(wWins));
			write.newLine();
			write.write(String.valueOf(bWins));
			write.newLine();
			write.write(String.valueOf(draws));
			write.newLine();
			for(int rows = 0; rows < 8; rows++)
			{
				for(int cols = 0; cols < 8; cols++)
				{
					sb.append(occuMap[rows][cols]).append(",");
				}
				write.write(String.valueOf(sb));
				write.newLine();
				sb = new StringBuilder();
			}
			for(int pieces = 0; pieces < pieceList.length; pieces++)
			{
				sb.append(pieceList[pieces]).append(",");
			}
			write.write(String.valueOf(sb));
			write.newLine();
			if(pawnMovedTwice)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(deletePawnThatMovedTwice)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			write.write(pawnThatMoved);
			write.newLine();
			write.write(positionThatPawnMovedTo);
			write.newLine();
			if(whiteKingChecked)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(blackKingChecked)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(whiteKingMoved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(blackKingMoved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(wr1Moved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(wr2Moved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(br1Moved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			if(br2Moved)
			{
				write.write("t");
			}
			else
			{
				write.write("f");
			}
			write.newLine();
			write.write(String.valueOf(whiteQueens));
			write.newLine();
			write.write(String.valueOf(blackQueens));
			write.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	//loads map from file
	//also loads all the important boolean, String, and integer values that are stored in there
	public static void loadMap()
	{
		try
		{
			Scanner readFile = new Scanner(file);
			String[] allLines = new String[27];
			for(int loop = 0; loop < 27; loop++)
			{
				allLines[loop] = readFile.nextLine();
			}
			turns = Integer.valueOf(allLines[0]);
			wWins = Integer.valueOf(allLines[1]);
			bWins = Integer.valueOf(allLines[2]);
			draws = Integer.valueOf(allLines[3]);
			String[] one = allLines[4].split(",");
			String[] two = allLines[5].split(",");
			String[] three = allLines[6].split(",");
			String[] four = allLines[7].split(",");
			String[] five = allLines[8].split(",");
			String[] six = allLines[9].split(",");
			String[] seven = allLines[10].split(",");
			String[] eight = allLines[11].split(",");
			String[][] places = {one, two, three, four, five, six, seven, eight};
			for(int rows = 0; rows < 8; rows++)
			{
				for(int cols = 0; cols < 8; cols++)
				{
					occuMap[rows][cols] = places[rows][cols];
				}
			}
			String[] pieces = allLines[12].split(",");
			for(int loop = 0; loop < pieces.length; loop++)
			{
				pieceList[loop] = pieces[loop];
			}
			if(allLines[13].equals("t"))
			{
				pawnMovedTwice = true;
			}
			else
			{
				pawnMovedTwice = false;
			}
			if(allLines[14].equals("t"))
			{
				deletePawnThatMovedTwice = true;
			}
			else
			{
				deletePawnThatMovedTwice = false;
			}
			pawnThatMoved = allLines[15];
			positionThatPawnMovedTo = allLines[16];
			if(allLines[17].equals("t"))
			{
				whiteKingChecked = true;
			}
			else
			{
				whiteKingChecked = false;
			}
			if(allLines[18].equals("t"))
			{
				blackKingChecked = true;
			}
			else
			{
				blackKingChecked = false;
			}
			if(allLines[19].equals("t"))
			{
				whiteKingMoved = true;
			}
			else
			{
				whiteKingMoved = false;
			}
			if(allLines[20].equals("t"))
			{
				blackKingMoved = true;
			}
			else
			{
				blackKingMoved = false;
			}
			if(allLines[21].equals("t"))
			{
				wr1Moved = true;
			}
			else
			{
				wr1Moved = false;
			}
			if(allLines[22].equals("t"))
			{
				wr2Moved = true;
			}
			else
			{
				wr2Moved = false;
			}
			if(allLines[23].equals("t"))
			{
				br1Moved = true;
			}
			else
			{
				br1Moved = false;
			}
			if(allLines[24].equals("t"))
			{
				br2Moved = true;
			}
			else
			{
				br2Moved = false;
			}
			whiteQueens = Integer.valueOf(allLines[25]);
			blackQueens = Integer.valueOf(allLines[26]);
			readFile.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("An error occured.\nMap failed to load.");
		}
	}
	//Finds the position of the piece on the posMap
	public static String positionOfPiece(String piece)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(occuMap[rows][cols].equals(piece))
				{
					return POSMAP[rows][cols];
				}
			}
		}
		return null;
	}
	//Finds the piece at 'position' on posMap
	public static String pieceAtPosition(String position)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(POSMAP[rows][cols].equals(position))
				{
					return occuMap[rows][cols];
				}
			}
		}
		return null;
	}
	//Finds the integers within the posMap arrays that 'piece' is at 
	public static int[] positionArrayOfPiece(String piece)
	{
		int[] array = new int[2];
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(occuMap[rows][cols].equals(piece))
				{
					array[0] = rows;
					array[1] = cols;		
					return array;
				}
			}
		}
		return null;
	}
	//Finds the integers within the posMap arrays that 'position' is at 
	public static int[] positionArrayOfPosition(String position)
		{
			int[] array = new int[2];
			for(int rows = 0; rows < 8; rows++)
			{
				for(int cols = 0; cols < 8; cols++)
				{
					if(POSMAP[rows][cols].equals(position))
					{
						array[0] = rows;
						array[1] = cols;		
						return array;
					}
				}
			}
			return null;
		}
	//used to see if a pawn can move forward twice
	//Finds the original position of the piece
	public static String findPieceOriginal(String piece)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(INITIALOCCUMAP[rows][cols].equals(piece))
				{
					return POSMAP[rows][cols];
				}
			}
		}
		return null;
	}
	//used to see if a pawn can move forward twice
	//Finds if the piece is at its original position
	public static boolean checkOriginalPosition(String piece)
	{
		if(positionOfPiece(piece).equals(findPieceOriginal(piece)))
		{
			return true;
		}
		return false;
	}
	//removes 'piece' from 'pieceList'
	//used when a piece is captured
	public static void removeFromPieceList(String piece)
	{
		for(int loop = 0; loop < pieceList.length; loop++)
		{
			if(pieceList[loop].equals(piece))
			{
				pieceList[loop] = "0";
			}
		}
	}
	//this adds a new piece to the piece list
	//		(this is basically only used when a pawn gets to the end of a map and it transforms into a queen)
	public static void addToPieceList()
	{
		String[] newPieceList = new String[pieceList.length + 1];
		for(int loop = 0; loop < pieceList.length; loop++)
		{
			newPieceList[loop] = pieceList[loop];
		}
		if(turns %2 != 0)
		{
			newPieceList[newPieceList.length - 1] = "wq" + whiteQueens;
			whiteQueens++;
		}
		else
		{
			newPieceList[newPieceList.length - 1] = "bq" + blackQueens;
			blackQueens++;
		}
		pieceList = copyString(newPieceList);
	}
	//checks if king would be checked in the string position
	public static boolean checkCheck(String king, String position)
	{
		int[] initialPosNos = positionArrayOfPiece(king);
		int[] posPosNos = positionArrayOfPosition(position);
//		occuMap[initialPosNos[0]][initialPosNos[1]] = "   ";
		String[][] bOccu = copyString(occuMap);
		occuMap[posPosNos[0]][posPosNos[1]] = king;
		occuMap[initialPosNos[0]][initialPosNos[1]] = "   ";
//		occuMap[posPosNos[0]][posPosNos[1]] = king;
		String[] allOptions = {};
		char enemy = 'b', ally = 'w';
		if(king.charAt(0) == 'b')
		{
			ally = enemy;
			enemy = 'w';
		}
		for(int loop = 0; loop < pieceList.length; loop++)
		{
			if(pieceList[loop].charAt(0) == enemy)
			{
				allOptions = addStringArray(allOptions, toPiece(pieceList[loop], "", 2, ally));
			}
		}
		for(int loop = 0; loop < allOptions.length; loop++)
		{
			if(allOptions[loop] != null)
			{
				if(allOptions[loop].equals(position))
				{
					occuMap = copyString(bOccu);
					return true;
				}
			}
		}
		occuMap = copyString(bOccu);
		return false;
	}
	//Checks if 'king' is checkmated
	public static boolean checkMate(String king)
	{
		char enemy = 'w';
		if(king.charAt(0) == 'w')
		{
			enemy = 'b';
		}
		int nullCount = 0;
		String[] options = toPiece(king, "", 1, enemy);
		for(String g: options)
		{
			if(g == null)
				nullCount++;
		}
		if(nullCount == options.length)
		{
			return true;
		}
		return false;
	}
	//puts options on map
	//draws them as X's
	public static void drawPieceOptions(String[] options)
	{
		backupOccuMap = copyString(occuMap);
		backupPieceList = copyString(pieceList);
		int[] posNos;
		for(int loop = 0; loop < options.length; loop++)
		{
			if(options[loop] != null)
			{
				posNos = positionArrayOfPosition(options[loop]);
				occuMap[posNos[0]][posNos[1]] = " X ";
			}
		}
		drawMap();
		System.out.println("Continue!\n   Enter 'return' to remove the X's");
		occuMap = copyString(backupOccuMap);
		pieceList = copyString(backupPieceList);
	}
	//this detects if the position the piece wants to move to is in the String[] of all its possible moving options
	//if true:
	//	 it returns a String beginning with "t," with the position in the options[] that the position is found
	//if false:
	//   returns "f"
	//the reason that this does not return a boolean is that pawns need to know if the moving option was attacking (diagonal) or if it was merely moving forward
	public static String moveTooTrue(String positionToMoveTo, String[] options)
	{
		for(int loop = 0; loop < options.length; loop++)
		{
			if(positionToMoveTo.equals(options[loop]))
			{
				return "t," + loop;
			}
		}
		return "f";
	}
	/*
	//NOTE: pawn, rook, and king do not use these because they have more complicated moving functions
	//		(king needs to make sure it's not being checked when it moved, and it needs to check if it's able to castle)
	//		(pawn has a few annoying problems with it such as en passant, tansfroming into a queen, 
	//			as well as the fact that its attacking pattern is different from its moving pattern)
	//		(rook needs to keep track of if it has moved so king can see if it can castle)
	*/
	public static void defaultMove(String positionMoveTo, String[] options, String piece, int[] positionNumbers)
	{
		if(moveTooTrue(positionMoveTo, options).charAt(0) == 't')
		{
			int[] enemyPosNos = positionArrayOfPosition(positionMoveTo);
			if(occuMap[enemyPosNos[0]][enemyPosNos[1]].charAt(0) == enemy)
			{
				removeFromPieceList(occuMap[enemyPosNos[0]][enemyPosNos[1]]);
			}
			occuMap[enemyPosNos[0]][enemyPosNos[1]] = piece;
			occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
		}
		else
		{
			didntMove = true;
		}
	}
	/*
	//these are the methods for the movement of every single type of piece
	//int[] positionNumbers = the integer values for the position of the given piece on the occuMap
	//String positionMoveTo = the String interpretation of the of the desired position to move to (Ex: "a-3", "h-8", etc.)
	//String piece = the String interpretation of the piece's identification (Ex: "wp1", "w_q", "b_k", etc.)
	//int from = decides where this method is being called from
	//		0 = from normal movement
	//		1 = from drawing options
	//		2 = from check
	//	this is used mostly to help with the king's movement
	//char enemy = the character identification for the enemy (Ex: 'b', 'w')
	*/
	public static String[] pawn(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		String[] options = new String[6];
		int allyQ = whiteQueens, allyOp = -1, max = 0;
		char ally = 'w';
		if(enemy == 'w')
		{
			ally = 'b';
			allyQ = blackQueens;
			allyOp = 1;
			max = 7;
		}
		if(positionNumbers[0] != max && positionNumbers[1] != 0)
		{
			if(occuMap[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] - 1].charAt(0) == enemy || from == 1)
			{
				options[2] = POSMAP[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] - 1];
			}
		}
		if(positionNumbers[0] != max && positionNumbers[1] != 7)
		{
			if(occuMap[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] + 1].charAt(0) == enemy || from == 1)
			{
				options[3] = POSMAP[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] + 1];
			}
		}
		if(from == 1 || from == 2)
		{
			String[] attackingOptions = {options[2], options[3]};
			return attackingOptions;
		}
		if(positionNumbers[0] != max)
		{
			if(occuMap[positionNumbers[0] + (1 * allyOp)][positionNumbers[1]].equals("   "))
			{
				options[0] = POSMAP[positionNumbers[0] + (1 * allyOp)][positionNumbers[1]];
			}
		}
		if(checkOriginalPosition(piece))
		{
			if(occuMap[positionNumbers[0] + (1 * allyOp)][positionNumbers[1]].equals("   ") && occuMap[positionNumbers[0] + (2 * allyOp)][positionNumbers[1]].equals("   "))
			{
				if(occuMap[positionNumbers[0] + (2 * allyOp)][positionNumbers[1]].equals("   "))
				{
					options[1] = POSMAP[positionNumbers[0] + (2 * allyOp)][positionNumbers[1]];
				}
			}
		}
		if(pawnMovedTwice && pawnThatMoved.charAt(0) == enemy)
		{
			int[] posOfPawnssant = positionArrayOfPosition(positionThatPawnMovedTo);
			if(positionNumbers[0] == posOfPawnssant[0])
			{
				if(posOfPawnssant[1] != 0)
				{
					if(positionNumbers[1] - 1 == posOfPawnssant[1])
					{
						options[4] = POSMAP[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] - 1];
					}
				}
				if(posOfPawnssant[1] != 7)
				{
					if(positionNumbers[1] + 1 == posOfPawnssant[1])
					{
						options[5] = POSMAP[positionNumbers[0] + (1 * allyOp)][positionNumbers[1] + 1];
					}
				}
			}
		}
		
		if(moveTooTrue(positionMoveTo, options).charAt(0) == 't')
		{

			String optionUsed = moveTooTrue(positionMoveTo, options).split(",")[1];
			if(optionUsed.equals("1"))
			{
				pawnMovedTwice = true;
				pawnThatMoved = piece;
				positionThatPawnMovedTo = positionMoveTo;
			}

			if(optionUsed.equals("2") || optionUsed.equals("3"))
			{
				removeFromPieceList(pieceAtPosition(positionMoveTo));
			}
			if(optionUsed.equals("4"))
			{
				removeFromPieceList(pieceAtPosition(POSMAP[positionNumbers[0]][positionNumbers[1] - 1]));
				occuMap[positionNumbers[0]][positionNumbers[1] - 1] = "   ";
			}
			if(optionUsed.equals("5"))
			{
				removeFromPieceList(pieceAtPosition(POSMAP[positionNumbers[0]][positionNumbers[1] + 1]));
				occuMap[positionNumbers[0]][positionNumbers[1] + 1] = "   ";
			}
			if(positionArrayOfPosition(positionMoveTo)[0] != max)
			{
				occuMap[positionArrayOfPosition(positionMoveTo)[0]][positionArrayOfPosition(positionMoveTo)[1]] = piece;
			}
			else
			{
				removeFromPieceList(piece);
				occuMap[positionArrayOfPosition(positionMoveTo)[0]][positionArrayOfPosition(positionMoveTo)[1]] = ally + "q" + allyQ;
				addToPieceList();
			}
			occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
		}
		else
		{
			didntMove = true;
		}
		return null;
	}
	public static String[] knight(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		String[] options = new String[8];
		char ally = 'w';
		if(enemy == 'w')	
		{
			ally = 'b';
		}
//		55 lines (old way) vs. 20 lines (new way)
		int counter = 0;
		for(int loop = -2; loop <= 2; loop += 4)
		{
			for(int loop2 = -1; loop2 <= 1; loop2 += 2)
			{
				if(positionNumbers[0] + loop >= 0 && positionNumbers[0] + loop <= 7 && positionNumbers[1] + loop2 >= 0 && positionNumbers[1] + loop2 <= 7)
				{
					if(occuMap[positionNumbers[0] + loop][positionNumbers[1] + loop2].charAt(0) != ally)
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1] + loop2];
					}
				}
				if(positionNumbers[0] + loop2 >= 0 && positionNumbers[0] + loop2 <= 7 && positionNumbers[1] + loop >= 0 && positionNumbers[1] + loop <= 7)
				{
					if(occuMap[positionNumbers[0] + loop2][positionNumbers[1] + loop].charAt(0) != ally)
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop2][positionNumbers[1] + loop];
					}
				}
			}
		}
		if(from == 1 || from == 2)
		{
			return options;
		}
		defaultMove(positionMoveTo, options, piece, positionNumbers);
		return null;
	}
	public static String[] rook(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		String[] options = new String[14];
		int counter = 0;
		//old way: 77 lines, 4 loops
		//new way: 69 lines, 1 loop
		boolean[] stopDir = {false,false,false,false}; 
		for(int loop = 1; loop <= 7; loop++)
		{
			//up
			if(positionNumbers[0] - loop >= 0 && !stopDir[0])
			{
				if(occuMap[positionNumbers[0] - loop][positionNumbers[1]].equals("   "))
				{
					options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1]];
				}
				else if(occuMap[positionNumbers[0] - loop][positionNumbers[1]].charAt(0) == enemy)
				{
					options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1]];
					stopDir[0] = true;
				}
				else
				{
					stopDir[0] = true;
				}
			}
			//down
			if(positionNumbers[0] + loop <= 7 && !stopDir[1])
			{
				if(occuMap[positionNumbers[0] + loop][positionNumbers[1]].equals("   "))
				{
					options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1]];
				}
				else if(occuMap[positionNumbers[0] + loop][positionNumbers[1]].charAt(0) == enemy)
				{
					options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1]];
					stopDir[1] = true;
				}
				else
				{
					stopDir[1] = true;
				}
			}
			//left
			if(positionNumbers[1] - loop >= 0 && !stopDir[2])
			{
				if(occuMap[positionNumbers[0]][positionNumbers[1] - loop].equals("   "))
				{
					options[counter++] = POSMAP[positionNumbers[0]][positionNumbers[1] - loop];
				}
				else if(occuMap[positionNumbers[0]][positionNumbers[1] - loop].charAt(0) == enemy)
				{
					options[counter++] = POSMAP[positionNumbers[0]][positionNumbers[1] - loop];
					stopDir[2] = true;
				}
				else
				{
					stopDir[2] = true;
				}
			}
			//right
			if(positionNumbers[1] + loop <= 7 && !stopDir[3])
			{
				if(occuMap[positionNumbers[0]][positionNumbers[1] + loop].equals("   "))
				{
					options[counter++] = POSMAP[positionNumbers[0]][positionNumbers[1] + loop];
				}
				else if(occuMap[positionNumbers[0]][positionNumbers[1] + loop].charAt(0) == enemy)
				{
					options[counter++] = POSMAP[positionNumbers[0]][positionNumbers[1] + loop];
					stopDir[3] = true;
				}
				else
				{
					stopDir[3] = true;
				}
			}
		}
		if(from == 1 || from == 2)
		{
			return options;
		}
		if(moveTooTrue(positionMoveTo, options).charAt(0) == 't')
		{
			int[] enemyPosNos = positionArrayOfPosition(positionMoveTo);
			if(occuMap[enemyPosNos[0]][enemyPosNos[1]].charAt(0) == enemy)
			{
				removeFromPieceList(occuMap[enemyPosNos[0]][enemyPosNos[1]]);
			}
			occuMap[enemyPosNos[0]][enemyPosNos[1]] = piece;
			occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
			if(piece == "wr1")
			{
				wr1Moved = true;
			}
			else if(piece == "wr2")
			{
				wr2Moved = true;
			}

			else if(piece == "br1")
			{
				br1Moved = true;
			}

			else if(piece == "br2")
			{
				br2Moved = true;
			}
		}
		else
		{
			didntMove = true;
		}
		return null;
	}
	public static String[] bishop(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		String[] options = new String[14];
		int counter = 0;
		//old way: 106 lines, 4 loops
		//new way: 79 lines, 1 loop
		boolean[] stopDir = {false,false,false,false};
		for(int loop = 1; loop <= 7; loop++)
		{
			//up
			if(positionNumbers[0] - loop >= 0)
			{
				//left
				if(positionNumbers[1] - loop >= 0 && !stopDir[0])
				{
					if(occuMap[positionNumbers[0] - loop][positionNumbers[1] - loop].equals("   "))
					{
						options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1] - loop];
					}
					else if(occuMap[positionNumbers[0] - loop][positionNumbers[1] - loop].charAt(0) == enemy)
					{
						options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1] - loop];
						stopDir[0] = true;
					}
					else
					{
						stopDir[0] = true;
					}
				}
				//right
				if(positionNumbers[1] + loop <= 7 && !stopDir[1])
				{
					if(occuMap[positionNumbers[0] - loop][positionNumbers[1] + loop].equals("   "))
					{
						options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1] + loop];
					}
					else if(occuMap[positionNumbers[0] - loop][positionNumbers[1] + loop].charAt(0) == enemy)
					{
						options[counter++] = POSMAP[positionNumbers[0] - loop][positionNumbers[1] + loop];
						stopDir[1] = true;
					}
					else
					{
						stopDir[1] = true;
					}
				}
			}
			//down
			if(positionNumbers[0] + loop <= 7)
			{
				//left
				if(positionNumbers[1] - loop >= 0 && !stopDir[2])
				{
					if(occuMap[positionNumbers[0] + loop][positionNumbers[1] - loop].equals("   "))
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1] - loop];
					}
					else if(occuMap[positionNumbers[0] + loop][positionNumbers[1] - loop].charAt(0) == enemy)
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop ][positionNumbers[1] - loop];
						stopDir[2] = true;
					}
					else
					{
						stopDir[2] = true;
					}
				}
				//right
				if(positionNumbers[1] + loop <= 7 && !stopDir[3])
				{
					if(occuMap[positionNumbers[0] + loop][positionNumbers[1] + loop].equals("   "))
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1] + loop];
					}
					else if(occuMap[positionNumbers[0] + loop][positionNumbers[1] + loop].charAt(0) == enemy)
					{
						options[counter++] = POSMAP[positionNumbers[0] + loop ][positionNumbers[1] + loop];
						stopDir[3] = true;
					}
					else
					{
						stopDir[3] = true;
					}
				}
			}
		}
		if(from == 1 || from == 2)
		{
			return options;
		}
		defaultMove(positionMoveTo, options, piece, positionNumbers);
		return null;
	}
	public static String[] queen(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		//75 (bishop method) + 65 (rook method) + 13 (default moving) = 153 lines (old way) vs. 5 lines (new way)
		String[] options = addStringArray(bishop(positionNumbers, null, null, 2, enemy), rook(positionNumbers, null, null, 2, enemy));
		if(from == 1 || from == 2)
		{
			return options;
		}
		defaultMove(positionMoveTo, options, piece, positionNumbers);
		return null;
	}
	public static String[] king(int[] positionNumbers, String positionMoveTo, String piece, int from, char enemy)
	{
		String[] options = new String[10];
		boolean castleOptionsOne = false, castleOptionsTwo = false, castleOptionsThree = false, castleOptionsFour = false;
		//63 lines (old way) vs. 14 lines (new way)
		int counter = 0;
		for(int loop = -1; loop < 2; loop++)
		{
			for(int loop2 = -1; loop2 < 2; loop2++)
			{
				if(positionNumbers[0] + loop >= 0 && positionNumbers[0] + loop <= 7 && positionNumbers[1] + loop2 >= 0 && positionNumbers[1] + loop2 <= 7)
				{
					if((occuMap[positionNumbers[0] + loop][positionNumbers[1] + loop2].charAt(0) == enemy || 
							occuMap[positionNumbers[0] + loop][positionNumbers[1] + loop2].equals("   "))
							&& (from != 2 && !checkCheck(piece, POSMAP[positionNumbers[0] + loop][positionNumbers[1] + loop2])))
					{
						if(!((loop == loop2) && loop == 0))
						{
							options[counter++] = POSMAP[positionNumbers[0] + loop][positionNumbers[1] + loop2];
						}
					}
				}
			}
		}
		if(from == 1 || from == 2)
		{
			return options;
		}
		if(turns %2 != 0)
		{
			if(!whiteKingMoved && !whiteKingChecked)
			{
				if(!wr1Moved && occuMap[positionNumbers[0]][positionNumbers[1] - 1].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] - 2].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] - 3].equals("   "))
				{
					options[8] = POSMAP[positionNumbers[0]][positionNumbers[1] - 2];
					castleOptionsOne = true;
				}
				if(!wr2Moved && occuMap[positionNumbers[0]][positionNumbers[1] + 1].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] + 2].equals("   "))
				{
					options[9] = POSMAP[positionNumbers[0]][positionNumbers[1] + 2];
					castleOptionsTwo = true;
				}
			}
		}
		else
		{
			if(!blackKingMoved && !blackKingChecked)
			{
				if(!br1Moved && occuMap[positionNumbers[0]][positionNumbers[1] - 1].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] - 2].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] - 3].equals("   "))
				{
					options[8] = POSMAP[positionNumbers[0]][positionNumbers[1] - 2];
					castleOptionsThree = true;
				}
				if(!br2Moved && occuMap[positionNumbers[0]][positionNumbers[1] + 1].equals("   ") && occuMap[positionNumbers[0]][positionNumbers[1] + 2].equals("   "))
				{
					options[9] = POSMAP[positionNumbers[0]][positionNumbers[1] + 2];
					castleOptionsFour = true;
				}
			}
		}
		String moveTrue = moveTooTrue(positionMoveTo, options);
		if(moveTrue.charAt(0) == 't')
		{
			int[] enemyPosNos = positionArrayOfPosition(positionMoveTo);
			if(moveTrue.charAt(2) == '8' || moveTrue.charAt(2) == '9')
			{
				if(castleOptionsOne && moveTrue.charAt(2) == '8')
				{
					if(!checkCheck("w_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 1]))
					{
						if(!checkCheck("w_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 2]))
						{
							occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 2] = piece; 
							int[] rookPlaces = positionArrayOfPiece("wr1");
							occuMap[rookPlaces[0]][rookPlaces[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 1] = "wr1";
						}
						else
						{
							didntMove = true;
						}
					}
					else
					{
						didntMove = true;
					}
				}
				if(castleOptionsTwo && moveTrue.charAt(2) == '9')
				{
					if(!checkCheck("w_k", POSMAP[positionNumbers[0]][positionNumbers[1] + 1]))
					{
						if(!checkCheck("w_k", POSMAP[positionNumbers[0]][positionNumbers[1] + 2]))
						{
							occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] + 2] = piece; 
							int[] rookPlaces = positionArrayOfPiece("wr2");
							occuMap[rookPlaces[0]][rookPlaces[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] + 1] = "wr2";
						}
						else
						{
							didntMove = true;
						}
					}
					else
					{
						didntMove = true;
					}
				}
				if(castleOptionsThree && moveTrue.charAt(2) == '8')
				{
					if(!checkCheck("b_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 1]))
					{
						if(!checkCheck("b_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 2]))
						{
							occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 2] = piece; 
							int[] rookPlaces = positionArrayOfPiece("br1");
							occuMap[rookPlaces[0]][rookPlaces[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 1] = "br1";
						}
						else
						{
							didntMove = true;
						}
					}
					else
					{
						didntMove = true;
					}
				}
				if(castleOptionsFour && moveTrue.charAt(2) == '9')
				{
					if(!checkCheck("b_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 1] ))
					{
						if(!checkCheck("b_k", POSMAP[positionNumbers[0]][positionNumbers[1] - 2]))
						{
							occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 2] = piece;
							int[] rookPlaces = positionArrayOfPiece("br1");
							occuMap[rookPlaces[0]][rookPlaces[1]] = "   ";
							occuMap[positionNumbers[0]][positionNumbers[1] - 1] = "br1";
						}
						else
						{
							didntMove = true;
						}
					}
					else
					{
						didntMove = true;
					}
				}
			}
			else
			{
				occuMap[positionNumbers[0]][positionNumbers[1]] = "   ";
				if(occuMap[enemyPosNos[0]][enemyPosNos[1]].charAt(0) == enemy)
				{
					removeFromPieceList(occuMap[enemyPosNos[0]][enemyPosNos[1]]);
				}
				occuMap[enemyPosNos[0]][enemyPosNos[1]] = piece;
			}
			if(turns %2 != 0)
			{
				whiteKingMoved = true;
			}
			else
			{
				blackKingMoved = true;
			}
		}
		else
		{
			didntMove = true;
		}
		return null;
	}
	//this is used to make the main method less THICC (even though it is already very THICC as it is)
	//this method is simply called by the main method to direct String piece to its correct method
	//it is also used in the checkCheck() and checkMate() methods to thin down those methods as well
	public static String[] toPiece(String piece, String moveTo, int from, char enemy)
	{
		backupOccuMap = copyString(occuMap);
		backupPieceList = copyString(pieceList);
		int[] posNos = positionArrayOfPiece(piece);
		if(piece.charAt(1) == 'p')
		{
			return pawn(posNos, moveTo, piece, from, enemy);
		}
		else if(piece.charAt(1) == 'k')
		{
			return knight(posNos, moveTo, piece, from, enemy);
		}
		else if(piece.charAt(1) == 'r')
		{
			return rook(posNos, moveTo, piece, from, enemy);
		}
		else if(piece.charAt(1) == 'b')
		{
			return bishop(posNos, moveTo, piece, from, enemy);
		}
		else if(piece.charAt(2) == 'q' || piece.charAt(1) == 'q' )
		{
			
			return queen(posNos, moveTo, piece, from, enemy);
		}
		else if(piece.charAt(2) == 'k')
		{
			return king(posNos, moveTo, piece, from, enemy);
		}
		return null;
	}
	//waits for @seconds seconds
	public static void wait(int seconds)
	{
		try 
		{
			TimeUnit.SECONDS.sleep(seconds);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
}
class EditArray 
{
	//copies String[][] original into String[][] copy; you can make changes to copy without it changing original
	public static String[][] copyString(String[][] original)
	{
		int longest = 0;
		for(int loop = 0; loop < original.length; loop++)
		{
			if(original[loop].length > longest)
			{
				longest = original[loop].length;
			}
		}
		String[][] copy = new String[original.length][longest];
		for(int loop = 0; loop < original.length; loop++)
		{
			for(int loop2 = 0; loop2 < original[loop].length; loop2++)
			{
				copy[loop][loop2] = new String(original[loop][loop2]);
			}
		}
		return copy;
	}
	//copies String[] original into String[] copy; you can make changes to copy without it changing original
	public static String[] copyString(String[] original)
	{
		String[] copy = new String[original.length];
		for(int loop = 0; loop < original.length; loop++)
		{
			copy[loop] = new String(original[loop]);
		}
		return copy;
	}
	//adds an array of Strings to another array of Strings and returns the new array of Strings
	public static String[] addStringArray(String[] array, String[] addTo)
	{
		String[] i = new String[addTo.length + array.length];
		for(int loopArray = 0; loopArray < array.length; loopArray++)
		{
			i[loopArray] = array[loopArray];
		}
		int newLoop = 0;
		for(int loopAddTo = array.length; loopAddTo < i.length; loopAddTo++)
		{
			i[loopAddTo] = addTo[newLoop++];
		}
		return i;
	}
}


