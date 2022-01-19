import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PathFinderTester {

    public static void main(String[] args) throws IOException {

//        MinHeap heap = new MinHeap();
//        for (int i = 0; i < 12; i++){
//            heap.add(i, 12.0 - i);
//        }
//        System.out.println(heap.getHeap());

//        PathFinder pathFinder = new PathFinder();
//        ArrayList<ArrayList<Node>> adj = new ArrayList<>();
//        pathFinder.numIntersections = 5;
//
//        for (int i = 0; i < 5; i++){
//            ArrayList<Node> item = new ArrayList<Node>();
//            adj.add(item);
//        }
//        adj.get(0).add(new Node(1, 9));
//        adj.get(0).add(new Node(2, 6));
//        adj.get(0).add(new Node(3, 5));
//        adj.get(0).add(new Node(4, 3));
//
//        adj.get(2).add(new Node(1, 2));
//        adj.get(2).add(new Node(3, 4));
//        pathFinder.map = adj;
//
//        double[] distances = pathFinder.shortestPathDistances(0);
//        for (int i = 0; i < distances.length; i++){
//            System.out.println(distances[i]);
//        }

//        PathFinder pathFinder = new PathFinder();
//        ArrayList<ArrayList<Node>> adj = new ArrayList<>();
//        pathFinder.numIntersections = 5;
//
//        for (int i = 0; i < 5; i++){
//            ArrayList<Node> item = new ArrayList<Node>();
//            adj.add(item);
//        }
//        adj.get(0).add(new Node(1, 8.101121));
//        adj.get(0).add(new Node(2, 6.101121));
//        adj.get(0).add(new Node(3, 5.101121));
//        adj.get(0).add(new Node(4, 3));
//
//        adj.get(2).add(new Node(1, 2));
//        adj.get(2).add(new Node(3, 4));
//        adj.get(3).add(new Node(1, 3));
//        pathFinder.map = adj;
//
//        int paths = pathFinder.noOfShortestPaths(0,1);
//
//        System.out.println(paths);

//        PathFinder pathFinder = new PathFinder();
//        ArrayList<ArrayList<Node>> adj = new ArrayList<>();
//        pathFinder.numIntersections = 5;
//
//        for (int i = 0; i < 5; i++){
//            ArrayList<Node> item = new ArrayList<Node>();
//            adj.add(item);
//        }
//
//        pathFinder.intersections = new Intersection[5];
//        for (int i = 0; i < 5; i++){
//            pathFinder.intersections[i] = new Intersection(1,1);
//        }
//        adj.get(0).add(new Node(1, 1));
//        adj.get(0).add(new Node(2, 1));
//        adj.get(0).add(new Node(3, 1));
//        adj.get(0).add(new Node(4, 7));
//        adj.get(1).add(new Node(2, 2));
//        adj.get(1).add(new Node(3, 2));
//        adj.get(1).add(new Node(4, 2));
//        adj.get(2).add(new Node(3, 1));
//        adj.get(2).add(new Node(4, 1));
//        adj.get(3).add(new Node(4, 8));
//        adj.get(0).add(new Node(2, 6));
//        adj.get(0).add(new Node(3, 5.101121));
//        adj.get(0).add(new Node(4, 3));
//
//        adj.get(1).add(new Node(0, 1));
//        adj.get(2).add(new Node(0, 1));
//        adj.get(3).add(new Node(0, 1));
//        adj.get(4).add(new Node(0, 7));
//        adj.get(2).add(new Node(1, 2));
//        adj.get(3).add(new Node(1, 2));
//        adj.get(4).add(new Node(1, 2));
//        adj.get(3).add(new Node(2, 1));
//        adj.get(4).add(new Node(3, 1));
//        adj.get(4).add(new Node(3, 2));
//        adj.get(2).add(new Node(0, 6));
//        adj.get(3).add(new Node(0, 5.101121));
//        adj.get(4).add(new Node(0, 3));
////
////        adj.get(2).add(new Node(1, 2));
////        adj.get(2).add(new Node(3, 4));
////        adj.get(3).add(new Node(1, 3));
//        pathFinder.map = adj;
//        ArrayList<Integer> al = new ArrayList<Integer>();
//        al.add(1);
//        al.add(3);
//        ArrayList<Integer> paths = pathFinder.fromSrcToDestVia(0,4, al, 1,0);
//        //ArrayList<Integer> paths = pathFinder.fromSrcToDest(1,4, 1,0);
//
//        System.out.println(paths);

        PathFinder pathFinder = new PathFinder();
        pathFinder.readInput("sample0.txt");
//        double[] bleh = pathFinder.shortestPathDistances(3);
//        for (double bleg : bleh){
//            System.out.println(bleg);
//        }

//        double[] d = pathFinder.shortestPathDistances(3);
//        for (double dd : d) System.out.println(dd);
//        System.out.println();
//
//        System.out.println(pathFinder.noOfShortestPaths(3,6));
//        int[] agerwq = pathFinder.minCostReachabilityFromSrc(3);
//        for (int asd : agerwq)
//        System.out.println(asd);
//        System.out.println(pathFinder.minCostOfReachableFromSrc(3));
//        System.out.println();
        System.out.println(pathFinder.fromSrcToDest(0,3,0,1));
//
//        System.out.println(bleh);

    }
}
