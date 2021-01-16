import java.util.Scanner;
public class Driver{
    static public String printList(Dictionary myList){
        StringBuilder sb = new StringBuilder();
        while(myList!=null){
            String temp = myList.key+"_"+myList.address+"_"+myList.size;
            sb.append(temp);
            myList = myList.getNext();
        }
        return sb.toString();
    }
    public static void main(String args[]){
        int numTestCases;
        Scanner sc = new Scanner(System.in);
        numTestCases = sc.nextInt();
        while(numTestCases-->0){
            int size;
            size = sc.nextInt();
            A1DynamicMem obj = new A1DynamicMem(size);
            while(sc.hasNext()) {
                String command;
                command = sc.next();
                int argument;
                argument = sc.nextInt();
                int result = -5;
                switch (command) {
                    case "Allocate":
                        result = obj.Allocate(argument);
                        break;
                    case "Free":
                        result = obj.Free(argument);
                        break;
                    case "Defragment":
                        obj.Defragment();
                        break;
                    default:
                        break;
                }
                System.out.println(result);
                System.out.println(printList(obj.freeBlk));
                System.out.println(printList(obj.allocBlk));
            }
            
        }
    }
}