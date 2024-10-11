package com.cotede.interns.task.environment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnvironmentService {

    @Autowired
    private EnvironmentRepository environmentRepository;

    public Environment createEnvironment(Environment environment) {
        return environmentRepository.save(environment);
    }

    public Optional<Environment> getEnvironmentById(Long environmentId) {
        return environmentRepository.findById(environmentId);
    }

    public List<Environment> getAllEnvironments() {
        return environmentRepository.findAll();
    }

    public void deleteEnvironment(Long environmentId) {
        environmentRepository.deleteById(environmentId);
    }
}