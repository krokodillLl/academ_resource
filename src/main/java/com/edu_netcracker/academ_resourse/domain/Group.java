package com.edu_netcracker.academ_resourse.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "grp")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="univ_id")
	private University university;

//  private int taskId;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
//	@JoinColumn(name ="group_fk)
//	private Set<User> users;

    public Group(){

	}

	public Group(String name) {
		this.name = name;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String groupName) {
		this.name = name;
	}

	public University getUniversity() {
		return university;
	}

	public void setUniversity(University university) {
		this.university = university;
	}
	public String getUniversityName(){
		return university !=null ? university.getName() : "<none>";
	}
}
