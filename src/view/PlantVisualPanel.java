package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

public class PlantVisualPanel {
    public static class StrawberryPanelV5 extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawStrawberry((Graphics2D) g, getWidth() / 2, getHeight() / 2);
        }

        private void drawStrawberry(Graphics2D g2d, int cx, int cy) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = 160;
            int height = 140;

            // 그림자
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(cx - (width - 30) / 2, cy + height / 2 - 10, width - 30, 20);

            // 딸기 본체
            GeneralPath strawberry = new GeneralPath();
            strawberry.moveTo(cx, cy - height / 2);
            strawberry.curveTo(cx - width / 2, cy - height / 2, cx - width / 2, cy + height / 4, cx, cy + height / 2);
            strawberry.curveTo(cx + width / 2, cy + height / 4, cx + width / 2, cy - height / 2, cx, cy - height / 2);
            strawberry.closePath();

            GradientPaint gradient = new GradientPaint(cx, cy - height / 2, Color.WHITE, cx, cy + height / 2, Color.RED);
            g2d.setPaint(gradient);
            g2d.fill(strawberry);

            // 잎사귀
            g2d.setColor(new Color(0, 128, 0));
            for (int i = -1; i <= 1; i++) {
                GeneralPath leaf = new GeneralPath();
                leaf.moveTo(cx, cy - height / 2);
                leaf.curveTo(cx + i * 10, cy - height / 2 - 25, cx + i * 30, cy - height / 2 - 10, cx + i * 40, cy - height / 2);
                leaf.closePath();
                g2d.fill(leaf);
            }

            // 씨앗
            g2d.setColor(Color.YELLOW);
            for (int y = cy - height / 3; y < cy + height / 2; y += 16) {
                for (int x = cx - width / 2 + 15; x < cx + width / 2 - 15; x += 20) {
                    if (strawberry.contains(new Point2D.Double(x, y))) {
                        g2d.fillOval(x - 1, y - 1, 2, 2);
                    }
                }
            }
        }
    }

    public static class CarrotPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawCarrot((Graphics2D) g, getWidth() / 2, getHeight() / 2);
        }

        private void drawCarrot(Graphics2D g2d, int cx, int cy) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = 80;
            int height = 180;

            // 그림자
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(cx - 30, cy + height / 2 - 10, 60, 15);

            // 당근 본체
            GeneralPath carrot = new GeneralPath();
            carrot.moveTo(cx, cy + height / 2);  // 아래 끝 (뾰족한 부분)
            carrot.curveTo(cx - width / 2, cy + height / 4, cx - width / 2, cy - height / 4, cx - width / 2, cy - height / 2 + 10);  // 왼쪽 곡선
            carrot.curveTo(cx - width / 2, cy - height / 2, cx - width / 4, cy - height / 2, cx, cy - height / 2);  // 상단 왼쪽 모서리 둥글게
            carrot.curveTo(cx + width / 4, cy - height / 2, cx + width / 2, cy - height / 2, cx + width / 2, cy - height / 2 + 10);  // 상단 오른쪽 모서리 둥글게
            carrot.curveTo(cx + width / 2, cy - height / 4, cx + width / 2, cy + height / 4, cx, cy + height / 2);  // 오른쪽 곡선
            carrot.closePath();

            // 당근 그라데이션
            GradientPaint gradient = new GradientPaint(
                    cx - width / 2, cy - height / 2, new Color(255, 200, 100),
                    cx + width / 2, cy + height / 2, new Color(255, 140, 0)
            );
            g2d.setPaint(gradient);
            g2d.fill(carrot);

            // 당근 가로 줄무늬
            g2d.setColor(new Color(255, 120, 0, 100));
            for (int i = 0; i < 7; i++) {
                int y = cy - height / 2 + (i * height / 8) + 15;
                int lineWidth = width - (i * 8);  // 아래로 갈수록 좁아짐
                if (lineWidth > 10) {
                    g2d.fillRoundRect(cx - lineWidth / 2, y, lineWidth, 3, 2, 2);
                }
            }

            // 당근 잎사귀
            g2d.setColor(new Color(34, 139, 34));
            for (int i = -2; i <= 2; i++) {
                // 메인 줄기
                GeneralPath leafStem = new GeneralPath();
                leafStem.moveTo(cx + i * 8, cy - height / 2);
                leafStem.lineTo(cx + i * 8, cy - height / 2 - 40);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(leafStem);

                // 잎사귀들
                for (int j = 0; j < 4; j++) {
                    int leafY = cy - height / 2 - 10 - (j * 8);

                    // 왼쪽 잎
                    GeneralPath leftLeaf = new GeneralPath();
                    leftLeaf.moveTo(cx + i * 8, leafY);
                    leftLeaf.curveTo(cx + i * 8 - 8, leafY - 4, cx + i * 8 - 12, leafY - 2, cx + i * 8 - 15, leafY + 2);
                    leftLeaf.curveTo(cx + i * 8 - 12, leafY + 4, cx + i * 8 - 8, leafY + 2, cx + i * 8, leafY);
                    g2d.fill(leftLeaf);

                    // 오른쪽 잎
                    GeneralPath rightLeaf = new GeneralPath();
                    rightLeaf.moveTo(cx + i * 8, leafY);
                    rightLeaf.curveTo(cx + i * 8 + 8, leafY - 4, cx + i * 8 + 12, leafY - 2, cx + i * 8 + 15, leafY + 2);
                    rightLeaf.curveTo(cx + i * 8 + 12, leafY + 4, cx + i * 8 + 8, leafY + 2, cx + i * 8, leafY);
                    g2d.fill(rightLeaf);
                }
            }

            // 당근 끝부분 하이라이트
            g2d.setColor(new Color(255, 255, 255, 80));
            g2d.fillOval(cx - 3, cy + height / 2 - 8, 6, 8);

            // 상단 하이라이트
            GradientPaint topHighlight = new GradientPaint(
                    cx - width / 2, cy - height / 2, new Color(255, 255, 255, 120),
                    cx, cy - height / 2 + 30, new Color(255, 255, 255, 0)
            );
            g2d.setPaint(topHighlight);
            Ellipse2D.Double highlight = new Ellipse2D.Double(cx - width / 2, cy - height / 2, width, 60);
            g2d.fill(highlight);
        }
    }

    public static class GrapePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawGrape((Graphics2D) g, getWidth() / 2, getHeight() / 2);
        }

        private void drawGrape(Graphics2D g2d, int cx, int cy) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = 50;
            int height = 80;

            // 줄기
            g2d.setColor(new Color(101, 67, 33));
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.drawLine(cx, cy - height / 2 - 30, cx, cy - height / 2 + 10);

            // 포도잎
            drawGrapeLeaf(g2d, cx - 25, cy - height / 2 - 15, 40, 35, -20);
            drawGrapeLeaf(g2d, cx + 20, cy - height / 2 - 10, 35, 30, 25);

            // 포도 송이
            int[] rowWidths = { 2, 3, 3, 2, 2, 1}; // 각 줄의 포도 개수 (줄임)
            int startY = cy - height / 2 + 20;
            int grapeSize = 18; // 크기 증가
            int spacing = 22; // 간격도 증가

            for (int row = 0; row < rowWidths.length; row++) {
                int grapeCount = rowWidths[row];
                int rowY = startY + row * (spacing - 2);

                // 각 줄의 포도들을 중앙 정렬
                int startX = cx - (grapeCount - 1) * spacing / 2;

                for (int col = 0; col < grapeCount; col++) {
                    int grapeX = startX + col * spacing;

                    // 랜덤성을 위해 위치 조정
                    int offsetX = (int)(Math.sin(row * 0.5 + col * 0.8) * 3);
                    int offsetY = (int)(Math.cos(row * 0.7 + col * 0.6) * 2);

                    drawSingleGrape(g2d, grapeX + offsetX, rowY + offsetY, grapeSize);
                }
            }
        }

        private void drawSingleGrape(Graphics2D g2d, int x, int y, int size) {
            // 포도 베이스 색상
            Color baseColor = new Color(75, 0, 130);
            Color lightColor = new Color(138, 43, 226);
            Color darkColor = new Color(50, 0, 87);

            // 포도알 그라데이션
            RadialGradientPaint gradient = new RadialGradientPaint(
                    x - size / 4, y - size / 4, size * 0.8f,
                    new float[]{0f, 0.7f, 1f},
                    new Color[]{lightColor, baseColor, darkColor}
            );

            g2d.setPaint(gradient);
            g2d.fillOval(x - size / 2, y - size / 2, size, size);

            // 하이라이트
            g2d.setColor(new Color(200, 150, 255, 120));
            g2d.fillOval(x - size / 2 + 2, y - size / 2 + 2, size / 3, size / 3);

            // 포도알 테두리
            g2d.setColor(new Color(40, 0, 60));
            g2d.setStroke(new BasicStroke(0.5f));
            g2d.drawOval(x - size / 2, y - size / 2, size, size);
        }

        private void drawGrapeLeaf(Graphics2D g2d, int x, int y, int w, int h, double rotation) {
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));

            // 잎사귀 색상
            Color leafGreen = new Color(34, 139, 34);
            Color lightGreen = new Color(124, 205, 124);
            Color darkGreen = new Color(0, 100, 0);

            // 포도잎 모양
            GeneralPath leaf = new GeneralPath();

            // 잎의 중심에서 시작
            leaf.moveTo(0, h / 2);

            // 아래쪽 중앙 끝
            leaf.lineTo(0, h / 2);

            // 오른쪽 아래 날개
            leaf.curveTo(w / 4, h / 2, w / 2, h / 4, w / 2, 0);

            // 오른쪽 위 날개
            leaf.curveTo(w / 2, -h / 4, w / 4, -h / 2, 0, -h / 2);

            // 왼쪽 위 날개
            leaf.curveTo(-w / 4, -h / 2, -w / 2, -h / 4, -w / 2, 0);

            // 왼쪽 아래 날개
            leaf.curveTo(-w / 2, h / 4, -w / 4, h / 2, 0, h / 2);

            leaf.closePath();

            // 잎사귀 그라데이션
            GradientPaint leafGradient = new GradientPaint(
                    0, -h / 2, lightGreen,
                    0, h / 2, darkGreen
            );

            g2d.setPaint(leafGradient);
            g2d.fill(leaf);

            // 잎맥 그리기
            g2d.setColor(new Color(0, 80, 0));
            g2d.setStroke(new BasicStroke(1.0f));

            // 중앙 잎맥
            g2d.drawLine(0, -h / 2 + 3, 0, h / 2 - 3);

            // 측면 잎맥들
            g2d.drawLine(0, -h / 4, w / 3, -h / 3);
            g2d.drawLine(0, -h / 4, -w / 3, -h / 3);
            g2d.drawLine(0, h / 6, w / 4, h / 4);
            g2d.drawLine(0, h / 6, -w / 4, h / 4);

            // 잎사귀 테두리
            g2d.setColor(darkGreen);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.draw(leaf);

            g2d.rotate(-Math.toRadians(rotation));
            g2d.translate(-x, -y);
        }
    }

    public static class HallabongPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawHallabong((Graphics2D) g, getWidth() / 2, getHeight() / 2);
        }

        private void drawHallabong(Graphics2D g2d, int cx, int cy) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int radius = 70;

            // 그림자
            g2d.setColor(new Color(0, 0, 0, 50));
            g2d.fillOval(cx - radius + 10, cy + radius - 5, (radius - 10) * 2, 15);

            // 한라봉 본체
            Ellipse2D.Double body = new Ellipse2D.Double(cx - radius, cy - radius, radius * 2, radius * 2);

            // 한라봉 그라데이션
            GradientPaint gradient = new GradientPaint(
                    cx - radius, cy - radius, new Color(255, 200, 80),
                    cx + radius, cy + radius, new Color(255, 140, 0)
            );
            g2d.setPaint(gradient);
            g2d.fill(body);

            // 한라봉 돌출부
            Ellipse2D.Double protrusion = new Ellipse2D.Double(cx - 15, cy - radius - 25, 30, 35);
            GradientPaint protrusionGradient = new GradientPaint(
                    cx - 15, cy - radius - 25, new Color(255, 180, 60),
                    cx + 15, cy - radius + 10, new Color(255, 120, 0)
            );
            g2d.setPaint(protrusionGradient);
            g2d.fill(protrusion);

            // 한라봉 표면 점
            g2d.setColor(new Color(255, 100, 0, 150));
            for (int i = 0; i < 80; i++) {
                double angle = Math.random() * Math.PI * 2;
                double distance = Math.random() * (radius - 10);
                int x = (int)(cx + Math.cos(angle) * distance);
                int y = (int)(cy + Math.sin(angle) * distance);

                if (body.contains(x, y)) {
                    g2d.fillOval(x - 1, y - 1, 2, 2);
                }
            }

            // 돌출부 표면 점
            for (int i = 0; i < 15; i++) {
                double angle = Math.random() * Math.PI * 2;
                double distance = Math.random() * 12;
                int x = (int)(cx + Math.cos(angle) * distance);
                int y = (int)(cy - radius - 8 + Math.sin(angle) * distance);

                if (protrusion.contains(x, y)) {
                    g2d.fillOval(x - 1, y - 1, 2, 2);
                }
            }

            // 한라봉 세로 홈
            g2d.setColor(new Color(255, 110, 0, 120));
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < 8; i++) {
                double angle = (Math.PI * 2 * i) / 8;
                int startX = (int)(cx + Math.cos(angle) * (radius - 20));
                int startY = (int)(cy + Math.sin(angle) * (radius - 20));
                int endX = (int)(cx + Math.cos(angle) * (radius - 5));
                int endY = (int)(cy + Math.sin(angle) * (radius - 5));

                g2d.drawLine(startX, startY, endX, endY);
            }

            // 하이라이트
            GradientPaint highlight = new GradientPaint(
                    cx - radius + 10, cy - radius + 10, new Color(255, 255, 255, 100),
                    cx - radius + 40, cy - radius + 40, new Color(255, 255, 255, 0)
            );
            g2d.setPaint(highlight);
            Ellipse2D.Double highlightArea = new Ellipse2D.Double(cx - radius + 10, cy - radius + 10, 60, 60);
            g2d.fill(highlightArea);

            // 돌출부 하이라이트
            g2d.setPaint(new GradientPaint(
                    cx - 10, cy - radius - 20, new Color(255, 255, 255, 80),
                    cx + 5, cy - radius - 10, new Color(255, 255, 255, 0)
            ));
            Ellipse2D.Double protrusionHighlight = new Ellipse2D.Double(cx - 10, cy - radius - 20, 20, 25);
            g2d.fill(protrusionHighlight);

            // 꼭지
            g2d.setColor(new Color(21, 143, 12));
            g2d.fillOval(cx - 6, cy - radius - 23, 12, 3);

        }
    }

    public static class LettucePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawLettuce((Graphics2D) g, getWidth() / 2, getHeight() / 2);
        }

        private void drawLettuce(Graphics2D g2d, int cx, int cy) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = 180;
            int height = 160;

            // 그림자
            g2d.setColor(new Color(0, 0, 0, 30));
            g2d.fillOval(cx - (width - 40) / 2, cy + height / 2 - 5, width - 40, 15);

            // 바깥쪽 잎사귀들
            drawLettuceLeaf(g2d, cx - 30, cy - 20, 70, 80, new Color(100, 180, 80), -15);
            drawLettuceLeaf(g2d, cx + 25, cy - 15, 65, 75, new Color(120, 190, 90), 20);
            drawLettuceLeaf(g2d, cx - 45, cy + 10, 60, 70, new Color(90, 170, 70), -25);
            drawLettuceLeaf(g2d, cx + 40, cy + 15, 55, 65, new Color(110, 185, 85), 30);

            // 중간층 잎사귀들
            drawLettuceLeaf(g2d, cx - 15, cy - 10, 60, 70, new Color(130, 200, 100), -8);
            drawLettuceLeaf(g2d, cx + 10, cy - 5, 55, 65, new Color(140, 210, 110), 12);
            drawLettuceLeaf(g2d, cx - 20, cy + 20, 50, 60, new Color(125, 195, 95), -18);
            drawLettuceLeaf(g2d, cx + 15, cy + 25, 45, 55, new Color(135, 205, 105), 22);

            // 안쪽 잎사귀들
            drawLettuceLeaf(g2d, cx - 8, cy, 40, 50, new Color(160, 230, 130), -5);
            drawLettuceLeaf(g2d, cx + 5, cy + 5, 35, 45, new Color(170, 240, 140), 8);
            drawLettuceLeaf(g2d, cx - 5, cy + 15, 30, 40, new Color(155, 225, 125), -10);

        }

        private void drawLettuceLeaf(Graphics2D g2d, int x, int y, int w, int h, Color baseColor, double rotation) {
            g2d.translate(x, y);
            g2d.rotate(Math.toRadians(rotation));

            // 잎맥 그라데이션
            GradientPaint gradient = new GradientPaint(
                    0, -h/2, new Color(baseColor.getRed() + 20, Math.min(255, baseColor.getGreen() + 30), baseColor.getBlue() + 15),
                    0, h/2, new Color(Math.max(0, baseColor.getRed() - 20), Math.max(0, baseColor.getGreen() - 20), Math.max(0, baseColor.getBlue() - 10))
            );

            // 주름진 잎사귀 모양
            GeneralPath leaf = new GeneralPath();
            leaf.moveTo(0, -h/2);

            // 왼쪽 가장자리 (물결 모양)
            leaf.curveTo(-w/3, -h/3, -w/2 - 5, -h/4, -w/2, 0);
            leaf.curveTo(-w/2 + 3, h/6, -w/3 - 2, h/3, -w/4, h/2);
            leaf.curveTo(-w/6, h/2 + 2, 0, h/2 + 3, 0, h/2);

            // 오른쪽 가장자리 (물결 모양)
            leaf.curveTo(w/6, h/2 + 2, w/4, h/2, w/3 + 2, h/3);
            leaf.curveTo(w/2 - 3, h/6, w/2, 0, w/2 + 5, -h/4);
            leaf.curveTo(w/3, -h/3, 0, -h/2, 0, -h/2);

            leaf.closePath();

            g2d.setPaint(gradient);
            g2d.fill(leaf);

            // 잎맥
            g2d.setColor(new Color(baseColor.getRed() + 30, Math.min(255, baseColor.getGreen() + 40), baseColor.getBlue() + 20));
            g2d.setStroke(new BasicStroke(1.0f));

            // 중앙 잎맥
            g2d.drawLine(0, -h/2 + 5, 0, h/2 - 5);

            // 측면 잎맥들
            for (int i = -h/3; i < h/3; i += h/6) {
                g2d.drawLine(0, i, -w/4, i + h/8);
                g2d.drawLine(0, i, w/4, i + h/8);
            }

            g2d.rotate(-Math.toRadians(rotation));
            g2d.translate(-x, -y);
        }
    }
}
