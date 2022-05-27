package sii.internship.lipinski.service;

import sii.internship.lipinski.dao.dto.BrowseUserDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.util.exception.LoginTakenException;

public interface UserService {
    UserDto register(UserDto userDto) throws LoginTakenException;
    Iterable<BrowseUserDto> getAll();
}
