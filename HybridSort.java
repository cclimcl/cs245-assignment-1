/**
 *
 * This program creates a hybrid sort using randomized quickSort and
 * insertionSort to sort an unsorted array
 *
 * @author Chiara Lim
 *
 * Sources: https://www.geeksforgeeks.org/quicksort-using-random-pivoting/
 *
 **/

import java.util.*;
import java.io.*;

public class HybridSort {

    public static void main(String[] args) throws FileNotFoundException{

        File data = new File("big_data.txt");
        Scanner scanner = new Scanner(data);

        double [] arr = new double[1000000];

        int index = 0;
        while(scanner.hasNextLine()){

            String number = scanner.nextLine();
            int result = Integer.parseInt(number);
            arr[index++] = result;

        }

        hybridSort(arr, 0, arr.length - 1);

        /* Output the sorted list to standard out (the terminal) */
        for(int i = 0; i < arr.length; i++){
            System.out.println(arr[i]);
        }
        System.out.println("HybridSort Completed.");
    }

    /**
     * This function swaps to elements at the given index
     *
     * @param arr
     * @param i
     * @param j
     * @return void
     **/
    public static void swap(double [] arr, int i, int j){
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Acts as partition() and returns index of pivot
     *
     * @param arr
     * @param start
     * @param end
     **/
    public static int quickSort(double [] arr, int start, int end){
        /* Create instance of Random class */
        Random rand = new Random();
        int rand_num = rand.nextInt(end - start + 1) + start;

        swap(arr, rand_num, end);

        int next_pivot = start;
        int curr_pivot = end;
        double pivot = arr[curr_pivot];

        /* Marks out where the pivot index should be */
        for(int i = start; i < end; i++){
            if(arr[i] <= pivot) {
                swap(arr, i, next_pivot);
                next_pivot += 1;
            }
        }
        swap(arr, next_pivot, curr_pivot);

        return next_pivot;
    }

    /**
     * InsertionSort
     * @param arr
     * @param left
     * @param right
     */
    public static void quadraticSort(double [] arr, int left, int right){
        /* Insertion sort */
        for(int i = 1 + left; i <= right; i++){
            double next = arr[i];
            int j = i  - 1;
            /* Only is the temp is smaller than value at index, it pushes
             * the numbers back to insert temp at that index
             */
            while(j >= 0 && next < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = next;
        }
    }

    /**
     * Combination of randomized QuickSort and InsertionSort
     * @param arr
     * @param left
     * @param right
     */
    public static void hybridSort(double [] arr, int left, int right){
        /* This is the arr length to which I determine whether to use quickSort or InsertionSort */
        int length = 5000;

        /* Quadratic Sort Method */
        if((right - left) < length){
            quadraticSort(arr, left, right);
        } else {
                int pivot_index = quickSort(arr, left, right);
                hybridSort(arr, left, pivot_index - 1);
                hybridSort(arr, pivot_index + 1, right);
        }
    }

}