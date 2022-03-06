package com.cocoon.service;

import com.cocoon.dto.InstitutionDTO;
import com.cocoon.entity.Institution;

import java.util.List;

public interface InstitutionService {

    List<InstitutionDTO> getAllInstitutions();

    InstitutionDTO getInstitutionById(Long id);

    List<InstitutionDTO> saveIfNotExist(List<String> names);

    InstitutionDTO save(InstitutionDTO institutionDTO);
}
