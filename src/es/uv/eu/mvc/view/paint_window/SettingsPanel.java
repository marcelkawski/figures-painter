package es.uv.eu.mvc.view.paint_window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

public class SettingsPanel extends JPanel {
    
    private PaintView paintView;
    private ToolsPanel toolsPanel;
    private JSlider currentThicknessSlider;
    private ColorsPanel currentColorsPanel;
    
    public SettingsPanel(PaintView paintView) {
        
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.paintView = paintView;
        
        this.toolsPanel = new ToolsPanel(paintView);
        this.currentThicknessSlider = new JSlider(0, 100);
        this.currentColorsPanel = new ColorsPanel();
        
        currentThicknessSlider.setMajorTickSpacing(10);
        currentThicknessSlider.setMinorTickSpacing(1);
        currentThicknessSlider.setValue(paintView.getModel().getCurrentThickness());
        currentThicknessSlider.setPaintTicks(true);
        currentThicknessSlider.setPaintLabels(true);
        currentThicknessSlider.setFont(new Font("Symbol", Font.PLAIN, 15)); 
        currentThicknessSlider.setPreferredSize(new Dimension(600, 84)); 
        // 100 (ColorsPanel height(the highest in the SettingsPanel)) - 2*8 (ColorsPanel empty border size)
        currentThicknessSlider.setMaximumSize(currentThicknessSlider.getPreferredSize());
        currentThicknessSlider.setMinimumSize(currentThicknessSlider.getPreferredSize());
        Border titledEtchedBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                "Thickness", TitledBorder.CENTER, TitledBorder.TOP);
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border border = BorderFactory.createCompoundBorder(titledEtchedBorder, emptyBorder);
        currentThicknessSlider.setBorder(border);
        currentThicknessSlider.setName("currentThicknessSlider");
        
        Dimension gap = new Dimension(70, 0);
        
        this.add(Box.createRigidArea(gap));
        this.add(toolsPanel);
        this.add(Box.createHorizontalGlue());
        this.add(currentThicknessSlider);
        this.add(Box.createHorizontalGlue());
        this.add(currentColorsPanel);
        this.add(Box.createRigidArea(gap));
    }
    
    public Color getCurrentOutlineColorByButton(JButton button) {
        return currentColorsPanel.getCurrentOutlineColorByButton(button);
    }
    
    public Color getCurrentBackgroundColorByButton(JButton button) {
        return currentColorsPanel.getCurrentBackgroundColorByButton(button);
    }
    
    public void displayToolsPanelButtonAsChosen(JButton chosenButton) {
        toolsPanel.displayButtonAsChosen(chosenButton);
    }
    
    public void displayOtherToolsPanelButtonsAsUnchosen(JButton chosenButton) {
        toolsPanel.displayOtherButtonsAsUnchosen(chosenButton);
    }
    
    public void updateCurrentThicknessSlider(int newCurrentThicknessValue) {
        if (newCurrentThicknessValue >= currentThicknessSlider.getMinimum() && 
                newCurrentThicknessValue <= currentThicknessSlider.getMaximum())
            currentThicknessSlider.setValue(newCurrentThicknessValue);
    }
    
    public void setChangeListener(ChangeListener cl) {
        currentThicknessSlider.addChangeListener(cl);
    }
    
    public void setActionListener(ActionListener al) {
        toolsPanel.setActionListener(al);
        currentColorsPanel.setActionListener(al);
    }
}
