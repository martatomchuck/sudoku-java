/*
 * klasa SudokuGame zawiera planszê gry Sudoku i logikê gry
 */
public class SudokuGame {

	static int[][] userGrid = new int[9][9];
	static int[][] grid = new int[9][9];
//	static String[][] pgrid = new String[9][9];
			
	
	public static void main(String[] args) {
		
		System.out.println(validity(0,0,grid));
		
		start(grid);
		start(loop(0,0,grid));
	}
	
	public static void start(int[][] grid) {

		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				System.out.print(grid[i][j]);
			}
			System.out.println();
		}
	}
	
	public static boolean validity(int x, int y, int[][] grid) {
		String temp="";
		
		for (int i=0; i<9; i++) {
			temp+=Integer.toString(grid[i][y]); //horizontal
			temp+=Integer.toString(grid[x][i]); //vertical
			temp+=Integer.toString(grid[(x/3)*3+i/3][(y/3)*3+i%3]); //square
		}
		
		int count=0, idx=0;
		
		while((idx=temp.indexOf(Integer.toString(grid[x][y]), idx)) !=-1) {
			idx++; count++;
		}
		
		return count==3;
	}
	
	public static int[][] loop(int y, int x, int[][] grid) {
		return grid;
	}

}
