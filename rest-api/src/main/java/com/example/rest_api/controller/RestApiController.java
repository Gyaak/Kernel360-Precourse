package com.example.rest_api.controller;

import com.example.rest_api.model.BookQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class RestApiController {

    @GetMapping(path = "/hello")
    public String hello() {
        var html = "<html> <body> <h1> Hello Spring Boot </h2> </body> <html>";
        return html;
    }

    @GetMapping("/echo/{message}")
    public String echo(
            @PathVariable(name = "message") String msg
    ) {
        System.out.println("echo message = " + msg);
        // TODO 대문자로 변환해서 RETURN
        return msg.toUpperCase();
    }

    // http://localhost:8080/api/book?category=IT&issuedYear=2023&issuedMonth=01&issuedDay=31
    @GetMapping("/book")
    public void queryParam(
            BookQueryParam bookQueryParam
//            @RequestParam String category,
//            @RequestParam String issuedYear,
//            @RequestParam(name = "issuedMonth") String issuedMonth,
//            @RequestParam String issueDay
    ) {
        System.out.println("category = " + bookQueryParam.getCategory());
        System.out.println("issuedYear = " + bookQueryParam.getIssuedYear());
        System.out.println("issuedMonth = " + bookQueryParam.getIssuedMonth());
        System.out.println("issuedDay = " + bookQueryParam.getIssuedDay());
    }

    @DeleteMapping(path = {
            "/user/{userName}/delete/",
            "/user/{userName}/del/"
        }
    )
    public void delete(
        @PathVariable String userName
    ) {
        log.info("user-name : {}", userName);
    }
}
