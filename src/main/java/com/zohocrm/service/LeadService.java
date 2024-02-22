package com.zohocrm.service;

import com.zohocrm.entity.Lead;
import com.zohocrm.payload.LeadDto;
import com.zohocrm.payload.LeadResponse;

import java.util.List;

public interface LeadService {
    LeadDto createLead(LeadDto leadDto);

    void deleteLeadById(String lid);

    LeadResponse getAllLeads(int pageNo, int pageSize, String sortBy, String sortDir);

    List<Lead> getLeadsExcelReports();
}


