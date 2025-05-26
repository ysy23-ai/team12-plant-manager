package view;

import util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 메인 대시보드 뷰 - UI만 담당
 * Controller가 이벤트를 처리하고, 이 클래스는 화면 표시만 담당
 */
public class MainDashboardView extends JFrame {

    // UI 컴포넌트들 - Controller가 접근할 수 있도록 private + getter 제공
    private JTable plantTable;
    private DefaultTableModel plantTableModel;
    private JButton addPlantBtn;
    private JButton harvestBtn;
    private JButton saleBtn;
    private JButton detailBtn;
    private JButton editBtn;
    private JButton deleteBtn;
    private JButton refreshChartBtn;
    private JLabel statusLabel;
    private JPanel chartPanel;
    private JPanel statisticsPanel;

    public MainDashboardView() {
        initializeUI();
    }

    /**
     * UI 초기화 - 모든 화면 구성 요소 생성
     */
    private void initializeUI() {
        setTitle("식물 농장 관리 시스템");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 메뉴바 설정
        setJMenuBar(createMenuBar());

        // 상단 패널 (날짜 및 빠른 액션 버튼)
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);

        // 중앙 패널 (식물 테이블)
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);

        // 하단 패널 (차트)
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * 메뉴바 생성
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 파일 메뉴
        JMenu fileMenu = new JMenu("파일");
        fileMenu.add(new JMenuItem("새로 만들기"));
        fileMenu.add(new JMenuItem("열기"));
        fileMenu.add(new JMenuItem("저장"));
        fileMenu.addSeparator();
        fileMenu.add(new JMenuItem("종료"));
        menuBar.add(fileMenu);

        // 식물 관리 메뉴
        JMenu plantMenu = new JMenu("식물 관리");
        plantMenu.add(new JMenuItem("식물 등록"));
        plantMenu.add(new JMenuItem("식물 상태 관리"));
        plantMenu.add(new JMenuItem("인벤토리 관리"));
        menuBar.add(plantMenu);

        // 통계 메뉴
        JMenu statsMenu = new JMenu("통계");
        statsMenu.add(new JMenuItem("수확/판매 통계"));
        //statsMenu.add(new JMenuItem("판매 통계"));
        menuBar.add(statsMenu);

        // 도움말 메뉴
        JMenu helpMenu = new JMenu("도움말");
        helpMenu.add(new JMenuItem("사용 방법"));
        helpMenu.add(new JMenuItem("정보"));
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * 상단 패널 생성 (날짜 표시 + 빠른 액션 버튼)
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        // 현재 날짜 표시
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String currentDate = dateFormat.format(new Date());
        JLabel dateLabel = new JLabel("오늘 날짜: " + currentDate);
        dateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        topPanel.add(dateLabel, BorderLayout.WEST);

        // 빠른 액션 버튼 패널
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addPlantBtn = new JButton("식물 추가");
        harvestBtn = new JButton("수확 기록");
        saleBtn = new JButton("판매 기록");

        // 버튼 스타일링
        addPlantBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        harvestBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        saleBtn.setFont(new Font("맑은 고딕", Font.BOLD, 12));

        actionPanel.add(addPlantBtn);
        actionPanel.add(harvestBtn);
        actionPanel.add(saleBtn);
        topPanel.add(actionPanel, BorderLayout.EAST);

        return topPanel;
    }

    /**
     * 중앙 패널 생성 (식물 목록 테이블)
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("관리 중인 식물 목록"));

        // 테이블 데이터 및 컬럼 설정
        String[] columns = {"ID", "이름", "종류", "식물 유형", "심은 날짜", "예상 수확일", "성장 단계", "건강 상태"};
        Object[][] data = {
                {"1", "딸기A", "딸기", "과일", "2025-02-10", "2025-05-15", "FLOWERING", "HEALTHY"},
                {"2", "상추B", "상추", "작물", "2025-03-05", "2025-04-10", "GROWTH", "HEALTHY"},
                {"3", "포도C", "포도", "과일", "2025-01-20", "2025-08-25", "GROWTH", "INFECTED"},
                {"4", "당근D", "당근", "작물", "2025-04-01", "2025-06-15", "GERMINATION", "HEALTHY"},
                {"5", "한라봉E", "한라봉", "과일", "2024-11-15", "2025-07-20", "FRUITING", "TREATED"}
        };

        // 테이블 모델 생성 (편집 불가능)
        plantTableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀 편집 비활성화
            }
        };

        // 테이블 생성 및 설정
        plantTable = new JTable(plantTableModel);
        plantTable.setFillsViewportHeight(true);
        plantTable.setRowHeight(25);
        plantTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // 테이블 헤더 스타일링
        plantTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 12));
        plantTable.setFont(new Font("맑은 고딕", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(plantTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 테이블 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        detailBtn = new JButton("상세 정보");
        editBtn = new JButton("편집");
        deleteBtn = new JButton("삭제");

        // 버튼 스타일링
        detailBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        editBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        deleteBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 11));

        buttonPanel.add(detailBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        return centerPanel;
    }

    /**
     * 하단 패널 생성 (차트 영역)
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        // 원형 차트 패널
        chartPanel = createChartPanel();

        // 막대 차트 패널
        statisticsPanel = createStatisticsPanel();

        bottomPanel.add(chartPanel);
        bottomPanel.add(statisticsPanel);

        return bottomPanel;
    }

    /**
     * 원형 차트 패널 생성
     */
    private JPanel createChartPanel() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("요일별 내가 돌봐야 할 식물의 개수에 대한 현황차트"));

        // 차트 표시 영역
        JPanel pieChart = new CustomPieChart();
        chartPanel.add(pieChart, BorderLayout.CENTER);

        // 범례 패널
        JPanel legendPanel = new JPanel(new GridLayout(6, 1, 0, 2));
        legendPanel.add(UIUtils.createLegendItem("월요일(2개)", Color.RED));
        legendPanel.add(UIUtils.createLegendItem("화요일(2개)", Color.GREEN));
        legendPanel.add(UIUtils.createLegendItem("수요일(3개)", Color.BLUE));
        legendPanel.add(UIUtils.createLegendItem("목요일(2개)", Color.MAGENTA));
        legendPanel.add(UIUtils.createLegendItem("금요일(1개)", Color.CYAN));
        legendPanel.add(UIUtils.createLegendItem("기타(1개)", Color.YELLOW));

        chartPanel.add(legendPanel, BorderLayout.WEST);

        // 차트 하단 버튼
        JPanel chartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshChartBtn = new JButton("새로 고침");
        refreshChartBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        chartButtonPanel.add(refreshChartBtn);
        chartPanel.add(chartButtonPanel, BorderLayout.SOUTH);

        return chartPanel;
    }

    /**
     * 막대 차트 패널 생성
     */
    private JPanel createStatisticsPanel() {
        JPanel statisticsPanel = new JPanel(new BorderLayout());
        statisticsPanel.setBorder(BorderFactory.createTitledBorder("요일별 내가 돌볼 식물의 개수에 대한 막대차트"));

        // 막대 차트 표시 영역
        JPanel barChart = new CustomBarChart();
        statisticsPanel.add(barChart, BorderLayout.CENTER);

        // 통계 하단 버튼
        JPanel statsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton colorChangeBtn = new JButton("색상 변경");
        colorChangeBtn.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        statsButtonPanel.add(colorChangeBtn);
        statisticsPanel.add(statsButtonPanel, BorderLayout.SOUTH);

        return statisticsPanel;
    }

    /**
     * 상태바 추가 메소드 - Controller에서 호출
     */
    public void addStatusBar() {
        statusLabel = new JLabel(" 준비됨");
        statusLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(statusLabel, BorderLayout.WEST);

        // 기존 레이아웃에 상태바 추가
        JPanel contentPane = (JPanel) getContentPane();
        JPanel originalContent = (JPanel) contentPane.getComponent(0);

        contentPane.removeAll();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(originalContent, BorderLayout.CENTER);
        contentPane.add(statusBar, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    // ========== UI 업데이트 메소드들 ==========

    /**
     * 상태 메시지 업데이트
     */
    public void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(" " + message);
        }
    }

    /**
     * 식물 테이블에서 행 제거
     */
    public void removePlantFromTable(int row) {
        if (row >= 0 && row < plantTableModel.getRowCount()) {
            plantTableModel.removeRow(row);
        }
    }

    /**
     * 차트 새로고침 (Controller에서 호출)
     */
    public void refreshCharts() {
        // 차트 패널 갱신
        chartPanel.removeAll();
        chartPanel.setBorder(BorderFactory.createTitledBorder("요일별 내가 돌봐야 할 식물의 개수에 대한 현황차트"));

        JPanel pieChart = new CustomPieChart();
        chartPanel.add(pieChart, BorderLayout.CENTER);

        // 범례 다시 추가
        JPanel legendPanel = new JPanel(new GridLayout(6, 1, 0, 2));
        legendPanel.add(UIUtils.createLegendItem("월요일(2개)", Color.RED));
        legendPanel.add(UIUtils.createLegendItem("화요일(2개)", Color.GREEN));
        legendPanel.add(UIUtils.createLegendItem("수요일(3개)", Color.BLUE));
        legendPanel.add(UIUtils.createLegendItem("목요일(2개)", Color.MAGENTA));
        legendPanel.add(UIUtils.createLegendItem("금요일(1개)", Color.CYAN));
        legendPanel.add(UIUtils.createLegendItem("기타(1개)", Color.YELLOW));

        chartPanel.add(legendPanel, BorderLayout.WEST);

        JPanel chartButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        chartButtonPanel.add(refreshChartBtn);
        chartPanel.add(chartButtonPanel, BorderLayout.SOUTH);

        // 통계 패널도 갱신
        statisticsPanel.removeAll();
        statisticsPanel.setBorder(BorderFactory.createTitledBorder("요일별 내가 돌볼 식물의 개수에 대한 막대차트"));

        JPanel barChart = new CustomBarChart();
        statisticsPanel.add(barChart, BorderLayout.CENTER);

        JPanel statsButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton colorChangeBtn = new JButton("색상 변경");
        statsButtonPanel.add(colorChangeBtn);
        statisticsPanel.add(statsButtonPanel, BorderLayout.SOUTH);

        // 화면 갱신
        chartPanel.revalidate();
        chartPanel.repaint();
        statisticsPanel.revalidate();
        statisticsPanel.repaint();
    }

    // ========== Getter 메소드들 - Controller가 UI 컴포넌트에 접근하기 위함 ==========

    public JTable getPlantTable() { return plantTable; }
    public DefaultTableModel getPlantTableModel() { return plantTableModel; }
    public JButton getAddPlantButton() { return addPlantBtn; }
    public JButton getHarvestButton() { return harvestBtn; }
    public JButton getSaleButton() { return saleBtn; }
    public JButton getDetailButton() { return detailBtn; }
    public JButton getEditButton() { return editBtn; }
    public JButton getDeleteButton() { return deleteBtn; }
    public JButton getRefreshChartButton() { return refreshChartBtn; }

    // ========== 차트 렌더링 클래스들 ==========

    /**
     * 커스텀 원형 차트 패널
     */
    public static class CustomPieChart extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int diameter = Math.min(width, height) - 30;
            int x = (width - diameter) / 2;
            int y = (height - diameter) / 2;

            // 색상 배열
            Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.YELLOW};

            // 데이터 (요일별 식물 개수)
            int[] values = {2, 2, 3, 2, 1, 1};

            int total = 0;
            for (int value : values) {
                total += value;
            }

            int startAngle = 0;
            for (int i = 0; i < values.length; i++) {
                int arcAngle = (int) (360.0 * values[i] / total);
                g2d.setColor(colors[i % colors.length]);
                g2d.fillArc(x, y, diameter, diameter, startAngle, arcAngle);

                // 테두리 그리기
                g2d.setColor(Color.BLACK);
                g2d.drawArc(x, y, diameter, diameter, startAngle, arcAngle);

                startAngle += arcAngle;
            }
        }
    }

    /**
     * 커스텀 막대 차트 패널
     */
    public static class CustomBarChart extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int barWidth = (width - 80) / 6;  // 6개 막대 + 여백
            int maxValue = 3;  // 최대값

            // 데이터 및 라벨
            int[] values = {2, 2, 3, 2, 1, 1};
            String[] labels = {"월", "화", "수", "목", "금", "기타"};
            Color[] colors = {Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED, Color.PINK, Color.YELLOW};

            // 축 그리기
            g2d.setColor(Color.BLACK);
            g2d.drawLine(40, height - 50, 40, 20);  // Y축
            g2d.drawLine(40, height - 50, width - 20, height - 50);  // X축

            // Y축 눈금 및 라벨
            int yStep = (height - 70) / maxValue;
            for (int i = 0; i <= maxValue; i++) {
                int y = height - 50 - (i * yStep);
                g2d.drawLine(37, y, 43, y);
                g2d.drawString(String.valueOf(i), 20, y + 5);
            }

            // 막대 그리기
            for (int i = 0; i < values.length; i++) {
                int x = 60 + (i * barWidth);
                int barHeight = (int) ((double) values[i] / maxValue * (height - 70));
                int y = height - 50 - barHeight;

                // 막대 그리기
                g2d.setColor(colors[i % colors.length]);
                g2d.fillRect(x, y, barWidth - 10, barHeight);

                // 막대 테두리
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, barWidth - 10, barHeight);

                // X축 라벨
                String label = labels[i] + "(" + values[i] + ")";
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x + (barWidth - 10 - labelWidth) / 2, height - 30);
            }
        }
    }
}