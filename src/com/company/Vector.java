package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vector {

    private String vectorClass;
    private List<Double> coordinates;
    private int dimension;


    public Vector(List<Double> coordinates, String vectorClass) {
        this.vectorClass = vectorClass;
        this.coordinates = coordinates;
        this.dimension = coordinates.size();
    }

    public Vector(List<Double> coordinates) {
        this(coordinates, null);
    }

    //Initializes a zero vector in R defined by dimension
    public Vector(int dimension) {
        ArrayList<Double> coordinates = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            coordinates.add(0.0);
        }
        this.vectorClass = null;
        this.coordinates = coordinates;
        this.dimension = dimension;
    }

    public String getVectorClass() {
        return this.vectorClass;
    }

    public int getDimension() {
        return dimension;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setVectorClass(String vectorClass) {
        this.vectorClass = vectorClass;
    }

    public Vector sum(Vector v) {
        if (getDimension() == v.getDimension()) {
            for (int i = 0; i < getDimension(); i++) {
                v.getCoordinates().set(i, (v.getCoordinates().get(i) + getCoordinates().get(i)));
            }
            return v;
        } else throw new RuntimeException("Invalid vector dimension.");
    }

    public double dotProduct(Vector v) {
        if (this.getDimension() == v.getDimension()) {
            ArrayList<Double> resultVectorCoordinates = new ArrayList<>();
            for (int i = 0; i < this.getDimension(); i++) {
                resultVectorCoordinates.add(v.getCoordinates().get(i) * this.getCoordinates().get(i));
            }
            return resultVectorCoordinates.stream().reduce((a1, a2) -> a1 + a2).get();
        } else throw new RuntimeException("Invalid vector dimension.");
    }

    public Vector multiply(double coefficient) {
            Vector vector = new Vector(new ArrayList<>(this.getCoordinates()));
            for (int i = 0; i < this.getDimension(); i++) {
                vector.getCoordinates().set(i, (vector.getCoordinates().get(i) * coefficient));
            }
            return vector;
    }


    public Vector clone() {
        return new Vector(this.coordinates, this.vectorClass);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "vectorClass='" + vectorClass + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return Objects.equals(vectorClass, vector.vectorClass) &&
                Objects.equals(coordinates, vector.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vectorClass, coordinates);
    }
}

