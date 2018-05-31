package db;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页数据表示
 * @sice 2017年8月21日
 * @vesion 1.0
 * @param <T>
 */
public class Page<T> {
    private Integer total = 0;
    private List<T> rows;
    
    public static <T> Page<T> empty(Class<T> c) {
        Page<T> page = new Page<T>();
        page.setRows(new ArrayList<T>());
        return page;
    }
    
    /**
     * 总行数
     * @return
     */
    public Integer getTotal() {
        return total;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    /**
     * 分页中记录数据
     * @return
     */
    public List<T> getRows() {
        return rows;
    }
    public void setRows(List<T> rows) {
        this.rows = rows;
    }

}
