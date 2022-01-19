import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class RobotPath {

    item[][] map;
    int currentRow;
    int currentCol;
    int destRow;
    int destCol;

    public void readInput(String filename) throws IOException {
        Scanner scnr = new Scanner(new FileReader(filename));

        scnr.next();
        int nrows = scnr.nextInt();

        scnr.next();
        int ncols = scnr.nextInt();
        map = new item[nrows][ncols];

        for (int i = 0; i < nrows; i++) {
            for (int j = 0; j < ncols; j++) {
                map[i][j] = new item();
                map[i][j].row = i;
                map[i][j].col = j;
            }
        }


        scnr.next();
        currentRow = scnr.nextInt();
        currentCol = scnr.nextInt();
        map[currentRow][currentCol].type = "S";

        scnr.next();
        destRow = scnr.nextInt();
        destCol = scnr.nextInt();
        map[destRow][destCol].type = "D";

        scnr.next();
        while (scnr.hasNextInt()){
            map[scnr.nextInt()][scnr.nextInt()].type = "*";
        }

        scnr.close();
    }


    private boolean operable(int x, int y, item[][] grid)
    {
        if (x >= 0 && y >= 0 && x < grid.length
                && y < grid[0].length && grid[x][y].type != "*"
                && map[x][y].searched == false) {
            return true;
        }
        return false;
    }

    public int planShortest() {

        item source = new item();
        source.row = currentRow;
        source.col = currentCol;

        item dest = new item();
        dest.col = destCol;
        dest.row = destRow;
        String[][] dir = new String[map.length][map[0].length];
        for(int i = 0; i < dir.length; i++){
            for(int j = 0; j < dir[i].length; j++){
                dir[i][j] = new String();
            }
        }


        LinkedList<item> q = new LinkedList<>();
        Stack<item> stack = new Stack<>();

        item temp = new item();
        temp.row = source.row ;
        temp.col  = source.col;
        q.add(temp);


        map[source.row][source.col].searched = true;

        while (!q.isEmpty()) {
            item v = q.remove();

            if (v.row == destRow && v.col == destCol){
                dest.distance = v.distance;
                break;
            }

            if (operable(v.row - 1, v.col, map)) {
                temp = new item();
                temp.row = v.row - 1;
                temp.col = v.col;
                temp.distance = v.distance + 1;
                q.add(temp);
                map[v.row - 1][v.col].searched = true;

            }

            if (operable(v.row + 1, v.col, map)) {
                temp = new item();
                temp.row = v.row + 1;
                temp.col = v.col;
                temp.distance = v.distance + 1;
                q.add(temp);
                map[v.row + 1][v.col].searched = true;

            }

            if (operable(v.row, v.col - 1, map)) {
                temp = new item();
                temp.row = v.row;
                temp.col = v.col - 1;
                temp.distance = v.distance + 1;
                q.add(temp);

                map[v.row][v.col - 1].searched = true;

            }

            if (operable(v.row, v.col + 1, map)) {
                temp = new item();
                temp.row = v.row;
                temp.col = v.col + 1;
                temp.distance = v.distance + 1;
                q.add(temp);
                map[v.row][v.col + 1].searched = true;

            }
            stack.add(v);
        }


        while(!stack.isEmpty() && stack.peek().distance >= dest.distance){
            stack.pop();
        }
        planShortestRec(dest, stack, dir);

        return -1;
    }

    boolean found = false;

    public void quickPlan() {
        try {
            /* Base cases for recursion */
            //Found destination
            if (map[currentRow][currentCol].searched) {
                return;
            }
            map[currentRow][currentCol].searched = true;

            if (map[currentRow][currentCol].type.equals("D")) {
                found = true;
                return;
            }
            //Found obstacle
            if (map[currentRow][currentCol].type.equals("*")) {
                return;
            }

            /* Recursion */
            //Finding the closeness of all directions

            double southDist = findDist(currentRow + 1, currentCol, destRow, destCol);
            double northDist = findDist(currentRow - 1, currentCol, destRow, destCol);
            double westDist = findDist(currentRow, currentCol - 1, destRow, destCol);
            double eastDist = findDist(currentRow, currentCol + 1, destRow, destCol);

            char[] lowest = new char[4];

            //Initializing lowest to be an array of the four directions sorted in order of closeness.
            if (northDist <= southDist && northDist <= eastDist && northDist <= westDist){
                lowest[0] = 'n';
                lowest[3] = 's';
                if (westDist <= eastDist){
                    lowest[1] = 'w';
                    lowest[2] = 'e';
                } else {
                    lowest[1] = 'e';
                    lowest[2] = 'w';
                }
            } else if (westDist <= eastDist && westDist <= northDist && westDist <= southDist){
                lowest[0] = 'w';
                lowest[3] = 'e';
                if (northDist <= southDist) {
                    lowest[1] = 'n';
                    lowest[2] = 's';
                } else {
                    lowest[1] = 's';
                    lowest[2] = 'n';
                }
            } else if (eastDist <= westDist && eastDist <= northDist && eastDist <= southDist){
                lowest[0] = 'e';
                lowest[3] = 'w';
                if (northDist <= southDist) {
                    lowest[1] = 'n';
                    lowest[2] = 's';
                } else {
                    lowest[1] = 's';
                    lowest[2] = 'n';
                }
            } else {
                lowest[0] = 's';
                lowest[3] = 'n';
                if (westDist <= eastDist){
                    lowest[1] = 'w';
                    lowest[2] = 'e';
                } else {
                    lowest[1] = 'e';
                    lowest[2] = 'w';
                }
            }

            //Where the method calls itself recursively:
            //Finding a path by recursively checking the best possible move from the current spot.
            //Once a path is found the characters in the grid are changed accordingly. If not path is found
            //then the algorithm will try to step out of bounds to avoid an obstacle and in doing so
            //will throw an array out of bounds exception. This exception is then caught and nothing
            //is done, because this simply means no path is possible so grid should be unchanged.
            switch (lowest[0]) {
                case 's':
                    currentRow++;
                    quickPlan();
                    currentRow--;
                    break;
                case 'n':
                    currentRow--;
                    quickPlan();
                    currentRow++;
                    break;
                case 'w':
                    currentCol--;
                    quickPlan();
                    currentCol++;
                    break;
                default:
                    currentCol++;
                    quickPlan();
                    currentCol--;
            }
            if (found) {
                map[currentRow][currentCol].type = Character.toString(lowest[0]);
                return;
            }

            switch (lowest[1]) {
                case 's':
                    currentRow++;
                    quickPlan();
                    currentRow--;
                    break;
                case 'n':
                    currentRow--;
                    quickPlan();
                    currentRow++;
                    break;
                case 'w':
                    currentCol--;
                    quickPlan();
                    currentCol++;
                    break;
                default:
                    currentCol++;
                    quickPlan();
                    currentCol--;
            }
            if (found) {
                map[currentRow][currentCol].type = Character.toString(lowest[1]);
                return;
            }

            switch (lowest[2]) {
                case 's':
                    currentRow++;
                    quickPlan();
                    currentRow--;
                    break;
                case 'n':
                    currentRow--;
                    quickPlan();
                    currentRow++;
                    break;
                case 'w':
                    currentCol--;
                    quickPlan();
                    currentCol++;
                    break;
                default:
                    currentCol++;
                    quickPlan();
                    currentCol--;
            }
            if (found) {
                map[currentRow][currentCol].type = Character.toString(lowest[2]);
                return;
            }

            switch (lowest[3]) {
                case 's':
                    currentRow++;
                    quickPlan();
                    currentRow--;
                    break;
                case 'n':
                    currentRow--;
                    quickPlan();
                    currentRow++;
                    break;
                case 'w':
                    currentCol--;
                    quickPlan();
                    currentCol++;
                    break;
                default:
                    currentCol++;
                    quickPlan();
                    currentCol--;
            }
            if (found) {
                map[currentRow][currentCol].type = Character.toString(lowest[3]);
                return;
            }
        } catch (ArrayIndexOutOfBoundsException e){
            //If the endpoint is not reachable this will happen. Nothing happens here because if this happens the
            //method should just exit without writing paths on the grid.
        }
    }

    public void output() {
        map[currentRow][currentCol].type = "S";
        for (item[] x : map){
            for (item y : x){
                System.out.printf("%5s", y.type);
            }
            System.out.println();
        }
        resetGrid();
    }

    public double findDist(double curRow, double curCol, double desRow, double desCol){
        return Math.sqrt(Math.pow((curRow - desRow),2) + Math.pow((curCol - desCol),2));
    }

    public void resetGrid(){
        for (item[] x : map){
            for (item y : x){
                y.searched = false;
                if (!y.type.equals("S") && !y.type.equals("D") && !y.type.equals("*") && !y.type.equals("0")){
                    y.type = "0";
            }
        }
    }
    }


    public void planShortestRec(item base, Stack<item> stack, String[][] direcs){
        Stack<item> keepNodes = new Stack<>();

        while(!stack.isEmpty() && stack.peek().distance >= base.distance){
            keepNodes.add(stack.pop());
        }

        while(!stack.isEmpty() && stack.peek().distance == base.distance - 1) {

            item tmp = stack.pop();

            if (base.row -1 == tmp.row && base.col == tmp.col) {

                direcs[tmp.row][tmp.col] += "s";
                keepNodes.add(tmp);
                planShortestRec(tmp, stack, direcs);

            } else if (base.row +1  == tmp.row && base.col == tmp.col) {

                direcs[tmp.row][tmp.col] += "n";
                keepNodes.add(tmp);
                planShortestRec(tmp, stack, direcs);

            } else if (base.row  == tmp.row && base.col +1 == tmp.col) {

                direcs[tmp.row][tmp.col] += "w";
                keepNodes.add(tmp);
                planShortestRec(tmp, stack, direcs);

            } else if (base.row == tmp.row && base.col -1 == tmp.col) {

                if (map[tmp.row][tmp.col].type == "0") {
                    map[tmp.row][tmp.col].type = "";
                }
                direcs[tmp.row][tmp.col] += "e";
                keepNodes.add(tmp);
                planShortestRec(tmp, stack, direcs);

            } else {
                keepNodes.add(tmp);
            }
        }

        while(!(keepNodes.isEmpty())){
            stack.add(keepNodes.pop());
        }

        for (int i = 0; i < map.length; i++) {

            for (int j = 0; j < map[i].length; j++) {

                String tmp = "";
                if(direcs[i][j].contains("s")){
                    tmp += "s";
                } if(direcs[i][j].contains("n")){
                    tmp += "n";
                } if(direcs[i][j].contains("w")){
                    tmp += "w";
                } if(direcs[i][j].contains("e")){
                    tmp += "e";
                }

                if(tmp.length() > 0) {
                    map[i][j].type = tmp;
                }

            }
        }


    }

}

class item {

    String type;
    boolean searched;
    int row;
    int col;
    int distance = 0;

    item(){
            this.type = "0";
            this.searched = false;
    }
}
