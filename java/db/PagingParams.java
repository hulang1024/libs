package db;

/**
 * 分页参数
 * @author hulang
 * @sice 2017年8月21日
 * @vesion 1.0
 */
public class PagingParams {
    /**
     * 默认1
     */
    private Integer page = 1;
    /**
     * 默认20
     */
    private Integer rows = 20;
    
    public PagingParams() {}
    public PagingParams(Integer page, Integer rows) {
        this.page = page;
        this.rows = rows;
    }
    
    /**
     * 当前第几页,从1开始数
     * @return
     */
    public Integer getPage() {
        return page;
    }
    public void setPage(Integer page) {
        this.page = page;
    }
    
    /**
     * 每页记录数
     * @return
     */
    public Integer getRows() {
        return rows;
    }
    public void setRows(Integer rows) {
        this.rows = rows;
    }
}
