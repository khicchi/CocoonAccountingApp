package com.cocoon.implementation;

import com.cocoon.dto.CompanyDTO;
import com.cocoon.entity.Company;
import com.cocoon.exception.CocoonException;
import com.cocoon.repository.CompanyRepo;
import com.cocoon.service.CompanyService;
import com.cocoon.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private MapperUtil mapperUtil;

    @Override
    public CompanyDTO getCompanyById(Long id) throws CocoonException {
        Optional<Company> companyOptional = companyRepo.findById(id);
        if (!companyOptional.isPresent())
            throw new CocoonException("There is no company with id " + id);

        return mapperUtil.convert(companyOptional.get(), new CompanyDTO());
    }

    @Override
    public List<CompanyDTO> getAllCompanies() {
        List<Company> companyList = companyRepo.findAll();
        return companyList.stream().map(company ->
                mapperUtil.convert(company, new CompanyDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(CompanyDTO companyDTO) throws CocoonException{
        //if same company name already exists in company table we have to throw exception
        if (companyRepo.existsByTitle(companyDTO.getTitle()))
            throw new CocoonException("This company name already saved to database.");

        //if establishment date is a date in the future, it makes no sense. it is not permitted
        checkEstablishmentDate(companyDTO);

        //when a company is saved it is assumed that it's status is enabled
        companyDTO.setEnabled(true);

        //if the input address value length exceeds 254 chars then we need to split it two
        // and save the rest in the second address field
        if (companyDTO.getAddress1().length() > 254){
            String fullAddress = companyDTO.getAddress1();
            int indexOfSpaceBeforeSplitLength = fullAddress.substring(0, 254).lastIndexOf(" ");
            companyDTO.setAddress1(fullAddress.substring(0, indexOfSpaceBeforeSplitLength));
            companyDTO.setAddress2(fullAddress.substring(indexOfSpaceBeforeSplitLength + 1));
        }

        //saving to database
        Company savedCompany = companyRepo.save(mapperUtil.convert(companyDTO, new Company()));
    }

    /**
     * checks whether the provided companyDTO object's establishment date is at a time in the future
     * @param companyDTO
     * @throws CocoonException
     */
    private void checkEstablishmentDate(CompanyDTO companyDTO) throws CocoonException{
        if (companyDTO.getEstablishmentDate().isAfter(LocalDate.now()))
            throw new CocoonException("Establishment date should not be at a time in future.");
    }

}
