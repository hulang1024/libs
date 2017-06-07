package dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xtxk.hb.framework.model.Page;
import com.xtxk.hb.framework.model.PageInfo;
import com.xtxk.hb.framework.model.PageRowBounds;

/**
 * Mybatis SqlSessionTemplate的简单封装，可用作一般DAO
 * @author hulang
 * @sice 2017年5月27日
 * @vesion 1.0
 */
@Component
public class SimpleMybatisSqlSessionTemplate {
    @Autowired
    protected SqlSessionTemplate template;
    
    public <E> Page<E> list(Class<E> clazz, String statement, Object parameter, PageInfo pageInfo) {
        return (Page<E>) template.selectList(statement, parameter, new PageRowBounds(pageInfo));
    }
}
