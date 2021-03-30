package com.in28minutes.rest.webservices.restfulwebservices.user;

import java.net.URI;


import java.util.List;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.EntityModel;
//import org.springframework.hateoas.Link;
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserResource {

	@Autowired
	private UserDaoService service;

	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	@GetMapping("/users/{id}")
	public User retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);		
		if(user==null)
			throw new UserNotFoundException("id-"+ id);
		return user;
	}
	
//	//using HATEOAS
//	@GetMapping(path = "/users/{id}")
//    public EntityModel<User> retrieveUser(@PathVariable int id) {
//        User user = service.findOne(id);
//        if (null == user)
//            throw new UserNotFoundException("id-" + id);
//
//        EntityModel<User> entityModel = EntityModel.of(user);
//        Link link= WebMvcLinkBuilder.linkTo(
//                methodOn(this.getClass()).retrieveAllUsers()).withRel("all-users");
//        entityModel.add(link);
//        return entityModel;
//    }

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		User user = service.deleteById(id);
		
		if(user==null)
			throw new UserNotFoundException("id-"+ id);		
	}

	// input - details of user
	// output - CREATED & Return the created URI
	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Validated @RequestBody User user) {
		User savedUser = service.save(user);
		// CREATED
		// /users/{id}     savedUser.getId()
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(savedUser.getId()).toUri();
		
		return ResponseEntity.created(location).build();
		
	}
}
