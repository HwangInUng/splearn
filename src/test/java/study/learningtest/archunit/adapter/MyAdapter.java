package study.learningtest.archunit.adapter;

import study.learningtest.archunit.application.MyService;

public class MyAdapter {
	MyService myService;

	void run () {
		myService = new MyService();
		System.out.println(myService);
	}
}
