package controller;

import model.WaterLog;
import model.FertilizerLog;

import java.util.ArrayList;

public class PlantCareController {
    private ArrayList<WaterLog> waterLogs = new ArrayList<>();
    private ArrayList<FertilizerLog> fertilizerLogs = new ArrayList<>();

    public void addWaterLog(String date, String plantName, double amount) {
        WaterLog log = new WaterLog(date, plantName, amount);
        waterLogs.add(log);
        System.out.println("물주기 기록 추가: " + plantName + " - " + amount + "L");
    }

    public void addFertilizerLog(String date, String plantName, String type) {
        FertilizerLog log = new FertilizerLog(date, plantName, type);
        fertilizerLogs.add(log);
        System.out.println("비료 기록 추가: " + plantName + " - " + type);
    }

    public void showLogs() {
        System.out.println("\n=== 물주기 기록 ===");
        for (WaterLog log : waterLogs) {
            System.out.println(log.getDate() + " | " + log.getPlantName() + " | " + log.getAmount() + "L");
        }

        System.out.println("\n=== 비료 기록 ===");
        for (FertilizerLog log : fertilizerLogs) {
            System.out.println(log.getDate() + " | " + log.getPlantName() + " | " + log.getFertilizerType());
        }
    }
}
