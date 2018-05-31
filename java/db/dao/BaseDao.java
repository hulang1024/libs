package db.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.ByteConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.FloatConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import db.Page;
import db.PagingParams;
import db.dao.annotation.PK;
import db.dao.annotation.Seq;
import db.dao.annotation.Table;

/**
 * 
 * @author hulang
 * @sice 2017年8月14日
 * @vesion 1.0
 */
@Repository
public class BaseDao {
    @Autowired
    protected JdbcTemplate jdbctpl;
        
    static {
        ConvertUtils.register(new BooleanConverter(null), Boolean.class);  
        ConvertUtils.register(new ByteConverter(null), Byte.class);  
        ConvertUtils.register(new ShortConverter(null), Short.class);  
        ConvertUtils.register(new IntegerConverter(null), Integer.class);  
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new FloatConverter(null), Float.class);  
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        
        ConvertUtils.register(new DateConverter(null), java.util.Date.class);
    }
    
    /**
     * 对象的PK注解解释结果信息
     * @author hulang
     * @sice 2017年8月24日
     * @vesion 1.0
     */
    private static class PKInfo {
        /**
         * 注解目标字段
         */
        public Field field;
        /**
         * 数据库表中主键名称
         */
        public String pkName;
        
        /**
         * 分析PK注解
         * @param clazz
         * @return
         */
        public static List<PKInfo> getList(Class<?> clazz) {
            List<PKInfo> pkInfoList = new ArrayList<PKInfo>();
            Field[] fields = clazz.getDeclaredFields();  
            for (Field field : fields) {  
                if(field.isAnnotationPresent(PK.class)) {  
                    PKInfo pkInfo = new PKInfo();
                    pkInfo.field = field;
                    String name = field.getAnnotation(PK.class).name();
                    pkInfo.pkName = StringUtils.isEmpty(name) ? camelCaseToUnderline(field.getName()) : name;
                    pkInfoList.add(pkInfo);
                }
            }
            
            return pkInfoList;
        }
    }
    
    private static class SeqInfo {
        /**
         * 注解目标字段
         */
        public Field field;
        /**
         * 数据库表中序列名称
         */
        public String seqName;
        
        /**
         * 分析序列注解
         * @param clazz
         * @return
         */
        public static SeqInfo get(Class<?> clazz) {
            Field[] fields = clazz.getDeclaredFields();  
            for (Field field : fields) {  
                if(field.isAnnotationPresent(Seq.class)) {  
                    SeqInfo seqInfo = new SeqInfo();
                    seqInfo.field = field;
                    String name = field.getAnnotation(Seq.class).name();
                    if (StringUtils.isEmpty(name)) {
                        seqInfo.seqName = String.format("SEQ_%s", getTableName(clazz));
                    } else {
                        seqInfo.seqName = name;
                    }
                    return seqInfo;
                }
            }
            
            return null;
        }
    }
    
    public JdbcTemplate getJdbctpl() {
        return jdbctpl;
    }

    public void setJdbctpl(JdbcTemplate jdbctpl) {
        this.jdbctpl = jdbctpl;
    }
    
    public <T> T get(Class<T> type, String sql, Object... args) {
        try {
            return mapToBean(type, jdbctpl.queryForMap(sql, args));
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public <T> List<T> getAll(Class<T> type, String... columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append((columns.length == 0 ? "*" : StringUtils.join(columns, ",")));
        sql.append(" from ").append(getTableName(type));
        return getList(type,  sql.toString());
    }
    
    public <T> List<T> getList(Class<T> type, String sql, Object... args) {
        return mapListToBeanList(type, jdbctpl.queryForList(sql, args));
    }
    
    public <T> List<T> getList(Class<T> type, String sql, Map<String, Object> params) {
        return mapListToBeanList(type, new NamedParameterJdbcTemplate(jdbctpl).queryForList(sql, params));
    }
    
    public <T> List<T> queryForList(Class<T> type, String sql, Object... args) {
        return jdbctpl.queryForList(sql, type, args);
    }
    
    public <T> List<T> queryForList(Class<T> type, String sql, Map<String, Object> params) {
        return new NamedParameterJdbcTemplate(jdbctpl).queryForList(sql, params, type);
    }
    
    public int count(String tableName, String conds, Object... args) {
        StringBuilder select = new StringBuilder();
        select.append("select count(1) from ").append(tableName);
        select.append(" where ").append(conds);
        return jdbctpl.queryForObject(select.toString(), Integer.class, args);
    }
    
    public boolean exists(String tableName, String conds, Object... args) {
    	return count(tableName, conds, args) != 0;
    }
    
    public boolean existsByPK(Class<?> clazz, Object... pks) {
        return exists(getTableName(clazz), pkCond(PKInfo.getList(clazz)), pks);
    }
    
    public boolean exists(Object bean) {
        final Map<String, Object> map = csmms.common.utils.BeanUtils.describeMap(bean);
        List<PKInfo> pkInfoList = PKInfo.getList(bean.getClass());
        Object[] pkValues = new Object[pkInfoList.size()];
        int index = 0;
        for (PKInfo pkInfo : pkInfoList) {
            pkValues[index++] = map.get(pkInfo.field.getName());
        }
        return exists(getTableName(bean.getClass()), pkCond(pkInfoList), pkValues);
    }
    
    public int count(String selectSql, Object... args) {
        StringBuilder select = new StringBuilder();
        select.append("select count(1) from(");
        select.append(selectSql);
        select.append(") tmp_for_rownum_ ");
        return jdbctpl.queryForObject(select.toString(), Integer.class, args);
    }
    
    public int count(String selectSql, Map<String,Object> args) {
        StringBuilder select = new StringBuilder();
        select.append("select count(1) from(");
        select.append(selectSql);
        select.append(") tmp_for_rownum_ ");
        return new NamedParameterJdbcTemplate(jdbctpl).queryForObject(select.toString(), args, Integer.class);
    }
    
    /**
     * 插入
     * @param bean
     * @return
     */
    public boolean save(Object bean) {
        return save(bean ,false) == 1;
    }
    
    public int saveGetIntPK(Object bean) {
    	return save(bean, true);
    }
    
    private int save(Object bean, boolean getPk) {
        final Map<String, Object> map = csmms.common.utils.BeanUtils.describeMap(bean);
        /// insert sql
        final StringBuilder sql = new StringBuilder("insert into ");
        // table name
        String tableName = getTableName(bean.getClass());
        sql.append(tableName);
        
        final SeqInfo seqInfo = SeqInfo.get(bean.getClass());
        final boolean generateSeq = seqInfo != null;
        int seqIndexInValuePlace = 0;

        // column names
        sql.append("(");
        int valueCount = 0;
        String key;
        for (Iterator<String> keyIter = map.keySet().iterator(); keyIter.hasNext();) { // 注意:与设置参数的遍历方式相同,保证正确顺序
            key = keyIter.next();
            
            if (generateSeq && key.equals(seqInfo.field.getName()))
                seqIndexInValuePlace = valueCount;
            
            sql.append(camelCaseToUnderline(key));
            valueCount++;
            if (keyIter.hasNext())
                sql.append(",");
        }
        sql.append(")");
        
        // value palces
        String[] valuePalces = new String[valueCount];
        Arrays.fill(valuePalces, "?");
        // 判断如果具有序列注解字段,并且bean中该属性值为null
        if (generateSeq) {
            valuePalces[seqIndexInValuePlace] = seqInfo.seqName + ".nextval";
        }
        sql.append(" values(").append(StringUtils.join(valuePalces, ",")).append(")");
        
        int rows = jdbctpl.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement stmt = con.prepareStatement(sql.toString());
                String key;
                Object val;
                int index = 1;
                for (Iterator<String> keyIter = map.keySet().iterator(); keyIter.hasNext(); ) {
                    key = keyIter.next();
                    if (generateSeq && key.equals(seqInfo.field.getName()))
                        continue;
                    val = map.get(key);
                    if (val instanceof Date)
                        stmt.setTimestamp(index, new Timestamp(((Date)val).getTime()));
                    else
                        stmt.setObject(index, val);
                    index++;
                }
                return stmt;
            }
        });
        if (getPk) {
            if (seqInfo != null) {
                return jdbctpl.queryForObject(String.format("select %s.currval from dual", seqInfo.seqName), Integer.class);
            } else {
                throw new RuntimeException("no seq");
            }
        }
        return rows;

    }
    

    /**
     * 根据主键获取bean
     * @param clazz
     * @param pkValue
     * @return
     */
    public <T> T getByPK(Class<T> clazz, Object pkValue) {
        PKInfo pkInfo = PKInfo.getList(clazz).get(0);
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getTableName(clazz));
        sql.append(" where ").append(pkInfo.pkName).append("=?");
        return get(clazz, sql.toString(), pkValue);
    }
    
    /**
     * 根据主键更新
     * @param bean
     * @return
     */
    public <T> boolean updateByPK(Object bean) {
        final Map<String, Object> map = csmms.common.utils.BeanUtils.describeMap(bean);
        
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getTableName(bean.getClass())).append(" set ");

        final List<PKInfo> pkInfoList = PKInfo.getList(bean.getClass());
        final List<Object> values = new ArrayList<Object>();
        boolean prevComma = false;
        // 设置列名（不包括主键）
        for (Iterator<String> keyIter = map.keySet().iterator(); keyIter.hasNext(); ) {
            String key = keyIter.next();
            
            boolean isPK = false;
            for (PKInfo pkInfo : pkInfoList) {
                if (key.equals(pkInfo.field.getName())) {
                    isPK = true;
                    break;
                }
            }
            
            if (!isPK) {
                if (prevComma)
                    sql.append(",");
                sql.append(camelCaseToUnderline(key)).append("=").append("?");
                if (keyIter.hasNext())
                    prevComma = true;
                values.add(map.get(key));
            }
        }
        for (PKInfo pkInfo : pkInfoList) {
            values.add(map.get(pkInfo.field.getName()));
        }
        
        // where
        sql.append(" where ").append(pkCond(pkInfoList));
        
        int rows = jdbctpl.update(sql.toString(), new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int index = 1;
                for (Iterator<Object> iter = values.iterator(); iter.hasNext();) {
                    Object val = iter.next();
                    if (val instanceof Date) {
                        ps.setTimestamp(index, new Timestamp(((Date)val).getTime()));
                    } else {
                        ps.setObject(index, val);
                    }
                    index++;
                }
            }
        });
        
        return rows == 1;
    }

    /**
     * 根据主键更新,只更新非空字段
     * <br>要求字段都是引用类型,对于基本类型是包装器类型(例如Integer),才能与null进行比较
     * @param bean
     * @return
     */
    public boolean updateByPKSelectiveNonNull(Object bean) {
        final Map<String, Object> map = csmms.common.utils.BeanUtils.describeMap(bean);
        
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(getTableName(bean.getClass())).append(" set ");
        
        final List<Object> values = new ArrayList<Object>();
        final List<PKInfo> pkInfoList = PKInfo.getList(bean.getClass());
        // 设置非空值的列名占位（不包括主键）
        boolean prevComma = false;
        for (Iterator<String> keyIter = map.keySet().iterator(); keyIter.hasNext(); ) {
            String key = keyIter.next();
            
            boolean isPK = false;
            for (PKInfo pkInfo : pkInfoList) {
                if (key.equals(pkInfo.field.getName())) {
                    isPK = true;
                    break;
                }
            }
            
            if (!isPK) {
                Object val = map.get(key);
                if (val != null) {
                    values.add(val);
                    if (prevComma)
                        sql.append(",");
                    sql.append(camelCaseToUnderline(key)).append("=").append("?");
                    if (keyIter.hasNext())
                        prevComma = true;
                }
            }
        }
        for (PKInfo pkInfo : pkInfoList) {
            values.add(map.get(pkInfo.field.getName()));
        }
        
        // where
        sql.append(" where ").append(pkCond(pkInfoList));

        int rows = jdbctpl.update(sql.toString(), new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                Object val;
                int index = 1;
                for (Iterator<Object> valueIter = values.iterator(); valueIter.hasNext(); ) {
                    val = valueIter.next();
                    if (val instanceof Date) {
                        ps.setTimestamp(index, new Timestamp(((Date)val).getTime()));
                    } else {
                        ps.setObject(index, val);
                    }
                    index++;
                }
            }
        });
        
        return rows == 1;
    }
    
    /**
     * 根据主键删除
     * @param clazz
     * @param pkValue
     * @return
     */
    public <T> boolean deleteByPK(Class<?> clazz, final Object pkValue) {        
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getTableName(clazz));
        sql.append(" where ").append(pkCond(PKInfo.getList(clazz)));

        int rows = jdbctpl.update(sql.toString(), new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setObject(1, pkValue);
            }
        });
        
        return rows == 1;
    }
    
    /**
     * 根据组合主键删除
     * @param clazz
     * @param pkValue
     * @return
     */
    public <T> boolean deleteByPKs(Class<?> clazz, final Object... pkValues) {        
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(getTableName(clazz));
        sql.append(" where ").append(pkCond(PKInfo.getList(clazz)));

        int rows = jdbctpl.update(sql.toString(), new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int index = 1;
                for (Object pkValue : pkValues)
                    ps.setObject(index++, pkValue);
            }
        });
        
        return rows == 1;
    }
    
    
    /**
     * 获取分页bean
     * @param type
     * @param selectSql
     * @param pparams
     * @param args
     * @return
     */
    public <T> Page<T> getPage(Class<T> type, String selectSql, PagingParams pparams, Object... args) {
        Page<T> pageObj = new Page<T>();
        pageObj.setRows(getList(type, createPageSQL(type, selectSql, pparams), args));
        pageObj.setTotal(this.count(selectSql, args));
        return pageObj;
    }
    
    public <T> Page<T> getPageAll(Class<T> type, PagingParams pparams, String... columns) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ").append((columns.length == 0 ? "*" : StringUtils.join(columns, ",")));
        sql.append(" from ").append(getTableName(type));
        return getPage(type, sql.toString(), pparams);
    }
    
    public <T> Page<T> getPage(Class<T> type, StringBuilder selectSql, PagingParams pparams, Map<String, Object> params) {
        return getPage(type, selectSql.toString(), pparams, params);
    }
    
    public <T> Page<T> getPage(Class<T> type, String selectSql, PagingParams pparams, Map<String, Object> params) {
        Page<T> pageObj = new Page<T>();
        pageObj.setRows(getList(type, createPageSQL(type, selectSql, pparams), params));
        pageObj.setTotal(this.count(selectSql,params));
        return pageObj;
    }
    
    public int delete(String sql, Object... args) {
        return doSQL(sql, args);
    }
    
    public int insert(String sql, Object... args) {
        return doSQL(sql, args);
    }
    
    public int update(String sql, Object... args) {
        return doSQL(sql, args);
    }
    
    public int delete(String sql, Map<String, Object> params) {
        return doSQL(sql, params);
    }
    
    public int insert(String sql, Map<String, Object> params) {
        return doSQL(sql, params);
    }
    
    public int update(String sql, Map<String, Object> params) {
        return doSQL(sql, params);
    }
    
    private int doSQL(String sql, Object... args) {
        return jdbctpl.update(sql, args);
    }
    
    private int doSQL(String sql, Map<String, Object> params) {
        return new NamedParameterJdbcTemplate(jdbctpl).update(sql, params);
    }
    
    private String createPageSQL(Class<?> type, String selectSql, PagingParams pparams) {
        int page = pparams.getPage();
        int rows = pparams.getRows();
        int staRow = (page - 1) * rows + 1;
        int endRow = staRow + rows - 1;
        
        StringBuilder sql = new StringBuilder();
        sql.append("select * from(select tmp_for_rownum_.*,rownum rn from(");
        sql.append(selectSql);
        sql.append(") tmp_for_rownum_ where rownum<=");
        sql.append(endRow);
        sql.append(") where rn>=");
        sql.append(staRow);
        
        return sql.toString();
    }
    
    private String pkCond(List<PKInfo> pkInfoList) {
        StringBuilder sb = new StringBuilder();
        int count = pkInfoList.size();
        for (PKInfo pkInfo : pkInfoList) {
            sb.append(pkInfo.pkName).append("=").append("?");
            if (count-- > 1)
                sb.append(" and ");
        }
        return sb.toString();
    }
    
    private static <T> List<T> mapListToBeanList(Class<T> type, List<Map<String, Object>> mapList) {
        List<T> beanList = new ArrayList<T>();
        for (Map<String, Object> map : mapList) {
            beanList.add(mapToBean(type, map));
        }

        return beanList;
    }
    
    /* SQL查询结果map转换到bean */
    private static <T> T mapToBean(Class<T> type, Map<String, Object> map) {
        map = toUnderlineToCamelCase(map);
        
        try {
            T bean = type.newInstance();
            BeanUtils.populate(bean, map);
            return bean;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private static Map<String, Object> toUnderlineToCamelCase(Map<String, Object> map) {
        Map<String, Object> map1 = new HashMap<String, Object>(map.size());
        for (String key : map.keySet()) {
            map1.put(underlineToCamelCase(key), map.get(key));
        }
        
        return map1;
    }
    
    /* 获取表名 */
    private static String getTableName(Class<?> clazz) {
        if( clazz.isAnnotationPresent(Table.class))
            return clazz.getAnnotation(Table.class).name();
        return camelCaseToUnderline(clazz.getSimpleName());
    }
    
    /* 将下划线大写风格转换为驼峰风格 */
    private static String underlineToCamelCase(String str) {
        StringBuilder sb = new StringBuilder();
        
        boolean nextUpperCase = false;
        char c;
        for (int i = 0, len = str.length(); i < len; i++) {
            c = str.charAt(i);
            if (c == '_') {
                nextUpperCase = true;
            } else {
                if (nextUpperCase) {
                    sb.append(Character.toUpperCase(c));
                    nextUpperCase = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
        }
        
        return sb.toString();
    }
    
    /* 将驼峰风格转换为下划线大写风格 */
    private static String camelCaseToUnderline(String str) {
        //TODO:需要考虑连续大写，例如URLAbc，应转换为URL_ABC
        StringBuilder sb = new StringBuilder();
        
        sb.append(Character.toUpperCase(str.charAt(0)));
        char c;
        for (int i = 1, len = str.length(); i < len; i++) {
            c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_").append(c);
            } else {
                sb.append(Character.toUpperCase(c));
            }
        }
        
        return sb.toString();
    }
}
