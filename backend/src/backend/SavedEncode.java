package backend;

public class SavedEncode {

    private int time;
    private String inString, outString;
    private String machineSettings;

    ////////////////////////////////////////

    public SavedEncode(){
        time = 0;
    }

    ////////////////////////////////////////

    public String getInString() {
        return inString;
    }
    public String getOutString() {
        return outString;
    }
    public String getMachineSettings() {
        return machineSettings;
    }

    public int getTime() {
        return time;
    }

    ////////////////////////////////////////

    public void setInString(String in){

        this.inString = in;
    }
    public void setOutString(String out){

        this.outString = out;
    }
    public void setMachineSettings(String machineSettings) {

        this.machineSettings = machineSettings;
    }

    public void setTime(int time) {
        this.time = time;
    }

    ////////////////////////////////////////



}
