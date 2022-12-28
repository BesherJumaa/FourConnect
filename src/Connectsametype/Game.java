package Connectsametype;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 *
 * @author manal
 */
public class Game {

    char computer = '1';
    char human = '0';
    Board board = new Board(3, 3, 3);
    int even;


    public void Play() {
        System.out.println(board);
        while (true) {
            humanPlay();
            System.out.println(board);

            if (board.isWin(human)) {
                System.out.println("You Win I lose  :'(");
                break;
            }
            if (board.isWithdraw()) {
                System.out.println("I guess we are even -_-");
                break;
            }
            computerPlay();
            System.out.println("_____Computer Turn______");
            System.out.println(board);
            if (board.isWin(computer)) {
                System.out.println("I win :D I'm way smarter than you, you stupid human!");
                break;
            }
            if (board.isWithdraw()) {
                System.out.println("I guess we are even -_-");
                break;
            }
        }

    }

    //1         ************** YOUR CODE HERE 1************___          \\
    private void computerPlay() {
        board = maxMove(board).getValue();
    }

    /**
     * Human plays
     *
     * @return1 the column the human played in
     */
    private void humanPlay() {
        Scanner s = new Scanner(System.in);
        int col;
        while (true) {
            System.out.print("Enter even number: ");
            even = s.nextInt();
            while(even%2!=0){
                System.out.println("The number is  " + even+ " is odd !, try again");
                System.out.print("Enter even number: ");
                even = s.nextInt();
                System.out.println();}

            System.out.print("Enter column: ");
            col = s.nextInt();
            System.out.println();
            if ((col > 0) && (col - 1 < board.getWidth())) {
                if (board.Play(even, col - 1)) {
                    return;
                }
                System.out.println("Invalid Column: Column " + col + " is full!, try again");
            }
            System.out.println("Invalid Column: out of range " + board.getWidth() + ", try agine");
        }
    }










    private Pair<Integer, Board> maxMove(Board b) {
        //List of nextStates
        List<Board> nextStates = b.getPussibleNextBoards(1);
        //break condition.. when its final..
        if(b.isFinished()){
            return new Pair <> (b.evaluate(computer),b);
        }

         int index = 0;
        // max = min value
        Pair<Integer,Board> max= new Pair<>(Integer.MIN_VALUE,null);
      // foreach state check if the minMove bigger than our max state, if so then max = the new bigger state..
        for ( int i=0;i<nextStates.size() ;i++) {

            if (minMove(nextStates.get(i)).getKey() > max.getKey()) {
                max = minMove(nextStates.get(i));
                index = i;
            }
        }
        // return the max state
        return new Pair<>(max.getKey(),nextStates.get(index));
    }
    // Same as maxMin but reversed.. (and getPossibleNextBoards for the human not the computer)
    private Pair<Integer, Board> minMove(Board b) {
        List<Board> nextStates = b.getPussibleNextBoards(even);
        if(b.isFinished()){
            return new Pair <> (b.evaluate(computer),b);
        }
        Pair<Integer,Board> min= new Pair<>(Integer.MAX_VALUE,null);
        int index = 0;
        for ( int i=0;i<nextStates.size() ;i++) {
            if (maxMove(nextStates.get(i)).getKey() < min.getKey()) {
                min = maxMove(nextStates.get(i));
                index = i;
            }
        }
        return new Pair<>(min.getKey(),nextStates.get(index));
    }





    public static void main(String[] args) {
        Game g = new Game();
        g.Play();
    }

}
