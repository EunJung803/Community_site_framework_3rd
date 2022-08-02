package com.ll.exam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private static Map<String , RouteInfo> routeInfos;
    
    static {
        routeInfos = new HashMap<>();
        
        scanMappings();
    }

    // 컨트롤러를 다 가져와서 그 안에 있는 @GetMapping을 가져옴
    private static void scanMappings() {
        Reflections ref = new Reflections(App.BASE_PACKAGE_PATH);
        for (Class<?> cl : ref.getTypesAnnotatedWith(Controller.class)) {
            Method[] methods = cl.getDeclaredMethods();

            for (Method method : methods) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);

                if (getMapping != null) {
                    String path = getMapping.value();


                }

                if (postMapping != null) {
                    String path = postMapping.value();
                }
            }
        }

        System.out.println(routeInfos);
    }

    public static void runAction(HttpServletRequest req, HttpServletResponse resp) {
        
    }
}
