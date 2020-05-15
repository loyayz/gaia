package sample.mapper;

import com.loyayz.gaia.data.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import sample.entity.User;

import java.util.List;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE email LIKE CONCAT('%',#{email},'%')")
    List<User> listByEmail(@Param("email") String email);

}
