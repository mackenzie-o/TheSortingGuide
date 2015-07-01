package com.msu_software_factory.thesortingguide;

import java.util.Random;

public class Sorting {
    static Random randomgenerator = new Random();

    public static int[] randlist(int size){
        int[] newlist = new int[size];
        for (int i = 0; i < newlist.length; i++) {
            int randomInt = randomgenerator.nextInt(50);
            newlist[i] = randomInt;
        }
        return newlist;
    }
    public static String toString(int[] list){
        String out = "[ ";
        for (int i = 0; i < list.length; i++) {
            out += list[i] + " ";
        }
        return out + "]";
    }
    public static int[] bubblesort(int[] unsorted){
        for (int i = 1; i < unsorted.length; i++) {
            for (int j = 0; j < unsorted.length -i; j++) {
                if (unsorted[j] > unsorted[j+1]){
                    int temp = unsorted[j];
                    unsorted[j] = unsorted[j+1];
                    unsorted[j+1] = temp;
                }
            }
        }
        return unsorted;
    }
    public static void selectionsort(int[] unsorted){
        for (int i = 0; i < unsorted.length -2; i++) {
            int min = i;
            for (int j = i +1; j <= unsorted.length - 1; j++) {
                if (unsorted[j] < unsorted[min]){
                    min = j;
                }
            }
            if (min != i){
                int temp = unsorted[min];
                unsorted[min] = unsorted[i];
                unsorted[i] = temp;
            }
        }
    }
    public static void insertionsort(int[] unsorted){
        for (int i = 1; i < unsorted.length; i++) {
            int temp = unsorted[i];
            int j;
            for (j = i-1; j >= 0 && temp < unsorted[j]; j--) {
                unsorted[j+1] = unsorted[j];
            }
            unsorted[j+1] = temp;
        }
    }
}