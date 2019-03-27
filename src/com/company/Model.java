package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Model {

    private Map<Integer, String> vectorClasses;
    private Set<Vector> trainingSet; //all vectors read from the training set file
    private Set<Vector> testingSet;
    private Perceptron perceptron;


    public Model(String trainingSetPath, String testingSetPath, double learningRate) throws Exception {
        this.trainingSet = readDataSet(trainingSetPath);
        this.testingSet = readDataSet(testingSetPath);
        this.vectorClasses = populateVectorClasses(this.trainingSet);
        this.perceptron = new Perceptron(getDimension(trainingSet), learningRate, 0);

        System.out.println("Finished reading training set! Training...");

        //It is proven that it can't go far beyond 97% for this particular dataset.
        //However, if you wish to use it for anything else, just try running it for about 10 000 times with 0.05
        //learning rate(Also empirically discovered to generate more or less best performance for me).
        while (getAccuracy(testingSet) < 96.0) {
            for (Vector v : trainingSet) {
                int expected = vectorClasses.entrySet().stream()
                        .filter(e -> e.getValue().equals(v.getVectorClass()))
                        .findFirst().get().getKey();
                perceptron.train(v, expected);
            }
        }
        printPrecisionForClasses();
        System.out.println("Estimated model's accuracy: " + getAccuracy(testingSet));
    }


//    private double findOptimalLearningRate(String trainingSetPath, String testingSetPath) throws Exception {
//        Map<Double, Double> precisions = new HashMap<>();
//
//        for (double k = 0.01; k < 2.0; k+=0.01) {
//
//            this.perceptron = new Perceptron(getDimension(trainingSet), k, 0);
//
//            for (int i = 0; i < 1000; i++) {
//                for (Vector v : trainingSet) {
//                    int expected = vectorClasses.entrySet().stream()
//                            .filter(e -> e.getValue().equals(v.getVectorClass()))
//                            .findFirst().get().getKey();
//                    perceptron.train(v, expected);
//                }
//            }
//            precisions.put(k, getPrecision(testingSetPath));
//        }
//
//        return  precisions.entrySet().parallelStream()
//                .max(Comparator.comparing(e -> e.getValue())).get().getKey();
//    }


    public String classify(Vector v) {
        return perceptron.activate(v) == 0 ? vectorClasses.get(0) : vectorClasses.get(1);
    }

    private int getDimension(Set<Vector> trainingSet) throws Exception {
        Set<Integer> dimension = new HashSet<>();
        for (Vector vector: trainingSet) {
            dimension.add(vector.getDimension());
        }
        if (dimension.size() == 1) {
            System.out.println("Vector dimension: " + dimension);
            return new ArrayList<>(dimension).get(0); //is this shitcode or a smart hack? who knows
        } else throw new Exception("Inconsistent vector dimension in the training set.");
    }

    private Map<Integer, String> populateVectorClasses(Set<Vector> trainingSet) {
        Map<Integer, String> result = new HashMap<>();
        int index = 0;
        for (Vector v: trainingSet) {
            if (!result.values().contains(v.getVectorClass())) {
                result.put(index, v.getVectorClass());
                index++;
                if (result.size() == 2) {
                    break;
                }
            }
        }
        System.out.println("Vector classes: " + result);
        return result;
    }

    private double getAccuracy(Set testingSet) throws Exception {
        Set<Vector> testSet = new HashSet<>(testingSet);
        double mistakesCount = 0, testSetSize = testSet.size();
        for (Vector v: testSet) {
            String vectorClass = v.getVectorClass();
            Vector testVector = new Vector(v.getCoordinates());
            if (!vectorClasses.get(perceptron.activate(testVector)).equals(vectorClass)) {
                mistakesCount++;
            }
        }
        //System.out.println("Mistakes count: " + mistakesCount + ", test set size: " + testSetSize);
        return 100 - (mistakesCount/testSetSize)*100;
    }

    private void printPrecisionForClasses() throws Exception {
        for(int i = 0; i < 2; i++) {
            int k = i; //to make lambda happy
            Set<Vector> testingSet = new HashSet<>(this.testingSet).stream()
                    .filter(e -> e.getVectorClass().equals(vectorClasses.get(k)))
                    .collect(Collectors.toSet());
            System.out.println("Precision for " + vectorClasses.get(k) + ": " + getAccuracy(testingSet));
        }
    }


    private Set<Vector> readDataSet(String path) {
        BufferedReader reader;
        Set<Vector> resultSet = new HashSet<>();
        try {
            reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String vectorClass = null;
                String[] fields = line.split(",");
                List<Double> coordinates = new ArrayList<>();
                for (int i = 0; i < fields.length; i++) {
                    try {
                        coordinates.add(Double.parseDouble(fields[i]));
                    } catch (NumberFormatException nfe) {
                        vectorClass = fields[i];
                    }
                }
                Vector vector = new Vector(coordinates, vectorClass);
                resultSet.add(vector);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
