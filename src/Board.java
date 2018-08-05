import java.util.ArrayList;
import java.util.List;

public class Board {

	private int dimension;

	private int[][] blocks;

	private int manhattan = -1;

	public Board(int[][] blocks) {
		this.blocks = clone(blocks);
		this.dimension = blocks.length;
		// construct a board from an n-by-n array of blocks
	}

	// (where blocks[i][j] = block in row i, column j)
	public int dimension() {
		return dimension;
	} // board dimension n

	public int hamming() {
		int hamming = 0;
		for (int outer = 1; outer <= dimension; outer++) {
			for (int inner = 1; inner <= dimension; inner++) {
				int ele = (outer - 1) * dimension + inner;
				if (blocks[outer - 1][inner - 1] != 0 && blocks[outer - 1][inner - 1] != ele) {
					hamming++;
				}
			}
		}
		return hamming;
	} // number of blocks out of place

	public int manhattan() {
		if (manhattan != -1) {
			return manhattan;
		}
		int manhattan = 0;
		for (int outer = 1; outer <= dimension; outer++) {
			for (int inner = 1; inner <= dimension; inner++) {
				int ele = blocks[outer - 1][inner - 1];
				int newElement = ele - 1;
				int row, col;
				if (ele != 0) {
					row = (newElement / dimension) + 1;
					col = (newElement % dimension) + 1;
					manhattan += (Math.abs(outer - row) + Math.abs(inner - col));
				}
			}
		}
		this.manhattan = manhattan;
		return manhattan;
	} // sum of Manhattan distances between blocks and goal

	public boolean isGoal() {
		for (int outer = 1; outer <= dimension; outer++) {
			for (int inner = 1; inner <= dimension; inner++) {
				if (outer != dimension || inner != dimension) {
					int ele = (outer - 1) * dimension + inner;
					if (blocks[outer - 1][inner - 1] != ele) {
						return false;
					}
				}
			}
		}
		return true;
	}// is this board the goal board?

	public Board twin() {
		Board twin = null;
		outer: for (int outer = 0; outer < dimension; outer++) {
			for (int inner = 0; inner < dimension - 1; inner++) {
				int ele = blocks[outer][inner];
				int nextEle = blocks[outer][inner + 1];
				if (ele != 0 && nextEle != 0) {
					int[][] newBlock = getCopy();
					int temp = ele;
					newBlock[outer][inner] = newBlock[outer][inner + 1];
					newBlock[outer][inner + 1] = temp;
					twin = new Board(newBlock);
					break outer;
				}
			}
		}
		return twin;
	} // a board that is obtained by exchanging any pair of blocks

	private int[][] getCopy() {
		int[][] copy = new int[dimension][dimension];
		for (int outer = 0; outer < dimension; outer++) {
			for (int inner = 0; inner < dimension; inner++) {
				copy[outer][inner] = blocks[outer][inner];
			}
		}
		return copy;
	}

	private int[][] clone(int[][] array) {
		int dimension = array.length;
		int[][] copy = new int[dimension][dimension];
		for (int outer = 0; outer < dimension; outer++) {
			for (int inner = 0; inner < dimension; inner++) {
				copy[outer][inner] = array[outer][inner];
			}
		}
		return copy;
	}

	public boolean equals(Object y) {
		if (y == null || !(y instanceof Board)) {
			return false;
		}
		Board board = (Board) y;
		if (board.dimension != this.dimension) {
			return false;
		}
		for (int outer = 0; outer < dimension; outer++) {
			for (int inner = 0; inner < dimension; inner++) {
				if (board.blocks[outer][inner] != this.blocks[outer][inner]) {
					return false;
				}
			}
		}
		return true;
		// return board.toString().equals(this.toString());
	} // does this board equal y?

	public Iterable<Board> neighbors() {
		int[] rowIndex = new int[] { -1, 1, 0, 0 };
		int[] colIndex = new int[] { 0, 0, 1, -1 };
		List<Board> boards = new ArrayList<>();
		int outer = 0;
		int inner = 0;
		outer: for (outer = 0; outer < dimension; outer++) {
			for (inner = 0; inner < dimension; inner++) {
				if (blocks[outer][inner] == 0) {
					break outer;
				}
			}
		}
		for (int index = 0; index < rowIndex.length; index++) {
			int targetRowIndex = outer + rowIndex[index];
			int targetColIndex = inner + colIndex[index];
			if (targetRowIndex >= 0 && targetRowIndex < dimension && targetColIndex >= 0
					&& targetColIndex < dimension) {
				int[][] copy = getCopy();
				copy[outer][inner] = blocks[targetRowIndex][targetColIndex];
				copy[targetRowIndex][targetColIndex] = 0;
				boards.add(new Board(copy));
			}
		}
		return boards;
	} // all neighboring boards

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(dimension);
		for (int outer = 0; outer < dimension; outer++) {
			builder.append("\n");
			StringBuilder innerBuilder = new StringBuilder();
			for (int inner = 0; inner < dimension; inner++) {
				innerBuilder.append(blocks[outer][inner] + " ");
			}
			builder.append(innerBuilder.toString().trim());
		}
		return builder.toString().trim();
	} // string representation of this board (in the output format specified below)

	public static void main(String[] args) {
		Board board = new Board(new int[][] { { 1, 2 }, { 0, 3 } });
		System.out.println(board.isGoal());
	}// unit tests (not graded)
}