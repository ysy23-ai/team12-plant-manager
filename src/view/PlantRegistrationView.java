package view;

import util.UIUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 식물 등록 뷰 - UI만 담당
 * Controller가 이벤트를 처리하고, 이 클래스는 화면 표시만 담당
 */
public class PlantRegistrationView extends JFrame {
    
    // 입력 폼 컴포넌트들
    private JTextField idField;
    private JTextField nameField;
    private JTextField speciesField;
    private JTextField plantDateField;
    private JTextField locationField;
    private JTextArea notesArea;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton datePickerButton;
    
    // 식물 목록 테이블 컴포넌트들
    private JTable plantsTable;
    private DefaultTableModel tableModel;
    private JButton editButton;
    private JButton deleteButton;
    private JButton refreshButton;
    
    public PlantRegistrationView() {
        initializeUI();
    }
    
    /**
     * UI 초기화 - 모든 화면 구성 요소 생성
     */
    private void initializeUI() {
        setTitle("식물 등록/관리");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // 좌측 폼 패널과 우측 리스트 패널을 스플릿으로 분리
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                              createFormPanel(), 
                                              createListPanel());
        splitPane.setDividerLocation(420);
        splitPane.setResizeWeight(0.5); // 양쪽 균등 분할
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    /**
     * 입력 폼 패널 생성 (좌측)
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("식물 정보 입력"));
        
        // 입력 필드들을 담을 패널
        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ID 필드 (숨김 - Controller에서 관리용)
        idField = new JTextField(20);
        idField.setVisible(false);
        
        // 식물 이름 입력
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        JLabel nameLabel = new JLabel("식물 이름:");
        nameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(nameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        nameField = new JTextField(20);
        nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(nameField, gbc);
        
        // 종류 입력
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        JLabel speciesLabel = new JLabel("종류:");
        speciesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(speciesLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        speciesField = new JTextField(20);
        speciesField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(speciesField, gbc);
        
        // 식재 일자 입력
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        JLabel plantDateLabel = new JLabel("식재 일자:");
        plantDateLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(plantDateLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 1;
        plantDateField = new JTextField(15);
        plantDateField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        // 현재 날짜로 초기화
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        plantDateField.setText(dateFormat.format(new Date()));
        fieldsPanel.add(plantDateField, gbc);
        
        // 날짜 선택 버튼
        gbc.gridx = 2; gbc.gridwidth = 1;
        datePickerButton = new JButton("📅");
        datePickerButton.setPreferredSize(new Dimension(40, 25));
        datePickerButton.setToolTipText("날짜 선택");
        fieldsPanel.add(datePickerButton, gbc);
        
        // 재배 위치 입력
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        JLabel locationLabel = new JLabel("재배 위치:");
        locationLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        fieldsPanel.add(locationLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        locationField = new JTextField(20);
        locationField.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        fieldsPanel.add(locationField, gbc);
        
        // 메모 입력
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        JLabel notesLabel = new JLabel("메모:");
        notesLabel.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        notesLabel.setVerticalAlignment(SwingConstants.TOP);
        fieldsPanel.add(notesLabel, gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2; gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        notesArea = new JTextArea(4, 20);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        notesArea.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane notesScrollPane = new JScrollPane(notesArea);
        notesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        fieldsPanel.add(notesScrollPane, gbc);
        
        // 버튼 패널
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3; gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        saveButton = new JButton("저장");
        cancelButton = new JButton("취소");
        
        // 버튼 스타일링
        saveButton.setFont(new Font("맑은 고딕", Font.BOLD, 12));
        cancelButton.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        saveButton.setPreferredSize(new Dimension(80, 30));
        cancelButton.setPreferredSize(new Dimension(80, 30));
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        fieldsPanel.add(buttonPanel, gbc);
        
        panel.add(fieldsPanel, BorderLayout.CENTER);
        
        // 안내 메시지 패널
        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel infoLabel = new JLabel("<html><small>💡 팁: 테이블에서 식물을 더블클릭하면 편집할 수 있습니다.</small></html>");
        infoLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 10));
        infoLabel.setForeground(Color.GRAY);
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * 식물 목록 패널 생성 (우측)
     */
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("등록된 식물 목록"));
        
        // 테이블 생성
        String[] columns = {"ID", "이름", "종류", "식재 일자", "위치"};
        Object[][] data = {
            {"1", "토마토", "채소", "2025-01-15", "온실 A-1"},
            {"2", "상추", "채소", "2025-02-10", "온실 B-3"},
            {"3", "딸기", "과일", "2025-03-05", "온실 C-2"},
            {"4", "바질", "허브", "2025-03-20", "실내 D-1"},
            {"5", "당근", "뿌리채소", "2025-04-01", "야외 E-2"}
        };
        
        tableModel = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀 편집 비활성화
            }
        };
        
        plantsTable = new JTable(tableModel);
        plantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plantsTable.setRowHeight(28);
        plantsTable.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        plantsTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 12));
        
        // 테이블 컬럼 너비 조정
        plantsTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        plantsTable.getColumnModel().getColumn(1).setPreferredWidth(100); // 이름
        plantsTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // 종류
        plantsTable.getColumnModel().getColumn(3).setPreferredWidth(100); // 식재 일자
        plantsTable.getColumnModel().getColumn(4).setPreferredWidth(120); // 위치
        
        // 테이블 선택 시 하이라이트
        plantsTable.setSelectionBackground(new Color(216, 184, 229));
        plantsTable.setSelectionForeground(Color.BLACK);
        
        JScrollPane scrollPane = new JScrollPane(plantsTable);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // 테이블 상단 정보 패널
        JPanel tableInfoPanel = new JPanel(new BorderLayout());
        JLabel countLabel = new JLabel("총 " + tableModel.getRowCount() + "개의 식물이 등록되어 있습니다.");
        countLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        countLabel.setForeground(Color.DARK_GRAY);
        tableInfoPanel.add(countLabel, BorderLayout.WEST);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 10));
        editButton = new JButton("편집");
        deleteButton = new JButton("삭제");
        refreshButton = new JButton("새로고침");
        
        // 버튼 스타일링
        editButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        deleteButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        refreshButton.setFont(new Font("맑은 고딕", Font.PLAIN, 11));
        
        editButton.setPreferredSize(new Dimension(80, 28));
        deleteButton.setPreferredSize(new Dimension(80, 28));
        refreshButton.setPreferredSize(new Dimension(90, 28));
        
        // 삭제 버튼 색상 변경
        deleteButton.setForeground(Color.RED);
        
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // 패널 조합
        panel.add(tableInfoPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ========== UI 업데이트 메소드들 ==========
    
    /**
     * 입력 폼 초기화
     */
    public void clearForm() {
        idField.setText("");
        nameField.setText("");
        speciesField.setText("");
        plantDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        locationField.setText("");
        notesArea.setText("");
        nameField.requestFocus(); // 포커스를 이름 필드로 이동
    }
    
    /**
     * 식물 데이터로 폼 설정 (편집 모드)
     */
    public void setPlantData(String id, String name, String species, String plantDate, String location) {
        idField.setText(id);
        nameField.setText(name);
        speciesField.setText(species);
        plantDateField.setText(plantDate);
        locationField.setText(location);
        notesArea.setText(""); // 메모는 별도로 설정
        nameField.requestFocus();
    }
    
    /**
     * 테이블 행 수 정보 업데이트
     */
    public void updateTableInfo() {
        // 테이블 정보 라벨 찾아서 업데이트
        Container parent = plantsTable.getParent().getParent().getParent(); // JScrollPane -> Panel
        if (parent instanceof JPanel) {
            Component[] components = ((JPanel) parent).getComponents();
            for (Component comp : components) {
                if (comp instanceof JPanel) {
                    Component[] subComps = ((JPanel) comp).getComponents();
                    for (Component subComp : subComps) {
                        if (subComp instanceof JLabel) {
                            ((JLabel) subComp).setText("총 " + tableModel.getRowCount() + "개의 식물이 등록되어 있습니다.");
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 입력 필드 유효성 시각적 표시
     */
    public void highlightInvalidFields(boolean nameValid, boolean speciesValid, boolean dateValid) {
        // 유효하지 않은 필드는 빨간 테두리로 표시
        nameField.setBorder(nameValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        speciesField.setBorder(speciesValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
        
        plantDateField.setBorder(dateValid ? 
            UIManager.getBorder("TextField.border") : 
            BorderFactory.createLineBorder(Color.RED, 2));
    }
    
    /**
     * 모든 필드 테두리 정상화
     */
    public void resetFieldBorders() {
        nameField.setBorder(UIManager.getBorder("TextField.border"));
        speciesField.setBorder(UIManager.getBorder("TextField.border"));
        plantDateField.setBorder(UIManager.getBorder("TextField.border"));
    }
    
    // ========== Getter 메소드들 - Controller가 UI 컴포넌트에 접근하기 위함 ==========
    
    // 입력 필드 Getter들
    public JTextField getIdField() { return idField; }
    public JTextField getNameField() { return nameField; }
    public JTextField getSpeciesField() { return speciesField; }
    public JTextField getPlantDateField() { return plantDateField; }
    public JTextField getLocationField() { return locationField; }
    public JTextArea getNotesArea() { return notesArea; }
    
    // 버튼 Getter들
    public JButton getSaveButton() { return saveButton; }
    public JButton getCancelButton() { return cancelButton; }
    public JButton getDatePickerButton() { return datePickerButton; }
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getRefreshButton() { return refreshButton; }
    
    // 테이블 Getter들
    public JTable getPlantsTable() { return plantsTable; }
    public DefaultTableModel getTableModel() { return tableModel; }
    
    // ========== 편의 메소드들 ==========
    
    /**
     * 현재 선택된 테이블 행 반환
     */
    public int getSelectedTableRow() {
        return plantsTable.getSelectedRow();
    }
    
    /**
     * 테이블 행 선택
     */
    public void selectTableRow(int row) {
        if (row >= 0 && row < tableModel.getRowCount()) {
            plantsTable.setRowSelectionInterval(row, row);
        }
    }
    
    /**
     * 저장 버튼 활성화/비활성화
     */
    public void setSaveButtonEnabled(boolean enabled) {
        saveButton.setEnabled(enabled);
    }
    
    /**
     * 편집 모드 UI 표시
     */
    public void setEditMode(boolean editMode) {
        if (editMode) {
            setTitle("식물 등록/관리 - 편집 모드");
            saveButton.setText("💾 수정");
        } else {
            setTitle("식물 등록/관리");
            saveButton.setText("💾 저장");
        }
    }
    
    /**
     * 로딩 상태 표시
     */
    public void setLoading(boolean loading) {
        saveButton.setEnabled(!loading);
        editButton.setEnabled(!loading);
        deleteButton.setEnabled(!loading);
        refreshButton.setEnabled(!loading);
        
        if (loading) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}