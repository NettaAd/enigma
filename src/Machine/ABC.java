package Machine;

public class ABC {
    String chars;

    public ABC(char[] chars) {

        this.chars =String.valueOf(chars);
    }

    public char toLetter(int input){

        return chars.charAt(input);
    }

    public int toIndex(char input){

        return chars.indexOf(input);
    }
}
