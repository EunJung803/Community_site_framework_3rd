package com.ll.exam;

import com.ll.exam.annotation.Autowired;
import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.Repository;
import com.ll.exam.annotation.Service;
import com.ll.exam.mymap.MyMap;
import org.reflections.Reflections;
import util.Ut;

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
        scanCustom();

        // 레고 조립
        resolveDependenciesAllComponents();
    }

    private static void scanCustom() {
        objects.put(MyMap.class, new MyMap(App.DB_HOST, App.DB_PORT, App.DB_ID, App.DB_PASSWORD, App.DB_NAME));
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
                .filter(f -> f.isAnnotationPresent(Autowired.class))    // Autowired 붙은 것만 필터링
                .map(field -> {
                    field.setAccessible(true);
                    return field;
                })
                .forEach(field -> {
                    Class cls = field.getType();
                    Object dependency = objects.get(cls);   // 필드의 타입을 dependency에 저장

                    try {
                        field.set(o, dependency);
                        // o.필드명 = dependency;
                        // 위와 같은 표현이다. (o 객체 안에 있는 필드에 dependency 값을 넣는다)
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