package Machine;


public class SpinningRotor implements Rotor {

    int size, pos, id;
    int notch;
    Letter[] leftArr, rightArr;

    public int getId() {
        return id;
    }



    public SpinningRotor(Letter[] rightArr , Letter[] leftArr, int leftNotch, int id) {

        this.leftArr=leftArr; this.rightArr=rightArr;
        notch=leftNotch; pos=0; size = rightArr.length;
        this.id=id;
    }

    ///////////////////////////////////////////////////////////

    // ******************** Rotors getters *********************
    public int getPos(){

        return pos;
    }

    public int getNotch() {
        return notch;
    }


    public Letter[] getRightArr(){
        return rightArr;
    }
    public int indexOf(String searchedValue){
        for(int i = 0; i< size ;i++ ){

            if(rightArr[i].letter.equals(searchedValue) ){


                return i;
            }
        }
        return -1;

    }

    // ******************** Rotors setters *********************
    public void setRotor(String right) throws Exception {
        int newPosIndex = indexOf(right);
        if(newPosIndex==-1){
            throw  new Exception("letter was not found");
        }
        pos = newPosIndex;
    }


    ///////////////////////////////////////////////////////////

    // ******************** Rotors actions *********************
    public boolean move(){

        pos++;
        if( pos == size) { pos = 0;}
        return (pos==notch);
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
    }}



