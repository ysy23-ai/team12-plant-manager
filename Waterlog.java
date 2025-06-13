package model;

public class WaterLog {
    private String date;
    private String plantName;
    private double amount;

    public WaterLog(String date, String plantName, double amount) {
        this.date = date;
        this.plantName = plantName;
        this.amount = amount;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getPlantName() { return plantName; }
    public void setPlantName(String plantName) { this.plantName = plantName; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
