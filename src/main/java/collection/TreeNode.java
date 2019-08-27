package collection;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T> {
	public static final int DEPTH_FIRST_MODE = 0;
	public static final int BREATH_FIRST_MODE = 1;
	
	T data;
	List<TreeNode<T>> children;

	public TreeNode(T data) {
		this.data = data;
	}
		
	public TreeNode<T> getChild(int i) {
		if (i >= 0 && i < children.size()) {
			return children.get(i);
		} else {
			return null;
		}
	}
	
	/**
     * Adds a child to the list of children for this Node<T>. The addition of
     * the first child will create a new List<Node<T>>.
     * @param child a Node<T> object to set.
     */
	public void addChild(TreeNode<T> child) {
		if (this.children == null) {
			this.children = new ArrayList<TreeNode<T>>();
		}
		this.children.add(child);
	}
	
	/**
     * Inserts a Node<T> at the specified position in the child list. Will     
     * throw an ArrayIndexOutOfBoundsException if the index does not exist.
     * @param index the position to insert at.
     * @param child the Node<T> object to insert.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void addChildAt(TreeNode<T> child, int index) throws IndexOutOfBoundsException {
        if (index == getNumberOfChildren()) {
            // this is really an append
            addChild(child);
            return;
        } else {
            children.get(index); //just to throw the exception, and stop here
            children.add(index, child);
        }
    }
    
    /**
     * Remove the Node<T> element at index index of the List<Node<T>>.
     * @param index the index of the element to delete.
     * @throws IndexOutOfBoundsException if thrown.
     */
    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    
    /**
     * Returns the number of immediate children of this Node<T>.
     * @return the number of immediate children.
     */
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }


	
	
	
	
	
    /**
     * Returns the Tree<T> as a List of Node<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     * @return a List<Node<T>>.
     */
    public List<TreeNode<T>> toList() {
    	return toList(DEPTH_FIRST_MODE);
    }
    
	/**
     * Returns the Tree<T> as a List of Node<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     * @param mode tree walking mode, DEPTH_FIRST_MODE or BREATH_FIRST_MODE
     * @return a List<Node<T>>.
     */
    public List<TreeNode<T>> toList(int mode) {
    	List<TreeNode<T>> list = new ArrayList<TreeNode<T>>();
        if (mode == DEPTH_FIRST_MODE) {
        	walkDepthFirst(this, list);
        } else if (mode == BREATH_FIRST_MODE) {
        	List<TreeNode<T>> sibList = new ArrayList<TreeNode<T>>();
        	sibList.add(this);
        	List<TreeNode<T>> childList = new ArrayList<TreeNode<T>>();
        	walkBreathFirst(0, sibList, childList, list);
        } else {
        	throw new IllegalArgumentException("wrong input of tree walking mode"); 
        }
    	
        return list;
    }
    
    /**
     * Walks the Tree in depth-first style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference     
     * as it recurses down the tree.
     * @param node the starting element.
     * @param list the output of the walk.
     */
    private void walkDepthFirst(TreeNode<T> node, List<TreeNode<T>> list) {
        list.add(node);
        for (TreeNode<T> data : node.getChildren()) {
            walkDepthFirst(data, list);
        }
    }
    
    /**
     * Walks the Tree in breath-first style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference     
     * as it recurses down the tree.
     * @param node the starting element.
     * @param list the output of the walk.
     */
    private void walkBreathFirst(int sibIndex, List<TreeNode<T>> sibList, List<TreeNode<T>> childList, List<TreeNode<T>> list) {
    	TreeNode<T> node = sibList.get(sibIndex);
    	
    	list.add(node);
    	for (TreeNode<T> child : node.getChildren()) {
    		childList.add(child);
    	}
    	
    	
    	if (sibIndex == (sibList.size()-1)) { // last node in the sibling list
    		if (childList != null && childList.size() > 0) {
    			walkBreathFirst(0, childList, new ArrayList<TreeNode<T>>(), list);  // go down one level
    		} else {
    			return;  // exit of the recursive call stack
    		}	
    	} else { // not the last node in the sibling list
    		walkBreathFirst(sibIndex+1, sibList, childList, list);  // move to next sibling node
    	}
    }
    
	
    public List<TreeNode<T>> getChildren() {
		if (children == null) {
			return new ArrayList<TreeNode<T>>();
		}
		return children;
	}
    
	public void setChildren(List<TreeNode<T>> children) {
		this.children = children;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append(getData().toString()).append(",[");
        int i = 0;
        for (TreeNode<T> e : getChildren()) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(e.getData().toString());
            i++;
        }
        sb.append("]").append("}");
        return sb.toString();
    }
	
	/**
     * Returns a String representation of the node and all its children. The elements are generated
     * from a pre-order traversal of the Tree.
     * @return the String representation of the Tree.
     */
    public String toStringWalk(int walkMode) {
        return toList(walkMode).toString();
    }
	
	/**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     * @return the String representation of the Tree.
     */
    public String toStringWalkFormatted(int walkMode) {
    	StringBuilder sb = new StringBuilder("Print out the tree: \n");
    	for (TreeNode<T> node : toList(walkMode)) {
    		 sb.append("\t" + node.toString() + "\n");
    	}
        return sb.toString();
    }
	
	

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// simple test with node type of String
		TreeNode<String> rootNode = new TreeNode<String>("root string node");
		
		for (int i=0; i<5; i++) {
			rootNode.addChild(new TreeNode<String>("child node " + i));
		}
		
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.0"));
		rootNode.getChild(2).getChild(0).addChild(new TreeNode<String>("child node 2.0.0"));
		rootNode.getChild(2).getChild(0).addChild(new TreeNode<String>("child node 2.0.1"));
		rootNode.getChild(2).getChild(0).addChild(new TreeNode<String>("child node 2.0.2"));
		
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.1"));
		rootNode.getChild(2).getChild(1).addChild(new TreeNode<String>("child node 2.1.0"));
		rootNode.getChild(2).getChild(1).addChild(new TreeNode<String>("child node 2.1.1"));
		rootNode.getChild(2).getChild(1).addChild(new TreeNode<String>("child node 2.1.2"));
		
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.2"));
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.3"));
		rootNode.getChild(2).getChild(3).addChild(new TreeNode<String>("child node 2.3.0"));
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.4"));
		rootNode.getChild(2).addChild(new TreeNode<String>("child node 2.5"));
		rootNode.getChild(2).getChild(5).addChild(new TreeNode<String>("child node 2.5.1"));
		rootNode.getChild(2).getChild(5).addChild(new TreeNode<String>("child node 2.5.2"));
		
		rootNode.getChild(3).addChild(new TreeNode<String>("child node 3.0"));
		rootNode.getChild(3).addChild(new TreeNode<String>("child node 3.1"));
		rootNode.getChild(3).addChild(new TreeNode<String>("child node 3.2"));
		rootNode.getChild(3).getChild(0).addChild(new TreeNode<String>("child node 3.0.0"));
		rootNode.getChild(3).getChild(0).addChild(new TreeNode<String>("child node 3.0.1"));
		rootNode.getChild(3).getChild(0).addChild(new TreeNode<String>("child node 3.0.2"));
		
		System.out.println("now the simple toString():");
		System.out.println(rootNode.toString());
		
		System.out.println("now the toStringAllFormatted(DEPTH_FIRST_MODE):");
		System.out.print(rootNode.toStringWalkFormatted(DEPTH_FIRST_MODE));
		
		System.out.println("now the toStringAll(DEPTH_FIRST_MODE):");
		System.out.println(rootNode.toStringWalk(DEPTH_FIRST_MODE));
		
		System.out.println("now the toStringAllFormatted(BREATH_FIRST_MODE):");
		System.out.print(rootNode.toStringWalkFormatted(BREATH_FIRST_MODE));
		
		System.out.println("now the toStringAll(BREATH_FIRST_MODE):");
		System.out.println(rootNode.toStringWalk(BREATH_FIRST_MODE));

	}

}
