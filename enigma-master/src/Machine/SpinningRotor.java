package Machine;

import java.util.Arrays;


public class SpinningRotor implements Rotor {
    int size;
    int notch=3;
    int pos;

    Letter[] leftArr;
    Letter[] rightArr;

    public SpinningRotor(Letter[] rightArr ,Letter[] leftArr,int pos,int notch ) {
        this.leftArr=leftArr;
        this.rightArr=rightArr;
        this.pos=pos;
        this.notch=notch;
        size = 6;
    }
    public int getPos(){return pos;}

    public int getSize() {
        return size;
    }

    public Letter[] getLeftArr(){
        return leftArr;
    }

    public Letter[] getRightArr(){
        return rightArr;
    }

    public boolean isNotch(){
        return pos==notch;
    }
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
        if(pos==size){
            pos=0;
        }

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
        if(pos==notch){
            System.out.print("spin!");
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
