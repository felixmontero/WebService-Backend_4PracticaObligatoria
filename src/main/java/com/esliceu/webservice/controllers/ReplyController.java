package com.esliceu.webservice.controllers;

import com.esliceu.webservice.services.ReplyService;
import com.esliceu.webservice.services.TopicService;
import com.esliceu.webservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReplyController {

    @Autowired
    ReplyService replyService;
    @Autowired
    TopicService topicService;
    @Autowired
    UserService userService;


}
