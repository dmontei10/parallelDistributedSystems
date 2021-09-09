package app;

public class Data {
    private int id;
    private String name;
    private String description;
    private int quantity;
    private int local_id;
    private int item_id;

    public Data(int id, String name, String description, int quantity) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
    }

    public Data(int id) {
        super();
        this.id = id;
    }

    public Data(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Data(int id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Data(int id, int quantity) {
        super();
        this.id = id;
        this.quantity = quantity;
    }

    public Data(int id, String name, int quantity) {
        super();
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Data(int id, int quantity, int local_id, int item_id) {
        super();
        this.id = id;
        this.quantity = quantity;
        this.local_id = local_id;
        this.item_id = item_id;
    }

    public int getId() {
        return id;
    }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getQuantity() {
        return quantity;
    }

    public int getLocal_id() {
        return local_id;
    }

    public int getItem_id() {
        return item_id;
    }
}
