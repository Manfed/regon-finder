package regonfinder.location;

public class Location {

    private String voivodeshipId;
    private String countyId;
    private String communeId;
    private String placeId;
    private String placeName;

    public Location() {
    }

    public Location(String voivodeshipId, String countyId, String communeId, String placeId, String placeName) {
        this.voivodeshipId = voivodeshipId;
        this.countyId = countyId;
        this.communeId = communeId;
        this.placeId = placeId;
        this.placeName = placeName;
    }

    public String getVoivodeshipId() {
        return voivodeshipId;
    }

    public void setVoivodeshipId(String voivodeshipId) {
        this.voivodeshipId = voivodeshipId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getCommuneId() {
        return communeId;
    }

    public void setCommuneId(String communeId) {
        this.communeId = communeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
