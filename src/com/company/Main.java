package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter the learning coefficient for the perceptron: ");
        double learningCoefficient = Double.parseDouble(reader.readLine());

        Model model = new Model("training.txt", "training.txt", learningCoefficient);

        while (true) {
            System.out.println("Please enter a vector to classify: ");
            String[] vectorString = reader.readLine().replaceAll(" ", "").split(",");
            try {
                List<Double> coordinates = new ArrayList<>();
                for (int i = 0; i < vectorString.length; i++) {
                    coordinates.add(Double.parseDouble(vectorString[i]));
                }
                Vector vector = new Vector(coordinates);
                System.out.println("Your Iris was classified as: " + model.classify(vector));
            } catch (Exception e) {
                System.out.println("Please enter a valid vector for the supplied training set.\nComma-separated ints or doubles, no repeated spaces.");
            }
        }
    }
}
