import Machine.Letter;
import Machine.Machine;
import Machine.Reflector;
import Machine.SpinningRotor;
import Machine.PlugBoard;
import Machine.ABC;

public class Main {

/*    public static void printRotors(SpinningRotor one, SpinningRotor two) {

        int ind = one.getPos();

        Letter[] r_one = one.getRightArr();
        Letter[] l_one = one.getLeftArr();
        Letter[] r_two = two.getRightArr();
        Letter[] l_two = two.getLeftArr();

        for(int i = 0; i < 6; i++) {

            System.out.println( l_two[ind % 6].theLetter() + " " + r_two[ind % 6].theLetter()+"  |  "+l_one[ind % 6].theLetter() + " " + r_one[ind % 6].theLetter()+"  :"+i);
            ind++;
        }

        System.out.println(" ");

    }
*/
    public static void main(String[] args) {

        Letter[] rightOne = {   new Letter("D", 5),
                                new Letter("E", 4),
                                new Letter("F", 3),
                                new Letter("A", 2),
                                new Letter("B", 1),
                                new Letter("C", 0)  };

        Letter[] leftOne = {    new Letter("C", 5),
                                new Letter("B", 4),
                                new Letter("A", 3),
                                new Letter("F", 2),
                                new Letter("E", 1),
                                new Letter("D", 0)  };


        SpinningRotor one = new SpinningRotor(rightOne, leftOne, "C", 1);
        one.setRotor("D", "C");

        Letter[] rightTwo = {   new Letter("D", 5),
                                new Letter("E", 3),
                                new Letter("F", 0),
                                new Letter("A", 2),
                                new Letter("B", 4),
                                new Letter("C", 1)  };

        Letter[] leftTwo = {    new Letter("F", 2),
                                new Letter("C", 5),
                                new Letter("A", 3),
                                new Letter("E", 1),
                                new Letter("B", 4),
                                new Letter("D", 0)  };

        SpinningRotor two = new SpinningRotor(rightTwo, leftTwo, "E", 2);
        two.setRotor("D", "C");
/*
        Letter[] rightThree = {   new Letter("A", 1),
                                  new Letter("B", 0),
                                  new Letter("C", 3),
                                  new Letter("D", 2),
                                  new Letter("E", 5),
                                  new Letter("F", 4)  };

        Letter[] leftThree = {    new Letter("B", 1),
                                  new Letter("A", 0),
                                  new Letter("D", 3),
                                  new Letter("C", 2),
                                  new Letter("F", 5),
                                  new Letter("E", 4)  };

        SpinningRotor three = new SpinningRotor(rightThree, leftThree, "E");
        three.setRotor("D", "C");*/

        // set machine parts
        Reflector ref =     new Reflector(new int[]{3, 4, 5, 0, 1, 2});
        ABC abc =           new ABC(new char[]{'A', 'B', 'C', 'D', 'E', 'F'});
        PlugBoard p =       new PlugBoard(new int[]{0,1,2,3,4,5});
        p.PlugIn(0, 5);

        // set machine
        Machine mac = new Machine();
        SpinningRotor[] arr = {one, two};
        mac.setActiveRotors(arr, 2);
        mac.setReflector(ref);
        mac.setPlugBoard(p);

        /////////////////////////////////////////////

        //    char[] string = {'A','A','B','B','C','C','D','D','E','E','F','F'};
        //    char[] string = {'C','E','E','F','B','D','F','C','D','A','A','B'};

        char[] string = {'F','E','D','C','B','A','D','D','E','F'};

        for(char c: string){

            int res = mac.act(abc.toIndex(c));
            System.out.println(abc.toLetter(res));
        }
    }
}