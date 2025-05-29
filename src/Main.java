import java.util.Random;

public class Main {
    public static void main(String[] args)  {
        int dim = 10000000;
        int threadNum = 8;
        ArrClass arrClass = new ArrClass(dim, threadNum);





        int[] parallelMin = arrClass.findMin();
        System.out.println("Multi-thread min: " + parallelMin[0] + " at index " + parallelMin[1]);
    }
}