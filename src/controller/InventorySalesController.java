package controller;

import view.InventorySalesView;
import model.*;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 인벤토리 & 판매 관리 컨트롤러 - 수확/재고/판매 로직 담당
 * View는 UI만 담당하고, 이 클래스가 모든 비즈니스 로직을 처리
 */
public class InventorySalesController {
    private InventorySalesView view;
    private Map<String, PlantModel> plantModels;
    private List<HarvestRecord> harvestRecords;
    private List<SalesRecord> salesRecords;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.KOREA);
    
    public InventorySalesController(InventorySalesView view) {
        this.view = view;
        
        // 데이터 초기화
        initializeData();
        
        // UI 업데이트
        view.updatePlantCombos(plantModels.keySet());
        
        // 이벤트 리스너 설정
        setupEventListeners();
        
        // 초기 테이블 업데이트
        updateAllTables();
        updateSummaryInformation();
    }
    
    /**
     * 테스트 데이터 초기화
     */
    private void initializeData() {
        // 식물 모델 초기화
        plantModels = new HashMap<>();
        plantModels.put("딸기", new PlantModel("딸기-1", "딸기", "과일", 5000));
        plantModels.put("상추", new PlantModel("상추-1", "상추", "작물", 2000));
        plantModels.put("포도", new PlantModel("포도-1", "포도", "과일", 7000));
        plantModels.put("당근", new PlantModel("당근-1", "당근", "작물", 3000));
        plantModels.put("바질", new PlantModel("바질-1", "바질", "허브", 4000));
        plantModels.put("토마토", new PlantModel("토마토-1", "토마토", "채소", 3500));
        
        // 수확 기록 초기화 (더 많은 테스트 데이터)
        harvestRecords = new ArrayList<>();
        harvestRecords.add(new HarvestRecord("2025-04-15", "딸기", 25.0));
        harvestRecords.add(new HarvestRecord("2025-04-20", "상추", 15.0));
        harvestRecords.add(new HarvestRecord("2025-05-01", "딸기", 30.0));
        harvestRecords.add(new HarvestRecord("2025-05-02", "상추", 20.0));
        harvestRecords.add(new HarvestRecord("2025-05-05", "포도", 18.0));
        harvestRecords.add(new HarvestRecord("2025-05-10", "당근", 12.0));
        harvestRecords.add(new HarvestRecord("2025-05-15", "바질", 8.0));
        harvestRecords.add(new HarvestRecord("2025-05-18", "토마토", 22.0));
        
        // 판매 기록 초기화
        salesRecords = new ArrayList<>();
        salesRecords.add(new SalesRecord("2025-04-18", "딸기", 20.0, 5000));
        salesRecords.add(new SalesRecord("2025-04-25", "상추", 12.0, 2000));
        salesRecords.add(new SalesRecord("2025-05-03", "딸기", 15.0, 5000));
        salesRecords.add(new SalesRecord("2025-05-06", "상추", 18.0, 2000));
        salesRecords.add(new SalesRecord("2025-05-08", "포도", 10.0, 7000));
        salesRecords.add(new SalesRecord("2025-05-12", "당근", 8.0, 3000));
        salesRecords.add(new SalesRecord("2025-05-16", "바질", 5.0, 4000));
        salesRecords.add(new SalesRecord("2025-05-20", "토마토", 15.0, 3500));
    }
    
    /**
     * 모든 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        // 수확 탭 이벤트들
        setupHarvestEventListeners();
        
        // 재고 탭 이벤트들
        setupInventoryEventListeners();
        
        // 판매 탭 이벤트들
        setupSalesEventListeners();
        
        // 공통 이벤트들
        view.getCloseButton().addActionListener(e -> view.dispose());
    }
    
    /**
     * 수확 탭 이벤트 리스너 설정
     */
    private void setupHarvestEventListeners() {
        view.getAddHarvestButton().addActionListener(e -> addHarvestRecord());
        view.getHarvestDatePickerButton().addActionListener(e -> 
            UIUtils.showDatePicker(view, view.getHarvestDateField()));
        
        // 수확량 입력 시 실시간 검증
        view.getHarvestQuantityField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                validateHarvestInput();
            }
        });
    }
    
    /**
     * 재고 탭 이벤트 리스너 설정
     */
    private void setupInventoryEventListeners() {
        view.getSearchButton().addActionListener(e -> searchInventory());
        view.getClearSearchButton().addActionListener(e -> clearSearch());
        view.getAddInventoryButton().addActionListener(e -> showAddInventoryDialog());
        view.getRemoveInventoryButton().addActionListener(e -> removeInventory());
        view.getReportButton().addActionListener(e -> generateInventoryReport());
        
        // Enter 키로 검색
        view.getSearchField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchInventory();
                }
            }
        });
    }
    
    /**
     * 판매 탭 이벤트 리스너 설정
     */
    private void setupSalesEventListeners() {
        view.getSalePlantCombo().addActionListener(e -> updateSalePrice());
        view.getSaleDatePickerButton().addActionListener(e -> 
            UIUtils.showDatePicker(view, view.getSaleDateField()));
        view.getAddSalesButton().addActionListener(e -> addSalesRecord());
        view.getExportButton().addActionListener(e -> exportSalesToExcel());
        
        // 판매량 입력 시 총액 자동 계산
        view.getSaleQuantityField().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateSaleTotal();
                validateSalesInput();
            }
        });
    }
    
    /**
     * 수확 기록 추가
     */
    private void addHarvestRecord() {
        view.setLoading(true);
        
        try {
            // 입력 검증
            if (!validateHarvestInputComplete()) {
                return;
            }
            
            String date = view.getHarvestDateField().getText().trim();
            String plant = view.getSelectedHarvestPlant();
            double quantity = Double.parseDouble(view.getHarvestQuantityField().getText().trim());
            
            // 수확 기록 추가
            HarvestRecord record = new HarvestRecord(date, plant, quantity);
            harvestRecords.add(record);
            
            // 성공 메시지 및 UI 업데이트
            updateAllTables();
            updateSummaryInformation();
            view.clearInputFields();
            
            view.showTemporaryMessage("수확 기록이 추가되었습니다: " + plant + " " + quantity + "kg");
            
            UIUtils.showInfoMessage(view,
                "✅ 수확 기록이 성공적으로 추가되었습니다!\n\n" +
                "📅 날짜: " + date + "\n" +
                "🌱 식물: " + plant + "\n" +
                "⚖️ 수확량: " + quantity + "kg\n\n" +
                "💡 현재 총 재고량: " + getTotalStock(plant) + "kg",
                "수확 기록 추가");
            
        } catch (NumberFormatException ex) {
            UIUtils.showWarningMessage(view,
                "⚠️ 수확량은 올바른 숫자로 입력해주세요.",
                "입력 오류");
        } finally {
            view.setLoading(false);
        }
    }
    
    /**
     * 수확 입력 검증
     */
    private boolean validateHarvestInputComplete() {
        String date = view.getHarvestDateField().getText().trim();
        String plant = view.getSelectedHarvestPlant();
        String quantityStr = view.getHarvestQuantityField().getText().trim();
        
        boolean dateValid = UIUtils.isValidDate(date);
        boolean plantValid = plant != null;
        boolean quantityValid = UIUtils.isValidDouble(quantityStr) && 
                               Double.parseDouble(quantityStr) > 0;
        
        view.highlightHarvestFields(dateValid, plantValid, quantityValid);
        
        if (!dateValid) {
            UIUtils.showWarningMessage(view, "📅 올바른 날짜를 입력해주세요.", "날짜 오류");
            return false;
        }
        if (!plantValid) {
            UIUtils.showWarningMessage(view, "🌱 식물을 선택해주세요.", "식물 선택 오류");
            return false;
        }
        if (!quantityValid) {
            UIUtils.showWarningMessage(view, "⚖️ 수확량은 0보다 큰 숫자여야 합니다.", "수확량 오류");
            return false;
        }
        
        return true;
    }
    
    /**
     * 수확 입력 실시간 검증
     */
    private void validateHarvestInput() {
        String quantityStr = view.getHarvestQuantityField().getText().trim();
        boolean quantityValid = quantityStr.isEmpty() || 
                               (UIUtils.isValidDouble(quantityStr) && Double.parseDouble(quantityStr) > 0);
        
        view.highlightHarvestFields(true, true, quantityValid);
    }
    
    /**
     * 재고 검색
     */
    private void searchInventory() {
        String keyword = view.getSearchField().getText().trim().toLowerCase();
        
        if (keyword.isEmpty()) {
            updateInventoryTable();
            return;
        }
        
        view.getInventoryTableModel().setRowCount(0);
        
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        for (Map.Entry<String, PlantModel> entry : plantModels.entrySet()) {
            String plant = entry.getKey();
            PlantModel model = entry.getValue();
            
            // 식물명 또는 카테고리에 검색어가 포함되어 있는지 확인
            if (plant.toLowerCase().contains(keyword) || 
                model.getCategory().toLowerCase().contains(keyword)) {
                
                addInventoryRowToTable(plant, model, harvestTotals, salesTotals);
            }
        }
        
        if (view.getInventoryTableModel().getRowCount() == 0) {
            UIUtils.showInfoMessage(view, "🔍 '" + keyword + "'에 대한 검색 결과가 없습니다.", "검색 결과");
        } else {
            view.showTemporaryMessage("검색 완료: " + view.getInventoryTableModel().getRowCount() + "개 항목");
        }
    }
    
    /**
     * 검색 초기화
     */
    private void clearSearch() {
        view.getSearchField().setText("");
        updateInventoryTable();
        view.showTemporaryMessage("전체 재고 목록이 표시되었습니다");
    }
    
    /**
     * 인벤토리 추가 다이얼로그 표시
     */
    private void showAddInventoryDialog() {
        JDialog dialog = new JDialog(view, "새 식물 품목 추가", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(view);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 제목
        JLabel titleLabel = new JLabel("🌱 새 식물 품목 추가");
        titleLabel.setFont(new java.awt.Font("맑은 고딕", java.awt.Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // 입력 패널
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // 식물 이름
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("식물 이름:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        inputPanel.add(nameField, gbc);
        
        // 카테고리
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("카테고리:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
            "과일", "채소", "작물", "허브", "뿌리채소", "기타"
        });
        inputPanel.add(categoryCombo, gbc);
        
        // 단가
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("단가(원/kg):"), gbc);
        gbc.gridx = 1;
        JTextField priceField = new JTextField(20);
        inputPanel.add(priceField, gbc);
        
        // 설명
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("설명:"), gbc);
        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descArea);
        inputPanel.add(scrollPane, gbc);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton saveButton = new JButton("💾 저장");
        JButton cancelButton = new JButton("❌ 취소");
        
        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String category = (String) categoryCombo.getSelectedItem();
            String priceStr = priceField.getText().trim();
            
            if (name.isEmpty()) {
                UIUtils.showWarningMessage(dialog, "식물 이름을 입력해주세요.", "입력 오류");
                return;
            }
            
            if (plantModels.containsKey(name)) {
                UIUtils.showWarningMessage(dialog, 
                    "'" + name + "'은(는) 이미 등록된 식물입니다.", "중복 오류");
                return;
            }
            
            if (!UIUtils.isValidDouble(priceStr) || Double.parseDouble(priceStr) <= 0) {
                UIUtils.showWarningMessage(dialog, "올바른 단가를 입력해주세요.", "단가 오류");
                return;
            }
            
            double price = Double.parseDouble(priceStr);
            String id = name + "-" + (plantModels.size() + 1);
            PlantModel model = new PlantModel(id, name, category, price);
            
            plantModels.put(name, model);
            view.updatePlantCombos(plantModels.keySet());
            updateInventoryTable();
            updateSummaryInformation();
            
            UIUtils.showInfoMessage(dialog, 
                "✅ '" + name + "' 식물이 성공적으로 추가되었습니다!", 
                "추가 완료");
            dialog.dispose();
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    /**
     * 인벤토리 삭제
     */
    private void removeInventory() {
        String selectedPlant = view.getSelectedInventoryPlant();
        if (selectedPlant == null) {
            UIUtils.showWarningMessage(view, "🗑️ 삭제할 식물을 선택해주세요.", "선택 오류");
            return;
        }
        
        boolean confirm = UIUtils.showConfirmDialog(view,
            "🗑️ '" + selectedPlant + "' 품목을 정말 삭제하시겠습니까?\n\n" +
            "⚠️ 경고: 다음 데이터가 모두 삭제됩니다:\n" +
            "   • 모든 수확 기록\n" +
            "   • 모든 판매 기록\n" +
            "   • 재고 정보\n\n" +
            "이 작업은 되돌릴 수 없습니다.",
            "품목 삭제 확인");
        
        if (confirm) {
            // 관련 기록들 삭제
            harvestRecords.removeIf(record -> record.getPlant().equals(selectedPlant));
            salesRecords.removeIf(record -> record.getPlant().equals(selectedPlant));
            plantModels.remove(selectedPlant);
            
            // UI 업데이트
            view.updatePlantCombos(plantModels.keySet());
            updateAllTables();
            updateSummaryInformation();
            
            UIUtils.showInfoMessage(view,
                "✅ '" + selectedPlant + "' 품목이 성공적으로 삭제되었습니다.",
                "삭제 완료");
        }
    }
    
    /**
     * 인벤토리 보고서 생성
     */
    private void generateInventoryReport() {
        JDialog reportDialog = new JDialog(view, "📊 인벤토리 보고서", true);
        reportDialog.setSize(800, 700);
        reportDialog.setLocationRelativeTo(view);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 제목
        JLabel titleLabel = new JLabel("📊 인벤토리 종합 보고서");
        titleLabel.setFont(new java.awt.Font("맑은 고딕", java.awt.Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // 보고서 내용
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new java.awt.Font("맑은 고딕", java.awt.Font.PLAIN, 11));
        
        String reportContent = generateReportContent();
        reportArea.setText(reportContent);
        
        JScrollPane scrollPane = new JScrollPane(reportArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton printButton = new JButton("🖨️ 인쇄");
        printButton.addActionListener(e -> {
            UIUtils.showInfoMessage(reportDialog, 
                "인쇄 기능은 향후 업데이트에서 제공될 예정입니다.", "안내");
        });
        
        JButton saveButton = new JButton("💾 파일로 저장");
        saveButton.addActionListener(e -> saveReportToFile(reportContent));
        
        JButton closeButton = new JButton("❌ 닫기");
        closeButton.addActionListener(e -> reportDialog.dispose());
        
        buttonPanel.add(printButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        reportDialog.add(mainPanel);
        reportDialog.setVisible(true);
    }
    
    /**
     * 보고서 내용 생성
     */
    private String generateReportContent() {
        StringBuilder report = new StringBuilder();
        
        report.append("═══════════════════════════════════════════════════════════════════\n");
        report.append("                           📊 인벤토리 종합 보고서\n");
        report.append("═══════════════════════════════════════════════════════════════════\n\n");
        report.append("📅 생성일시: ").append(getCurrentDateTime()).append("\n");
        report.append("📋 보고서 유형: 전체 재고 현황 및 거래 내역\n\n");
        
        // 1. 요약 정보
        report.append("📊 요약 정보\n");
        report.append("─────────────────────────────────────────────────────────────────\n");
        report.append("총 관리 품목 수: ").append(plantModels.size()).append("개\n");
        report.append("총 재고 가치: ").append(currencyFormat.format(calculateTotalInventoryValue())).append("\n");
        report.append("이번 달 총 수확량: ").append(calculateCurrentMonthHarvest()).append(" kg\n");
        report.append("이번 달 총 판매액: ").append(currencyFormat.format(calculateCurrentMonthSales())).append("\n\n");
        
        // 2. 품목별 상세 정보
        report.append("🌱 품목별 상세 현황\n");
        report.append("─────────────────────────────────────────────────────────────────\n");
        
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        for (Map.Entry<String, PlantModel> entry : plantModels.entrySet()) {
            String plant = entry.getKey();
            PlantModel model = entry.getValue();
            
            double harvestTotal = harvestTotals.getOrDefault(plant, 0.0);
            double salesTotal = salesTotals.getOrDefault(plant, 0.0);
            double currentStock = harvestTotal - salesTotal;
            double stockValue = currentStock * model.getPrice();
            
            report.append(String.format("🌿 %s (%s)\n", plant, model.getCategory()));
            report.append(String.format("   ├ 누적 수확량: %.1f kg\n", harvestTotal));
            report.append(String.format("   ├ 누적 판매량: %.1f kg\n", salesTotal));
            report.append(String.format("   ├ 현재 재고량: %.1f kg\n", currentStock));
            report.append(String.format("   ├ 단가: %s/kg\n", currencyFormat.format(model.getPrice())));
            report.append(String.format("   └ 재고 가치: %s\n\n", currencyFormat.format(stockValue)));
        }
        
        // 3. 월별 수확 현황
        report.append("📈 월별 수확 현황 (최근 3개월)\n");
        report.append("─────────────────────────────────────────────────────────────────\n");
        appendMonthlyHarvestData(report);
        
        // 4. 월별 판매 현황
        report.append("💰 월별 판매 현황 (최근 3개월)\n");
        report.append("─────────────────────────────────────────────────────────────────\n");
        appendMonthlySalesData(report);
        
        // 5. 권장사항
        report.append("💡 권장사항 및 개선점\n");
        report.append("─────────────────────────────────────────────────────────────────\n");
        appendRecommendations(report);
        
        report.append("\n═══════════════════════════════════════════════════════════════════\n");
        report.append("                              보고서 끝\n");
        report.append("═══════════════════════════════════════════════════════════════════");
        
        return report.toString();
    }
    
    /**
     * 판매 단가 업데이트
     */
    private void updateSalePrice() {
        String selectedPlant = view.getSelectedSalesPlant();
        if (selectedPlant != null && plantModels.containsKey(selectedPlant)) {
            PlantModel model = plantModels.get(selectedPlant);
            view.updateSalePrice(String.valueOf(model.getPrice()));
            updateSaleTotal();
            checkStockAvailability();
        }
    }
    
    /**
     * 판매 총액 업데이트
     */
    private void updateSaleTotal() {
        try {
            String quantityStr = view.getSaleQuantityField().getText().trim();
            String priceStr = view.getSalePriceField().getText().trim();
            
            if (quantityStr.isEmpty() || priceStr.isEmpty()) {
                view.updateSaleTotal("");
                return;
            }
            
            double quantity = Double.parseDouble(quantityStr);
            double price = Double.parseDouble(priceStr);
            double total = quantity * price;
            
            view.updateSaleTotal(currencyFormat.format(total));
        } catch (NumberFormatException e) {
            view.updateSaleTotal("");
        }
    }
    
    /**
     * 재고 가용성 확인
     */
    private void checkStockAvailability() {
        String selectedPlant = view.getSelectedSalesPlant();
        String quantityStr = view.getSaleQuantityField().getText().trim();
        
        if (selectedPlant == null || quantityStr.isEmpty()) {
            view.clearStockWarning();
            return;
        }
        
        try {
            double requestedQuantity = Double.parseDouble(quantityStr);
            double availableStock = getAvailableStock(selectedPlant);
            
            if (requestedQuantity > availableStock) {
                view.showStockWarning(String.format(
                    "재고 부족! 요청량: %.1fkg, 재고량: %.1fkg", 
                    requestedQuantity, availableStock));
            } else {
                view.clearStockWarning();
            }
        } catch (NumberFormatException e) {
            view.clearStockWarning();
        }
    }
    
    /**
     * 판매 기록 추가
     */
    private void addSalesRecord() {
        view.setLoading(true);
        
        try {
            // 입력 검증
            if (!validateSalesInputComplete()) {
                return;
            }
            
            String date = view.getSaleDateField().getText().trim();
            String plant = view.getSelectedSalesPlant();
            double quantity = Double.parseDouble(view.getSaleQuantityField().getText().trim());
            double price = Double.parseDouble(view.getSalePriceField().getText().trim());
            
            // 재고 확인
            double availableStock = getAvailableStock(plant);
            if (quantity > availableStock) {
                UIUtils.showWarningMessage(view,
                    "⚠️ 판매량이 현재 재고량을 초과합니다!\n\n" +
                    "요청 판매량: " + quantity + "kg\n" +
                    "현재 재고량: " + availableStock + "kg\n\n" +
                    "재고량 이하로 조정해주세요.",
                    "재고 부족");
                return;
            }
            
            // 판매 기록 추가
            SalesRecord record = new SalesRecord(date, plant, quantity, price);
            salesRecords.add(record);
            
            // UI 업데이트
            updateAllTables();
            updateSummaryInformation();
            view.clearInputFields();
            
            view.showTemporaryMessage("판매 기록이 추가되었습니다: " + plant + " " + quantity + "kg");
            
            UIUtils.showInfoMessage(view,
                "✅ 판매 기록이 성공적으로 추가되었습니다!\n\n" +
                "📅 날짜: " + date + "\n" +
                "🌱 식물: " + plant + "\n" +
                "⚖️ 판매량: " + quantity + "kg\n" +
                "💰 단가: " + currencyFormat.format(price) + "/kg\n" +
                "💵 총액: " + currencyFormat.format(quantity * price) + "\n\n" +
                "📦 남은 재고량: " + (availableStock - quantity) + "kg",
                "판매 기록 추가");
            
        } catch (NumberFormatException ex) {
            UIUtils.showWarningMessage(view,
                "⚠️ 판매량과 단가는 올바른 숫자로 입력해주세요.",
                "입력 오류");
        } finally {
            view.setLoading(false);
        }
    }
    
    /**
     * 판매 입력 검증
     */
    private boolean validateSalesInputComplete() {
        String date = view.getSaleDateField().getText().trim();
        String plant = view.getSelectedSalesPlant();
        String quantityStr = view.getSaleQuantityField().getText().trim();
        
        boolean dateValid = UIUtils.isValidDate(date);
        boolean plantValid = plant != null;
        boolean quantityValid = UIUtils.isValidDouble(quantityStr) && 
                               Double.parseDouble(quantityStr) > 0;
        
        view.highlightSalesFields(dateValid, plantValid, quantityValid);
        
        if (!dateValid) {
            UIUtils.showWarningMessage(view, "📅 올바른 날짜를 입력해주세요.", "날짜 오류");
            return false;
        }
        if (!plantValid) {
            UIUtils.showWarningMessage(view, "🌱 식물을 선택해주세요.", "식물 선택 오류");
            return false;
        }
        if (!quantityValid) {
            UIUtils.showWarningMessage(view, "⚖️ 판매량은 0보다 큰 숫자여야 합니다.", "판매량 오류");
            return false;
        }
        
        return true;
    }
    
    /**
     * 판매 입력 실시간 검증
     */
    private void validateSalesInput() {
        String quantityStr = view.getSaleQuantityField().getText().trim();
        boolean quantityValid = quantityStr.isEmpty() || 
                               (UIUtils.isValidDouble(quantityStr) && Double.parseDouble(quantityStr) > 0);
        
        view.highlightSalesFields(true, true, quantityValid);
        checkStockAvailability();
    }
    
    /**
     * 엑셀 내보내기
     */
    private void exportSalesToExcel() {
        UIUtils.showInfoMessage(view,
            "📊 엑셀 내보내기 기능은 향후 업데이트에서 제공될 예정입니다.\n\n" +
            "현재는 '보고서 생성' 기능을 통해 텍스트 파일로 저장할 수 있습니다.",
            "기능 안내");
    }
    
    // ========== 테이블 업데이트 메소드들 ==========
    
    /**
     * 모든 테이블 업데이트
     */
    private void updateAllTables() {
        updateHarvestTable();
        updateInventoryTable();
        updateSalesTable();
        view.updateTableCounts();
    }
    
    /**
     * 수확 테이블 업데이트
     */
    private void updateHarvestTable() {
        view.getHarvestTableModel().setRowCount(0);
        
        // 날짜순으로 정렬
        harvestRecords.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        Map<String, Double> cumulativeHarvest = new HashMap<>();
        
        for (HarvestRecord record : harvestRecords) {
            String plant = record.getPlant();
            double cumulative = cumulativeHarvest.getOrDefault(plant, 0.0) + record.getQuantity();
            cumulativeHarvest.put(plant, cumulative);
            
            Object[] row = {
                record.getDate(),
                record.getPlant(),
                String.format("%.1f", record.getQuantity()),
                String.format("%.1f", cumulative)
            };
            view.getHarvestTableModel().addRow(row);
        }
    }
    
    /**
     * 재고 테이블 업데이트
     */
    private void updateInventoryTable() {
        view.getInventoryTableModel().setRowCount(0);
        
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        for (Map.Entry<String, PlantModel> entry : plantModels.entrySet()) {
            String plant = entry.getKey();
            PlantModel model = entry.getValue();
            addInventoryRowToTable(plant, model, harvestTotals, salesTotals);
        }
    }
    
    /**
     * 재고 테이블에 행 추가
     */
    private void addInventoryRowToTable(String plant, PlantModel model, 
                                      Map<String, Double> harvestTotals, 
                                      Map<String, Double> salesTotals) {
        double harvestTotal = harvestTotals.getOrDefault(plant, 0.0);
        double salesTotal = salesTotals.getOrDefault(plant, 0.0);
        double currentStock = harvestTotal - salesTotal;
        double stockValue = currentStock * model.getPrice();
        
        Object[] row = {
            plant,
            model.getCategory(),
            String.format("%.1f", harvestTotal),
            String.format("%.1f", salesTotal),
            String.format("%.1f", currentStock),
            currencyFormat.format(model.getPrice()),
            currencyFormat.format(stockValue)
        };
        
        view.getInventoryTableModel().addRow(row);
    }
    
    /**
     * 판매 테이블 업데이트
     */
    private void updateSalesTable() {
        view.getSalesTableModel().setRowCount(0);
        
        // 날짜순으로 정렬 (최신순)
        salesRecords.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        
        for (SalesRecord record : salesRecords) {
            Object[] row = {
                record.getDate(),
                record.getPlant(),
                String.format("%.1f", record.getQuantity()),
                currencyFormat.format(record.getPrice()),
                currencyFormat.format(record.getTotal())
            };
            view.getSalesTableModel().addRow(row);
        }
    }
    
    // ========== 계산 메소드들 ==========
    
    /**
     * 식물별 총 수확량 계산
     */
    private Map<String, Double> calculateHarvestTotals() {
        Map<String, Double> totals = new HashMap<>();
        
        for (HarvestRecord record : harvestRecords) {
            String plant = record.getPlant();
            double quantity = record.getQuantity();
            totals.put(plant, totals.getOrDefault(plant, 0.0) + quantity);
        }
        
        return totals;
    }
    
    /**
     * 식물별 총 판매량 계산
     */
    private Map<String, Double> calculateSalesTotals() {
        Map<String, Double> totals = new HashMap<>();
        
        for (SalesRecord record : salesRecords) {
            String plant = record.getPlant();
            double quantity = record.getQuantity();
            totals.put(plant, totals.getOrDefault(plant, 0.0) + quantity);
        }
        
        return totals;
    }
    
    /**
     * 특정 식물의 가용 재고량 계산
     */
    private double getAvailableStock(String plant) {
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        double harvested = harvestTotals.getOrDefault(plant, 0.0);
        double sold = salesTotals.getOrDefault(plant, 0.0);
        
        return Math.max(0, harvested - sold);
    }
    
    /**
     * 특정 식물의 총 재고량 계산
     */
    private double getTotalStock(String plant) {
        return calculateHarvestTotals().getOrDefault(plant, 0.0);
    }
    
    /**
     * 총 재고 가치 계산
     */
    private double calculateTotalInventoryValue() {
        double totalValue = 0.0;
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        for (Map.Entry<String, PlantModel> entry : plantModels.entrySet()) {
            String plant = entry.getKey();
            PlantModel model = entry.getValue();
            
            double currentStock = harvestTotals.getOrDefault(plant, 0.0) - 
                                 salesTotals.getOrDefault(plant, 0.0);
            totalValue += currentStock * model.getPrice();
        }
        
        return totalValue;
    }
    
    /**
     * 이번 달 총 수확량 계산
     */
    private double calculateCurrentMonthHarvest() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        
        return harvestRecords.stream()
                .filter(record -> {
                    try {
                        Date date = dateFormat.parse(record.getDate());
                        cal.setTime(date);
                        return cal.get(Calendar.MONTH) == currentMonth && 
                               cal.get(Calendar.YEAR) == currentYear;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .mapToDouble(HarvestRecord::getQuantity)
                .sum();
    }
    
    /**
     * 이번 달 총 판매액 계산
     */
    private double calculateCurrentMonthSales() {
        Calendar cal = Calendar.getInstance();
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        
        return salesRecords.stream()
                .filter(record -> {
                    try {
                        Date date = dateFormat.parse(record.getDate());
                        cal.setTime(date);
                        return cal.get(Calendar.MONTH) == currentMonth && 
                               cal.get(Calendar.YEAR) == currentYear;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .mapToDouble(SalesRecord::getTotal)
                .sum();
    }
    
    // ========== 요약 정보 업데이트 ==========
    
    /**
     * 요약 정보 업데이트
     */
    private void updateSummaryInformation() {
        String harvestSummary = "이번 달 총 수확량: " + 
                               String.format("%.1f kg", calculateCurrentMonthHarvest());
        
        String salesSummary = "이번 달 총 판매액: " + 
                             currencyFormat.format(calculateCurrentMonthSales());
        
        String inventoryValue = "총 재고 가치: " + 
                               currencyFormat.format(calculateTotalInventoryValue());
        
        view.updateSummaryLabels(harvestSummary, salesSummary, inventoryValue);
    }
    
    // ========== 보고서 관련 메소드들 ==========
    
    /**
     * 월별 수확 데이터 추가
     */
    private void appendMonthlyHarvestData(StringBuilder report) {
        Calendar cal = Calendar.getInstance();
        
        for (int i = 2; i >= 0; i--) {
            cal.add(Calendar.MONTH, -i);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            
            double monthlyHarvest = harvestRecords.stream()
                    .filter(record -> {
                        try {
                            Date date = dateFormat.parse(record.getDate());
                            Calendar recordCal = Calendar.getInstance();
                            recordCal.setTime(date);
                            return recordCal.get(Calendar.MONTH) == month && 
                                   recordCal.get(Calendar.YEAR) == year;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .mapToDouble(HarvestRecord::getQuantity)
                    .sum();
            
            report.append(String.format("%d년 %d월: %.1f kg\n", year, month + 1, monthlyHarvest));
            cal = Calendar.getInstance(); // 리셋
        }
        report.append("\n");
    }
    
    /**
     * 월별 판매 데이터 추가
     */
    private void appendMonthlySalesData(StringBuilder report) {
        Calendar cal = Calendar.getInstance();
        
        for (int i = 2; i >= 0; i--) {
            cal.add(Calendar.MONTH, -i);
            int month = cal.get(Calendar.MONTH);
            int year = cal.get(Calendar.YEAR);
            
            double monthlySales = salesRecords.stream()
                    .filter(record -> {
                        try {
                            Date date = dateFormat.parse(record.getDate());
                            Calendar recordCal = Calendar.getInstance();
                            recordCal.setTime(date);
                            return recordCal.get(Calendar.MONTH) == month && 
                                   recordCal.get(Calendar.YEAR) == year;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .mapToDouble(SalesRecord::getTotal)
                    .sum();
            
            report.append(String.format("%d년 %d월: %s\n", year, month + 1, currencyFormat.format(monthlySales)));
            cal = Calendar.getInstance(); // 리셋
        }
        report.append("\n");
    }
    
    /**
     * 권장사항 추가
     */
    private void appendRecommendations(StringBuilder report) {
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        // 재고 부족 품목
        List<String> lowStockItems = new ArrayList<>();
        // 과재고 품목
        List<String> overStockItems = new ArrayList<>();
        // 판매 실적 우수 품목
        String bestSeller = "";
        double maxSales = 0;
        
        for (Map.Entry<String, PlantModel> entry : plantModels.entrySet()) {
            String plant = entry.getKey();
            double currentStock = harvestTotals.getOrDefault(plant, 0.0) - 
                                 salesTotals.getOrDefault(plant, 0.0);
            double totalSalesValue = salesTotals.getOrDefault(plant, 0.0) * entry.getValue().getPrice();
            
            // 재고 분석
            if (currentStock < 5.0) {
                lowStockItems.add(plant);
            } else if (currentStock > 50.0) {
                overStockItems.add(plant);
            }
            
            // 최고 판매 품목
            if (totalSalesValue > maxSales) {
                maxSales = totalSalesValue;
                bestSeller = plant;
            }
        }
        
        // 권장사항 작성
        if (!lowStockItems.isEmpty()) {
            report.append("⚠️  재고 부족 품목: ").append(String.join(", ", lowStockItems)).append("\n");
            report.append("   → 추가 재배 또는 수확을 고려하세요.\n\n");
        }
        
        if (!overStockItems.isEmpty()) {
            report.append("📦 과재고 품목: ").append(String.join(", ", overStockItems)).append("\n");
            report.append("   → 마케팅 강화 또는 가격 조정을 고려하세요.\n\n");
        }
        
        if (!bestSeller.isEmpty()) {
            report.append("🏆 최고 판매 품목: ").append(bestSeller).append("\n");
            report.append("   → 해당 품목의 재배 확대를 고려하세요.\n\n");
        }
        
        report.append("💡 일반 권장사항:\n");
        report.append("   • 정기적인 재고 점검으로 효율적인 관리를 하세요.\n");
        report.append("   • 계절별 수요 변화를 고려한 재배 계획을 세우세요.\n");
        report.append("   • 고수익 품목의 비중을 늘려보세요.\n");
        report.append("   • 판매 채널 다양화를 통해 판매 기회를 확대하세요.\n");
    }
    
    /**
     * 보고서 파일로 저장
     */
    private void saveReportToFile(String content) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("보고서 저장");
        fileChooser.setSelectedFile(new java.io.File("inventory_report_" + 
            new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date()) + ".txt"));
        
        int result = fileChooser.showSaveDialog(view);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            
            try (java.io.BufferedWriter writer = new java.io.BufferedWriter(
                    new java.io.FileWriter(file, java.nio.charset.StandardCharsets.UTF_8))) {
                writer.write(content);
                
                UIUtils.showInfoMessage(view,
                    "✅ 보고서가 성공적으로 저장되었습니다!\n\n" +
                    "📁 파일 위치: " + file.getAbsolutePath(),
                    "저장 완료");
            } catch (Exception e) {
                UIUtils.showErrorMessage(view,
                    "❌ 파일 저장 중 오류가 발생했습니다:\n" + e.getMessage(),
                    "저장 실패");
            }
        }
    }
    
    // ========== 유틸리티 메소드들 ==========
    
    /**
     * 현재 날짜시간 반환
     */
    private String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
    
    // ========== 공개 메소드들 (다른 컨트롤러에서 호출 가능) ==========
    
    /**
     * 새 식물 품목 추가 (다른 컨트롤러에서 호출)
     */
    public void addNewPlantModel(String name, String category, double price) {
        if (!plantModels.containsKey(name)) {
            String id = name + "-" + (plantModels.size() + 1);
            PlantModel model = new PlantModel(id, name, category, price);
            plantModels.put(name, model);
            
            view.updatePlantCombos(plantModels.keySet());
            updateInventoryTable();
            updateSummaryInformation();
        }
    }
    
    /**
     * 식물 품목 삭제 (다른 컨트롤러에서 호출)
     */
    public boolean removePlantModel(String plantName) {
        if (plantModels.containsKey(plantName)) {
            // 관련 기록들도 함께 삭제
            harvestRecords.removeIf(record -> record.getPlant().equals(plantName));
            salesRecords.removeIf(record -> record.getPlant().equals(plantName));
            plantModels.remove(plantName);
            
            view.updatePlantCombos(plantModels.keySet());
            updateAllTables();
            updateSummaryInformation();
            
            return true;
        }
        return false;
    }
    
    /**
     * 수확 기록 추가 (다른 컨트롤러에서 호출)
     */
    public void addHarvestRecordExternal(String date, String plant, double quantity) {
        if (plantModels.containsKey(plant)) {
            HarvestRecord record = new HarvestRecord(date, plant, quantity);
            harvestRecords.add(record);
            updateAllTables();
            updateSummaryInformation();
        }
    }
    
    /**
     * 판매 기록 추가 (다른 컨트롤러에서 호출)
     */
    public boolean addSalesRecordExternal(String date, String plant, double quantity, double price) {
        if (plantModels.containsKey(plant)) {
            double availableStock = getAvailableStock(plant);
            if (quantity <= availableStock) {
                SalesRecord record = new SalesRecord(date, plant, quantity, price);
                salesRecords.add(record);
                updateAllTables();
                updateSummaryInformation();
                return true;
            }
        }
        return false;
    }
    
    /**
     * 특정 탭으로 전환 (다른 컨트롤러에서 호출)
     */
    public void switchToTab(int tabIndex) {
        view.switchToTab(tabIndex);
        view.focusOnFirstField();
    }
    
    /**
     * 현재 재고 현황 반환 (다른 컨트롤러에서 호출)
     */
    public Map<String, Double> getCurrentStockLevels() {
        Map<String, Double> stockLevels = new HashMap<>();
        Map<String, Double> harvestTotals = calculateHarvestTotals();
        Map<String, Double> salesTotals = calculateSalesTotals();
        
        for (String plant : plantModels.keySet()) {
            double currentStock = harvestTotals.getOrDefault(plant, 0.0) - 
                                 salesTotals.getOrDefault(plant, 0.0);
            stockLevels.put(plant, currentStock);
        }
        
        return stockLevels;
    }
    
    /**
     * 재고 부족 품목 목록 반환 (다른 컨트롤러에서 호출)
     */
    public List<String> getLowStockItems(double threshold) {
        return getCurrentStockLevels().entrySet().stream()
                .filter(entry -> entry.getValue() < threshold)
                .map(Map.Entry::getKey)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * 인벤토리 요약 정보 반환 (다른 컨트롤러에서 호출)
     */
    public InventorySummary getInventorySummary() {
        return new InventorySummary(
            plantModels.size(),
            harvestRecords.size(),
            salesRecords.size(),
            calculateTotalInventoryValue(),
            calculateCurrentMonthHarvest(),
            calculateCurrentMonthSales()
        );
    }
    
    /**
     * 인벤토리 요약 정보 클래스
     */
    public static class InventorySummary {
        public final int totalPlantTypes;
        public final int totalHarvestRecords;
        public final int totalSalesRecords;
        public final double totalInventoryValue;
        public final double currentMonthHarvest;
        public final double currentMonthSales;
        
        public InventorySummary(int plantTypes, int harvestRecords, int salesRecords,
                               double inventoryValue, double monthHarvest, double monthSales) {
            this.totalPlantTypes = plantTypes;
            this.totalHarvestRecords = harvestRecords;
            this.totalSalesRecords = salesRecords;
            this.totalInventoryValue = inventoryValue;
            this.currentMonthHarvest = monthHarvest;
            this.currentMonthSales = monthSales;
        }
    }
}