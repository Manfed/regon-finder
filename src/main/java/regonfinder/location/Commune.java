package regonfinder.location;

import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Commune implements Serializable {

    private String name;
    private String id;
    private List<Place> places;

    public Commune(String name, String id, List<Place> places) {
        this.name = name;
        this.id = id;
        this.places = places;
    }

    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public String getId() {
        return id;
    }

    public Optional<Place> getPlaceByName(@NonNull String placeName) {
        return places.stream()
                .filter(place -> place.getName().equals(placeName))
                .findFirst();
    }
}
