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
        BSTree newNode = new BSTree(address, size, key);
        BSTree currentNode;
        BSTree parentNode;
        if(this.parent == null){
            currentNode = this.right;
            parentNode = this;
        }else{
            currentNode = this;
            parentNode = this.parent;
        }
        while(currentNode!=null){
            parentNode = currentNode;
            if(currentNode.key<newNode.key){
                currentNode = currentNode.right;
            }else if(currentNode.key>newNode.key){
                currentNode = currentNode.left;
            }else if(currentNode.key==newNode.key){
                if(newNode.address>currentNode.address){
                    currentNode = currentNode.right;
                }else{
                    currentNode = currentNode.left;
                }
            }
        }
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
        BSTree currentNode;
        if(this.parent==null){
            currentNode = this.right;
        }else{
            currentNode = this;
        }
        while(currentNode!=null){
            if(currentNode.key<e.key){
                currentNode=currentNode.right;
            }else if(currentNode.key>e.key){
                currentNode=currentNode.left;
            }else{
                if(currentNode.address == e.address){
                    // Exact match, delete this node
                    System.out.println("Match found, deletion started");

                    if(currentNode.left==null && currentNode.right==null){
                        System.out.println("Case 1");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = null;
                            return true;
                        }else{
                            currentNode.parent.left=null;
                            System.out.println("Done");
                            return true;
                        }
                    }
                    else if(currentNode.left!=null && currentNode.right==null){
                        System.out.println("Case 2");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            System.out.println("Done");
                            return true;
                        }else{
                            currentNode.parent.left=currentNode.left;
                            currentNode.left.parent = currentNode.parent;
                            System.out.println("Done");
                            return true;
                        }
                    }
                    else if(currentNode.left==null && currentNode.right!=null){
                        System.out.println("Case 3");
                        if(currentNode.parent.right==currentNode){
                            currentNode.parent.right = currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            System.out.println("Done sub 1");
                            return true;
                        }else{
                            currentNode.parent.left=currentNode.right;
                            currentNode.right.parent = currentNode.parent;
                            System.out.println("Done sub 2");
                            return true;
                        }
                    }
                    else{
                        System.out.println("Case 4");
                        BSTree succ = currentNode.right.getFirst();
                        currentNode.address = succ.address;
                        currentNode.size = succ.size;
                        currentNode.key = succ.key;
                        currentNode.right.Delete(succ);
                    }
                }else if(currentNode.address<e.address){
                    currentNode=currentNode.right;
                }else{
                    currentNode=currentNode.left;
                }
            }
        }
        return false;
    }
        
    public BSTree Find(int key, boolean exact)
    { 
        BSTree currentNode;
        BSTree approxMatch = null;
        if(this.parent == null){
            currentNode = this.right;
        }else{
            currentNode = this;
        }
        if(exact){
            while(currentNode != null){
                if(currentNode.key>key){
                    currentNode = currentNode.left;
                }else if(currentNode.key<key){
                    currentNode = currentNode.right;
                }else if(currentNode.key==key){
                    return currentNode;
                }
            }
            return null;
        }else{
            while(currentNode != null){
                if(currentNode.key>key){
                    approxMatch = currentNode;
                    currentNode = currentNode.left;
                }else if(currentNode.key<key){
                    currentNode = currentNode.right;
                }else if(currentNode.key==key){
                    return currentNode;
                }
            }
            if(approxMatch == null){
                return null;
            }else{
                return approxMatch;
            }
        }
    }

    public BSTree getFirst()
    { 
        BSTree currentNode;
        if(this.parent==null){
            currentNode=this.right;
        }else{
            currentNode=this;
        }
        if(currentNode!=null){
            while(currentNode.left!=null){
                currentNode = currentNode.left;
            }
        }
        return currentNode;
    }

    public BSTree getNext()
    { 
        if(this.parent==null){
            return this.right.getFirst();
        }else{
            if(this.right==null && this.parent.parent==null){
                return null;
            }
            else if(this.right!=null){
                return this.right.getFirst();
            }else{
                BSTree parentNode = this.parent;
                BSTree currentNode = this;
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

    public boolean sanity()
    { 
        return true;
    }

    public static void main(String[] args){
        BSTree tree = new BSTree();
        tree.Insert(10,10,10);
        tree.Insert(20,20,20);
        tree.Insert(30,30,30);
        tree.Insert(40,40,40);
        tree.Insert(25,25,25);
        tree.Insert(35,35,35);
        tree.Insert(15,15,15);
        BSTree temp = new BSTree(10,10,10);
        tree.Delete(temp);
        BSTree temp2 = new BSTree(20,20,20);
        tree.Delete(temp2);
        BSTree dic;
        for(dic = (BSTree) tree.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.key);
        }
        if(dic==null){
            System.out.println("In Traversal Finished");
        }
    }

}


 


