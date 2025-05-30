package Inventory;

import model.SalesRecord;
import java.util.ArrayList;
import java.util.List;

public class SalesManager {
    private final List<SalesRecord> salesRecords = new ArrayList<>();

    public void addSale(SalesRecord record) {
        salesRecords.add(record);
    }

    public List<SalesRecord> getAll() {
        return new ArrayList<>(salesRecords);
    }

    public double getTotalQuantityByPlant(String plantName) {
        return salesRecords.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName))
                .mapToDouble(SalesRecord::getQuantity)
                .sum();
    }

    public double getTotalRevenueByPlant(String plantName) {
        return salesRecords.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName))
                .mapToDouble(SalesRecord::getTotal)
                .sum();
    }

    public double getMonthlyRevenue(String plantName, String currentMonth) {
        return salesRecords.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName) && r.getDate().startsWith(currentMonth))
                .mapToDouble(SalesRecord::getTotal)
                .sum();
    }
}


