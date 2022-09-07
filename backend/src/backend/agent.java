package backend;
import Machine.Machine;

import Machine.SpinningRotor;
import common_models.activeRotor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class agent {

    private final int agentId;
    private final String input;
    private final int missionSize;
    private ArrayList<String> possibleMessages;
    private Machine machine;
    private String[] rotorsInitState;
    private ArrayList<String> Dictionary;

    ///////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\ //////////////////////////// \\\\\\\\\\\\\\\\\\\\\\

    public agent(int id,  String input, int missionSize) throws Exception {

        this.agentId = id;
        this.input = input;
        this.missionSize = missionSize;

        this.possibleMessages = new ArrayList<>();
    }

    ///////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\ //////////////////////////// \\\\\\\\\\\\\\\\\\\\\\

    public void setMachine(Machine machine, String[] initState) {

        this.rotorsInitState = initState;
        this.machine = machine;
        this.machine.setActiveRotors(machine.getActiveRotors(),machine.getNumberOfActiveRotors());
        this.machine.setPlugBoard(machine.getPlugBoard());

    }
    public void setDictionary(ArrayList<String> dictionary) {
        this.Dictionary = dictionary;
    }

    ///////////////////////// \\\\\\\\\\\\\\\\\\\\\\\\\\\\ //////////////////////////// \\\\\\\\\\\\\\\\\\\\\\

    public void resetMachine(){

        try {
            for (int i = 0; i < machine.getActiveRotors().size(); i++) {
                machine.getActiveRotors().get(i).setRotor(rotorsInitState[i]);
            }
        }

        catch (Exception e) {

            e.printStackTrace();
        }
    }
    public String DecodeString(String rawString)  {

        if ( machine.getActiveRotors().size() == 0 ) {

            return  "no settings were set, please choose command 3 or 4 before trying to decode";
        }

        char[] string = rawString.toCharArray();
        StringBuilder res = new StringBuilder();

            for ( char c: string ) {

                int intLetter = machine.getAbc().toIndex(c);
                int decodedLetter = machine.act(intLetter);
                char charLetter = machine.getAbc().toLetter(decodedLetter);
                res.append(charLetter);
            }
            return res.toString();
    }
    public Boolean checkInDictionary(String decrypt){

        System.out.println(decrypt);

        String[] decryptArr = decrypt.split(" ");

        boolean testFlag = true;

        for (String testWord: decryptArr) {

            testWord = testWord.toLowerCase();
            if( ! Dictionary.stream().anyMatch(testWord::equals) ) {

                testFlag = false;
                break;
            }
        }
        System.out.println("is good? " + testFlag);

        return testFlag;
    }
    public ArrayList<String> run() throws Exception {

        for (int i = 0; i < missionSize; i++) {

            String str = DecodeString(input);
            Boolean possibility = checkInDictionary(str);

            if ( possibility ){
                possibleMessages.add(str);
            }

            resetMachine();
            machine.moveRotorsByNotch();
        }
        return possibleMessages;
    }
}
