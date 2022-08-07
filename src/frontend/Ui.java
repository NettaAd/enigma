package frontend;

import backend.BackEndMain;

import javax.xml.bind.JAXBException;
import java.util.Scanner;

public class Ui {

    Scanner userInput =new Scanner(System.in);
    BackEndMain backend = new BackEndMain();

    String[] menuOptions=new String[]{
            "load Machine settings via XML file",
            "show Machine settings",
            "set up machine settings",
            "gen machine settings",
            "encode",
            "reset settings",
            "stats",
            "exit"
    };

    public Ui() {
    }

    public int getUserMenuInput(){
        int number;
        while(true){
            try {

                int res =Integer.parseInt(userInput.next());
                if(res<0 || res>=menuOptions.length){
                    System.out.println("thats not on the menu");
                    continue;
                }else{
                    return res;
                }
            } catch(NumberFormatException ne) {
                System.out.print("That's not a whole number.\n");
            }
        }
    }

    public void runApp(){
        System.out.println("hey there!");
        System.out.println("----WELLCOME----");
        this.showMenu();
        int answer =this.getUserMenuInput();
        while(answer!=7){
            menuSwitch(answer);
            this.showMenu();
            answer =this.getUserMenuInput();
        }
        
    

    }
    
    
    public void inputXML(){
        System.out.println("please enter a pull path to your XML file");
        String path =userInput.nextLine()+userInput.nextLine();
//        --------- sends input to backend- if all good return empty string--------
//        --------- otherwise returns string with given error and ui address the issue to the user -------
        try {
            String res = backend.setXmlData(path);
            if(res=="ok"){
                System.out.println("you just done uploading the file!"+res);
            }else{
                System.out.println("ERROR: "+res);
            }
            int[] rotorsId = new int[]{1,3};
            String[] rotorPostions = new String[]{"D","C"};
            backend.setRotorsViaUser(rotorsId,rotorPostions);
            System.out.println("");
            return;
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }
//        return "that a bad file"


    }

    public void menuSwitch(int menuItem){
        switch(menuItem) {
            case 1:
                inputXML();
                break;
            case 5:
                String toDecode =userInput.nextLine()+userInput.nextLine();

            System.out.println(backend.DecodeString(toDecode.toUpperCase()));
            break;
            default:
                System.out.println("NOPE");
                break;
        }
        return;
    }



    public void showMenu(){
    for(int i=0;i<menuOptions.length;i++){
        System.out.println(i+1+": "+menuOptions[i]);
    }
}
}
