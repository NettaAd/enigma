package Machine;

public class Reflector implements Rotor {
    private int[] wired;

    public Reflector(int[] wired) {
        this.wired = wired;
    }



    @Override
    public int decode(int in, boolean dir) {
        return wired[in];
    }
}
