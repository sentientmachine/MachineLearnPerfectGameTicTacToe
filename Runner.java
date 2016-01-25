import java.util.ArrayList;
import java.util.Random;

public class Runner
{
	private static ArrayList<String> player1StateArray; 
	private static ArrayList<String> player1CupContents;
	private static ArrayList<Integer> player1CupsDrawnFromArray;
	private static ArrayList<Integer> player1ColorDrawnFromCupArray;
	
	private static ArrayList<String> player2StateArray; 
	private static ArrayList<String> player2CupContents;
	private static ArrayList<Integer> player2CupsDrawnFromArray;
	private static ArrayList<Integer> player2ColorDrawnFromCupArray;
	
	private static int maxBeadsPerCup; 
	
	public static void main(String[] args)
	{
		//Code written by Eric Leschinski, idea nabbed from O'REILLY statistics hacks
		//Hack #52.  
		
		//Tic Tac Toe index positions
		
		// 0 | 1 | 2     
		//-----------
		// 3 | 4 | 5 
		//-----------
		// 6 | 7 | 8
		
		//A state of the tic-tac-toe board is called a state
		//for example a state looks like this:
		
		// X | O |      
		//-----------
		// O | X |  
		//-----------
		//   |   |  O
		
		//                                                012345678
		//The above state is represented with a string:  "XO OX   O"

		//The color of the beads in the cup are represented with integer  0 through 8
		
		//We create states it on the fly so we don't make more than we have to.
		

		
		
		
		
		//CONCLUSIONS I LEARNED FROM THIS APPLICATION:
		
		//1.  Whoever gets to start at Tic-tac-toe has a massive advantage to win.
		//2.  If you get to start, the best place to start is a corner.
		
		
		//3.  This application is not perfect, consider the following state,
		//    X to move:
		
		//      | O |  
		//   ---|---|---
		//    X | O | O
		//   ---|---|---
		//    O | X | X
		
		//The choices are a Loss or a Draw.  Position 2 is Draw, Position 0 is loss.
		//This algorithm punishes Player1 for draw and loss, so the choices are 
		//reduced to the minimum and the cupContents become very small.
		//The problem is that this algorithm has no concept of "given a choice between
		//loss and draw, choose draw".  It hates them both the same so it ends up
		//choosing randomly.
		
		
		//This is a list of all the states.  player1StateArray and player1CupContents are the same length.
		player1StateArray = new ArrayList<String>();
		//This is a list of the cup contents by state.
		player1CupContents = new ArrayList<String>();
	
		
		//This is a list of all the states.  player2StateArray and player2CupContents are the same length.
		player2StateArray = new ArrayList<String>();
		//This is a list of the cup contents by state.
		player2CupContents = new ArrayList<String>();
		
		
		
		player1CupsDrawnFromArray = new ArrayList<Integer>();
		player1ColorDrawnFromCupArray = new ArrayList<Integer>();
		
		player2CupsDrawnFromArray = new ArrayList<Integer>();
		player2ColorDrawnFromCupArray = new ArrayList<Integer>();
		
		maxBeadsPerCup = 100;    //The number of beads in a cup won't exceed this number
		                         //If we put more than 100 in a cup, the new integer pushed in
		                         //deletes another memory spot.
		
		String currentState;
		
		//Have two learning algorithms play against eachother a bunch of times.
		
		char whoWon = ' ';
		
		
		boolean player1Learns = true;  //Set one of these guys to false to see how much of an advantage learning gives you.
		boolean player2Learns = false;
		
		int player1Wins = 0;
		int player2Wins = 0;
		
		int player1Started = 0;
		int player2Started = 0;
		

		int counter = 0;
		
		//Play tic-tac-toe 4000 times:
		for(int i = 0; i < 4000; i++)
		{
			currentState = getBlankState();
			System.out.println("============= NEW GAME =================");
			player1CupsDrawnFromArray.clear();
			player1ColorDrawnFromCupArray.clear();
			player2CupsDrawnFromArray.clear();
			player2ColorDrawnFromCupArray.clear();
			
			//purpose of this is to give X and O equal chances at beginning
			//new games.  Whoever starts at tic-tac-toe has an advantage at 
			//winning the game because you can pick a corner which will force
			//the opponent into defending two areas at once.
			if (counter % 2 == 0)
			{
				while (true)
				{
					player2Started++;  //counting how often player2 got to start.
					currentState = addMarkToState(currentState, player2GetNextMove(currentState), 'O');
					displayStateNicely(currentState);
					whoWon = testStateSeeWhoWins(currentState);
					if (whoWon != ' ') break;
	
					currentState = addMarkToState(currentState, player1GetNextMove(currentState), 'X');
					displayStateNicely(currentState);
					whoWon = testStateSeeWhoWins(currentState);
					if (whoWon != ' ') break;
	
				}
			}
			else
			{
				while (true)
				{	
					player1Started++;  //counting how often player1 got to start.
					currentState = addMarkToState(currentState, player1GetNextMove(currentState), 'X');
					displayStateNicely(currentState);
					whoWon = testStateSeeWhoWins(currentState);
					if (whoWon != ' ') break;
					
					currentState = addMarkToState(currentState, player2GetNextMove(currentState), 'O');
					displayStateNicely(currentState);
					whoWon = testStateSeeWhoWins(currentState);
					if (whoWon != ' ') break;

				}
			}
			counter++;
			
			if (whoWon == 'O')
			{
				//Reward O by adding beads to it's winning strategy group, if there are more 
				//than maxBeadsPerCup in the cup, rob from another memory location.
				
				//Punish X by removing beads to it's losing strategy group if there are more 
				//than maxBeadsPerCup in the cup, rob from another memory location.
				
				System.out.println("player2 won");
				
				if (player2Learns)
				{
					for(int x = 0; x < player2CupsDrawnFromArray.size(); x++)
					{
						player2_add_beads_to_cup(player2CupsDrawnFromArray.get(x), player2ColorDrawnFromCupArray.get(x), 8);
					}
				}
				if (player1Learns)
				{
					for(int x = 0; x < player1CupsDrawnFromArray.size(); x++)
					{
						player1RemoveBead(player1CupsDrawnFromArray.get(x), player1ColorDrawnFromCupArray.get(x), 8);
					}
				}
				player2Wins++;
			}
			else if (whoWon == 'X')
			{
				//Reward X by adding beads to it's winning strategy group.if there are more 
				//than maxBeadsPerCup in the cup, rob from another memory location.
				
				//Punish O by removing beads to it's losing strategy group.if there are more 
				//than maxBeadsPerCup in the cup, rob from another memory location.
				
				System.out.println("player1 X won");
				
				if (player2Learns)
				{
					for(int x = 0; x < player2CupsDrawnFromArray.size(); x++)
					{
						player2RemoveBead(player2CupsDrawnFromArray.get(x), player2ColorDrawnFromCupArray.get(x), 8);
					}
				}
				if (player1Learns)
				{
					for(int x = 0; x < player1CupsDrawnFromArray.size(); x++)
					{
						player1_add_beads_to_cup(player1CupsDrawnFromArray.get(x), player1ColorDrawnFromCupArray.get(x), 8);
					}
				}
				player1Wins++;
			}
			else if (whoWon == 'D')
			{
				//Was a draw, punish both.
				
				if (player2Learns)
					for(int x = 0; x < player2CupsDrawnFromArray.size(); x++)
					{
						//Cycling through each Cup We drew from.
						player2RemoveBead(player2CupsDrawnFromArray.get(x), player2ColorDrawnFromCupArray.get(x), 9);
					}
				
				if (player1Learns)
					for(int x = 0; x < player1CupsDrawnFromArray.size(); x++)
					{
						//Cycling through each Cup We drew from.
						player1RemoveBead(player1CupsDrawnFromArray.get(x), player1ColorDrawnFromCupArray.get(x), 9);
					}
			}
			else
			{
				throw new RuntimeException("Bad response from testStateSeeWhoWins ");
			}
			
			
		}
		System.out.println("");
		System.out.println("");
		System.out.println("Player1 'X':");
		for(int x = 0; x < player1StateArray.size(); x++)
		{
			displayStateNicely(player1StateArray.get(x));
			System.out.println("X to move,  cupContents:  '" + player1CupContents.get(x) + "'");
		}
		/*
		System.out.println("");
		System.out.println("Player2 'O':");
		for(int x = 0; x < player2StateArray.size(); x++)
		{
			displayStateNicely(player2StateArray.get(x));
			System.out.println("O to move, cupContents:  '" + player2CupContents.get(x) + "'");
		}
		*/
		System.out.println("");
		System.out.println("Player1Wins 'X':  " + player1Wins);
		System.out.println("Player2Wins 'O':  " + player2Wins);
		
		System.out.println("Player1Started: " + player1Started);
		System.out.println("Player2Started: " + player2Started);
		
		
	}
	public static void player1RemoveBead(int index, int beadColor, int cnt)
	{
		//Remove bead from cup if it's there, if not, don't do anything.
		for(int x = 0; x < cnt; x++)
		{
			if (player1CupContents.get(index).length() < 2)
				return;
			
			if (player1CupContents.get(index).lastIndexOf(beadColor + "") != player1CupContents.get(index).indexOf(beadColor + ""))
				player1CupContents.set(index, player1CupContents.get(index).replaceFirst(beadColor + "", ""));
			else
				break;
		}
	}
	public static void player2RemoveBead(int index, int beadColor, int cnt)
	{
		//Remove bead from cup if it's there, if not, don't do anything.
		for(int x = 0; x < cnt; x++)
		{
			if (player2CupContents.get(index).length() < 2)
				return;
			if (player2CupContents.get(index).lastIndexOf(beadColor + "") != player2CupContents.get(index).indexOf(beadColor + ""))
				player2CupContents.set(index, player2CupContents.get(index).replaceFirst(beadColor + "", ""));
			else
				break;
		}
	}
	public static void player2_add_beads_to_cup(int index, int beadColor, int cnt)
	{
		//if there is < maxBeadsPerCup in the state, just add it
		//if more than maxBeadsPerCup, then gotta delete to make room.
		
		
		if (player2CupContents.get(index).length() < maxBeadsPerCup)
		{
			for(int x = 0; x < cnt; x++)
				player2CupContents.set(index, player2CupContents.get(index) + beadColor);
		}
		else
		{
			//need to erase some memory to make room.  Get the index of the 
			//first spot that is not beadColor, strengthening the good knowledge
			
			for(int x = 0; x < player2CupContents.get(index).length(); x++)
			{
				if (Integer.parseInt(player2CupContents.get(index).charAt(x) + "") != Integer.toString(beadColor).charAt(0))
				{
					char[] charArray = player2CupContents.get(index).toCharArray();
					charArray[x] = Integer.toString(beadColor).charAt(0);
					player2CupContents.set(index, new String(charArray));
					return;
				}
			}
			//if we get to this point, it means we have 100 positions full of the beadColor 
			//we want in there.  lol it seems pretty sure of itself.  Let it be.
			
		}
		
	}

	public static void player1_add_beads_to_cup(int index, int beadColor, int cnt)
	{
		//if there is < maxBeadsPerCup in the state, just add it
		//if more than maxBeadsPerCup, then gotta delete to make room.
		
		
		if (beadColor < 0 || beadColor > 8)
			throw new RuntimeException("player1_add_beads_to_cup invalid beadColor: " + beadColor + beadColor + beadColor + beadColor);
		
		if (player1CupContents.get(index).length() < maxBeadsPerCup)
		{
			for(int x = 0; x < cnt; x++)
				player1CupContents.set(index, player1CupContents.get(index) + beadColor);
		}
		else
		{
			//If we are over the top, then we need to erase some memory to make room.
			
			//Get the index of the first spot that is not beadColor
			
			for(int x = 0; x < player1CupContents.get(index).length(); x++)
			{
				if (Integer.parseInt(player1CupContents.get(index).charAt(x) + "") != Integer.toString(beadColor).charAt(0))
				{
					char[] charArray = player1CupContents.get(index).toCharArray();
					charArray[x] = Integer.toString(beadColor).charAt(0); 
					player1CupContents.set(index, new String(charArray));
					return;
				}
			}
			//if we get to this point, it means we have 100 positions full of the beadColor 
			//we want in there.  lol it seems pretty sure of itself.  Let it be.
			
		}
		
	}
	public static char testStateSeeWhoWins(String state)
	{
		//returns ' ' if nobody has won yet
		//returns 'X' if X has won.
		//returns 'O' if O has won.
		
		ArrayList<String> winningPositions = new ArrayList<String>();
		
		//If the following spots have all the same 'X' or 'O' then that wins.
		winningPositions.add("012");
		winningPositions.add("345");
		winningPositions.add("678");
		winningPositions.add("036");
		winningPositions.add("147");
		winningPositions.add("258");
		winningPositions.add("048");
		winningPositions.add("246");
		
		for(int x = 0; x < winningPositions.size(); x++)
		{
			if (state.charAt(Integer.parseInt(winningPositions.get(x).charAt(0) + "")) != ' ' && state.charAt(Integer.parseInt(winningPositions.get(x).charAt(0) + "")) == state.charAt(Integer.parseInt(winningPositions.get(x).charAt(1) + "")) &&
				state.charAt(Integer.parseInt(winningPositions.get(x).charAt(1) + "")) == state.charAt(Integer.parseInt(winningPositions.get(x).charAt(2) + "")))
			{
				return state.charAt(Integer.parseInt(winningPositions.get(x).charAt(0) + ""));
			}
		}
		
		
		//Test to see if it's a draw.
		if (state.contains(" ") == false)
			return 'D';
		
		return ' ';
		
	}
	public static String addMarkToState(String state, int position, char mark)
	{
		//This function puts mark into state at position
		if (position > 8 || position < 0)
			throw new RuntimeException("addMarkToState position out of range: " + position);
		
		if (mark != ' ' && mark != 'X' && mark != 'O')
			throw new RuntimeException("addMarkToState mark not correct");
		
		char[] charArray = state.toCharArray();
		
		charArray[position] = mark;
		
		return new String(charArray);
	}
	public static void displayStateNicely(String state)
	{
		System.out.println("");
		System.out.println(" " + state.charAt(0) + " | " + state.charAt(1) + " | " + state.charAt(2));
		System.out.println("---|---|---");
		System.out.println(" " + state.charAt(3) + " | " + state.charAt(4) + " | " + state.charAt(5));
		System.out.println("---|---|---");
		System.out.println(" " + state.charAt(6) + " | " + state.charAt(7) + " | " + state.charAt(8));
	}
	public static int player2GetNextMove(String currentState)
	{
		//player 2 is always 'O' (the letter).  Given the passed in state,
		//it returns the index of where the 'O' should go to win.
		
		//If the state is unknown, we gotta add a default cup contents
		int stateIndex = player2_get_index_of_state(currentState);
		int randomMove = -1;
		
		if (testStateSeeWhoWins(currentState) != ' ')
			throw new RuntimeException("Your asking a player to move but someone has already won or there is a draw");
		
		if (stateIndex == -1)
		{
			player2StateArray.add(currentState);
			player2CupContents.add(getInitialCupContentsByState(currentState));
			stateIndex = player2CupContents.size()-1;
		}
		
		randomMove = player2_get_random_move_from_cup_contents(stateIndex);
		
		//We have to remember what color bead we picked from which cup so we can
		//reward or punish this player2 after we find out if we win or lose.
		player2CupsDrawnFromArray.add(stateIndex);
		player2ColorDrawnFromCupArray.add(randomMove);
		
		return randomMove;
	}
	
	public static int player1GetNextMove(String currentState)
	{
		//player 1 is always 'X'.  Given the passed in state,
		//it returns the index of where the 'X' should go to win.
		
		if (testStateSeeWhoWins(currentState) != ' ')
			throw new RuntimeException("Your asking a player to move but someone has already won or there is a draw");
		
		//If the state is unknown, we gotta add a default cup contents
		int stateIndex = player1_get_index_of_state(currentState);
		int randomMove = -1;
		if (stateIndex == -1)
		{
			player1StateArray.add(currentState);
			player1CupContents.add(getInitialCupContentsByState(currentState));
			stateIndex = player1CupContents.size()-1;
		}
		
		randomMove = player1_get_random_move_from_cup_contents(stateIndex);
		
		//We have to remember what color bead we picked from which cup so we can
		//reward or punish this player2 after we find out if we win or lose.
		
		player1CupsDrawnFromArray.add(stateIndex);
		player1ColorDrawnFromCupArray.add(randomMove);
		
		return randomMove;		
	}

	public static int player2_get_random_move_from_cup_contents(int stateIndex)
	{
		//returns the index of where we put our mark;
		//we know that we are X. 
		
		//pick a random position of this string.
		
		Random r = new Random();
		
		int randPosition = r.nextInt(player2CupContents.get(stateIndex).length());
		//System.out.println("RandPosition: '" + randPosition + "'");
		
		return Integer.parseInt(player2CupContents.get(stateIndex).charAt(randPosition) + "");
		
	}
	public static int player1_get_random_move_from_cup_contents(int stateIndex)
	{
		//returns the index of where we put our mark;
		//we know that we are X. 
		
		//pick a random position of this string.
		
		Random r = new Random();
		
		int randPosition = r.nextInt(player1CupContents.get(stateIndex).length());
		//System.out.println("RandPosition: '" + randPosition + "'");
		
		return Integer.parseInt(player1CupContents.get(stateIndex).charAt(randPosition) + "");
		
	}
	public static int player2_get_index_of_state(String state)
	{
		//checks  player1StateArray for our state.
	
		//if the state does not exist return -1;
		
		for(int x = 0; x < player2StateArray.size(); x++)
		{
			if (state.equals(player2StateArray.get(x)))
			{
				return x;
			}
		}
		return -1;
		
	}
	public static int player1_get_index_of_state(String state)
	{
		//checks  player1StateArray for our state.
		//if the state does not exist return -1;
		
		for(int x = 0; x < player1StateArray.size(); x++)
		{
			if (state.equals(player1StateArray.get(x)))
			{
				return x;
			}
		}
		return -1;
		
	}
	public static String getInitialCupContentsByState(String state)
	{
		//This is Initialization, simulates filling the cups with the
		//beads before we start teaching the algorithm.
		
		
		//The contents of the cup will be a string.
		//The maximum length of the string will be 100 characters
		//representing 100 beads.
		
		//0 = upper left
		//1 = upper middle
		//2 = upper right
		//3 = middle left
		//etc
		
		
		//"000011112222"   means that there is a 33% chance we choose upper left spot for our mark.
		
		//The default cup contents are 8 beads of each valid move.  A total of 9 states * 8 beads = 72

		if (testStateSeeWhoWins(state) == 'D')
			throw new RuntimeException("Error in getInitialCupContentByState, you shouldn't be looking at a state with a full board");
		
		String cupContents = "";
		char blank = ' ';
		
		int beadStartCnt = 8;
		
		for(int index = 0; index < state.length(); index++)
		{
			if (state.charAt(index) == blank)
			{
				for(int x = 0; x < beadStartCnt; x++)
					cupContents += "" + index;
			}
		}
		return cupContents;
		
	}
	
	public static String getBlankState()
	{
		//      012345678
		return "         ";
	}
	
}
