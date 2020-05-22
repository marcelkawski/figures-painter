package es.uv.eu.mvc.controller;

import es.uv.eu.mvc.model.PaintModel;
import es.uv.eu.mvc.view.default_values_window.DVWView;
import es.uv.eu.mvc.view.paint_window.PaintView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// TODO ZMIENIC ZEBY CVW NIE BYL ZAWSZE NAJWYZEJ
// TODO PO ZMIANIE CURRENT NA DFAULT UCINALO RYSUNEK

public class PaintController {
    
    private PaintModel model;
    private DVWView defValWindView;
    private PaintView paintView;
    
    public PaintController(PaintModel model, DVWView defValWindView, PaintView paintView) {
        this.model = model;
        this.defValWindView = defValWindView;
        this.paintView = paintView;
        
        PaintWindowListener paintWindowListener = new PaintWindowListener();
        
        defValWindView.addWindowListener(paintWindowListener);
        paintView.addWindowListener(paintWindowListener);
        
        PaintActionListener paintActionListener = new PaintActionListener();
        PaintChangeListener paintChangeListener = new PaintChangeListener();
        
        defValWindView.setActionListener(paintActionListener);
        defValWindView.setChangeListener(paintChangeListener);
        
        paintView.setActionListener(paintActionListener);
        paintView.setChangeListener(paintChangeListener);
        paintView.setMouseListener(new PainterMouseListener());
    }
    
    private class PaintActionListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent ae) {
            String command = ae.getActionCommand();
            JButton button;
            if (ae.getSource() instanceof JButton) {
                button = (JButton) ae.getSource();
                switch (command) {
                    case "setDrawingRectanglesMode":
                        model.setDrawingMode(PaintModel.DrawingMode.DRAWING_RECTANGLES);
                        paintView.displayToolsPanelButtonAsChosen(button);
                        paintView.displayOtherToolsPanelButtonsAsUnchosen(button);
                        break;
                    case "setDrawingTrianglesMode":
                        model.setDrawingMode(PaintModel.DrawingMode.DRAWING_TRIANGLES);
                        paintView.displayToolsPanelButtonAsChosen(button);
                        paintView.displayOtherToolsPanelButtonsAsUnchosen(button);
                        break;
                    case "setDrawingCirclesMode":
                        model.setDrawingMode(PaintModel.DrawingMode.DRAWING_CIRCLES);
                        paintView.displayToolsPanelButtonAsChosen(button);
                        paintView.displayOtherToolsPanelButtonsAsUnchosen(button);
                        break;
                    case "setEraserMode":
                        model.setDrawingMode(PaintModel.DrawingMode.ERASER);
                        paintView.displayToolsPanelButtonAsChosen(button);
                        paintView.displayOtherToolsPanelButtonsAsUnchosen(button);
                        break;
                    case "changeCurrentOutlineColor":
                        model.setCurrentOutlineColor(paintView.getCurrentOutlineColorByButton(button));
                        paintView.updateCurrentOutlineColorLabel(model.getCurrentOutlineColor());
                        break;
                    case "changeCurrentBackgroundColor":
                        model.setCurrentBackgroundColor(paintView.getCurrentBackgroundColorByButton(button));
                        paintView.updateCurrentBackgroundColorLabel(model.getCurrentBackgroundColor());
                        break;
                    case "changeDefaultOutlineColor":
                        model.setDefaultOutlineColor(defValWindView.getDefaultOutlineColorByButton(button));
                        defValWindView.updateDefaultOutlineColorLabel(model.getDefaultOutlineColor());
                        break;
                    case "changeDefaultBackgroundColor":
                        model.setDefaultBackgroundColor(defValWindView.getDefaultBackgroundColorByButton(button));
                        defValWindView.updateDefaultBackgroundColorLabel(model.getDefaultBackgroundColor());
                        break;
                    case "confirmDefaultValues":
                        defValWindView.dispatchEvent(new WindowEvent(defValWindView, WindowEvent.WINDOW_CLOSING));
                        model.setCurrentToDefault();
                        //paintView.updateChosenValuesAfterDefaultValuesChange(model.getCurrentThickness(),
                                //model.getCurrentOutlineColor(), model.getCurrentBackgroundColor());
                        paintView.setEnabled(true);
                        break;
                    default:
                        System.out.println("Controller: Command " + command + 
                            " not recognised.");
                        break;
                }
            }
            else {
                switch(command) {
                    case "changeDefaultValues":
                        defValWindView = new DVWView(model);        
                        defValWindView.addWindowListener(new PaintWindowListener());
                        defValWindView.setActionListener(new PaintActionListener());
                        defValWindView.setChangeListener(new PaintChangeListener());
                        break;
                    case "setCurrentValuesToDefault":
                        model.setCurrentToDefault();
                        paintView.updateChosenValuesToDefault(model.getCurrentThickness(),
                                model.getCurrentOutlineColor(), model.getCurrentBackgroundColor());
                        break;
                    case "exit":
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Controller: Command " + command + 
                            " not recognised.");
                        break;
                }
            }
        }
    }
    
    private class PaintChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent ce) {
            JSlider slider = (JSlider)ce.getSource();
            String sliderName = slider.getName();
            int newThickness = slider.getValue();
            switch(sliderName) {
                case "currentThicknessSlider":
                    model.setCurrentThickness(newThickness);
                    paintView.updateCurrentThicknessValueLabel(model.getCurrentThickness());
                    break;
                case "defaultThicknessSlider":
                    model.setDefaultThickness(newThickness);
                    defValWindView.updateDefaultThicknessValueLabel(model.getDefaultThickness());
                    break;
                default:
                    System.out.println("Controller: There is no " + sliderName + 
                        " slider.");
                    break;
            }
        }
    }
    
    private class PainterMouseListener extends MouseAdapter {
        
        private int rectangleStartingX, rectangleStartingY;
        private int circleStartingX, circleStartingY;
        private ArrayList<Integer> triangleXpoints = new ArrayList<>();
        private ArrayList<Integer> triangleYpoints = new ArrayList<>();
        private PaintModel.DrawingMode mode;

        @Override
        public void mousePressed(MouseEvent me) {
            mode = model.getDrawingMode();
            if (mode != null) {
                switch(mode) {
                    case DRAWING_RECTANGLES:
                        if(me.getButton() == MouseEvent.BUTTON1) {
                            rectangleStartingX = me.getX();
                            rectangleStartingY = me.getY();
                        }
                        break;
                    case DRAWING_CIRCLES:
                        if(me.getButton() == MouseEvent.BUTTON1) {
                            circleStartingX = me.getX();
                            circleStartingY = me.getY();
                        }
                        break;
                }
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent me) {
            if (mode != null) {
                switch(mode) {
                    case DRAWING_RECTANGLES:
                        if(me.getButton() == MouseEvent.BUTTON1) {
                            int mouseX = me.getX();
                            int mouseY = me.getY();
                            if(mouseX != rectangleStartingX && mouseY != rectangleStartingY) {
                                paintView.drawRectangle(rectangleStartingX, rectangleStartingY, mouseX, mouseY, 
                                    model.getCurrentOutlineColor(), model.getCurrentBackgroundColor(), 
                                    model.getCurrentThickness());
                                model.incrementDrawnFigures();
                                paintView.updateDrawnFiguresCounterValueLabel(model.getDrawnFiguresNumber());
                            }
                        }
                        break;
                    case DRAWING_CIRCLES:
                        if(me.getButton() == MouseEvent.BUTTON1) {
                            int currentThickness = model.getCurrentThickness();
                            double r = Math.sqrt( Math.pow(me.getX()-circleStartingX, 2) + Math.pow(me.getY()-circleStartingY, 2) );
                            int radius = (int)Math.round(r);
                            if (r > currentThickness) {
                                paintView.drawCircle(circleStartingX, circleStartingY, radius, model.getCurrentOutlineColor(), 
                                        model.getCurrentBackgroundColor(), model.getCurrentThickness());
                                model.incrementDrawnFigures();
                                paintView.updateDrawnFiguresCounterValueLabel(model.getDrawnFiguresNumber());
                            }
                        }
                        break;
                }
            }
        }
       
        @Override
        public void mouseClicked(MouseEvent me) {
            mode = model.getDrawingMode();
            if (mode == PaintModel.DrawingMode.DRAWING_TRIANGLES && me.getButton() == MouseEvent.BUTTON1) {
                if (triangleXpoints.size() < 3) {
                    triangleXpoints.add(me.getX());
                    triangleYpoints.add(me.getY());
                }
                if (triangleXpoints.size() == 3) {
                    int x0 = triangleXpoints.get(0);
                    int x1 = triangleXpoints.get(1);
                    int x2 = triangleXpoints.get(2);
                    int y0 = triangleYpoints.get(0);
                    int y1 = triangleYpoints.get(1);
                    int y2 = triangleYpoints.get(2);
                    // the triangle cannot have two or three points which are the same
                    if ( !(x0 == x1 && y0 == y1) && !(x1 == x2 && y1 == y2) && !(x0 == x2 && y0 == y2) ) {
                        paintView.drawTriangle(x0, y0, x1, y1, x2, y2, 
                                model.getCurrentOutlineColor(), model.getCurrentBackgroundColor(), 
                                model.getCurrentThickness());
                        model.incrementDrawnFigures();
                        paintView.updateDrawnFiguresCounterValueLabel(model.getDrawnFiguresNumber());
                    }
                    triangleXpoints = new ArrayList<>();
                    triangleYpoints = new ArrayList<>();
                }
            }
            else if(me.getButton() == MouseEvent.BUTTON3) {
                // deleting all drawn figures
            }
        }
    }
    
    private class PaintWindowListener extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            JFrame closedWindow = (JFrame)e.getSource();
            String windowName= closedWindow.getName();
            switch(windowName) {
                case "paintWindow":
                    System.exit(0);
                    break;
                case "defaultValuesWindow":
                    closedWindow.dispose();
                    break;   
            }
        }
    }  
}
