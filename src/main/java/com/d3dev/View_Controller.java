package com.d3dev;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;

public class View_Controller {
    Model model_ref;
    Controller controller_ref;
    View_Controller(Model model_ref, Controller controller_ref){
        this.model_ref = model_ref;
        this.controller_ref = controller_ref;
        initialize_toolbar();
        model_ref.root.addPanels(model_ref.image_View, model_ref.sidebar_View);
    }

    void initialize_toolbar(){
        model_ref.root.getChildren().add(model_ref.toolbar_View.root);

        Button open = (Button) model_ref.ui_.get("openfile_button");
        open.setOnMouseClicked(e->{controller_ref.openFile();});
        StringProperty viewport_cs = (StringProperty) model_ref.props_.get("viewport_cs");
        viewport_cs.addListener(e->{controller_ref.update_cs();});


    }




}
