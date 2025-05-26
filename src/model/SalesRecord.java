package model;

/**
 * 판매 기록 모델 클래스
 */
public class SalesRecord {
    private String date;
    private String plant;
    private double quantity;
    private double price;
    
    public SalesRecord(String date, String plant, double quantity, double price) {
        this.date = date;
        this.plant = plant;
        this.quantity = quantity;
        this.price = price;
    }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public String getPlant() { return plant; }
    public void setPlant(String plant) { this.plant = plant; }
    
    public double getQuantity() { return quantity; }
    public void setQuantity(double quantity) { this.quantity = quantity; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public double getTotal() { return price * quantity; }
}