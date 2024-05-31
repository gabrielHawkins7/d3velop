package com.d3dev;

import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;

import com.twelvemonkeys.image.BrightnessContrastFilter;
import com.twelvemonkeys.image.ImageUtil;
import com.twelvemonkeys.image.ResampleOp;

import atlantafx.base.controls.ToggleSwitch;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;

public class View_Controller {
    Model model_ref;
    Controller controller_ref;

    View_Controller(Model model_ref, Controller controller_ref){
        this.model_ref = model_ref;
        this.controller_ref = controller_ref;
        initialize_toolbar();
        initialize_sidebar();
        model_ref.root.addPanels(model_ref.image_View, model_ref.sidebar_View);
    }

    @SuppressWarnings("unchecked")
    void initialize_toolbar(){
        model_ref.root.getChildren().add(model_ref.toolbar_View.root);

        Button open = (Button) model_ref.ui_.get("openfile_button");
        open.setOnMouseClicked(e->{controller_ref.openFile();});
        Button save = (Button) model_ref.ui_.get("savefile_button");
        save.setOnMouseClicked(e->{controller_ref.saveFile();});
        Button undo = (Button) model_ref.ui_.get("undo_button");
        undo.setOnMouseClicked(e->{controller_ref.reset_image();});

        
        ToggleButton crop_button = (ToggleButton) model_ref.ui_.get("crop_button");

        crop_button.selectedProperty().addListener(e->{
            if(crop_button.isSelected()){
                model_ref.props_.crop_mode.setValue(true);
            }else{
                model_ref.props_.crop_mode.setValue(false);
            }
        });

        ComboBox<String> viewport_cs = (ComboBox<String>) model_ref.ui_.get("viewport_cs");
        viewport_cs.valueProperty().bindBidirectional(model_ref.props_.viewport_cs);
        viewport_cs.valueProperty().addListener(e->{
            controller_ref.update_cs();
            controller_ref.upadteImage();
        });
        viewport_cs.getSelectionModel().selectFirst();


        ToggleSwitch colormgmt_switch = (ToggleSwitch) model_ref.ui_.get("colormgmt_switch");
        colormgmt_switch.selectedProperty().addListener(e->{
            if(colormgmt_switch.isSelected() == true){
                model_ref.props_.color_mgmt.setValue(true);
                controller_ref.update_cs();
                controller_ref.upadteImage();
            }else{
                model_ref.props_.color_mgmt.setValue(false);
                controller_ref.reset_cs();
                controller_ref.upadteImage();
            }
        });

        

    }

    @SuppressWarnings("unchecked")
    void initialize_sidebar(){
        Button dev_button = (Button) model_ref.ui_.get("dev_button");
        dev_button.setOnMouseClicked(e->{controller_ref.develop();});
        ComboBox<String> workspace_combobox = (ComboBox<String>) model_ref.ui_.get("workspace_combobox");
        workspace_combobox.valueProperty().bindBidirectional(model_ref.props_.work_cs);
        workspace_combobox.getSelectionModel().selectFirst();
        workspace_combobox.valueProperty().addListener(e->{
                controller_ref.update_cs();
                controller_ref.upadteImage();
        });
        ComboBox<String> outspace_combobox = (ComboBox<String>) model_ref.ui_.get("outspace_combobox");
        outspace_combobox.valueProperty().bindBidirectional(model_ref.props_.out_cs);
        outspace_combobox.getSelectionModel().select(1);
        outspace_combobox.valueProperty().addListener(e->{
            controller_ref.update_cs();
            controller_ref.upadteImage();
        });

        
    }




}
