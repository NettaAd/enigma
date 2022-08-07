package Machine;

public class Reflector implements Rotor {
    private final int[] ref;
    private String id;
    public Reflector(int[] ref,String id) {
        this.ref = ref;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    @Override
    public int decode(int in, boolean dir) {
        return ref[in];
    }
}
