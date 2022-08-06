package Machine;


public class SpinningRotor implements Rotor {

    int size, pos, id;
    String notch;
    Letter[] leftArr, rightArr;

    public SpinningRotor(Letter[] rightArr ,Letter[] leftArr, String leftNotch, int id) {

        this.leftArr=leftArr; this.rightArr=rightArr;
        notch=leftNotch; pos=0; size = 6;
        this.id=id;
    }

    ///////////////////////////////////////////////////////////

    // ******************** Rotors getters *********************
    public int getPos(){

        return pos;
    }
    public Letter[] getLeftArr(){

        return leftArr;
    }
    public Letter[] getRightArr(){

        return rightArr;
    }

    // ******************** Rotors setters *********************
    public void setRotor(String left, String right){

        for(int i = 0; i< size ;i++ ){

            if(leftArr[i].letter.equals(left) || rightArr[i].letter.equals(right)){

                pos = i;
                return;
            }
        }
    }


    ///////////////////////////////////////////////////////////

    // ******************** Rotors actions *********************
    public boolean move(){

        pos++;
        if( pos == size) { pos = 0;}

        return leftArr[pos].letter.equals(notch);
    }
    @Override
    public int decode(int index, boolean dir) {

        //  dir = true:  left -> right
        //  dir = false: left <- right
        
        int ind, theTwinIndex, res;
        ind = (index + pos) % size;

        if (dir) { theTwinIndex =  leftArr[ind].myTwinIndex;  }
        else     { theTwinIndex = rightArr[ind].myTwinIndex;  }

        res = (theTwinIndex - pos) % size;

        if (res < 0) {  res += size;  }

        return res;
    }
}
