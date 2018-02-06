package regonfinder.location;

import java.io.Serializable;

public class Place implements Serializable {

    private String name;

    public Place(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
