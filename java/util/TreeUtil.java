package util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.Closure;
import org.springframework.util.ObjectUtils;

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
    
    public static void forEach(List<? extends TreeNode> nodes, Closure closure) {
        if (nodes != null) {
            for(TreeNode node : nodes) {
                closure.execute(node);
                forEach(node.getChildren(), closure);
            }
        }
    }
    
    public static TreeNode findById(Integer id, List<? extends TreeNode> nodes) {
        TreeNode foundNode;
        if (nodes != null) {
            for(TreeNode node : nodes) {
                if(id.equals(node.getId()))
                    return node;
                else {
                    foundNode = findById(id, node.getChildren());
                    if (foundNode != null)
                        return foundNode;
                }
            }
        }
        return null;
    }
    
    public static List<? extends TreeNode> buildTrees(List<? extends TreeNode> nodes) {
        if (nodes == null)
            return null;
        List<TreeNode> trees = new ArrayList<TreeNode>();
        for(TreeNode node : nodes) {
            if (ObjectUtils.isEmpty(node.getChildren())) {
                node.setChildren(findChildren(node.getId(), nodes));
                buildTrees(node.getChildren());
            }
            
            trees.add(node);
        }
        
        List<TreeNode> topTrees = new ArrayList<TreeNode>();
        boolean contains;
        for (TreeNode node : trees) {
            contains = false;
            for (TreeNode other : trees) {
                if (!other.getId().equals(node.getId()))
                    if( findById(node.getId(), Arrays.asList(other)) != null) {
                        contains = true;
                        break;
                    }
            }
            if (!contains)
                topTrees.add(node);
        }
        
        return topTrees;
    }
}
