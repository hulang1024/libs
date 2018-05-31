package db.sql;

import org.springframework.util.StringUtils;

/**
 * SQL where
 * 解决拼接SQL字符串开头多余的and,or等问题
 * @author hulang
 * @sice 2017年9月1日
 * @vesion 1.0
 */
public class Where {
    private StringBuilder sql = new StringBuilder();
    
    public Where() { }
    
    /**
     * 追加内容
     * @param s
     * @return
     */
    public Where add(String s, String... rest) {
        if (!StringUtils.isEmpty(s))
            sql.append(" ").append(s);
        for (int i = 0; i < rest.length; i++) {
            s = rest[i];
            if (!StringUtils.isEmpty(s))
                sql.append(s);
        }
        return this;
    }
    
    /**
     * 与add相同，add的别名
     * @param s
     * @return
     */
    public Where append(String s, String... rest) {
        return add(s, rest);
    }
    
    /**
     * @return SQL字符串
     */
    @Override
    public String toString() {
        String content = sql.toString();
        while (!content.isEmpty()) {
            content = StringUtils.trimLeadingWhitespace(content);
            if (content.startsWith("and"))
                content = content.substring("and".length());
            else if (content.startsWith("or"))
                content = content.substring("or".length());
            else break;
        }
        while (!content.isEmpty()) {
            content = StringUtils.trimTrailingWhitespace(content);
            if (content.endsWith("and"))
                content = content.substring(0, content.length() - "and".length());
            else if (content.endsWith("or"))
                content = content.substring(0, content.length() - "or".length());
            else break;
        }
        
        return content.isEmpty() ? "" : " where " + content;
    }
}
