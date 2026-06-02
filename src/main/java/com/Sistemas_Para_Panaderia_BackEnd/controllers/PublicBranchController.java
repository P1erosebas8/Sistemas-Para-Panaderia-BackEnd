package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Branch;
import com.Sistemas_Para_Panaderia_BackEnd.services.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Público - Sedes")
public class PublicBranchController {

    private final BranchService branchService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Branch>> getAllActiveBranches() {
        return ResponseEntity.ok(branchService.getAllActiveBranches());
    }
}
