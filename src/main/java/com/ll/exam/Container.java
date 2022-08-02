package com.ll.exam;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.Repository;
import com.ll.exam.annotation.Service;
import org.reflections.Reflections;

import java.util.*;

public class Container {
    private static Map<Class, Object> objects;

    static {
        objects = new HashMap<>();

        scanComponents();
    }

    private static void scanComponents() {
        // 전체 레고 생성
        scanRepositories();
        scanServices();
        scanControllers();

        // 레고 조립
        resolveDependenciesAllComponents();
    }

    private static void resolveDependenciesAllComponents() {
        for (Class cls : objects.keySet()) {
            Object o = objects.get(cls);

            resolveDependencies(o);
        }
    }

    // 객체 의존성 처리
    private static void resolveDependencies(Object o) {
        Arrays.asList(o.getClass().getDeclaredFields())
                .stream()
                .filter(f -> f.isAnnotationPresent(Autowired.class))
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .forEach(field -> {
                    Class cls = field.getType();
                    Object dependency = objects.get(cls);

                    try {
                        field.set(o, dependency);
                    } catch (IllegalAccessException e) {

                    }
                });
    }

    private static void scanRepositories() {
        Reflections ref = new Reflections(App.BASE_PACKAGE_PATH);
        for (Class<?> cls : ref.getTypesAnnotatedWith(Repository.class)) {
            objects.put(cls, Ut.cls.newObj(cls, null));
        }
    }

    private static void scanServices() {
        Reflections ref = new Reflections(App.BASE_PACKAGE_PATH);
        for (Class<?> cls : ref.getTypesAnnotatedWith(Service.class)) {
            objects.put(cls, Ut.cls.newObj(cls, null));
        }
    }

    private static void scanControllers() {
        Reflections ref = new Reflections(App.BASE_PACKAGE_PATH);
        for (Class<?> cls : ref.getTypesAnnotatedWith(Controller.class)) {
            objects.put(cls, Ut.cls.newObj(cls, null));
        }
    }

    public static <T> T getObj(Class<T> cls) {
        return (T) objects.get(cls);
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