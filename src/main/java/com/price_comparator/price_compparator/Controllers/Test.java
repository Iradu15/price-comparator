package com.price_comparator.price_compparator.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {
    @GetMapping("/test")
    public void print(){
        System.out.println("Hello Radu");
    }
}

