package com.ll.exam.article.controller;

import com.ll.exam.annotation.Controller;
import com.ll.exam.annotation.GetMapping;
import com.ll.exam.article.service.ArticleService;

// ArticleController 가 컨트롤러 이다.
// 라고 부가설명 해주는 것 (이렇게 골뱅이로 알려주는게 == 어노테이션)
// 컴퓨터가 읽는 주석, 꼬리표, 어노테이션 자체에 큰 능력은 없음
@Controller
// 아래 ArticleController 클래스는 Controller 이다.
public class ArticleController {
    private ArticleService articleService;

    // GetMapping은 /usr/article/list/free 와 같이 관련된 요청을 처리하는 함수이다.
    @GetMapping("/usr/article/list")
    // 아래 showList 는 Get - /usr/article/list 으로 요청이 왔을 때 실행 되어야 하는 함수이다.
    public void showList() {

    }

    public ArticleService getArticleServiceForTest() {
        return articleService;
    }
}