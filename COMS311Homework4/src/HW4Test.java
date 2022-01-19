import java.io.IOException;

public class HW4Test {

    public static void main(String[] args) throws IOException {
        RobotPath rPath = new RobotPath();
        rPath.readInput(args[0]);
        System.out.println("\n planShortest:\n");
        rPath.planShortest();
        rPath.output();
        System.out.println("\n quickPlan:\n");
        rPath.quickPlan();
        rPath.output();


//        System.out.println(rPath.findDist(3,4,4,10));
//        System.out.println(rPath.findDist(3,4,4,10) == rPath.findDist(5,4,4,10));
    }
}