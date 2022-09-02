package common_models;

public class activeRotor {
    private String id;
    int  order;
    private String position;

    public activeRotor(String id, int order, String position) {
        this.id = id;
        this.order = order;
        this.position = position;

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
}
