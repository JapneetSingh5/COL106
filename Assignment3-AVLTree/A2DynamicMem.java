// Class: A2DynamicMem
// Implements Degragment in A2. No other changes should be needed for other functions.

public class A2DynamicMem extends A1DynamicMem {
      
    public A2DynamicMem() {  super(); }

    public A2DynamicMem(int size) { super(size); }

    public A2DynamicMem(int size, int dict_type) { super(size, dict_type); }

    // In A2, you need to test your implementation using BSTrees and AVLTrees. 
    // No changes should be required in the A1DynamicMem functions. 
    // They should work seamlessly with the newly supplied implementation of BSTrees and AVLTrees
    // For A2, implement the Defragment function for the class A2DynamicMem and test using BSTrees and AVLTrees. 

    public void Defragment() {
        BSTree freeBlkByAddress = new BSTree();
        BSTree currentNode = (BSTree) this.freeBlk.getFirst();
        while(currentNode != null){
            freeBlkByAddress.Insert(currentNode.address, currentNode.size, currentNode.address);
            currentNode = currentNode.getNext();
        }
        currentNode = freeBlkByAddress.getFirst();
        BSTree nextNode = currentNode.getNext();
        while(nextNode!=null){
            if(currentNode.key+currentNode.size==nextNode.key){
                BSTree nodeTBD1 = new BSTree(currentNode.address, currentNode.size, currentNode.size);
                BSTree nodeTBD2 = new BSTree(nextNode.address, nextNode.size, nextNode.size);
                this.freeBlk.Delete(nodeTBD1);
                this.freeBlk.Delete(nodeTBD2);
                BSTree newNodeFreeBySize = (BSTree) this.freeBlk.Insert(currentNode.address, currentNode.size+nextNode.size, currentNode.size+nextNode.size);

                freeBlkByAddress.Delete(currentNode);
                freeBlkByAddress.Delete(nextNode);
                BSTree newNodeFreeByAddress = freeBlkByAddress.Insert(newNodeFreeBySize.address, newNodeFreeBySize.size, newNodeFreeBySize.address);

                currentNode = newNodeFreeByAddress;
                nextNode = newNodeFreeByAddress.getNext();
            }else{
                currentNode = currentNode.getNext();
                nextNode = nextNode.getNext();
            }
        }

        freeBlkByAddress = null;

        return ;
    }
}