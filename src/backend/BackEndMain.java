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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Machine.Letter;
import  Machine.SpinningRotor;
import Machine.Reflector;
import Machine.PlugBoard;
import Machine.ABC;

public class BackEndMain {

    Machine myEnigma;
    String[] rotorsInitState;
    ABC abc;
    Reflector[] reflectors;
    private int messagesCount;

    private ArrayList<SavedEncode> messages;
    private ArrayList<String> machineSettings;

    ///////////////////////////////////////////

    public int getMessagesCount() {
        return messagesCount;
    }

    public void addEncode(String in, String out) {

        /* check for doubles
        for( int i = 0 ; i < messages.size() ; i++) {

            if(in.equals( messages.get(i).getInString() )) { return; }
        }*/

        SavedEncode s = new SavedEncode();
        s.setInString(in);
        s.setOutString(out);
        s.setMachineSettings(getFormatedStats()); // todo

        if(!machineSettings.contains(getFormatedStats())) {
            machineSettings.add(getFormatedStats());
        }
        messages.add(s);
    }

    public void showHistory(long start, long end) {

        for (String set : machineSettings) {

            System.out.println("The machine settings are: " + set);

            for (SavedEncode s : messages) {

                if (set.equals(s.getMachineSettings())) {

                    long time = end - start;
                    String in = s.getInString();
                    String out = s.getOutString();

                    System.out.println("#. <" + in + "> --> <" + out + "> (" + time + " nano-seconds)");
                    System.out.println();
                }
            }
        }
    }


    ///////////////////////////////////////////

    public BackEndMain() {

        machineSettings = new ArrayList<>();
        messages = new ArrayList<>();
        messagesCount = 0;
    }


    ///////////////////////////////////////////
    public  String activeRotorsDisplay(){
        String res="";
        for (SpinningRotor rotor:myEnigma.getActiveRotors()){
            res+=rotor.getId();
            res+=",";
        }
        res.substring(0, res.length() - 1);
        return res;
    }

    public  String activeRotorsPosDisplay(){
        String res="";
        for (SpinningRotor rotor:myEnigma.getActiveRotors()){
            res+=rotor.getRightArr()[rotor.getPos()].theLetter();
            res+=",";
        }
        res.substring(0, res.length() - 1);
        return res;
    }

    public  String ReflectorDisplay(){
        String res="";
        for (Reflector ref:myEnigma.getReflectors()){
            res+=ref.getId();
            res+=",";
        }

        return res;
    }


    public Reflector[] getReflectors() {
        return reflectors;
    }

    public  String activeReflectorDisplay(){
        return myEnigma.getActiveReflector().getId();
    }

//    not sure about that one
    public  boolean isRotorExist(int id){
        try {
            myEnigma.getRotor(id);
            return true;
        }catch (Exception e){
            return  false;
        }
    }

    public  String activePlugBoardStateDisplay(){
        String res="";
        ArrayList<Integer> seen = new ArrayList<>();
        for (HashMap.Entry<Integer, Integer> entry :  myEnigma.getPlugBoard().getPlugs().entrySet()) {
            int key = entry.getKey();
            int value = entry.getValue();
            if(key!=value && !seen.contains(value) &&!seen.contains(key)){
                res+=abc.toLetter(key)+"|"+abc.toLetter(value);
                seen.add(key);

            }

        }
        return res;
    }

    public ABC getAbc() {
        return abc;
    }

    public String getFormatedStats()
    {
        return "<"+this.activeRotorsDisplay()+">" +
                " <"+this.activeRotorsPosDisplay()+">" +
                " <"+this.activeReflectorDisplay()+"> " +
                "<"+this.activePlugBoardStateDisplay()+">";
    }
    public static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext jc=JAXBContext.newInstance("enigma.jaxb.schema.generated");
        Unmarshaller U = jc.createUnmarshaller();
        return (CTEEnigma) U.unmarshal(in);
    }


    public String setXmlData(String path) throws JAXBException {

        try {
            if(checkIfFileExists(path)==NO_FILE){
                throw new Exception("file is not there");
            }else if (checkIfFileExists(path)==INCORRECT){
                throw new Exception("file is not a xml file");
            }

            InputStream inputStream = new FileInputStream(path);
            CTEEnigma rotors = deserializeFrom(inputStream);

            int amountOfRotors = rotors.getCTEMachine().getRotorsCount();
            int amountOfReflectors = rotors.getCTEMachine().getCTEReflectors().getCTEReflector().size();
            int abcLength = rotors.getCTEMachine().getABC().trim().length();
            int PositionsSize = rotors.getCTEMachine().getCTERotors().getCTERotor().get(0).getCTEPositioning().toArray().length;


            if(checkRotorSize(amountOfRotors)==INCORRECT){
                throw  new Exception("file include only one rotor?!! this is not SAFE..try again dummy");
            }
            if(checkAbcSize(abcLength)==INCORRECT){
                throw  new Exception("abc in the settings is not ok, curr size is "+abcLength );
            }
            if(checkDoubleInRotor(rotors.getCTEMachine().getCTERotors().getCTERotor(),PositionsSize)==INCORRECT){
                throw  new Exception("some of the rotors are not uniuqe");

            }
            reflectors = new Reflector[amountOfReflectors];
            SpinningRotor[] machineRotors =new SpinningRotor[amountOfRotors + 1];

            abc = new ABC(rotors.getCTEMachine().getABC().trim().toCharArray());

            // ---------------------init rotors---------------------
            rotorsInitState = new String[amountOfRotors + 1];

            for( int i = 0 ; i <= amountOfRotors ; i++ ) {

                CTERotor currPos = rotors.getCTEMachine().getCTERotors().getCTERotor().get(i);
                if(checkNotch(currPos.getNotch(),currPos.getCTEPositioning().size())==INCORRECT){
                    throw  new Exception("rotor number "+i+" is having a unvaild notch");
                }

                Letter[] right = new Letter[PositionsSize];
                Letter[] left  = new Letter[PositionsSize];

                for( int k = 0 ; k < PositionsSize ; k++) {

                    String currRight = currPos.getCTEPositioning().get(k).getRight();

                    for ( int j=0 ; j < PositionsSize ; j++) {

                        String currLeft = currPos.getCTEPositioning().get(j).getLeft();

                        if (currRight.equals(currLeft)) {

                            right[k] = new Letter(currRight, j);
                            left[j]  = new Letter(currRight, k);
                            break;
                        }
                    }
                }
                rotorsInitState[i]=String.valueOf(abc.toLetter(0));
                SpinningRotor rotor = new SpinningRotor(right, left, currPos.getNotch()-1 , currPos.getId());
                machineRotors[i] = rotor;

            }
            if(checkRotorsID(machineRotors,amountOfRotors)==INCORRECT){
                throw new Exception("rotors id are not in order");
            }

//            here

            //-----------------------------init ref---------------------
            List<CTEReflector> refs= rotors.getCTEMachine().getCTEReflectors().getCTEReflector();

            //  Reflector[] refsInit = new Reflector[amountOfReflectors];
            int refAbcSize = abcLength;
            //int refSize = refs.get(0).getCTEReflect().size();

            for ( int i=0 ; i < amountOfReflectors ; i++ ) {

                int[] initRef = new int[refAbcSize+1];
                List <CTEReflect> currRefs = refs.get(i).getCTEReflect();

                for(int k = 0 ; k < refAbcSize / 2 ; k++){

                    int I =  currRefs.get(k).getInput() - 1;
                    int U =  currRefs.get(k).getOutput() - 1;
                    if(checkDoubleInReflectors(I,U)==INCORRECT){
                        throw new Exception("in reflector "+i+" row number "+k+" is unvaild,letter cant go to itself, dummy");
                    }
                    initRef[I] = U;
                    initRef[U] = I;
                }
                reflectors[i] = new Reflector(initRef,refs.get(i).getId());
            }
            if(checkReflectorID(reflectors,amountOfReflectors)==INCORRECT){
                throw new Exception("one of the Reflectors id is not vaild");
            }
            //  -------------------------init PlugBoard---------------------
            int[] plugInit = new int[refAbcSize];
            for( int i=0 ; i < refAbcSize ; i++ ){

                plugInit[i]=i;
            }
            PlugBoard plugBoard = new PlugBoard(plugInit);

            myEnigma = new Machine(plugBoard,machineRotors,reflectors);
            myEnigma.addActiveRotor(0);
            myEnigma.addActiveRotor(1);
            myEnigma.addActiveRotor(2);
            System.out.println("done");
        }

        catch (Exception e) {
            e.printStackTrace();
            return  e.getMessage();
        }
        return "ok";
    }

    ///////////////////////////////////////////




    public Machine getMachine(){
        return myEnigma;
    }
    public String RandomMachine(){
          /*    TODO----choose random amount of rotors in a random order
                TODO----set rotors to random pos
                TODO----pick a random refactor off the reflectors available
                TODO----plugBoard, pick how many will switch and randomly choose pairs
                TODO----set the machine to the random settings we just made and add it to the default state in the backend */
        return "";
    }
    public void setReflectorViaUser(String r){

        for (Reflector ref: reflectors){

            if( ref.getId().equals(r)) {
                myEnigma.setReflector(ref);
            }
        }
    }
    public void setPlugBoardViaUser(char[] plugs) {

        int[] letters = new int[abc.getSize()];
        for(int i = 0 ; i < abc.getSize() ; i++){

            letters[i] = i;
        }

        PlugBoard p = new PlugBoard(letters);

        if( plugs.length%2!=0 ){
//            TODO fix it , make it pretty
            throw  new Exception("plug board blah blah");
        }

        for(int i = 0 ; i < plugs.length  ; i+=2) {

            int a = abc.toIndex(plugs[i]);
            int b = abc.toIndex(plugs[i+1]);

            p.PlugIn(a,b);
        }

        myEnigma.setPlugBoard(p);
    }
    public void setRotorsViaUser (int[] rotorsId, String[] posSet) {

        //<45,27,94><AO!><III><A|Z,D|E>
        ArrayList<SpinningRotor> newRotors = new ArrayList<>();

        for (int i = 0 ; i < rotorsId.length ; i++) {

            try {

                int id = rotorsId[i];
                SpinningRotor rotor = myEnigma.getRotor(id);
                rotor.setRotor(posSet[i]);
                rotorsInitState[i] = posSet[i];

                newRotors.add(rotor);
            }

            catch (Exception e){

                e.printStackTrace();
            }
        }

        myEnigma.setActiveRotors(newRotors, rotorsId.length);

    }
    public String DecodeString(String rawString) {
        if ( myEnigma.getActiveRotors().size() == 0 ) {

            return  "no active rotors at the moment...";
        }

        char[] string = rawString.toCharArray();
        String res = "";

        if ( abc.checkInAbc(rawString) ) {

            for ( char c: string ) {

                int decodedLetter = myEnigma.act(abc.toIndex(c));
                res+=abc.toLetter(decodedLetter);
            }
            messagesCount++;
            addEncode(rawString, res.toString());

            //////// TODO-----SHOULD BE ITS OWN METHOD
            messagesCount++;
            for ( int i = 0 ; i < myEnigma.getActiveRotors().size() ; i++ ) {

                myEnigma.getActiveRotors().get(i).setRotor(rotorsInitState[i]);
            }
            /////////////////////////////////////////////////

            return res.toString();
        }

        else {
            return "bad string";
        }
    }

    ///////////////////////////////////////////

    private static final int NO_FILE = 0;
    private static final int CORRECT = 2;
    private static final int INCORRECT = 3;

    // validation check

    /* TODO!!
    *
דגשים:
•	יש לאפשר למשתמש לטעון כמה קבצים אחד אחרי השני (כלומר להפעיל את הפקודה כמה פעמים רצוף).
כל קובץ תקין "דורס" לחלוטין את כל פרטי הקובץ (התקין) שהיה טעון לפניו במע' (ככל שהיה כזה).
כל נסיון טעינה של קובץ תקול לא דורס את פרטי הקובץ (התקין) האחרון שהיה במע' (ככל שהיה כזה)

•	פקודה זו מוצגת ומאופשרת תמיד. אפשר לבחור בה בכל רגע נתון במע'.

    *
    * */


    public int checkIfFileExists(String path) {
        File tempFile = new File(path);
        boolean isRightExtension = path.endsWith(".xml");
        boolean isExists = tempFile.exists();
        if(isRightExtension &&  isExists) return CORRECT;
        else if(!isExists) return NO_FILE;
        else return INCORRECT;
    }
    public int getAmountOfRotors(){

        return myEnigma.getNumberOfRotors();
    }

    public boolean inRotorsRange(int size){

     return size <= myEnigma.getNumberOfRotors();
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

            if( arr[id-1] == 0 )
                arr[id-1]=id;
            else
                return INCORRECT;
        }

        for ( int i = 1; i < size + 1 ; i++) {

            if( arr[i] == 0)
                return INCORRECT;
        }

        return CORRECT;
    }
    public int checkDoubleInRotor(List<CTERotor> all, int posSize){

        for(CTERotor rotor: all) {
            ArrayList<String> seen = new ArrayList<>();
            for( int k = 0 ; k < posSize ; k++) {
                String currRight = rotor.getCTEPositioning().get(k).getRight();
                if(seen.contains(currRight)){
                    return INCORRECT;
                }
                seen.add(currRight);
        }

        }
        return CORRECT;

    }


    public int checkNotch(int n, int size){
        if(n > size || n < 0)
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
    public int checkDoubleInReflectors(int left,int right){
        if(left==right){
            return INCORRECT;
        }
        return CORRECT;
        // TODO!  9.	אין מיפוי בין אות לעצמה באף לא אחד מן המשקפים המוגדרים

    }
}
