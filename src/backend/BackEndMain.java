package backend;

import Machine.*;
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
import java.util.Random;

public class BackEndMain {

    private Machine first;
    String firstSettings;

    private Machine myEnigma;
    private ABC abc;
    private Reflector[] reflectors;
    private String[] rotorsInitState;

    //////////////////////////////////////////////////////////////////////////////////////

    public BackEndMain() {

        first = new Machine();
        machineSettings = new ArrayList<>();
        messages = new ArrayList<>();
        messagesCount = 0;
    }

    ///////////////////////       getters       ///////////////////////
    public int getMessagesCount() {
        return messagesCount;
    }
    public Reflector[] getReflectors() {
        return reflectors;
    }
    public ABC getAbc() {
        return abc;
    }
    public String getFormatStats() {

        return  "<" + this.activeRotorsDisplay()         + "> "  +
                "<" + this.activeRotorsPosDisplay()      + "> "  +
                "<" + this.activeReflectorDisplay()      + "> "  +
                "<" + this.activePlugBoardStateDisplay() + ">" ;
    }
    public ArrayList<SpinningRotor> getRotorsArr(){
        return myEnigma.getActiveRotors();
    }
    public int getAmountOfRotors(){

        return myEnigma.getNumberOfRotors();
    }
    public int getAmountOfActiveRotors(){

        return myEnigma.getNumberOfActiveRotors();
    }
    public String getReflectorId(){

        return myEnigma.getActiveReflector().getId();
    }
    public ArrayList<Integer> getWantedPlugs(){

        return myEnigma.getWantedPlugs();
    }
    public String getFirstSettings(){
        return  "<" + this.activeRotorsInitDisplay()         + "> "  +
                "<" + this.activeRotorsInitPosDisplay()      + "> "  +
                "<" + this.activeReflectorDisplay()      + "> "  +
                "<" + this.activePlugBoardStateDisplay() + ">" ;
    }

    ///////////////////////       setters       ///////////////////////
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

      /*  if( plugs.length % 2 != 0 ){
           TODO fix it , make it pretty
            throw new Exception("plug board blah blah");
        }*/

        for(int i = 0 ; i < plugs.length; i+=2) {

            int a = abc.toIndex(plugs[i]);
            int b = abc.toIndex(plugs[i+1]);

            p.PlugIn(a,b);
        }

        myEnigma.setPlugBoard(p);
    }
    public void setRotorsViaUser (int[] rotorsId, char[] posSet) {

        ArrayList<SpinningRotor> newRotors = new ArrayList<>();

        for (int i = 0 ; i < rotorsId.length ; i++) {

            try {

                int id = rotorsId[i];
                SpinningRotor rotor = myEnigma.getRotor(id);

                String currPos = posSet[i] + "";
                rotor.setRotor(currPos);
                rotorsInitState[i] = currPos;

                newRotors.add(rotor);
            }

            catch (Exception e){

                e.printStackTrace();
            }
        }

        myEnigma.setActiveRotors(newRotors, rotorsId.length);

    }
    public void setFirstMachine(){

        firstSettings = getFormatStats();
    }

    ///////////////////////       4 random       ///////////////////////
    private ArrayList<Integer> usedId;
    private ArrayList<Integer> plugs;

    public void randRotors() {

        Random rand = new Random();

        int rotorsAmount = myEnigma.getNumberOfRotors()+1;
        int activeRotorsAmount = rand.nextInt(rotorsAmount);

        while(activeRotorsAmount < 2) { // minimum num of rotors is 2
            activeRotorsAmount = rand.nextInt(rotorsAmount);
        }

        usedId = new ArrayList<>();
        int[] activeRotorsId = new int[activeRotorsAmount];
        char[] activeRotorsSetPos = new char[activeRotorsAmount];

        for(int i = 0; i < activeRotorsAmount ; i++) {

            activeRotorsId[i] = randID(activeRotorsAmount);

            int letter = rand.nextInt(abc.getSize());
            activeRotorsSetPos[i] = abc.toLetter(letter);
        }

        setRotorsViaUser(activeRotorsId, activeRotorsSetPos);
    }
    public int randID(int size) {

        Random rand = new Random();
        int id = rand.nextInt(size) + 1; // id starts from 1

        while(usedId.contains(id)) {
            id = rand.nextInt(size) + 1;
        }

        usedId.add(id);

        return id;
    }
    public void randReflector(){

        Random rand = new Random();
        int index = rand.nextInt(reflectors.length);
        myEnigma.setReflector(reflectors[index]);
    }
    public void randPlugBoard(){

        Random rand = new Random();

        plugs = new ArrayList<>();
        int size = abc.getSize() / 2;
        int plugAmount = rand.nextInt(size);

        while(plugAmount % 2 != 0){
            plugAmount = rand.nextInt(size);
        }

        int[] res = new int[abc.getSize()];
        for( int i = 0 ; i < abc.getSize() ; i++) {
            res[i] = i;
        }

        PlugBoard p = new PlugBoard(res);
        for( int i = 0 ; i < plugAmount ; i++) {

            int a = randPlug();
            int b = randPlug();
            p.PlugIn(a, b);
        }

        myEnigma.setPlugBoard(p);
    }
    public int randPlug() {

        Random rand = new Random();
        int size = abc.getSize();
        int onePlug = rand.nextInt(size);

        while(plugs.contains(onePlug)) {

            onePlug = rand.nextInt(size);
        }

        plugs.add(onePlug);
        return onePlug;
    }
    public void RandomMachine() {

        randRotors();
        randReflector();
        randPlugBoard();

        // add it to the default state in the backend
    }

    ///////////////////////       5 encode       ///////////////////////
    public String DecodeString(String rawString) throws Exception {

        if ( myEnigma.getActiveRotors().size() == 0 ) {

            return  "no settings were set, please choose command 3 or 4 before trying to decode";
        }
        String settings = getFormatStats();
        char[] string = rawString.toCharArray();
        StringBuilder res = new StringBuilder();
        ArrayList<Character> badLetters =  abc.checkInAbc(rawString);
        if ( badLetters.isEmpty() ) {

            for ( char c: string ) {

                int intLetter = abc.toIndex(c);
                int decodedLetter = myEnigma.act(intLetter);
                char charLetter = abc.toLetter(decodedLetter);
                res.append(charLetter);
            }
            messagesCount++;
            addEncode(rawString, res.toString(),settings);
            return res.toString();
        }

        else {
            String resStr = "";
            for(int i=0;i<badLetters.size();i++){

                resStr+=badLetters.get(i);
                if(i!=badLetters.size()-1){
                    resStr+=',';
                }
            }
            throw new Exception("the next letter are not allowed with the curr settings "+resStr);
        }
    }
    public void resetMachine(){

        for ( int i = 0 ; i < myEnigma.getActiveRotors().size() ; i++ ) {

            myEnigma.getActiveRotors().get(i).setRotor(rotorsInitState[i]);
        }
    }

    ///////////////////////     6 reset to first settings    ///////////////////////

//    public void resetMachine(){
//        this.myEnigma=this.first;


//        String fi = getFirstSettings();
//        String st = getFormatStats();
//
//        myEnigma.setActiveRotors(first.getActiveRotors(), first.getNumberOfActiveRotors());
//        st = getFormatStats();
//
//        myEnigma.setReflector(first.getActiveReflector());
//        st = getFormatStats();
//
//        myEnigma.setPlugBoard(first.getPlugBoard());
//        st = getFormatStats();
//    }

    ///////////////////////      7 stats and history       ///////////////////////
    private int messagesCount;
    private final ArrayList<SavedEncode> messages;
    private final ArrayList<String> machineSettings;

    public void addEncode(String in, String out,String Curr_settings) {

        /* check for doubles
        for( int i = 0 ; i < messages.size() ; i++) {

            if(in.equals( messages.get(i).getInString() )) { return; }
        }*/

        SavedEncode s = new SavedEncode();
        s.setInString(in);
        s.setOutString(out);
        s.setMachineSettings(Curr_settings);

        if(!machineSettings.contains(Curr_settings)) {
            machineSettings.add(Curr_settings);
        }
        messages.add(s);
    }
    public ArrayList<SavedEncode> getMessages() {return messages;}
    public ArrayList<String> getMachineSettings(){return machineSettings;}
    public void resetHistory(){

        messagesCount = 0;
        messages.clear();
        machineSettings.clear();
    }

    ///////////////////////      2 display       ///////////////////////
    public String activeRotorsDisplay(){

        StringBuilder res = new StringBuilder();
        int size = myEnigma.getActiveRotors().size();

        for (int i = 0 ; i < size ; i++){

            SpinningRotor rotor = myEnigma.getActiveRotors().get(i);
            res.append(rotor.getId());

            int distanceFromNotch = ((rotor.getNotch() - rotor.getPos()) + abc.getSize() ) % abc.getSize();
            res.append( "(" +  distanceFromNotch + ")");

            if (i != size - 1) {
                res.append(",");
            }
        }

      // res.substring(0, res.length() - 1);

        return res.toString();
    }
    public String activeRotorsInitDisplay(){

        StringBuilder res = new StringBuilder();
        int size = myEnigma.getActiveRotors().size();

        for (int i = 0 ; i < size ; i++){

            SpinningRotor rotor = myEnigma.getActiveRotors().get(i);
            res.append(rotor.getId());

            int distanceFromNotch = ((rotor.getNotch() - abc.toIndex(rotorsInitState[i].toCharArray()[0] )) + abc.getSize() ) % abc.getSize();
            res.append( "(" +  distanceFromNotch + ")");

            if (i != size - 1) {
                res.append(",");
            }
        }

        // res.substring(0, res.length() - 1);

        return res.toString();
    }
    public String activeRotorsPosDisplay(){

        StringBuilder res = new StringBuilder();
        int size = myEnigma.getActiveRotors().size();
        for (int i = 0 ; i < size ; i++ ){

            SpinningRotor rotor  = myEnigma.getActiveRotors().get(i);
            res.append(rotor.getRightArr()[rotor.getPos()].theLetter());

            if (i != size - 1) {
                res.append(",");
            }
        }
       // res.substring(0, res.length() - 1);
        return res.toString();
    }

    public String activeRotorsInitPosDisplay(){

        StringBuilder res = new StringBuilder();
        int size = myEnigma.getActiveRotors().size();
        for (int i = 0 ; i < size ; i++ ){

            res.append(rotorsInitState[i]);

            if (i != size - 1) {
                res.append(",");
            }
        }
        // res.substring(0, res.length() - 1);
        return res.toString();
    }
    public String ReflectorDisplay(){

        StringBuilder res= new StringBuilder();

        for (Reflector ref: myEnigma.getReflectors()){

            res.append(ref.getId());

            res.append(",");
        }
        return res.toString();
    }
    public String activeReflectorDisplay(){

        return myEnigma.getActiveReflector().getId();
    }
    //    not sure about that one
    public boolean isRotorExist(int id){
        try {
            myEnigma.getRotor(id);
            return true;
        }

        catch (Exception e){
            return  false;
        }
    }
    public String activePlugBoardStateDisplay(){

        StringBuilder res = new StringBuilder();
        ArrayList<Integer> seen = new ArrayList<>();

        int i = 1;
        for (HashMap.Entry<Integer, Integer> entry:  myEnigma.getPlugBoard().getPlugs().entrySet()) {

            int key = entry.getKey();
            int value = entry.getValue();

            if(key != value && !seen.contains(value) && !seen.contains(key)) {

                res.append(abc.toLetter(key)).append("|").append(abc.toLetter(value));

                if(i != myEnigma.getPlugBoard().getWantedPlugs().size() / 2) {
                    res.append(",");
                }
                i++;
                seen.add(key);
            }
        }
        return res.toString();
    }

    ///////////////////////       1 file       ///////////////////////

    ///////////    xml staff    ///////////////
    public static CTEEnigma deserializeFrom(InputStream in) throws JAXBException {

        JAXBContext jc=JAXBContext.newInstance("enigma.jaxb.schema.generated");
        Unmarshaller U = jc.createUnmarshaller();
        return (CTEEnigma) U.unmarshal(in);
    }
    public String setXmlData(String path) throws JAXBException {

        try {
            if(checkIfFileExists(path) == NO_FILE){
                throw new Exception("file is not there");
            }
            else if (checkIfFileExists(path) == INCORRECT){
                throw new Exception("file is not a xml file");
            }

            InputStream inputStream = new FileInputStream(path);
            CTEEnigma rotors = deserializeFrom(inputStream);

            int amountOfRotors = rotors.getCTEMachine().getRotorsCount();
            int logical_rotors_size = amountOfRotors-1;
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
                throw  new Exception("some of the rotors are not unique");

            }
            reflectors = new Reflector[amountOfReflectors];
            SpinningRotor[] machineRotors =new SpinningRotor[amountOfRotors];

            abc = new ABC(rotors.getCTEMachine().getABC().trim().toCharArray());

            // ---------------------init rotors---------------------
            rotorsInitState = new String[amountOfRotors];

            for( int i = 0 ; i < amountOfRotors; i++ ) {

                CTERotor currPos = rotors.getCTEMachine().getCTERotors().getCTERotor().get(i);
                if(checkNotch(currPos.getNotch(),currPos.getCTEPositioning().size())==INCORRECT){
                    throw  new Exception("rotor number "+i+" is not having valid notch");
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
//                rotorsInitState[i]=String.valueOf(right[i].theLetter());
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
                        throw new Exception("in reflector "+i+" row number "+k+" is not valid, letter cant go to itself, dummy");
                    }
                    initRef[I] = U;
                    initRef[U] = I;
                }
                reflectors[i] = new Reflector(initRef,refs.get(i).getId());
            }
            if(checkReflectorID(reflectors,amountOfReflectors)==INCORRECT){
                throw new Exception("one of the Reflectors id is not valid");
            }
            //  -------------------------init PlugBoard---------------------
            int[] plugInit = new int[refAbcSize];
            for( int i=0 ; i < refAbcSize ; i++ ){

                plugInit[i]=i;
            }
            PlugBoard plugBoard = new PlugBoard(plugInit);

            myEnigma = new Machine(plugBoard,machineRotors,reflectors);
        }

        catch (Exception e) {
            e.printStackTrace();
            return  e.getMessage();
        }
        return "ok";
    }

    ///////////    validation check    ///////////
    private static final int NO_FILE = 0;
    private static final int CORRECT = 2;
    private static final int INCORRECT = 3;

    public boolean checkPlugSize(char[] plugsArr){

        return plugsArr.length % 2 == 0;
    }
    public boolean checkPlugs(char[] plugsArr){

        for (char c : plugsArr) {

            String plug = "" + c;
            if (abc.checkInAbc(plug).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    public int checkIfFileExists(String path) {
        File tempFile = new File(path);
        boolean isRightExtension = path.endsWith(".xml");
        boolean isExists = tempFile.exists();
        if(isRightExtension &&  isExists) return CORRECT;
        else if(!isExists) return NO_FILE;
        else return INCORRECT;
    }
    public boolean checkInRotorsRange(int size){

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

        int[] arr = new int[size];
        // init
        for ( int i = 0 ; i < size ; i++) {

            arr[i] = 0;
        }

        for(SpinningRotor rotor: all) {

            int id = rotor.getId();

            if( arr[id-1] == 0 )
                arr[id-1]=id;
            else
                return INCORRECT;
        }

        for ( int i = 0; i < size; i++) {

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

        for (int i = 1; i < size; i++) {

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

    //////////////////////////////////////////////////////////////////////////////////////
}
