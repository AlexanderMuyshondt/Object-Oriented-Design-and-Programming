import java.util.*;

public class ParallelMergeSorter
{
    /**
     * Sorts a range of an array, using the merge sort algorithm.
     *
     * @param a the array to sort
     * @param comp the comparator to compare array elements
     */
    public static <E> void sort(E[] a, Comparator<? super E> comp, Integer numCores) {
        parallelMergeSort(a, 0, a.length - 1, comp, numCores);
    }

    /**
     * Sorts a range of an array, using the merge sort algorithm.
     *
     * @param a the array to sort
     * @param from the first index of the range to sort
     * @param to the last index of the range to sort
     * @param comp the comparator to compare array elements
     */
    public static <E> void parallelMergeSort(E[] a, int from, int to,
            Comparator<? super E> comp, Integer numCores) {

    	int mid = (from + to)/2;
            Runnable run1 = new Runnable(){
			@Override
			public void run(){
				parallelMergeSort(a, from, mid, comp, numCores/2);
			}
		};
		Runnable run2 = new Runnable(){
			@Override
			public void run(){
				parallelMergeSort(a, mid + 1, to, comp, numCores/2);
			}
		};

		if (numCores.equals(1)) {
                    MergeSorter.mergeSort(a, from, to, comp);
                }
		else 
		{
                    Thread thread1 = new Thread(run1, "Thread 1");
                    Thread thread2 = new Thread(run2, "Thread 2");

                    thread1.start();
                    thread2.start();

                    try{
			thread1.join();
			thread2.join();
                    }catch(InterruptedException e){}

                    merge(a, from, mid, to, comp);
		}
	
    }

	@SuppressWarnings("unchecked")
    private static <E> void merge(E[] a,
            int from, int mid, int to, Comparator<? super E> comp) {
        int n = to - from + 1;
         // Size of the range to be merged

        // Merge both halves into a temporary array b
        Object[] b = new Object[n];

        int i1 = from;
        // Next element to consider in the first range
        int i2 = mid + 1;
        // Next element to consider in the second range
        int j = 0;
         // Next open position in b

        // As long as neither i1 nor i2 past the end, move
        // the smaller element into b
        while (i1 <= mid && i2 <= to) {
            if (comp.compare(a[i1], a[i2]) < 0) {
                b[j] = a[i1];
                i1++;
            } else {
                b[j] = a[i2];
                i2++;
            }
            j++;
        }

        // Note that only one of the two while loops
        // below is executed
        // Copy any remaining entries of the first half
        while (i1 <= mid) {
            b[j] = a[i1];
            i1++;
            j++;
        }

        // Copy any remaining entries of the second half
        while (i2 <= to) {
            b[j] = a[i2];
            i2++;
            j++;
        }

        // Copy back from the temporary array
        for (j = 0; j < n; j++) {
            a[from + j] = (E) b[j];
        }
    }
	
}
