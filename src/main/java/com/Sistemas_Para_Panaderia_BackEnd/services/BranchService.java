package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.BranchRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Branch;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {

    private final BranchRepository branchRepository;

    public List<Branch> getAllActiveBranches() {
        return branchRepository.findByIsActiveTrue();
    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Branch createBranch(BranchRequestDTO dto) {
        Branch branch = Branch.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isActive(true)
                .build();
        return branchRepository.save(branch);
    }

    public Branch toggleBranchStatus(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        
        branch.setIsActive(!branch.getIsActive());
        return branchRepository.save(branch);
    }
}
