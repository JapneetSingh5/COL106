// Class: Implementation of BST in A2
// Implement the following functions according to the specifications provided in Tree.java

public class BSTree extends Tree {

    private BSTree left, right;     // Children.
    private BSTree parent;          // Parent pointer.
        
    public BSTree(){  
        super();
        // This acts as a sentinel root node
        // How to identify a sentinel node: A node with parent == null is SENTINEL NODE
        // The actual tree starts from one of the child of the sentinel node!.
        // CONVENTION: Assume right child of the sentinel node holds the actual root! and left child will always be null.
    }    

    public BSTree(int address, int size, int key){
        super(address, size, key); 
    }

    public BSTree Insert(int address, int size, int key) 
    { 
        //Insert goes up to the root element from the currentNode and then traverses the tree
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=721
        //We move down levels in accordance with the BST ordering and Insert the new Node at the right location
        BSTree newNode = new BSTree(address, size, key);
        BSTree currentNode;
        BSTree parentNode;
        if(this.parent == null){
            //if this is the sentinel node, start from its right child i.e the tree's root element
            currentNode = this.right;
            parentNode = this;
        }else{
            //else, move upwards till we reach the root element
            currentNode = this;
            while(currentNode.parent.parent != null){
                currentNode = currentNode.parent;
            }
            parentNode = currentNode.parent;
        }
        while(currentNode!=null){
            //traverse down the tree to find the correct location to insert the node
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
                    //currentNode's address is less than newNode's address, move to right subtree
                    currentNode = currentNode.right;
                }else{
                    //currentNode's address is more than newNode's address, move to left subtree
                    currentNode = currentNode.left;
                }
            }
        }
        //Assignnment of pointers to insert the node in the tree
        newNode.parent = parentNode;
        if(parentNode.key<newNode.key){
            parentNode.right = newNode;
        }else if(parentNode.key>newNode.key){
            parentNode.left = newNode;
        }else if(parentNode.key==newNode.key){
            if(newNode.address>parentNode.address){
                parentNode.right = newNode;
            }else{
                parentNode.left = newNode;
            }
        }
        return newNode;
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
        //Case 4 requires us to delete the immediate successor of a node from the node's right subtree, we call Delete with fromSubtree set to true
        BSTree currentNode;
        if(this.parent==null){
            //if currentNode is a sentinel, begin searching from the root element i.e. right child of the sentinel
            currentNode = this.right;
        }else{
            currentNode = this;
            //if fromSubtree is true, we will start the search from the currentNode 
            //else move up to the right child of the sentinel i.e the root element
            if(!fromSubtree){
                while(currentNode.parent.parent != null){
                    currentNode = currentNode.parent;
                }  
            }
        }
        while(currentNode!=null){
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
                            return true;
                        }else{
                            currentNode.parent.left=null;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            return true;
                        }
                    }
                    else if(currentNode.left!=null && currentNode.right==null){
                        // System.out.println("Case 2 - right child is null, left child is present");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            //System.out.println("Subcase 1 - cN is the right child of its parent");
                            return true;
                        }else{
                            currentNode.parent.left=currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            return true;
                        }
                    }
                    else if(currentNode.left==null && currentNode.right!=null){
                        // System.out.println("Case 3 - left child is null, right child is present");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            //System.out.println("Subcase 1 - cN is the right child of its parent");
                            return true;
                        }else{
                            currentNode.parent.left=currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            //System.out.println("Subcase 2 - cN is the left child of its parent");
                            return true;
                        }
                    }
                    else{
                        // System.out.println("Case 4 - both right and left children present");
                        BSTree succ = currentNode.right;
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
                        BSTree newNode = new BSTree(tempAd, tempSize, tempKey);

                        newNode.parent = currentNode.parent;
                        newNode.left = currentNode.left;
                        newNode.right = currentNode.right;

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
        //if we reach here -> Deletion did not take place, hence we return false
        return false;
    }
        
    public BSTree Find(int key, boolean exact)
    { 
        //Find searches in the entire tree
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=729_f38
        BSTree currentNode;
        BSTree approxMatchWithLowestAdd = null;
        BSTree exactMatchWithLowestAdd = null;
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
        if(exact){  //for exact match i.e exact==true, we find the node which matches the key exactly
            while(currentNode != null){ 
                if(currentNode.key>key){  //if node's key is greater than query, we move to the left subtree
                    currentNode = currentNode.left;
                }else if(currentNode.key<key){ //if node's key is smaller than query, we move to the right subtree
                    currentNode = currentNode.right;
                }else if(currentNode.key==key){ 
                    exactMatchWithLowestAdd = currentNode; //as we have an exact match for the key, we store that pointer
                    //we move to the left subtree to see if we get a node with the same key but a smaller address, 
                    //here, smaller address than exactMatchWithLowestAdd can only lie in the left subtree due to the ordering
                    currentNode = currentNode.left;  
                }
            }
            return exactMatchWithLowestAdd;
        }else{
            while(currentNode != null){
                if(currentNode.key>key){
                    //we have a node with key greater than search query, we store its pointer in approxMatchWithLowestAdd
                    approxMatchWithLowestAdd = currentNode;
                    //we move onto the left subtree, as all the nodes will have smaller keys than approxMatchWithLowestAdd.key
                    //if the keys are the same, going left ensures that we will move onto the node with the smaller address
                    currentNode = currentNode.left;
                }else if(currentNode.key<key){
                    currentNode = currentNode.right;
                }else if(currentNode.key==key){
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

    public BSTree getFirst()
    { 
        //getFirst returns the smallest element appearing in the inorder traversal of the COMPLETE tree
        //ref https://piazza.com/class/kfjlgnjaoyp318?cid=721_f29
        BSTree currentNode;
        if(this.parent==null){
            //if this is the sentinel, then we start from its right child i.e. the root element
            currentNode=this.right;
        }else{
            //else, we move upwards to the root element
            currentNode=this;
            // while(currentNode.parent.parent != null){
            //     currentNode = currentNode.parent;
            // }
        }
        if(currentNode!=null){
            //keep going left from the root element to find the lowest (key, address) pair in the complete tree
            while(currentNode.left!=null){
                currentNode = currentNode.left;
            }
        }
        return currentNode;
    }

    public BSTree getNext()
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
                BSTree currentNode = this.right;
                if(currentNode!=null){
                    while(currentNode.left!=null){
                        currentNode = currentNode.left;
                    }
                }
                return currentNode;
            }else{
                //right subtree is null
                BSTree parentNode = this.parent;
                BSTree currentNode = this;
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
        //this helper function helps us use recursion to check for sanity of the complete tree
        //fromSubtree = tree restricts the function to checking the sanity of only the concerned subtrees
        //fromSubtree = false -> we go up to the root element and then start checking for the sanity of the complete tree irrespective of where sanity was called
        //In our implementation,. the root node is a sentinel node
        //The sentinel node has its parent as null
        //Sentinel node MUST not have a left child, right child may or may not be null
        if(this.parent==null){
            if(this.left!=null) return false;
        }
        BSTree currentNode;
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
    //     BSTree tree = new BSTree();
    //     tree.Insert(10,10,10);
    //     tree.Insert(20,20,20);
    //     tree.Insert(30,30,30);
    //     tree.Insert(40,40,40);
    //     tree.Insert(25,25,25);
    //     tree.Insert(35,35,35);
    //     tree.Insert(15,15,15);
    //     BSTree temp = new BSTree(10,10,10);
    //     tree.Delete(temp);
    //     BSTree temp2 = new BSTree(20,20,20);
    //     tree.Delete(temp2);
    //     BSTree dic;
    //     for(dic = (BSTree) tree.getFirst(); dic!=null; dic =dic.getNext()){
    //         System.out.println(dic.key);
    //     }
    //     if(dic==null){
    //         System.out.println("Inorder Traversal Finished");
    //     }
    // }

}


 


