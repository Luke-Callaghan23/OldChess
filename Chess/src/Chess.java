import java.util.Scanner;
import java.util.concurrent.TimeUnit;
public class Chess 
{
	//records turn numbers.
	public static double turnNo = 2;
	//records wins and draws
	public static int whiteWins = 0, blackWins = 0, draws = 0;
	//initial map of pieces (used when reseting the board)
	public static final String[][] initialOccuMap = {
			{"br1","bk1","bb1","b_q","b_k","bb2","bk2","br2"},
			{"bp1","bp2","bp3","bp4","bp5","bp6","bp7","bp8"},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"   ","   ","   ","   ","   ","   ","   ","   "},
			{"wp1","wp2","wp3","wp4","wp5","wp6","wp7","wp8"},
			{"wr1","wk1","wb1","w_q","w_k","wb2","wk2","wr2"}};
	//full list of all pieces
	public static final String[] initialPieceList = {
			"br1","bk1","bb1","b_q","b_k","bb2","bk2","br2",
			"bp1","bp2","bp3","bp4","bp5","bp6","bp7","bp8",
			"wp1","wp2","wp3","wp4","wp5","wp6","wp7","wp8",
			"wr1","wk1","wb1","w_q","w_k","wb2","wk2","wr2"};
	//map of every position on the board
	public static final String[][] posMap = {
			{"a-8","b-8","c-8","d-8","e-8","f-8","g-8","h-8"},
		    {"a-7","b-7","c-7","d-7","e-7","f-7","g-7","h-7"},
	     	{"a-6","b-6","c-6","d-6","e-6","f-6","g-6","h-6"},
			{"a-5","b-5","c-5","d-5","e-5","f-5","g-5","h-5"},
			{"a-4","b-4","c-4","d-4","e-4","f-4","g-4","h-4"},
			{"a-3","b-3","c-3","d-3","e-3","f-3","g-3","h-3"},
			{"a-2","b-2","c-2","d-2","e-2","f-2","g-2","h-2"},
			{"a-1","b-1","c-1","d-1","e-1","f-1","g-1","h-1"}};
	//map of pieces
	public static String[][] occuMap = initialOccuMap;
	//list of living pieces
	public static String[] pieceList = initialPieceList;
	//draws the map
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
	public static void resetMap()
	{
		occuMap = initialOccuMap;
		pieceList = initialPieceList;
		turnNo = 1;
		if(turnNo % 2 != 0)
		{
			blackWins++;
		}
		else
		{
			whiteWins++;
		}
		drawMap();
		System.out.println("Board reset!");
		System.out.println("Score: " + whiteWins + "-" + blackWins + "-" + draws);
	}
	//finds the location of any piece on the map
	public static String findPiece(String piece)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(occuMap[rows][cols].equals(piece))
					return posMap[rows][cols];
			}
		}
		return null;
	}
	//finds the location of the piece on the original occupy map
	public static String findPieceOriginal(String piece)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(initialOccuMap[rows][cols].equals(piece))
					return posMap[rows][cols];
			}
		}
		return null;
	}
	//returns piece at moveTo
	public static String findPieceAt(String moveTo)
	{
		int[] posNos = posNos(moveTo);
		for(int loopPieces = 0; loopPieces < pieceList.length; loopPieces++)
		{
			if(occuMap[posNos[0]][posNos[1]].equals(pieceList[loopPieces]))
			{
				return occuMap[posNos[0]][posNos[1]];
			}
		}
		return null;
	}
	//checks if piece is at its original position
	public static boolean originalPos(String piece)
	{
		if(findPiece(piece) == findPieceOriginal(piece))
		{
			return true;
		}
		return false;
	}
	//finds the position on position map in terms of columns and rows
	public static int[] posNos(String position)
	{
		int[] pos = new int[2];
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(posMap[rows][cols].equals(position))
				{
					pos[0] = rows; pos[1] = cols;
					return pos;
				}
			}
		}
		return null;
	}
	//validates input
	public static boolean validate(String input)
	{
		if(input.contains(","))
		{
			if(input.indexOf(',') == input.lastIndexOf(','))
			{
				String[] split = input.split(",");
				if(validPiece(split[0]))
				{
					if(validPos(split[1]))
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
	//checks if piece player is trying to move is still alive & if it is a valid piece
	public static boolean validPiece(String piece)
	{
		for(int loopPieces = 0; loopPieces < 32; loopPieces++)
		{
			if(pieceList[loopPieces].equals(piece))
			{
				return true;
			}
		}
		return false;
	}
	//checks if position player wants to move their piece to is a valid position (NOT IF THE PIECE *CAN* MOVE THERE!!!!)
	public static boolean validPos(String moveTo)
	{
		for(int rows = 0; rows < 8; rows++)
		{
			for(int cols = 0; cols < 8; cols++)
			{
				if(posMap[rows][cols].equals(moveTo))
				{	
					return true;
				}
			}
		}
		return false;
	}
	//just putting this here to make main less T H I C C
	public static String[] goToPiece(String piece, String moveTo, boolean fromCheck)
	{
		if(piece.charAt(1) == 'p')
		{
			if(!fromCheck)
				pawn(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return pawn(posNos(findPiece(piece)), moveTo, piece, true);
		}
		else if(piece.charAt(1) == 'r')
		{
			if(!fromCheck)
				rook(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return rook(posNos(findPiece(piece)), moveTo, piece, true);
		}
		else if(piece.charAt(1) == 'k')
		{
			if(!fromCheck)
				knight(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return knight(posNos(findPiece(piece)), moveTo, piece, true);
		}
		else if(piece.charAt(1) == 'b')
		{
			if(!fromCheck)
				bishop(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return bishop(posNos(findPiece(piece)), moveTo, piece, true);
		}
		else if(piece.charAt(2) == 'q')
		{
			if(!fromCheck)
				queen(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return queen(posNos(findPiece(piece)), moveTo, piece, true);
		}
		else if(piece.charAt(2) == 'k')
		{
			if(!fromCheck)
				king(posNos(findPiece(piece)), moveTo, piece, false);
			else
				return king(posNos(findPiece(piece)), moveTo, piece, true);
		}
		return null;
	}
	//checks if moveTo is is a valid option
	public static String checkMoveTo(String moveTo, String[] options)
	{
		for(int loop = 0; loop < options.length; loop++)
		{
			if(moveTo.equals(options[loop]))
				return "t," + loop;
		}
		return "f";
	}
	public static String enPassant = "f";
	public static boolean deleteEnPassant = false;
	public static int wqn = 1, bqn = 1;
	//returns which index in the pieceList array the piece is
	public static int findInPieceList(String piece)
	{
		for(int loop = 0; loop < pieceList.length; loop++)
		{
			if(pieceList[loop].equals(piece))
			{
				return loop;
			}
		}
		return 0;
	}
	//removes a piece from the piece list
	public static void removePieceList(String piece)
	{
		for(int loop = 0; loop < 32; loop++)
		{
			if(pieceList[loop] == piece)
			{
				pieceList[loop] = "";
			}
		}
	}	
	public static boolean checkCheck(String kingPos, String king)
	{
		boolean resetTurnNo = false;
		String[] allOptions = {""};
		if(king.charAt(0) == 'w')
		{
			if(turnNo % 2 != 0)
			{
				turnNo++;
				resetTurnNo = true;
			}
			for(int loop = 0; loop < pieceList.length; loop++)
			{
				if(pieceList[loop] != "")
				{
					if(pieceList[loop].charAt(0) == 'b' && pieceList[loop].charAt(2) != 'k')
					{
						allOptions = EditArray.addStringArray(allOptions, goToPiece(pieceList[loop], "", true));
					}
				}
			}
		}
		else
		{
			if(turnNo % 2 == 0)
			{
				turnNo++;
				resetTurnNo = true;
			}
			for(int loop = 0; loop < pieceList.length; loop++)
			{
				if(pieceList[loop] != "")
				{
					if(pieceList[loop].charAt(0) == 'w' && pieceList[loop].charAt(2) != 'k')
					{
						allOptions = EditArray.addStringArray(allOptions, goToPiece(pieceList[loop], "", true));
					}
				}
			}
		}
		if(resetTurnNo)
		{
			turnNo--;
		}
		if(checkMoveTo(kingPos, allOptions).charAt(0) == 't')
		{
			return true;
		}
		return false;
	}
	public static String[][] backupOccuMap;
	public static String[] backupPieceList = pieceList;
	public static boolean checkMate(int[] atArray, String king)
	{
		int badCount = 0;
		char ally = 0;
		if(turnNo % 2 != 0)
			ally = 'w';
		else
			ally = 'b';
		if(atArray[0] != 0)
		{
			if(checkCheck(posMap[atArray[0] - 1][atArray[1]], king) || occuMap[atArray[0] - 1][atArray[1]].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[0] != 7)
		{
			if(checkCheck(posMap[atArray[0] + 1][atArray[1]], king) || occuMap[atArray[0] + 1][atArray[1]].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[1] != 0)
		{
			if(checkCheck(posMap[atArray[0]][atArray[1] - 1], king) || occuMap[atArray[0]][atArray[1] - 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[1] != 7)
		{
			if(checkCheck(posMap[atArray[0]][atArray[1] + 1], king) || occuMap[atArray[0]][atArray[1] + 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[0] != 0 && atArray[1] != 0)
		{
			if(checkCheck(posMap[atArray[0] - 1][atArray[1] - 1], king) || occuMap[atArray[0] - 1][atArray[1] - 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[0] != 7 && atArray[1] != 7)
		{
			if(checkCheck(posMap[atArray[0] + 1][atArray[1] + 1], king) || occuMap[atArray[0] + 1][atArray[1] + 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[0] != 0 && atArray[1] != 7)
		{
			if(checkCheck(posMap[atArray[0] - 1][atArray[1] + 1], king) || occuMap[atArray[0] - 1][atArray[1] + 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(atArray[0] != 7 && atArray[1] != 0)
		{
			if(checkCheck(posMap[atArray[0] + 1][atArray[1] - 1], king) || occuMap[atArray[0] + 1][atArray[1] - 1].charAt(0) == ally)
			{
				badCount++;
			}
		}
		if(badCount == 8)
		{
			return true;
		}
		return false;
	}
	//code for moving each particular piece
	public static String[] pawn(int[] atArray, String moveTo, String pawn, boolean fromCheck)
	{
		
		String[] options = {"","","","","",""};
		if(turnNo % 2 != 0)
		{
			if(pawn.charAt(0) == 'w' || fromCheck)
			{
				try
				{
					backupOccuMap = occuMap;
					backupPieceList = pieceList;
					if(atArray[0] != 0)
					{
						options[0] = posMap[atArray[0] - 1][atArray[1]];
						if(atArray[1] != 7)
						{
							if(occuMap[atArray[0] - 1][atArray[1] + 1].charAt(0) == 'b')
							{
								options[1] = posMap[atArray[0] - 1][atArray[1] + 1];
							}
						}
						if(atArray[1] != 0)
						{
							if(occuMap[atArray[0] - 1][atArray[1] - 1].charAt(0) == 'b')
							{
								options[2] = posMap[atArray[0] - 1][atArray[1] - 1];
							}
						}
						else
						{
							options[2] = "";
						}
						if(originalPos(pawn) && occuMap[atArray[0] - 1][atArray[1]] == "   ")
						{
							options[3] = posMap[atArray[0] - 2][atArray[1]];
						}
						if(enPassant.charAt(0) == 't')
						{
							String[] splitEnPassant = enPassant.split(",");
							String enPawnssant = splitEnPassant[1];
							if(atArray[1] != 0)
							{
								if(occuMap[atArray[0] - 1][atArray[1] - 1] == "   ")
								{
									options[4] = posMap[atArray[0] - 1][atArray[1] - 1];
								}
							}
							if(atArray[1] != 7)
							{
								if(occuMap[atArray[0] - 1][atArray[1] + 1] == "   ")
								{
									options[5] = posMap[atArray[0] - 1][atArray[1] + 1];
								}
							}
						}
						if(fromCheck)
						{
							String[] opArray = {options[1], options[2]};
							return opArray;
						}
						if(checkMoveTo(moveTo, options).charAt(0) == 't')
						{
							int[] posNosMoveTo = posNos(moveTo);
							if(checkMoveTo(moveTo, options).charAt(2) == '1')
							{
								removePieceList(findPieceAt(moveTo));
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '2')
							{
								removePieceList(findPieceAt(moveTo));
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '3')
							{
								enPassant = "t," + pawn;
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '4' || checkMoveTo(moveTo, options).charAt(2) == '5')
							{
								removePieceList(findPieceAt(occuMap[posNosMoveTo[0] + 1][posNosMoveTo[1]]));
								occuMap[posNosMoveTo[0] + 1][posNosMoveTo[1]] = "   ";
							}
							if(posNos(moveTo)[0] != 0 && !(checkMoveTo(moveTo, options).charAt(2) == '4' || checkMoveTo(moveTo, options).charAt(2) == '5'))
							{
								occuMap[posNosMoveTo[0]][posNosMoveTo[1]] = pawn;
							}
							else
							{
								occuMap[posNosMoveTo[0]][posNosMoveTo[1]] = "wq" + wqn;
								pieceList[findInPieceList(pawn)] = "wq" + wqn++;
							}
							occuMap[atArray[0]][atArray[1]] = "   ";
							if(checkCheck(findPiece("w_k"), "w_k"))
							{
								System.out.println(1 / 0);
							}
							if(checkMate(posNos(findPiece("b_k")), "b_k"))
							{
								System.out.println("Checkmate!");
								turnNo = 1;
								whiteWins++;
								resetMap();
								return null;
							}
							turnNo++;
							drawMap();
							System.out.println("Next turn!");
						}
						else
						{
							System.out.println("You can't move there!");
						}
					}
					else
					{
						System.out.println("You can't move there!");
					}
				}
				catch(Exception e)
				{
					occuMap = backupOccuMap;
					pieceList = backupPieceList;
					System.out.println("That would check your king!");
					return null;
				}
			}
			else
			{
				System.out.println("Not your piece!");
			}
		}
		else
		{
			if(pawn.charAt(0) == 'b' || fromCheck)
			{
				try
				{
					backupOccuMap = occuMap;
					backupPieceList = pieceList;
					if(atArray[0] != 7)
					{
						options[0] = posMap[atArray[0] + 1][atArray[1]];
						if(atArray[1] != 7)
						{
							if(occuMap[atArray[0] + 1][atArray[1] + 1].charAt(0) == 'w')
							{
								options[1] = posMap[atArray[0] + 1][atArray[1] + 1];
							}
							else
							{
								options[2] = "";
							}
						}
						else
						{
							options[1] = "";
						}
						if(atArray[1] != 0)
						{
							if(occuMap[atArray[0] + 1][atArray[1] - 1].charAt(0) == 'w')
							{
								options[2] = posMap[atArray[0] + 1][atArray[1] - 1];
							}
							else
							{
								options[2] = "";
							}
						}
						else
						{
							options[2] = "";
						}
						if(originalPos(pawn) && occuMap[atArray[0] + 1][atArray[1]] == "   ")
						{
							if(atArray[0] != 6)
								options[3] = posMap[atArray[0] + 2][atArray[1]];
						}
						else
						{
							options[3] = "";
						}
						if(enPassant.charAt(0) == 't')
						{
							String[] splitEnPassant = enPassant.split(",");
							String enPawnssant = splitEnPassant[1];
							if(atArray[1] != 0)
							{
								if(occuMap[atArray[0]][atArray[1] - 1].equals(enPawnssant))
								{
									options[4] = posMap[atArray[0] + 1][atArray[1] - 1];
								}
							}
							if(atArray[1] != 7)
							{
								if(occuMap[atArray[0]][atArray[1] + 1].equals(enPawnssant))
								{
									options[5] = posMap[atArray[0] + 1][atArray[1] + 1];
								}
							}
						}
						if(fromCheck)
						{
							String[] opArray = {options[1], options[2]};
							return opArray;
						}
						if(checkMoveTo(moveTo, options).charAt(0) == 't')
						{
							int[] posNosMoveTo = posNos(moveTo);
							if(checkMoveTo(moveTo, options).charAt(2) == '1')
							{
								removePieceList(findPieceAt(moveTo));
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '2')
							{
								removePieceList(findPieceAt(moveTo));
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '3')
							{
								enPassant = "t," + pawn;
							}
							if(checkMoveTo(moveTo, options).charAt(2) == '4' || checkMoveTo(moveTo, options).charAt(2) == '5')
							{
								removePieceList(findPieceAt(occuMap[posNosMoveTo[0] - 1][posNosMoveTo[1]]));
								occuMap[posNosMoveTo[0] - 1][posNosMoveTo[1]] = "   ";
							}
							if(posNos(moveTo)[0] != 0 && !(checkMoveTo(moveTo, options).charAt(2) == '4' || checkMoveTo(moveTo, options).charAt(2) == '5'))
							{
								occuMap[posNosMoveTo[0]][posNosMoveTo[1]] = pawn;
							}
							else
							{
								occuMap[posNosMoveTo[0]][posNosMoveTo[1]] = "bq" + bqn;
								pieceList[findInPieceList(pawn)] = "bq" + bqn++;
							}
							occuMap[atArray[0]][atArray[1]] = "   ";
							if(checkCheck(findPiece("b_k"), "b_k"))
							{
								System.out.println(1 / 0);
							}
							if(checkMate(posNos(findPiece("w_k")), "w_k"))
							{
								System.out.println("Checkmate!");
								turnNo = 1;
								whiteWins++;
								resetMap();
								return null;
							}
							turnNo++;
							drawMap();
							System.out.println("Next turn!");
						}
						else
						{
							System.out.println("You can't move there!");
						}
					}
					else
					{
						System.out.println("You can't move there!");
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					occuMap = backupOccuMap;
					pieceList = backupPieceList;
					System.out.println("That would check your king!");
					return null;
				}
			}
			else
			{
				System.out.println("Not your piece!");
			}
		}
		return null;
	}
	public static String[] rook(int[] atArray, String moveTo, String rook, boolean fromCheck)
	{
		String[] options = {"","","","","","","","","","","","","",""};
		if((turnNo % 2 != 0 && rook.charAt(0) == 'w') || (turnNo % 2 == 0 && rook.charAt(0) == 'b') || fromCheck)
		{
			try
			{
				backupOccuMap = occuMap;
				backupPieceList = pieceList;
				char ally = 0, enemy = 0;
				if(turnNo % 2 != 0)
				{
					ally = 'w';
					enemy = 'b';
				}
				else
				{
					ally = 'b';
					enemy = 'w';
				}
				int count = 0;
				for(int forward = atArray[0] - 1; forward > -1; forward--)
				{
					options[count] = posMap[forward][atArray[1]];
					count++;
					if(occuMap[forward][atArray[1]] != "   ")
					{
						if(occuMap[forward][atArray[1]].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int backwards = atArray[0] + 1; backwards < 8; backwards++)
				{
					options[count] = posMap[backwards][atArray[1]];
					count++;
					if(!occuMap[backwards][atArray[1]].equals("   "))
					{
						if(occuMap[backwards][atArray[1]].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int left = atArray[1] - 1; left > -1; left--)
				{
					options[count] = posMap[atArray[0]][left];
					count++;
					if(!occuMap[atArray[0]][left].equals("   "))
					{
						if(occuMap[atArray[0]][left].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int right = atArray[1] + 1; right < 8; right++)
				{
					options[count] = posMap[atArray[0]][right];
					count++;
					if(!occuMap[atArray[0]][right].equals("   "))
					{
						if(occuMap[atArray[0]][right].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				if(fromCheck)
				{
					return options;
				}
				if(checkMoveTo(moveTo, options).charAt(0) == 't')
				{
					if(occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] != "   ")
					{
						removePieceList(findPieceAt(moveTo));
					}
					occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] = rook;
					occuMap[atArray[0]][atArray[1]] = "   ";
					if(turnNo % 2 != 0)
					{
						if(checkCheck(findPiece("w_k"), "w_k"))
						{
							System.out.println(1 / 0);
						}
					}
					else
					{
						if(checkCheck(findPiece("b_k"), "b_k"))
						{
							System.out.println(1 / 0);
						}
					}
					if(checkMate(posNos(findPiece(enemy + "_k")), enemy + "_k"))
					{
						System.out.println("Checkmate!");
						turnNo = 1;
						whiteWins++;
						resetMap();
						return null;
					}
					turnNo++;
					drawMap();
					System.out.println("Next turn!");
				}
				else
				{
					System.out.println("You can't move there!");
				}
			}
			catch(Exception e)
			{
				occuMap = backupOccuMap;
				pieceList = backupPieceList;
				System.out.println("That would check your king!");
				return null;
			}
		}
		else
		{
			System.out.println("Not your piece!");
		}
		return null;
	}
	public static String[] knight(int[] atArray, String moveTo, String knight, boolean fromCheck)
	{
		String[] options = {"","","","","","","",""};
		if((knight.charAt(0) == 'w' && turnNo % 2 != 0) || (knight.charAt(0) == 'b' && turnNo % 2 == 0) || fromCheck)
		{
			try
			{
				backupOccuMap = occuMap;
				backupPieceList = pieceList;
				char opponent = 0;
				if(turnNo % 2 != 0)
					opponent = 'b';
				else
					opponent = 'w';
				if(atArray[0] >= 2 && atArray[1] >= 1)
				{
					if(occuMap[atArray[0] - 2][atArray[1] - 1] == "   " || occuMap[atArray[0] - 2][atArray[1] - 1].charAt(0) == opponent)
					{
						options[0] = posMap[atArray[0] - 2][atArray[1] - 1];
					}
				}
				if(atArray[0] >= 1 && atArray[1] >= 2)
				{
					if(occuMap[atArray[0] - 1][atArray[1] - 2] == "   " || occuMap[atArray[0] - 1][atArray[1] - 2].charAt(0) == opponent)
					{
						options[1] = posMap[atArray[0] - 1][atArray[1] - 2];
					}
				}
				if(atArray[0] <= 5 && atArray[1] <= 6)
				{
					if(occuMap[atArray[0] + 2][atArray[1] + 1] == "   " || occuMap[atArray[0] + 2][atArray[1] + 1].charAt(0) == opponent)
					{
						options[2] = posMap[atArray[0] + 2][atArray[1] + 1];
					}
				}
				if(atArray[0] <= 6 && atArray[1] <= 5)
				{
					if(occuMap[atArray[0] + 1][atArray[1] + 2] == "   " || occuMap[atArray[0] + 1][atArray[1] + 2].charAt(0) == opponent)
					{
						options[3] = posMap[atArray[0] + 1][atArray[1] + 2];
					}
				}
				if(atArray[0] <= 5 && atArray[1] >= 1)
				{
					if(occuMap[atArray[0] + 2][atArray[1] - 1] == "   " || occuMap[atArray[0] + 2][atArray[1] - 1].charAt(0) == opponent)
					{
						options[4] = posMap[atArray[0] + 2][atArray[1] - 1];
					}
				}
				if(atArray[0] >= 1 && atArray[1] <= 5)
				{
					if(occuMap[atArray[0] - 1][atArray[1] + 2] == "   " || occuMap[atArray[0] - 1][atArray[1] + 2].charAt(0) == opponent)
					{
						options[5] = posMap[atArray[0] - 1][atArray[1] + 2];
					}
				}
				if(atArray[0] >= 2 && atArray[1] <= 6)
				{
					if(occuMap[atArray[0] - 2][atArray[1] + 1] == "   " || occuMap[atArray[0] - 2][atArray[1] + 1].charAt(0) == opponent)
					{
						options[6] = posMap[atArray[0] - 2][atArray[1] + 1];
					}
				}
				if(atArray[0] <= 6 && atArray[1] >= 2)
				{
					if(occuMap[atArray[0] + 1][atArray[1] - 2] == "   " || occuMap[atArray[0] + 1][atArray[1] - 2].charAt(0) == opponent)
					{
						options[7] = posMap[atArray[0] + 1][atArray[1] - 2];
					}
				}
				if(fromCheck)
				{
					return options;
				}
				if(checkMoveTo(moveTo, options).charAt(0) == 't')
				{
					if(occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] != "   ")
					{
						System.out.println(findPieceAt(moveTo));
						removePieceList(findPieceAt(moveTo));
					}
					occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] = knight;
					occuMap[atArray[0]][atArray[1]] = "   ";
					if(turnNo % 2 != 0)
					{
						{
							System.out.println(1 / 0);
						}
					}
					else
					{
						if(checkCheck(findPiece("b_k"), "b_k"))
						{
							//HERE
							System.out.println(1 / 0);
						}
					}
					if(checkMate(posNos(findPiece(opponent + "_k")), opponent + "_k"))
					{
						System.out.println("IS A");
						System.out.println("Checkmate!");
						turnNo = 1;
						whiteWins++;
						resetMap();
						return null;
					}
					drawMap();
					turnNo++;
					System.out.println("Next turn!");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				occuMap = backupOccuMap;
				pieceList = backupPieceList;
				System.out.println("That would check your king!");
				return null;
			}
		}
		else
		{
			System.out.println("Not your piece!");
		}
		return null;
	}
	public static String[] bishop(int[] atArray, String moveTo, String bishop, boolean fromCheck)
	{
		String[] options = {"","","","","","","","","","","","","",""};
		if((turnNo % 2 != 0 && bishop.charAt(0) == 'w') || (turnNo % 2 == 0 && bishop.charAt(0) == 'b') || fromCheck)
		{
			try
			{
				backupOccuMap = occuMap;
				backupPieceList = pieceList;
				char ally = 0, enemy = 0;
				if(turnNo % 2 != 0)
				{
					ally = 'w';
					enemy = 'b';
				}
				else
				{
					ally = 'b';
					enemy = 'w';
				}
				int count = 0, fRight = atArray[1] + 1, fLeft = atArray[1] - 1, bRight = atArray[1] + 1, bLeft = atArray[1] - 1;
				if(fRight <= 7)
				{
					for(int forwardRight = atArray[0] - 1; forwardRight > -1; forwardRight--)
					{options[count] = posMap[forwardRight][fRight];
						fRight++;
						count++;
						
						if(occuMap[forwardRight][fRight] != "   ")
						{
							if(occuMap[forwardRight][fRight].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(fRight == 7)
						{
							break;
						}
						
					}
				}
				if(fLeft >= 0)
				{
					for(int forwardLeft = atArray[0] - 1; forwardLeft > -1; forwardLeft--)
					{
						options[count] = posMap[forwardLeft][fLeft];
						count++;
						if(occuMap[forwardLeft][fLeft] != "   ")
						{
							if(occuMap[forwardLeft][fLeft].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(fLeft == 0)
						{
							break;
						}
						fLeft--;
					}
				}
				if(bRight <= 7)
				{
					for(int backwardRight = atArray[0] + 1; backwardRight < 8; backwardRight++)
					{
						options[count] = posMap[backwardRight][bRight];
						if(occuMap[backwardRight][bRight] != "   ")
						{
							if(occuMap[backwardRight][bRight].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(bRight == 7)
						{
							break;
						}
						bRight++;
						count++;
					}
				}
				if(bLeft >= 0)
				{
					for(int backwardLeft = atArray[0] + 1; backwardLeft < 8; backwardLeft++)
					{
						options[count] = posMap[backwardLeft][bLeft];
						if(occuMap[backwardLeft][bLeft] != "   ")
						{
							if(occuMap[backwardLeft][bLeft].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(bLeft == 0)
						{
							break;
						}
						bLeft--;
						count++;
					}
				}
				if(fromCheck)
				{
					return options;
				}
				for(String g: options)
					System.out.println(g);
				if(checkMoveTo(moveTo, options).charAt(0) == 't')
				{
					if(occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] != "   ")
					{
						removePieceList(findPieceAt(moveTo));
					}
					occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] = bishop;
					occuMap[atArray[0]][atArray[1]] = "   ";
					if(turnNo % 2 != 0)
					{
						if(checkCheck(findPiece("w_k"), "w_k"))
						{
							System.out.println(1 / 0);
						}
					}
					else
					{
						if(checkCheck(findPiece("b_k"), "b_k"))
						{
							System.out.println(1 / 0);
						}
					}
					if(checkMate(posNos(findPiece(enemy + "_k")), enemy + "_k"))
					{
						System.out.println("Checkmate!");
						turnNo = 1;
						whiteWins++;
						resetMap();
						return null;
					}
					drawMap();
					turnNo++;
					System.out.println("Next turn!");
				}
				else
				{
					System.out.println("You can't move there!");
				}
			}
			catch(Exception e)
			{
				occuMap = backupOccuMap;
				pieceList = backupPieceList;
				System.out.println("That would check your king!");
				return null;
			}
		}
		else
		{
			System.out.println("Not your piece!");
		}
		return null;
	}
	public static String[] queen(int[] atArray, String moveTo, String queen, boolean fromCheck)
	{
		String[] options = {"","","","","","","","","","","","","","",
							"","","","","","","","","","","","","",""};
		if((turnNo % 2 != 0 && queen.charAt(0) == 'w') || (turnNo % 2 == 0 && queen.charAt(0) == 'b') || fromCheck)
		{
			try
			{
				backupOccuMap = occuMap;
				backupPieceList = pieceList;
				char ally = 0, enemy = 0;
				if(turnNo % 2 != 0)
				{
					ally = 'w';
					enemy = 'b';
				}
				else
				{
					ally = 'b';
					enemy = 'w';
				}
				int count = 0, fRight = atArray[1] + 1, fLeft = atArray[1] - 1, bRight = atArray[1] + 1, bLeft = atArray[1] - 1;
				if(fRight <= 7)
				{
					for(int forwardRight = atArray[0] - 1; forwardRight > -1; forwardRight--)
					{options[count] = posMap[forwardRight][fRight];
						fRight++;
						count++;
						
						if(occuMap[forwardRight][fRight] != "   ")
						{
							if(occuMap[forwardRight][fRight].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(fRight == 7)
						{
							break;
						}
						
					}
				}
				if(fLeft >= 0)
				{
					for(int forwardLeft = atArray[0] - 1; forwardLeft > -1; forwardLeft--)
					{
						options[count] = posMap[forwardLeft][fLeft];
						count++;
						if(occuMap[forwardLeft][fLeft] != "   ")
						{
							if(occuMap[forwardLeft][fLeft].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(fLeft == 0)
						{
							break;
						}
						fLeft--;
					}
				}
				if(bRight <= 7)
				{
					for(int backwardRight = atArray[0] + 1; backwardRight < 8; backwardRight++)
					{
						options[count] = posMap[backwardRight][bRight];
						if(occuMap[backwardRight][bRight] != "   ")
						{
							if(occuMap[backwardRight][bRight].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(bRight == 7)
						{
							break;
						}
						bRight++;
						count++;
					}
				}
				if(bLeft >= 0)
				{
					for(int backwardLeft = atArray[0] + 1; backwardLeft < 8; backwardLeft++)
					{
						options[count] = posMap[backwardLeft][bLeft];
						if(occuMap[backwardLeft][bLeft] != "   ")
						{
							if(occuMap[backwardLeft][bLeft].charAt(0) == ally)
							{
								options[count] = "";
							}
							break;
						}
						if(bLeft == 0)
						{
							break;
						}
						bLeft--;
						count++;
					}
				}
				for(int forward = atArray[0] - 1; forward > -1; forward--)
				{
					options[count] = posMap[forward][atArray[1]];
					count++;
					if(occuMap[forward][atArray[1]] != "   ")
					{
						if(occuMap[forward][atArray[1]].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int backwards = atArray[0] + 1; backwards < 8; backwards++)
				{
					options[count] = posMap[backwards][atArray[1]];
					count++;
					if(!occuMap[backwards][atArray[1]].equals("   "))
					{
						if(occuMap[backwards][atArray[1]].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int left = atArray[1] - 1; left > -1; left--)
				{
					options[count] = posMap[atArray[0]][left];
					count++;
					if(!occuMap[atArray[0]][left].equals("   "))
					{
						if(occuMap[atArray[0]][left].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				for(int right = atArray[1] + 1; right < 8; right++)
				{
					options[count] = posMap[atArray[0]][right];
					count++;
					if(!occuMap[atArray[0]][right].equals("   "))
					{
						if(occuMap[atArray[0]][right].charAt(0) == ally)
						{
							count--;
							options[count] = "";
						}
						break;
					}
				}
				if(fromCheck)
				{
					return options;
				}
				if(checkMoveTo(moveTo, options).charAt(0) == 't')
				{
					if(occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] != "   ")
					{
						removePieceList(findPieceAt(moveTo));
					}
					occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] = queen;
					occuMap[atArray[0]][atArray[1]] = "   ";
					if(turnNo % 2 != 0)
					{
						if(checkCheck(findPiece("w_k"), "w_k"))
						{
							System.out.println(1 / 0);
						}
					}
					else
					{
						if(checkCheck(findPiece("b_k"), "b_k"))
						{
							System.out.println(1 / 0);
						}
					}
					if(checkMate(posNos(findPiece(enemy + "_k")), enemy + "_k"))
					{
						System.out.println("Checkmate!");
						turnNo = 1;
						whiteWins++;
						resetMap();
						return null;
					}
					drawMap();
					turnNo++;
					System.out.println("Next turn!");
				}
				else
				{
					System.out.println("You can't move there!");
				}
			}
			catch(Exception e)
			{
				occuMap = backupOccuMap;
				pieceList = backupPieceList;
				System.out.println("That would check your king!");
				return null;
			}
		}
		else
		{
			System.out.println("Not your piece!");
		}
		return null;
	}
	public static String[] king(int[] atArray, String moveTo, String king, boolean fromCheck)
	{
		String[] options = {"","","","","","","","","",""};
		char ally = 0, enemy = 0;
		if(turnNo % 2 != 0)
		{
			ally = 'w';
			enemy = 'b';
		}
		else
		{
			ally = 'b';
			enemy = 'w';
		}
		if(atArray[0] != 0)
		{
			if(!checkCheck(posMap[atArray[0] - 1][atArray[1]], king) && occuMap[atArray[0] - 1][atArray[1]].charAt(0) != ally)
			{
				options[0] = posMap[atArray[0] - 1][atArray[1]];
			}
		}
		if(atArray[0] != 7)
		{
			if(!checkCheck(posMap[atArray[0] + 1][atArray[1]], king) && occuMap[atArray[0] + 1][atArray[1]].charAt(0) != ally)
			{
				options[1] = posMap[atArray[0] + 1][atArray[1]];
			}
		}
		if(atArray[1] != 0)
		{
			if(!checkCheck(posMap[atArray[0]][atArray[1] - 1], king) && occuMap[atArray[0]][atArray[1] - 1].charAt(0) != ally)
			{
				options[2] = posMap[atArray[0]][atArray[1] - 1];
			}
		}
		if(atArray[1] != 7)
		{
			if(!checkCheck(posMap[atArray[0]][atArray[1] + 1], king) && occuMap[atArray[0]][atArray[1] + 1].charAt(0) != ally)
			{
				options[3] = posMap[atArray[0]][atArray[1] + 1];
			}
		}
		if(atArray[0] != 0 && atArray[1] != 0)
		{
			if(!checkCheck(posMap[atArray[0] - 1][atArray[1] - 1], king) && occuMap[atArray[0] - 1][atArray[1] - 1].charAt(0) != ally)
			{
				options[4] = posMap[atArray[0] - 1][atArray[1] - 1];
			}
		}
		if(atArray[0] != 7 && atArray[1] != 7)
		{
			if(!checkCheck(posMap[atArray[0] + 1][atArray[1] + 1], king) && occuMap[atArray[0] + 1][atArray[1] + 1].charAt(0) != ally)
			{
				options[5] = posMap[atArray[0] + 1][atArray[1] + 1];
			}
		}
		if(atArray[0] != 0 && atArray[1] != 7)
		{
			if(!checkCheck(posMap[atArray[0] - 1][atArray[1] + 1], king) && occuMap[atArray[0] - 1][atArray[1] + 1].charAt(0) != ally)
			{
				options[6] = posMap[atArray[0] - 1][atArray[1] + 1];
			}
		}
		if(atArray[0] != 7 && atArray[1] != 0)
		{
			if(!checkCheck(posMap[atArray[0] + 1][atArray[1] - 1], king) && occuMap[atArray[0] + 1][atArray[1] - 1].charAt(0) != ally)
			{
				options[7] = posMap[atArray[0] + 1][atArray[1] - 1];
			}
		}
		if(originalPos(ally + "r1") && originalPos(king))
		{
			if(occuMap[atArray[0]][atArray[1] - 1] == "   " && occuMap[atArray[0]][atArray[1] - 2] == "   ")
			{
				if(!checkCheck(posMap[atArray[0]][atArray[1]], king) && !checkCheck(posMap[atArray[0]][atArray[1] - 1], king) && !checkCheck(posMap[atArray[0]][atArray[1] - 2], king))
				{
					options[8] = posMap[atArray[0]][atArray[1] - 2];
				}
			}
		}
		if(originalPos(ally + "r2") && originalPos(king))
		{
			if(occuMap[atArray[0]][atArray[1] + 1] == "   " && occuMap[atArray[0]][atArray[1] + 2] == "   ")
			{
				if(!checkCheck(posMap[atArray[0]][atArray[1]], king) && !checkCheck(posMap[atArray[0]][atArray[1] + 1], king) && !checkCheck(posMap[atArray[0]][atArray[1] + 2], king))
				{
					options[8] = posMap[atArray[0]][atArray[1] + 2];
				}
			}
		}
		if(fromCheck)
		{
			return options;
		}
		if(checkMoveTo(moveTo, options).charAt(0) == 't')
		{
			if(checkMoveTo(moveTo, options).charAt(2) == 8)
			{
				occuMap[0][0] = "   ";
				occuMap[atArray[0]][atArray[1] - 1] = ally + "r1";
				if((checkMate(posNos(findPiece(enemy + "_k")), enemy + "_k")))
				{
					System.out.println("Checkmate!");
					turnNo = 1;
					whiteWins++;
					resetMap();
					return null;
				}
			}
			else if(checkMoveTo(moveTo, options).charAt(2) == 9)
			{
				occuMap[0][7] = "   ";
				occuMap[atArray[0]][atArray[1] + 1] = ally + "r2";
				if((checkMate(posNos(findPiece(enemy + "_k")), enemy + "_k")))
				{
					System.out.println("Checkmate!");
					turnNo = 1;
					whiteWins++;
					resetMap();
					return null;
				}
			}
			else if(occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] != "   ")
			{
				removePieceList(findPieceAt(moveTo));
			}
			occuMap[posNos(moveTo)[0]][posNos(moveTo)[1]] = king;
			occuMap[atArray[0]][atArray[1]] = "   ";
			drawMap();
			turnNo++;
			System.out.println("Next turn!");
		}
		return null;
	}
	//main
	public static void main(String[] args) 
	{
		Scanner scanIn = new Scanner(System.in);
		String input, piece, moveTo;
		String[] split;
		drawMap();
		System.out.println("\nEnter the piece you would like to move and the place you would like it move seperated by a comma.\n   Ex: \"wp1,a-3\"\n   You can also enter 'r' to reset the map (your opponent will get a point though!)");
		boolean valid = false;
		while(!valid)
		{
			try
			{
				input = scanIn.next();
				if(!input.equals("r"))
				{
					if(validate(input))
					{	
						valid = true;
						split = input.split(",");
						piece = split[0];
						moveTo = split[1];
						goToPiece(piece, moveTo, false);
					}
					else
					{
						System.out.println("Try again!");
						scanIn = new Scanner(System.in);
					}
				}
				else
				{
					resetMap();
				}
				while(true)
				{
					scanIn = new Scanner(System.in);
					valid = false;
					while(!valid)
					{
						try
						{
							if(turnNo % 2 != 0)
							{
								if(checkCheck(findPiece("w_k"), "w_k"))
								{
									System.out.println("Your king is checked!");
								}
							}
							else
							{
								if(checkCheck(findPiece("b_k"), "b_k"))
								{
									System.out.println("Your king is checked!");
								}
							}
							input = scanIn.next();
							if(!input.equals("r"))
							{
								if(validate(input))
								{	
									valid = true;
									split = input.split(",");
									piece = split[0];
									moveTo = split[1];
									goToPiece(piece, moveTo, false);
									if(deleteEnPassant)
									{
										deleteEnPassant = false;
										enPassant = "f";
									}
									if(enPassant.charAt(0) == 't')
									{
										deleteEnPassant = true;
									}
								}
								else
								{
									System.out.println("Try again!");
									scanIn = new Scanner(System.in);
								}
							}
							else
							{
								resetMap();
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
							try 
							{
								TimeUnit.SECONDS.sleep(1);
							} 
							catch (InterruptedException e1) 
							{
								e1.printStackTrace();
							}
							scanIn = new Scanner(System.in);
						}
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				try 
				{
					TimeUnit.SECONDS.sleep(1);
				} 
				catch (InterruptedException e1) 
				{
					e1.printStackTrace();
				}
				scanIn = new Scanner(System.in);
			}
		}
	}
}