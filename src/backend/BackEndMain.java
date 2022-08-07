package backend;

import Machine.Machine;
import enigma.jaxb.schema.generated.CTEEnigma;
import enigma.jaxb.schema.generated.CTEReflect;
import enigma.jaxb.schema.generated.CTEReflector;
import enigma.jaxb.schema.generated.CTERotor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Machine.Letter;
import  Machine.SpinningRotor;
import  Machine.Rotor;
import Machine.Reflector;
import Machine.PlugBoard;
import Machine.ABC;

public class BackEndMain {
Machine myEnigma;

String[] rotorsInitState;

ABC abc;


    public static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc=JAXBContext.newInstance("enigma.jaxb.schema.generated");
        Unmarshaller U = jc.createUnmarshaller();
        return (CTEEnigma) U.unmarshal(in);
    }
    public String setXmlData(String path) throws JAXBException {
        try{
            InputStream inputStream = new FileInputStream(new File(path));
            CTEEnigma rotors = deserializeFrom(inputStream);
            int amoutOfRotors=rotors.getCTEMachine().getRotorsCount();
            int amoutOfReflectors=rotors.getCTEMachine().getCTEReflectors().getCTEReflector().size();
            int abcLength=rotors.getCTEMachine().getABC().length();
            int PositionsSize =rotors.getCTEMachine().getCTERotors().getCTERotor().get(0).getCTEPositioning().toArray().length;
            Reflector[] reflectors= new Reflector[amoutOfReflectors];
            SpinningRotor[] machineRotors =new SpinningRotor[amoutOfRotors+1];

            abc=new ABC(rotors.getCTEMachine().getABC().trim().toCharArray());

//            ---------------------init rotors---------------------
            rotorsInitState=new String[amoutOfRotors+1];
            for(int i=0;i<=amoutOfRotors;i++){
                CTERotor currPos=rotors.getCTEMachine().getCTERotors().getCTERotor().get(i);
                Letter[] right=new Letter[PositionsSize];
                Letter[] left=new Letter[PositionsSize];;
                for(int k=0;k<PositionsSize;k++){
                    String currRight = currPos.getCTEPositioning().get(k).getRight();
                    for(int j=0;j<PositionsSize;j++) {
                        String currLeft = currPos.getCTEPositioning().get(j).getLeft();
                        if (currRight.equals(currLeft)) {
                            right[k] = new Letter(currRight, j);
                            left[j] = new Letter(currRight, k);
                            break;
                        }
                    }
                }
                SpinningRotor rotor=new SpinningRotor(
                        right,
                        left,
                        currPos.getNotch()-1,
                        currPos.getId());
                rotor.setRotor("D");
                rotorsInitState[i]="D";

                machineRotors[i]=rotor;


                }

            //            ---------------------init ref---------------------
            List<CTEReflector> refs= rotors.getCTEMachine().getCTEReflectors().getCTEReflector();
//            Reflector[] refsInit = new Reflector[amoutOfReflectors];
            int refAbcSize = abcLength/2;
            int refSize = refs.get(0).getCTEReflect().size();
            for(int i=0;i<amoutOfReflectors;i++){
                int[] initRef = new int[refAbcSize];
                List<CTEReflect> currRefs=  refs.get(i).getCTEReflect();
                for(int k=0;k<refAbcSize/2;k++){
                    int I=  currRefs.get(k).getInput()-1;
                    int U =  currRefs.get(k).getOutput()-1;
                    initRef[I]=U;
                    initRef[U]=I;
                }
                reflectors[i]=new Reflector(initRef,refs.get(i).getId());
            }
            //            ---------------------init PlugBoard---------------------
            int[] plugInit = new int[refAbcSize];
            for(int i=0;i<refAbcSize;i++){
                plugInit[i]=i;
            }
            PlugBoard plugBoard = new PlugBoard(plugInit);

            myEnigma = new Machine(plugBoard,machineRotors,reflectors);
            myEnigma.addActiveRotor(0);
            myEnigma.addActiveRotor(1);
            myEnigma.addActiveRotor(2);
            System.out.println("done");
        } catch (FileNotFoundException e) {
            return  e.getMessage();
        }
        return "ok";
    }

    public String RandomMachine(){
//        TODO----choose random amount of rotors in a random order
//        TODO----set rotors to random pos
//        TODO----pick a random reflactor off the reflactors aviable
//        TODO----plugBoard, pick how many will switch and randomly choose pairs
//        TODO----set the machine to the random settings we just made and add it to the deafult state in the backend

        return "";
    }

    //<45,27,94><AO!><III><A|Z,D|E>
    public void setRotorsViaUser (int[] rotorsId,String[] posSet){
        ArrayList<SpinningRotor> newRotors = new ArrayList<SpinningRotor>();
        for (int i=0;i<rotorsId.length;i++){
            try {
                SpinningRotor r = myEnigma.getRotor(rotorsId[i]);
                r.setRotor(posSet[i]);
                newRotors.add(r);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        myEnigma.setActiveRotors(newRotors,rotorsId.length);


    }

    public String DecodeString(String rawString){

        if(myEnigma.getActiveRotors().size()==0){
            return  "no active rotors at the moment...";
        }
        char[] string = rawString.toCharArray();


        String res="";
        if(abc.checkifInAbc(rawString)){
        for(char c: string){
            int decodedLetter = myEnigma.act(abc.toIndex(c));
            res+=abc.toLetter(decodedLetter);
        }
//        TODO-----SHOULD BE ITS OWN METHOD
        for (int i=0;i< myEnigma.getActiveRotors().size();i++){
            myEnigma.getActiveRotors().get(i).setRotor(rotorsInitState[i]);
        }
//---------------------------------
        return res;
        }
        else{
            return "bad string";
        }
    }

    ///////////////////////////////////////////

    private static final int NO_FILE = 0;
    private static final int CORRECT = 2;
    private static final int INCORRECT = 3;

    // validation check
    public int checkIfFileExists(){

        // ???
        return NO_FILE;
    }
    public int checkAbcSize(int size){

        if(size % 2 == 0) {

            return CORRECT;
        }
        else {

            return INCORRECT;
        }
    }
    public int checkRotorSize(int size){

        if(size >= 2)
            return CORRECT;
        else
            return INCORRECT;
    }
    public int checkActiveRotorSize(int sizeAll, int sizeActive){

        if(sizeAll >= sizeActive)
            return CORRECT;
        else
            return INCORRECT;

    }
    public int checkRotorsID(SpinningRotor[] all, int size){

        int[] arr = new int[size+1];
        // init
        for ( int i = 0 ; i < size + 1 ; i++) {

            arr[i] = 0;
        }

        for(SpinningRotor rotor: all) {

            int id = rotor.getId();

            if( arr[id] == 0 )
                arr[id]=id;
            else
                return INCORRECT;
        }

        for ( int i = 1; i < size + 1 ; i++) {

            if( arr[i] == 0)
                return INCORRECT;
        }

        return CORRECT;
    }
    public int checkDoubleInRotor(int n, int size){

        return INCORRECT;
    }
    public int checkNotch(int n, int size){

        if(n >= size || n < 0)
            return INCORRECT;
        else
            return CORRECT;
    }
    public int checkReflectorID(Reflector[] all, int size) {

        RomanNumber num = new RomanNumber();
        int[] arr = new int[size + 1];
        // init
        for (int i = 0; i < size + 1; i++) {

            arr[i] = 0;
        }

        for (Reflector ref : all) {

            String RomeID = ref.getId();
            int id = num.romanToInteger(RomeID);

            if (arr[id] == 0)
                arr[id] = id;
            else
                return INCORRECT;
        }

        for (int i = 1; i < size + 1; i++) {

            if (arr[i] == 0)
                return INCORRECT;
        }

        return CORRECT;
    }


}
