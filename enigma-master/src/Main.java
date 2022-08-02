import Machine.*;

import java.util.ArrayList;

public class Main {

    public static void printRotors(SpinningRotor one, SpinningRotor two) {

        int ind_one = one.getPos();  int ind_two = two.getPos();
        Letter[] r_one=one.getRightArr();
        Letter[] l_one = one.getLeftArr();
        Letter[] r_two=two.getRightArr();
        Letter[] l_two = two.getLeftArr();

        for(int i = 0; i < 6; i++) {

            System.out.println( l_two[ind_one % 6].theLetter() + " " + r_two[ind_one % 6].theLetter()+"      "+l_one[ind_one % 6].theLetter() + " " + r_one[ind_one % 6].theLetter()+" :"+i);
            ind_one++; ind_two++;
        }

        System.out.println(" ");

    }

    public static void main(String[] args) {
        Letter[] rightTwo  =    {
                new Letter("D", 5),
                new Letter("E", 3),
                new Letter("F", 0),
                new Letter("A", 2),
                new Letter("B", 4),
                new Letter("C", 1)  };
        Letter[] leftTwo={    new Letter("F", 2),
                new Letter("C", 5),
                new Letter("A", 3),
                new Letter("E", 1),
                new Letter("B", 4),
                new Letter("D", 0)  };

        Letter[] rightOne  =    {
                new Letter("D", 5),
                new Letter("E", 4),
                new Letter("F", 3),
                new Letter("A", 2),
                new Letter("B", 1),
                new Letter("C", 0)  };
        Letter[] leftOne={
                new Letter("C", 5),
                new Letter("B", 4),
                new Letter("A", 3),
                new Letter("F", 2),
                new Letter("E", 1),
                new Letter("D", 0)  };

        Reflector ref=new Reflector(new int[]{3,4,5,0,1,2});


        SpinningRotor one = new SpinningRotor(rightOne,leftOne,0,3);
        SpinningRotor two = new SpinningRotor(rightTwo,leftTwo,0,0);
        printRotors(one,two);

        one.setRotor("D","C");
        two.setRotor("D","C");

        printRotors(one,two);

        one.move();
        two.move();

        printRotors(one,two);

        int firstdecode= one.decode(1,false);
        int secondDecode= two.decode(firstdecode,false);
        int afterRef=ref.decode(secondDecode,false);
        int fourDecode= two.decode(afterRef,true);
        int endingdecode= one.decode(fourDecode,true);
        System.out.println(endingdecode);




//        ABC notes = new ABC(new char[]{'a', 'b', 'c'});
//        System.out.println(notes.toIndex('a'));
//        System.out.println(notes.toIndex('b'));
//        System.out.println(notes.toIndex('c'));

        //////////////////////////////////////////



        System.out.println(" ============================= ");
        System.out.println("move");
     //   r.move();
     //   r.print();

        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));

//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
//
//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
//
//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
//
//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
//
//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
//
//        //////////////////////////////////////////
//
//        System.out.println(" ============================= ");
//        System.out.println("move");
//        r.move();
//        r.print();
//
//        System.out.println(" ========= ");
//        System.out.println("the wanted index is: " + r.decode(0, true));
    }
}