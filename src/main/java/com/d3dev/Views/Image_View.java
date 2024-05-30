package com.d3dev.Views;

import atlantafx.base.controls.RingProgressIndicator;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import com.d3dev.Model;
import com.d3dev.Utils.SwingFXUtils;


public class Image_View extends ScrollPane{
    public StackPane content_pane = new StackPane();
    public ImageView image_view = new ImageView();
    final int OFFSET = 20;
    final int ZOOMFACTOR = 10;
    public double IMAGE_RATIO;
    Rectangle viewPort = new Rectangle();
    Model model;

    public Image_View(Model model){
        this.model = model;
        image_view.setPreserveRatio(true);     
        content_pane.getChildren().add(image_view);
        content_pane.setAlignment(Pos.CENTER);

        this.setContent(content_pane);
        this.setPannable(false);

    }

    public void loadImage(Image img){
        image_view.setImage(img);
        if(img.getWidth() > img.getHeight()){
            image_view.setFitWidth(this.getWidth());
        }else{
            image_view.setFitHeight(this.getHeight());
        }
        
        viewPort = new Rectangle(0-OFFSET, 0-OFFSET, image_view.boundsInParentProperty().get().getWidth() + (OFFSET*2) ,image_view.boundsInParentProperty().get().getHeight() + (OFFSET*2));
        IMAGE_RATIO = img.getHeight() / img.getWidth();
        content_pane.getChildren().add(viewPort);
        viewPort.setFill(Color.rgb(0,0,0,0));
        content_pane.setPrefWidth(this.getWidth());
        content_pane.setPrefHeight(this.getHeight());

        this.widthProperty().addListener(e->{
            content_pane.setPrefWidth(this.getWidth());
        });
        this.heightProperty().addListener(e->{
            content_pane.setPrefHeight(this.getHeight());
        });

        viewPort.widthProperty().bind(Bindings.createDoubleBinding(()->image_view.boundsInParentProperty().get().getWidth() + (OFFSET * 2), image_view.boundsInParentProperty()));
        viewPort.heightProperty().bind(Bindings.createDoubleBinding(()->image_view.boundsInParentProperty().get().getHeight() + (OFFSET * 2), image_view.boundsInParentProperty()));

        
    }   
    public void loadImage(BufferedImage img){ 
            Image i = SwingFXUtils.toFXImage(img, null);
            loadImage(i);
    } 

    public void zoomIn(){
        if(image_view.getImage().getWidth() > image_view.getImage().getHeight()){
            image_view.setFitWidth(image_view.getFitWidth() + ZOOMFACTOR);
        }else{
            image_view.setFitHeight(image_view.getFitHeight() + ZOOMFACTOR);
        }
    }
    public void zoomOut(){
        if(image_view.getImage().getWidth() > image_view.getImage().getHeight()){
            image_view.setFitWidth(image_view.getFitWidth() - ZOOMFACTOR);
        }else{
            image_view.setFitHeight(image_view.getFitHeight() - ZOOMFACTOR);
        }
    }




}
