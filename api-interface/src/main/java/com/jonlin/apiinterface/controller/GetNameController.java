package com.jonlin.apiinterface.controller;

import com.jonlin.apiclient.model.User;
import com.jonlin.apiclient.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class GetNameController {

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        return user.getUsername();
    }

}
