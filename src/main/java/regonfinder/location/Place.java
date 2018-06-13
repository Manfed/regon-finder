package regonfinder.location;

import java.io.Serializable;

public class Place implements Serializable {

    private String name;
    private String id;

    public Place(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
