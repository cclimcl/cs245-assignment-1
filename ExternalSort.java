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

import java.io.*;
import java.util.*;

public class ExternalSort {

    public static void main(String[] args) throws IOException{

        externalSort("input.txt", "output.txt", 9, 3);

    }

    /**
     * Sorts data in input file to temp files of k size and then performs k-mergeSort
     * to sort all temp files
     * @param inputFile
     * @param outputFile
     * @param n
     * @param k
     * @throws IOException
     */
    public static void externalSort(String inputFile, String outputFile, int n, int k) throws IOException{

        File input = new File(inputFile);
        File output = new File(outputFile);
        Scanner scanner = new Scanner(input);

        int num_chunk = (int) Math.ceil((double) n /(double) k);
        int file_num = 0;

        /* Read in a chunk at a time */
        for(int i = 0; i < num_chunk - 1; i ++){
            /* Populate one chunk file */
            double [] temp = new double[k];
            for(int j = 0; j < k; j ++){
                String number = scanner.nextLine();
                double result = Double.parseDouble(number);
                temp[j] = result;
            }
            insertionSort(temp);
            createFile(file_num, temp, k);
            file_num += 1;
        }

        /* Special case: last chunk */
        int size = 0;
        if(n % k == 0){
            size = k;
        } else {
            size = n % k;
        }
        double [] last_chunk = new double[size];
        int last = 0;
        while(scanner.hasNextLine()) {
            /* Populate one chunk file */
            String number = scanner.nextLine();
            double result = Double.parseDouble(number);
            last_chunk[last] = result;
            last += 1;
        }
        insertionSort(last_chunk);
        createFile(file_num, last_chunk, size);

        /* Time to sort the files */
        file_num = 0;
        String filename = "";
        int indices [] = new int[num_chunk];
        int readline [] = new int[num_chunk];
        double temp [] = new double[num_chunk];
        int temp_i = 0;

        /* Initialize indices */
        for(int i = 0; i < num_chunk; i++){
            indices[i] = k;
        }
        /* Last chunk */
        indices[num_chunk - 1] = size;

        /* Initialize temp */
        for(int i = 0; i < num_chunk; i++){
            filename = "temp" + file_num + ".txt";
            File temp_file = new File(filename);
            Scanner temp_s = new Scanner(temp_file);
            file_num += 1;

            String num = temp_s.nextLine();
            double value = Double.parseDouble(num);
            temp[temp_i++] = value;
        }

        kMerge(output, n, k, num_chunk, size);
        System.out.println("Successfully transferred data.");

    }

    /**
     * It is is similar to merge sort but merges from k arrays
     * @param outputFile
     * @param n
     * @param k
     * @param num_chunk
     * @param size
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void kMerge(File outputFile, int n, int k, int num_chunk, int size) throws IOException, FileNotFoundException{
        /* Time to sort the files */
        int file_num = 0;
        String filename = "";
        int indices [] = new int[num_chunk];
        int readline [] = new int[num_chunk];
        double temp [] = new double[num_chunk];
        int temp_i = 0;

        /* Initialize indices */
        for(int i = 0; i < num_chunk; i++){
            indices[i] = k;
        }
        /* Last chunk */
        indices[num_chunk - 1] = size;

        /* Initialize temp */
        for(int i = 0; i < num_chunk; i++){
            filename = "temp" + file_num + ".txt";
            File temp_file = new File(filename);
            Scanner temp_s = new Scanner(temp_file);
            file_num += 1;

            String num = temp_s.nextLine();
            double value = Double.parseDouble(num);
            temp[temp_i++] = value;
        }

        /* K-merge Sort */
        /* This keeps track of how many files have been sorted into the output file */
        int num_sorted = 0;
        int last_file = 0;
        int last_readline = 0;
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");

        while(num_sorted < num_chunk){

            /* Find the smallest value in temp */
            int index = findMin(temp);
            /* Append it to output file */
            writer.println(temp[index]);

            /* Increment the index for that file and replace the temp*/
            indices[index]  -= 1;
            readline[index] += 1;
            /* If everything in file_index has been outputted, we don't want to access it */
            if(indices[index] <= 0){
                num_sorted += 1;
                temp[index] = -1;
            } else {
                filename = "temp" + index + ".txt";
                File temp_file = new File(filename);
                Scanner temp_s = new Scanner(temp_file);
                String num = "";
                /* Keeps track of which line to read in the file */
                for(int i = 0; i <= readline[index]; i++){
                    num = temp_s.nextLine();
                }
                double value = Double.parseDouble(num);
                /* Update the value of temp */
                temp[index] = value;
            }

            last_file = index;
            last_readline = readline[index];
        }
        writer.close();
    }

    /**
     * Returns index of min value in the array
     * @param a
     * @return
     */
    public static int findMin(double [] a){
        int index = 0;
        while(a[index] == -1){
            index += 1;
        }
        for(int i = index + 1; i < a.length; i++){
            if(a[i] == -1.0){
                /* We want to skip it */
            }else if(a[i] < a[index]){
                index = i;
            }
        }
        return index;
    }

    /**
     * InsertionSort to sort temp files
     * @param arr
     */
    public static void insertionSort(double [] arr){

        for(int i = 1; i < arr.length; i++){
            double next = arr[i];
            int j = i  - 1;
            /* Only is the temp is smaller than value at index,
             * it pushes the numbers back to insert temp at that index
             */
            while(j >= 0 && next < arr[j]){
                arr[j + 1] = arr[j];
                j -= 1;
            }
            arr[j + 1] = next;
        }

    }

    /**
     * Create and writes to temp files
     * @param file_num
     * @param arr
     * @param k
     * @throws IOException
     */
    public static void createFile(int file_num, double [] arr, int k) throws IOException{

        /* Create the file */
        String filename = "temp" + file_num + ".txt";
        File new_file = new File(filename);
        new_file.createNewFile();
        System.out.println("Successfully created " + filename);

        /* Write into the file */
        FileWriter file_writer = new FileWriter(filename);
        for(int i = 0; i < k; i++){
            file_writer.write(arr[i] + "\n");
        }
        file_writer.close();
        System.out.println("Successfully wrote to " + filename + "\n");

    }

}