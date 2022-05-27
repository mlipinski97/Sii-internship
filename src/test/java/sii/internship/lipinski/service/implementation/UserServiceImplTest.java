package sii.internship.lipinski.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import sii.internship.lipinski.dao.dto.BrowseUserDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.UserRepository;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.exception.LoginTakenException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Logger logger = Logger.getLogger(UserServiceImplTest.class.getName());

    @Mock
    UserRepository userRepository;

    ModelMapper modelMapper = new ModelMapper();

    //object under tests
    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("when given correct non taken credentials then it registers same account (nothing changes)")
    void whenGivenCorrectTakenRegistrationCredentials_thenItRegistersSameAccount() {
        //given
        String testLogin = "testLogin";
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setEmail("test@email.com");
        expectedUserDto.setLogin(testLogin);
        //when
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.of(modelMapper.map(expectedUserDto, User.class)));
        when(userRepository.save(any())).thenReturn(modelMapper.map(expectedUserDto, User.class));
        //then
        try {
            userService.register(expectedUserDto);
        } catch (LoginTakenException e) {
            logger.info("Problem occurred while trying to register new user: "
                    + expectedUserDto.getLogin() + " " + expectedUserDto.getEmail()
                    + "\nfor more info see: " + Arrays.toString(e.getStackTrace()));
        }
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(expectedUserDto, modelMapper.map(userArgumentCaptor.getValue(), UserDto.class));
    }

    @Test
    @DisplayName("when given correct non taken credentials then it registers new account")
    void whenGivenCorrectNonTakenRegistrationCredentials_thenItRegistersNewAccount() {
        //given
        String testLogin = "testLogin";
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setEmail("test@email.com");
        expectedUserDto.setLogin(testLogin);
        //when
        when(userRepository.save(any())).thenReturn(modelMapper.map(expectedUserDto, User.class));
        //then
        try {
            userService.register(expectedUserDto);
        } catch (LoginTakenException e) {
            logger.info("Problem occurred while trying to register new user: "
                    + expectedUserDto.getLogin() + " " + expectedUserDto.getEmail()
                    + "\nfor more info see: " + Arrays.toString(e.getStackTrace()));
        }
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(expectedUserDto, modelMapper.map(userArgumentCaptor.getValue(), UserDto.class));
    }

    @Test
    @DisplayName("when given taken user login then it throw LoginTakenException")
    void whenGivenTakenUserLogin_thenItThrowsLoginTakenException() {
        //given
        String testLogin = "testLogin";
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(1L);
        expectedUserDto.setEmail("test@email.com");
        expectedUserDto.setLogin(testLogin);
        UserDto actualUserDto = new UserDto();
        actualUserDto.setLogin(testLogin);
        actualUserDto.setEmail("anotherTest@email.com");
        //when
        when(userRepository.findByLogin(testLogin)).thenReturn(Optional.of(modelMapper.map(expectedUserDto, User.class)));
        //then
        assertThrows(LoginTakenException.class, () -> userService.register(actualUserDto));
    }

    @Test
    @DisplayName("when called it returns list of all registered users with emails")
    void whenCalled_thenItReturnsListOfAllRegisteredUsers() {
        //given
        User expectedUser1 = new User();
        User expectedUser2 = new User();
        User expectedUser3 = new User();
        expectedUser1.setEmail("test1@email.com");
        expectedUser2.setEmail("test2@email.com");
        expectedUser3.setEmail("test3@email.com");
        expectedUser1.setLogin("login1");
        expectedUser2.setLogin("login1");
        expectedUser3.setLogin("login1");
        List<User> users = new ArrayList<>(Arrays.asList(expectedUser1, expectedUser2, expectedUser3));
        List<BrowseUserDto> expectedUserList = new ArrayList<>();
        expectedUserList.add(modelMapper.map(expectedUser1, BrowseUserDto.class));
        expectedUserList.add(modelMapper.map(expectedUser2, BrowseUserDto.class));
        expectedUserList.add(modelMapper.map(expectedUser3, BrowseUserDto.class));
        //when
        when(userRepository.findAll()).thenReturn(users);
        //then
        Iterable<BrowseUserDto> actualUserList = userService.getAll();
        assertEquals(expectedUserList, actualUserList);
    }

}
