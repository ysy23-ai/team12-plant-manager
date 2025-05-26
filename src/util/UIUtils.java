package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 공통 유틸리티 클래스
 */
public class UIUtils {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static void showDatePicker(Component parent, JTextField field) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(parent), "날짜 선택", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(parent);

        JPanel calendarPanel = new JPanel(new GridLayout(7, 7));
        final String[] daysOfWeek = {"일", "월", "화", "수", "목", "금", "토"};

        for (String day : daysOfWeek) {
            JLabel label = new JLabel(day, JLabel.CENTER);
            calendarPanel.add(label);
        }

        Calendar calendar = Calendar.getInstance();
        try {
            Date date = DATE_FORMAT.parse(field.getText());
            calendar.setTime(date);
        } catch (ParseException e) {
            // 파싱 오류 시 현재 날짜 사용
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        JPanel headerPanel = new JPanel();
        JComboBox<Integer> yearCombo = new JComboBox<>();
        for (int i = year - 5; i <= year + 5; i++) {
            yearCombo.addItem(i);
        }
        yearCombo.setSelectedItem(year);

        JComboBox<String> monthCombo = new JComboBox<>();
        for (int i = 0; i < 12; i++) {
            monthCombo.addItem((i + 1) + "월");
        }
        monthCombo.setSelectedIndex(month);

        headerPanel.add(yearCombo);
        headerPanel.add(monthCombo);
        dialog.add(headerPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(6, 7));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        Calendar prevMonth = (Calendar) calendar.clone();
        prevMonth.add(Calendar.MONTH, -1);
        int daysInPrevMonth = prevMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < 42; i++) {
            final JButton dayButton = new JButton();
            if (i < firstDayOfWeek) {
                int day = daysInPrevMonth - firstDayOfWeek + i + 1;
                dayButton.setText(String.valueOf(day));
                dayButton.setEnabled(false);
            } else if (i < firstDayOfWeek + daysInMonth) {
                int day = i - firstDayOfWeek + 1;
                dayButton.setText(String.valueOf(day));
                final int selectedDay = day;
                dayButton.addActionListener(e -> {
                    int selectedYear = (int) yearCombo.getSelectedItem();
                    int selectedMonth = monthCombo.getSelectedIndex();
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);
                    field.setText(DATE_FORMAT.format(selectedDate.getTime()));
                    dialog.dispose();
                });
            } else {
                int day = i - firstDayOfWeek - daysInMonth + 1;
                dayButton.setText(String.valueOf(day));
                dayButton.setEnabled(false);
            }
            daysPanel.add(dayButton);
        }

        dialog.add(daysPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    public static JButton createDashboardButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(200, 100));
        button.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        return button;
    }
    
    public static boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    public static JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(15, 15));
        panel.add(colorBox);
        panel.add(new JLabel(text));
        return panel;
    }
    
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }
    
    public static void showInfoMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void showWarningMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }
    
    public static void showErrorMessage(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    public static boolean areAllFieldsFilled(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public static String getCurrentDateString() {
        return DATE_FORMAT.format(new Date());
    }
    
    public static boolean isValidDate(String dateStr) {
        try {
            DATE_FORMAT.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}