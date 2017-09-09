package assingment;


import java.io.*;
import java.util.*;

public class TicTacToe implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte player_num;
	private int size;
	private int win_seq;
	private static final String SYMBOL_PLAYER = " XOABCDEFGHIJKLMNPQRSTUVWYZ";
	private int[][] status;
	private int round_count = 0;

	private int player_current;
	private static int win_count;
	private static int row_origin, column_origin;
//	private static final String fileName_defult = "TicTacToe.txt";

	private static BufferedReader bReader;
	private byte check_win = TicTacToe.NO_WIN;

	private static final byte NO_WIN = -1;
	private static final byte Fair = -2;
	private static final byte WRONG = -10;
	private static final byte QUIT = 0;

	private static final byte DIRECT_UP = 0;
	private static final byte DIRECT_UP_RIGHT = 1;
	private static final byte DIRECT_RIGHT = 2;
	private static final byte DIRECT_DOWN_RIGHT = 3;
	private static final byte DIRECT_DOWN = 4;
	private static final byte DIRECT_DOWN_LEFT = 5;
	private static final byte DIRECT_LEFT = 6;
	private static final byte DIRECT_UP_LEFT = 7;

	public static void main(String[] args) {
		TicTacToe current_game = TicTacToe.init();
		current_game.show();
		while (current_game.check_win == TicTacToe.NO_WIN) {
			int winner = current_game.input();
			switch (winner) {
			case TicTacToe.QUIT:// quit
				System.exit(0);
			case TicTacToe.WRONG:// wrong
				continue;
			case TicTacToe.Fair:// normal continue
				current_game.check_win = TicTacToe.Fair;
				break;
			default:// winner
				current_game.check_win = (byte) winner;
				break;
			}
			current_game.show();

			if (current_game.check_win == TicTacToe.Fair) {
				System.out.println("Game Over! The result is Fair!!!");
			} else if (current_game.check_win >= 0) {
				System.out.println(
						"Game Over\nThe winner is Player" + current_game.player_current + "! Congratulation!!!");
			} else {
				current_game.player_current = (current_game.player_current + 1) % (current_game.player_num + 1);
				if (current_game.player_current == 0) {
					current_game.player_current++;
				}
			}
		}
	}

	/** init board and system */
	public static TicTacToe init() {
		boolean loop_signal = true;
		bReader = new BufferedReader(new InputStreamReader(System.in));
		do {
			loop_signal = true;
			try {
				System.out.print("Resume a saved game(y/n)?");
				String resume_sign = bReader.readLine().trim();
				if (resume_sign.equals("y")) {
					return load();
				} else if (resume_sign.equals("n")) {
					byte player_numNum = -1;
					int boardSize = -1, winSequence = -1;

					do {
						try {
							loop_signal = true;
							System.out.print("How many players are playing(Max26)?\t");
							player_numNum = Byte.parseByte(bReader.readLine().trim());
							loop_signal = false;
						} catch (Exception e) {
							System.out.println("Input ERROR, Please Retry!");
							loop_signal = true;
						}
					} while (loop_signal);
					do {
						try {
							loop_signal = true;
							System.out.print("How large the board should be(Max999)?\t");
							boardSize = Integer.parseInt(bReader.readLine().trim());
							loop_signal = false;
						} catch (Exception e) {
							System.out.println("Input ERROR, Please Retry!");
							loop_signal = true;
						}
					} while (loop_signal);
					do {
						try {
							loop_signal = true;
							System.out.print("What the win sequence count should be?\t");
							winSequence = Integer.parseInt(bReader.readLine().trim());

							if (winSequence > boardSize || player_numNum * winSequence >= boardSize * boardSize) {
								System.out.println("There is no win chance, please reset!");
								continue;
							}

							loop_signal = false;
						} catch (Exception e) {
							System.out.println("Input ERROR, Please Retry!");
							loop_signal = true;
						}
					} while (loop_signal);

					TicTacToe newGame = new TicTacToe(player_numNum, boardSize, winSequence);
					return newGame;
				}
			} catch (Exception e) {// catch resume
				System.out.println("Input ERROR, Please Retry!");
				loop_signal = true;
			}
		} while (loop_signal);
		return null;

	}

	public TicTacToe(byte player_numNum, int boardSize, int winSequence) {
		this.player_num = player_numNum;
		this.size = boardSize;
		this.win_seq = winSequence;
		status = new int[boardSize][boardSize];
		this.player_current = 1;
		this.round_count=boardSize*boardSize;
	}

//	public TicTacToe(byte player_numNum, int boardSize, int winSequence, int current_player, int[][] status) {
//		this(player_numNum, boardSize, winSequence);
//		this.player_current = current_player;
//		this.status = status;
//	}

	public void show() {// Show Whole Board
		System.out.print(" ");///3 space
		for (int i = 1; i <= size; i++) {// column number
			System.out.print("   " + i);// 3space
		}
		System.out.println();
		for (int i = 97; i < size; i++) {// each MarkRow+Grid
			for (int j =0; j < size; j++) {// Row of Boxes
				if (j == 0) {// special first box for each row
					System.out.print((i+1));
					int digit=(i+1),t_space=3;//999+space,99+2space,9+3space
					while (t_space!=0) {
						if((digit=digit/10)==0){
							System.out.print(" ");
						}
						t_space--;
					}
					System.out.print(SYMBOL_PLAYER.charAt(status[i][j]));
				} else {
					System.out.print(" | " + SYMBOL_PLAYER.charAt(status[i][j]));
				}
			}
			System.out.println();
			if (i < size - 1) {// Grid
				for (int j = 0; j < size; j++) {
					if (j == 0) {// special first box for each row
						System.out.print("   ---");
					} else {
						System.out.print("+---");
					}
				}
				System.out.println();
			}
		}
	}

	/** @return 0--exit;-10--retry;-1--NO_WIN;1-26--winner£»-2--Fair */
	public int input() {// input column
		System.out.print("Current Player:" + player_current
				+ "\nPlease Enter Row and Column(Seperate by Space) or Press Q to Save&Exit: ");
		try {
			String inputArray = bReader.readLine().trim();
			if (inputArray.equalsIgnoreCase("Q")) {
				save(this);
				return TicTacToe.QUIT;
			}
			StringTokenizer stringTokenizer = new StringTokenizer(inputArray);
			if (stringTokenizer.countTokens() == 2) {
				int row = Integer.parseInt(stringTokenizer.nextToken()) - 1;
				int column = Integer.parseInt(stringTokenizer.nextToken()) - 1;
				if (row > size || column > size || row < 0 || column < 0) {
					System.out.println("Entered row or column number out of board size! Please Retry!");
					return TicTacToe.WRONG;
				}
				if (status[row][column] == 0) {
					row_origin = row;
					column_origin = column;
					round_count--;
					status[row_origin][column_origin] = player_current;
				} else {
					System.out.println("The board has been accounted! Please Retry!");
					return TicTacToe.WRONG;
				}

				switch (check()) {
				case NO_WIN:
					return NO_WIN;
				case Fair:
					return Fair;
				case WRONG:
					return TicTacToe.WRONG;
				default:
					
					return player_current;
				}
			}
		} catch (IOException e) {
			System.out.println("Input Exception, Please Retry!");
			round_count++;
			return TicTacToe.WRONG;
		}
		System.out.println("Input Wrong, Please Retry!");
		round_count++;
		return TicTacToe.WRONG;
	}

	/**
	 * @param row
	 *            previous checked point
	 * @return NO_WIN,FAIR,winner number
	 */
	public int check(int direction, int row, int column) {// 0-7=8DIRECTIONS,-1=ALL
		int row_current = -1, column_current = -1;
		switch (direction) {
		case -1: {
			win_count = 1;
			for (int d = DIRECT_UP; d <= DIRECT_DOWN_RIGHT; d++) {
				int winner = check(d, row, column);
				if (winner > 0) {
					return winner;
				}
			}
			if (round_count == 0) {
				return Fair;
			} else
				return NO_WIN;
		}
		case DIRECT_UP: {
			row_current = row - 1;
			column_current = column;
			break;
		}
		case DIRECT_UP_RIGHT: {
			row_current = row - 1;
			column_current = column + 1;
			break;
		}
		case DIRECT_RIGHT: {
			row_current = row;
			column_current = column + 1;
			break;
		}
		case DIRECT_DOWN_RIGHT: {
			row_current = row + 1;
			column_current = column + 1;
			break;
		}
		case DIRECT_DOWN: {
			row_current = row + 1;
			column_current = column;
			break;
		}
		case DIRECT_DOWN_LEFT: {
			row_current = row + 1;
			column_current = column - 1;
			break;
		}
		case DIRECT_LEFT: {
			row_current = row;
			column_current = column - 1;
			break;
		}
		case DIRECT_UP_LEFT: {
			row_current = row - 1;
			column_current = column - 1;
			break;
		}
		default:
			return WRONG;
		}
		if (row_current >= 0 && row_current < size && column_current >= 0 && column_current < size
				&& status[row_current][column_current] == player_current) {
			win_count++;
			if (win_count >= win_seq) {
				return player_current;
			} else {
				return check(direction, row_current, column_current);
			}
		} else {
			if (direction + 4 >= 8) {
				win_count=1;
				return NO_WIN;
			} else {
				return check(direction + 4, row_origin, column_origin);
			}
		}
		// return WRONG;
	}

	public int check() {
		return check(-1, row_origin, column_origin);
	}

	public static void save(TicTacToe current_game) {
		String fileName = "";
		boolean inputFail = true;
		do {
			try {
				System.out.print("The file name for save:");
				fileName = bReader.readLine();
				FileOutputStream fStream = new FileOutputStream(fileName);
				ObjectOutputStream oStream = new ObjectOutputStream(fStream);
				oStream.writeObject(current_game);
				oStream.flush();
				oStream.close();
				fStream.close();
				inputFail=false;
				System.out.println("Saved successfully! Thank you for game.");
			} catch (FileNotFoundException e) {
				System.out.println("File cannot create or access! Please Retry!");
				inputFail=true;
			}catch (IOException e) {
				if(fileName==""){
					System.out.println("Input file name failed! Please Retry!");
					inputFail=true;
				}else{
					System.out.println("Save game failed! Program or system error! Game will exit without save!");
					System.exit(-1);
				}
			}
		} while (inputFail);
	}
	
	public static TicTacToe load() {
		String fileName = "";
		boolean inputFail = true;
		do {
			try {
				System.out.print("The file name for load:");
				fileName = bReader.readLine();
				FileInputStream fStream = new FileInputStream(fileName);
				ObjectInputStream iStream = new ObjectInputStream(fStream);
				TicTacToe current_game=(TicTacToe) iStream.readObject();
				iStream.close();
				fStream.close();
				return current_game;
			} catch (FileNotFoundException e) {
				System.out.println("File cannot create or access! Please Retry!");
				inputFail=true;
			}catch (IOException e) {
				if(fileName==""){
					System.out.println("Input file name failed! Please Retry!");
					inputFail=true;
				}else{
					System.out.println("Load game failed! Program or system error! Game will exit without load!");
					System.exit(-1);
				}
			}catch (Exception e) {
				System.out.println("Load game failed! Program or system error2! Game will exit while trying load!");
				System.exit(-1);
			}
		} while (inputFail);
		return null;
	}

}
