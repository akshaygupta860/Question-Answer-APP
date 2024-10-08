package com.ai.question_answer_app.controller;

import com.ai.question_answer_app.dto.UserInput;
import com.ai.question_answer_app.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class QueryController {

    @Autowired
    private QueryService queryService;


    @PostMapping("/query")
    public String getMessage(@RequestBody UserInput userInput) {
        return queryService.getQuery(userInput.getMessage());
    }


    @PostMapping(path = "/query/new",produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getMessageNew(@RequestParam String message) {
        System.out.println(message);
        return message;
    }

    @PostMapping("/importDataThroughURL")
    public void readURL(@RequestBody UserInput userInput) throws IOException {
         queryService.importDataThroughURL(userInput.getMessage());
    }


}
