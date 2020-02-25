package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.exception.ConflictException;
import com.ipiecoles.java.mdd050.model.Commercial;
import com.ipiecoles.java.mdd050.model.Employe;
import com.ipiecoles.java.mdd050.model.Technicien;
import com.ipiecoles.java.mdd050.repository.CommercialRepository;
import com.ipiecoles.java.mdd050.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    public static final String REGEX_MATRICULE = "^[MCT][0-9]{5}$";

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private CommercialRepository commercialRepository;

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
        if (!matricule.matches(REGEX_MATRICULE)) {
            throw new IllegalArgumentException("Le matricule fourni est incorrect !");
        }
        Employe employe = employeRepository.findByMatricule(matricule);
        if(employe == null) {
            throw new EntityNotFoundException("L'employé de matricule " + matricule + " n'a pas étét trouvé !");
        }
        return employe;
    }

    //@RequestMapping("")
    @GetMapping
    public Page<Employe> getListeEmploye(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(value = "sortProperty", defaultValue = "matricule") String sortProperty
    ) {
        Long maxPage = employeRepository.count() / size;

        if(size <= 0 || size > 50) {
            throw new IllegalArgumentException("La taille des pages doit être comprise entre 0 et 50");
        }

        if(page < 0 || page > maxPage ) {
            throw new IllegalArgumentException(("La page " + page + " doit être comprise entre 0 et " + maxPage));
        }

        if(!Arrays.asList("id", "nom", "prenom", "matricule", "dateEmbauche", "salaire").contains(sortProperty)) {
            throw new IllegalArgumentException("La propriété de tri est incorrecte");
        }
        //Même que le précédent
        if(Arrays.stream(Employe.class.getDeclaredFields()).map(Field::getName).filter(s -> s.equals(sortProperty)).count() != 1) {
            throw new IllegalArgumentException("La propriété " + sortProperty + "n'existe pas !");
        }

        return employeRepository.findAll(PageRequest.of(page, size, direction, sortProperty));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Employe createEmploye(
        @RequestBody Employe employe
    ) throws ConflictException {
        if(employeRepository.findByMatricule(employe.getMatricule()) != null) {
            throw new ConflictException("Un employé exsite déjà pour le matricule " + employe.getMatricule());
        }
        return employeRepository.save(employe);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Employe modifierEmploye(
            // Pour gérer les erreurs plus tard
            @PathVariable("id") Long idEmploye,
            @RequestBody Employe employe
    ) {
        return employeRepository.save(employe);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmploye(
            @PathVariable("id") Long idEmploye
    ) {
         employeRepository.deleteById(idEmploye);
    }

}