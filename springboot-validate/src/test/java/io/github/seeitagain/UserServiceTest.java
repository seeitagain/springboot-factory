package io.github.seeitagain;

import io.github.seeitagain.dto.UserAddDTO;
import io.github.seeitagain.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ValidateApplication.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testGet(){
        userService.get(-1);
    }
    @Test
    public void testAdd(){
        UserAddDTO addDTO = new UserAddDTO();
        userService.add(addDTO);
    }

    @Test
    public void testAdd01(){
        UserAddDTO addDTO = new UserAddDTO();
        userService.add01(addDTO);
    }

    @Test
    public void testAdd02(){
        UserAddDTO addDTO = new UserAddDTO();
        userService.add02(addDTO);
    }
}
