package model;

public class FertilizerLog {
    private String date;
    private String plantName;
    private String fertilizerType;

    public FertilizerLog(String date, String plantName, String fertilizerType) {
        this.date = date;
        this.plantName = plantName;
        this.fertilizerType = fertilizerType;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public String getFertilizerType() { return fertilizerType; }
    public void setFertilizerType(String fertilizerType) { this.fertilizerType = fertilizerType; }
}
