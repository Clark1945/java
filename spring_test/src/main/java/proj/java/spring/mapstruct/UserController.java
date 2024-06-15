package proj.java.spring.mapstruct;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/addUser")
    public String addUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        UserDTO userDTO1 = UserMapper.INSTANCE.userToUserDTO(user);
        UserDTO userTransfer = UserMapper.INSTANCE.createUserDTOWithoutId(user);
        System.out.println("User drop after transfer = " + userTransfer);
        return user.toString();
    }
}
