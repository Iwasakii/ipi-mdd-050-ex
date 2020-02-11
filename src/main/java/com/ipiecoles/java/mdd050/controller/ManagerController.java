package com.ipiecoles.java.mdd050.controller;


import com.ipiecoles.java.mdd050.model.Employe;
import com.ipiecoles.java.mdd050.model.Manager;
import com.ipiecoles.java.mdd050.model.Technicien;
import com.ipiecoles.java.mdd050.repository.EmployeRepository;
import com.ipiecoles.java.mdd050.repository.ManagerRepository;
import com.ipiecoles.java.mdd050.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @RequestMapping(value = "/{idManager}/equipe/{idTechnicien}/remove", method = RequestMethod.GET)
    public void supprimerEmployeParManager(
            @PathVariable("idManager") Long manager_id,
            @PathVariable("idTechnicien") Long idEmploye
    ) {
            Technicien technicien = technicienRepository.findById(idEmploye).get();

            technicien.setManager(null);

            technicienRepository.save(technicien);
    }


    @RequestMapping(value = "/managers/{idManager}/equipe/{matriculeTech}/add", method = RequestMethod.GET)
    public Manager ajouterEmployeParManager(
            @PathVariable("idManager") Long manager_id,
            @PathVariable("matriculeTech") String matriculeEmpl
    ) {
        Manager manager = managerRepository.findById(manager_id).get();

        Technicien technicien = technicienRepository.findByMatricule(matriculeEmpl);

        technicien.setManager(manager);

        technicienRepository.save(technicien);

        return manager;
    }
}