package Machine;

import java.util.ArrayList;

public class Machine {

    private PlugBoard plugBoard;
    private ArrayList<SpinningRotor> activeRotors;
    private int numberOfActiveRotors;
    private SpinningRotor[] rotors;
    private Reflector[] reflectors;
    private Reflector activeReflector;
    private ABC abc;

    /////////////////////////////////////////////
    public Machine() {}

    /////////////////////////////////////////////

    // ************ the machine getter's functions ************

    public SpinningRotor[] getRotorsArr(){
        return this.rotors;
    }
    public Reflector[] getReflectors(){
        return this.reflectors;
    }
    public ABC getAbc() {
        return this.abc;
    }
    public SpinningRotor getRotor(int id) throws Exception {

        for ( int i = 0 ; i < rotors.length ; i++ ) {

            if ( rotors[i].getId() == id ) {

                return rotors[i];
            }
        }
        throw new Exception("rotor was not found");
    }
    public void addActiveRotor(int index) {

        this.activeRotors.add(rotors[index]);
        numberOfActiveRotors++;
    }
    public PlugBoard getPlugBoard() {

        return plugBoard;
    }
    public ArrayList<Integer> getWantedPlugs(){

        return plugBoard.getWantedPlugs();
    }
    public ArrayList<SpinningRotor> getActiveRotors() {

        return activeRotors;
    }
    public Reflector getActiveReflector() {

        return activeReflector;
    }
    public int getNumberOfActiveRotors() {
        return activeRotors.size();
    }
    public int getNumberOfRotors() {
        return rotors.length;
    }

    // ************ the machine setter's functions ************

    public void setRotors(SpinningRotor[] rotors) {
        this.rotors = rotors;
    }
    public void setABC(ABC abc){
        this.abc = abc;
    }
    public void setActiveRotors(ArrayList<SpinningRotor> arr, int size){

        this.numberOfActiveRotors = size;
        this.activeRotors = arr;
    }
    public void setReflector(Reflector ref) {

        this.activeReflector = ref;
    }
    public void setPlugBoard(PlugBoard p){

        this.plugBoard = p;
    }
    public void setReflectors( Reflector[] reflectors) {
        this.reflectors = reflectors;
    }

    // ************ the machine action's functions ************

    public void moveRotorsByNotch() {

        boolean inNotch = activeRotors.get(0).move();

        for(int i=1 ; i<numberOfActiveRotors ; i++) {

            if (inNotch) {  inNotch = activeRotors.get(i).move(); }
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
            res = activeRotors.get(i).decode(res,false);
        }

        //////////////////////////////////////////////////////////

        // step 2: go throw the reflector
        res = activeReflector.decode(res,false);

        //////////////////////////////////////////////////////////

        // step 3: go throw the rotors the second time: left -> right
        for (int i = numberOfActiveRotors - 1 ; i >= 0 ; i--) {
            res = activeRotors.get(i).decode(res,true);
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
    public ArrayList<SpinningRotor> getRotors() {
        ArrayList<SpinningRotor> rotorsList = new ArrayList<>();
        for (int i = 0; i < rotors.length; i++) {
            rotorsList.add(rotors[i]);
        }
        return  rotorsList;
    }

}
