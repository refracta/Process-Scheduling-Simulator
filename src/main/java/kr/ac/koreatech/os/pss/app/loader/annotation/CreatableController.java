package kr.ac.koreatech.os.pss.app.loader.annotation;

import javafx.fxml.FXMLLoader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CreatableController {
    String value() default "";
}
