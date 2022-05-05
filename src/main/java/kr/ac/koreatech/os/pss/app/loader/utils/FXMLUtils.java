package kr.ac.koreatech.os.pss.app.loader.utils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import kr.ac.koreatech.os.pss.app.PSSApplication;
import kr.ac.koreatech.os.pss.app.loader.annotation.CreatableController;

import java.io.IOException;

public class FXMLUtils {

    public static FXMLLoader getLoader(Class<?> creatableController) {
        CreatableController annotation = creatableController.getAnnotation(CreatableController.class);
        FXMLLoader loader;
        String from = annotation.value();


        if ("".equals(from)) {
            loader = new FXMLLoader(creatableController.getResource(creatableController.getSimpleName() + ".fxml"));
        } else {
            from = from.startsWith("/") ? from : "/" + from;
            loader = new FXMLLoader(FXMLUtils.class.getResource(from));
        }
        return loader;
    }

    public static Parent create(Class<?> creatableController) {
        try {
            return getLoader(creatableController).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getController(Class<T> creatableController) {
        FXMLLoader loader = getLoader(creatableController);

        return loader.getController();
    }
}
