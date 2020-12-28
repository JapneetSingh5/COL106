import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

public class assignment4 {

    public static int randomizedPartition(ArrayList<A4Vertex> arr, int p, int r){
        int i = (int)(p+Math.random()*(r-p+1));
        A4Vertex temp = arr.get(i);
        arr.set(i, arr.get(r));
        arr.set(r, temp);
        return Partition(arr, p, r);
    }

    public static int Partition(ArrayList<A4Vertex> arr, int p, int r){
        A4Vertex x = arr.get(r);
        int i = p;
        int j = r;
        while(i<j && i<=r){
            while(i<=r && ( arr.get(i).occurences>x.occurences || (arr.get(i).occurences==x.occurences && lexSort(x.label, arr.get(i).label)<0 ) )){
                i++;
            }
            while(j>=p && (arr.get(j).occurences<x.occurences || (arr.get(j).occurences==x.occurences && lexSort(x.label, arr.get(j).label)>0 ) )){
                j--;
            }
            if(i<j){
                A4Vertex temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        return j;
    }

    public static void randomizedQuickSort(ArrayList<A4Vertex> arr, int p, int r){
        if(r>p){
            int q = randomizedPartition(arr, p, r);
            // System.out.println(arr.get(p).label + " " + arr.get(p).occurences+" " +arr.get(q).label + " "+arr.get(q).occurences+" "+p+" "+q);
            randomizedQuickSort(arr, p, q);
            randomizedQuickSort(arr, q+1, r);
        }
    }

    public static int lexSort(String s1, String s2){
        int len1 = s1.length();
        int len2 = s2.length();
        int minLen = Math.min(len1, len2);
        int i = 0;
        while(i<minLen && s1.charAt(i)==s2.charAt(i)){
            i++;
        }
        if(i>=minLen){
            if(len1>len2){
                return -1;
            }else{
                return +1;
            }
        }
        if((int)s1.charAt(i)<(int)s2.charAt(i)){
            return -1;
        }else{
            return +1;
        }

    }

    public static class A4Graph{
        HashMap<String, LinkedList> adjMap;
        LinkedList<String> nodes;
        ArrayList<A4Vertex> vertices;

        public A4Graph(){
            this.adjMap = new HashMap<String, LinkedList>();
            this.nodes = new LinkedList<String>();
            this.vertices = new ArrayList<A4Vertex>();
        }
    }
    public static class A4Vertex{
        String label;
        int occurences;
        int id;
        public A4Vertex(String labelInput, int ID){
            this.label = labelInput;
            this.occurences = 0;
            this.id = ID;
        }
    }
    public static class A4Edge{
        A4Vertex u;
        A4Vertex v;
        int weight;
        public A4Edge(A4Vertex v1, A4Vertex v2, int wt){
            this.u = v1;
            this.v = v2;
            this.weight = wt;
        }
    }
    public static void main(String[] args){
        A4Graph graph = new A4Graph();
        String nodesFile = args[0];
        String edgesFile = args[1];
        String function = args[2];
        BufferedReader br = null;
        BufferedReader brNode = null;
        switch(function){
            case "average":
                // System.out.println("Average function chosen");
                try{
                    br = new BufferedReader(new FileReader(edgesFile));  
                    brNode = new BufferedReader(new FileReader(nodesFile));  
                    String line;
                    String line2;
                    line = br.readLine();
                    line2 = brNode.readLine();
                    //Discarding header line
                    float totalKeys = 0;
                    while ((line2 = brNode.readLine()) != null) {
                        totalKeys++;
                    }
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
                    System.out.println(totalDegree +" , " +totalKeys);
                    float averageFloat = totalDegree/totalKeys;
                    // float totalKeys = graph.adjMap.keySet().size();
                    String average = String.format(java.util.Locale.US,"%.2f", averageFloat);
                    System.out.println(average);
                }catch (IOException exception) {
                    exception.printStackTrace();
                }catch (Exception ifTotalKeysZero) {
                    ifTotalKeysZero.printStackTrace();
                }finally {
                    try {
                        br.close();
                        brNode.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                // System.out.println(graph.adjMap.get("Triton").get(0));
                // System.out.println(graph.adjMap.get("Black Panther / T'chal").get(7));
                break;
            case "rank":
                HashMap<String, Integer> nodeId = new HashMap<>();
                // System.out.println("Rank function chosen");
                try{ 
                    brNode = new BufferedReader(new FileReader(nodesFile));  
                    String line2;
                    line2 = brNode.readLine();
                    //Discarding header lines in both files
                    while ((line2 = brNode.readLine()) != null) {
                        String[] nodeEntry = line2.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        // System.out.println("Adding "+nodeEntry[1]);
                        if(nodeEntry[1].charAt(0)=='\"'){
                            int strLen = nodeEntry[1].length();
                            nodeEntry[1] = nodeEntry[1].substring(1, strLen-1);
                        }
                        nodeId.put(nodeEntry[1], graph.vertices.size());
                        graph.vertices.add(new A4Vertex(nodeEntry[1], nodeId.get(nodeEntry[1])));
                    }
                    brNode.close();
                    //Discarding header line
                    br = new BufferedReader(new FileReader(edgesFile)); 
                    String line;
                    line = br.readLine();
                    int strLen = 0;
                    while ((line = br.readLine()) != null) {
                        String[] edgeEntry = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        if(edgeEntry[0].charAt(0)=='\"'){
                            strLen = edgeEntry[0].length();
                            edgeEntry[0] = edgeEntry[0].substring(1, strLen-1);
                        }
                        if(edgeEntry[1].charAt(0)=='\"'){
                            strLen = edgeEntry[1].length();
                            edgeEntry[1] = edgeEntry[1].substring(1, strLen-1);
                        }
                        if(nodeId.containsKey(edgeEntry[0]) && nodeId.containsKey(edgeEntry[1])){
                            int index1 = nodeId.get(edgeEntry[0]);
                            graph.vertices.get(index1).occurences+=Integer.valueOf(edgeEntry[2]);
                            int index2 = nodeId.get(edgeEntry[1]);
                            graph.vertices.get(index2).occurences+=Integer.valueOf(edgeEntry[2]);
                        }
                    }
                    randomizedQuickSort(graph.vertices, 0, graph.vertices.size()-1);
                    for (int i=0; i<graph.vertices.size(); i++) {
                        A4Vertex temp = graph.vertices.get(i);
                        // System.out.println((temp.id+1) + "->" + temp.label + "->" + temp.occurences);
                        System.out.print(temp.label);
                        if(i==(graph.vertices.size()-1)){
                            System.out.println();
                            break;
                        };
                        System.out.print(",");
                    }
                    // System.out.println(nodeId.size());
                }catch (IOException exception) {
                    exception.printStackTrace();
                }finally {
                    try {
                        br.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    try {
                        brNode.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
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