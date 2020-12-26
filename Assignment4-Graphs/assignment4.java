import java.io.*;
import java.util.Scanner;
import java.io.FileReader; 
import java.io.FileNotFoundException;
import java.io.IOException;

public class assignment4 {
    public static void main(String[] args){
        String nodesFile = args[0];
        String edgesFile = args[1];
        String function = args[2];
        switch(function){
            case "average":
                System.out.println("Average function chosen");
                try{
                    Scanner sc = new Scanner(new File("nodes.csv"));  
                    sc.useDelimiter(",");   
                    while (sc.hasNext())  
                    {  
                    System.out.print(sc.next());  
                    }   
                    sc.close();  
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "rank":
                System.out.println("Rank function chosen");
                break;
            case "independent_storylines_dfs":
                System.out.println("Independent Storylines function chosen");
                break;
            default:
                System.out.println("Unknown function chosen, please check your input");
                break;
        }
    }
}