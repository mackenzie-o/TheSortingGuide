package com.msu_software_factory.thesortingguide;

import java.util.LinkedList;
import java.util.Random;

public class Sorting {
    static Random randomgenerator = new Random();
    static SortSteps sortSteps;
    static int sortSize = 6;

    public static int[] randList(int size){
        int[] newlist = new int[sortSize];
        for (int i = 0; i < newlist.length; i++) {
            int randomInt = randomgenerator.nextInt(99);
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

    // BUBBLE
    public static int[] bubbleSort(int[] unsorted){
        sortSteps = new SortSteps();
        Step step;
        for (int i = 1; i < unsorted.length; i++) {
            for (int j = 0; j < unsorted.length -i; j++) {
                if (unsorted[j] > unsorted[j+1]){
                    step = new Step();
                    step.arrayBefore = unsorted.clone();
                    step.type = "swap";
                    step.start = j;
                    step.end = j+1;
                    int temp = unsorted[j];
                    unsorted[j] = unsorted[j+1];
                    unsorted[j+1] = temp;
                    step.arrayAfter = unsorted.clone();
                    sortSteps.addStep(step);
                } else {
                    step = new Step();
                    step.arrayBefore = step.arrayAfter = unsorted.clone();
                    step.type = "compare";
                    step.start = j;
                    step.end = j+1;
                    sortSteps.addStep(step);
                }
            }
        }
        return unsorted;
    }

    // SELECTION
    public static int[] selectionSort(int[] unsorted){
        sortSteps = new SortSteps();
        Step step;
        for (int i = 0; i < unsorted.length; i++) {
            int min = i;
            for (int j = i +1; j <= unsorted.length - 1; j++) {
                if (unsorted[j] < unsorted[min]){
                    min = j;
                }
                step = new Step();
                step.arrayBefore = step.arrayAfter = unsorted.clone();
                step.type = "compare";
                step.start = j;
                step.end = min;
                sortSteps.addStep(step);
            }
            if (min != i){
                step = new Step();
                step.arrayBefore = unsorted.clone();
                step.type = "swap";
                step.start = min;
                step.end = i;
                int temp = unsorted[min];
                unsorted[min] = unsorted[i];
                unsorted[i] = temp;
                step.arrayAfter = unsorted.clone();
                sortSteps.addStep(step);
            }
        }
        return unsorted;
    }

    // INSERTION
    public static int[] insertionSort(int[] unsorted){
        sortSteps = new SortSteps();
        Step step;
        for (int i = 1; i < unsorted.length; i++) {
            for(int j = i; j > 0 && unsorted[j] < unsorted[j-1]; j--) {
                step = new Step();
                step.arrayBefore = unsorted.clone();
                step.type = "swap";
                step.start = j;
                step.end = j-1;
                int temp = unsorted[j];
                unsorted[j] = unsorted[j-1];
                unsorted[j-1] = temp;
                step.arrayAfter = unsorted.clone();
                sortSteps.addStep(step);
            }
        }
        return unsorted;
    }
}

class SortSteps {
    LinkedList<Step> steps;

    public SortSteps() {
        steps = new LinkedList<Step>();
    }

    public void addStep(Step next) {
        steps.add(next);
    }

    public String toString() {
        String out = "Steps:";
        for (Step step : steps) {
            out += "\n\t" + step;
        }
        return out;
    }
}

class Step {
    // array before and array after can be removed later, but for now they are helpful for debuging
    int[] arrayBefore;
    int[] arrayAfter;
    // start and end are the starting and ending indexs of the moving value
    int start;
    int end;

    String type;

    public Step() {}

    public String toString() {
        String out = "Step:";
        out += "\n\t " + Sorting.toString(arrayBefore);
        out += "\n\t " + Sorting.toString(arrayAfter);
        out += "\n\t " + type;
        out += "\n\t Start: " + start;
        out += "\n\t End:   " + end;

        return out;
    }
}