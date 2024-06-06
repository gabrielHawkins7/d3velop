package com.d3dev;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import com.d3dev.Utils.CMS;
import com.d3dev.Utils.Developer;
import com.d3dev.Utils.img_transformer;

import atlantafx.base.controls.Notification;
import atlantafx.base.theme.Styles;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class Controller {
    App app_ref;
    public Model model = new Model();
    View_Controller view_controller;

    Notification warn;
    Notification message;

    @SuppressWarnings("unused")
    Controller(Stage stage_ref, Scene scene_ref, App app_ref){
        model.stage_ref = stage_ref;
        model.scene_ref = scene_ref;
        this.app_ref = app_ref;
        img_transformer img_ = new img_transformer(model,this);

        view_controller = new View_Controller(model, this);
        warn = new Notification("BEEP BOOP", new FontIcon(Feather.ALERT_OCTAGON));
        warn.getStyleClass().addAll(Styles.ELEVATED_1,Styles.DANGER);
        warn.setOnClose(e->model.sidebar_View.getChildren().remove(warn));
        message = new Notification("BEEP BOOP", new FontIcon(Feather.CHECK));
        message.getStyleClass().addAll(Styles.ELEVATED_1,Styles.SUCCESS);
        message.setOnClose(e->model.sidebar_View.getChildren().remove(message));
        keyboard();
    }

    void openFile(){
        if(model.open_image != null){
            model.open_image = null;
            model.image_View.image_view.setImage(null);
        }
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TIFF or PNG", "*.tiff","*.png"));
        File open_file = fc.showOpenDialog(model.stage_ref);
        model.open_file = open_file;
        if(open_file != null){
               try {
                model.open_image = ImageIO.read(open_file);
                model.open_image = CMS.convertTo16Bit(model.open_image);
                model.props_.original_cs.set(model.open_image.getColorModel().getColorSpace());
                //update_cs();
                Platform.runLater(()->{
                    loadImage(model.open_image);
                });
            } catch (IOException e) {
                throw_warning("Unable to Open File : " + e.getMessage());
            }
        }
    }
    void saveFile(){
        if(model.open_file != null){
            FileChooser fc = new FileChooser();
            FileChooser.ExtensionFilter tiffFilter = new FileChooser.ExtensionFilter("TIFF files (*.tiff)", "*.tiff");
            FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
            fc.getExtensionFilters().addAll(tiffFilter, pngFilter);
            File file = fc.showSaveDialog(model.stage_ref);
            try {
                ImageIO.write(model.open_image, file.toString().substring(file.toString().lastIndexOf(".")+1), file);
                throw_message("Saved to : " + file.getPath());
            } catch (IOException e) {
                throw_warning("Unable to Save File : " + e.getMessage());
            }
        }
    }

    void update_cs(){
        if(model.open_image != null && (Boolean) model.props_.color_mgmt.getValue() == true){
            model.image_View.image_view.setImage(null);
            try {
                if(model.props_.viewport_cs.getValue().equals("Working")){
                    model.open_image = CMS.convert_space(model.open_image, model.csprofiles_.get(model.props_.work_cs.getValue()));
                }else{
                    model.open_image = CMS.convert_space(model.open_image, model.csprofiles_.get(model.props_.out_cs.getValue()));
                }

            } catch (IOException e) {
                throw_warning("Unable Update Color Space  : " + e.getMessage());
            }

        }
        
    }

    void reset_cs(){
        if(model.open_image != null){
            try {
                model.open_image = CMS.convert_space(model.open_image, (ColorSpace) model.props_.original_cs.getValue());
            } catch (IOException e) {
                throw_warning("Unable Update Color Space  : " + e.getMessage());
            }
            
        }
    }
    void reset_image(){
        if(model.open_file != null){
            try {
             model.open_image = ImageIO.read(model.open_file);
             model.open_image = CMS.convertTo16Bit(model.open_image);
             update_cs();
             upadteImage();
         } catch (IOException e) {
             throw_warning("Unable to Open File : " + e.getMessage());
         }
     }
    }
    VBox getRoot(){
        return model.root;
    }

    void loadImage(BufferedImage img){
        model.image_View.loadImage(img);
    }
    public void upadteImage(){
        if(model.open_image != null){
            model.image_View.loadImage(model.open_image);
        }
    }

    public void throw_warning(String warning){
        warn.messageProperty().set(warning);
        model.sidebar_View.getChildren().add(warn);
    }
    public void throw_message(String message){
        this.message.messageProperty().set(message);
        model.sidebar_View.getChildren().add(this.message);
    }
    public void develop(){
        if(model.open_image != null){
            try {
                model.open_image = CMS.convert_space(model.open_image,model.csprofiles_.get(model.props_.work_cs.getValue()));
            } catch (IOException e) {
                throw_warning("Unable Update Color Space  : " + e.getMessage());
            }
            model.open_image = Developer.develop(model.open_image);
            update_cs();
            upadteImage();
        }
        
    }

    void keyboard(){
        model.scene_ref.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @SuppressWarnings("incomplete-switch")
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case MINUS:    model.image_View.zoomOut(); break;
                    case EQUALS:    model.image_View.zoomIn(); break;
                }
            }
        });
        model.scene_ref.setOnZoom(e->{
            if(e.getZoomFactor() > 1){
                model.image_View.zoomIn();
            }else{
                model.image_View.zoomOut();
            }
        });
    }

}
