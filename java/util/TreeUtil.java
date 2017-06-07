package util;

import java.util.ArrayList;
import java.util.List;

import util.TreeNode;

public class TreeUtil {
	/**
	 * 获得树中指定层级的节点
	 * @param nodes
	 * @param level 从0开始
	 * @return
	 */
    public static List<? extends TreeNode> getNodesByLevel(List<? extends TreeNode> nodes, int level) {
        if(level == 0) {
            return nodes;
        } else {
            List<TreeNode> children = new ArrayList<TreeNode>();
            for(TreeNode node : nodes)
                children.addAll( getNodesByLevel(node.getChildren(), level - 1) );
            return children;
        }
    }
    
    /**
     * 从一个列表中构建出树
     * @param parentId
     * @param nodes
     * @return
     */
    public static List<? extends TreeNode> findChildren(Integer parentId, List<? extends TreeNode> nodes) {
        List<TreeNode> children = new ArrayList<TreeNode>();
        for(TreeNode node : nodes) {
            if(parentId.equals(node.getParentId())) {
                children.add(node);
                node.setChildren( findChildren(node.getId(), nodes) );
            }
        }
        return children;
    }
}
