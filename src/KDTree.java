import java.util.ArrayList;
import java.util.Random;

/**
 * @author Adam Kay
 * CS2420
 */


public class KDTree<T extends Comparable<? super T>>
{
	public TreeNode root = null;
	public int dimensions = 0;
	
    public class TreeNode {
        Crowd.Boid boid;
        TreeNode left, right = null;
        int split;
        
        TreeNode(Crowd.Boid _boid, TreeNode left, TreeNode right, int s) {
            this.left = left;
            this.right = right;
            boid = _boid;
            split = s;
        }
    }
    
    public KDTree(ArrayList<Crowd.Boid> boids, int d) {
    	dimensions = d;
    	
    	ArrayList<Crowd.Boid> xSortedBoids = QuickSortX(boids);
    	ArrayList<Crowd.Boid> ySortedBoids = QuickSortY(boids);
    	
    	ArrayList<Crowd.Boid> temp = new ArrayList<Crowd.Boid>(); 
    	root = buildTreeX (xSortedBoids, ySortedBoids, 0, xSortedBoids.size(), temp);
    	
    }
    
    public TreeNode buildTreeX (ArrayList<Crowd.Boid> xPts, ArrayList<Crowd.Boid> yPts, int start, int end, ArrayList<Crowd.Boid> temp) {
    	if (start > end) return null;
    	
    	int median = (start + end) / 2;
    	Crowd.Boid pivot = xPts.get(median);
    	int i = start;
    	int j = median + 1;
    	
    	for (int k = start; k <= end; k++) {
    		if (pivot == yPts.get(k))
    			temp.set(median, yPts.get(k));
    		else if (yPts.get(k).posx < pivot.posx)
    			temp.set(i++, yPts.get(k));
    		else
    			temp.set(j++, yPts.get(k));
    	}
    	
    	TreeNode node = new TreeNode(pivot, null, null, 0);
    	node.left = buildTreeY(xPts, yPts, start, median - 1, temp);
    	node.right = buildTreeY(xPts, yPts, median + 1, end, temp);
    	return node;
    }
    

	public TreeNode buildTreeY (ArrayList<Crowd.Boid> xPts, ArrayList<Crowd.Boid> yPts, int start, int end, ArrayList<Crowd.Boid> temp) {
		if (start > end) return null;
		
		int median = (start + end) / 2;
		Crowd.Boid pivot = yPts.get(median);
		int i = start;
		int j = median + 1;
		
		for (int k = start; k <= end; k++) {
			if (pivot == xPts.get(k))
				temp.set(median, xPts.get(k));
			else if (xPts.get(k).posy < pivot.posy)
				temp.set(i++, xPts.get(k));
			else
				temp.set(j++, xPts.get(k));
		}
		
		TreeNode node = new TreeNode(pivot, null, null, 1);
		node.left = buildTreeX(xPts, yPts, start, median - 1, temp);
		node.right = buildTreeX(xPts, yPts, median + 1, end, temp);
		return node;
	}

    public ArrayList<Crowd.Boid> findNears(TreeNode n, double r, Crowd.Boid x) {
    	if (n == null) return null;
    	
    	ArrayList<Crowd.Boid> list = new ArrayList<Crowd.Boid>(); 
    	
    	if (Math.pow(x.posx - n.boid.posx, 2) + Math.pow(x.posy - n.boid.posy, 2) < r*r) {
    		list.add(n.boid);
    	}
    	
    	if (n.split == 0) {
    		if (n.boid.posx - x.posx > r) {
    			list.addAll(findNears(n.left, r, x)); 
    		}
    		else if (n.boid.posx - x.posx < -r) {
    			list.addAll(findNears(n.right, r, x));
    		}
    		else {
    			list.addAll(findNears(n.left, r, x));
    			list.addAll(findNears(n.right, r, x));
    		}
    	}
    	else if (n.split == 1) {
    		if (n.boid.posy - x.posy > r) {
    			list.addAll(findNears(n.left, r, x)); 
    		}
    		else if (n.boid.posy - x.posy < -r) {
    			list.addAll(findNears(n.right, r, x));
    		}
    		else {
    			list.addAll(findNears(n.left, r, x));
    			list.addAll(findNears(n.right, r, x));
    		}
    	}
    	
    	return list;
    }
	
	
    public static ArrayList<Crowd.Boid> QuickSortX(ArrayList<Crowd.Boid> a) {
		if (a.size() == 1 || a.size() == 0) return a;
		
		ArrayList<Crowd.Boid> leftList = new ArrayList<Crowd.Boid>();
		ArrayList<Crowd.Boid> rightList = new ArrayList<Crowd.Boid>();
		
		Crowd.Boid first = a.get(0);
		Crowd.Boid last = a.get(a.size() - 1);
		Crowd.Boid middle = a.get(a.size() / 2);
		
		Crowd.Boid pivot = null;
		
		if ((first.posx >= last.posx && first.posx <= middle.posx) || (first.posx <= last.posx && first.posx >= middle.posx)) {
			pivot = first;
		}
		else if ((middle.posx >= first.posx && middle.posx <= last.posx) || (middle.posx <= first.posx && middle.posx >= last.posx)) {
			pivot = middle;
		}
		else if ((last.posx >= first.posx && last.posx <= middle.posx) || (last.posx <= first.posx && last.posx >= middle.posx)) {
			pivot = last;
		}
		
		for (int i = 0; i < a.size() - 1; i++) {
			if (a.get(i) == pivot) continue;
			if (a.get(i).posx < pivot.posx) {
				leftList.add(a.get(i));
			}
			else {
				rightList.add(a.get(i));
			}
		}
		
		QuickSortX(leftList);
		QuickSortX(rightList);
		
		for (int i = 0; i < leftList.size(); i++) {
			a.set(i, leftList.get(i));
		}
		
		a.set(leftList.size(), pivot);
		
		for (int i = 0; i < rightList.size(); i++) {
			a.set(leftList.size() + i + 1, rightList.get(i));
		}
		return a;
	}
    
    public static ArrayList<Crowd.Boid> QuickSortY(ArrayList<Crowd.Boid> a) {
		if (a.size() == 1 || a.size() == 0) return a;
		
		ArrayList<Crowd.Boid> leftList = new ArrayList<Crowd.Boid>();
		ArrayList<Crowd.Boid> rightList = new ArrayList<Crowd.Boid>();
		
		Crowd.Boid first = a.get(0);
		Crowd.Boid last = a.get(a.size() - 1);
		Crowd.Boid middle = a.get(a.size() / 2);
		
		Crowd.Boid pivot = null;
		
		if ((first.posy >= last.posy && first.posy <= middle.posy) || (first.posy <= last.posy && first.posy >= middle.posy)) {
			pivot = first;
		}
		else if ((middle.posy >= first.posy && middle.posy <= last.posy) || (middle.posy <= first.posy && middle.posy >= last.posy)) {
			pivot = middle;
		}
		else if ((last.posy >= first.posy && last.posy <= middle.posy) || (last.posy <= first.posy && last.posy >= middle.posy)) {
			pivot = last;
		}
		
		for (int i = 0; i < a.size() - 1; i++) {
			if (a.get(i) == pivot) continue;
			if (a.get(i).posy < pivot.posy) {
				leftList.add(a.get(i));
			}
			else {
				rightList.add(a.get(i));
			}
		}
		
		QuickSortX(leftList);
		QuickSortX(rightList);
		
		for (int i = 0; i < leftList.size(); i++) {
			a.set(i, leftList.get(i));
		}
		
		a.set(leftList.size(), pivot);
		
		for (int i = 0; i < rightList.size(); i++) {
			a.set(leftList.size() + i + 1, rightList.get(i));
		}
		
		return a;
	}
    
    public int size() {
        return size(root);
    }
    

    /**
     * Returns the size of the tree
     * @param n
     * @return
     */
    private int size(TreeNode n) {
        if (n == null) return 0;
        return 1 + size(n.left) + size(n.right);
    }
    
    
    public void preorder() {
        System.out.println("preorder: ");
        preorder(root);
        System.out.println(" ");
    }
    
    private void preorder(TreeNode n) {
        if (n == null) {
        	System.out.println("Empty tree!");
        	return;
        }
        
        Code stacks = new Code();
        Code.ListStack<TreeNode> stack = stacks.new ListStack<TreeNode>();
        
        stack.push(n);
        
        while (!stack.isEmpty()) {
        	TreeNode poppedNode = stack.pop();
        	if (poppedNode == null) continue;
        	
        	stack.push(poppedNode.right);
        	stack.push(poppedNode.left);
        }
    }
    
  
    
    public void levelorder() {
    	System.out.println("levelorder: ");
        levelorder(root);
        System.out.println(" ");
    }
    
    private void levelorder(TreeNode n) {
    	 if (n == null) return;
         
         Code queues = new Code();
         Code.ListQueue<TreeNode> queue = queues.new ListQueue<TreeNode>();
         
         queue.enqueue(n);
         
         while (!queue.isEmpty()) {
         	TreeNode node = queue.dequeue();
         	if (node == null) continue;
         	queue.enqueue(node.left);
         	queue.enqueue(node.right);
         }
    }
    
}