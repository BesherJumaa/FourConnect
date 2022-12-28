package Connectsametype;


import java.util.LinkedList;
import java.util.List;


/**
 *
 * @author manal
 */
public class Board {

    private final int[][] grid;
    private final int[] topPieceIndex;

    private final int width;
    private final int height;
    private final int numOfPiecesToWin;

    private int fills;
    private int lastColumnPlayed = -1;
    

    public Board(int width, int height, int numOfPiecesToWin) {
        fills = 0;
        this.width = width;
        this.height = height;
        this.numOfPiecesToWin = numOfPiecesToWin;
        grid = new int[height][width];
        topPieceIndex = new int[width];
        for (int i = 0; i < topPieceIndex.length; i++) {
            topPieceIndex[i] = height;
        }
        for (int[] grid1 : grid) {
            for (int j = 0; j < grid1.length; j++) {
                grid1[j] = -1;
            }
        }
    }

    public Board(Board board) {
        grid = new int[board.height][board.width];
        topPieceIndex = new int[board.width];
        System.arraycopy(board.topPieceIndex, 0, topPieceIndex, 0, this.topPieceIndex.length);
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(board.grid[i], 0, this.grid[i], 0, board.width);
        }
        this.fills = board.fills;
        this.height = board.height;
        this.width = board.width;
        this.numOfPiecesToWin = board.numOfPiecesToWin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Board> getPussibleNextBoards(int nextPlayer) {
        List<Board> nextBoards = new LinkedList<>();
        for (int i = 0; i < width; i++) {
            if (topPieceIndex[i] != 0) {
                Board nextBoard = new Board(this);
                nextBoard.Play(nextPlayer , i);
                nextBoards.add(nextBoard);
            }
        }
        return nextBoards;
    }

    public boolean Play(int player, int col) {
        if (topPieceIndex[col] != 0) {
            topPieceIndex[col] -= 1;
            grid[topPieceIndex[col]][col] = player;
            fills++;
            lastColumnPlayed = col;
            return true;
        }
        return false;
    }

    /**
     * how good is the board for this player?
     *
     * @param player
     * @return1
     */
    public int evaluate(char player) {
        if(isWithdraw())return 0;
        else if(isWin(player))return Integer.MAX_VALUE;
        else return Integer.MIN_VALUE;

    }

    /**
     * checks if the game is withdraw
     *
     * @return
     */
    public boolean isWithdraw() {
        return (fills == width * height);
    }

    /**
     * checks if player putting last piece makes him win (connect four pieces)
     *
     * @param player
     * @return true if win1
     */
    public boolean isWinWithLastPiece(char player) {
        int col = this.lastColumnPlayed;
        return (isWinInColumn(player, col) || isWinInRow(player, col)
                || isWinInDiagonal_1(player, col) || isWinInDiagonal_2(player, col));
    }

    /**
     * checks if player is a winner (searching all the board not just last
     * piece)
     *
     * @param player
     * @return true if win1
     */
    public boolean isWin(char player) {
        for (int col = 0; col < width; col++) {
            if (isWinInColumn(player, col)) {
                return true;
            } else if (isWinInRow(player, col)) {
                return true;
            } else if (isWinInDiagonal_1(player, col)) {
                return true;
            } else if (isWinInDiagonal_2(player, col)) {
                return true;
            }
        }
        return false;
    }
    
    

    /**
     * checks if the game is win of withdraw for any player
     *
     * @return
     */
    public boolean isFinished() {
        return (isWin('1') || isWin('0') || isWithdraw());
    }
    
    private boolean same_type(char player,int num){
        return num != -1 && num % 2 == Character.getNumericValue(player);
    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in this col
     *
     * @param player
     * @param col the column the player played in
     * @return true if win1
     */
    
  
    private boolean isWinInColumn(char player, int col) {
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // check cells below
        try {
            for (int i = row + 1; i < height; i++) {
                if (same_type(player,grid[i][col])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);
    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the row containing piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win1
     */
    private boolean isWinInRow(char player, int col) {
        //collect row
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = col - 1; i >= 0; i--) {
                if (same_type(player,grid[row][i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = col + 1; i < width; i++) {
                if (grid[row][i] %2==Character.getNumericValue(player)) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the first diagonal containing this piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win1
     */
    private boolean isWinInDiagonal_1(char player, int col) {
        //collect diagonal
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = 1;; i++) {
                if ( same_type(player,grid[row - i][col - i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row + i][col + i])) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    /**
     * checks if player putting piece in col makes him win (connect four pieces)
     * in the second diagonal containing this piece
     *
     * @param player
     * @param col the column the player played in
     * @return true if win1
     */
    private boolean isWinInDiagonal_2(char player, int col) {
        //collect diagonal
        int row = topPieceIndex[col];
        //row is empty
        if (row == height) {
            return false;
        }
        // the cell itself
        if (!same_type(player,grid[row][col])) {
            return false;
        }
        int count = 1;
        // cells befor
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row - i][col + i] )) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        // cells after
        try {
            for (int i = 1;; i++) {
                if (same_type(player,grid[row + i][col - i] )) {
                    count++;
                } else {
                    break;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return (count >= numOfPiecesToWin);

    }

    private char otherPlayer(char player) {
        if (player == '1') {
            return '0';
        }
        return '1';
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            sb.append(" | ");
            for (int j = 0; j < width; j++) {
                if (grid[i][j]!=-1){
                    sb.append((char)(grid[i][j] + '0'));
                    sb.append(" | ");
                }
                else {
                    sb.append(" ");
                    sb.append(" | ");
                            }
            }

            sb.append('\n');
        }
        sb.append(" ");
        for (int i = 1; i < height; i++) {
            sb.append("-----");
        }
        sb.append("\n | ");
        for (int i = 1; i <= height; i++) {
            sb.append(i);
            sb.append(" | ");
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        Board board = new Board(4, 4, 3);

        board.Play(2, 1);
        board.Play(5, 1);
        board.Play(4, 2);
        board.Play(7, 2);
        board.Play(3, 3);
        board.Play(1, 3);

        System.out.println("board:");
        System.out.println(board);
        System.out.println("****************");
        System.out.println("is win for odd? " + board.isWin('1'));
        System.out.println("****************");

        List<Board> next = board.getPussibleNextBoards(3);
        int i = 1;
        for (Board b : next) {
            System.out.println(i + ": (" + b.evaluate('1') + ")");
            System.out.println(b);
            System.out.println("is win for odd? " + b.isWinWithLastPiece('1'));
            i++;
        }

    }
}
