package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * 인벤토리 & 판매 통합 관리 뷰 - UI만 담당
 * Controller가 이벤트를 처리하고, 이 클래스는 화면 표시만 담당
 */
public class InventorySalesView extends JFrame {
    
    private JTabbedPane tabbedPane;
    
    // ========== 수확 탭 컴포넌트들 ==========
    private JTextField harvestDateField;
    private JComboBox<String> harvestPlantCombo;
    private JTextField harvestQuantityField;
    private JTable harvestTable;
    private DefaultTableModel harvestTableModel;
    private JButton addHarvestButton;
    private JButton harvestDatePickerButton;
    private JLabel harvestSummaryLabel;
    
    // ========== 재고 탭 컴포넌트들 ==========
    private JTable inventoryTable;
    private DefaultTableModel inventoryTableModel;
    private JTextField searchField;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JButton addInventoryButton;
    private JButton removeInventoryButton;
    private JButton reportButton;
    private JLabel inventoryValueLabel;
    
    // ========== 판매 탭 컴포넌트들 ==========
    private JTextField saleDateField;
    private JComboBox<String> salePlantCombo;
    private JTextField saleQuantityField;
    private JTextField salePriceField;
    private JTextField saleTotalField;
    private JTable salesTable;
    private DefaultTableModel salesTableModel;
    private JButton addSalesButton;
    private JButton saleDatePickerButton;
    private JButton exportButton;
    private JLabel salesSummaryLabel;
    private JLabel stockWarningLabel;
    
    // ========== 공통 컴포넌트들 ==========
    private JButton closeButton;
    
    public InventorySalesView() {
        initializeUI();
    }
    
    /**
     * UI 초기화 - 모든 화면 구성 요소 생성
     */
    private void initializeUI() {
        setTitle("📦 인벤토리 & 판매 통합 관리");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 탭 패널 생성
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        
        // 각 탭 추가
        tabbedPane.addTab("수확 기록", createHarvestTab());
        tabbedPane.addTab("재고 현황", createInventoryTab());
        tabbedPane.addTab("판매 기록", createSalesTab());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // 하단 공통 버튼 패널
        JPanel bottomPanel = createBottomPanel();
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * 수확 탭 생성
     */
    private JPanel createHarvestTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 상단: 수확 정보 입력 패널
        JPanel inputPanel = createHarvestInputPanel();
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // 중앙: 수확 기록 테이블
        JPanel tablePanel = createHarvestTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * 수확 정보 입력 패널 생성
     */
    private JPanel createHarvestInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("수확 정보 입력"));
        
        // 입력 필드들
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 수확일
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel harvestDateLabel = new JLabel("수확일:");
        harvestDateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(harvestDateLabel, gbc);
        
        gbc.gridx = 1;
        harvestDateField = new JTextField(12);
        harvestDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        harvestDateField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(harvestDateField, gbc);
        
        gbc.gridx = 2;
        harvestDatePickerButton = new JButton("📅");
        harvestDatePickerButton.setPreferredSize(new Dimension(35, 25));
        harvestDatePickerButton.setToolTipText("날짜 선택");
        fieldsPanel.add(harvestDatePickerButton, gbc);
        
        // 식물 선택
        gbc.gridx = 3; gbc.gridy = 0;
        JLabel harvestPlantLabel = new JLabel("식물:");
        harvestPlantLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(harvestPlantLabel, gbc);
        
        gbc.gridx = 4;
        harvestPlantCombo = new JComboBox<>();
        harvestPlantCombo.setPreferredSize(new Dimension(120, 25));
        harvestPlantCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(harvestPlantCombo, gbc);
        
        // 수확량
        gbc.gridx = 5; gbc.gridy = 0;
        JLabel quantityLabel = new JLabel("수확량(kg):");
        quantityLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(quantityLabel, gbc);
        
        gbc.gridx = 6;
        harvestQuantityField = new JTextField(8);
        harvestQuantityField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(harvestQuantityField, gbc);
        
        // 추가 버튼
        gbc.gridx = 7; gbc.gridy = 0;
        addHarvestButton = new JButton("➕ 추가");
        addHarvestButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        addHarvestButton.setPreferredSize(new Dimension(70, 30));
        addHarvestButton.setBackground(new Color(144, 238, 144));
        fieldsPanel.add(addHarvestButton, gbc);
        
        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // 요약 정보 패널
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        harvestSummaryLabel = new JLabel("이번 달 총 수확량: 0 kg");
        harvestSummaryLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 11));
        harvestSummaryLabel.setForeground(new Color(34, 139, 34));
        summaryPanel.add(harvestSummaryLabel);
        inputPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        return inputPanel;
    }
    
    /**
     * 수확 기록 테이블 패널 생성
     */
    private JPanel createHarvestTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder(" 수확 기록"));
        
        // 테이블 생성
        String[] harvestColumns = {"날짜", "식물", "수확량(kg)", "누적 수확량(kg)"};
        harvestTableModel = new DefaultTableModel(harvestColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        harvestTable = new JTable(harvestTableModel);
        harvestTable.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        harvestTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 11));
        harvestTable.setRowHeight(25);
        harvestTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(harvestTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * 재고 탭 생성
     */
    private JPanel createInventoryTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 상단: 검색 및 요약 패널
        JPanel topPanel = createInventoryTopPanel();
        panel.add(topPanel, BorderLayout.NORTH);
        
        // 중앙: 재고 테이블
        JPanel tablePanel = createInventoryTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        // 하단: 버튼 패널
        JPanel buttonPanel = createInventoryButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * 재고 상단 패널 생성 (검색 및 요약)
     */
    private JPanel createInventoryTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // 검색 패널
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("검색"));
        
        searchPanel.add(new JLabel("검색어:"));
        searchField = new JTextField(20);
        searchField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        searchPanel.add(searchField);
        
        searchButton = new JButton("검색");
        searchButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        searchPanel.add(searchButton);
        
        clearSearchButton = new JButton("전체");
        clearSearchButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        searchPanel.add(clearSearchButton);
        
        // 요약 정보 패널
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        inventoryValueLabel = new JLabel("총 재고 가치: ₩0");
        inventoryValueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        inventoryValueLabel.setForeground(new Color(0, 100, 0));
        summaryPanel.add(inventoryValueLabel);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(summaryPanel, BorderLayout.EAST);
        
        return topPanel;
    }
    
    /**
     * 재고 테이블 패널 생성
     */
    private JPanel createInventoryTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("재고 현황"));
        
        // 테이블 생성
        String[] inventoryColumns = {"식물", "유형", "총 수확량(kg)", "총 판매량(kg)", 
                                    "현재 재고(kg)", "단가(원)", "재고 가치(원)"};
        inventoryTableModel = new DefaultTableModel(inventoryColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        inventoryTable = new JTable(inventoryTableModel);
        inventoryTable.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        inventoryTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 11));
        inventoryTable.setRowHeight(25);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 컬럼 너비 조정
        inventoryTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // 식물
        inventoryTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // 유형
        inventoryTable.getColumnModel().getColumn(2).setPreferredWidth(90);  // 총 수확량
        inventoryTable.getColumnModel().getColumn(3).setPreferredWidth(90);  // 총 판매량
        inventoryTable.getColumnModel().getColumn(4).setPreferredWidth(90);  // 현재 재고
        inventoryTable.getColumnModel().getColumn(5).setPreferredWidth(70);  // 단가
        inventoryTable.getColumnModel().getColumn(6).setPreferredWidth(100); // 재고 가치
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * 재고 버튼 패널 생성
     */
    private JPanel createInventoryButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        addInventoryButton = new JButton("인벤토리 추가");
        addInventoryButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        addInventoryButton.setBackground(new Color(173, 216, 230));
        
        removeInventoryButton = new JButton("인벤토리 삭제");
        removeInventoryButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        removeInventoryButton.setForeground(Color.RED);
        
        reportButton = new JButton("보고서 생성");
        reportButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        reportButton.setBackground(new Color(255, 218, 185));
        
        buttonPanel.add(addInventoryButton);
        buttonPanel.add(removeInventoryButton);
        buttonPanel.add(reportButton);
        
        return buttonPanel;
    }
    
    /**
     * 판매 탭 생성
     */
    private JPanel createSalesTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 상단: 판매 정보 입력 패널
        JPanel inputPanel = createSalesInputPanel();
        panel.add(inputPanel, BorderLayout.NORTH);
        
        // 중앙: 판매 기록 테이블
        JPanel tablePanel = createSalesTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        
        // 하단: 엑셀 내보내기 버튼
        JPanel exportPanel = createSalesExportPanel();
        panel.add(exportPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * 판매 정보 입력 패널 생성
     */
    private JPanel createSalesInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("판매 정보 입력"));
        
        // 입력 필드들
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 첫 번째 행: 판매일, 식물
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel saleDateLabel = new JLabel("판매일:");
        saleDateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(saleDateLabel, gbc);
        
        gbc.gridx = 1;
        saleDateField = new JTextField(12);
        saleDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        saleDateField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(saleDateField, gbc);
        
        gbc.gridx = 2;
        saleDatePickerButton = new JButton("📅");
        saleDatePickerButton.setPreferredSize(new Dimension(35, 25));
        saleDatePickerButton.setToolTipText("날짜 선택");
        fieldsPanel.add(saleDatePickerButton, gbc);
        
        gbc.gridx = 3;
        JLabel salePlantLabel = new JLabel("식물:");
        salePlantLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(salePlantLabel, gbc);
        
        gbc.gridx = 4;
        salePlantCombo = new JComboBox<>();
        salePlantCombo.setPreferredSize(new Dimension(120, 25));
        salePlantCombo.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(salePlantCombo, gbc);
        
        // 두 번째 행: 판매량, 단가, 총액
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel saleQuantityLabel = new JLabel("판매량(kg):");
        saleQuantityLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(saleQuantityLabel, gbc);
        
        gbc.gridx = 1;
        saleQuantityField = new JTextField(8);
        saleQuantityField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(saleQuantityField, gbc);
        
        gbc.gridx = 2;
        JLabel salePriceLabel = new JLabel("단가(원):");
        salePriceLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(salePriceLabel, gbc);
        
        gbc.gridx = 3;
        salePriceField = new JTextField(10);
        salePriceField.setEditable(false);
        salePriceField.setBackground(Color.LIGHT_GRAY);
        salePriceField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(salePriceField, gbc);
        
        gbc.gridx = 4;
        JLabel saleTotalLabel = new JLabel("총액(원):");
        saleTotalLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(saleTotalLabel, gbc);
        
        gbc.gridx = 5;
        saleTotalField = new JTextField(10);
        saleTotalField.setEditable(false);
        saleTotalField.setBackground(Color.LIGHT_GRAY);
        saleTotalField.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        saleTotalField.setForeground(Color.BLUE);
        fieldsPanel.add(saleTotalField, gbc);
        
        // 추가 버튼
        gbc.gridx = 6; gbc.gridy = 1;
        addSalesButton = new JButton("판매 기록 추가");
        addSalesButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        addSalesButton.setPreferredSize(new Dimension(120, 30));
        addSalesButton.setBackground(new Color(255, 215, 0));
        fieldsPanel.add(addSalesButton, gbc);
        
        inputPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // 하단: 재고 경고 및 요약
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        stockWarningLabel = new JLabel(" ");
        stockWarningLabel.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        stockWarningLabel.setForeground(Color.RED);
        bottomPanel.add(stockWarningLabel, BorderLayout.WEST);
        
        salesSummaryLabel = new JLabel("이번 달 총 판매액: ₩0");
        salesSummaryLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 11));
        salesSummaryLabel.setForeground(new Color(0, 100, 0));
        bottomPanel.add(salesSummaryLabel, BorderLayout.EAST);
        
        inputPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return inputPanel;
    }
    
    /**
     * 판매 기록 테이블 패널 생성
     */
    private JPanel createSalesTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("판매 기록"));
        
        // 테이블 생성
        String[] salesColumns = {"날짜", "식물", "판매량(kg)", "단가(원)", "총액(원)"};
        salesTableModel = new DefaultTableModel(salesColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        salesTable = new JTable(salesTableModel);
        salesTable.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        salesTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 11));
        salesTable.setRowHeight(25);
        salesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 컬럼 너비 조정
        salesTable.getColumnModel().getColumn(0).setPreferredWidth(100); // 날짜
        salesTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // 식물
        salesTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 판매량
        salesTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // 단가
        salesTable.getColumnModel().getColumn(4).setPreferredWidth(100); // 총액
        
        JScrollPane scrollPane = new JScrollPane(salesTable);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * 판매 내보내기 패널 생성
     */
    private JPanel createSalesExportPanel() {
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        exportButton = new JButton("엑셀로 내보내기");
        exportButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        exportButton.setBackground(new Color(152, 251, 152));
        exportButton.setPreferredSize(new Dimension(150, 30));
        
        exportPanel.add(exportButton);
        
        return exportPanel;
    }
    
    /**
     * 하단 공통 버튼 패널 생성
     */
    private JPanel createBottomPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        
        // 좌측: 정보 라벨
        JLabel infoLabel = new JLabel("팁: 각 탭을 클릭하여 수확, 재고, 판매를 관리하세요.");
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        buttonPanel.add(infoLabel, BorderLayout.WEST);
        
        // 우측: 닫기 버튼
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButton = new JButton("닫기");
        closeButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        closeButton.setPreferredSize(new Dimension(80, 35));
        rightPanel.add(closeButton);
        
        buttonPanel.add(rightPanel, BorderLayout.EAST);
        
        return buttonPanel;
    }
    
    // ========== UI 업데이트 메소드들 ==========
    
    /**
     * 식물 콤보박스들 업데이트
     */
    public void updatePlantCombos(Set<String> plantNames) {
        // 수확 콤보박스 업데이트
        harvestPlantCombo.removeAllItems();
        harvestPlantCombo.addItem("-- 식물 선택 --");
        for (String plantName : plantNames) {
            harvestPlantCombo.addItem(plantName);
        }
        
        // 판매 콤보박스 업데이트
        salePlantCombo.removeAllItems();
        salePlantCombo.addItem("-- 식물 선택 --");
        for (String plantName : plantNames) {
            salePlantCombo.addItem(plantName);
        }
    }
    
    /**
     * 재고 경고 메시지 표시
     */
    public void showStockWarning(String message) {
        stockWarningLabel.setText("⚠ " + message);
    }
    
    /**
     * 재고 경고 메시지 지우기
     */
    public void clearStockWarning() {
        stockWarningLabel.setText(" ");
    }
    
    /**
     * 요약 정보 업데이트
     */
    public void updateSummaryLabels(String harvestSummary, String salesSummary, String inventoryValue) {
        if (harvestSummary != null) {
            harvestSummaryLabel.setText(harvestSummary);
        }
        if (salesSummary != null) {
            salesSummaryLabel.setText(salesSummary);
        }
        if (inventoryValue != null) {
            inventoryValueLabel.setText(inventoryValue);
        }
    }
    
    /**
     * 판매 총액 필드 업데이트
     */
    public void updateSaleTotal(String total) {
        saleTotalField.setText(total);
    }
    
    /**
     * 판매 단가 필드 업데이트
     */
    public void updateSalePrice(String price) {
        salePriceField.setText(price);
    }
    
    /**
     * 입력 필드들 초기화
     */
    public void clearInputFields() {
        // 수확 필드 초기화
        harvestDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        harvestPlantCombo.setSelectedIndex(0);
        harvestQuantityField.setText("");
        
        // 판매 필드 초기화
        saleDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        salePlantCombo.setSelectedIndex(0);
        saleQuantityField.setText("");
        salePriceField.setText("");
        saleTotalField.setText("");
        
        // 검색 필드 초기화
        searchField.setText("");
        
        // 경고 메시지 지우기
        clearStockWarning();
    }
    
    /**
     * 로딩 상태 표시
     */
    public void setLoading(boolean loading) {
        // 모든 버튼 비활성화/활성화
        addHarvestButton.setEnabled(!loading);
        addSalesButton.setEnabled(!loading);
        addInventoryButton.setEnabled(!loading);
        removeInventoryButton.setEnabled(!loading);
        reportButton.setEnabled(!loading);
        exportButton.setEnabled(!loading);
        searchButton.setEnabled(!loading);
        
        // 입력 필드들 비활성화/활성화
        harvestPlantCombo.setEnabled(!loading);
        salePlantCombo.setEnabled(!loading);
        harvestQuantityField.setEditable(!loading);
        saleQuantityField.setEditable(!loading);
        searchField.setEditable(!loading);
        
        // 커서 변경
        if (loading) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    /**
     * 특정 탭으로 이동
     */
    public void switchToTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(tabIndex);
        }
    }
    
    /**
     * 현재 선택된 탭 인덱스 반환
     */
    public int getCurrentTabIndex() {
        return tabbedPane.getSelectedIndex();
    }
    
    // ========== Getter 메소드들 - Controller가 UI 컴포넌트에 접근하기 위함 ==========
    
    // 수확 관련 Getter들
    public JTextField getHarvestDateField() { return harvestDateField; }
    public JComboBox<String> getHarvestPlantCombo() { return harvestPlantCombo; }
    public JTextField getHarvestQuantityField() { return harvestQuantityField; }
    public JTable getHarvestTable() { return harvestTable; }
    public DefaultTableModel getHarvestTableModel() { return harvestTableModel; }
    public JButton getAddHarvestButton() { return addHarvestButton; }
    public JButton getHarvestDatePickerButton() { return harvestDatePickerButton; }
    
    // 재고 관련 Getter들
    public JTable getInventoryTable() { return inventoryTable; }
    public DefaultTableModel getInventoryTableModel() { return inventoryTableModel; }
    public JTextField getSearchField() { return searchField; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getClearSearchButton() { return clearSearchButton; }
    public JButton getAddInventoryButton() { return addInventoryButton; }
    public JButton getRemoveInventoryButton() { return removeInventoryButton; }
    public JButton getReportButton() { return reportButton; }
    
    // 판매 관련 Getter들
    public JTextField getSaleDateField() { return saleDateField; }
    public JComboBox<String> getSalePlantCombo() { return salePlantCombo; }
    public JTextField getSaleQuantityField() { return saleQuantityField; }
    public JTextField getSalePriceField() { return salePriceField; }
    public JTextField getSaleTotalField() { return saleTotalField; }
    public JTable getSalesTable() { return salesTable; }
    public DefaultTableModel getSalesTableModel() { return salesTableModel; }
    public JButton getAddSalesButton() { return addSalesButton; }
    public JButton getSaleDatePickerButton() { return saleDatePickerButton; }
    public JButton getExportButton() { return exportButton; }
    
    // 공통 Getter들
    public JButton getCloseButton() { return closeButton; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }
    
    // ========== 편의 메소드들 ==========
    
    /**
     * 수확 입력 필드 검증 시각적 피드백
     */
    public void highlightHarvestFields(boolean dateValid, boolean plantValid, boolean quantityValid) {
        harvestDateField.setBorder(dateValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        harvestPlantCombo.setBorder(plantValid ? 
            UIManager.getBorder("ComboBox.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        harvestQuantityField.setBorder(quantityValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    /**
     * 판매 입력 필드 검증 시각적 피드백
     */
    public void highlightSalesFields(boolean dateValid, boolean plantValid, boolean quantityValid) {
        saleDateField.setBorder(dateValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        salePlantCombo.setBorder(plantValid ? 
            UIManager.getBorder("ComboBox.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        saleQuantityField.setBorder(quantityValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    /**
     * 모든 필드 테두리 정상화
     */
    public void resetFieldBorders() {
        harvestDateField.setBorder(UIManager.getBorder("TextField.border"));
        harvestPlantCombo.setBorder(UIManager.getBorder("ComboBox.border"));
        harvestQuantityField.setBorder(UIManager.getBorder("TextField.border"));
        
        saleDateField.setBorder(UIManager.getBorder("TextField.border"));
        salePlantCombo.setBorder(UIManager.getBorder("ComboBox.border"));
        saleQuantityField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    /**
     * 현재 선택된 수확 식물 반환
     */
    public String getSelectedHarvestPlant() {
        String selected = (String) harvestPlantCombo.getSelectedItem();
        if (selected == null || selected.equals("-- 식물 선택 --")) {
            return null;
        }
        return selected;
    }
    
    /**
     * 현재 선택된 판매 식물 반환
     */
    public String getSelectedSalesPlant() {
        String selected = (String) salePlantCombo.getSelectedItem();
        if (selected == null || selected.equals("-- 식물 선택 --")) {
            return null;
        }
        return selected;
    }
    
    /**
     * 재고 테이블에서 선택된 행의 식물명 반환
     */
    public String getSelectedInventoryPlant() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow != -1) {
            return (String) inventoryTableModel.getValueAt(selectedRow, 0);
        }
        return null;
    }
    
    /**
     * 테이블 행 수 정보 업데이트
     */
    public void updateTableCounts() {
        // 각 탭의 제목에 개수 정보 추가 (선택사항)
        tabbedPane.setTitleAt(0, "수확 기록 (" + harvestTableModel.getRowCount() + ")");
        tabbedPane.setTitleAt(1, "재고 현황 (" + inventoryTableModel.getRowCount() + ")");
        tabbedPane.setTitleAt(2, "판매 기록 (" + salesTableModel.getRowCount() + ")");
    }
    
    /**
     * 성공 메시지를 상태 표시줄에 표시 (잠시 후 사라짐)
     */
    public void showTemporaryMessage(String message) {
        JLabel tempLabel = new JLabel("" + message);
        tempLabel.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        tempLabel.setForeground(new Color(0, 150, 0));
        
        // 하단 패널의 좌측에 임시로 표시
        JPanel bottomPanel = (JPanel) ((BorderLayout) getContentPane().getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (bottomPanel != null) {
            Component originalComponent = ((BorderLayout) bottomPanel.getLayout()).getLayoutComponent(BorderLayout.WEST);
            bottomPanel.remove(originalComponent);
            bottomPanel.add(tempLabel, BorderLayout.WEST);
            bottomPanel.revalidate();
            bottomPanel.repaint();
            
            // 3초 후 원래 컴포넌트로 복원
            Timer timer = new Timer(3000, e -> {
                bottomPanel.remove(tempLabel);
                bottomPanel.add(originalComponent, BorderLayout.WEST);
                bottomPanel.revalidate();
                bottomPanel.repaint();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * 테이블 데이터 유효성 검사 결과 표시
     */
    public void highlightTableRow(JTable table, int row, boolean isValid) {
        if (row >= 0 && row < table.getRowCount()) {
            if (isValid) {
                table.setSelectionBackground(new Color(144, 238, 144)); // 연한 녹색
            } else {
                table.setSelectionBackground(new Color(255, 182, 193)); // 연한 빨간색
            }
            table.setRowSelectionInterval(row, row);
        }
    }
    
    /**
     * 입력 필드 포커스 설정
     */
    public void focusOnFirstField() {
        int currentTab = getCurrentTabIndex();
        switch (currentTab) {
            case 0: // 수확 탭
                harvestDateField.requestFocus();
                break;
            case 1: // 재고 탭
                searchField.requestFocus();
                break;
            case 2: // 판매 탭
                saleDateField.requestFocus();
                break;
        }
    }
    
    /**
     * 다크 모드 테마 적용 (선택사항)
     */
    public void applyDarkTheme(boolean darkMode) {
        Color backgroundColor = darkMode ? new Color(45, 45, 45) : Color.WHITE;
        Color foregroundColor = darkMode ? Color.WHITE : Color.BLACK;
        
        // 메인 패널 색상 변경
        getContentPane().setBackground(backgroundColor);
        tabbedPane.setBackground(backgroundColor);
        tabbedPane.setForeground(foregroundColor);
        
        // 추가적인 테마 적용 로직...
    }
}