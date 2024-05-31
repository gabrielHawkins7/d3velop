package com.d3dev.Utils;

import java.awt.image.BufferedImage;

import com.d3dev.Controller;
import com.d3dev.Model;
import com.d3dev.Views.Image_View;

import javafx.geometry.Bounds;
import javafx.scene.control.ToggleButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * img_transformer
 */
public class img_transformer {
    

    Rectangle crop_rectangle = new Rectangle();
    Model model_ref;
    Image_View imageview_ref;
    Controller controller_ref;

    public img_transformer(Model model_ref, Controller controller_ref){
        this.model_ref = model_ref;
        this.controller_ref =controller_ref;
        this.imageview_ref = controller_ref.model.image_View;

        crop();
        crop_rectangle.setWidth(10);
        crop_rectangle.setHeight(10);
        crop_rectangle.setFill(Color.rgb(0, 0, 0, .1));
        crop_rectangle.setStroke(Color.WHITESMOKE);
        crop_rectangle.setStrokeWidth(1);


    }
    double x;
    double y;

    Bounds boundsLocal;
    Bounds boundsScene;

    double imageX;
    double imageY;

    double scale;

    double offsetX;
    double offsetY;

    void crop(){
        model_ref.props_.crop_mode.addListener(e->{
            if(model_ref.props_.crop_mode.getValue() == true && model_ref.open_image != null){
                imageview_ref.content_pane.getChildren().add(crop_rectangle);
            }else{
                imageview_ref.content_pane.getChildren().remove(crop_rectangle);
            }
        });
       
        imageview_ref.content_pane.setOnMousePressed(e->{
            if(model_ref.props_.crop_mode.getValue() == true){
                x = e.getX();
            y = e.getY();
            boundsLocal = imageview_ref.image_view.getBoundsInLocal();
            boundsScene = imageview_ref.image_view.localToScene(boundsLocal);

            double rX = e.getSceneX() - boundsScene.getMinX();
            double rY = e.getSceneY() - boundsScene.getMinY();

            // Calculate the corresponding pixel coordinates in the underlying image
            double imageWidth = imageview_ref.image_view.getImage().getWidth();
            double imageHeight = imageview_ref.image_view.getImage().getHeight();
            double viewWidth = boundsLocal.getWidth();
            double viewHeight = boundsLocal.getHeight();

            // Ensure the aspect ratio is preserved
            double scaleX = viewWidth / imageWidth;
            double scaleY = viewHeight / imageHeight;
            scale = Math.min(scaleX, scaleY);

            // Calculate the top-left corner of the image in the ImageView coordinates
            offsetX = (viewWidth - imageWidth * scale) / 2;
            offsetY = (viewHeight - imageHeight * scale) / 2;

            imageX = (rX - offsetX) / scale;
            imageY = (rY - offsetY) / scale;
            }
        });
        imageview_ref.content_pane.setOnMouseDragged(e->{
            if(model_ref.props_.crop_mode.getValue() == true){
                crop_rectangle.setWidth(Math.abs(e.getX() - x));
                crop_rectangle.setHeight(Math.abs(e.getY() - y));
                crop_rectangle.setTranslateX(x - (imageview_ref.content_pane.getWidth() / 2) + (crop_rectangle.getWidth() / 2));
                crop_rectangle.setTranslateY(y - (imageview_ref.content_pane.getHeight() / 2) + (crop_rectangle.getHeight() / 2));
            }
        });
        imageview_ref.content_pane.setOnMouseReleased(e->{
            if(model_ref.props_.crop_mode.getValue() == true){
                double w = crop_rectangle.localToScene(crop_rectangle.getBoundsInLocal()).getWidth();
                double h = crop_rectangle.localToScene(crop_rectangle.getBoundsInLocal()).getHeight();

                double imageW = (w - offsetX) / scale;
                double imageH = (h - offsetY) / scale;

                
                if(imageX > imageview_ref.image_view.getImage().getWidth()){
                    imageX = imageview_ref.image_view.getImage().getWidth();
                }
                if(imageX < 0){
                    imageX = 0;
                }
                
                if(imageY > imageview_ref.image_view.getImage().getHeight()){
                    imageY = imageview_ref.image_view.getImage().getHeight();
                }
                if(imageY < 0){
                    imageY = 0;
                }

                if(imageW > imageview_ref.image_view.getImage().getWidth()){
                    imageW -= imageW - imageview_ref.image_view.getImage().getWidth();
                    imageW -= imageX;
                }
                if(imageH > imageview_ref.image_view.getImage().getHeight()){
                    imageH -= imageH - imageview_ref.image_view.getImage().getHeight();
                    imageH -= imageY;
                }

                try {
                    model_ref.open_image = cropImage(model_ref.open_image, (int) imageX, (int) imageY, (int) imageW, (int) imageH);
                } catch (Exception excelption) {
                    controller_ref.throw_warning("Unable to Crop Image : " + excelption.getMessage()) ;
                    System.out.println("Image x " + imageX + "Image y " + imageY);
                    System.out.println("Image w " + imageW + " " + imageview_ref.image_view.getImage().getWidth());
                }
                controller_ref.upadteImage();
                ToggleButton crop_button = (ToggleButton) model_ref.ui_.get("crop_button");
                crop_button.setSelected(false);
                model_ref.props_.crop_mode.set(false);
                crop_rectangle.setWidth(10);
                crop_rectangle.setHeight(10);
                crop_rectangle.setTranslateX(0);
                crop_rectangle.setTranslateY(0);

            }
        });
    }
    BufferedImage cropImage(BufferedImage in, int x, int y, int w, int h){
        in = in.getSubimage(x,y,w,h);
        return in;
    }
    
}