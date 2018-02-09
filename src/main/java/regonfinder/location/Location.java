package regonfinder.location;

public class Location {

    private String voivodeshipName;
    private String countyName;
    private String communeName;
    private String placeName;

    public Location() {
    }

    public Location(String voivodeshipName, String countyName, String communeName, String placeName) {
        this.voivodeshipName = voivodeshipName;
        this.countyName = countyName;
        this.communeName = communeName;
        this.placeName = placeName;
    }

    public String getVoivodeshipName() {
        return voivodeshipName;
    }

    public void setVoivodeshipName(String voivodeshipName) {
        this.voivodeshipName = voivodeshipName;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getCommuneName() {
        return communeName;
    }

    public void setCommuneName(String communeName) {
        this.communeName = communeName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}
