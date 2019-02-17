package com.example.rest.microservices.restfulwebservices.user;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.rest.microservices.restfulwebservices.exceptions.UserNotFoundException;

@RestController
public class UserJPAResource {
	
	@Autowired
	private UserRepository userRepository; 
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping(path="/jpa/users")
	public List<User> retrieveAllUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping(path="/jpa/users/{id}")
	public Resource<Optional<User>> retrieveUser(@PathVariable int id) throws UserNotFoundException{
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id = "+id);
		}
		Resource<Optional<User>> resource = new Resource<Optional<User>>(user);
		Link link = linkTo(methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
		resource.add(link);
		link = linkTo(methodOn(this.getClass()).retrieveUser(id)).withSelfRel();
		resource.add(link);
		return resource;
	}
	
	// input - details of user
	// Output - CREATED & User
	@PostMapping(path="/jpa/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);
		//CREATED
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping(path="/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) throws UserNotFoundException {
		userRepository.deleteById(id);
	}
	
	@GetMapping(path="/jpa/users/{id}/posts")
	public List<Post> retrieveAllPosts(@PathVariable int id) throws UserNotFoundException{
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			throw new UserNotFoundException("id = "+id);
		}
		return user.get().getPosts();
	}
	
	@PostMapping(path="/jpa/users/{id}/posts")
	public ResponseEntity<?> createPost(@PathVariable int id, @RequestBody Post post) throws UserNotFoundException {
		Optional<User> userOptional = userRepository.findById(id);
		if(!userOptional.isPresent()) {
			throw new UserNotFoundException("id = "+id);
		}
		User user = userOptional.get();
		post.setUser(user);
		postRepository.save(post);
		//CREATED
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(post.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
