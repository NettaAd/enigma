package common_models;

public class activeRotor {
    private String id;
    private String order;
    private String position;

    public activeRotor(String id,String order,String position) {
        this.id = id;
        this.order = order;
        this.position = position;

    }

    public String getId() {
        return id;
    }


    public String getOrder() {
        return order;
    }

    public String getPosition() {
        return position;
    }
}
