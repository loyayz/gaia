package com.loyayz.gaia.data.mybatis.extension;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.core.injector.methods.Insert;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;
import java.util.stream.Stream;

import static com.loyayz.gaia.data.mybatis.extension.MybatisConstants.*;
import static java.util.stream.Collectors.toList;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
public class DefaultSqlInjector extends AbstractSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return Stream.of(
                new Insert() {
                    @Override
                    public String getMethod(SqlMethod sqlMethod) {
                        return METHOD_INSERT;
                    }
                },
                new InsertBatchSomeColumn() {
                    @Override
                    public String getMethod(SqlMethod sqlMethod) {
                        return METHOD_BATCH_INSERT;
                    }
                },
                new DeleteById() {
                    @Override
                    public String getMethod(SqlMethod sqlMethod) {
                        return METHOD_DELETE_BY_ID;
                    }
                },
                new DeleteBatchByIds() {
                    @Override
                    public String getMethod(SqlMethod sqlMethod) {
                        return METHOD_DELETE_BY_IDS;
                    }
                },
                new UpdateById() {
                    @Override
                    public String getMethod(SqlMethod sqlMethod) {
                        return METHOD_UPDATE_BY_ID;
                    }
                },
                new FindById(),
                new ListByIds(),
                new ListByCondition(),
                new CountByCondition()
        ).collect(toList());
    }

}
