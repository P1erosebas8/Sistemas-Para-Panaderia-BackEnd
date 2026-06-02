package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.BranchRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Branch;
import com.Sistemas_Para_Panaderia_BackEnd.services.BranchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/branches")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin - Sedes")
public class AdminBranchController {

    private final BranchService branchService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<Branch>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Branch> createBranch(@RequestBody BranchRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(branchService.createBranch(request));
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<Branch> toggleBranchStatus(@PathVariable Long id) {
        return ResponseEntity.ok(branchService.toggleBranchStatus(id));
    }
}
