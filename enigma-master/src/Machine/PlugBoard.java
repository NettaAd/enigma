package Machine;

import java.util.HashMap;

public class PlugBoard implements Rotor {
    HashMap<Integer,Integer> plugs= new HashMap();

    public PlugBoard(int[] abc) {
        for (int i: abc) {
            plugs.put(i, i);
        }
    }

    public void PlugIn(int a, int b){
        plugs.put(a,b);
        plugs.put(b,a);
    }

    public int GetThorughBoard(int a){
        return plugs.get(a);
    }



    @Override
    public int decode(int in, boolean dir) {
        return plugs.get(in);
    }
}
