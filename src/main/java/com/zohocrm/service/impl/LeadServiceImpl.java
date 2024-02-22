package com.zohocrm.service.impl;

import com.zohocrm.entity.Lead;
import com.zohocrm.exception.LeadNotFoundException;
import com.zohocrm.payload.LeadDto;
import com.zohocrm.payload.LeadResponse;
import com.zohocrm.repository.LeadRepository;
import com.zohocrm.service.LeadService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepo;
    private final ModelMapper modelMapper;

    public LeadServiceImpl(LeadRepository leadRepo, ModelMapper modelMapper) {
        this.leadRepo = leadRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public LeadDto createLead(LeadDto leadDto) {
        boolean emailExists = leadRepo.existsByEmail(leadDto.getEmail());
        boolean mobileExist = leadRepo.existsByMobile(leadDto.getMobile());
        if (emailExists) {
            throw new LeadNotFoundException("Email exist -" + leadDto.getEmail());
        }
        if (mobileExist) {
            throw new LeadNotFoundException("Mobile exist-" + leadDto.getMobile());
        }
        Lead lead = mapToEntity(leadDto);
        String leadId = UUID.randomUUID().toString();
        lead.setLid(leadId);
        Lead savedlead = leadRepo.save(lead);
        return mapToDto(savedlead);

    }

    @Override
    public void deleteLeadById(String lid) {
        Lead lead = leadRepo.findById(lid).orElseThrow(
                () -> new LeadNotFoundException("Lead with the id is not present :" + lid));
        leadRepo.deleteById(lid);
    }


    // Method to retrieve all leads from the database and convert them to LeadDto objects
    public LeadResponse getAllLeads(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Step 1: Sorting Configuration
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Step 2: Pagination Setup
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Step 3: Fetching Data
        Page<Lead> leadPage = leadRepo.findAll(pageable);

        // Step 4: Processing Results
        List<Lead> leads = leadPage.getContent();
        List<LeadDto> leadDtos = leads.stream()
                .map(lead -> modelMapper.map(lead, LeadDto.class))
                .collect(Collectors.toList());

        // Step 5: Building Response
        // // Create a LeadResponse object and set its properties
        LeadResponse leadResponse = new LeadResponse();
        leadResponse.setCompany(leadDtos);
        leadResponse.setPageNo(leadPage.getNumber());
        leadResponse.setTotalPages(leadPage.getTotalPages());
        leadResponse.setTotalElements((int) leadPage.getTotalElements());
        leadResponse.setPageSize(leadPage.getSize());
        leadResponse.setLast(leadPage.isLast());

        // Step 6: Returns the Response
        return leadResponse;
    }



    @Override
    public List<Lead> getLeadsExcelReports() {
        return leadRepo.findAll();
    }


    Lead mapToEntity(LeadDto leadDto) {
        return modelMapper.map(leadDto, Lead.class);
    }

    LeadDto mapToDto(Lead lead) {
        return modelMapper.map(lead, LeadDto.class);
    }
}