package com.tournaments.tournamentsbe.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Player {
    @Id
    @GeneratedValue
    private Long id;
    private String firstName;
    private String lastName;
    /*private Boolean deployed;
  //  private Category category;
    private Long groupPosition;
    private Long overallPosition;
    private Long balls;
    private Long sets;
    private Long points;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

   /* public Boolean getDeployed() {
        return deployed;
    }

    public void setDeployed(Boolean deployed) {
        this.deployed = deployed;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getGroupPosition() {
        return groupPosition;
    }

    public void setGroupPosition(Long groupPosition) {
        this.groupPosition = groupPosition;
    }

    public Long getOverallPosition() {
        return overallPosition;
    }

    public void setOverallPosition(Long overallPosition) {
        this.overallPosition = overallPosition;
    }

    public Long getBalls() {
        return balls;
    }

    public void setBalls(Long balls) {
        this.balls = balls;
    }

    public Long getSets() {
        return sets;
    }

    public void setSets(Long sets) {
        this.sets = sets;
    }

    public Long getPoints() {
        return points;
    }

    public void setPoints(Long points) {
        this.points = points;
    }*/
}
