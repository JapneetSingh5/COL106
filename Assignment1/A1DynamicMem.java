// Class: A1DynamicMem
// Implements DynamicMem
// Does not implement defragment (which is for A2).

public class A1DynamicMem extends DynamicMem {
      
    public A1DynamicMem() {
        super();
    }

    public A1DynamicMem(int size) {
        super(size);
    }

    public A1DynamicMem(int size, int dict_type) {
        super(size, dict_type);
    }

    public void Defragment() {
        return ;
    }

    // In A1, you need to implement the Allocate and Free functions for the class A1DynamicMem
    // Test your memory allocator thoroughly using Doubly Linked lists only (A1List.java).

    public int Allocate(int blockSize) {
        // System.out.println("Allocating");
        // if(!freeBlk.sanity() || !allocBlk.sanity()){
        //     return -1;
        // }
        if(blockSize>this.Memory.length){
            return -1;
        }
        Dictionary toBeAllocated = freeBlk.Find(blockSize, false);
        if(toBeAllocated==null){
            return -1;
        }
        else if(toBeAllocated.size==blockSize){
            allocBlk.Insert(toBeAllocated.address, toBeAllocated.size, toBeAllocated.address);
            freeBlk.Delete(toBeAllocated);
            return toBeAllocated.address;
        }else{
            freeBlk.Insert(toBeAllocated.address+blockSize, toBeAllocated.size - blockSize, toBeAllocated.size - blockSize);
            allocBlk.Insert(toBeAllocated.address, blockSize, toBeAllocated.address);
            freeBlk.Delete(toBeAllocated);
            return toBeAllocated.address;
        }
    } 
    
    public int Free(int startAddr) {
        // System.out.println("Freeing");
        Dictionary toBeFreed = allocBlk.Find(startAddr, true);
        if(toBeFreed == null){
            // System.out.println("Freeing");
            return -1;
        }else{
            freeBlk.Insert(toBeFreed.address, toBeFreed.size, toBeFreed.key);
            allocBlk.Delete(toBeFreed);
            return 0;
        }
    }

    public static void main(String[] args){
        DynamicMem mem = new A1DynamicMem(20);
        System.out.println("");
        mem.Allocate(10);
        System.out.println("Free");
        for(A1List dic = (A1List) mem.freeBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
        System.out.println("Allocated");
        for(A1List dic = (A1List) mem.allocBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
        System.out.println("");
        mem.Allocate(20);
        System.out.println("Free");
        for(A1List dic = (A1List) mem.freeBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
        System.out.println("Allocated");
        for(A1List dic = (A1List) mem.allocBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
        System.out.println("");
        mem.Free(50);
        System.out.println("Free");
        for(A1List dic = (A1List) mem.freeBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
        System.out.println("Allocated");
        for(A1List dic = (A1List) mem.allocBlk.getFirst(); dic!=null; dic =dic.getNext()){
            System.out.println(dic.address + " -> "+ (dic.size+dic.address));
        }
    }
    
}