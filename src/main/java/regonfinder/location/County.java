package regonfinder.location;

import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class County implements Serializable {

    private String name;
    private List<Commune> communes;

    public County(String name, List<Commune> communes) {
        this.name = name;
        this.communes = communes;
    }

    public String getName() {
        return name;
    }

    public List<Commune> getCommunes() {
        return communes;
    }

    public Optional<Commune> getCommuneByName(@NonNull String communeName) {
        return communes.stream()
                .filter(commune -> commune.getName().equals(communeName))
                .findFirst();
    }
}
