package Machine;

public class ABC {
    String chars;

    public ABC(char[] chars) {

        this.chars =String.valueOf(chars);
    }

    public boolean checkifInAbc(String s){

        char[] charArr = s.toCharArray();
        for (int i=0;i<s.length();i++){

            if(chars.indexOf(charArr[i])==-1){
                return false;
            }
        }
        return true;
    }

    public char toLetter(int input){

        return chars.charAt(input);
    }

    public int toIndex(char input){

        return chars.indexOf(input);
    }
}
