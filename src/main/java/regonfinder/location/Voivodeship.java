package regonfinder.location;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public class Voivodeship {
    private String name;
    private List<County> counties;

    public Voivodeship(String name, List<County> counties) {
        this.name = name;
        this.counties = counties;
    }

    public String getName() {
        return name;
    }

    public List<County> getCounties() {
        return counties;
    }

    public Optional<County> getCountyByName(@NonNull String countyName) {
        return counties.stream()
                .filter(county -> county.getName().equals(countyName))
                .findFirst();
    }
}
