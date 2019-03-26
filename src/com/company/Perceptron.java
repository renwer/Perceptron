package com.company;

import java.util.ArrayList;
import java.util.List;

public class Perceptron {

    private Vector weightsVector;
    private double learningRate;

    public Perceptron(int inputs, double learningRate, double threshold) {
        this.learningRate = learningRate;
        this.weightsVector = initWeightsVector(inputs); //initializing in R+1 dimension to keep Theta as the last coordinate
        this.weightsVector.getCoordinates().set(weightsVector.getCoordinates().size() - 1, threshold); //setting the Theta
    }

    public int activate(Vector inputVector) { //the input vector should be in R+1 dimension as well to keep -1 as the last coordinate
        List<Double> coordinatesWithMinusOne = new ArrayList<>(inputVector.getCoordinates());
        coordinatesWithMinusOne.add(-1.0);
        Vector modifiedInput = new Vector(coordinatesWithMinusOne);
        Vector weightsVectorCopy = this.weightsVector.clone();
        double dotProduct = weightsVectorCopy.dotProduct(modifiedInput);

        //System.out.println("Weights vector condition: " + weightsVector);
        return dotProduct > 0 ? 1 : 0;
    }

    private Vector initWeightsVector(int dimension) {
        Vector result = new Vector(dimension + 1);
        for (int i = 0; i < result.getCoordinates().size(); i++) {
            result.getCoordinates().set(i, 0.1); //Math.random() * 10
        }
        return result;
    }

    public void train(Vector inputVector, int expected) {
        int actual = activate(inputVector);
        List<Double> coordinatesWithMinusOne = new ArrayList<>(inputVector.getCoordinates());
        coordinatesWithMinusOne.add(-1.0);
        Vector modifiedInput = new Vector(coordinatesWithMinusOne);
//        System.out.println("Modified input vector: " + modifiedInput);
//        System.out.println("Weights vector: " + this.weightsVector);
        this.weightsVector = this.weightsVector.sum(modifiedInput.multiply(learningRate * (expected - actual)));
    }
}
