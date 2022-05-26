package sii.internship.lipinski.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.exception.LoginTakenException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto register(@RequestBody UserDto registerDto) throws LoginTakenException {
        return userService.register(registerDto);
    }
}
