package Machine;

public class Letter {

    String letter;
    int myTwinIndex;

    public Letter(){}
    public Letter(String theLetter, int index) {
        letter = theLetter;
        myTwinIndex = index;
    }

    public String theLetter(){

        return letter;
    }

    public int twinIndex(){

        return myTwinIndex;
    }
}
