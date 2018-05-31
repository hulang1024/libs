package util;

import java.util.Arrays;
import java.util.Collection;

import db.Page;
import db.PagingParams;

/**
 * 集合分页工具
 * @author hulang
 * @sice 2018年5月23日
 */
public class CollectionPagingUtils {
    /**
     * 分页
     * @param coll 集合
     * @param pparams 分页参数
     * @return 按照分页参数返回分页
     */
    @SuppressWarnings("unchecked")
    public static <T> Page<T> toPage(Collection<T> coll, PagingParams pparams) {
        return toPage((T[])coll.toArray(), pparams);
    }
    
    /**
     * 分页
     * @param array 数组
     * @param pparams 分页参数
     * @return 按照分页参数返回分页
     */
    public static <T> Page<T> toPage(T[] array, PagingParams pparams) {
        int offset = (pparams.getPage() - 1) * pparams.getRows();
        array = Arrays.copyOfRange(array, offset, Math.min(array.length, offset + pparams.getRows()));
        
        Page<T> page = new Page<T>();
        page.setTotal(array.length);
        page.setRows(Arrays.asList(array));
        
        return page;
    }
}
