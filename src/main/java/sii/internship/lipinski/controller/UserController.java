package sii.internship.lipinski.controller;

import org.springframework.web.bind.annotation.*;
import sii.internship.lipinski.dao.dto.BrowseUserDto;
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

    @GetMapping
    public Iterable<BrowseUserDto> getAllRegisteredUsers(){
        return userService.getAll();
    }
}
