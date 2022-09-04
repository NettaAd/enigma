package common_models;

public class activeRotor {
    private String id;
    int  order;
    private String position;

    private int notch;

    public activeRotor(String id, int order, String position,int notch) {
        this.id = id;
        this.order = order;
        this.position = position;
        this.notch=notch;
    }

    public String getId() {
        return id;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getOrder() {
        return order;
    }

    public String getPosition() {
        return position;
    }
    public void setId(String i) {
        id=i;
    }
    public void setOrder(int i) {
        order=i;
    }

    public int getNotch() {

        return notch;
    }
}
