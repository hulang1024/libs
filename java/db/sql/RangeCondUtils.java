package db.sql;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * 范围查询SQL构造utils
 * @author hulang
 * @sice 2017年9月3日
 * @vesion 1.0
 */
public class RangeCondUtils {
    
    /**
     * 同下,但没有前缀
     */
    public static String createNamed(String fieldName, String beginName,
        String endName, String beginValue, String endValue) {
        return createNamed(null, fieldName,
            beginName, endName, beginValue, endValue);
    }
    
    /**
     * 创建包含命名的SQL
     * @param prefix 前缀
     * @param fieldName 要进行比较的值的数据库表字段名
     * @param beginName 起始值名字
     * @param endName 结束值名字
     * @param beginValue 起始值
     * @param endValue 结束值
     * @return
     */
    public static String createNamed(String prefix, String fieldName,
        String beginName, String endName, String beginValue, String endValue) {
        return createSQL(prefix, fieldName,
            ":" + beginName, ":" + endName, beginValue, endValue);
    }
    
    /**
     * 同下,但没有前缀
     */
    public static String createNamed(String fieldName,
        String beginName, String endName, Map<String, Object> valueMap) {
        return createNamed(null, fieldName, beginName, endName, valueMap);
    }
    
    /**
     * 创建包含命名的SQL
     * @param prefix 前缀,例如"and ", "or "
     * @param fieldName 要进行比较的值的数据库表字段名
     * @param beginName 起始值名字, 同时是<code>valueMap</code>中的key
     * @param endName 结束值名字, 同时是<code>valueMap</code>中的key
     * @param valueMap
     * @return
     */
    public static String createNamed(String prefix, String fieldName,
        String beginName, String endName, Map<String, Object> valueMap) {
        String beginValue = Objects.toString(valueMap.get(beginName), "");
        String endValue = Objects.toString(valueMap.get(endName), "");
        return createSQL(prefix, fieldName,
            ":" + beginName, ":" + endName, beginValue, endValue);
    }
    
    /**
     * 同下,但没有前缀
     */
    public static String create(String fieldName, String beginValue, String endValue) {
        return create(null, fieldName, beginValue, endValue);
    }
    
    /**
     * 创建普通SQL
     * @param prefix
     * @param fieldName 
     * @param beginValue
     * @param endValue
     * @return
     */
    public static String create(String prefix, String fieldName,
        String beginValue, String endValue) {
        return createSQL(prefix, fieldName,
            "'" + beginValue + "'", "'" + endValue + "'", beginValue, endValue);
    }
    
    
    private static String createSQL(String prefix, String fieldName,
        String beginNameOrValue, String endNameOrValue, String beginValue, String endValue) {
        
        StringBuilder cond = new StringBuilder();
        
        beginValue = StringUtils.trim(beginValue);
        endValue = StringUtils.trim(endValue);
        
        if (StringUtils.isNotEmpty(beginValue) && StringUtils.isNotEmpty(endValue)) {
            cond.append(beginNameOrValue).append("<=").append(fieldName);
            cond.append(" and ");
            cond.append(fieldName).append("<=").append(endNameOrValue);
        } else if (StringUtils.isEmpty(beginValue) && StringUtils.isNotEmpty(endValue)) {
            cond.append(fieldName).append("<=").append(endNameOrValue);
        } else if (StringUtils.isNotEmpty(beginValue) && StringUtils.isEmpty(endValue)) {
            cond.append(beginNameOrValue).append("<=").append(fieldName);
        }
        
        if (cond.length() == 0)
            return "";
            
        return StringUtils.isNotEmpty(prefix) ?
            prefix + cond.toString() : cond.toString();
    }
   
}
