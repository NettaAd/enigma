package backend;

import Machine.Machine;
import Machine.ABC;
import Machine.SpinningRotor;
import common_models.activeRotor;

import java.util.ArrayList;
import java.util.List;

public class agent {

    private int id;
    private Machine myEnigma;
    private ArrayList<String> possibleMessages;
    private String input;
    private ArrayList<activeRotor> initRotors;
    private String refId;
    private int missionSize;
    private ArrayList<String> wordsDic;

    private ABC abc;



    public void setRotorsViaUserBetter (List<activeRotor> rotors) throws Exception {

        ArrayList<SpinningRotor> newRotors = new ArrayList<>();
        for(int i = rotors.size()-1; i>=0; i--)
        {
            activeRotor currRotor = rotors.get(i);
            int id = Integer.parseInt(currRotor.getId());
            SpinningRotor rotor = myEnigma.getRotor(id);
            String currPos = currRotor.getPosition();
            rotor.setRotor(currPos);
            newRotors.add(rotor);
        }
        this.myEnigma.setActiveRotors(newRotors, rotors.size());
    }

    public String DecodeString(String rawString)  {

        if ( myEnigma.getActiveRotors().size() == 0 ) {

            return  "no settings were set, please choose command 3 or 4 before trying to decode";
        }
        char[] string = rawString.toCharArray();
        StringBuilder res = new StringBuilder();

            for ( char c: string ) {

                int intLetter = abc.toIndex(c);
                int decodedLetter = myEnigma.act(intLetter);
                char charLetter = abc.toLetter(decodedLetter);
                res.append(charLetter);
                
            }
            return res.toString();
        }


    public agent(int id, Machine myEnigma, String input, ArrayList<activeRotor> initRotors, String refId, int missionSize, ArrayList<String> wordsDic,ABC abc) throws Exception {
        this.id = id;
        this.myEnigma = myEnigma;
        this.possibleMessages = new ArrayList<>();
        this.input = input;
        this.initRotors = initRotors;
        this.refId = refId;
        this.missionSize = missionSize;
        this.wordsDic = wordsDic;
        this.abc=abc;
        setRotorsViaUserBetter(initRotors);
    }

    public ArrayList<activeRotor> getActiveRotorsBeter(){
        ArrayList<activeRotor> rotorList = new ArrayList<>();
        myEnigma.getActiveRotors().stream().forEach(e->{
            SpinningRotor currRef = e;
            String Position=currRef.getRightArr()[e.getPos()].theLetter();
            int Order= myEnigma.getActiveRotors().indexOf(currRef);
            String Id =String.valueOf(e.getId());
            activeRotor currRotor = new activeRotor(Id,Order,Position,e.getNotch());


            rotorList.add(currRotor);
        });
        return rotorList;
    }

    public void run() throws Exception {
        for (int i = 0; i < missionSize; i++) {
            System.out.println("rotors at");
           initRotors.stream().forEach(e->System.out.println(e.getPosition()));
            String toTestString = DecodeString(this.input);
//            check if string is an option testString(toTestString);
            setRotorsViaUserBetter(initRotors);
            System.out.println("rotors after");

            initRotors.stream().forEach(e->System.out.println(e.getPosition()));

            myEnigma.moveRotorsByNotch();
            initRotors=getActiveRotorsBeter();
            
        }
       
    
    }


}
