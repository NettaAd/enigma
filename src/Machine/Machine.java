package Machine;

public class Machine {

    private PlugBoard plugBoard;
    private SpinningRotor[] activeRotors;
    private int numberOfActiveRotors;
    private Reflector reflector;

    public Machine() {}

    // ************ the machine setting's functions ************
    public void setActiveRotors(SpinningRotor[] arr, int size){

        this.numberOfActiveRotors = size;
        this.activeRotors = arr;
    }
    public void setReflector(Reflector ref) {

        this.reflector = ref;
    }
    public void setPlugBoard(PlugBoard p){

        this.plugBoard = p;
    }


    // ************ the machine action's functions ************
    public void moveRotorsByNotch() {

        boolean inNotch = activeRotors[0].move();

        for(int i=1 ; i<numberOfActiveRotors ; i++) {

            if (inNotch) {  inNotch = activeRotors[i].move(); }
            else         {  break; }
        }
    }
    public int getFromPlugBoard(int ind){

        return plugBoard.decode(ind, true);
    }
    public int decodeLetter(int index){

        int res = index;

        //////////////////////////////////////////////////////////

        // step 1: go throw the rotors the first time: left <- right
        for (int i=0; i < numberOfActiveRotors; i++) {
            res = activeRotors[i].decode(res,false);
        }

        //////////////////////////////////////////////////////////

        // step 2: go throw the reflector
        res = reflector.decode(res,false);

        //////////////////////////////////////////////////////////

        // step 3: go throw the rotors the second time: left -> right
        for (int i = numberOfActiveRotors - 1 ; i >= 0 ; i--) {
            res = activeRotors[i].decode(res,true);
        }

        return res;
    }
    public int act(int ind){

        // step 1: move the first Rotor and the other by the notch
        moveRotorsByNotch();

        ////////////////////////////////////////////////////////////

        // step 2: find out what char is the match of ind in the plug board
        int row  = getFromPlugBoard(ind);

        ////////////////////////////////////////////////////////////

        // step 3: decode res by all the active rotors
        int res  = decodeLetter(row);

        ////////////////////////////////////////////////////////////

        // step 4: find out what char is the match of res in the plug board
        return getFromPlugBoard(res);
    }
}
