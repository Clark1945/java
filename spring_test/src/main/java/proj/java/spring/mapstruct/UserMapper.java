package proj.java.spring.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "user_id",target = "id")
    @Mapping(source = "user_name",target = "name")
    UserDTO userToUserDTO(User user);
    @Mapping(source = "name",target = "user_name")
    @Mapping(source = "id",target = "user_id")
    User userDTOToUser(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "user_name",target = "name")
    UserDTO createUserDTOWithoutId(User user);
}
