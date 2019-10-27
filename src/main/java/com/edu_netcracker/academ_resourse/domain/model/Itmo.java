package com.edu_netcracker.academ_resourse.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Itmo implements University{
    private String group;
    private String schedule;

    public Itmo() {
    }

    public Itmo(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSchedule() {
        return this.schedule;
    }

    @Override
    public String getUrl() {
        return "http://www.ifmo.ru/ru/schedule/0/" + group.toUpperCase() + "/raspisanie_zanyatiy_" + group.toUpperCase() + ".htm";
    }

    @Override
    public String getQuery() {
        return "div.rasp_tabl_day";
    }
}
