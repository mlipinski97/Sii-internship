package sii.internship.lipinski.service.implementation;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sii.internship.lipinski.dao.dto.BrowseUserDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.UserRepository;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.enums.ErrorCode;
import sii.internship.lipinski.util.enums.ErrorMessage;
import sii.internship.lipinski.util.exception.LoginTakenException;
import sii.internship.lipinski.util.exception.UserNotFoundException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper = new ModelMapper();
    private final UserRepository userRepository;

    @Override
    public UserDto register(UserDto userDto) throws LoginTakenException {
        Optional<User> user = userRepository.findByLogin(userDto.getLogin());
        if (user.isPresent()) {
            if (!user.get().getEmail().equals(userDto.getEmail())) {
                throw new LoginTakenException(
                        ErrorMessage.LOGIN_ALREADY_TAKEN.getMessage(),
                        ErrorCode.LOGIN_ALREADY_TAKEN.getValue());
            } else {
                return modelMapper.map(user.get(), UserDto.class);
            }
        }
        User newUser = modelMapper.map(userDto, User.class);
        return modelMapper.map(userRepository.save(newUser), UserDto.class);
    }
//dodac paginacje
    @Override
    public Iterable<BrowseUserDto> getAll(Integer pageNumber, Integer pageSize) {
        Pageable userPaging = Pageable.unpaged();
        if (pageSize != null && pageSize > 0 && pageNumber >= 0) {
            userPaging = PageRequest.of(pageNumber, pageSize);
        }
        return userRepository.findAll(userPaging).stream()
                .map(user -> modelMapper.map(user, BrowseUserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto changeEmail(String userLogin, String newUserEmail) throws UserNotFoundException {
        User userToBeChanged = userRepository.findByLogin(userLogin).orElseThrow(() -> new UserNotFoundException(
                ErrorMessage.USER_NOT_FOUND.getMessage(), ErrorCode.USER_NOT_FOUND.getValue()
        ));
        userToBeChanged.setEmail(newUserEmail);
        return modelMapper.map(userRepository.save(userToBeChanged), UserDto.class);
    }
}
