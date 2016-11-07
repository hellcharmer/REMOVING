package com.example.charmer.moving.pojo;

import java.util.Date;

public class Exercises {


	private Long exerciseId;
    private Long publisherId;
    private String exerciseTitle;
    private String exerciseType;
    private String exerciseTheme;
    private String exerciseIntroduce;
    private String place;
    private Date activityTime;
    private Double cost;
    private String paymentMethod;
    private Integer currentNumber;
    private Integer totalNumber;
    private Date releaseTime;
    private String participator;
    private String groupMembers;
    private String activeState;
    private String enroller;
	private boolean isScan;

	public boolean isScan() {
		return isScan;
	}

	public void setScan(boolean isScan) {
		this.isScan = isScan;
	}


	public Long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public Long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(Long publisherId) {
		this.publisherId = publisherId;
	}

	public String getExerciseTitle() {
		return exerciseTitle;
	}

	public void setExerciseTitle(String exerciseTitle) {
		this.exerciseTitle = exerciseTitle;
	}

	public String getExerciseType() {
		return exerciseType;
	}

	public void setExerciseType(String exerciseType) {
		this.exerciseType = exerciseType;
	}

	public String getExerciseTheme() {
		return exerciseTheme;
	}

	public void setExerciseTheme(String exerciseTheme) {
		this.exerciseTheme = exerciseTheme;
	}

	public String getExerciseIntroduce() {
		return exerciseIntroduce;
	}

	public void setExerciseIntroduce(String exerciseIntroduce) {
		this.exerciseIntroduce = exerciseIntroduce;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	

	public Date getActivityTime() {
		return activityTime;
	}

	public void setActivityTime(Date activityTime) {
		this.activityTime = activityTime;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Integer getCurrentNumber() {
		return currentNumber;
	}

	public void setCurrentNumber(Integer currentNumber) {
		this.currentNumber = currentNumber;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public String getParticipator() {
		return participator;
	}

	public void setParticipator(String participator) {
		this.participator = participator;
	}

	public String getGroupMembers() {
		return groupMembers;
	}

	public void setGroupMembers(String groupMembers) {
		this.groupMembers = groupMembers;
	}

	public String getActiveState() {
		return activeState;
	}

	public void setActiveState(String activeState) {
		this.activeState = activeState;
	}


	public String getEnroller() {
		return enroller;
	}

	public void setEnroller(String enroller) {
		this.enroller = enroller;
	}
	public Exercises() {
    }

	public Exercises( Long publisherId,String exerciseTitle, String exerciseType,
			String exerciseTheme, String place, Double cost, String paymentMethod,
			Integer currentNumber, Integer totalNumber, Date activityTime) {
		super();
		this.publisherId = publisherId;
		this.exerciseTitle = exerciseTitle;
		this.exerciseType = exerciseType;
		this.exerciseTheme = exerciseTheme;
		this.place = place;
		this.cost = cost;
		this.paymentMethod = paymentMethod;
		this.currentNumber = currentNumber;
		this.totalNumber = totalNumber;
		this.activityTime = activityTime;
	}
	public Exercises(Long exerciseId, Long publisherId,String exerciseTitle, String exerciseType,
			String exerciseTheme, String place, Double cost, String paymentMethod,
			Integer currentNumber, Integer totalNumber, Date activityTime) {
		super();
		
		this.exerciseId = exerciseId;
		this.publisherId = publisherId;
		this.exerciseTitle = exerciseTitle;
		this.exerciseType = exerciseType;
		this.exerciseTheme = exerciseTheme;
		this.place = place;
		this.cost = cost;
		this.paymentMethod = paymentMethod;
		this.currentNumber = currentNumber;
		this.totalNumber = totalNumber;
		this.activityTime = activityTime;
	}

//	public Exercises(Long publisherId, String exerciseTitle, String exerciseType, String exerciseTheme, String exerciseIntroduce, String place, Date activityTime, Double cost, String paymentMethod, Integer totalNumber, Date releaseTime, String exerciseCode) {
//		this.publisherId = publisherId;
//		this.exerciseTitle = exerciseTitle;
//		this.exerciseType = exerciseType;
//		this.exerciseTheme = exerciseTheme;
//		this.exerciseIntroduce = exerciseIntroduce;
//		this.place = place;
//		this.activityTime = activityTime;
//		this.cost = cost;
//		this.paymentMethod = paymentMethod;
//		this.totalNumber = totalNumber;
//		this.releaseTime = releaseTime;
//		this.exerciseCode = exerciseCode;
//	}

	public Exercises(Long publisherId, String exerciseTitle, String exerciseType, String exerciseTheme, String exerciseIntroduce, String place, Date activityTime, Double cost, String paymentMethod, Integer totalNumber, Date releaseTime, String participator, String groupMembers, String enroller) {

		this.publisherId = publisherId;
		this.exerciseTitle = exerciseTitle;
		this.exerciseType = exerciseType;
		this.exerciseTheme = exerciseTheme;
		this.exerciseIntroduce = exerciseIntroduce;
		this.place = place;
		this.activityTime = activityTime;
		this.cost = cost;
		this.paymentMethod = paymentMethod;
		this.totalNumber = totalNumber;
		this.releaseTime = releaseTime;
		this.participator = participator;
		this.groupMembers = groupMembers;
		this.enroller = enroller;
	}
}