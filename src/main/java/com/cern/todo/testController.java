package com.cern.todo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path="/api")
@CrossOrigin(origins = "http://localhost:4200")
public class testController {
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello world!";
    }
}
