
import java.util.*;
import java.lang.*;

class State {
	int[] board;
	State parentPt;
	int depth;

	public State(int[] arr) {
		this.board = Arrays.copyOf(arr, arr.length);
		this.parentPt = null;
		this.depth = 0;
	}
	public State[] getSuccessors() {
		//get all four successors and return them in sorted order
		int emptySqrIndex = -1;
		for (int i = 0; i < this.board.length; i++) {
			if (this.board[i] == 0) emptySqrIndex = i;
		}// find the index of empty square
		int neighborIndex[] = new int[4]; //top, bot, left, right neighbor index
		switch(emptySqrIndex) {//calculating neighbor index
		case 0 :
			neighborIndex[0] = emptySqrIndex + 6;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex + 2;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 1 :
			neighborIndex[0] = emptySqrIndex + 6;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 2 :
			neighborIndex[0] = emptySqrIndex + 6;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex - 2;
			break;
		case 3 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex + 2;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 4 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 5 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex + 3;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex - 2;
			break;
		case 6 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex - 6;
			neighborIndex[2] = emptySqrIndex + 2;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 7 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex - 6;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex + 1;
			break;
		case 8 :
			neighborIndex[0] = emptySqrIndex - 3;
			neighborIndex[1] = emptySqrIndex - 6;
			neighborIndex[2] = emptySqrIndex - 1;
			neighborIndex[3] = emptySqrIndex - 2;
			break;	
		}

		State results[] = new State[4];// The results: an array of states
		for (int i = 0; i < neighborIndex.length; i++) {//exchange value at each position
			int[] successor = Arrays.copyOf(this.board, board.length);
			int temp = successor[emptySqrIndex];
			successor[emptySqrIndex] = successor[neighborIndex[i]];
			successor[neighborIndex[i]] = temp; //finishing swapping
			results[i] = new State(successor); // a new state has been generated
		}
		//now we sort the results, first compute numerical value of each successor
		int valueArray[] = new int[4];//store unsorted value into this array
		for (int i = 0; i < results.length; i++) {
			int value = 0;
			int power = 0;
			for (int j = 8; j >= 0; j--) { // results[i].board[j].length = 8
				value += (results[i].board[j])*(Math.pow(10, power));
				power++;
			}
			valueArray[i] = value;
		}
		//insertion sort, sorting valueArray & results at the same time
		int i, key, j;
		State k;
		for (i = 1; i < results.length; i++) {
			key = valueArray[i];
			k = results[i];
			j = i - 1;
			while (j >= 0 && valueArray[j] > key)
		       {
		           valueArray[j+1] = valueArray[j];
		           results[j+1] = results[j]; //sort results at the same time
		           j = j-1;
		       }
		       valueArray[j+1] = key;
		       results[j+1] = k; //sort results at the same time
		}
		//done
		return results;
	}

	public void printState(int option) {
		switch(option) {
		case 1: 
		case 2:
		case 4:
		case 5:
			System.out.println(this.getBoard());
			break;
		case 3:
			System.out.println(this.getBoard() + " parent " + this.parentPt.getBoard());
			break;
		
		}
		//print a torus State based on option (flag)
		
	}

	public String getBoard() { //return string info of current state's board
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(this.board[i]).append(" ");
		}
		return builder.toString().trim();
	}

	public boolean isGoalState() {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != (i + 1) % 9)
				return false;
		}
		return true;
	}
	@Override
	public boolean equals(Object src) {
		for (int i = 0; i < 9; i++) {
			if (this.board[i] != ((State)src).board[i])
				return false;
		}
		return true;
	}
	//check if a state is in a list
	public boolean isIn(List<State> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).equals(this)) return true;
		}
		return false;
	}
}

	

public class Torus {
	public static void main(String args[]) {
		if (args.length < 10) {
			System.out.println("Invalid Input");
			return;
		}
		int flag = Integer.valueOf(args[0]);
		int[] board = new int[9];
		for (int i = 0; i < 9; i++) {
			board[i] = Integer.valueOf(args[i + 1]);
		}
		int option = flag / 100;
		int cutoff = flag % 100; //cutoff for the depth limited dfs
		if (option == 1) {
			State init = new State(board);
			State[] successors = init.getSuccessors();
			for (State s : successors) {
				s.printState(option);
			}
		} else {
			State init = new State(board);
			int goalChecked = 0;
			boolean goalIsFound = false;
			while (true) {
				Stack<State> stack = new Stack<>();
				List<State> prefix = new ArrayList<>();
				stack.push(init);
				prefix.add(init);
				int maxStackSize = Integer.MIN_VALUE;
				int nullstate[] = new int[9];
				for (int i : nullstate) {
					i = 0;
				}
				init.parentPt = new State(nullstate);
				
				while (!stack.isEmpty()) {
					State buffer = stack.pop();
					if (option == 1 || option == 2 || option == 3) buffer.printState(option);
					if (buffer.depth != 0) {
						prefix.subList(prefix.indexOf(buffer.parentPt) + 1, prefix.size()).clear();
						prefix.add(buffer);
					}//remove all element after parent except the first element
					
					goalChecked++;
					if (option == 4 && buffer.depth == cutoff && goalChecked == buffer.depth + 1) {
						for (State s : prefix) {//reach leaves and the first node at the level
							s.printState(option);
						}
					}
					if (buffer.isGoalState()) {	
						goalIsFound = true;
						break;//success
					} else {
						if (buffer.depth == cutoff) continue; //reach leaves
						State[] successors = buffer.getSuccessors();
						for (State t : successors) {
							t.depth = buffer.depth + 1;
							t.parentPt = buffer;
							if (t.isIn(prefix) == true) {
								continue;
							}
							stack.push(t);
						}		
					}
					if (stack.size() > maxStackSize) maxStackSize = stack.size();//update maxStackSize
				}
				if (option != 5) {
					break;
				} else {
					cutoff++;//perform iterative deepening; 
					if (goalIsFound == true) {
						for (State s : prefix) {
							s.printState(option);//print path
						}
						System.out.println("Goal-check " + goalChecked);
						System.out.println("Max-stack-size " + maxStackSize);//print #s goalchecked & maxStackSize
						break;
					}
				}
			}
		}
	}
}
