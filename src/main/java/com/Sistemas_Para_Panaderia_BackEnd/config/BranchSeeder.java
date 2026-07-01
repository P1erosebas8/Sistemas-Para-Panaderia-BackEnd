package com.Sistemas_Para_Panaderia_BackEnd.config;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Branch;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BranchSeeder implements CommandLineRunner {

    private final BranchRepository branchRepository;

    @Override
    public void run(String... args) throws Exception {
        if (branchRepository.count() == 0) {
            log.info("Sembrando sedes iniciales en la base de datos...");
            List<Branch> initialBranches = List.of(
                Branch.builder().name("Briselli - Las Palmeras").address("Av. Las Palmeras 4099, Los Olivos").latitude(-11.986900008562472).longitude(-77.07252834238831).isActive(true).build(),
                Branch.builder().name("Briselli - Antunez de Mayolo").address("Av. Santiago Antunez de Mayolo 1639, Los Olivos").latitude(-11.996172457316801).longitude(-77.08373327552152).isActive(true).build(),
                Branch.builder().name("Briselli - Angélica Gamarra").address("Av. Angélica Gamarra 1904, Los Olivos").latitude(-12.006227911665578).longitude(-77.08198680680607).isActive(true).build(),
                Branch.builder().name("Briselli - Universitaria").address("Av. Universitaria 6117, Los Olivos 15314").latitude(-11.963674409044833).longitude(-77.07214010005937).isActive(true).build(),
                Branch.builder().name("Briselli - Huandoy").address("Av. A Los Olivos, Cercado de Lima 15306").latitude(-11.963674409044833).longitude(-77.07771674666107).isActive(true).build(),
                Branch.builder().name("Briselli - Alfa").address("Av. Alfa 2171, Los Olivos 15302").latitude(-12.006515670569831).longitude(-77.06959673749027).isActive(true).build(),
                Branch.builder().name("Briselli - Mexico").address("Mexico 488, Comas 15311").latitude(-11.955191527513517).longitude(-77.05951937797056).isActive(true).build(),
                Branch.builder().name("Briselli - Tupac").address("Av. Túpac Amaru 735, Lima 15311").latitude(-11.959675708249497).longitude(-77.05402315343272).isActive(true).build(),
                Branch.builder().name("Briselli - German Aguirre").address("Av. Germán Aguirre 1199, SMP 15103").latitude(-12.019266332865481).longitude(-77.0760052178505).isActive(true).build()
            );
            branchRepository.saveAll(initialBranches);
            log.info("9 sedes iniciales creadas exitosamente.");
        } else {
            log.info("Las sedes ya existen en la base de datos. Omitiendo creación.");
        }
    }
}
