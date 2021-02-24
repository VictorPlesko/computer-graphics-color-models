package by.bsu.plesko.controller;

import by.bsu.plesko.unit.ColorModels;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cInCMYKTextField;

    @FXML
    private TextField mInCMYKTextField;

    @FXML
    private TextField yInCMYKTextField;

    @FXML
    private TextField kInCMYKTextField;

    @FXML
    private TextField lInLABTextField;

    @FXML
    private TextField aInLABTextField;

    @FXML
    private TextField bInLABTextField;

    @FXML
    private TextField xInXYZTextField;

    @FXML
    private TextField yInXYZTextField;

    @FXML
    private TextField zInXYZTextField;

    @FXML
    private ColorPicker picker;

    @FXML
    private Circle circle;

    @FXML
    private Slider cInCMYKSlider;

    @FXML
    private Slider mInCMYKSlider;

    @FXML
    private Slider yInCMYKSlider;

    @FXML
    private Slider kInCMYKSlider;

    @FXML
    private Slider lInLABSlider;

    @FXML
    private Slider aInLABSlider;

    @FXML
    private Slider bInLABSlider;

    @FXML
    private Slider xInXYZSlider;

    @FXML
    private Slider yInXYZSlider;

    @FXML
    private Slider zInXYZSlider;

    @FXML
    void initialize() {
        pickerAction(new ActionEvent());

        settingCMYKSlider(cInCMYKSlider);
        settingCMYKSlider(mInCMYKSlider);
        settingCMYKSlider(yInCMYKSlider);
        settingCMYKSlider(kInCMYKSlider);

        settingLABSlider(lInLABSlider);
        settingLABSlider(aInLABSlider);
        settingLABSlider(bInLABSlider);

        settingXYZSlider(xInXYZSlider);
        settingXYZSlider(yInXYZSlider);
        settingXYZSlider(zInXYZSlider);

        sliderValueInTextField(cInCMYKSlider, cInCMYKTextField);
        sliderValueInTextField(mInCMYKSlider, mInCMYKTextField);
        sliderValueInTextField(yInCMYKSlider, yInCMYKTextField);
        sliderValueInTextField(kInCMYKSlider, kInCMYKTextField);

        sliderValueInTextField(lInLABSlider, lInLABTextField);
        sliderValueInTextField(aInLABSlider, aInLABTextField);
        sliderValueInTextField(bInLABSlider, bInLABTextField);

        sliderValueInTextField(xInXYZSlider, xInXYZTextField);
        sliderValueInTextField(yInXYZSlider, yInXYZTextField);
        sliderValueInTextField(zInXYZSlider, zInXYZTextField);

        CMYKTextFieldAction(cInCMYKTextField);
        CMYKTextFieldAction(mInCMYKTextField);
        CMYKTextFieldAction(yInCMYKTextField);
        CMYKTextFieldAction(kInCMYKTextField);

        XYZTextFieldAction(xInXYZTextField);
        XYZTextFieldAction(yInXYZTextField);
        XYZTextFieldAction(zInXYZTextField);

        LABTextFieldAction(lInLABTextField);
        LABTextFieldAction(aInLABTextField);
        LABTextFieldAction(bInLABTextField);

        settingCMYKTextField(cInCMYKTextField);
        settingCMYKTextField(mInCMYKTextField);
        settingCMYKTextField(yInCMYKTextField);
        settingCMYKTextField(kInCMYKTextField);

        settingLABTextField(lInLABTextField);
        settingLABTextField(aInLABTextField);
        settingLABTextField(bInLABTextField);

        settingXYZTextField(xInXYZTextField);
        settingXYZTextField(yInXYZTextField);
        settingXYZTextField(zInXYZTextField);
    }

    @FXML
    void pickerAction(ActionEvent event) {
        float red = (float) picker.getValue().getRed() * 255;
        float green = (float) picker.getValue().getGreen() * 255;
        float blue = (float) picker.getValue().getBlue() * 255;

        ColorModels.RGB rgb = ColorModels.RGB$.MODULE$.create(Math.round(red), Math.round(green), Math.round(blue));
        ColorModels.CMYK cmyk = rgb.toCMYK();
        ColorModels.XYZ xyz = rgb.toXYZ();
        ColorModels.LAB lab = xyz.toLab();

        updateCMYKTextField(cmyk);
        updateXYZTextField(xyz);
        updateLABTextField(lab);

        circle.setFill(picker.getValue());
    }

    boolean lockerClose = false;

    public void CMYKTextFieldAction(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!lockerClose && !textField.getText().isEmpty() && !textField.getText().equals("-")) {
                lockerClose = true;
                int c = Integer.parseInt(cInCMYKTextField.getText());
                int m = Integer.parseInt(mInCMYKTextField.getText());
                int y = Integer.parseInt(yInCMYKTextField.getText());
                int k = Integer.parseInt(kInCMYKTextField.getText());

                ColorModels.CMYK cmyk = ColorModels.CMYK$.MODULE$.create(c, m, y, k);
                ColorModels.RGB rgb = cmyk.toRGB();
                ColorModels.XYZ xyz = rgb.toXYZ();
                ColorModels.LAB lab = xyz.toLab();

                updateCMYKTextField(cmyk);
                updateXYZTextField(xyz);
                updateLABTextField(lab);

                updatePickerWithCircle(rgb);

                updateSliders();

                messageWarning();

                lockerClose = false;
            }
        });
    }

    boolean viewDailogLab = false;

    public void LABTextFieldAction(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!lockerClose && !textField.getText().isEmpty() && !textField.getText().equals("-")) {
                lockerClose = true;
                int l = Integer.parseInt(lInLABTextField.getText());
                int a = Integer.parseInt(aInLABTextField.getText());
                int b = Integer.parseInt(bInLABTextField.getText());

                ColorModels.LAB lab = ColorModels.LAB$.MODULE$.create(l, a, b);
                ColorModels.XYZ xyz = lab.toXYZ();
                ColorModels.RGB rgb = xyz.toRGB();
                ColorModels.CMYK cmyk = rgb.toCMYK();

                updateLABTextField(lab);
                updateCMYKTextField(cmyk);
                updateXYZTextField(xyz);

                updatePickerWithCircle(rgb);

                updateSliders();

                messageWarning();

                lockerClose = false;
            }
        });
    }

    public void XYZTextFieldAction(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!lockerClose && !textField.getText().isEmpty() && !textField.getText().equals("-")) {
                lockerClose = true;
                int x = Integer.parseInt(xInXYZTextField.getText());
                int y = Integer.parseInt(yInXYZTextField.getText());
                int z = Integer.parseInt(zInXYZTextField.getText());

                ColorModels.XYZ xyz = ColorModels.XYZ$.MODULE$.create(x, y, z);
                ColorModels.RGB rgb = xyz.toRGB();
                ColorModels.LAB lab = xyz.toLab();
                ColorModels.CMYK cmyk = rgb.toCMYK();

                updateXYZTextField(xyz);
                updateCMYKTextField(cmyk);
                updateLABTextField(lab);

                updatePickerWithCircle(rgb);

                updateSliders();

               messageWarning();

                lockerClose = false;
            }
        });
    }

    public void sliderValueInTextField(Slider slider, TextField textField) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            textField.setText(Integer.toString(Math.round(newValue.floatValue())));
        });
    }

    private void updatePickerWithCircle(ColorModels.RGB rgb) {
        picker.setValue(new Color(rgb.r() / 255d, rgb.g() / 255d, rgb.b() / 255d, 1));
        circle.setFill(picker.getValue());
    }

    private void updateCMYKTextField(ColorModels.CMYK cmyk) {
        cInCMYKTextField.setText(Integer.toString(cmyk.c()));
        mInCMYKTextField.setText(Integer.toString(cmyk.m()));
        yInCMYKTextField.setText(Integer.toString(cmyk.y()));
        kInCMYKTextField.setText(Integer.toString(cmyk.k()));
    }

    private void updateLABTextField(ColorModels.LAB lab) {
        lInLABTextField.setText(Integer.toString(lab.l()));
        aInLABTextField.setText(Integer.toString(lab.a()));
        bInLABTextField.setText(Integer.toString(lab.b()));
    }

    private void updateXYZTextField(ColorModels.XYZ xyz) {
        xInXYZTextField.setText(Integer.toString(xyz.x()));
        yInXYZTextField.setText(Integer.toString(xyz.y()));
        zInXYZTextField.setText(Integer.toString(xyz.z()));
    }

    private void settingCMYKSlider(Slider slider) {
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
    }

    private void settingLABSlider(Slider slider) {
        if (slider.equals(lInLABSlider)) {
            slider.setMin(0);
            slider.setMax(100);
        } else {
            slider.setMin(-128);
            slider.setMax(128);
        }
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
    }

    private void settingXYZSlider(Slider slider) {
        slider.setMin(0);
        if (slider.equals(xInXYZSlider)) slider.setMax(95);
        else if (slider.equals(yInXYZSlider)) slider.setMax(100);
        else slider.setMax(108);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
    }

    private void updateSliders() {
        cInCMYKSlider.setValue(Double.parseDouble(cInCMYKTextField.getText()));
        mInCMYKSlider.setValue(Double.parseDouble(mInCMYKTextField.getText()));
        yInCMYKSlider.setValue(Double.parseDouble(yInCMYKTextField.getText()));
        kInCMYKSlider.setValue(Double.parseDouble(kInCMYKTextField.getText()));

        lInLABSlider.setValue(Double.parseDouble(lInLABTextField.getText()));
        aInLABSlider.setValue(Double.parseDouble(aInLABTextField.getText()));
        bInLABSlider.setValue(Double.parseDouble(bInLABTextField.getText()));

        xInXYZSlider.setValue(Double.parseDouble(xInXYZTextField.getText()));
        yInXYZSlider.setValue(Double.parseDouble(yInXYZTextField.getText()));
        zInXYZSlider.setValue(Double.parseDouble(zInXYZTextField.getText()));
    }

    private void settingCMYKTextField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(
                change -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
    }

    private void settingLABTextField(TextField textField) {
        if (textField.equals(lInLABTextField)) {
            textField.setTextFormatter(new TextFormatter<>(
                    change -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
        } else {
            textField.setTextFormatter(new TextFormatter<>(
                    change -> (change.getControlNewText().matches("[-]?[0-9]*")) ? change : null));
        }
    }

    private void settingXYZTextField(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(
                change -> (change.getControlNewText().matches("[0-9]*")) ? change : null));
    }

    private void messageWarning() {
        if (!viewDailogLab) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");

            alert.setContentText("Translation to other color models is possible with an error due to cropping!");

            alert.showAndWait();
            viewDailogLab = true;
        }
    }
}
