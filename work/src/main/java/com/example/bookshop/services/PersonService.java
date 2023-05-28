package com.example.bookshop.services;

import com.example.bookshop.models.Person;
import com.example.bookshop.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> getAllPerson(){
        return personRepository.findAll();
    }

    public Person getPersonId(int id){
        Optional<Person> optional = personRepository.findById(id);
        return optional.orElse(null);
    }

    public Person findByLogin(Person person){
        Optional<Person> optional = personRepository.findByLogin(person.getLogin());
        return optional.orElse(null);
    }

    @Transactional
    public void register(Person person){
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");
        personRepository.save(person);
    }

    @Transactional
    public void setRole(Person person, String role){
        person.setRole(role);
        personRepository.save(person);
    }
}
