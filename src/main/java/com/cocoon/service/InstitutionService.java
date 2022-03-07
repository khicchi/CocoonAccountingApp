package com.cocoon.service;

import com.cocoon.dto.InstitutionDTO;

import java.util.List;
import java.util.stream.Stream;

public interface InstitutionService {

    List<InstitutionDTO> getAllInstitutions();

    InstitutionDTO getInstitutionById(Long id);

    List<InstitutionDTO> saveIfNotExist(List<String> names);

    InstitutionDTO save(InstitutionDTO institutionDTO);

    List<String> getInstitutionsAtStartUp();
}
