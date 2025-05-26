package view;

import model.PlantModel;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * 식물 모니터링 뷰 - UI만 담당
 * Controller가 이벤트를 처리하고, 이 클래스는 화면 표시만 담당
 */
public class PlantMonitoringView extends JFrame {
    
    // 상단 컴포넌트들
    private JComboBox<String> plantSelector;
    private JLabel selectedPlantLabel;
    
    // 성장 단계 컴포넌트들
    private JProgressBar growthProgressBar;
    private JPanel stageIndicatorPanel;
    private JButton advanceStageButton;
    private JLabel currentStageLabel;
    
    // 건강 상태 컴포넌트들
    private JPanel healthStatusPanel;
    private JTextArea plantHealthNotesArea;
    private JButton healthyButton;
    private JButton infectedButton;
    private JButton treatedButton;
    private JLabel healthStatusLabel;
    
    // 하단 버튼들
    private JButton saveButton;
    private JButton cancelButton;
    private JButton exportButton;
    
    public PlantMonitoringView() {
        initializeUI();
    }
    
    /**
     * UI 초기화 - 모든 화면 구성 요소 생성
     */
    private void initializeUI() {
        setTitle("식물 상태 모니터링");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 상단 패널 (제목 및 식물 선택)
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        // 중앙 패널 (성장 단계 및 건강 상태)
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        // 하단 패널 (버튼)
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    /**
     * 상단 패널 생성 (제목 및 식물 선택)
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("식물 상태 모니터링");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        titleLabel.setForeground(new Color(34, 139, 34));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        // 현재 시간 표시
        JLabel timeLabel = new JLabel("마지막 업데이트: " + java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        timeLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        timeLabel.setForeground(Color.GRAY);
        titlePanel.add(timeLabel, BorderLayout.EAST);

        // 식물 선택 패널
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        
        JLabel selectorLabel = new JLabel("모니터링할 식물 선택:");
        selectorLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        selectorPanel.add(selectorLabel);
        
        selectorPanel.add(Box.createHorizontalStrut(10));
        
        plantSelector = new JComboBox<>();
        plantSelector.setPreferredSize(new Dimension(200, 30));
        plantSelector.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        selectorPanel.add(plantSelector);
        
        selectorPanel.add(Box.createHorizontalStrut(15));
        
        selectedPlantLabel = new JLabel("선택된 식물: 없음");
        selectedPlantLabel.setFont(new Font("맑은 고딕", Font.ITALIC, 12));
        selectedPlantLabel.setForeground(Color.BLUE);
        selectorPanel.add(selectedPlantLabel);

        topPanel.add(titlePanel, BorderLayout.NORTH);
        topPanel.add(selectorPanel, BorderLayout.SOUTH);
        
        return topPanel;
    }
    
    /**
     * 중앙 패널 생성 (성장 단계 및 건강 상태)
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 0, 15));

        // 성장 단계 패널
        centerPanel.add(createGrowthPanel());
        
        // 건강 상태 패널
        centerPanel.add(createHealthPanel());

        return centerPanel;
    }
    
    /**
     * 성장 단계 패널 생성
     */
    private JPanel createGrowthPanel() {
        JPanel growthPanel = new JPanel(new BorderLayout(10, 10));
        growthPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "성장 단계 모니터링",
            0, 0, 
            new Font("맑은 고딕", Font.BOLD, 14)));

        // 성장 단계 표시 패널
        JPanel stagesPanel = new JPanel(new GridLayout(1, PlantModel.GrowthStage.values().length, 5, 0));
        stageIndicatorPanel = new JPanel(new GridLayout(1, PlantModel.GrowthStage.values().length, 5, 0));
        
        for (PlantModel.GrowthStage stage : PlantModel.GrowthStage.values()) {
            JPanel stagePanel = new JPanel(new BorderLayout());
            stagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
            stagePanel.setPreferredSize(new Dimension(100, 60));
            
            // 단계 아이콘
            JLabel iconLabel = new JLabel(getStageIcon(stage), JLabel.CENTER);
            iconLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 20));
            stagePanel.add(iconLabel, BorderLayout.CENTER);
            
            // 단계 이름
            JLabel stageLabel = new JLabel(stage.getLabel(), JLabel.CENTER);
            stageLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
            stagePanel.add(stageLabel, BorderLayout.SOUTH);
            
            stageIndicatorPanel.add(stagePanel);
        }
        growthPanel.add(stageIndicatorPanel, BorderLayout.NORTH);

        // 진행률 패널
        JPanel progressPanel = new JPanel(new BorderLayout(10, 10));
        
        currentStageLabel = new JLabel("현재 단계: 선택된 식물 없음");
        currentStageLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        progressPanel.add(currentStageLabel, BorderLayout.NORTH);
        
        growthProgressBar = new JProgressBar(0, 100);
        growthProgressBar.setStringPainted(true);
        growthProgressBar.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        growthProgressBar.setForeground(new Color(34, 139, 34));
        growthProgressBar.setPreferredSize(new Dimension(0, 25));
        progressPanel.add(growthProgressBar, BorderLayout.CENTER);
        
        growthPanel.add(progressPanel, BorderLayout.CENTER);

        // 단계 진행 버튼 패널
        JPanel stageButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        advanceStageButton = new JButton("다음 단계로 진행");
        advanceStageButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        advanceStageButton.setPreferredSize(new Dimension(150, 35));
        stageButtonPanel.add(advanceStageButton);
        growthPanel.add(stageButtonPanel, BorderLayout.SOUTH);

        return growthPanel;
    }
    
    /**
     * 건강 상태 패널 생성
     */
    private JPanel createHealthPanel() {
        JPanel healthPanel = new JPanel(new BorderLayout(10, 10));
        healthPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "💚 건강 상태 관리", 
            0, 0, 
            new Font("맑은 고딕", Font.BOLD, 14)));

        // 건강 상태 표시 및 변경 패널
        JPanel statusPanel = new JPanel(new BorderLayout(10, 10));
        
        // 현재 상태 표시
        JPanel currentStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel statusTitleLabel = new JLabel("현재 건강 상태:");
        statusTitleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        currentStatusPanel.add(statusTitleLabel);
        
        healthStatusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        currentStatusPanel.add(healthStatusPanel);
        
        healthStatusLabel = new JLabel("선택된 식물 없음");
        healthStatusLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        currentStatusPanel.add(healthStatusLabel);
        
        statusPanel.add(currentStatusPanel, BorderLayout.NORTH);
        
        // 상태 변경 버튼 패널
        JPanel healthButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        healthyButton = new JButton("건강");
        healthyButton.setPreferredSize(new Dimension(80, 35));
        healthyButton.setBackground(new Color(144, 238, 144));
        healthyButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        
        infectedButton = new JButton("감염");
        infectedButton.setPreferredSize(new Dimension(80, 35));
        infectedButton.setBackground(new Color(255, 182, 193));
        infectedButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        
        treatedButton = new JButton("치료중");
        treatedButton.setPreferredSize(new Dimension(80, 35));
        treatedButton.setBackground(new Color(255, 218, 185));
        treatedButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        
        healthButtonPanel.add(healthyButton);
        healthButtonPanel.add(infectedButton);
        healthButtonPanel.add(treatedButton);
        
        statusPanel.add(healthButtonPanel, BorderLayout.CENTER);
        healthPanel.add(statusPanel, BorderLayout.NORTH);

        // 건강 상태 기록 패널
        JPanel healthNotesPanel = new JPanel(new BorderLayout(5, 5));
        
        JLabel notesLabel = new JLabel("건강 상태 기록 및 관찰 내용:");
        notesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        healthNotesPanel.add(notesLabel, BorderLayout.NORTH);
        
        plantHealthNotesArea = new JTextArea(6, 30);
        plantHealthNotesArea.setLineWrap(true);
        plantHealthNotesArea.setWrapStyleWord(true);
        plantHealthNotesArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        plantHealthNotesArea.setBorder(BorderFactory.createLoweredBevelBorder());
        
        JScrollPane scrollPane = new JScrollPane(plantHealthNotesArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        healthNotesPanel.add(scrollPane, BorderLayout.CENTER);
        
        // 기록 작성 팁
        JLabel tipLabel = new JLabel("<html><small>💡 팁: 물주기, 시비, 병해충 발견, 치료 과정 등을 자세히 기록해보세요.</small></html>");
        tipLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        tipLabel.setForeground(Color.GRAY);
        healthNotesPanel.add(tipLabel, BorderLayout.SOUTH);

        healthPanel.add(healthNotesPanel, BorderLayout.CENTER);

        return healthPanel;
    }
    
    /**
     * 하단 패널 생성 (저장/취소 버튼)
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // 좌측: 내보내기 버튼
        JPanel leftButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        exportButton = new JButton("모니터링 데이터 내보내기");
        exportButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        exportButton.setPreferredSize(new Dimension(180, 30));
        leftButtonPanel.add(exportButton);
        
        // 우측: 저장/취소 버튼
        JPanel rightButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");
        
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        cancelButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        saveButton.setPreferredSize(new Dimension(80, 35));
        cancelButton.setPreferredSize(new Dimension(80, 35));
        
        // 저장 버튼 강조
        saveButton.setBackground(new Color(173, 216, 230));
        
        rightButtonPanel.add(saveButton);
        rightButtonPanel.add(cancelButton);
        
        bottomPanel.add(leftButtonPanel, BorderLayout.WEST);
        bottomPanel.add(rightButtonPanel, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
    /**
     * 성장 단계별 아이콘 반환
     */
    private String getStageIcon(PlantModel.GrowthStage stage) {
        switch (stage) {
            case SEED: return "🌰";
            case GERMINATION: return "🌱";
            case GROWTH: return "🌿";
            case FLOWERING: return "🌸";
            case FRUITING: return "🍎";
            case HARVEST: return "🧺";
            default: return "❓";
        }
    }
    
    // ========== UI 업데이트 메소드들 ==========
    
    /**
     * 성장 진행률 업데이트
     */
    public void updateGrowthProgress(PlantModel.GrowthStage stage) {
        if (stage != null) {
            growthProgressBar.setValue(stage.getProgressValue());
            growthProgressBar.setString(stage.getLabel() + " (" + stage.getProgressValue() + "%)");
            currentStageLabel.setText("현재 단계: " + stage.getLabel());
            
            // 단계 표시기 업데이트
            updateStageIndicators(stage);
        } else {
            growthProgressBar.setValue(0);
            growthProgressBar.setString("선택된 식물 없음");
            currentStageLabel.setText("현재 단계: 선택된 식물 없음");
        }
    }
    
    /**
     * 단계 표시기 업데이트 (현재 단계 하이라이트)
     */
    private void updateStageIndicators(PlantModel.GrowthStage currentStage) {
        Component[] components = stageIndicatorPanel.getComponents();
        PlantModel.GrowthStage[] stages = PlantModel.GrowthStage.values();
        
        for (int i = 0; i < components.length && i < stages.length; i++) {
            JPanel stagePanel = (JPanel) components[i];
            if (stages[i] == currentStage) {
                stagePanel.setBackground(new Color(173, 216, 230)); // 현재 단계 하이라이트
                stagePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else if (stages[i].getProgressValue() <= currentStage.getProgressValue()) {
                stagePanel.setBackground(new Color(240, 255, 240)); // 완료된 단계
                stagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
            } else {
                stagePanel.setBackground(Color.WHITE); // 미완료 단계
                stagePanel.setBorder(BorderFactory.createRaisedBevelBorder());
            }
        }
        stageIndicatorPanel.repaint();
    }
    
    /**
     * 건강 상태 패널 업데이트
     */
    public void updateHealthStatusPanel(PlantModel.HealthStatus status) {
        healthStatusPanel.removeAll();
        
        if (status != null) {
            JPanel statusIndicator = new JPanel();
            statusIndicator.setPreferredSize(new Dimension(20, 20));
            statusIndicator.setBackground(status.getColor());
            statusIndicator.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            healthStatusPanel.add(statusIndicator);
            healthStatusLabel.setText(status.getLabel());
            healthStatusLabel.setForeground(status.getColor());
        } else {
            healthStatusLabel.setText("선택된 식물 없음");
            healthStatusLabel.setForeground(Color.GRAY);
        }

        healthStatusPanel.revalidate();
        healthStatusPanel.repaint();
    }
    
    /**
     * 식물 선택기 업데이트
     */
    public void updatePlantSelector(Map<String, ?> plants) {
        plantSelector.removeAllItems();
        plantSelector.addItem("-- 식물을 선택하세요 --");
        
        for (String plantName : plants.keySet()) {
            plantSelector.addItem(plantName);
        }
        
        if (plants.isEmpty()) {
            selectedPlantLabel.setText("등록된 식물이 없습니다.");
            selectedPlantLabel.setForeground(Color.RED);
        } else {
            selectedPlantLabel.setText("선택된 식물: 없음");
            selectedPlantLabel.setForeground(Color.BLUE);
        }
    }
    
    /**
     * 선택된 식물 라벨 업데이트
     */
    public void updateSelectedPlantLabel(String plantName) {
        if (plantName != null && !plantName.equals("-- 식물을 선택하세요 --")) {
            selectedPlantLabel.setText("선택된 식물: " + plantName);
            selectedPlantLabel.setForeground(new Color(34, 139, 34));
        } else {
            selectedPlantLabel.setText("선택된 식물: 없음");
            selectedPlantLabel.setForeground(Color.BLUE);
        }
    }
    
    /**
     * 전체 UI 상태 초기화
     */
    public void resetUI() {
        updateGrowthProgress(null);
        updateHealthStatusPanel(null);
        plantHealthNotesArea.setText("");
        updateSelectedPlantLabel(null);
    }
    
    /**
     * 버튼 활성화/비활성화
     */
    public void setButtonsEnabled(boolean enabled) {
        advanceStageButton.setEnabled(enabled);
        healthyButton.setEnabled(enabled);
        infectedButton.setEnabled(enabled);
        treatedButton.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        exportButton.setEnabled(enabled);
    }
    
    /**
     * 로딩 상태 표시
     */
    public void setLoading(boolean loading) {
        setButtonsEnabled(!loading);
        plantSelector.setEnabled(!loading);
        plantHealthNotesArea.setEditable(!loading);
        
        if (loading) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            selectedPlantLabel.setText("데이터 로딩 중...");
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
    
    // ========== Getter 메소드들 - Controller가 UI 컴포넌트에 접근하기 위함 ==========
    
    public JComboBox<String> getPlantSelector() { return plantSelector; }
    public JProgressBar getGrowthProgressBar() { return growthProgressBar; }
    public JPanel getHealthStatusPanel() { return healthStatusPanel; }
    public JTextArea getPlantHealthNotesArea() { return plantHealthNotesArea; }
    public JButton getAdvanceStageButton() { return advanceStageButton; }
    public JButton getHealthyButton() { return healthyButton; }
    public JButton getInfectedButton() { return infectedButton; }
    public JButton getTreatedButton() { return treatedButton; }
    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getExportButton() { return exportButton; }
    
    // ========== 편의 메소드들 ==========
    
    /**
     * 현재 선택된 식물 이름 반환
     */
    public String getSelectedPlant() {
        String selected = (String) plantSelector.getSelectedItem();
        if (selected == null || selected.equals("-- 식물을 선택하세요 --")) {
            return null;
        }
        return selected;
    }
    
    /**
     * 식물 선택
     */
    public void selectPlant(String plantName) {
        plantSelector.setSelectedItem(plantName);
    }
    
    /**
     * 건강 상태 기록 텍스트 반환
     */
    public String getHealthNotes() {
        return plantHealthNotesArea.getText();
    }
    
    /**
     * 건강 상태 기록 텍스트 설정
     */
    public void setHealthNotes(String notes) {
        plantHealthNotesArea.setText(notes);
        plantHealthNotesArea.setCaretPosition(0); // 텍스트 처음으로 스크롤
    }
}