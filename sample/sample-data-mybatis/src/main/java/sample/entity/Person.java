package sample.entity;

import com.loyayz.gaia.data.mybatis.AbstractEntity;
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
public class Person extends AbstractEntity<Person> {

    private String name;

    public Person(Long id) {
        super();
        super.setId(id);
    }

}
