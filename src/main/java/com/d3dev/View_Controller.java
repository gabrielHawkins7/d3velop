package com.d3dev;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
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

    void initialize_toolbar(){
        model_ref.root.getChildren().add(model_ref.toolbar_View.root);

        Button open = (Button) model_ref.ui_.get("openfile_button");
        open.setOnMouseClicked(e->{controller_ref.openFile();});
        Button save = (Button) model_ref.ui_.get("savefile_button");
        save.setOnMouseClicked(e->{controller_ref.saveFile();});

        StringProperty viewport_cs = (StringProperty) model_ref.props_.get("viewport_cs");
        viewport_cs.addListener(e->{controller_ref.update_cs(); controller_ref.upadteImage();});
        
        ToggleButton crop_button = (ToggleButton) model_ref.ui_.get("crop_button");
        crop_button.setOnMouseClicked(e->{
            if(model_ref.open_image == null){
                if(crop_button.isSelected() == false){
                    crop_button.setSelected(true);
                    model_ref.props_.get("crop_mode").setValue(true);;
                }else{
                    crop_button.setSelected(false);
                    model_ref.props_.get("crop_mode").setValue(false);;

                }
            }
        });

        BooleanProperty colormgmt = (BooleanProperty) model_ref.props_.get("colormgmt");
        colormgmt.addListener(e->{
            if (colormgmt.getValue() == true) {
                controller_ref.update_cs();
                controller_ref.upadteImage();
            }else{
                controller_ref.reset_cs();
                controller_ref.upadteImage();
            }
        });

    }

    void initialize_sidebar(){
        StringProperty work_profile = (StringProperty) model_ref.props_.get("work_profile");
        work_profile.addListener(e->{controller_ref.update_cs(); controller_ref.upadteImage();});
        StringProperty out_profile = (StringProperty) model_ref.props_.get("out_profile");
        out_profile.addListener(e->{controller_ref.update_cs(); controller_ref.upadteImage();});
        
        Button dev_button = (Button) model_ref.ui_.get("dev_button");
        dev_button.setOnMouseClicked(e->{controller_ref.develop();});

    }




}
