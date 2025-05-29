import java.util.Random;

public class ArrClass {
    private final int threadNum;
    public final int[] arr;

    private int min = Integer.MAX_VALUE;
    private int minIndex = -1;

    private final Object lock = new Object();

    private int threadCount = 0;

    public ArrClass(int arrLength, int threadNum) {

        arr = new int[arrLength];
        this.threadNum = threadNum;

        Random random = new Random();


        for(int i = 0; i < arr.length; i++){
            arr[i] = random.nextInt(1000);
        }

        int randomIndex = random.nextInt(arr.length);
        arr[randomIndex] = -100;
    }

    public void partMin(int startIndex, int finishIndex) {
        int localMin = Integer.MAX_VALUE;
        int localMinIndex = -1;

        for (int i = startIndex; i < finishIndex; i++) {
            if (arr[i] < localMin) {
                localMin = arr[i];
                localMinIndex = i;
            }
        }

        synchronized (lock) {
            if (localMin < min) {
                min = localMin;
                minIndex = localMinIndex;
            }
            threadCount++;
            if (threadCount == threadNum) {
                synchronized (this) {
                    notify();
                }
            }
        }
    }


    synchronized public int[] getResult() {
        while (threadCount < threadNum) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new int[]{min, minIndex};
    }


    public int[] findMin() {
        int chunkSize = arr.length / threadNum;
        int remaining = arr.length % threadNum;
        int startIndex = 0;

        for (int i = 0; i < threadNum; i++) {
            int endIndex = startIndex + chunkSize;
            if (remaining > 0) {
                endIndex++;
                remaining--;
            }
            new Thread(new ThreadMin(startIndex, endIndex, this)).start();
            startIndex = endIndex;
        }

        return getResult();
    }
}
