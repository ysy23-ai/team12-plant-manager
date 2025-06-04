package controller;

import view.*;
import util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * 메인 대시보드 컨트롤러 - 모든 이벤트 처리 및 비즈니스 로직 담당
 * View는 UI만 담당하고, 이 클래스가 모든 로직을 처리
 */
public class MainDashboardController {
    private MainDashboardView view;
    private Map<String, JFrame> openWindows;

    public MainDashboardController(MainDashboardView view) {
        this.view = view;
        this.openWindows = new HashMap<>();

        // 상태바 추가
        view.addStatusBar();

        // 이벤트 리스너 설정
        setupEventListeners();

        updateStatus("애플리케이션이 시작되었습니다.");
    }

    /**
     * 모든 이벤트 리스너 설정
     */
    private void setupEventListeners() {
        // 메뉴 이벤트 설정
        setupMenuListeners();

        // 테이블 관리 버튼들
        view.getDetailButton().addActionListener(e -> showPlantDetailDialog());
        view.getEditButton().addActionListener(e -> editSelectedPlant());
        view.getDeleteButton().addActionListener(e -> deleteSelectedPlant());

        // 차트 새로고침 버튼
        view.getRefreshChartButton().addActionListener(e -> refreshCharts());
    }

    /**
     * 메뉴 이벤트 리스너 설정
     */
    private void setupMenuListeners() {
        JMenuBar menuBar = view.getJMenuBar();
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu menu = menuBar.getMenu(i);
            if (menu != null) {
                for (int j = 0; j < menu.getItemCount(); j++) {
                    JMenuItem item = menu.getItem(j);
                    if (item != null) {
                        item.addActionListener(new MenuActionListener());
                    }
                }
            }
        }
    }

    /**
     * 메뉴 아이템 액션 리스너 클래스
     */
    private class MenuActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JMenuItem source = (JMenuItem) e.getSource();
            String menuText = source.getText();

            switch (menuText) {
                case "식물 등록":
                    openPlantRegistrationWindow();
                    break;
                case "식물 상태 관리":
                    openPlantMonitoringWindow();
                    break;
                case "인벤토리 관리":
                    openInventorySalesWindow(1); // 재고 탭으로
                    break;
                case "수확 통계":
                case "판매 통계":
                    showStatisticsDialog();
                    break;
                case "종료":
                    exitApplication();
                    break;
                case "새로 만들기":
                    createNewProject();
                    break;
                case "열기":
                    openProject();
                    break;
                case "저장":
                    saveProject();
                    break;
                case "사용 방법":
                    showHelpDialog();
                    break;
                case "정보":
                    showAboutDialog();
                    break;
                default:
                    UIUtils.showInfoMessage(view,
                            "'" + menuText + "' 기능은 아직 구현되지 않았습니다.",
                            "안내");
            }
            updateStatus("'" + menuText + "' 메뉴 선택됨");
        }
    }

    /**
     * 식물 등록 창 열기
     */
    private void openPlantRegistrationWindow() {
        if (!openWindows.containsKey("plantRegistration")) {
            PlantRegistrationView regView = new PlantRegistrationView();
            PlantRegistrationController regController = new PlantRegistrationController(regView);
            openWindows.put("plantRegistration", regView);
        }
        openWindows.get("plantRegistration").setVisible(true);
        openWindows.get("plantRegistration").toFront();
        updateStatus("식물 등록/관리 창이 열렸습니다.");
    }

    /**
     * 식물 모니터링 창 열기
     */
    private void openPlantMonitoringWindow() {
        if (!openWindows.containsKey("plantMonitoring")) {
            PlantMonitoringView monView = new PlantMonitoringView();
            PlantMonitoringController monController = new PlantMonitoringController(monView);
            openWindows.put("plantMonitoring", monView);
        }
        openWindows.get("plantMonitoring").setVisible(true);
        openWindows.get("plantMonitoring").toFront();
        updateStatus("식물 상태 모니터링 창이 열렸습니다.");
    }

    /**
     * 인벤토리 & 판매 창 열기
     * @param tabIndex 열릴 탭 인덱스 (0: 수확, 1: 재고, 2: 판매)
     */
    private void openInventorySalesWindow(int tabIndex) {
        if (!openWindows.containsKey("inventorySales")) {
            InventorySalesView invView = new InventorySalesView();
            InventorySalesController invController = new InventorySalesController(invView);
            openWindows.put("inventorySales", invView);
        }

        InventorySalesView invView = (InventorySalesView) openWindows.get("inventorySales");
        invView.switchToTab(tabIndex); // 지정된 탭으로 이동
        invView.setVisible(true);
        invView.toFront();

        String[] tabNames = {"수확", "재고", "판매"};
        updateStatus("인벤토리 & 판매 관리 창이 열렸습니다. (" + tabNames[tabIndex] + " 탭)");
    }

    /**
     * 선택된 식물 상세 정보 다이얼로그 표시
     */
    private void showPlantDetailDialog() {
        int selectedRow = view.getPlantTable().getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarningMessage(view,
                    "상세 정보를 볼 식물을 선택해주세요.",
                    "선택 오류");
            return;
        }

        // 선택된 식물 정보 가져오기
        String plantId = (String) view.getPlantTable().getValueAt(selectedRow, 0);
        String plantName = (String) view.getPlantTable().getValueAt(selectedRow, 1);
        String plantSpecies = (String) view.getPlantTable().getValueAt(selectedRow, 2);
        String plantType = (String) view.getPlantTable().getValueAt(selectedRow, 3);
        String plantDate = (String) view.getPlantTable().getValueAt(selectedRow, 4);
        String harvestDate = (String) view.getPlantTable().getValueAt(selectedRow, 5);
        String growthStage = (String) view.getPlantTable().getValueAt(selectedRow, 6);
        String healthStatus = (String) view.getPlantTable().getValueAt(selectedRow, 7);

        // 상세 정보 다이얼로그 생성
        JDialog dialog = new JDialog(view, "🌱 식물 상세 정보: " + plantName, true);
        dialog.setSize(650, 720);
        dialog.setLocationRelativeTo(view);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 헤더 패널
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(" " + plantName + " 상세 정보");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(34, 139, 34));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JLabel idLabel = new JLabel("ID: " + plantId);
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        idLabel.setForeground(Color.GRAY);
        headerPanel.add(idLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 정보 패널들을 담을 중앙 패널
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // 기본 정보 패널
        JPanel basicInfoPanel = new JPanel(new GridLayout(0, 2, 15, 8));
        basicInfoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "기본 정보",
                0, 0,
                new Font("맑은 고딕", Font.BOLD, 14)));

        basicInfoPanel.add(createInfoLabel("식물 이름:", plantName));
        basicInfoPanel.add(createInfoLabel("종류:", plantSpecies));
        basicInfoPanel.add(createInfoLabel("식물 유형:", plantType));
        basicInfoPanel.add(createInfoLabel("심은 날짜:", plantDate));
        basicInfoPanel.add(createInfoLabel("예상 수확일:", harvestDate));
        basicInfoPanel.add(createInfoLabel("성장 단계:", growthStage));
        basicInfoPanel.add(createInfoLabel("건강 상태:", healthStatus));
        basicInfoPanel.add(createInfoLabel("등록일:", getCurrentDate()));

        centerPanel.add(basicInfoPanel);

        // 상세 설명 패널
        JPanel descPanel = new JPanel(new BorderLayout(10, 10));
        descPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "상세 설명 및 관리 기록",
                0, 0,
                new Font("맑은 고딕", Font.BOLD, 14)));

        JTextArea descArea = new JTextArea();
        descArea.setEditable(false);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        descArea.setBackground(new Color(248, 248, 248));

        // 설명 내용 생성
        StringBuilder desc = new StringBuilder();
        desc.append("").append(plantName).append("는 ").append(plantSpecies).append(" 유형의 식물로, ");
        desc.append(plantDate).append("에 심어졌습니다.\n\n");
        desc.append("현재 상태:\n");
        desc.append("   • 성장 단계: ").append(growthStage).append("\n");
        desc.append("   • 건강 상태: ").append(healthStatus).append("\n");
        desc.append("   • 예상 수확일: ").append(harvestDate).append("\n\n");
        desc.append("관리 팁:\n");
        desc.append("   • 정기적인 물주기와 충분한 햇빛 노출이 필요합니다.\n");
        desc.append("   • 성장 단계에 따라 적절한 영양 공급을 해주세요.\n");
        desc.append("   • 병해충 발생 여부를 주기적으로 확인하세요.\n\n");
        desc.append("통계 정보:\n");
        desc.append("   • 심은 지 ").append(calculateDaysBetween(plantDate)).append("일 경과\n");
        desc.append("   • 예상 수확까지 ").append(calculateDaysToHarvest(harvestDate)).append("일 남음");

        descArea.setText(desc.toString());

        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        descPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(descPanel);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 과일 모형 추가//
        if (plantName.contains("딸기")) {
            PlantVisualPanel.StrawberryPanelV5 strawberryPanel = new PlantVisualPanel.StrawberryPanelV5();
            strawberryPanel.setPreferredSize(new Dimension(300, 250));
            centerPanel.add(strawberryPanel);  // 🍓
        }
        if (plantName.contains("포도")) {
            PlantVisualPanel.GrapePanel grapePanel = new PlantVisualPanel.GrapePanel();
            grapePanel.setPreferredSize(new Dimension(300, 250));
            centerPanel.add(grapePanel);  // 🍇
        }
        if (plantName.contains("당근")) {
            PlantVisualPanel.CarrotPanel carrotPanel = new PlantVisualPanel.CarrotPanel();
            carrotPanel.setPreferredSize(new Dimension(200, 300));
            centerPanel.add(carrotPanel);  // 🥕
        }
        if (plantName.contains("한라봉")) {
            PlantVisualPanel.HallabongPanel hallabongPanel = new PlantVisualPanel.HallabongPanel();
            hallabongPanel.setPreferredSize(new Dimension(250, 250));
            centerPanel.add(hallabongPanel);  // 🍊
        }
        if (plantName.contains("상추")) {
            PlantVisualPanel.LettucePanel lettucePanel = new PlantVisualPanel.LettucePanel();
            lettucePanel.setPreferredSize(new Dimension(250, 250));

            JPanel lettuceWrapper = new JPanel(new BorderLayout());
            lettuceWrapper.setOpaque(false);
            lettuceWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            lettuceWrapper.add(lettucePanel, BorderLayout.CENTER);

            centerPanel.add(lettuceWrapper);  // 🥬 상추 추가
        }



        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton editButton = new JButton("편집");
        editButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        editButton.addActionListener(e -> {
            dialog.dispose();
            editSelectedPlant();
        });

        JButton monitorButton = new JButton("모니터링");
        monitorButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        monitorButton.addActionListener(e -> {
            dialog.dispose();
            openPlantMonitoringWindow();
        });

        JButton closeButton = new JButton("닫기");
        closeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(editButton);
        buttonPanel.add(monitorButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
        updateStatus("'" + plantName + "' 식물의 상세 정보를 표시했습니다.");
    }

    /**
     * 정보 라벨 생성 헬퍼 메소드
     */
    private JPanel createInfoLabel(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        keyLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        panel.add(keyLabel, BorderLayout.WEST);
        panel.add(valueLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 선택된 식물 편집
     */
    private void editSelectedPlant() {
        int selectedRow = view.getPlantTable().getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarningMessage(view,
                    "편집할 식물을 선택해주세요.",
                    "선택 오류");
            return;
        }

        // 식물 등록 창을 열고 편집 모드로 설정
        openPlantRegistrationWindow();

        // TODO: 실제로는 PlantRegistrationController에 편집할 데이터 전달
        updateStatus("선택된 식물 편집 모드로 진입했습니다.");
    }

    /**
     * 선택된 식물 삭제
     */
    private void deleteSelectedPlant() {
        int selectedRow = view.getPlantTable().getSelectedRow();
        if (selectedRow == -1) {
            UIUtils.showWarningMessage(view,
                    "삭제할 식물을 선택해주세요.",
                    "선택 오류");
            return;
        }

        String plantName = (String) view.getPlantTable().getValueAt(selectedRow, 1);

        boolean confirm = UIUtils.showConfirmDialog(view,
                "🗑'" + plantName + "' 식물을 정말 삭제하시겠습니까?\n\n" +
                        "경고: 삭제하면 다음 데이터도 함께 삭제됩니다:\n" +
                        "   • 모든 수확 기록\n" +
                        "   • 모든 판매 기록\n" +
                        "   • 모니터링 기록\n\n" +
                        "이 작업은 되돌릴 수 없습니다.",
                "식물 삭제 확인");

        if (confirm) {
            // 테이블에서 행 제거
            view.removePlantFromTable(selectedRow);

            // TODO: 실제로는 데이터베이스에서도 삭제 및 관련 기록들 삭제

            updateStatus("'" + plantName + "' 식물이 삭제되었습니다.");

            UIUtils.showInfoMessage(view,
                    "'" + plantName + "' 식물이 성공적으로 삭제되었습니다.",
                    "삭제 완료");
        }
    }

    /**
     * 차트 새로고침
     */
    private void refreshCharts() {
        view.refreshCharts();
        updateStatus("차트가 새로고침되었습니다.");

        UIUtils.showInfoMessage(view,
                "모든 차트가 최신 데이터로 업데이트되었습니다.",
                "차트 새로고침");
    }

    /**
     * 통계 분석 다이얼로그 표시
     */
    private void showStatisticsDialog() {
        JDialog dialog = new JDialog(view, "📊농장 통계 분석", true);
        dialog.setSize(900, 700);
        dialog.setLocationRelativeTo(view);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 제목 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("농장 통계 분석 대시보드");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(34, 139, 34));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JLabel dateLabel = new JLabel("생성일: " + getCurrentDate());
        dateLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        dateLabel.setForeground(Color.GRAY);
        titlePanel.add(dateLabel, BorderLayout.EAST);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // 탭 패널 생성
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        // 수확 통계 탭
        JPanel harvestStatsPanel = createHarvestStatsPanel();
        tabbedPane.addTab("🌾 수확 통계", harvestStatsPanel);

        // 판매 통계 탭
        JPanel salesStatsPanel = createSalesStatsPanel();
        tabbedPane.addTab("판매 통계", salesStatsPanel);

        // 종합 분석 탭
        JPanel overallStatsPanel = createOverallStatsPanel();
        tabbedPane.addTab("종합 분석", overallStatsPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton exportButton = new JButton("내보내기");
        exportButton.setFont(new Font("맑은 고딕", Font.BOLD, 11));
        exportButton.addActionListener(e -> {
            UIUtils.showInfoMessage(dialog,
                    "통계 내보내기 기능은 아직 구현되지 않았습니다.",
                    "안내");
        });

        JButton printButton = new JButton("인쇄");
        printButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        printButton.addActionListener(e -> {
            UIUtils.showInfoMessage(dialog,
                    "인쇄 기능은 아직 구현되지 않았습니다.",
                    "안내");
        });

        JButton closeButton = new JButton("닫기");
        closeButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(exportButton);
        buttonPanel.add(printButton);
        buttonPanel.add(closeButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
        updateStatus("통계 분석 창이 열렸습니다.");
    }

    /**
     * 수확 통계 패널 생성
     */
    private JPanel createHarvestStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // 요약 카드들
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        summaryPanel.add(createStatCard("총 수확량", "120 kg", Color.GREEN));
        summaryPanel.add(createStatCard("이번 달", "50 kg", Color.BLUE));
        summaryPanel.add(createStatCard("평균/일", "2.3 kg", Color.ORANGE));
        summaryPanel.add(createStatCard("최고 기록", "15 kg", Color.RED));
        panel.add(summaryPanel, BorderLayout.NORTH);

        // 상세 데이터 테이블
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("월별 수확 데이터"));

        String[] columns = {"월", "딸기(kg)", "상추(kg)", "포도(kg)", "당근(kg)", "합계(kg)"};
        Object[][] data = {
                {"1월", 0, 0, 0, 0, 0},
                {"2월", 5, 3, 0, 2, 10},
                {"3월", 15, 8, 5, 7, 35},
                {"4월", 25, 15, 10, 10, 60},
                {"5월", 30, 20, 15, 15, 80}
        };

        JTable table = new JTable(data, columns);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 11));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 판매 통계 패널 생성
     */
    private JPanel createSalesStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // 요약 카드들
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        summaryPanel.add(createStatCard("총 판매액", "₩450,000", Color.GREEN));
        summaryPanel.add(createStatCard("이번 달", "₩200,000", Color.BLUE));
        summaryPanel.add(createStatCard("평균 단가", "₩4,500", Color.ORANGE));
        summaryPanel.add(createStatCard("최고 매출", "₩80,000", Color.RED));
        panel.add(summaryPanel, BorderLayout.NORTH);

        // 상세 데이터 테이블
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("월별 판매 데이터"));

        String[] columns = {"월", "딸기(원)", "상추(원)", "포도(원)", "당근(원)", "합계(원)"};
        Object[][] data = {
                {"1월", 0, 0, 0, 0, 0},
                {"2월", 25000, 6000, 0, 6000, 37000},
                {"3월", 75000, 16000, 35000, 21000, 147000},
                {"4월", 125000, 30000, 70000, 30000, 255000},
                {"5월", 150000, 40000, 105000, 45000, 340000}
        };

        JTable table = new JTable(data, columns);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 11));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 종합 분석 패널 생성
     */
    private JPanel createOverallStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        JTextArea analysisArea = new JTextArea();
        analysisArea.setEditable(false);
        analysisArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        analysisArea.setLineWrap(true);
        analysisArea.setWrapStyleWord(true);

        StringBuilder analysis = new StringBuilder();
        analysis.append("농장 운영 종합 분석 보고서\n");
        analysis.append("=====================================\n\n");
        analysis.append("주요 성과:\n");
        analysis.append("   • 총 5종류의 식물을 재배 중\n");
        analysis.append("   • 누적 수확량: 120kg (목표 대비 120% 달성)\n");
        analysis.append("   • 누적 판매액: ₩450,000 (목표 대비 115% 달성)\n\n");
        analysis.append("성장 추세:\n");
        analysis.append("   • 월평균 성장률: +25%\n");
        analysis.append("   • 가장 수익성 높은 작물: 딸기 (단가 ₩5,000/kg)\n");
        analysis.append("   • 가장 생산량 많은 작물: 상추 (월 20kg)\n\n");
        analysis.append("⚠ 개선 필요 사항:\n");
        analysis.append("   • 당근의 성장 속도가 예상보다 느림\n");
        analysis.append("   • 포도의 수확량 대비 판매량이 낮음\n");
        analysis.append("   • 여름철 대비 추가 품종 검토 필요\n\n");
        analysis.append("향후 전망:\n");
        analysis.append("   • 6월 예상 수확량: 95kg\n");
        analysis.append("   • 6월 예상 매출: ₩280,000\n");
        analysis.append("   • 권장사항: 딸기 재배 면적 확대, 새로운 고부가가치 작물 도입");

        analysisArea.setText(analysis.toString());

        JScrollPane scrollPane = new JScrollPane(analysisArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    /**
     * 통계 카드 생성 헬퍼 메소드
     */
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(color, 2));
        card.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        titleLabel.setForeground(Color.GRAY);
        card.add(titleLabel, BorderLayout.NORTH);

        JLabel valueLabel = new JLabel(value, JLabel.CENTER);
        valueLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        valueLabel.setForeground(color);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    /**
     * 새 프로젝트 생성
     */
    private void createNewProject() {
        boolean confirm = UIUtils.showConfirmDialog(view,
                "새 프로젝트를 생성하시겠습니까?\n\n" +
                        "⚠ 주의: 저장되지 않은 현재 데이터는 모두 손실됩니다.\n" +
                        "계속하기 전에 현재 작업을 저장하는 것을 권장합니다.",
                "새 프로젝트 생성");

        if (confirm) {
            // TODO: 실제 새 프로젝트 생성 로직
            // - 모든 테이블 데이터 초기화
            // - 차트 리셋
            // - 열린 창들 닫기

            updateStatus("새 프로젝트가 생성되었습니다.");
            UIUtils.showInfoMessage(view,
                    "새 프로젝트가 성공적으로 생성되었습니다.",
                    "프로젝트 생성 완료");
        }
    }

    /**
     * 프로젝트 열기
     */
    private void openProject() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("농장 프로젝트 파일 선택");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".farm");
            }

            @Override
            public String getDescription() {
                return "농장 프로젝트 파일 (*.farm)";
            }
        });

        int result = fileChooser.showOpenDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();

            // TODO: 실제 프로젝트 파일 로딩 로직
            // - 파일 유효성 검사
            // - 데이터 로딩
            // - UI 업데이트

            updateStatus("프로젝트 '" + fileName + "' 를 불러왔습니다.");
            UIUtils.showInfoMessage(view,
                    "프로젝트 파일이 성공적으로 로드되었습니다.\n" +
                            "파일: " + fileName,
                    "프로젝트 열기 완료");
        }
    }

    /**
     * 프로젝트 저장
     */
    private void saveProject() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("농장 프로젝트 저장");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(java.io.File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".farm");
            }

            @Override
            public String getDescription() {
                return "농장 프로젝트 파일 (*.farm)";
            }
        });

        int result = fileChooser.showSaveDialog(view);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();

            // .farm 확장자 자동 추가
            if (!fileName.toLowerCase().endsWith(".farm")) {
                selectedFile = new java.io.File(selectedFile.getParent(), fileName + ".farm");
            }

            // TODO: 실제 프로젝트 파일 저장 로직
            // - 모든 데이터 직렬화
            // - 파일 쓰기
            // - 오류 처리

            updateStatus("프로젝트가 '" + selectedFile.getName() + "' 로 저장되었습니다.");
            UIUtils.showInfoMessage(view,
                    "프로젝트가 성공적으로 저장되었습니다.\n" +
                            "파일: " + selectedFile.getName(),
                    "프로젝트 저장 완료");
        }
    }

    /**
     * 도움말 다이얼로그 표시
     */
    private void showHelpDialog() {
        JDialog helpDialog = new JDialog(view, "사용 설명서", true);
        helpDialog.setSize(700, 600);
        helpDialog.setLocationRelativeTo(view);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 제목
        JLabel titleLabel = new JLabel("식물 농장 관리 시스템 사용 설명서");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(new Color(34, 139, 34));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 도움말 내용
        JTextArea helpText = new JTextArea();
        helpText.setEditable(false);
        helpText.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        helpText.setLineWrap(true);
        helpText.setWrapStyleWord(true);

        StringBuilder help = new StringBuilder();
        help.append("식물 농장 관리 시스템 사용 설명서\n");
        help.append("=====================================\n\n");
        help.append("1. 기본 사용법\n");
        help.append("   • 메인 화면에서 등록된 식물 목록을 확인할 수 있습니다.\n");
        help.append("   • 상단 버튼을 통해 빠른 액세스가 가능합니다.\n");
        help.append("   • 테이블에서 식물을 선택하고 편집/삭제할 수 있습니다.\n\n");
        help.append("2. 식물 관리\n");
        help.append("   • '식물 추가' 버튼 또는 메뉴 > 식물 관리 > 식물 등록\n");
        help.append("   • 식물 이름, 종류, 심은 날짜, 위치 등을 입력합니다.\n");
        help.append("   • 등록된 식물은 목록에서 편집하거나 삭제할 수 있습니다.\n\n");
        help.append("3. 모니터링\n");
        help.append("   • 메뉴 > 식물 관리 > 식물 상태 관리\n");
        help.append("   • 각 식물의 성장 단계와 건강 상태를 추적합니다.\n");
        help.append("   • 관찰 내용과 관리 기록을 작성할 수 있습니다.\n\n");
        help.append("4. 인벤토리 & 판매\n");
        help.append("   • 수확 기록: 언제, 어떤 식물을, 얼마나 수확했는지 기록\n");
        help.append("   • 재고 현황: 현재 보유 중인 농산물의 양과 가치 확인\n");
        help.append("   • 판매 기록: 판매 내역 관리 및 수익 분석\n\n");
        help.append("5. 통계 및 분석\n");
        help.append("   • 메뉴 > 통계에서 수확 및 판매 통계 확인\n");
        help.append("   • 월별, 작물별 데이터 분석\n");
        help.append("   • 차트를 통한 시각적 데이터 표현\n\n");
        help.append("6. 프로젝트 관리\n");
        help.append("   • 파일 > 저장: 현재 데이터를 파일로 저장\n");
        help.append("   • 파일 > 열기: 저장된 프로젝트 불러오기\n");
        help.append("   • 파일 > 새로 만들기: 새 프로젝트 시작\n\n");
        help.append("7. 사용 팁\n");
        help.append("   • 정기적으로 프로젝트를 저장하세요.\n");
        help.append("   • 식물 상태를 주기적으로 업데이트하세요.\n");
        help.append("   • 통계 기능을 활용하여 농장 운영을 개선하세요.\n\n");
        help.append("8. 문제 해결\n");
        help.append("   • 데이터가 표시되지 않는 경우: 새로고침 버튼 클릭\n");
        help.append("   • 오류 발생 시: 프로그램 재시작\n");
        help.append("   • 추가 문의: 개발팀 연락처 참조");

        helpText.setText(help.toString());

        JScrollPane scrollPane = new JScrollPane(helpText);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> helpDialog.dispose());
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        helpDialog.add(mainPanel);
        helpDialog.setVisible(true);
    }

    /**
     * 정보 다이얼로그 표시
     */
    private void showAboutDialog() {
        JDialog aboutDialog = new JDialog(view, "ℹ프로그램 정보", true);
        aboutDialog.setSize(500, 400);
        aboutDialog.setLocationRelativeTo(view);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 로고 및 제목
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel logoLabel = new JLabel("", JLabel.CENTER);
        logoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 48));
        headerPanel.add(logoLabel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("식물 농장 관리 시스템", JLabel.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        titleLabel.setForeground(new Color(34, 139, 34));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 정보 내용
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));

        infoPanel.add(createInfoRow("버전:", "v1.0.0"));
        infoPanel.add(createInfoRow("개발일:", "2025년 5월"));
        infoPanel.add(createInfoRow("개발자:", "식물 농장 관리팀"));
        infoPanel.add(createInfoRow("언어:", "Java + Swing"));
        infoPanel.add(createInfoRow("라이선스:", "MIT License"));
        infoPanel.add(createInfoRow("지원:", "Java 8 이상"));

        JPanel infoContainer = new JPanel(new BorderLayout());
        infoContainer.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(infoContainer, BorderLayout.CENTER);

        // 저작권 정보
        JPanel copyrightPanel = new JPanel(new BorderLayout());
        JTextArea copyrightText = new JTextArea();
        copyrightText.setEditable(false);
        copyrightText.setOpaque(false);
        copyrightText.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        copyrightText.setText(
                "Copyright © 2025 식물 농장 관리 시스템\n" +
                        "All rights reserved.\n\n" +
                        "이 소프트웨어는 농장 관리의 효율성을 높이기 위해 개발되었습니다.\n" +
                        "지속적인 업데이트와 개선을 통해 더 나은 서비스를 제공하겠습니다."
        );
        copyrightPanel.add(copyrightText, BorderLayout.CENTER);
        mainPanel.add(copyrightPanel, BorderLayout.SOUTH);

        // 버튼
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> aboutDialog.dispose());
        buttonPanel.add(closeButton);

        aboutDialog.add(mainPanel, BorderLayout.CENTER);
        aboutDialog.add(buttonPanel, BorderLayout.SOUTH);

        aboutDialog.setVisible(true);
    }

    /**
     * 정보 행 생성 헬퍼 메소드
     */
    private JPanel createInfoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());

        JLabel keyLabel = new JLabel(label);
        keyLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        keyLabel.setPreferredSize(new Dimension(80, 20));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));

        row.add(keyLabel, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);

        return row;
    }

    /**
     * 애플리케이션 종료
     */
    private void exitApplication() {
        boolean confirm = UIUtils.showConfirmDialog(view,
                "프로그램을 종료하시겠습니까?\n\n" +
                        "⚠ 저장되지 않은 데이터는 손실됩니다.\n" +
                        "계속하기 전에 현재 작업을 저장하는 것을 권장합니다.",
                "프로그램 종료");

        if (confirm) {
            // 열린 모든 창 닫기
            for (JFrame window : openWindows.values()) {
                window.dispose();
            }

            updateStatus("프로그램을 종료합니다.");
            System.exit(0);
        }
    }

    // ========== 유틸리티 메소드들 ==========

    /**
     * 상태 메시지 업데이트
     */
    private void updateStatus(String message) {
        view.updateStatus(message);
    }

    /**
     * 현재 날짜 반환
     */
    private String getCurrentDate() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
    }

    /**
     * 두 날짜 사이의 일수 계산
     */
    private int calculateDaysBetween(String startDate) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date start = sdf.parse(startDate);
            java.util.Date now = new java.util.Date();

            long diffInMillies = Math.abs(now.getTime() - start.getTime());
            return (int) (diffInMillies / (1000 * 60 * 60 * 24));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 수확까지 남은 일수 계산
     */
    private int calculateDaysToHarvest(String harvestDate) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date harvest = sdf.parse(harvestDate);
            java.util.Date now = new java.util.Date();

            long diffInMillies = harvest.getTime() - now.getTime();
            return Math.max(0, (int) (diffInMillies / (1000 * 60 * 60 * 24)));
        } catch (Exception e) {
            return 0;
        }
    }
}