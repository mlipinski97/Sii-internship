package sii.internship.lipinski.service.implementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import sii.internship.lipinski.dao.dto.BrowseUserDto;
import sii.internship.lipinski.dao.dto.UserDto;
import sii.internship.lipinski.dao.entity.User;
import sii.internship.lipinski.repository.UserRepository;
import sii.internship.lipinski.service.UserService;
import sii.internship.lipinski.util.exception.LoginTakenException;
import sii.internship.lipinski.util.exception.UserNotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        //then
        try {
            userService.register(expectedUserDto);
        } catch (LoginTakenException e) {
            logger.info("Problem occurred while trying to register new user: "
                    + expectedUserDto.getLogin() + " " + expectedUserDto.getEmail()
                    + "\nfor more info see: " + Arrays.toString(e.getStackTrace()));
        }
        verify(userRepository, never()).save(any());
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
    @DisplayName("when called without params it returns list of all registered users with emails as one page")
    void whenCalledWithoutParams_thenItReturnsListOfAllRegisteredUsersAsOnePage() {
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
        Page<User> expectedUsersPage = new PageImpl<>(users);
        Pageable userPaging = Pageable.unpaged();
        //when
        when(userRepository.findAll(userPaging)).thenReturn(expectedUsersPage);
        //then
        Iterable<BrowseUserDto> actualUserList = userService.getAll(0, 0);
        assertEquals(expectedUserList, actualUserList);
    }

    @Test
    @DisplayName("when called it returns list of all registered users with emails as one page")
    void whenCalledWithIncorrectParams_thenItReturnsListOfAllRegisteredUsersAsOnePage() {
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
        Page<User> expectedUsersPage = new PageImpl<>(users);
        Pageable userPaging = Pageable.unpaged();
        //when
        when(userRepository.findAll(userPaging)).thenReturn(expectedUsersPage);
        //then
        Iterable<BrowseUserDto> actualUserList = userService.getAll(-7, -2);
        assertEquals(expectedUserList, actualUserList);
    }

    @Test
    @DisplayName("when called it returns list of all registered users with emails as parametrized page")
    void whenCalledWithParams_thenItReturnsListOfAllRegisteredUsersAsParametrizedPage() {
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
        Page<User> expectedUsersPage = new PageImpl<>(users);
        Pageable userPaging = Pageable.unpaged();
        //when
        when(userRepository.findAll(userPaging)).thenReturn(expectedUsersPage);
        //then
        Iterable<BrowseUserDto> actualUserList = userService.getAll(-7, -2);
        assertEquals(expectedUserList, actualUserList);
    }

    @Test
    @DisplayName("when given user login and new email then it changes email")
    void whenGivenUserLoginAndNewEmail_thenItChangesEmail() {
        //given
        String newEmail = "new@email.com";
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("old@email.com");
        testUserDto.setLogin("login");
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setEmail(newEmail);
        expectedUserDto.setLogin("login");
        User expectedUser = modelMapper.map(expectedUserDto, User.class);
        UserDto actualUserDto = null;
        //when
        when(userRepository.findByLogin(testUserDto.getLogin())).thenReturn(Optional.of(modelMapper.map(testUserDto, User.class)));
        when(userRepository.save(any())).thenReturn(expectedUser);
        //then
        try {
            actualUserDto = userService.changeEmail(testUserDto.getLogin(), newEmail);
        } catch (UserNotFoundException e) {
            logger.info("Problem occurred while testing changing user email"
                    + "\n see more: " + Arrays.asList(e.getStackTrace()));
        }
        verify(userRepository).save(expectedUser);
        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    @DisplayName("when given user wrong login and new email then it throws UserNotFoundException")
    void whenGivenWrongUserLoginAndNewEmail_thenItThrowsUserNotFoundException() {
        //given
        String newEmail = "new@email.com";
        UserDto testUserDto = new UserDto();
        testUserDto.setEmail("old@email.com");
        testUserDto.setLogin("login");
        //when
        //then
        assertThrows(UserNotFoundException.class, () -> userService.changeEmail(testUserDto.getLogin(), newEmail));
        verify(userRepository, never()).save(any());
    }
}
