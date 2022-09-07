package enigma.ui;

import Machine.SpinningRotor;
import backend.BackEndMain;
import backend.SavedEncode;
import java.util.ArrayList;
import java.util.Scanner;

public class Ui {

    long start, end;
    boolean firstSet;
    Scanner userInput = new Scanner(System.in);
    BackEndMain backend = new BackEndMain();
    String[] menuOptions = new String[] {   "Load Machine settings via XML file"
            , "Show Machine settings"
            , "Set up machine settings"
            , "Random machine settings"
            , "Encode"
            , "Reset settings"
            , "Stats and history "
            , "TEST47"
         };

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public Ui() {

        firstSet = false;
        start = end = 0;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public void showMenu() {

        for( int i = 0 ; i < menuOptions.length ; i++) {

            System.out.println(i + 1 + ": " + menuOptions[i]);
        }
    }
    public void runApp() throws Exception {

        int exit = 8;
        System.out.println("Hey there!");
        System.out.println("---- WELCOME ----");

        this.showMenu();
        int answer = this.getUserMenuInput();

        while (answer != 1){

            System.out.println("Opps! you have to set a xml file first!");
            System.out.println("Let's try again: ");

            this.showMenu();
            answer = this.getUserMenuInput();
        }

        while(true) {

            menuSwitch(answer);
            if(answer == exit){
                break;
            }
            else {
                this.showMenu();
                answer = this.getUserMenuInput();
            }
        }
    }
    public void inputXML() {

        System.out.println("Please enter a pull path to your XML file: ");
        String path = userInput.nextLine() + userInput.nextLine();
        //  sends input to backend- if all good return empty string
        //  otherwise returns string with given error and ui address the issue to the user
        try {

            String res = backend.setXmlData(path);
            if (res.equals("ok")) {

                System.out.println("you just done uploading the file! " + res);
                backend.resetHistory();
            }
            else {

                System.out.println("ERROR: " + res);
            }
        }

        catch (Exception e) {

            e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
        }

        System.out.println();
    }

    public int getUserIntInputSafe() {

        while ( true ) {

            try {

                return Integer.parseInt(userInput.next());
            }
            catch(NumberFormatException ne) {

                System.out.print("That's not a whole number.\n");
            }
        }
    }

    public boolean checkIfNumber(String k) {
        try {

            Integer.parseInt(k);
            return true;
        }
        catch(NumberFormatException ne) {

            return false;
        }

    }
    public int getUserMenuInput() {

        while ( true ) {

            try {

                int res = Integer.parseInt(userInput.next());

                if ( res <= 0 || res > menuOptions.length ) {

                    System.out.println("that's not on the menu");
                }
                else {
                    return res;
                }
            }
            catch(Exception ne) {

                System.out.print("That's not a whole number.\n");
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    public void menuSwitch(int menuItem) throws Exception {

        switch (menuItem) {

            case 1: inputXML();      break;                           // Load Machine settings via XML file

            case 2: ShowMachineSettings();    break;                  // Show Machine settings

            case 3: getMachineSettings();    break;                    // Set up machine settings

//            case 4: randomMachine();      break;               // Random machine settings

            case 5: decode();    break;                                // Decode

            case 6: reset(); break;                  // Reset settings

            case 7: showHistory(); break;            // stats

            case 8: System.out.println("<<<<<<<<  GOODBYE! :D  >>>>>>>>"); break; // Exit

            case 4: backend.testAgent();
            default: System.out.println("Please enter valid choice: "); break;
        }
    }

    /////////////////////////////------------ 2 ------------/////////////////////////////

    public void ShowMachineSettings() {

        if(backend.getAmountOfActiveRotors()==0){
            System.out.println("no settings were set, please choose command 3 or 4 before trying to decode");
            return;
        }
        System.out.println();

        int activeRotorsNum = backend.getAmountOfActiveRotors();
        int rotorsNum = backend.getAmountOfRotors();
     System.out.println("The number of active rotors: " + activeRotorsNum + "/" + rotorsNum);
        for(SpinningRotor r: backend.getRotorsArr()){

            System.out.println("The notch of Rotor " + r.getId() + " is: "+ (r.getNotch() + 1));
        }
        System.out.println("The number of reflectors is: " + backend.getReflectors().length);
        System.out.println("The number of encoded messages are: " + backend.getMessagesCount());

        System.out.println("The first machine settings are:   " + backend.getFirstSettings());
        System.out.println("The current machine settings are: " + backend.getFormatStats());
        System.out.println();
    }

    /////////////////////////////------------ 3 ------------/////////////////////////////

   /* public int getActiveRotorsNum() {

        int rotorsNum = backend.getAmountOfRotors();
        System.out.println("There are " + rotorsNum + " rotors available.");

        System.out.println("How many active rotors would you like to set? Remember that there have to be at least 2 :)");
        int activeRotorAmount = this.getUserIntInputSafe() - 1;

        // valid check
        while( !backend.checkInRotorsRange(activeRotorAmount) ){

            System.out.println("You only got " + rotorsNum + " rotors!");
            System.out.println("Please try again");
            activeRotorAmount = this.getUserIntInputSafe() - 1;
        }

        System.out.println();
        return activeRotorAmount;
    }
    public int[] getRotorsID(int activeRotorAmount, int[] activeRotorsId ) {

        ArrayList<Integer> seenRotorsId = new ArrayList<>();
        System.out.println("Please enter your choice of active rotors id's by the chosen order: ");

        for(int i = 0 ; i <= activeRotorAmount ; i++)   {

            System.out.println("Choose id for the " + (i+1) + "'s rotor: " );
            activeRotorsId[i] = this.getUserIntInputSafe();

            while( !backend.isRotorExist(activeRotorsId[i]) || seenRotorsId.contains(activeRotorsId[i]) ) {

                if(seenRotorsId.contains(activeRotorsId[i])){
                    System.out.println("The id: " + activeRotorsId[i] + " is already picked, try again: " );
                }
                else {
                    System.out.println("The id: " + activeRotorsId[i] + " is not a valid, try again: ");
                }

                activeRotorsId[i] = this.getUserIntInputSafe();
            }
            seenRotorsId.add( activeRotorsId[i]);
        }

        System.out.println();
        return activeRotorsId;
    }
    public String[] getRotorsPos(int activeRotorAmount,  int[] activeRotorsId , String[] activeRotorsPos){

        System.out.println("Please pick the pos for each rotor in the same order as the id's: ");
        //   WORKAROUND-
        //   weird nextLine bug -> https://stackoverflow.com/questions/24470587/java-using-nextline-in-for-loop
        userInput.nextLine();

        System.out.println();
        // todo -already done: changed activeRotorsId[i], used to be just i

        for( int i = 0 ; i <= activeRotorAmount ; i++) {

            System.out.println( "Choose pos for the " + activeRotorsId[i] + "'s rotor:" );
            activeRotorsPos[i] = userInput.nextLine().toUpperCase();

            while (!backend.getAbc().checkInAbc(activeRotorsPos[i]).isEmpty() || activeRotorsPos[i].equals("")){

                System.out.println("Not a legit letter!!! Please try again" );
                activeRotorsPos[i] = userInput.nextLine();
            }
        }
        System.out.println();

        return activeRotorsPos;
    }
   */
    public void getRotors() {

        // get rotors like 23,45,16 (one string, with ,) ---------------------------------------
        int rotorsNum = backend.getAmountOfRotors();
        System.out.println("Please enter the id of the rotors of your choice, separated by ',' (For example: 23,526,231). " );
        System.out.println("There are " + rotorsNum + " rotors available.");

        String rotorsIdString = userInput.nextLine();
        String[] rotorsId = rotorsIdString.split(",");
        while(true){
            try{
                for(String i : rotorsId){
                    if(!checkIfNumber(i)){
                        throw new Exception("shit");
                    }
                }
                break;
            }
            catch(Exception e){
                System.out.println("input were not numbers, try typing numbers: ");
                rotorsIdString = userInput.nextLine();
                System.out.println("");
                rotorsId = rotorsIdString.split(",");
                continue;
            }
        }

        while (!checkID(rotorsId)) {
            rotorsIdString = userInput.nextLine();
            rotorsId = rotorsIdString.split(",");
        }
        String[] tmp = new String[rotorsId.length];
        int count=0;
        for (int i = rotorsId.length-1; i >= 0 ; i--){
            tmp[count]=rotorsId[i];
            count+=1;
        }
        rotorsId=tmp;

        int activeRotorsAmount = rotorsId.length;
        int[] activeRotorsId =  new int[activeRotorsAmount];
        for(int i = 0 ; i < activeRotorsAmount ; i++){

            activeRotorsId[i] = Integer.parseInt(rotorsId[i]);
        }

        // get pos (right arr of the rotor) like AC4B (one string, without any spaces) ----------------------
        System.out.println("Please enter the init the rotors positions (characters in ABC).");
        System.out.println("They must be entered without spaces (for example: AB3D).");

        String rotorsPosString = userInput.nextLine();
        char[] rotorsPos = rotorsPosString.toUpperCase().toCharArray();

        while(!checkPos(rotorsPos)){

            rotorsPosString = userInput.nextLine();
            rotorsPos = rotorsPosString.toCharArray();
        }

        char[] tmpPos = new char[rotorsPos.length];
        count=0;
        for (int i = rotorsPos.length-1; i >= 0 ; i--){
            tmpPos[count]=rotorsPos[i];
            count+=1;
        }
        rotorsPos=tmpPos;

        backend.setRotorsViaUser(activeRotorsId, rotorsPos);

        // print to user  -------------------------------------------------------------------
        System.out.println("-----------------------------------------");
        System.out.println("Your active rotors are:");
        for( int i = 0 ; i < backend.getAmountOfActiveRotors() ; i++) {

            System.out.println((i+1) + ": id = " + activeRotorsId[i] + ", set pos = " + rotorsPos[i] );
        }
        System.out.println("-----------------------------------------");
        /*

        ~~~ the old code ~~~
        // get rotors amount
        int size = getActiveRotorsNum();

        // get id's
        int[] activeId =  new int[size + 1];
        activeId = getRotorsID(size, activeId);

        //  TODO--- tests input AND ERROR HANDLING

        // get pos
        String[] activePos = new String[size + 1];
        activePos = getRotorsPos(size, activeId, activePos);

        //  backend.setRotorsViaUser(activeId, activePos);

        System.out.println("Your active rotors are:");
        for( int i = 0 ; i <= size ; i++) {

            System.out.println((i+1) + ": id = " + activeId[i] + ", set pos = " + activePos[i] );
        }
        System.out.println();*/
    }
    //~~~~~~~~~~~~~~~~~~~
    public void getReflector() {

        System.out.println("The available reflectors are: ");
        for(int i = 0 ; i < backend.getReflectors().length ; i++) {

            System.out.println(( i + 1 ) + ": " + backend.getReflectors()[i].getId());
        }

        System.out.println("Pick reflector via ID: ");
        int refId = this.getUserIntInputSafe() - 1;

        backend.setReflectorViaUser(backend.getReflectors()[refId].getId());
        System.out.println("Your active reflector is: " + backend.getReflectors()[refId].getId());
        System.out.println();
    }
    //~~~~~~~~~~~~~~~~~~~
    public void getPlugs() {

        System.out.println("Please enter the wanted plugs in one line, without any spaces between to plugs: ");
        userInput.nextLine();
        String wantedPlugs =  userInput.nextLine();
        char[] plugsArr = wantedPlugs.toUpperCase().toCharArray();



        while (!backend.checkPlugs(plugsArr)){

            System.out.println("The chosen plugs are not in the ABC! Please enter valid plugs:");
            wantedPlugs = userInput.nextLine();
            plugsArr = wantedPlugs.toCharArray();
        }

        while(!backend.checkPlugSize(plugsArr)) {

            System.out.println("Invalid amount of plug! Please enter even number of plugs:");
            wantedPlugs = userInput.nextLine();
            plugsArr = wantedPlugs.toCharArray();
        }

        backend.setPlugBoardViaUser(plugsArr);

        System.out.println("The plugs are: ");
        for(int i = 0; i < plugsArr.length ; i+=2){

            System.out.println(plugsArr[i] + " | " + plugsArr[i + 1]);
        }
    }
    //~~~~~~~~~~~~~~~~~~~

    public boolean checkID(String[] rotorsId) {

        int rotorsNum = backend.getAmountOfRotors();
        int activeRotorAmount = rotorsId.length;
        ArrayList<Integer> seenRotorsId = new ArrayList<>();

        // check if the active rotors amount is valid
        if (!backend.checkInRotorsRange(activeRotorAmount)) {

            System.out.println("You only got " + rotorsNum + " rotors!");
            System.out.println("Please try again");
            return false;
        }
        if(activeRotorAmount < 2) {

            System.out.println("There have to be at least 2 rotors! ");
            System.out.println("Please try again");
            return false;
        }

        for (String s : rotorsId) {
            try{
                int id = Integer.parseInt(s);
                if (seenRotorsId.contains(id)) {

                    System.out.println("The id: " + id + " is already picked, try again: ");
                    return false;
                }

                if (!backend.isRotorExist(id)) {

                    System.out.println("The id: " + id + " is not a valid, try again: ");
                    return false;
                }
                seenRotorsId.add(id);
            }catch (NumberFormatException e){
                System.out.println("NOT A NUMBER,TRY AGAIN..");
                return false;
            }catch (Exception e){
                System.out.println("something went terribly wrong..");
                return false;
            }

        }
        return true;
    }
    public boolean checkPos(char[] rotorsPos) {

        for(int i = 0; i< rotorsPos.length ; i++){

            String c = rotorsPos[i] + "";

            if(!backend.getAbc().checkInAbc(c).isEmpty()) {

                System.out.println(c + " is not in the ABC! ");
                System.out.println("Please try again");

                return false; // todo say each chars are invalid
            }
        }
        return true;
    }

    public void getMachineSettings() {

        getRotors();
        //   C:\Users\yigal\IdeaProjects\blabla\enigma\src\ex1-sanity-small.xml

        //getRotors();
        //   TODO-->TESTS AND ERROR HANDLING
        getReflector();
        getPlugs();

        System.out.println();
    }

    /////////////////////////////------------ 4 ------------/////////////////////////////

    public void printRandomMachine(){

        System.out.println("Your active rotors are:");
        ArrayList<SpinningRotor> s = backend.getRotorsArr();
        int ind = 1;
        for( SpinningRotor rotor: s) {

            char setLetter = backend.getAbc().toLetter(rotor.getPos());
            System.out.println(ind + ": id = " + rotor.getId() + ", set pos = " + setLetter);
            ind++;
        }

        //////

        System.out.println("Your active reflector is: " + backend.getReflectorId());

        //////
        ArrayList<Integer> plugsArr = backend.getWantedPlugs();

        if(plugsArr.size()  == 0){
            System.out.println("No plugs");
        }
        else {
            System.out.println("The plugs are: ");

            for(int i = 0; i < plugsArr.size() ; i+=2){

                int a = plugsArr.get(i);
                int b = plugsArr.get(i + 1);
                System.out.println(backend.getAbc().toLetter(a) + " | " + backend.getAbc().toLetter(b));
            }
        }
        System.out.println();
    }
    public void randomMachine(){

        backend.RandomMachine();

        if(!firstSet) {

            firstSet = true;
            backend.setFirstMachine();
        }
        printRandomMachine();
    }

    /////////////////////////////------------ 5 ------------/////////////////////////////

    public void decode(){

        String toDecode = userInput.nextLine() + userInput.nextLine();
        toDecode = toDecode.toUpperCase();
        try {
            System.out.println("you asked to decode: " + toDecode);

            start = System.nanoTime();
            String res = backend.DecodeString(toDecode,true);
            end = System.nanoTime();
            System.out.println("secret code: " + res);
        }catch (Exception e){
            System.out.println("ERROR"+e);
        }
        System.out.println();
    }

    /////////////////////////////------------ 6 ------------/////////////////////////////

    public void reset(){
        backend.resetMachine();
//        backend.resetMachine();
//        System.out.println("Reset has been done! the current setting are:");
//        ShowMachineSettings();
//        System.out.println("Reset has been done! the current setting are:");
    }

    /////////////////////////////------------ 7 ------------/////////////////////////////
    public void showHistory() {

        ArrayList<SavedEncode> messages = backend.getMessages();

        if(messages.size() == 0){
            System.out.println("No history");
        }
        else{

            for (String set : backend.getMachineSettings()) {

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
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////

}
