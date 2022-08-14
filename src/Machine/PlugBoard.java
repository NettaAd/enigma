package Machine;

import java.util.ArrayList;
import java.util.HashMap;

public class PlugBoard implements Rotor {

    HashMap<Integer,Integer> plugs;
    ArrayList<Integer> wantedPlugs;

    public PlugBoard(int[] abc) {

        wantedPlugs = new ArrayList<>();
        plugs = new HashMap<>();
        for (int i: abc) { plugs.put(i, i); }
    }

    public HashMap<Integer, Integer> getPlugs() {
        return plugs;
    }

    public void PlugIn(int a, int b){

        plugs.put(a,b);
        plugs.put(b,a);

        wantedPlugs.add(a);
        wantedPlugs.add(b);
    }

    public ArrayList<Integer> getWantedPlugs() {

        return this.wantedPlugs;
    }

    @Override
    public int decode(int in, boolean dir) {

        return plugs.get(in);
    }
}