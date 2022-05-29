package sii.internship.lipinski.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sii.internship.lipinski.dao.dto.BrowseUserDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.exception.LoginTakenException;
import sii.internship.lipinski.util.exception.UserNotFoundException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto register(@RequestBody UserDto registerDto) throws LoginTakenException {
        return userService.register(registerDto);
    }

    @GetMapping(value = {"", "/{pageNumber}/{pageSize}"})
    public Iterable<BrowseUserDto> getAllRegisteredUsers(@PathVariable(required = false) Integer pageNumber, @PathVariable(required = false) Integer pageSize){
        return userService.getAll(pageNumber,pageSize);
    }

    @PatchMapping("/{userLogin}/{newUserEmail}")
    public UserDto changeMail(@PathVariable String userLogin, @PathVariable String newUserEmail) throws UserNotFoundException {
        return userService.changeEmail(userLogin, newUserEmail);
    }
}
