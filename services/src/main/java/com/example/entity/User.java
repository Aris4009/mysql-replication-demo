package com.example.entity;

import org.beetl.sql.annotation.entity.Auto;
import org.beetl.sql.annotation.entity.Table;

@Table(name = "user")
public class User {

	@Auto
	private Integer id;

	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
