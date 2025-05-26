package controller;

import view.PlantMonitoringView;
import model.PlantModel;
import util.UIUtils;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 식물 모니터링 컨트롤러 - 식물 상태 관리 로직 담당
 * View는 UI만 담당하고, 이 클래스가 모든 비즈니스 로직을 처리
 */
public class PlantMonitoringController {
    private PlantMonitoringView view;
    private Map<String, PlantModel> plantsData;
    private String currentSelectedPlant = null;
    
    public PlantMonitoringController(PlantMonitoringView view) {
        this.view = view;
        this.plantsData = new HashMap<>();
        
        // 테스트 데이터 초기화
        initializeTestData();
        
        // UI 업데이트
        view.updatePlantSelector(plantsData);
        
        // 이벤트 리스너 설정
        setupEventListeners();
        
        // 초기 식물 정보 업데이트
        if (!plantsData.isEmpty()) {
            String firstPlant = plantsData.keySet().iterator().next();
            view.selectPlant(firstPlant);
            updatePlantInfo();
        }
    }
    
    /**
     * 테스트용 식물 데이터 초기화
     */
    private void initializeTestData() {
        // 딸기 데이터
        PlantModel strawberry = new PlantModel("딸기A", "딸기", "과일", 5000);
        strawberry.setGrowthStage(PlantModel.GrowthStage.FLOWERING);
        strawberry.setHealthStatus(PlantModel.HealthStatus.HEALTHY);
        strawberry.setHealthNotes("🌸 꽃이 피기 시작했습니다. 수분 작업 완료.\n" +
                                  "💧 매일 오전 8시에 물주기 실시.\n" +
                                  "📊 성장 상태 양호, 잎의 색깔이 진한 녹색을 유지하고 있음.");
        strawberry.setPlantDate("2025-02-10");
        strawberry.setLocation("온실 C-2");
        plantsData.put("딸기A", strawberry);

        // 상추 데이터
        PlantModel lettuce = new PlantModel("상추B", "상추", "작물", 2000);
        lettuce.setGrowthStage(PlantModel.GrowthStage.GROWTH);
        lettuce.setHealthStatus(PlantModel.HealthStatus.HEALTHY);
        lettuce.setHealthNotes("🌱 빠른 성장 중입니다. 잎이 크고 두꺼워지고 있음.\n" +
                              "☀️ 충분한 햇빛 노출 중 (하루 6-8시간).\n" +
                              "🔍 병해충 발견되지 않음. 정기 점검 지속.");
        lettuce.setPlantDate("2025-03-05");
        lettuce.setLocation("온실 B-3");
        plantsData.put("상추B", lettuce);

        // 포도 데이터
        PlantModel grape = new PlantModel("포도C", "포도", "과일", 7000);
        grape.setGrowthStage(PlantModel.GrowthStage.GROWTH);
        grape.setHealthStatus(PlantModel.HealthStatus.INFECTED);
        grape.setHealthNotes("⚠️ 잎에 흰가루병 발견됨 (5월 20일).\n" +
                            "💊 천연 살균제(베이킹소다 용액) 살포 완료.\n" +
                            "📅 3일 간격으로 재처리 예정.\n" +
                            "🔍 감염된 잎 제거 완료, 통풍 개선 조치.");
        grape.setPlantDate("2025-01-20");
        grape.setLocation("온실 A-1");
        plantsData.put("포도C", grape);

        // 당근 데이터
        PlantModel carrot = new PlantModel("당근D", "당근", "작물", 3000);
        carrot.setGrowthStage(PlantModel.GrowthStage.GERMINATION);
        carrot.setHealthStatus(PlantModel.HealthStatus.HEALTHY);
        carrot.setHealthNotes("🌱 발아가 완료되었습니다. 작은 잎들이 나타남.\n" +
                             "💧 토양 수분을 적절히 유지 중.\n" +
                             "🌡️ 온도 관리 양호 (20-25도 유지).\n" +
                             "📏 현재 높이 약 3cm, 성장 속도 정상.");
        carrot.setPlantDate("2025-04-01");
        carrot.setLocation("야외 E-2");
        plantsData.put("당근D", carrot);

        // 한라봉 데이터
        PlantModel hallabong = new PlantModel("한라봉E", "한라봉", "과일", 8000);
        hallabong.setGrowthStage(PlantModel.GrowthStage.FRUITING);
        hallabong.setHealthStatus(PlantModel.HealthStatus.TREATED);
        hallabong.setHealthNotes("🍊 작은 열매들이 맺히기 시작했습니다.\n" +
                                "🐛 진딧물 발생 후 천적 곤충 투입으로 처리 완료.\n" +
                                "💊 유기농 방제제 1주일간 사용.\n" +
                                "📈 현재 경과 관찰 중, 회복 양호.\n" +
                                "🍃 새 잎들이 건강하게 자라고 있음.");
        hallabong.setPlantDate("2024-11-15");
        hallabong.setLocation("온실 D-4");
        plantsData.put("한라봉E", hallabong);

        // 바질 데이터
        PlantModel basil = new PlantModel("바질F", "바질", "허브", 4000);
        basil.setGrowthStage(PlantModel.GrowthStage.GROWTH);
        basil.setHealthStatus(PlantModel.HealthStatus.HEALTHY);
        basil.setHealthNotes("🌿 향긋한 냄새가 강해지고 있습니다.\n" +
                            "✂️ 첫 수확을 위한 순지르기 완료.\n" +
                            "🌡️ 실내 온도 22도 유지 중.\n" +
                            "💡 LED 조명으로 추가 광량 공급.");
        basil.setPlantDate("2025-03-20");
        basil.setLocation("실내 D-1");
        plantsData.put("바질F", basil);
    }
    
    /**
     * 모든 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        // 식물 선택 콤보박스 이벤트
        view.getPlantSelector().addActionListener(e -> {
            updatePlantInfo();
            updateSelectedPlantLabel();
        });
        
        // 성장 단계 관련 버튼
        view.getAdvanceStageButton().addActionListener(e -> advanceGrowthStage());
        
        // 건강 상태 변경 버튼들
        view.getHealthyButton().addActionListener(e -> changeHealthStatus(PlantModel.HealthStatus.HEALTHY));
        view.getInfectedButton().addActionListener(e -> changeHealthStatus(PlantModel.HealthStatus.INFECTED));
        view.getTreatedButton().addActionListener(e -> changeHealthStatus(PlantModel.HealthStatus.TREATED));
        
        // 저장/취소 버튼
        view.getSaveButton().addActionListener(e -> saveHealthNotes());
        view.getCancelButton().addActionListener(e -> view.dispose());
        
        // 데이터 내보내기 버튼
        view.getExportButton().addActionListener(e -> exportMonitoringData());
    }
    
    /**
     * 선택된 식물 정보 업데이트
     */
    private void updatePlantInfo() {
        String selectedPlant = view.getSelectedPlant();
        currentSelectedPlant = selectedPlant;
        
        if (selectedPlant != null && plantsData.containsKey(selectedPlant)) {
            PlantModel plantData = plantsData.get(selectedPlant);
            
            // 성장 단계 업데이트
            view.updateGrowthProgress(plantData.getGrowthStage());
            
            // 건강 상태 업데이트
            view.updateHealthStatusPanel(plantData.getHealthStatus());
            
            // 건강 상태 노트 업데이트
            view.setHealthNotes(plantData.getHealthNotes());
            
            // 버튼 활성화
            view.setButtonsEnabled(true);
        } else {
            // 선택된 식물이 없거나 유효하지 않은 경우
            view.resetUI();
            view.setButtonsEnabled(false);
        }
    }
    
    /**
     * 선택된 식물 라벨 업데이트
     */
    private void updateSelectedPlantLabel() {
        String selectedPlant = view.getSelectedPlant();
        view.updateSelectedPlantLabel(selectedPlant);
    }
    
    /**
     * 성장 단계 진행
     */
    private void advanceGrowthStage() {
        String selectedPlant = currentSelectedPlant;
        if (selectedPlant != null && plantsData.containsKey(selectedPlant)) {
            PlantModel plantData = plantsData.get(selectedPlant);
            PlantModel.GrowthStage currentStage = plantData.getGrowthStage();
            
            // 다음 단계로 진행
            PlantModel.GrowthStage[] stages = PlantModel.GrowthStage.values();
            for (int i = 0; i < stages.length - 1; i++) {
                if (stages[i] == currentStage) {
                    PlantModel.GrowthStage nextStage = stages[i + 1];
                    plantData.setGrowthStage(nextStage);
                    
                    // 성장 단계 변경에 따른 자동 기록 추가
                    String stageChangeNote = generateStageChangeNote(currentStage, nextStage);
                    addHealthNote(plantData, stageChangeNote);
                    
                    updatePlantInfo();
                    
                    UIUtils.showInfoMessage(view,
                        "🚀 " + selectedPlant + "의 성장 단계가 '" + nextStage.getLabel() + 
                        "'로 진행되었습니다!\n\n" +
                        "💡 " + getStageAdviceByStage(nextStage),
                        "성장 단계 진행");
                    return;
                }
            }
            
            // 이미 마지막 단계인 경우
            if (currentStage == PlantModel.GrowthStage.HARVEST) {
                boolean startNewCycle = UIUtils.showConfirmDialog(view,
                    "🎉 " + selectedPlant + "는 이미 수확 단계입니다!\n\n" +
                    "🔄 새로운 재배 사이클을 시작하시겠습니까?\n" +
                    "(성장 단계가 '씨앗' 단계로 초기화됩니다)",
                    "새로운 재배 사이클");
                
                if (startNewCycle) {
                    plantData.setGrowthStage(PlantModel.GrowthStage.SEED);
                    String newCycleNote = "🔄 " + getCurrentDateTime() + " - 새로운 재배 사이클 시작\n" +
                                         "이전 사이클 수확 완료 후 새로 시작합니다.";
                    addHealthNote(plantData, newCycleNote);
                    updatePlantInfo();
                    
                    UIUtils.showInfoMessage(view,
                        "🔄 새로운 재배 사이클이 시작되었습니다.\n" +
                        "성장 단계: 씨앗",
                        "새로운 사이클 시작");
                }
            }
        }
    }
    
    /**
     * 건강 상태 변경
     */
    private void changeHealthStatus(PlantModel.HealthStatus newStatus) {
        String selectedPlant = currentSelectedPlant;
        if (selectedPlant != null && plantsData.containsKey(selectedPlant)) {
            PlantModel plantData = plantsData.get(selectedPlant);
            PlantModel.HealthStatus oldStatus = plantData.getHealthStatus();
            
            plantData.setHealthStatus(newStatus);
            
            // 상태 변경에 따른 기본 메시지 추가
            String statusChangeNote = generateStatusChangeNote(oldStatus, newStatus);
            view.setHealthNotes(statusChangeNote + "\n\n" + view.getHealthNotes());
            
            updatePlantInfo();
            
            // 상태별 추가 안내
            String additionalInfo = getHealthStatusAdvice(newStatus);
            UIUtils.showInfoMessage(view,
                "💚 " + selectedPlant + "의 건강 상태가 '" + newStatus.getLabel() + 
                "'으로 변경되었습니다.\n\n" + additionalInfo,
                "건강 상태 변경");
        }
    }
    
    /**
     * 건강 상태 기록 저장
     */
    private void saveHealthNotes() {
        String selectedPlant = currentSelectedPlant;
        if (selectedPlant != null && plantsData.containsKey(selectedPlant)) {
            PlantModel plantData = plantsData.get(selectedPlant);
            String notes = view.getHealthNotes();
            
            // 현재 시간 정보 추가
            String timestampedNotes = addTimestampToNotes(notes);
            plantData.setHealthNotes(timestampedNotes);
            
            UIUtils.showInfoMessage(view,
                "💾 " + selectedPlant + "의 건강 상태 기록이 저장되었습니다.\n\n" +
                "📅 저장 시간: " + getCurrentDateTime(),
                "저장 완료");
        }
    }
    
    /**
     * 모니터링 데이터 내보내기
     */
    private void exportMonitoringData() {
        if (plantsData.isEmpty()) {
            UIUtils.showWarningMessage(view,
                "📊 내보낼 모니터링 데이터가 없습니다.",
                "데이터 없음");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("모니터링 데이터 내보내기");
        fileChooser.setSelectedFile(new java.io.File("plant_monitoring_" + 
            new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".txt"));
        
        int result = fileChooser.showSaveDialog(view);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            
            try {
                exportToFile(file);
                UIUtils.showInfoMessage(view,
                    "📊 모니터링 데이터가 성공적으로 내보내졌습니다!\n\n" +
                    "📁 파일 위치: " + file.getAbsolutePath(),
                    "내보내기 완료");
            } catch (Exception e) {
                UIUtils.showErrorMessage(view,
                    "❌ 파일 저장 중 오류가 발생했습니다:\n" + e.getMessage(),
                    "내보내기 실패");
            }
        }
    }
    
    /**
     * 파일로 데이터 내보내기
     */
    private void exportToFile(java.io.File file) throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, java.nio.charset.StandardCharsets.UTF_8))) {
            writer.write("=== 식물 모니터링 데이터 보고서 ===\n");
            writer.write("생성일시: " + getCurrentDateTime() + "\n");
            writer.write("총 식물 수: " + plantsData.size() + "개\n\n");
            
            for (Map.Entry<String, PlantModel> entry : plantsData.entrySet()) {
                String plantName = entry.getKey();
                PlantModel plant = entry.getValue();
                
                writer.write("🌱 식물명: " + plantName + "\n");
                writer.write("종류: " + plant.getCategory() + "\n");
                writer.write("심은 날짜: " + (plant.getPlantDate() != null ? plant.getPlantDate() : "미기록") + "\n");
                writer.write("위치: " + (plant.getLocation() != null ? plant.getLocation() : "미기록") + "\n");
                writer.write("성장 단계: " + plant.getGrowthStage().getLabel() + 
                           " (" + plant.getGrowthStage().getProgressValue() + "%)\n");
                writer.write("건강 상태: " + plant.getHealthStatus().getLabel() + "\n\n");
                
                writer.write("📝 관찰 기록:\n");
                writer.write(plant.getHealthNotes() != null ? plant.getHealthNotes() : "기록 없음");
                writer.write("\n");
                writer.write("=====================================\n\n");
            }
            
            // 통계 정보 추가
            writer.write("📊 통계 정보\n");
            writer.write("=====================================\n");
            writeStatistics(writer);
        }
    }
    
    /**
     * 통계 정보 작성
     */
    private void writeStatistics(BufferedWriter writer) throws Exception {
        Map<PlantModel.GrowthStage, Integer> stageCount = new HashMap<>();
        Map<PlantModel.HealthStatus, Integer> healthCount = new HashMap<>();
        Map<String, Integer> categoryCount = new HashMap<>();
        
        // 통계 계산
        for (PlantModel plant : plantsData.values()) {
            stageCount.put(plant.getGrowthStage(), 
                          stageCount.getOrDefault(plant.getGrowthStage(), 0) + 1);
            healthCount.put(plant.getHealthStatus(), 
                           healthCount.getOrDefault(plant.getHealthStatus(), 0) + 1);
            categoryCount.put(plant.getCategory(), 
                             categoryCount.getOrDefault(plant.getCategory(), 0) + 1);
        }
        
        writer.write("성장 단계별 분포:\n");
        for (PlantModel.GrowthStage stage : PlantModel.GrowthStage.values()) {
            int count = stageCount.getOrDefault(stage, 0);
            writer.write("  " + stage.getLabel() + ": " + count + "개\n");
        }
        
        writer.write("\n건강 상태별 분포:\n");
        for (PlantModel.HealthStatus status : PlantModel.HealthStatus.values()) {
            int count = healthCount.getOrDefault(status, 0);
            writer.write("  " + status.getLabel() + ": " + count + "개\n");
        }
        
        writer.write("\n식물 종류별 분포:\n");
        for (Map.Entry<String, Integer> entry : categoryCount.entrySet()) {
            writer.write("  " + entry.getKey() + ": " + entry.getValue() + "개\n");
        }
    }
    
    // ========== 유틸리티 메소드들 ==========
    
    /**
     * 성장 단계 변경 기록 생성
     */
    private String generateStageChangeNote(PlantModel.GrowthStage oldStage, PlantModel.GrowthStage newStage) {
        return "🚀 " + getCurrentDateTime() + " - 성장 단계 진행\n" +
               oldStage.getLabel() + " → " + newStage.getLabel() + 
               " (" + newStage.getProgressValue() + "% 달성)";
    }
    
    /**
     * 건강 상태 변경 기록 생성
     */
    private String generateStatusChangeNote(PlantModel.HealthStatus oldStatus, PlantModel.HealthStatus newStatus) {
        String statusIcon = getHealthStatusIcon(newStatus);
        return statusIcon + " " + getCurrentDateTime() + " - 건강 상태 변경\n" +
               oldStatus.getLabel() + " → " + newStatus.getLabel();
    }
    
    /**
     * 건강 상태별 아이콘 반환
     */
    private String getHealthStatusIcon(PlantModel.HealthStatus status) {
        switch (status) {
            case HEALTHY: return "😊";
            case INFECTED: return "😷";
            case TREATED: return "🩹";
            default: return "❓";
        }
    }
    
    /**
     * 성장 단계별 조언 반환
     */
    private String getStageAdviceByStage(PlantModel.GrowthStage stage) {
        switch (stage) {
            case SEED:
                return "적절한 온도와 습도를 유지하세요.";
            case GERMINATION:
                return "발아 후 충분한 빛을 제공하세요.";
            case GROWTH:
                return "물과 영양분 공급을 꾸준히 해주세요.";
            case FLOWERING:
                return "꽃이 피는 시기입니다. 수분 작업을 고려하세요.";
            case FRUITING:
                return "열매가 맺히는 시기입니다. 지지대를 확인하세요.";
            case HARVEST:
                return "수확 시기입니다! 적절한 때에 수확하세요.";
            default:
                return "지속적인 관찰과 관리가 필요합니다.";
        }
    }
    
    /**
     * 건강 상태별 조언 반환
     */
    private String getHealthStatusAdvice(PlantModel.HealthStatus status) {
        switch (status) {
            case HEALTHY:
                return "💡 현재 상태를 유지하기 위해 정기적인 관찰을 계속하세요.";
            case INFECTED:
                return "⚠️ 감염 원인을 파악하고 즉시 적절한 조치를 취하세요.\n" +
                       "다른 식물로의 전파를 방지하기 위해 격리를 고려하세요.";
            case TREATED:
                return "🔍 치료 후 경과를 면밀히 관찰하세요.\n" +
                       "회복 상태에 따라 추가 조치가 필요할 수 있습니다.";
            default:
                return "전문가의 조언을 구하는 것을 권장합니다.";
        }
    }
    
    /**
     * 기록에 타임스탬프 추가
     */
    private String addTimestampToNotes(String notes) {
        if (notes == null || notes.trim().isEmpty()) {
            return "";
        }
        
        // 이미 최근 타임스탬프가 있는지 확인
        String[] lines = notes.split("\n");
        if (lines.length > 0 && lines[0].contains(getCurrentDate())) {
            return notes; // 이미 오늘 날짜가 있으면 그대로 반환
        }
        
        return notes;
    }
    
    /**
     * 건강 기록에 새 내용 추가
     */
    private void addHealthNote(PlantModel plant, String newNote) {
        String currentNotes = plant.getHealthNotes();
        if (currentNotes == null || currentNotes.trim().isEmpty()) {
            plant.setHealthNotes(newNote);
        } else {
            plant.setHealthNotes(newNote + "\n\n" + currentNotes);
        }
    }
    
    /**
     * 현재 날짜시간 반환
     */
    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
    
    /**
     * 현재 날짜 반환
     */
    private String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
    
    // ========== 공개 메소드들 (다른 컨트롤러에서 호출 가능) ==========
    
    /**
     * 새 식물 추가 (다른 컨트롤러에서 호출)
     */
    public void addPlant(String id, String name, String category) {
        if (!plantsData.containsKey(id)) {
            PlantModel newPlant = new PlantModel(id, name, category);
            newPlant.setGrowthStage(PlantModel.GrowthStage.SEED);
            newPlant.setHealthStatus(PlantModel.HealthStatus.HEALTHY);
            newPlant.setHealthNotes("🌱 " + getCurrentDateTime() + " - 새로운 식물이 등록되었습니다.\n" +
                                   "정기적인 관찰과 관리를 시작합니다.");
            
            plantsData.put(id, newPlant);
            view.updatePlantSelector(plantsData);
            view.selectPlant(id);
            updatePlantInfo();
            
            UIUtils.showInfoMessage(view,
                "🌱 " + id + "(" + name + ") 식물이 모니터링 시스템에 추가되었습니다.",
                "식물 추가");
        }
    }
    
    /**
     * 식물 삭제 (다른 컨트롤러에서 호출)
     */
    public void removePlant(String id) {
        if (plantsData.containsKey(id)) {
            plantsData.remove(id);
            view.updatePlantSelector(plantsData);
            
            if (!plantsData.isEmpty()) {
                String firstPlant = plantsData.keySet().iterator().next();
                view.selectPlant(firstPlant);
                updatePlantInfo();
            } else {
                view.resetUI();
            }
            
            UIUtils.showInfoMessage(view,
                "🗑️ " + id + " 식물이 모니터링 시스템에서 삭제되었습니다.",
                "식물 삭제");
        }
    }
    
    /**
     * 특정 식물의 정보 반환 (다른 컨트롤러에서 호출)
     */
    public PlantModel getPlantData(String id) {
        return plantsData.get(id);
    }
    
    /**
     * 모든 식물 데이터 반환 (다른 컨트롤러에서 호출)
     */
    public Map<String, PlantModel> getAllPlantsData() {
        return new HashMap<>(plantsData); // 복사본 반환
    }
    
    /**
     * 식물 정보 업데이트 (다른 컨트롤러에서 호출)
     */
    public void updatePlantData(String id, PlantModel updatedPlant) {
        if (plantsData.containsKey(id)) {
            plantsData.put(id, updatedPlant);
            
            // 현재 선택된 식물이라면 UI 업데이트
            if (id.equals(currentSelectedPlant)) {
                updatePlantInfo();
            }
        }
    }
    
    /**
     * 특정 식물 선택 (다른 컨트롤러에서 호출)
     */
    public void selectPlantForMonitoring(String plantId) {
        if (plantsData.containsKey(plantId)) {
            view.selectPlant(plantId);
            updatePlantInfo();
            updateSelectedPlantLabel();
        }
    }
    
    /**
     * 건강하지 않은 식물 목록 반환
     */
    public java.util.List<String> getUnhealthyPlants() {
        return plantsData.entrySet().stream()
                .filter(entry -> entry.getValue().getHealthStatus() != PlantModel.HealthStatus.HEALTHY)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 수확 준비된 식물 목록 반환
     */
    public java.util.List<String> getHarvestReadyPlants() {
        return plantsData.entrySet().stream()
                .filter(entry -> entry.getValue().getGrowthStage() == PlantModel.GrowthStage.HARVEST)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 모니터링 요약 정보 반환
     */
    public MonitoringSummary getMonitoringSummary() {
        int totalPlants = plantsData.size();
        long healthyCount = plantsData.values().stream()
                .filter(plant -> plant.getHealthStatus() == PlantModel.HealthStatus.HEALTHY)
                .count();
        long harvestReady = plantsData.values().stream()
                .filter(plant -> plant.getGrowthStage() == PlantModel.GrowthStage.HARVEST)
                .count();
        long needsAttention = plantsData.values().stream()
                .filter(plant -> plant.getHealthStatus() == PlantModel.HealthStatus.INFECTED)
                .count();
        
        return new MonitoringSummary(totalPlants, (int)healthyCount, (int)harvestReady, (int)needsAttention);
    }
    
    /**
     * 모니터링 요약 정보 클래스
     */
    public static class MonitoringSummary {
        public final int totalPlants;
        public final int healthyPlants;
        public final int harvestReadyPlants;
        public final int plantsNeedingAttention;
        
        public MonitoringSummary(int total, int healthy, int harvestReady, int needsAttention) {
            this.totalPlants = total;
            this.healthyPlants = healthy;
            this.harvestReadyPlants = harvestReady;
            this.plantsNeedingAttention = needsAttention;
        }
    }
}