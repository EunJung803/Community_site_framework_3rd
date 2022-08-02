package com.ll.exam;

import com.ll.exam.annotation.Controller;
import com.ll.exam.article.controller.ArticleController;
import com.ll.exam.home.controller.HomeController;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Container {
    private static final ArticleController articleController;
    private static final HomeController homeController;

    static {
        articleController = (ArticleController) Ut.cls.newObj(ArticleController.class, null);
        homeController = (HomeController) Ut.cls.newObj(HomeController.class, null);
    }

    public static ArticleController getArticleController() {
        return articleController;
    }

    public static List<String> getControllerNames() {
        List<String> names = new ArrayList<>();

        Reflections ref = new Reflections("com.ll.exam");

        for (Class<?> cls : ref.getTypesAnnotatedWith(Controller.class)) {
            String clsSimpleName = cls.getSimpleName(); // HomeController
            clsSimpleName = clsSimpleName.replace("Controller", ""); // Home
            clsSimpleName = Ut.str.decapitalize(clsSimpleName); // home

            names.add(clsSimpleName.replace("Controller", clsSimpleName));
        }

        return names;
    }

    public static HomeController getHomeController() {
        return homeController;
    }
}
