package frontend;
import java.util.ArrayList;
import java.util.Scanner;

import Machine.Machine;
import Machine.SpinningRotor;
import backend.BackEndMain;


public class Ui {
    long start, end;
    Scanner userInput = new Scanner(System.in);
    BackEndMain backend = new BackEndMain();
    String[] menuOptions = new String[] {   "Load Machine settings via XML file"
            , "Show Machine settings"
            , "Set up machine settings"
            , "Gen machine settings"
            , "Encode"
            , "Reset settings"
            , "Stats"
            , "Exit" };

    public Ui() {
        start = end = 0;
    }


    //////////////////////////////////////////////////////////

    public void showMenu() {

        for( int i = 0 ; i < menuOptions.length ; i++) {

            System.out.println(i + 1 + ": " + menuOptions[i]);
        }
    }
    public void menuSwitch(int menuItem) {

        switch(menuItem) {

            case 1: // Load Machine settings via XML file
                inputXML();
                break;

            case 2: // Show Machine settings              // TODO: current settings - TOMER MAKE PRETTY
                ShowMachineSettings(backend.getMachine());
                break;

            case 3: // Set up machine settings             // TODO: all - TOMER- almost REFACTORING NEEDED
                getMachineSettings();
                break;

            case 4: // Random machine settings             // TODO: all - NETTA
                backend.RandomMachine();
                break;

            case 5: // Encode
                // TODO: done :D
                String toDecode = userInput.nextLine() + userInput.nextLine();
                toDecode=toDecode.toUpperCase();
                System.out.println("you asked to decode: "+toDecode);
                start = System.nanoTime();
                String res = backend.DecodeString(toDecode);
                end = System.nanoTime();
                System.out.println("secret code: "+res);
                break;

            case 6: // Reset settings                     // TODO: all ??????
                break;

            case 7: // stats                             // TODO - NETTA
                backend.showHistory(start, end);
                break;

            case 8: // Exit                              // TODO: done :D
                System.out.println("GOODBYE! ");
                return;

            default:
                System.out.println("NOPE");
                break;

        }

        return;
    }

    //////////////////////////////////////////////////////////

    // 2 IN MENU
    public void ShowMachineSettings(Machine m) {

//        m.getPlugBoard().PlugIn(1,2);
        System.out.println();
        System.out.println("The number of active rotors: " + m.getNumberOfActiveRotors() + "/" + m.getNumberOfRotors());
        for(SpinningRotor r: m.getActiveRotors()){

            System.out.println("The notch of Rotor "+ r.getId() + " is: "+ (r.getNotch() + 1));
        }

        System.out.println("The number of reflectors is: " + m.getReflectorsSize());
        System.out.println("The number of encoded messages are: " + backend.getMessagesCount());

        System.out.println("The current machine settings are: ");
        System.out.println(backend.getFormatedStats());
        System.out.println();

    }

    //3 IN MENU
    public void getMachineSettings() {

//        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("how many active rotors would you like to set?");
        int activeRotorAmount = this.getUserIntInputSafe()-1;
        while(!backend.inRotorsRange(activeRotorAmount)){
            System.out.println("you only got "+backend.getAmountOfRotors()+" rotors..");
            System.out.println("try again");
            activeRotorAmount = this.getUserIntInputSafe()-1;
        }

        int[] activeRotorsId= new int[activeRotorAmount+1];
        ArrayList<Integer> seenRotorsId= new ArrayList<Integer>();
        String[] activeRotorsPos= new String[activeRotorAmount+1];

        System.out.println("Please enter your choice of active rotors by the chosen order: ");
        for(int i=0;i<=activeRotorAmount;i++){
            System.out.println("choose id for the "+i+"'s rotor" );
            activeRotorsId[i]=this.getUserIntInputSafe();
            while(!backend.isRotorExist(activeRotorsId[i]) || seenRotorsId.contains(activeRotorsId[i])){
                System.out.println(activeRotorsId[i]+" is not a real rotor,or its already picked by you, try again: " );
                activeRotorsId[i]=this.getUserIntInputSafe();
            }
            seenRotorsId.add( activeRotorsId[i]);

        }

        //            TODO--- tests input AND ERROR HANDLING

        System.out.println("in the same order please pick the pos for every one:");
//        WORKAROUND- weird nextLine bug -> https://stackoverflow.com/questions/24470587/java-using-nextline-in-for-loop
        userInput.nextLine();
        for(int i=0;i<=activeRotorAmount;i++){
            System.out.println("choose pos for the "+i+"'s rotor" );
            activeRotorsPos[i]=userInput.nextLine().toUpperCase();
            while (!backend.getAbc().checkInAbc(activeRotorsPos[i]) || activeRotorsPos[i].equals("")){
                System.out.println("not a legit letter, try again" );
                activeRotorsPos[i]=userInput.nextLine();
            }
        }
        backend.setRotorsViaUser(activeRotorsId,activeRotorsPos);
        for(int i=0;i<=activeRotorAmount;i++){
            System.out.println(i+": "+activeRotorsPos[i] );

        }
//        TODO-->TESTS AND ERROR HANDLING
        System.out.println("aviable reflactors: ");
        for(int i=0;i<backend.getReflectors().length;i++){
            System.out.println(i+": "+backend.getReflectors()[i].getId());
        }
        System.out.println("pick Reflactor via ID: ");
        int refId = this.getUserIntInputSafe();
//TODO---fix this shitty code later, your past and completely fucked self..
        backend.setReflectorViaUser(backend.getReflectors()[refId].getId());

        System.out.println("how many plugs you wanna connect?");
        int plugsAmount=userInput.nextInt();
        char[] Plugs= new char[plugsAmount*2];
        userInput.nextLine();
        for(int i=0;i<plugsAmount*2;i+=2){
            System.out.println("pick 2 letter for the "+i+"'s"+" plug.");
            System.out.println("from: ");
            Plugs[i]=userInput.next().charAt(0);

            System.out.println("To: ");

            Plugs[i+1]= userInput.next().charAt(0);


        }
        for(int i=0;i<plugsAmount*2;i+=2){
            System.out.println(i+": "+Plugs[i]+"-"+ Plugs[i+1]);
            backend.setPlugBoardViaUser(Plugs);

        }








        System.out.println("");
    }


    //////////////////////////////////////////////////////////

    public void runApp() {

        System.out.println("Hey there!");
        System.out.println("---- WELCOME ----");

        this.showMenu();
        int answer = this.getUserMenuInput();

        while( answer != 8 ) {

            menuSwitch(answer);
            this.showMenu();
            answer =this.getUserMenuInput();
        }
    }
    public void inputXML() {

        System.out.println("Please enter a pull path to your XML file: ");
        String path = userInput.nextLine() + userInput.nextLine();

        //  sends input to backend- if all good return empty string
        //  otherwise returns string with given error and ui address the issue to the user
        try {

            String res = backend.setXmlData(path);
            if ( res == "ok") {

                System.out.println("you just done uploading the file! " + res);
            }
            else {

                System.out.println("ERROR: " + res);
            }



//            int[] rotorsId = new int[]{1,2};
//            String[] rotorPositions = new String[]{"C","C"};
//
//            backend.setRotorsViaUser(rotorsId, rotorPositions);
//
//            char[] c = {'A','F'};
//            backend.setPlugBoardViaUser(c);
//
//            backend.setReflectorViaUser("I");
//            System.out.println("");


        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    public int getUserIntInputSafe() {

        int number;
        while ( true ) {

            try {

                int res = Integer.parseInt(userInput.next());
                    return res;
            }
            catch(NumberFormatException ne) {

                System.out.print("That's not a whole number.\n");
            }
        }
    }

    public int getUserMenuInput() {

        int number;
        while ( true ) {

            try {

                int res = Integer.parseInt(userInput.next());

                if ( res < 0 || res >= menuOptions.length ) {

                    System.out.println("that's not on the menu");
                    continue;
                }
                else {
                    return res;
                }
            }
            catch(NumberFormatException ne) {

                System.out.print("That's not a whole number.\n");
            }
        }
    }
}
