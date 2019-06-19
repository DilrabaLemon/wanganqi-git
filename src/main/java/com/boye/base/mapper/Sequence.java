package com.boye.base.mapper;

public class Sequence {

	private String name;

	private int length;

	public Sequence() {
		super();
	}

	public static Sequence build(String name, int length) {
		Sequence seq = new Sequence();
		seq.name = name;
		seq.length = length;
		return seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
