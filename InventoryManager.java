package Inventory;

import Inventory.HarvestManager;

public class InventoryManager {
    private final HarvestManager harvestManager;
    private final SalesManager salesManager;

    public InventoryManager(HarvestManager harvestManager, SalesManager salesManager) {
        this.harvestManager = harvestManager;
        this.salesManager = salesManager;
    }

    public double getAvailableStock(String plantName) {
        double harvested = harvestManager.getTotalQuantityByPlant(plantName);
        double sold = salesManager.getTotalQuantityByPlant(plantName);
        return harvested - sold;
    }

    public int getPlantTypeCount() {
        return harvestManager.getPlantTypeCount(); // 식물 종류는 수확 기준
    }

    public double getTotalHarvest(String plant) {
        return harvestManager.getTotalQuantityByPlant(plant);
    }

    public double getTotalSales(String plant) {
        return salesManager.getTotalQuantityByPlant(plant);
    }

    public double getTotalRevenue(String plant) {
        return salesManager.getTotalRevenueByPlant(plant);
    }

    public double getMonthlyHarvest(String plant, String currentMonth) {
        return harvestManager.getMonthlyHarvestQuantity(plant, currentMonth);
    }

    public double getMonthlySalesRevenue(String plant, String currentMonth) {
        return salesManager.getMonthlyRevenue(plant, currentMonth);
    }
}

