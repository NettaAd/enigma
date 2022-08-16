package Machine;

import java.util.ArrayList;

public class ABC {
    String chars;

    public ABC(char[] chars) {

        this.chars = String.valueOf(chars);
    }

    public int getSize(){

        return chars.length();
    }
    public ArrayList<Character> checkInAbc(String s){
        ArrayList<Character> badLetters = new ArrayList<>();
        char[] charArr = s.toCharArray();

        for (int i=0;i<s.length();i++) {

            if(chars.indexOf(charArr[i]) == -1 && badLetters.indexOf(charArr[i])==-1 ) {
                badLetters.add(charArr[i]);
            }
        }
        return badLetters;
    }

    public char toLetter(int input){

        return chars.charAt(input);
    }

    public int toIndex(char input){

        return chars.indexOf(input);
    }
}
