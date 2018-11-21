package com.company.appname.app.controller;

// App classes
import com.company.appname.app.dto.user.UserSimpleDTO;
import com.company.appname.app.service.UserService;

// Swagger
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

// Spring web
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;

@RestController
@Api(value = "/api-app-name/users", description = "Operations for users")
@RequestMapping("/api-app-name/users")
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiOperation(value = "Retrieve all users", response = UserSimpleDTO.class, responseContainer = "List")
    public ResponseEntity findAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

}
