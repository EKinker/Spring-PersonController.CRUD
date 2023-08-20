package io.zipcoder.crudapp.controller;

//Create a PersonController class with Person createPerson(Person p), Person getPerson(int id),
// List<Person> getPersonList(), Person updatePerson(Person p), and void DeletePerson(int id) methods,
// and let it track a list of Person objects.

import io.zipcoder.crudapp.model.Person;
import io.zipcoder.crudapp.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;


    @PostMapping("/people")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person newPerson = personRepository.save(person);
        return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    }

    @GetMapping("/people/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable int id) {
        Person personData = personRepository.findById(id).orElse(null);
        if (personData != null) {
            return new ResponseEntity<>(personData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/people")
    public ResponseEntity<List<Person>> getPersonList() {

        List<Person> personList = new ArrayList<>();
        personRepository.findAll().forEach(personList::add);

        if (personList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(personList, HttpStatus.OK);
    }

    @PutMapping("/people/{id}")
    public ResponseEntity<Person> updatePerson(@RequestBody Person newPersonData) {
        int id = newPersonData.getId();

        if (personRepository.existsById(id)) {
            Person updatedPerson = personRepository.findById(id).orElse(null);
            if (updatedPerson != null) {
                updatedPerson.setFirstName(newPersonData.getFirstName());
                updatedPerson.setLastName(newPersonData.getLastName());


                Person newUpdatedPerson = personRepository.save(updatedPerson);
                return new ResponseEntity<>(newUpdatedPerson, HttpStatus.OK);
            }
        } else if (!personRepository.existsById(id)) {
            personRepository.save(newPersonData);
            return new ResponseEntity<>(newPersonData, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/people/{id}")
    public ResponseEntity<HttpStatus> DeletePerson(@PathVariable int id) {
        personRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
