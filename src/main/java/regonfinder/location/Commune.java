package regonfinder.location;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class Commune {

    private String name;
    private List<Place> places;

    public Commune(String name, List<Place> places) {
        this.name = name;
        this.places = places;
    }

    public String getName() {
        return name;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public Optional<Place> getPlaceByName(@NonNull String placeName) {
        return places.stream()
                .filter(place -> place.getName().equals(placeName))
                .findFirst();
    }
}
