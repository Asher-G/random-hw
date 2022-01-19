package test;

public class Test3 {


    public static int findLastZero(int[] A){
     return findLastZeroRec(A,0, A.length-1);
    }

    public static int findLastZeroRec(int[] A, int i, int j){
        int k = (i+j) / 2;
        if (A[k] == 0 && A[k+1] == 1) return k;
        if (A[k] == 0) return findLastZeroRec(A,k+1,j);
        return findLastZeroRec(A,i,k-1);
    }

    public static void main(String[] args){
        int[] A = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};
        System.out.println(findLastZero(A));
    }
}
