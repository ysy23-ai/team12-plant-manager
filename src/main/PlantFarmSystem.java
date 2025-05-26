package main;

import view.*;
import controller.*;
import model.*;
import util.UIUtils;

import javax.swing.*;

/**
 * 식물 농장 관리 시스템 메인 클래스
 * 애플리케이션 초기화 및 실행을 담당
 */
public class PlantFarmSystem {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // 시스템 기본 룩앤필 설정
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // 메인 대시보드 시작
            MainDashboardView dashboardView = new MainDashboardView();
            MainDashboardController dashboardController = new MainDashboardController(dashboardView);
            
            dashboardView.setVisible(true);
        });
    }
}