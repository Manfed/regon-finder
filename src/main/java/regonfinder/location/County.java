package regonfinder.location;

import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class County implements Serializable {

    private String name;
    private String id;
    private List<Commune> communes;

    public County(String name, String id, List<Commune> communes) {
        this.name = name;
        this.id = id;
        this.communes = communes;
    }

    public String getName() {
        return name;
    }

    public List<Commune> getCommunes() {
        return communes;
    }

    public String getId() {
        return id;
    }

    public Optional<Commune> getCommuneByName(@NonNull String communeName) {
        return communes.stream()
                .filter(commune -> commune.getName().equals(communeName))
                .findFirst();
    }
}
