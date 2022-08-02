package com.ll.exam;

import com.ll.exam.annotation.Controller;
import com.ll.exam.article.controller.ArticleController;
import com.ll.exam.home.controller.HomeController;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {
    private static Map<Class, Object> objects;

    static {
        objects = new HashMap<>();

        Reflections ref = new Reflections("com.ll.exam");
        for (Class<?> cls : ref.getTypesAnnotatedWith(Controller.class)) {
            objects.put(cls, Ut.cls.newObj(cls, null));
        }
    }

    public static Object getObj(Class cls) {
        return objects.get(cls);
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
}
