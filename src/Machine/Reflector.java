package Machine;

public class Reflector implements Rotor {
    private final int[] ref;
    public Reflector(int[] ref) {
        this.ref = ref;
    }
    @Override
    public int decode(int in, boolean dir) {
        return ref[in];
    }
}
