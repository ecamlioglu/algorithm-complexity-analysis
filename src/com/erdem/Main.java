package com.erdem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int m = 0;
        int n = 0;
        if (args.length > 0) {
            Scanner reader = new Scanner(new FileInputStream(args[0]));
            var inputLine = reader.nextLine();
            var inputs = inputLine.toCharArray();
            if (inputs.length < 3) {
                System.out.println("Error... Check txt file again.");
            } else {
                m = Integer.parseInt(String.valueOf(inputs[0]));
                n = Integer.parseInt(String.valueOf(inputs[2]));
                System.out.println("Matrix size in txt file: " + m + "-" + n);
            }
        } else {
            System.out.println("Unknown TXT file. Default matrix size 5x5. Running...");
            m = 5;
            n = 5;
        }
        Neighbourhood[][] grid = CreateMap(m, n);
        final long startTime1 = System.nanoTime();
        FindTop(grid);
        final long endTime1 = System.nanoTime();
        System.out.println("Exec. Time For Part-1 Question : " + (endTime1 - startTime1));

        final long startTime2 = System.nanoTime();
        var topmostNeighbourhood = GetTopNeighbourhoods(grid);
        final long endTime2 = System.nanoTime();
        System.out.println("Exec. Time For Part-2 Question : " + (endTime2 - startTime2));

        final long startTime3 = System.nanoTime();
        var topeesNeighbourhoodDQ = GetTopsInNeighbourhoodsDQ(grid);
        final long endTime3 = System.nanoTime();
        System.out.println("Exec. Time For Part-3 Question : " + (endTime3 - startTime3));
    }

    public static Neighbourhood[][] CreateMap(int m, int n) {
        Neighbourhood[][] neighbourhoods = new Neighbourhood[m][n];

        Random rand = new Random();

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                neighbourhoods[i][j] = new Neighbourhood();
                neighbourhoods[i][j].Coordinates = new Neighbourhood.Pair(i, j);
                neighbourhoods[i][j].EffectedCount = rand.nextInt(10);
            }
        }
        return neighbourhoods;
    }

    // 1

    public static void FindTop(Neighbourhood[][] grid) {
        var x = grid.length;
        var y = grid[0].length;

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                grid[i][j].IsTop = FindNeighbourSum(grid, i, j);
            }
        }
    }

    public static boolean FindNeighbourSum(Neighbourhood[][] grid, int x, int y) {
        var westEffected = x - 1 >= 0 ? grid[x - 1][y].EffectedCount : 0;
        var eastEffected = x + 1 < grid.length ? grid[x + 1][y].EffectedCount : 0;
        var southEffected = y + 1 < grid[0].length ? grid[x][y + 1].EffectedCount : 0;
        var northEffected = y - 1 >= 0 ? grid[x][y - 1].EffectedCount : 0;

        int[] effects = {westEffected, eastEffected, southEffected, northEffected};
        var sortedEffects = bubbleSort(effects);
        return grid[x][y].EffectedCount >= sortedEffects[sortedEffects.length - 1];
    }

    private static int[] bubbleSort(int arr[]) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
        return arr;
    }

    // 2

    public static Neighbourhood GetTopNeighbourhoods(Neighbourhood[][] grid) {
        List<Neighbourhood> topees = new ArrayList<Neighbourhood>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].IsTop = FindNeighbourSum(grid, i, j);
                if (grid[i][j].IsTop) {
                    topees.add(grid[i][j]);
                }
            }
        }

        var sortedArray = bubbleSortForTop(topees);

        return sortedArray.get(sortedArray.size() - 1);
    }

    private static List<Neighbourhood> bubbleSortForTop(List<Neighbourhood> arr) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr.get(j).EffectedCount > arr.get(j + 1).EffectedCount) {
                    var temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);
                }
        return arr;
    }

    // 3

    public static Neighbourhood GetTopsInNeighbourhoodsDQ(Neighbourhood[][] grid) {
        List<Neighbourhood> tops = new ArrayList<Neighbourhood>();

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j].IsTop = FindNeighbourSum(grid, i, j);
                if (grid[i][j].IsTop) {
                    tops.add(grid[i][j]);
                }
            }
        }
        HeapSort(tops);

        return tops.get(tops.size() - 1);
    }

    public static void HeapSort(List<Neighbourhood> arr) {
        int n = arr.size();
        for (int i = n / 2 - 1; i >= 0; i--)
            inHeap(arr, n, i);
        for (int i = n - 1; i > 0; i--) {
            var temp = arr.get(0);
            arr.set(0, arr.get(i));
            arr.set(i, temp);
            inHeap(arr, i, 0);
        }
    }

    public static void inHeap(List<Neighbourhood> arr, int n, int i) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2;
        if (l < n && arr.get(l).EffectedCount > arr.get(largest).EffectedCount)
            largest = l;
        if (r < n && arr.get(r).EffectedCount > arr.get(largest).EffectedCount)
            largest = r;

        if (largest != i) {
            var swap = arr.get(i);
            arr.set(i, arr.get(largest));
            arr.set(largest, swap);
            inHeap(arr, n, largest);
        }
    }
}
