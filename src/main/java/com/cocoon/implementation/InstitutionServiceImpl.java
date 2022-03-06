package com.cocoon.implementation;

import com.cocoon.dto.InstitutionDTO;
import com.cocoon.entity.Institution;
import com.cocoon.repository.InstitutionsRepo;
import com.cocoon.service.InstitutionService;
import com.cocoon.util.MapperUtil;
import com.cocoon.util.payment.GetInstitutions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    List<String> institutionNames = GetInstitutions.institutions;

    private final InstitutionsRepo institutionsRepo;
    private final MapperUtil mapperUtil;

    public InstitutionServiceImpl(InstitutionsRepo institutionsRepo, MapperUtil mapperUtil) {
        this.institutionsRepo = institutionsRepo;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<InstitutionDTO> getAllInstitutions() {
        List<Institution> institutions = institutionsRepo.findAll();
        return institutions.stream().map(obj -> mapperUtil.convert(obj, new InstitutionDTO())).collect(Collectors.toList());
    }

    @Override
    public InstitutionDTO getInstitutionById(Long id) {
        Institution institution = institutionsRepo.getById(id);
        return mapperUtil.convert(institution, new InstitutionDTO());
    }

    @Override
    public List<InstitutionDTO> saveIfNotExist(List<String> institutionNames) {

        Set<String> institutionSet = new HashSet<>(institutionNames);
        List<Institution> institutions = institutionsRepo.findAll();
        if (institutions.size() > 0){
            institutions.stream()
                    .peek(obj -> obj.setIsDeleted(true))
                    .map(Institution::getName)
                    .forEach(institutionSet::add);
        }

        return institutionSet.stream()
                .map(Institution::new)
                .peek(institutionsRepo::save)
                .map(obj -> mapperUtil.convert(obj, new InstitutionDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public InstitutionDTO save(InstitutionDTO institutionDTO) {
        Institution institution = mapperUtil.convert(institutionDTO, new Institution());
        return mapperUtil.convert(institutionsRepo.save(institution), new InstitutionDTO());
    }
}
