package com.example.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dao.UserDao;
import com.example.entity.User;

@RestController
@RequestMapping("/api/test")
public class TestController {

	private UserDao userDao;

	public TestController(UserDao userDao) {
		this.userDao = userDao;
	}

	@GetMapping("/insert")
	public void insert() {
		User user = new User();
		user.setName(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
		userDao.insert(user);
	}

	@GetMapping("/select")
	public ResponseEntity<List<User>> select() {
		List<User> list = userDao.all();
		return ResponseEntity.ok(list);
	}
}
