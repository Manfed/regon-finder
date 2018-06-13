package regonfinder.location;

import lombok.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class Voivodeship implements Serializable {
    private String name;
    private int id;
    private List<County> counties;

    public Voivodeship(String name, int id, List<County> counties) {
        this.name = name;
        this.id = id;
        this.counties = counties;
    }

    public String getName() {
        return name;
    }

    public List<County> getCounties() {
        return counties;
    }

    public int getId() {
        return id;
    }

    public Optional<County> getCountyByName(@NonNull String countyName) {
        return counties.stream()
                .filter(county -> county.getName().equals(countyName))
                .findFirst();
    }
}
