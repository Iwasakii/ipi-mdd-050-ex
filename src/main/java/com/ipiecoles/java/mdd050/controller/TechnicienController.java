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
@RequestMapping("/techniciens")
public class TechnicienController {

    @Autowired
    private TechnicienRepository technicienRepository;
    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(method = RequestMethod.GET, value = "{idTech}/manager/{matriculeManager}/add")
    public Technicien addManagerToTechnicien(
            @PathVariable("idTechnicien") Long idTechnicien,
            @PathVariable("matriculeManager") String matriculeManager
    ) {
        Technicien technicien = technicienRepository.findById(idTechnicien).get();

        Manager manager = (Manager) employeRepository.findByMatricule(matriculeManager);

        technicien.setManager(manager);

        return technicienRepository.save(technicien);
    }
}