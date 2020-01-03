package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.model.Employe;
import com.ipiecoles.java.mdd050.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Long countEmployes() {
        //Récupère le nb d'employés et l'envoyer au client
        return employeRepository.count();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Employe getEmployeById(@PathVariable(value = "id") Long id) {
        return employeRepository.findById(id).get();
    }

    @RequestMapping(value = "", params = "matricule")
    public Employe getEmployeMatricule(@RequestParam("matricule") String matricule) {
        return employeRepository.findByMatricule(matricule);
    }

    @RequestMapping("")
    public Page<Employe> getListeEmploye(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size,
            @RequestParam("sortDirection") Sort.Direction direction,
            @RequestParam("sortProperty") String matricule
    ) {
        return employeRepository.findAll(PageRequest.of(page, size, direction, matricule));
    }

}