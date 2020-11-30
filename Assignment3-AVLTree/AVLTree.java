// Class: Height balanced AVL Tree
// Binary Search Tree

public class AVLTree extends BSTree {
    
    private AVLTree left, right;     // Children. 
    private AVLTree parent;          // Parent pointer. 
    private int height;  // The height of the subtree
        
    public AVLTree() { 
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node !.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
        
    }

    public AVLTree(int address, int size, int key) { 
        super(address, size, key);
        this.height = 0;
    }

    // Implement the following functions for AVL Trees.
    // You need not implement all the functions. 
    // Some of the functions may be directly inherited from the BSTree class and nothing needs to be done for those.
    // Remove the functions, to not override the inherited functions.
    
    public AVLTree Insert(int address, int size, int key) 
    { 
        AVLTree newNode = new AVLTree(address, size, key);
        newNode.height = 1;
        AVLTree currentNode;
        AVLTree parentNode;

        if(this.parent == null){
            //if this is the sentinel node, start from the right child of the sentinel
            currentNode = this.right;
            parentNode = this;
        }else{
            //else, move up to the rool element i.e the right child of the sentinel node
            currentNode = this;
            while(currentNode.parent.parent != null){
                currentNode = currentNode.parent;
            }
            parentNode = this.parent;
        }
        while(currentNode!=null){
            //traverse the tree to find the correct location where the new node should be put in,
            //we dont consider the balance while moving down, we just find the right place and insert like in BSTree
            parentNode = currentNode;
            if(currentNode.key<newNode.key){
                //currentNode's key is less than newNode's , move to the right subtree
                currentNode = currentNode.right;
            }else if(currentNode.key>newNode.key){
                //currentNode's key is more than newNode's , move to the left subtree
                currentNode = currentNode.left;
            }else if(currentNode.key==newNode.key){
                //if currentNode's key is the same as that of newNode, break the tie using tha address
                //we assume (key, address) pairs will always be distinct and make a total ordering for the nodes
                if(newNode.address>currentNode.address){
                    //currentNode's address is less than the newNode's address, move to right subtree
                    currentNode = currentNode.right;
                }else{
                    //currentNode's address is greater than the newNode's address, move to left subtree
                    currentNode = currentNode.left;
                }
            }

        }
        //Assignment of pointers to insert the newNode into the tree at the found correct position
        newNode.parent = parentNode;
        if(parentNode.key<newNode.key){
            //newNode will be right child of parentNode
            parentNode.right = newNode;
        }else if(parentNode.key>newNode.key){
            //newNode will be left child of parentNode
            parentNode.left = newNode;
        }else if(parentNode.key==newNode.key){
            if(newNode.address>parentNode.address){
                parentNode.right = newNode;
            }else{
                parentNode.left = newNode;
            }
        }

        currentNode = newNode;
        //We move up from the newNode to its parent and grandparent and so on
        //only these nodes will have a change in their heights, all other subtrees and nodes will remain unaffected

        while(currentNode.parent.parent!=null){
            currentNode = currentNode.parent;
            currentNode.setHeight();
            if(Math.abs(currentNode.deltaHeight())>1){
                // If imbalance is found, balance the subtree
                AVLTree temp = currentNode.parent;
                if(currentNode.parent.parent==null){
                    temp.right =  currentNode.balanceSubtree();
                    currentNode = temp.right;
                }else{
                    if(currentNode.parent.right==currentNode){
                        temp.right = currentNode.balanceSubtree();
                        currentNode = temp.right;
                    }else{
                        temp.left = currentNode.balanceSubtree();
                        currentNode = temp.left;
                    }   
                }
            }  
        }

        return newNode;
    }

    private int setHeight(){
        //this private fuction sets the height of a node
        //considers corner cases where some child of the node may be null and so on 
        if(this.right==null && this.left==null){
            this.height = 1;
            return 1;
        }
        else if(this.right!=null && this.left==null){
            this.height = this.right.height + 1;
            return this.height;
        }
        else if(this.right==null && this.left!=null){
            this.height = this.left.height + 1;
            return this.height;
        }
        else{
            this.height = 1 + Math.max(this.right.height,this.left.height);
            return this.height;
        }
    }

    private int getHeight(AVLTree node){
        //returns height of the node if not null else 0
        if(node==null){
            return 0;
        }else{
            return node.height;
        }
    }

    private int deltaHeight(){
        //returns left.height - right.height for a node
        //accomodates cases where either ot both children are null for an easier abstraction
        if(this.right==null && this.left==null){
            return 0;
        }
        else if(this.right!=null && this.left==null){
            return -this.right.height;
        }
        else if(this.right==null && this.left!=null){
            return this.left.height;
        }
        else{
            return (this.left.height - this.right.height);
        }
    }

    private AVLTree balanceSubtree(){
        //detects the kind of imbalance present and fixes it using the corresponding algorithm
        if(this.deltaHeight()>1){
            if(getHeight(this.left.left)>getHeight(this.left.right)){
                 return this.rightRotate();
            }else{
                return this.leftRightRotate();
            }
        }else if(this.deltaHeight()<-1){
            if(getHeight(this.right.right)>getHeight(this.right.left)){
                return this.leftRotate();
            }else{
                return this.rightLeftRotate();
            }
        }
        return this;
    }

    private AVLTree rightRotate(){
        AVLTree temp = this.left;
        if(temp!=null) this.left = temp.right;
        if(temp!=null && temp.right!=null) this.left.parent = this;
        if(temp!=null) temp.parent = this.parent;

        if(temp!=null) temp.right = this;
        this.parent = temp;

        this.setHeight();
        temp.setHeight();
        return temp;

    }

    private AVLTree leftRotate(){
        AVLTree temp = this.right;
        if(temp!=null) this.right = temp.left;
        if(temp!=null && temp.left!=null) this.right.parent = this;
        if(temp!=null) temp.parent = this.parent;

        if(temp!=null) temp.left = this;
        this.parent = temp;

        this.setHeight();
        temp.setHeight();
        return temp;
    }

    private AVLTree leftRightRotate(){
        this.left = this.left.leftRotate();
        return this.rightRotate();
    }

    private AVLTree rightLeftRotate(){
        this.right = this.right.rightRotate();
        return this.leftRotate();
    }

    public boolean Delete(Dictionary e)
    {
        //By default, Delete searches the entire tree for the match to be deleted
        //We use a helper private function Delete(Dictionary, Boolean) to also allow for Deletion in subtree by overloading the same codeblock
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=721
        return Delete(e, false);
    }

    private boolean Delete(Dictionary e, boolean fromSubtree)
    {   
        //Deletion searches the entire tree for an exact match of Dictionary e if boolean fromSubtree is false
        //by default, fromSubtree will remain false, unless we hit Case 4 of delete
        //Deletion searches the entire tree for an exact match of Dictionary e
        AVLTree currentNode;
        boolean flag = false;
        if(this.parent==null){
            //if currentNode is a sentinel, begin searching from the root element i.e. right child of the sentinel
            currentNode = this.right;
        }else{
            currentNode = this;
            //move up to the right child of the sentinel i.e the root element
            if(!fromSubtree){
                while(currentNode.parent.parent != null){
                    currentNode = currentNode.parent;
            }  
            }
        }
        while(currentNode!=null && !flag){
            if(currentNode.key<e.key){
                //query's key is greater than the current node's key, move to the right subtree
                currentNode=currentNode.right;
            }else if(currentNode.key>e.key){
                //query's key is less than the current node's key, move to the left subtree
                currentNode=currentNode.left;
            }else{
                if(currentNode.address == e.address){
                    // Both key and address are an exact match, we must delete this node
                    if(currentNode.left==null && currentNode.right==null){
                        // System.out.println("Case 1 - cN is a leaf, both children are null");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = null;
                            //System.out.println("Subcase 1 - cN is the right child of its parent");
                            flag = true;
                        }else{
                            currentNode.parent.left=null;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            flag = true;
                        }
                        currentNode = currentNode.parent;
                        currentNode.setHeight();
                    }
                    else if(currentNode.left!=null && currentNode.right==null){
                        // System.out.println("Case 2 - right child is null, left child is present");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            //System.out.println("Subcase 1 - cN is the right child of its parent");
                            flag = true;
                        }else{
                            currentNode.parent.left=currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            flag = true;
                        }
                        currentNode = currentNode.left;
                        currentNode.setHeight();
                    }
                    else if (currentNode.left==null && currentNode.right!=null){
                        // System.out.println("Case 3 - left child is null, right child is present");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            //System.out.println("Subcase 1 - cN is the right child of its parent");
                            flag = true;
                        }else{
                            currentNode.parent.left=currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            flag = true;
                        }
                        currentNode = currentNode.right;
                        currentNode.setHeight();
                    }
                    else{
                        // System.out.println("Case 4 - both right and left children present");
                        AVLTree succ = currentNode.right;
                        if(succ!=null){
                            //keep going left from the currentNode to find the lowest (key, address) pair in its right subtree i.e its successor
                            while(succ.left!=null){
                                succ = succ.left;
                            }
                        }
                        int tempAd = succ.address;
                        int tempSize = succ.size;
                        int tempKey = succ.key;
                        //We copy the successor node and replace the currentNode by it
                        AVLTree newNode = new AVLTree(tempAd, tempSize, tempKey);

                        newNode.parent = currentNode.parent;
                        newNode.left = currentNode.left;
                        newNode.right = currentNode.right;
                        newNode.height = currentNode.height;

                        currentNode.left.parent = newNode;
                        currentNode.right.parent = newNode;

                        if(currentNode.parent.right==currentNode){
                            newNode.parent.right=newNode;
                        }else{
                            newNode.parent.left=newNode;
                        }

                        currentNode = newNode;
                        //Delete the successor from the right subtree of the currentNode
                        //effectively, we have deleted the currentNode as currentNode now holds the succ's values
                        //We recursively call Delete on the right subtree, with fromSubtree as true
                        currentNode.right.Delete(succ, true);
                        return true;
                        
                        //Failed implementation, commented out below
                        // currentNode.address = succ.address;
                        // currentNode.size = succ.size;
                        // currentNode.key = succ.key;
                        // currentNode.right.Delete(succ);
                    }
                }else if(currentNode.address<e.address){
                    //Key matches but address of currentNode is less than address of query
                    //Move to the right subtree to find the exact match in accordance with the ordering
                    currentNode=currentNode.right;
                }else{
                    //Key matches but address of currentNode is greater than address of query
                    //Move to the left subtree to find the exact match in accordance with the ordering
                    currentNode=currentNode.left;
                }
            }
        }

        //Moves up from the node which has replaced the deleted node and fixes imbalances wherever present
        //Only parent to parent move up works as explained in Insert()
        //flag is true whenever a deletion takes place
        if(flag){
            while(currentNode.parent!=null && currentNode.parent.parent!=null){
                currentNode = currentNode.parent;
                // System.out.println("At " + currentNode.key);
                currentNode.setHeight();
                if(Math.abs(currentNode.deltaHeight())>1){
                    // System.out.println("Imbalance at " + currentNode.key);
                    AVLTree temp = currentNode.parent;
                    if(currentNode.parent.parent==null){
                        temp.right =  currentNode.balanceSubtree();
                        currentNode = temp.right;
                    }else{
                        if(currentNode.parent.right==currentNode){
                            temp.right = currentNode.balanceSubtree();
                            currentNode = temp.right;
                        }else{
                            temp.left = currentNode.balanceSubtree();
                            currentNode = temp.left;
                        }   
                    }
                }  
            }
        }
        return flag;
    }
        
    public AVLTree Find(int k, boolean exact)
    { 
        //Find searches in the entire tree
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=729_f38
        AVLTree currentNode;
        AVLTree approxMatchWithLowestAdd = null;
        AVLTree exactMatchWithLowestAdd = null;
        // System.out.println("Finding "+k);
        if(this.parent == null){
            //if called on the sentinel, we start finding from the right child of the sentinel i.e. the root element of the tree
            currentNode = this.right;
        }else{
            //if called on any other element in the tree, we go up to the root of the tree and then start our search
            currentNode = this;
            while(currentNode.parent.parent != null){
                currentNode = currentNode.parent;
            }
        }
        if(exact){ 
             //for exact match i.e exact==true, we find the node which matches the key exactly
            // System.out.println("Finding exact match");
            while(currentNode != null){ 
                // System.out.println("At " + currentNode.key);
                if(currentNode.key>k){  //if node's key is greater than query, we move to the left subtree
                    currentNode = currentNode.left;
                }else if(currentNode.key<k){ //if node's key is smaller than query, we move to the right subtree
                    currentNode = currentNode.right;
                }else if(currentNode.key==k){ 
                    // System.out.println("Match " + currentNode.key);
                    exactMatchWithLowestAdd = currentNode; //as we have an exact match for the key, we store that pointer
                    //we move to the left subtree to see if we get a node with the same key but a smaller address, 
                    //here, smaller address than exactMatchWithLowestAdd can only lie in the left subtree due to the ordering
                    currentNode = currentNode.left;  
                }
            }
            return exactMatchWithLowestAdd;
        }else{
            // System.out.println("Finding approx match");
            while(currentNode != null){
                // System.out.println("At " + currentNode.key);
                if(currentNode.key>k){
                    //we have a node with key greater than search query, we store its pointer in approxMatchWithLowestAdd
                    approxMatchWithLowestAdd = currentNode;
                    //we move onto the left subtree, as all the nodes will have smaller keys than approxMatchWithLowestAdd.key
                    //if the keys are the same, going left ensures that we will move onto the node with the smaller address
                    currentNode = currentNode.left;
                }else if(currentNode.key<k){
                    currentNode = currentNode.right;
                }else if(currentNode.key==k){
                    // System.out.println("Match " + currentNode.key);
                    approxMatchWithLowestAdd = currentNode;
                    //just like in exact = true, we move to the left subtree to see if we get a node with the same key but a smaller address, 
                    //here, smaller address than approxMatchWithLowestAdd can only lie in the left subtree due to the ordering
                    currentNode=currentNode.left;
                }
            }
            if(approxMatchWithLowestAdd == null){
                return null;
            }else{
                return approxMatchWithLowestAdd;
            }
        }
    }

    public AVLTree getFirst() 
    {                            
        //getFirst returns the smallest element appearing in the inorder traversal of the COMPLETE tree
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=721_f29
        AVLTree currentNode;
        if(this.parent==null){
            //if this is the sentinel, then we start from its right child i.e. the root element
            currentNode=this.right;
        }else{
            //else, we move upwards to the root element
            currentNode=this;
        }
        if(currentNode!=null){
            //keep going left from the root element to find the lowest (key, address) pair in the complete tree
            while(currentNode.left!=null){
                currentNode = currentNode.left;
            }
        }
        return currentNode;
    }

    public AVLTree getNext() 
    {                            
        //getNext can be called from anywhere - returns next node in inorder traversal
        //should return null if called on the sentinel node
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=729
        if(this.parent==null){
            //this.is the sentinel node
            return null;
        }else{
            if(this.right==null && this.parent.parent==null){
                //no node succeeds this one
                //right subtree is null 
                //the node's (key, address) is greater than its parent and its parent is the root elemennt
                return null;
            }
            else if(this.right!=null){
                //right subtree is not null
                //return the smallest (key, adress) {leftmost node} pair node in the right subtree
                AVLTree currentNode = this.right;
                if(currentNode!=null){
                    while(currentNode.left!=null){
                        currentNode = currentNode.left;
                    }
                }
                return currentNode;
            }else{
                // System.out.println("Returing parent as right subtree is empty");
                //right subtree is null
                AVLTree parentNode = this.parent;
                AVLTree currentNode = this;
                //while the currentNode is the right child of its parent, keep going up
                while(parentNode.parent!=null && currentNode==parentNode.right){
                    currentNode = parentNode;
                    parentNode = parentNode.parent;
                }
                if(parentNode.parent==null){
                    return null;
                }
                return parentNode;
            }
        }
    }

    public boolean sanity(){
        //by default, we check sanity for the complete tree
        return this.sanity(false);
    }

    private boolean sanity(boolean fromSubtree)
    {   
        //sanity check for AVLTree is similar to BST with an additional constraint of deltaHeight for every node being less than or equal to 1 in magnitude

        //this helper function helps us use recursion to check for sanity of the complete tree
        //fromSubtree = tree restricts the function to checking the sanity of only the concerned subtrees
        //fromSubtree = false -> we go up to the root element and then start checking for the sanity of the complete tree irrespective of where sanity was called
        //In our implementation,. the root node is a sentinel node
        //The sentinel node has its parent as null
        //Sentinel node MUST not have a left child, right child may or may not be null
        if(this.parent==null){
            if(this.left!=null) return false;
        }
        AVLTree currentNode;
        if(this.parent==null){
            currentNode = this.right;
        }else{
            currentNode = this;
            if(!fromSubtree){
                while(currentNode.parent.parent != null){
                    currentNode = currentNode.parent;
                }
            }
        }

        if(currentNode==null){
            return true;
        }

        //Now, We check for BST Ordering Invariant
        //In this case, (address, size, key) tuple determines the order
        //for Node1<Node2, Node1.key<Node2.key OR ( Node1.key==Node2.key AND Node1.address<Node2.address) 
        //as addresses will ideally be unique, we will have total ordering in the tree

        //Node.key must be less than or equal to Node.right's key
        if(currentNode.right!=null && currentNode.key>currentNode.right.key){
            //If Node.right.key<Node.key, BST Invariant does not hold
            return false;
        }
        if(currentNode.right!=null && currentNode.key==currentNode.right.key){
            //If Node.key is equal to Node.right.key, we break the tie using the address
            //Node.right.address must be strictly less than Node's address
            if(currentNode.right.address<currentNode.address){
                return false;
            }
        }
        //Similarly, Node.key must be greater than OR equal to Node.left.key
        if(currentNode.left!=null && currentNode.key<currentNode.left.key){
            //If Node.left.key>Node.key, BST Invariant is not satisfied
            return false;
        }
        if(currentNode.left!=null && currentNode.key==currentNode.left.key){
            //If Node.key==Node.left.key, we break the tie using the addresses
            //Node.left.address must strictly be less than Node.address
            if(currentNode.left.address>currentNode.address){
                return false;
            }  
        }

        //Post checking for BST invariant at the node, we check for AVLTree property
        if(Math.abs(currentNode.deltaHeight())>1){
            //AVL Tree Height restriction does not hold
            return false;
        }

        if(currentNode.left==null){
            if(currentNode.right==null){
                return true;
            }else{
                return currentNode.right.sanity(true);
            }
        }else{
            if(currentNode.right==null){
                return currentNode.left.sanity(true);
            }else{
                return (currentNode.left.sanity(true)&&currentNode.right.sanity(true));
            }
        }
    }

    // main function used to test and debug the code >>>

    // public static void main(String[] args){
    //     AVLTree tree = new AVLTree();
    //     tree.Insert(0,0,0);
    //     tree.Insert(1,1,1);
    //     tree.Insert(10,10,10);
    //     tree.Insert(17,17,17);
    //     // tree.Insert(26,26,26);
    //     AVLTree dic;
    //     // tree.Defragment();
    //     for(dic = (AVLTree) tree.getFirst(); dic!=null; dic = (AVLTree) dic.getNext()){
    //         System.out.println(dic.key + " Height: " + dic.height);
    //     }
    //     // if(dic==null){
    //     //     System.out.println("In Traversal Finished");
    //     // }
    // }
}


