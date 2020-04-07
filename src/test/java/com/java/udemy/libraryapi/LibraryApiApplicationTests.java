package com.java.udemy.libraryapi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class LibraryApiApplicationTests {

	@Test
	public void setUp() {
		LibraryApiApplication library = new LibraryApiApplication();
	}

}
