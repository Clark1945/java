package proj.java.spring.mapstruct;

import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-15T13:22:44+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 1.8.0_362 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getUser_id() );
        userDTO.setName( user.getUser_name() );
        userDTO.setEmail( user.getEmail() );

        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUser_name( userDTO.getName() );
        user.setUser_id( userDTO.getId() );
        user.setEmail( userDTO.getEmail() );

        return user;
    }

    @Override
    public UserDTO createUserDTOWithoutId(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setName( user.getUser_name() );
        userDTO.setEmail( user.getEmail() );

        return userDTO;
    }
}
