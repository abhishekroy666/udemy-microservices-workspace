package com.example.rest.microservices.restfulwebservices.user;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.rest.microservices.restfulwebservices.exceptions.UserNotFoundException;

import java.net.URI;

@RestController
public class UserResource {
	
	@Autowired
	UserDaoService dao;
	
	@GetMapping(path="/users")
	public List<User> retrieveAllUsers(){
		return dao.findAll();
	}
	
	@GetMapping(path="/users/{id}")
	public Resource<User> retrieveUser(@PathVariable int id) throws UserNotFoundException{
		User user = dao.findOne(id);
		if(user==null) {
			throw new UserNotFoundException("id = "+id);
		}
		Resource<User> resource = new Resource<User>(user);
		Link link = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
		resource.add(link);
		link = linkTo(methodOn(this.getClass()).retrieveUser(id)).withSelfRel();
		resource.add(link);
		return resource;
	}
	
	// input - details of user
	// Output - CREATED & User
	@PostMapping(path="/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		User savedUser = dao.save(user);
		//CREATED
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/users/{id}")
	public void deleteUser(@PathVariable int id) throws UserNotFoundException {
		User user = dao.deleteById(id);
		if(user==null) {
			throw new UserNotFoundException("id = "+id);
		}
	}
}
