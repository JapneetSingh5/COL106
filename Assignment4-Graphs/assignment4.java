import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;

public class assignment4 {

    public static class A4Graph{
        HashMap<String, LinkedList> adjMap;
        LinkedList<String> nodes;

        public A4Graph(){
            this.adjMap = new HashMap<String, LinkedList>();
            this.nodes = new LinkedList<String>();
        }
    }
    public static void main(String[] args){
        A4Graph graph = new A4Graph();
        String nodesFile = args[0];
        String edgesFile = args[1];
        String function = args[2];
        BufferedReader br = null;
        switch(function){
            case "average":
                // System.out.println("Average function chosen");
                try{
                    br = new BufferedReader(new FileReader(edgesFile));  
                    String line;
                    line = br.readLine();
                    //Discarding header line
                    while ((line = br.readLine()) != null) {
                        String[] edgeEntry = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        if(graph.adjMap.containsKey(edgeEntry[0])){
                            graph.adjMap.get(edgeEntry[0]).addLast(edgeEntry[1]);
                        }else{
                            graph.adjMap.put(edgeEntry[0], new LinkedList<String>());
                            graph.adjMap.get(edgeEntry[0]).add(edgeEntry[1]);
                        }
                        if(graph.adjMap.containsKey(edgeEntry[1])){
                            graph.adjMap.get(edgeEntry[1]).addLast(edgeEntry[0]);
                        }else{
                            graph.adjMap.put(edgeEntry[1], new LinkedList<String>());
                            graph.adjMap.get(edgeEntry[1]).add(edgeEntry[0]);
                        }
                    }
                    float totalDegree = 0;
                    for (String key : graph.adjMap.keySet()) {
                        totalDegree += graph.adjMap.get(key).size();
                    }
                    float totalKeys = graph.adjMap.keySet().size();
                    System.out.println(totalDegree +" , " +totalKeys);
                    float averageFloat = totalDegree/totalKeys;
                    String average = String.format(java.util.Locale.US,"%.2f", averageFloat);
                    System.out.println(average);
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // System.out.println(graph.adjMap.get("Triton").get(0));
                // System.out.println(graph.adjMap.get("Black Panther / T'chal").get(7));
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