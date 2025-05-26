package model;

/**
 * 식물 정보 모델 클래스
 */
public class PlantModel {
    private String id;
    private String name;
    private String category;
    private double price;
    private GrowthStage growthStage;
    private HealthStatus healthStatus;
    private String plantDate;
    private String expectedHarvestDate;
    private String location;
    private String notes;
    private String healthNotes;
    
    public enum GrowthStage {
        SEED("씨앗", 0),
        GERMINATION("발아", 20),
        GROWTH("성장", 40),
        FLOWERING("개화", 60),
        FRUITING("열매", 80),
        HARVEST("수확", 100);

        private final String label;
        private final int progressValue;

        GrowthStage(String label, int progressValue) {
            this.label = label;
            this.progressValue = progressValue;
        }

        public String getLabel() { return label; }
        public int getProgressValue() { return progressValue; }
    }

    public enum HealthStatus {
        HEALTHY("건강", java.awt.Color.GREEN),
        INFECTED("감염", java.awt.Color.RED),
        TREATED("치료중", java.awt.Color.ORANGE);

        private final String label;
        private final java.awt.Color color;

        HealthStatus(String label, java.awt.Color color) {
            this.label = label;
            this.color = color;
        }

        public String getLabel() { return label; }
        public java.awt.Color getColor() { return color; }
    }
    
    // 기본 생성자
    public PlantModel(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = 0.0;
        this.growthStage = GrowthStage.SEED;
        this.healthStatus = HealthStatus.HEALTHY;
        this.healthNotes = "";
    }
    
    // 가격 포함 생성자
    public PlantModel(String id, String name, String category, double price) {
        this(id, name, category);
        this.price = price;
    }
    
    // 전체 정보 생성자
    public PlantModel(String id, String name, String category, double price, 
                     GrowthStage growthStage, HealthStatus healthStatus,
                     String plantDate, String expectedHarvestDate) {
        this(id, name, category, price);
        this.growthStage = growthStage;
        this.healthStatus = healthStatus;
        this.plantDate = plantDate;
        this.expectedHarvestDate = expectedHarvestDate;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    public GrowthStage getGrowthStage() { return growthStage; }
    public void setGrowthStage(GrowthStage growthStage) { this.growthStage = growthStage; }
    
    public HealthStatus getHealthStatus() { return healthStatus; }
    public void setHealthStatus(HealthStatus healthStatus) { this.healthStatus = healthStatus; }
    
    public String getPlantDate() { return plantDate; }
    public void setPlantDate(String plantDate) { this.plantDate = plantDate; }
    
    public String getExpectedHarvestDate() { return expectedHarvestDate; }
    public void setExpectedHarvestDate(String expectedHarvestDate) { this.expectedHarvestDate = expectedHarvestDate; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getHealthNotes() { return healthNotes; }
    public void setHealthNotes(String healthNotes) { this.healthNotes = healthNotes; }
    
    public int getGrowthPercentage() {
        return (growthStage != null) ? growthStage.getProgressValue() : 0;
    }
    
    public double getStockValue(double quantity) {
        return price * quantity;
    }
    
    @Override
    public String toString() {
        return id + ": " + name + " (" + category + ")";
    }
}