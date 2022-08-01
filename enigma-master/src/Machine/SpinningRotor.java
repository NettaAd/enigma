package Machine;

import java.util.Arrays;


public class SpinningRotor implements Rotor {
    int size;
    int pos;

    /*
    * A 3
    * B 4
    * C 5
    * A 0
    * B 1
    * C 2
    *
    *
    *
    *
    * */
    Letter[] leftArr = {    new Letter("F", 2),
                            new Letter("C", 5),
                            new Letter("A", 3),
                            new Letter("E", 1),
                            new Letter("B", 4),
                            new Letter("D", 0)  };

    Letter[] rightArr = {   new Letter("D", 5),
                            new Letter("E", 3),
                            new Letter("F", 0),
                            new Letter("A", 2),
                            new Letter("B", 4),
                            new Letter("C", 1)  };



    public int my_decode(int index) {

      //  decode: left <- right
        int ind = (index + pos) % size;


        int theTwinIndex = rightArr[ind].myTwinIndex;
        int res = (theTwinIndex - pos) % size;

        if(res < 0){
            return res+size;
        }
        return res;
    }





    public SpinningRotor() { pos = 0; size = 6;}

    public void print(){

        int ind = pos;
        for(int i = 0; i < size; i++) {

            System.out.println(i+": " +leftArr[ind % size].letter + " " + rightArr[ind % size].letter );
            ind++;
        }

        System.out.println(" ");

    }
    public void move(){
        /*int ind = pos;

        Letter[] r = new Letter[size];
        Letter[] l = new Letter[size];
        for(int i = 0; i < size; i++) {
            r[(i + 1) % size] = rightArr[i];
            ind++;

        }
        ind=pos;
        for(int i = 0; i < size; i++) {
            l[(i + 1) % size] = leftArr[i];
            ind++;

        }
        rightArr=r;
        leftArr=l;
*/
        pos++;
    }

    public void setRotor(String left, String right){

        for(int i = 0; i< size ;i++ ){

            if(leftArr[i].letter.equals(left) || rightArr[i].letter.equals(right)){

                pos = i;
                return;
            }
        }
    }

    public String getCurrentPos(){

        return leftArr[0].letter + " " + rightArr[0].letter;
    }

    /*  we get the index of one side and need to return
        the letter that pit to it in the other side     */
    public Letter getMyLeft(int index){

        return leftArr[index];
    }
    public Letter getMyRight(int index){

        return rightArr[index];
    }

    @Override
    public int decode(int index, boolean dir) {

        //  dir = true:  left -> right
        //  dir = false: left <- right
        
        int ind, theTwinIndex, res;
        ind = (index + pos) % size;

        if (dir) { theTwinIndex =  leftArr[ind].myTwinIndex;   }
        else     { theTwinIndex = rightArr[ind].myTwinIndex;  }

        res = (theTwinIndex - pos) % size;

        if (res < 0) {

            res += size;
        }

        return res;


        /*int index = 0;
        for (Letter entryLetter : leftArr) {

            if (rightArr[in].letter==entryLetter.letter) {
                return index;
            }
            else {
                index++;
            }
        }
        return 0;*/
    }
}
