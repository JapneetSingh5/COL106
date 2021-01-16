import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

public class A4_2019MT10696 {

    public static class A4Graph{
        HashMap<String, LinkedList<String>> adjMap;
        LinkedList<String> nodes;
        ArrayList<A4Vertex> vertices;
        HashMap<String, Integer> nodeId;
        ArrayList<ArrayList<String>> connectedComponents;
        int componentCount;

        public A4Graph(){
            this.adjMap = new HashMap<String, LinkedList<String>>();
            this.nodes = new LinkedList<String>();
            this.vertices = new ArrayList<A4Vertex>();
            this.nodeId = new HashMap<String, Integer>();
            this.connectedComponents = new ArrayList<ArrayList<String>>();
            this.componentCount = 0;
        }

        public void performDFS(){
            for(int i=0; i<this.vertices.size(); i++){
                if(this.vertices.get(i).visited==0){
                    this.componentCount++;
                    this.connectedComponents.add(new ArrayList<String>());
                    DFS(vertices.get(i));
                }
            }
        }

        public void DFS(A4Vertex v){
            v.visited=1;
            this.connectedComponents.get(componentCount-1).add(v.label);
            LinkedList<String> neighbors = this.adjMap.get(v.label);
            if(neighbors!=null){
                for(int i=0; i<neighbors.size(); i++){
                    String neighbor = neighbors.get(i);
                    // System.out.println("Getting id for " + neighbor +", nodeId map size is "+ this.nodeId.size());
                    int index = this.nodeId.get(neighbor);
                    // System.out.println("Got it");
                    A4Vertex neighborVertex = this.vertices.get(index);
                    if(neighborVertex.visited==0){
                        DFS(neighborVertex);
                    }
                }  
            }
        }


    }
    public static class A4Vertex{
        String label;
        float occurences;
        int id;
        int visited;
        public A4Vertex(String labelInput, int ID){
            this.label = labelInput;
            this.occurences = 0;
            this.id = ID;
            this.visited = 0;
        }
    }
    public static class A4Edge{
        A4Vertex u;
        A4Vertex v;
        float weight;
        public A4Edge(A4Vertex v1, A4Vertex v2, float wt){
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
                    //Reader to read from edgesFile
                    brNode = new BufferedReader(new FileReader(nodesFile));  
                    //reader to read from nodesFile
                    String line;
                    //edge entry is read into line
                    String line2;
                    //node entry is read into line2
                    line = br.readLine();
                    line2 = brNode.readLine();
                    //Discarding header lines for the files
                    float totalKeys = 0;
                    //total no of keys/nodes is zero initally
                    while ((line2 = brNode.readLine()) != null) { //while !EOF, read the node entry and increment node total
                        totalKeys++;
                    }
                    int strLen = 0;
                    while ((line = br.readLine()) != null) { //while !EOF, read a row from edgesFile
                        String[] edgeEntry = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); //Split the read row according the regex

                        //if label contains a "", remove it before storing
                        if(edgeEntry[0].charAt(0)=='\"'){
                            strLen = edgeEntry[0].length();
                            edgeEntry[0] = edgeEntry[0].substring(1, strLen-1);
                        }
                        if(edgeEntry[1].charAt(0)=='\"'){
                            strLen = edgeEntry[1].length();
                            edgeEntry[1] = edgeEntry[1].substring(1, strLen-1);
                        }

                        //if adjMap has an entry corresponding to the source vertex, get the adjList from it
                        //else create a new adjList, put it as value for the vertex's label as key and then add the target vertex into the adjList
                        if(graph.adjMap.containsKey(edgeEntry[0])){
                            graph.adjMap.get(edgeEntry[0]).addLast(edgeEntry[1]);
                        }else{
                            graph.adjMap.put(edgeEntry[0], new LinkedList<String>());
                            graph.adjMap.get(edgeEntry[0]).add(edgeEntry[1]);
                        }
                        //repeat above process for the target vertex and add source vertex to its adjList
                        if(graph.adjMap.containsKey(edgeEntry[1])){
                            graph.adjMap.get(edgeEntry[1]).addLast(edgeEntry[0]);
                        }else{
                            graph.adjMap.put(edgeEntry[1], new LinkedList<String>());
                            graph.adjMap.get(edgeEntry[1]).add(edgeEntry[0]);
                        }

                    }
                    float totalDegree = 0;
                    //for every key in adjMap, compute the size of the adjList, ie the degree of each vertex and add it to the total
                    for (String key : graph.adjMap.keySet()) {
                        totalDegree += graph.adjMap.get(key).size();
                    }
                    float averageFloat = 0; 

                    //if totalVertices are zero, output 0 else output the average degree
                    if(totalKeys!=0){
                        averageFloat = totalDegree/totalKeys;
                    }
                    // System.out.println(totalDegree +" , " +totalKeys);
                    // float totalKeys = graph.adjMap.keySet().size();

                    String average = String.format("%.2f", averageFloat);
                    System.out.print(average);
                    System.out.println();
                }catch (IOException exception) {
                    //IOException encountered
                }finally {
                    try {
                        br.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                    try {
                        brNode.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                }
                // System.out.println(graph.adjMap.get("Triton").get(0));
                // System.out.println(graph.adjMap.get("Black Panther / T'chal").get(7));
                break;

            case "rank":
                // System.out.println("Rank function chosen");
                try{ 
                    brNode = new BufferedReader(new FileReader(nodesFile));  

                    //BufferedReader to read nodes from nodesFile
                    String line2;
                    //line2 is string to fetch node row from nodesFile
                    line2 = brNode.readLine(); //line 2 reads a row from nodesFile, here it reads the header row
                    //Discarding header line
                    while ((line2 = brNode.readLine()) != null) { //while !EOF, read a row from nodesFile

                        String[] nodeEntry = line2.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); //split the row according to delimiter regex
                        // System.out.println("Adding "+nodeEntry[1]);

                        if(nodeEntry[1].charAt(0)=='\"'){ //if the name has double quotes, remove them before storing it into the graph
                            int strLen = nodeEntry[1].length();
                            nodeEntry[1] = nodeEntry[1].substring(1, strLen-1);
                        }

                        graph.nodeId.put(nodeEntry[1], graph.vertices.size());  
                        //graph.nodeId is the hashmap which has node's label as key and its array index as value
                        //array index is used to get the vertex from the ArrayList containing all the vertices

                        graph.vertices.add(new A4Vertex(nodeEntry[1], graph.nodeId.get(nodeEntry[1])));
                        //insert the vertex corresponding to the read node row into the vertices ArrayList at the right index
                    }

                    
                    //BufferedReader to read edges from edgesFile
                    br = new BufferedReader(new FileReader(edgesFile)); 
                    String line; 
                    //line is string to fetch edge row from the edgesFile
                    line = br.readLine();
                    //Discarding header line

                    int strLen = 0;

                    while ((line = br.readLine()) != null) { //while !EOF, read a row corresponding to the edge
                        String[] edgeEntry = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"); //split the edge entry

                        //if edge vertices' labels contain "", remove them before storing them
                        if(edgeEntry[0].charAt(0)=='\"'){ 
                            strLen = edgeEntry[0].length();
                            edgeEntry[0] = edgeEntry[0].substring(1, strLen-1);
                        }
                        if(edgeEntry[1].charAt(0)=='\"'){
                            strLen = edgeEntry[1].length();
                            edgeEntry[1] = edgeEntry[1].substring(1, strLen-1);
                        }

                        //if the edge entry's vertices are present in the nodeId Hashmap i.e the node HAS BEEN READ from the nodes file before
                        //we proceed to register the edge by adding the weight of the edge to its end vertices
                        if(graph.nodeId.containsKey(edgeEntry[0]) && graph.nodeId.containsKey(edgeEntry[1])){
                            //we get index corresponding to the first/source vertex of the edge from nodeId Hashmap,
                            int index1 = graph.nodeId.get(edgeEntry[0]);
                            //we get the vertex in the ArrayList and update its weight
                            graph.vertices.get(index1).occurences+=Float.valueOf(edgeEntry[2]);
                            //we repeat the above process for the target vertex
                            int index2 = graph.nodeId.get(edgeEntry[1]);
                            graph.vertices.get(index2).occurences+=Float.valueOf(edgeEntry[2]);
                        }
                    }
                    randomizedQuickSort(graph.vertices, 0, graph.vertices.size()-1, 1);
                    //we sort the vertices list using randomizedQuickSort in O(nlogn) time

                    //Printing the sorted vertices in the specified format

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
                    //IOException has occured
                }finally {
                    try {
                        br.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                    try {
                        brNode.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                }
                break;

            case "independent_storylines_dfs":
                // System.out.println("Independent Storylines function chosen");
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
                        // System.out.println("Adding nodeId for "+nodeEntry[1]+"->"+graph.vertices.size());
                        graph.nodeId.put(nodeEntry[1], graph.vertices.size());
                        graph.vertices.add(new A4Vertex(nodeEntry[1], graph.nodeId.get(nodeEntry[1])));
                    }
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
                    graph.performDFS();
                    for(int c=0; c<graph.connectedComponents.size(); c++){
                        randomizedQuickSort(graph.connectedComponents.get(c), 0, graph.connectedComponents.get(c).size()-1, 3);
                    }
                    randomizedQuickSort(graph.connectedComponents, 0, graph.connectedComponents.size()-1, 2);
                    for(int i=0; i<graph.connectedComponents.size(); i++){
                        // System.out.println("Connected Component Number "+(i+1)+", cc size is "+graph.connectedComponents.get(i).size());
                        for(int j=0; j<graph.connectedComponents.get(i).size(); j++){
                            System.out.print(graph.connectedComponents.get(i).get(j));
                            if(j==graph.connectedComponents.get(i).size()-1){
                                System.out.println();
                                break;
                            }
                            System.out.print(",");
                        }
                    }
                }catch (IOException exception) {
                    exception.printStackTrace();
                }finally {
                    try {
                        br.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                    try {
                        brNode.close();
                    } catch (Exception exception) {
                        //Exception occured
                    }
                }
                break;

            default:
                System.out.println("Unknown function chosen, please check your input");
                break;
        }
    }

    public static void randomizedQuickSort(ArrayList arr, int p, int r, int sortId){
        if(r>p){
            int q = randomizedPartition(arr, p, r, sortId);
            // System.out.println(arr.get(p).label + " " + arr.get(p).occurences+" " +arr.get(q).label + " "+arr.get(q).occurences+" "+p+" "+q);
            randomizedQuickSort(arr, p, q, sortId);
            randomizedQuickSort(arr, q+1, r, sortId);
        }
    }

    public static int randomizedPartition(ArrayList arr, int p, int r, int sortId){
        int i = (int)(p+Math.random()*(r-p+1));
        Object temp = arr.get(i);
        arr.set(i, arr.get(r));
        arr.set(r, temp);
        switch(sortId){
            case 1:
                return PartitionRank(arr, p, r);
            case 2:
                return PartitionComponentSize(arr, p, r);
            case 3:
                return PartitionComponentLexSort(arr, p, r);
            default:
                return -1;
        }
    }

    public static int PartitionRank(ArrayList<A4Vertex> arr, int p, int r){
        A4Vertex x = arr.get(r);
        int i = p;
        int j = r;
        while(i<j && i<=r){
            while(i<=r && ( arr.get(i).occurences>x.occurences || (arr.get(i).occurences==x.occurences && lexSort(x.label, arr.get(i).label)>0 ) )){
                i++;
            }
            while(j>=p && (arr.get(j).occurences<x.occurences || (arr.get(j).occurences==x.occurences && lexSort(x.label, arr.get(j).label)<0 ) )){
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

    public static int PartitionComponentSize(ArrayList<ArrayList<String>> arr, int p, int r){
        ArrayList<String> x = arr.get(r);
        int i = p;
        int j = r;
        while(i<j && i<=r){
            while(i<=r && ( arr.get(i).size()>x.size() || (arr.get(i).size()==x.size() && lexSort(arr.get(i).get(0), x.get(0))<0) )){
                i++;
            }
            while(j>=p && (arr.get(j).size()<x.size() || (arr.get(j).size()==x.size() && lexSort(arr.get(j).get(0), x.get(0))>0) )){
                j--;
            }
            if(i<j){
                ArrayList temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        return j;
    }

    public static int PartitionComponentLexSort(ArrayList<String> arr, int p, int r){
        String x = arr.get(r);
        int i = p;
        int j = r;
        while(i<j && i<=r){
            while(i<=r && ( lexSort(x, arr.get(i))>0 ) ){ //lexSort returns +1 when s2>s1 
                i++;
            }
            while(j>=p && ( lexSort(x, arr.get(j))<0 ) ){
                j--;
            }
            if(i<j){
                String temp = arr.get(i);
                arr.set(i, arr.get(j));
                arr.set(j, temp);
            }
        }
        return j;
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
            if(len1<len2){
                return +1;
            }else{
                return -1;
            }
        }
        if((int)s1.charAt(i)<(int)s2.charAt(i)){
            return +1;  //returns -1 if s2<s1 i.e. s2 should appear AFTER s1 in descending lexicographical order
        }else{
            return -1;  //returns +1 if s2>s1 i.e. s2 should appear BEFORE s1 in descending lexicographical order
        }

    }
}