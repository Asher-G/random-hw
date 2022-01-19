import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PathFinder {

    public PathFinder(){
        heap = new MinHeap();
        numIntersections = 0;
        map = new ArrayList<ArrayList<Node>>();
    }

    int numIntersections;
    ArrayList<ArrayList<Node>> map;
    MinHeap heap;
    Intersection[] intersections;

    public void readInput(String fileName) throws IOException {
        Scanner in = new Scanner(new FileReader(fileName));

        numIntersections = in.nextInt();
        int numRoadways = in.nextInt();

        intersections = new Intersection[numIntersections];


        for (int i = 0; i < numIntersections; i++){
            ArrayList<Node> item = new ArrayList<Node>();
            map.add(item);
        }

        for (int i = 0; i < numIntersections; i++){
            intersections[in.nextInt()] = new Intersection(in.nextInt(), in.nextInt());
        }

        in.nextLine();

        for (int i = 0; i < numRoadways*2; i+=2){
            int x1 = in.nextInt();
            int x2 = in.nextInt();
            double distance = distance(intersections[x1].x, intersections[x1].y, intersections[x2].x,intersections[x2].y);
            map.get(x1).add(new Node(x2, distance));
            map.get(x2).add(new Node(x1, distance));
        }


    }

    public double[] shortestPathDistances(int src){
        double[] distances = new double[numIntersections];
        Set<Integer> settled = new HashSet<Integer>();
        for (int i = 0; i < numIntersections; i++) distances[i] = Integer.MAX_VALUE;

        heap.add(src, 0);
        distances[src] = 0;

        while (settled.size() != numIntersections){
            if (heap.heap.peek() == null) {
                for (int i = 0; i < distances.length; i++){
                    if (distances[i] == Integer.MAX_VALUE) distances[i] = -1;
                }
                return distances;
            }

            int u = heap.heap.poll().key;

            if(settled.contains(u)) continue;
            settled.add(u);

            double distance, newDistance = -1;

            for(int i = 0; i < map.get(u).size(); i++){
                Node v = map.get(u).get(i);

                if (!settled.contains(v.key)){
                    distance = v.value;
                    newDistance = distances[u] + distance;

                    if (newDistance < distances[v.key]) distances[v.key] = newDistance;

                    heap.add(v.key, distances[v.key]);
                }
            }
        }
        for (int i = 0; i < distances.length; i++){
            if (distances[i] == Integer.MAX_VALUE) distances[i] = -1;
        }
        return distances;
    }


    public int noOfShortestPaths(int src, int dest){
        double[] distances = new double[numIntersections];
        int[] paths = new int[numIntersections];
        Set<Integer> settled = new HashSet<Integer>();
        for (int i = 0; i < numIntersections; i++) distances[i] = Integer.MAX_VALUE;

        heap.add(src, 0);
        distances[src] = 0;
        paths[src] = 1;

        while (settled.size() != numIntersections){
            if (heap.heap.peek() == null) return paths[dest];

            int u = heap.heap.poll().key;

            if(settled.contains(u)) continue;
            settled.add(u);

            double distance, newDistance = -1;

            for(int i = 0; i < map.get(u).size(); i++){
                Node v = map.get(u).get(i);

                if (!settled.contains(v.key)){
                    distance = v.value;
                    newDistance = distances[u] + distance;

                    if (newDistance < distances[v.key]) {
                        distances[v.key] = newDistance;
                        paths[v.key] = paths[u];
                    }
                    else if (newDistance == distances[v.key]) paths[v.key] += paths[u];

                    heap.add(v.key, distances[v.key]);
                }
            }
        }

        return paths[dest];
    }

    public ArrayList<Integer> fromSrcToDest(int src, int dest, int A, int B){
        if (src == dest){
            ArrayList<Integer> path = new ArrayList<Integer>();
            path.add(src);
            return path;
        }
        double[] distances = new double[numIntersections];
        ArrayList<Integer>[] paths = new ArrayList[numIntersections];
        for (int i = 0; i < paths.length; i++){
            ArrayList<Integer> path = new ArrayList<Integer>();
            //path.add(src);
            paths[i] = path;
        }
        Set<Integer> settled = new HashSet<Integer>();
        for (int i = 0; i < numIntersections; i++) distances[i] = Integer.MAX_VALUE;

        heap.add(src, 0);
        distances[src] = 0;

        while (settled.size() != numIntersections){
            if (heap.heap.peek() == null) {
                if (paths[dest].size() == 0) return null;
                paths[dest].add(dest);
                return paths[dest];
            }


            int u = heap.heap.poll().key;

            if(settled.contains(u)) continue;
            settled.add(u);

            double distance, newDistance = -1;

            for(int i = 0; i < map.get(u).size(); i++){
                Node v = map.get(u).get(i);

                if (!settled.contains(v.key)){
                    distance = v.value;
                    newDistance = A * (distances[u] + distance)
                                + B * ( distance(intersections[v.key].x,intersections[v.key].y,intersections[dest].x,intersections[dest].y)
                                        - ( distance(intersections[u].x,intersections[u].y,intersections[dest].x,intersections[dest].y)));

                    if (newDistance < distances[v.key]) {
                        distances[v.key] = newDistance;
                        ArrayList<Integer> temp = new ArrayList<Integer>();
                        for (int j = 0; j < paths[u].size(); j++){
                            temp.add(paths[u].get(j));
                        }
                        paths[v.key] = temp;
                        paths[v.key].add(u);
                    }  else if (newDistance > distances[v.key]);

                    heap.add(v.key, distances[v.key]);
                }
            }
        }
        if (paths[dest].size() == 0) return null;
        paths[dest].add(dest);
        return paths[dest];
    }

    public ArrayList<Integer> fromSrcToDestVia(int src, int dest, ArrayList<Integer> intersects, int A, int B){
        ArrayList<Integer> ret = fromSrcToDest(src, intersects.get(0),A,B);
        for (int i = 0; i < intersects.size() - 1; i++){
            ArrayList<Integer> temp = fromSrcToDest(intersects.get(i), intersects.get(i+1),A,B);
            temp.remove(0);
            ret.addAll(temp);
        }
        ArrayList<Integer> temp = fromSrcToDest(intersects.get(intersects.size()-1), dest,A,B);
        temp.remove(0);
        ret.addAll(temp);
        return ret;
    }
/*
minheap = neighbors of src;
visited.add src;
tree += src;

for->

temp = minheap.pop;
if pop !visited{
    tree[i] += temp;
    visited.add temp;
    minheap += temp.neighbors;
}
 */

    public int[] minCostReachabilityFromSrc(int src){
        MinHeap heap = new MinHeap();
        MinHeap parents = new MinHeap();
        boolean[] visited = new boolean[map.size()];
        int[] tree = new int[map.size()];

        visited[src] = true;

        for (int i = 0; i < tree.length; i++) tree[i] = -1;
        tree[src] = src;
        for (int i = 0; i < map.get(src).size(); i++){
            heap.add(map.get(src).get(i).key, map.get(src).get(i).value,src);
        }

        while (heap.heap.peek() != null){
            Node temp = heap.heap.poll();
            if (!visited[temp.key]){
                visited[temp.key] = true;
                tree[temp.key] = temp.parent;
                for (int i = 0; i < map.get(temp.key).size(); i++){
                    heap.add(map.get(temp.key).get(i).key, map.get(temp.key).get(i).value,temp.key);
                }
            }
        }

        return tree;
    }



    public double minCostOfReachableFromSrc(int src){
        int [] tree = minCostReachabilityFromSrc(src);
        double cost = 0;
        for(int i = 0; i < tree.length; i++){
            if(tree[i] != -1 && tree[i] != i){
                for (int j = 0; j < map.get(i).size(); j++) {
                    if (map.get(i).get(j).key == tree[i]) cost += map.get(i).get(j).value;
                }
            }
        }
        return cost;
    }

    public boolean isFullReachableFromSrc(int src){
        int [] tree = minCostReachabilityFromSrc(src);
        for (int value : tree) {
            if (value == -1) {
                return false;
            }
        }
        return true;
    }

    public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }
}

class MinHeap {

    PriorityQueue<Node> heap;

    public MinHeap(){
        heap = new PriorityQueue<Node>();
    }

    public void add(int i, double d){
        heap.add(new Node(i,d));
    }
    public void add(int i, double d, int p){
        heap.add(new Node(i,d,p));
    }

    public ArrayList<Integer> getHeap(){
        PriorityQueue<Node> temp = new PriorityQueue<>(heap);
        ArrayList<Integer> keys = new ArrayList<>();
        while(temp.peek() != null){
            keys.add(temp.poll().key);
        }
        return keys;
    }

}

class Intersection {
    int x;
    int y;

    Intersection(int x, int y){
        this.x = x;
        this.y = y;
    }
}

class Node implements Comparable<Node>{
    int key;
    double value;
    int parent;

    public Node(){
    }

    public Node(int key, double value){
        this.key = key;
        this.value = value;
    }

    public Node(int key, double value, int parent){
        this.key = key;
        this.value = value;
        this.parent = parent;
    }

    @Override
    public int compareTo(Node node){
        if (this.value < node.value) return -1;
        if (this.value > node.value) return 1;
        if (this.key < node.key) return -1;
        else return 1;
    }
}
