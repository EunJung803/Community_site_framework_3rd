package com.ll.exam;

import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import com.ll.exam.mymap.MyMap;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.reflections.Reflections;
import util.Ut;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private static Map<String, RouteInfo> routeInfos;

    static {
        routeInfos = new HashMap<>();

        scanMappings();
    }

    private static void scanMappings() {
        Reflections ref = new Reflections(App.BASE_PACKAGE_PATH);
        for (Class<?> controllerCls : ref.getTypesAnnotatedWith(Controller.class)) {
            Method[] methods = controllerCls.getDeclaredMethods();

            for (Method method : methods) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);

                String httpMethod = null;
                String path = null;

                if (getMapping != null) {
                    path = getMapping.value();
                    httpMethod = "GET";
                }

                if (path != null && httpMethod != null) {
                    String actionPath = Ut.str.beforeFrom(path, "/", 4);

                    String key = httpMethod + "___" + actionPath;

                    routeInfos.put(key, new RouteInfo(path, actionPath, controllerCls, method));
                }
            }
        }
    }

    public static void runAction(HttpServletRequest req, HttpServletResponse resp) {
        Rq rq = new Rq(req, resp);

        String routeMethod = rq.getRouteMethod();
        String actionPath = rq.getActionPath();

        String mappingKey = routeMethod + "___" + actionPath;

        System.out.println("mappingKey : " + mappingKey);
        System.out.println("keySet : " + routeInfos.keySet());

        boolean contains = routeInfos.containsKey(mappingKey);

        if (contains == false) {
            rq.println("해당 요청은 존재하지 않습니다.");
            return;
        }

//        runAction(rq, routeInfos.get(mappingKey));
        RouteInfo routeInfo = routeInfos.get(mappingKey);
        rq.setRouteInfo(routeInfo);

        runAction(rq);
    }

    // 진짜 실행
    private static void runAction(Rq rq) {
        RouteInfo routeInfo = rq.getRouteInfo();
        Class controllerCls = routeInfo.getControllerCls();     // 컨트롤러 클래스 얻기
        Method actionMethod = routeInfo.getMethod();            // 메서드 얻기

        Object controllerObj = Container.getObj(controllerCls);     // 호출하기 위해서는 컨테이너에게 컨트롤러 클래스를 달라고 요청하기
        // 이를 컨트롤러 객체에 저장

        try {
            actionMethod.invoke(controllerObj, rq);     // 액션 메서드 실행
        } catch (IllegalAccessException e) {
            rq.println("액션시작에 실패하였습니다.");
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
            //rq.println("액션시작에 실패하였습니다.");
        } finally {
            MyMap myMap = Container.getObj(MyMap.class);
            myMap.closeConnection(); // 현재 쓰레드에 할당된 커넥션을 닫는다.
            // 안하면 벌어지는 일
            // 매 요청마다, DB 요청이 쌓인다.
        }
    }

    public static void init() {

    }

    public static Map<String, RouteInfo> getRouteInfosForTest() {
        return routeInfos;
    }
}