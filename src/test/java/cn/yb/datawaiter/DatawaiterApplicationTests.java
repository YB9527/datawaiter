package cn.yb.datawaiter;


import org.junit.jupiter.api.Test;

import java.io.*;


class DatawaiterApplicationTests {


    @Test
    void contextLoads() throws FileNotFoundException {
        int[] arr = new int[]{9,5,4,6,8,7,3,1,2};

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i-1; j++) {
                if(arr[j] < arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }


}
