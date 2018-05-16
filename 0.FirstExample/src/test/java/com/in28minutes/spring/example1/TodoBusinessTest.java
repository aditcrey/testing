package com.in28minutes.spring.example1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Configuration
@ComponentScan(basePackages = { "com.in28minutes" })
class SpringContext {
}

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringContext.class)
public class TodoBusinessTest {
	
	TodoBusinessService businessService = new TodoBusinessService();
	
	
	
	
	@Test
	public void testGetTodosAboutSpring() {
		List<String> todos = businessService.retrieveTodosRelatedToSpring("Ranga");
		assertEquals(1,todos.size());  //checks that only one item is returned
		assertEquals("Learn Spring",todos.get(0));
		
	}
}


class TodoBusinessService{
	
	
	//TodoBusinessService needs TodoDataService to do it's work and hence it's a dependency
	TodoDataService dataService = new TodoDataService(); //here TodoBusinessService is tightly coupled with TodoDataService
	//Suppose the database went down then you can no longer retrieve data from the database
	//and if we run junit test then it'll show fail...since the database is down...this is the problem with tightly coupled classes
	//as a developer, we don't want to write 1000s of lines of code and test all of them together
	//so I want to focus on writing TodoBusinessService without caring for what the dataService is doing
	//solution to this can be creation of stub
	//so instead of writing TodoDataService, we would write TodoDataServiceStub and use that as below
	
//	TodoDataServiceStub dataService = new TodoDataServiceStub();
	
	//Note: we use stub only while testing...when we deploy it to the server, we use the real classes only
	
	
	
	List<String> retrieveTodosRelatedToSpring(String user){
		
		List<String> todosRelatedToSpring = new ArrayList<String>();
		
		List<String> todos = dataService.retrieveTodos(user);
		
		for(String todo: todos){
			if(todo.contains("Spring")){
				todosRelatedToSpring.add(todo);
			}
		}
		
		
		return todosRelatedToSpring;
	}
}

class TodoDataServiceStub{ //so we don't want this stub to talk to the database but instead return some dummy values
	
	List<String> retrieveTodos(String user){
		return Arrays.asList("Learn Spring","Learn Struts","Learn to Dance");
		
	}

}



class TodoDataService{
		
	List<String> retrieveTodos(String user){
		return Database.retrieveTodos(user);
		
	}

}


class Database{
//	static List<String> retrieveTodos(String user){
//		return Arrays.asList("Learn Spring","Learning Struts","Learn to Dance");
//	}
	
	
	//simulating that database went down (to show the problems with style of programming before the intro of spring)
	static List<String> retrieveTodos(String user){
		throw new RuntimeException("Database Down");
//	return Arrays.asList("Learn Spring","Learning Struts","Learn to Dance");
}
	
	
	
}