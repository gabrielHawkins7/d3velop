package com.d3dev;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.text.PlainDocument;

import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.d3dev.Utils.CMS;
import com.d3dev.Views.Main_Window;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller {
    Model model = new Model();
    View_Controller view_controller;

    BooleanProperty loading_ = new SimpleBooleanProperty();

    Notification warn;
    Notification message;

    Controller(Stage stage_ref, Scene scene_ref){
        model.stage_ref = stage_ref;
        model.scene_ref = scene_ref;
        model.props_.put("loading_", loading_);
        loadingController();

        view_controller = new View_Controller(model, this);
        warn = new Notification("BEEP BOOP", new FontIcon(Feather.ALERT_OCTAGON));
        warn.getStyleClass().addAll(Styles.ELEVATED_1,Styles.DANGER);
        warn.setOnClose(e->model.sidebar_View.getChildren().remove(warn));
        message = new Notification("BEEP BOOP", new FontIcon(Feather.CHECK));
        message.getStyleClass().addAll(Styles.ELEVATED_1,Styles.SUCCESS);
        message.setOnClose(e->model.sidebar_View.getChildren().remove(message));
    }

    void openFile(){
        if(model.open_image != null){
            model.image_View.image_view.setImage(null);
        }
        loading_.set(true);
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TIFF or PNG", "*.tiff","*.png"));
        model.open_file = fc.showOpenDialog(model.stage_ref);
        if(model.open_file != null){
               try {
                model.open_image = ImageIO.read(model.open_file);
                model.open_image = CMS.convertTo16Bit(model.open_image);

                if((String) model.props_.get("viewport_cs").getValue() == "Working"){
                    model.open_image = CMS.convert_space(model.open_image,(String) model.csprofiles_.get(model.props_.get("work_profile").getValue()));
                }else{
                    model.open_image = CMS.convert_space(model.open_image,(String) model.csprofiles_.get(model.props_.get("out_profile").getValue()));
                }
                loadImage(model.open_image);
            } catch (IOException e) {
                throw_warning("Unable to Open File : " + e.getMessage());
            }
        }
    }

    void update_cs(){
        if(model.open_file != null){
            loading_.setValue(true);
            model.image_View.image_view.setImage(null);
            try {
             if((String) model.props_.get("viewport_cs").getValue() == "Working"){
                 model.open_image = CMS.convert_space(model.open_image,(String) model.csprofiles_.get(model.props_.get("work_profile").getValue()));
             }else{
                 model.open_image = CMS.convert_space(model.open_image,(String) model.csprofiles_.get(model.props_.get("out_profile").getValue()));
             }
             loadImage(model.open_image);
         } catch (IOException e) {
             throw_warning("Unable Update Color Space  : " + e.getMessage());
         }
     }
     
     
    }

    VBox getRoot(){
        return model.root;
    }

    void loadImage(BufferedImage img){
        model.image_View.loadImage(img);
    }

    public void throw_warning(String warning){
        warn.messageProperty().set(warning);
        model.sidebar_View.getChildren().add(warn);
    }
    public void throw_message(String message){
        this.message.messageProperty().set(message);
        model.sidebar_View.getChildren().add(this.message);
    }

    public void loadingController(){
        ProgressBar bar = new ProgressBar();
        loading_.addListener(e->{
            if(loading_.getValue() == true){
                model.image_View.content_pane.getChildren().addLast(bar);
            }else{
                model.image_View.content_pane.getChildren().remove(bar);
            }
        });
    }

}
