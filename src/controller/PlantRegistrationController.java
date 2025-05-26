package controller;

import view.PlantRegistrationView;
import model.PlantModel;
import util.UIUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 식물 등록 컨트롤러 - 식물 등록/수정/삭제 로직 담당
 * View는 UI만 담당하고, 이 클래스가 모든 비즈니스 로직을 처리
 */
public class PlantRegistrationController {
    private PlantRegistrationView view;
    private List<PlantModel> plants;
    private String currentPlantId = ""; // 현재 편집 중인 식물 ID
    private boolean isEditMode = false;  // 편집 모드 여부
    
    public PlantRegistrationController(PlantRegistrationView view) {
        this.view = view;
        this.plants = new ArrayList<>();
        
        // 테스트 데이터 초기화
        initializeTestData();
        
        // 이벤트 리스너 설정
        setupEventListeners();
        
        // 테이블 업데이트
        refreshPlantsTable();
        
        // 초기 포커스 설정
        view.getNameField().requestFocus();
    }
    
    /**
     * 테스트용 식물 데이터 초기화
     */
    private void initializeTestData() {
        plants.add(new PlantModel("1", "토마토", "채소", 3000));
        plants.get(plants.size() - 1).setPlantDate("2025-01-15");
        plants.get(plants.size() - 1).setLocation("온실 A-1");
        plants.get(plants.size() - 1).setNotes("방울토마토 품종. 지지대 설치 완료.");
        
        plants.add(new PlantModel("2", "상추", "채소", 2000));
        plants.get(plants.size() - 1).setPlantDate("2025-02-10");
        plants.get(plants.size() - 1).setLocation("온실 B-3");
        plants.get(plants.size() - 1).setNotes("적상추. 수경재배로 관리 중.");
        
        plants.add(new PlantModel("3", "딸기", "과일", 5000));
        plants.get(plants.size() - 1).setPlantDate("2025-03-05");
        plants.get(plants.size() - 1).setLocation("온실 C-2");
        plants.get(plants.size() - 1).setNotes("설향 품종. 런너 제거 완료.");
        
        plants.add(new PlantModel("4", "바질", "허브", 4000));
        plants.get(plants.size() - 1).setPlantDate("2025-03-20");
        plants.get(plants.size() - 1).setLocation("실내 D-1");
        plants.get(plants.size() - 1).setNotes("스위트 바질. 실내 화분 재배.");
        
        plants.add(new PlantModel("5", "당근", "뿌리채소", 3000));
        plants.get(plants.size() - 1).setPlantDate("2025-04-01");
        plants.get(plants.size() - 1).setLocation("야외 E-2");
        plants.get(plants.size() - 1).setNotes("오렌지 당근. 토양 개량 완료.");
    }
    
    /**
     * 모든 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        // 폼 버튼 이벤트들
        view.getSaveButton().addActionListener(e -> savePlant());
        view.getCancelButton().addActionListener(e -> cancelEdit());
        view.getDatePickerButton().addActionListener(e -> showDatePicker());
        
        // 테이블 관리 버튼 이벤트들
        view.getEditButton().addActionListener(e -> editSelectedPlant());
        view.getDeleteButton().addActionListener(e -> deleteSelectedPlant());
        view.getRefreshButton().addActionListener(e -> refreshPlantsTable());
        
        // 테이블 더블클릭 이벤트 (편집)
        view.getPlantsTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedPlant();
                }
            }
        });
        
        // 테이블 선택 변경 이벤트
        view.getPlantsTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateButtonStates();
            }
        });
        
        // 입력 필드 변경 이벤트 (실시간 검증)
        view.getNameField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                validateInputRealtime();
            }
        });
        
        view.getSpeciesField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                validateInputRealtime();
            }
        });
        
        view.getPlantDateField().addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                validateInputRealtime();
            }
        });
    }
    
    /**
     * 식물 저장 (신규 등록 또는 수정)
     */
    private void savePlant() {
        view.setLoading(true);
        
        try {
            // 입력 검증
            if (!validateInput()) {
                return;
            }
            
            String id = currentPlantId.isEmpty() ? String.valueOf(getNextId()) : currentPlantId;
            String name = view.getNameField().getText().trim();
            String species = view.getSpeciesField().getText().trim();
            String plantDate = view.getPlantDateField().getText().trim();
            String location = view.getLocationField().getText().trim();
            String notes = view.getNotesArea().getText().trim();
            
            if (isEditMode && !currentPlantId.isEmpty()) {
                // 기존 식물 수정
                PlantModel plant = findPlantById(currentPlantId);
                if (plant != null) {
                    plant.setName(name);
                    plant.setCategory(species);
                    plant.setPlantDate(plantDate);
                    plant.setLocation(location);
                    plant.setNotes(notes);
                    
                    UIUtils.showInfoMessage(view, 
                        "" + name + "' 식물 정보가 성공적으로 수정되었습니다.",
                        "수정 완료");
                }
            } else {
                // 새 식물 추가
                // 중복 이름 확인
                if (isPlantNameExists(name)) {
                    UIUtils.showWarningMessage(view,
                        "⚠ '" + name + "'은(는) 이미 등록된 식물 이름입니다.\n" +
                        "다른 이름을 사용하거나 구별할 수 있는 식별자를 추가해주세요.",
                        "중복된 식물 이름");
                    view.getNameField().requestFocus();
                    return;
                }
                
                PlantModel newPlant = new PlantModel(id, name, species);
                newPlant.setPlantDate(plantDate);
                newPlant.setLocation(location);
                newPlant.setNotes(notes);
                
                // 예상 수확일 자동 계산 (품종별 기본 재배 기간)
                newPlant.setExpectedHarvestDate(calculateExpectedHarvestDate(species, plantDate));
                
                plants.add(newPlant);
                
                UIUtils.showInfoMessage(view, 
                    "'" + name + "' 식물이 성공적으로 등록되었습니다.\n" +
                    "예상 수확일: " + newPlant.getExpectedHarvestDate(), 
                    "등록 완료");
            }
            
            // UI 업데이트
            refreshPlantsTable();
            cancelEdit();
            
        } finally {
            view.setLoading(false);
        }
    }
    
    /**
     * 입력 데이터 유효성 검증
     */
    private boolean validateInput() {
        boolean isValid = true;
        
        // 필수 필드 검증
        boolean nameValid = !view.getNameField().getText().trim().isEmpty();
        boolean speciesValid = !view.getSpeciesField().getText().trim().isEmpty();
        boolean dateValid = UIUtils.isValidDate(view.getPlantDateField().getText().trim());
        
        // 시각적 피드백
        view.highlightInvalidFields(nameValid, speciesValid, dateValid);
        
        if (!nameValid) {
            UIUtils.showWarningMessage(view, 
                "식물 이름을 입력해주세요.",
                "입력 오류");
            view.getNameField().requestFocus();
            isValid = false;
        } else if (!speciesValid) {
            UIUtils.showWarningMessage(view, 
                "식물 종류를 입력해주세요.",
                "입력 오류");
            view.getSpeciesField().requestFocus();
            isValid = false;
        } else if (!dateValid) {
            UIUtils.showWarningMessage(view, 
                "올바른 날짜 형식(yyyy-MM-dd)으로 입력해주세요.\n\n" +
                "예시: 2025-05-23", 
                "날짜 형식 오류");
            view.getPlantDateField().requestFocus();
            isValid = false;
        } else {
            // 미래 날짜 검증
                            if (isFutureDate(view.getPlantDateField().getText().trim())) {
                UIUtils.showWarningMessage(view,
                    "심은 날짜는 오늘 이후의 날짜일 수 없습니다.\n" +
                    "올바른 날짜를 입력해주세요.",
                    "날짜 오류");
                view.getPlantDateField().requestFocus();
                isValid = false;
            }
        }
        
        return isValid;
    }
    
    /**
     * 실시간 입력 검증 (타이핑 중)
     */
    private void validateInputRealtime() {
        boolean nameValid = !view.getNameField().getText().trim().isEmpty();
        boolean speciesValid = !view.getSpeciesField().getText().trim().isEmpty();
        boolean dateValid = UIUtils.isValidDate(view.getPlantDateField().getText().trim());
        
        // 실시간 시각적 피드백 (경고색은 약하게)
        view.highlightInvalidFields(nameValid, speciesValid, dateValid);
        
        // 저장 버튼 활성화/비활성화
        boolean allValid = nameValid && speciesValid && dateValid;
        view.setSaveButtonEnabled(allValid);
    }
    
    /**
     * 날짜 선택기 표시
     */
    private void showDatePicker() {
        UIUtils.showDatePicker(view, view.getPlantDateField());
        
        // 날짜 선택 후 자동으로 예상 수확일 미리보기 (편의 기능)
        String plantDate = view.getPlantDateField().getText().trim();
        String species = view.getSpeciesField().getText().trim();
        
        if (!plantDate.isEmpty() && !species.isEmpty()) {
            String expectedHarvest = calculateExpectedHarvestDate(species, plantDate);
            // 임시로 상태 표시 (실제로는 별도 라벨에 표시)
            // view.updateExpectedHarvestPreview(expectedHarvest);
        }
    }
    
    /**
     * 선택된 식물 편집
     */
    private void editSelectedPlant() {
        int selectedRow = view.getSelectedTableRow();
        if (selectedRow == -1) {
            UIUtils.showWarningMessage(view, 
                "편집할 식물을 테이블에서 선택해주세요.",
                "선택 오류");
            return;
        }
        
        String plantId = (String) view.getTableModel().getValueAt(selectedRow, 0);
        PlantModel plant = findPlantById(plantId);
        
        if (plant != null) {
            // 편집 모드 활성화
            isEditMode = true;
            currentPlantId = plant.getId();
            
            // 폼에 데이터 설정
            view.getNameField().setText(plant.getName());
            view.getSpeciesField().setText(plant.getCategory());
            view.getPlantDateField().setText(plant.getPlantDate());
            view.getLocationField().setText(plant.getLocation() != null ? plant.getLocation() : "");
            view.getNotesArea().setText(plant.getNotes() != null ? plant.getNotes() : "");
            
            // UI 상태 변경
            view.setEditMode(true);
            view.getNameField().requestFocus();
            
            // 버튼 상태 업데이트
            updateButtonStates();
        }
    }
    
    /**
     * 선택된 식물 삭제
     */
    private void deleteSelectedPlant() {
        int selectedRow = view.getSelectedTableRow();
        if (selectedRow == -1) {
            UIUtils.showWarningMessage(view, 
                "삭제할 식물을 테이블에서 선택해주세요.",
                "선택 오류");
            return;
        }
        
        String plantId = (String) view.getTableModel().getValueAt(selectedRow, 0);
        PlantModel plant = findPlantById(plantId);
        
        if (plant != null) {
            boolean confirm = UIUtils.showConfirmDialog(view,
                "'" + plant.getName() + "' 식물을 정말 삭제하시겠습니까?\n\n" +
                "삭제된 데이터는 복구할 수 없습니다.\n" +
                "관련된 모든 기록(수확, 판매, 모니터링)도 함께 삭제됩니다.",
                "식물 삭제 확인");
            
            if (confirm) {
                // 현재 편집 중인 식물이라면 편집 모드 해제
                if (plant.getId().equals(currentPlantId)) {
                    cancelEdit();
                }
                
                // 식물 삭제
                plants.remove(plant);
                refreshPlantsTable();
                
                UIUtils.showInfoMessage(view,
                    "'" + plant.getName() + "' 식물이 성공적으로 삭제되었습니다.",
                    "삭제 완료");
            }
        }
    }
    
    /**
     * 편집 취소 / 폼 초기화
     */
    private void cancelEdit() {
        // 편집 모드 해제
        isEditMode = false;
        currentPlantId = "";
        
        // 폼 초기화
        view.clearForm();
        view.setEditMode(false);
        view.resetFieldBorders();
        
        // 버튼 상태 업데이트
        updateButtonStates();
        
        // 테이블 선택 해제
        view.getPlantsTable().clearSelection();
    }
    
    /**
     * 식물 목록 테이블 새로고침
     */
    private void refreshPlantsTable() {
        view.getTableModel().setRowCount(0);
        
        for (PlantModel plant : plants) {
            Object[] row = {
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getPlantDate(),
                plant.getLocation() != null ? plant.getLocation() : ""
            };
            view.getTableModel().addRow(row);
        }
        
        // 테이블 정보 업데이트
        view.updateTableInfo();
        
        // 버튼 상태 업데이트
        updateButtonStates();
    }
    
    /**
     * 버튼 상태 업데이트 (선택 여부에 따라)
     */
    private void updateButtonStates() {
        boolean hasSelection = view.getSelectedTableRow() != -1;
        boolean hasData = view.getTableModel().getRowCount() > 0;
        
        view.getEditButton().setEnabled(hasSelection);
        view.getDeleteButton().setEnabled(hasSelection);
        view.getRefreshButton().setEnabled(hasData);
    }
    
    /**
     * ID로 식물 찾기
     */
    private PlantModel findPlantById(String id) {
        return plants.stream()
                .filter(plant -> plant.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * 식물 이름 중복 확인
     */
    private boolean isPlantNameExists(String name) {
        return plants.stream()
                .anyMatch(plant -> plant.getName().equalsIgnoreCase(name.trim()) && 
                         !plant.getId().equals(currentPlantId));
    }
    
    /**
     * 다음 식물 ID 생성
     */
    private int getNextId() {
        return plants.stream()
                .mapToInt(plant -> {
                    try {
                        return Integer.parseInt(plant.getId());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max()
                .orElse(0) + 1;
    }
    
    /**
     * 미래 날짜 여부 확인
     */
    private boolean isFutureDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date inputDate = sdf.parse(dateStr);
            Date today = new Date();
            
            // 시간 부분 제거하고 날짜만 비교
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(today);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            cal.set(java.util.Calendar.SECOND, 0);
            cal.set(java.util.Calendar.MILLISECOND, 0);
            Date todayMidnight = cal.getTime();
            
            return inputDate.after(todayMidnight);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 예상 수확일 계산 (품종별 일반적인 재배 기간 기준)
     */
    private String calculateExpectedHarvestDate(String species, String plantDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(plantDate);
            
            // 품종별 일반적인 재배 기간 (일)
            int growthDays = getGrowthPeriodBySpecies(species);
            
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(java.util.Calendar.DAY_OF_MONTH, growthDays);
            
            return sdf.format(cal.getTime());
        } catch (Exception e) {
            return "계산 불가";
        }
    }
    
    /**
     * 품종별 재배 기간 반환 (일)
     */
    private int getGrowthPeriodBySpecies(String species) {
        String lowerSpecies = species.toLowerCase();
        
        // 품종별 일반적인 재배 기간 설정
        if (lowerSpecies.contains("상추") || lowerSpecies.contains("lettuce")) {
            return 45;  // 45일
        } else if (lowerSpecies.contains("딸기") || lowerSpecies.contains("strawberry")) {
            return 90;  // 90일
        } else if (lowerSpecies.contains("토마토") || lowerSpecies.contains("tomato")) {
            return 75;  // 75일
        } else if (lowerSpecies.contains("당근") || lowerSpecies.contains("carrot")) {
            return 70;  // 70일
        } else if (lowerSpecies.contains("바질") || lowerSpecies.contains("basil")) {
            return 60;  // 60일
        } else if (lowerSpecies.contains("시금치") || lowerSpecies.contains("spinach")) {
            return 40;  // 40일
        } else if (lowerSpecies.contains("배추") || lowerSpecies.contains("cabbage")) {
            return 80;  // 80일
        } else if (lowerSpecies.contains("오이") || lowerSpecies.contains("cucumber")) {
            return 55;  // 55일
        } else if (lowerSpecies.contains("호박") || lowerSpecies.contains("pumpkin")) {
            return 100; // 100일
        } else if (lowerSpecies.contains("포도") || lowerSpecies.contains("grape")) {
            return 120; // 120일 (포도는 더 길지만 간소화)
        } else {
            return 60;  // 기본값: 60일
        }
    }
    
    // ========== 공개 메소드들 (다른 컨트롤러에서 호출 가능) ==========
    
    /**
     * 외부에서 식물 추가 (다른 컨트롤러에서 호출)
     */
    public void addPlantFromExternal(String name, String species, String plantDate, String location) {
        view.getNameField().setText(name);
        view.getSpeciesField().setText(species);
        view.getPlantDateField().setText(plantDate);
        view.getLocationField().setText(location);
        view.getNameField().requestFocus();
    }
    
    /**
     * 특정 식물을 편집 모드로 설정 (다른 컨트롤러에서 호출)
     */
    public void editPlantById(String plantId) {
        PlantModel plant = findPlantById(plantId);
        if (plant != null) {
            // 테이블에서 해당 행 선택
            for (int i = 0; i < view.getTableModel().getRowCount(); i++) {
                if (view.getTableModel().getValueAt(i, 0).equals(plantId)) {
                    view.selectTableRow(i);
                    editSelectedPlant();
                    break;
                }
            }
        }
    }
    
    /**
     * 등록된 모든 식물 목록 반환 (다른 컨트롤러에서 사용)
     */
    public List<PlantModel> getAllPlants() {
        return new ArrayList<>(plants); // 복사본 반환
    }
    
    /**
     * 특정 ID의 식물 반환 (다른 컨트롤러에서 사용)
     */
    public PlantModel getPlantById(String id) {
        return findPlantById(id);
    }
    
    /**
     * 식물 삭제 (다른 컨트롤러에서 호출)
     */
    public boolean deletePlantById(String plantId) {
        PlantModel plant = findPlantById(plantId);
        if (plant != null) {
            plants.remove(plant);
            refreshPlantsTable();
            
            // 현재 편집 중인 식물이라면 편집 모드 해제
            if (plant.getId().equals(currentPlantId)) {
                cancelEdit();
            }
            
            return true;
        }
        return false;
    }
    
    /**
     * 식물 정보 업데이트 (다른 컨트롤러에서 호출)
     */
    public boolean updatePlant(PlantModel updatedPlant) {
        PlantModel existingPlant = findPlantById(updatedPlant.getId());
        if (existingPlant != null) {
            // 기존 식물 정보 업데이트
            existingPlant.setName(updatedPlant.getName());
            existingPlant.setCategory(updatedPlant.getCategory());
            existingPlant.setPlantDate(updatedPlant.getPlantDate());
            existingPlant.setLocation(updatedPlant.getLocation());
            existingPlant.setNotes(updatedPlant.getNotes());
            existingPlant.setGrowthStage(updatedPlant.getGrowthStage());
            existingPlant.setHealthStatus(updatedPlant.getHealthStatus());
            
            refreshPlantsTable();
            return true;
        }
        return false;
    }
    
    /**
     * 식물 검색 (이름 또는 종류로)
     */
    public List<PlantModel> searchPlants(String keyword) {
        return plants.stream()
                .filter(plant -> 
                    plant.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                    plant.getCategory().toLowerCase().contains(keyword.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 특정 종류의 식물 개수 반환
     */
    public int getPlantCountByCategory(String category) {
        return (int) plants.stream()
                .filter(plant -> plant.getCategory().equalsIgnoreCase(category))
                .count();
    }
    
    /**
     * 현재 편집 중인지 여부 반환
     */
    public boolean isCurrentlyEditing() {
        return isEditMode;
    }
    
    /**
     * 통계 정보 반환
     */
    public PlantStatistics getStatistics() {
        return new PlantStatistics(
            plants.size(),
            getPlantCountByCategory("과일"),
            getPlantCountByCategory("채소"),
            getPlantCountByCategory("허브"),
            plants.stream().map(PlantModel::getCategory).distinct().count()
        );
    }
    
    /**
     * 식물 통계 정보 클래스
     */
    public static class PlantStatistics {
        public final int totalPlants;
        public final int fruitCount;
        public final int vegetableCount;
        public final int herbCount;
        public final long categoryCount;
        
        public PlantStatistics(int total, int fruit, int vegetable, int herb, long categories) {
            this.totalPlants = total;
            this.fruitCount = fruit;
            this.vegetableCount = vegetable;
            this.herbCount = herb;
            this.categoryCount = categories;
        }
    }
}