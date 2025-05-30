package Inventory;

import model.HarvestRecord;
import java.util.ArrayList;
import java.util.List;

public class HarvestManager {
    private final List<HarvestRecord> harvestList = new ArrayList<>();
    private final List<HarvestRecord> harvestRecords = new ArrayList<>();

    // 새로운 수확 기록 추가
    public void addHarvest(HarvestRecord record) {
        harvestList.add(record);
    }

    // 특정 식물의 누적 수확량 계산
    public double getTotalHarvestQuantity(String plantName) {
        return harvestList.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName))
                .mapToDouble(HarvestRecord::getQuantity)
                .sum();
    }

    public double getTotalQuantityByPlant(String plantName) {
        return harvestRecords.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName))
                .mapToDouble(HarvestRecord::getQuantity)
                .sum();
    }

    // 식물 종류 수 계산 (중복 제거 후 count)
    public int getPlantTypeCount() {
        return (int) harvestRecords.stream()
                .map(HarvestRecord::getPlant)
                .distinct()
                .count();
    }

    // 특정 월의 수확량 계산
    public double getMonthlyHarvestQuantity(String plantName, String currentMonth) {
        return harvestRecords.stream()
                .filter(r -> r.getPlant().equalsIgnoreCase(plantName)
                        && r.getDate().startsWith(currentMonth))
                .mapToDouble(HarvestRecord::getQuantity)
                .sum();
    }

    // 전체 수확 목록 반환
    public List<HarvestRecord> getAllHarvests() {
        return new ArrayList<>(harvestList);
    }

    // 특정 식물의 수확 이력 반환
    public List<HarvestRecord> getHarvestsByPlant(String plantName) {
        List<HarvestRecord> result = new ArrayList<>();
        for (HarvestRecord record : harvestList) {
            if (record.getPlant().equalsIgnoreCase(plantName)) {
                result.add(record);
            }
        }
        return result;
    }

    // 전체 수확량 계산 (식물 상관없이)
    public double getTotalHarvestQuantityAllPlants() {
        return harvestList.stream()
                .mapToDouble(HarvestRecord::getQuantity)
                .sum();
    }
}


