package com.example.charmer.moving.pojo;

import java.util.ArrayList;

/**
 * Created by Charmer on 2016/9/14.
 */
public class VariableExercise {
    public int status;
    public int totalPage;
    public ArrayList<Exercises> exerciseList;

    public static class Exercises{
        public Long exerciseId;
        public Long publisherId;
        public String userImage;
        public String title;
        public String type;
        public String theme;
        public String place;
        public Double cost;
        public String paymentMethod;
        public Integer currentNumber;
        public Integer totalNumber;
        public String activityTime;
        public String exerciseIntroduce;
        public String releaseTime;
        @Override
        public String toString() {
            return "Exercises{" +
                    "exerciseId=" + exerciseId +
                    ", publisherName='" + publisherId + '\'' +
                    ", userImage='" + userImage + '\'' +
                    ", type='" + type + '\'' +
                    ", theme='" + theme + '\'' +
                    ", place='" + place + '\'' +
                    ", cost=" + cost +
                    ", paymentMethod='" + paymentMethod + '\'' +
                    ", currentNumber=" + currentNumber +
                    ", totalNumber=" + totalNumber +
                    ", activityTime='" + activityTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "VariableExercise{" +
                "status=" + status +
                ", exerciseList=" + exerciseList +
                '}';
    }
}
