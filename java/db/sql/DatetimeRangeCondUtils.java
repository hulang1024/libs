package db.sql;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期范围查询SQL构造utils
 * @author hulang
 * @sice 2017年9月1日
 * @vesion 1.0
 */
public class DatetimeRangeCondUtils {
    
    /**
     * 同下,但没有前缀
     */
    public static String createNamed(String fieldName, String beginTimeName,
        String endTimeName, String beginTimeValue, String endTimeValue) {
        return createNamed(null, fieldName,
            beginTimeName, endTimeName, beginTimeValue, endTimeValue);
    }
    
    /**
     * 创建包含命名的SQL
     * @param prefix 前缀
     * @param fieldName 要进行比较的时间值的数据库表字段名
     * @param beginTimeName 起始时间值名字
     * @param endTimeName 结束时间值名字
     * @param beginTimeValue 起始时间值
     * @param endTimeValue 结束时间值
     * @return
     */
    public static String createNamed(String prefix, String fieldName,
        String beginTimeName, String endTimeName, String beginTimeValue, String endTimeValue) {
        return createSQL(prefix, fieldName,
            ":" + beginTimeName, ":" + endTimeName, beginTimeValue, endTimeValue);
    }
    
    /**
     * 同下,但没有前缀
     */
    public static String createNamed(String fieldName,
        String beginTimeName, String endTimeName, Map<String, Object> valueMap) {
        return createNamed(null, fieldName, beginTimeName, endTimeName, valueMap);
    }
    
    /**
     * 创建包含命名的SQL
     * @param prefix 前缀,例如"and ", "or "
     * @param fieldName 要进行比较的时间值的数据库表字段名
     * @param beginTimeName 起始时间值名字, 同时是<code>valueMap</code>中的key
     * @param endTimeName 结束时间值名字, 同时是<code>valueMap</code>中的key
     * @param valueMap
     * @return
     */
    public static String createNamed(String prefix, String fieldName,
        String beginTimeName, String endTimeName, Map<String, Object> valueMap) {
        String beginTimeValue = (String) valueMap.get(beginTimeName);
        String endTimeValue = (String) valueMap.get(endTimeName);
        return createSQL(prefix, fieldName,
            ":" + beginTimeName, ":" + endTimeName, beginTimeValue, endTimeValue);
    }
    
    /**
     * 同下,但没有前缀
     */
    public static String create(String fieldName, String beginTimeValue, String endTimeValue) {
        return create(null, fieldName, beginTimeValue, endTimeValue);
    }
    
    /**
     * 创建普通SQL
     * @param prefix
     * @param fieldName 
     * @param beginTimeValue
     * @param endTimeValue
     * @return
     */
    public static String create(String prefix, String fieldName,
        String beginTimeValue, String endTimeValue) {
        return createSQL(prefix, fieldName,
            "'" + beginTimeValue + "'", "'" + endTimeValue + "'", beginTimeValue, endTimeValue);
    }
    
    
    private static String createSQL(String prefix, String fieldName,
        String beginTimeNameOrValue, String endTimeNameOrValue, String beginTimeValue, String endTimeValue) {
        
        StringBuilder cond = new StringBuilder();
        
        beginTimeValue = StringUtils.trim(beginTimeValue);
        endTimeValue = StringUtils.trim(endTimeValue);
        
        if (StringUtils.isNotEmpty(beginTimeValue) && StringUtils.isNotEmpty(endTimeValue)) {
            cond.append("to_date(").append(beginTimeNameOrValue)
                .append(",'YYYY-MM-DD HH24:MI:SS')<=").append(fieldName);
            cond.append(" and ");
            cond.append(fieldName).append("<=to_date(")
                .append(endTimeNameOrValue).append(",'YYYY-MM-DD HH24:MI:SS')");
        } else if (StringUtils.isEmpty(beginTimeValue) && StringUtils.isNotEmpty(endTimeValue)) {
            cond.append(fieldName).append("<=to_date(")
                .append(endTimeNameOrValue).append(",'YYYY-MM-DD HH24:MI:SS')");
        } else if (StringUtils.isNotEmpty(beginTimeValue) && StringUtils.isEmpty(endTimeValue)) {
            cond.append("to_date(").append(beginTimeNameOrValue)
                .append(",'YYYY-MM-DD HH24:MI:SS')<=").append(fieldName);
        }
        
        if (cond.length() == 0)
            return "";
            
        return StringUtils.isNotEmpty(prefix) ?
            prefix + cond.toString() : cond.toString();
    }
   
}
