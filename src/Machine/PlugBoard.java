package Machine;

import java.util.HashMap;

public class PlugBoard implements Rotor {

    HashMap<Integer,Integer> plugs;

    public PlugBoard(int[] abc) {

        plugs = new HashMap<>();

        for (int i: abc) { plugs.put(i, i); }
    }

    public HashMap<Integer, Integer> getPlugs() {
        return plugs;
    }

    public void PlugIn(int a, int b){

        plugs.put(a,b);
        plugs.put(b,a);
    }
    @Override
    public int decode(int in, boolean dir) {

        return plugs.get(in);
    }
}