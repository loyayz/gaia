package sample.entity;

import com.loyayz.gaia.data.mybatis.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity<User> {

    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Long roleId;

    public User(Long id) {
        super();
        this.setId(id);
    }

}
