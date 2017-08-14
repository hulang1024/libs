package util;

import java.util.List;

public interface TreeNode {
	Integer getId();
	Integer getParentId();
	void setParentId(Integer parentId);

	String getText();
	void setText(String text);

	void addChild(TreeNode child);
	List<? extends TreeNode> getChildren();
	void setChildren(List<? extends TreeNode> nodes);
}
