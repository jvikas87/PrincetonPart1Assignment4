import java.util.Comparator;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

	private Board board;

	private Stack<Board> stack;

	public Solver(Board initial) {

		this.board = initial;
		MinPQ<WrapperBoard> minPQ = new MinPQ<WrapperBoard>(new Comparator<WrapperBoard>() {
			@Override
			public int compare(WrapperBoard arg0, WrapperBoard arg1) {
				return Integer.compare(arg0.board.manhattan() + arg0.moves, arg1.board.manhattan() + arg1.moves);
			}
		});

		MinPQ<WrapperBoard> twinPQ = new MinPQ<WrapperBoard>(new Comparator<WrapperBoard>() {
			@Override
			public int compare(WrapperBoard arg0, WrapperBoard arg1) {
				return Integer.compare(arg0.board.manhattan() + arg0.moves, arg1.board.manhattan() + arg1.moves);
			}
		});
		minPQ.insert(new WrapperBoard(board, 0, null));
		twinPQ.insert(new WrapperBoard(board.twin(), 0, null));
		WrapperBoard destination = null;
		while (!minPQ.isEmpty() || !twinPQ.isEmpty()) {
			WrapperBoard poppedMinPQ = minPQ.delMin();
			if (poppedMinPQ.board.isGoal()) {
				destination = poppedMinPQ;
				break;
			}
			Iterator<Board> iterator = poppedMinPQ.board.neighbors().iterator();
			while (iterator.hasNext()) {
				Board neighbour = iterator.next();
				if (poppedMinPQ.parent == null || !poppedMinPQ.parent.board.equals(neighbour)) {
					minPQ.insert(new WrapperBoard(neighbour, poppedMinPQ.moves + 1, poppedMinPQ));
				}
			}

			WrapperBoard poppedTwinPQ = twinPQ.delMin();
			if (poppedTwinPQ.board.isGoal()) {
				break;
			}
			iterator = poppedTwinPQ.board.neighbors().iterator();
			while (iterator.hasNext()) {
				Board neighbour = iterator.next();
				if (poppedTwinPQ.parent == null || !poppedTwinPQ.parent.board.equals(neighbour)) {
					twinPQ.insert(new WrapperBoard(neighbour, poppedTwinPQ.moves + 1, poppedTwinPQ));
				}
			}
		}
		if (destination != null) {
			stack = new Stack<>();
			while (destination != null) {
				stack.push(destination.board);
				destination = destination.parent;
			}
		}
	} // find a solution to the initial board (using the A* algorithm)

	public boolean isSolvable() {
		return stack != null;
	} // is the initial board solvable?

	public int moves() {
		return stack != null ? stack.size() - 1 : -1;
	} // min number of moves to solve initial board; -1 if unsolvable

	public Iterable<Board> solution() {
		return stack;
	} // sequence of boards in a shortest solution; null if unsolvable

	private class WrapperBoard {
		private Board board;
		private int moves;
		private WrapperBoard parent;

		public WrapperBoard(Board board, int moves, WrapperBoard parent) {
			super();
			this.board = board;
			this.moves = moves;
			this.parent = parent;
		}
	}

	public static void main(String[] args) {

		// create initial board from file

		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		// Board initial = new Board(new int[][] { { 1, 2, 3 }, { 8, 6, 7 }, { 4, 0, 5 }
		// });
		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}