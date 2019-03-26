package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the learning coefficient for the perceptron: ");
        double learningCoefficient = Double.parseDouble(reader.readLine());

        Model model = new Model("training.txt", "training.txt", learningCoefficient);

        //System.out.println(model.classify(new Vector(Arrays.asList(5.0, 5.0, 5.0, 5.0))));
    }
}
