package model;

/**
 * 수확 기록 모델 클래스
 */
public class HarvestRecord {
    private String date;
    private String plant;
    private double quantity;
    
    public HarvestRecord(String date, String plant, double quantity) {
        this.date = date;
        this.plant = plant;
        this.quantity = quantity;
    }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getPlant() { return plant; }
    public void setPlant(String plant) { this.plant = plant; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
}