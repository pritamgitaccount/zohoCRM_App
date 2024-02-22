package com.zohocrm.controller;


import com.zohocrm.entity.Lead;
import com.zohocrm.exception.LeadNotFoundException;
import com.zohocrm.payload.LeadDto;
import com.zohocrm.payload.LeadResponse;
import com.zohocrm.service.LeadService;
import com.zohocrm.service.impl.ExcelHelperService;
import com.zohocrm.service.impl.PdfHelperService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    //http://localhost:8080/api/leads
    @PostMapping
    public ResponseEntity<?> createLead(@RequestBody LeadDto leadDto) {
        try {
            // If successful, return the created LeadDto with HTTP status 201 (CREATED)
            return new ResponseEntity<>(leadService.createLead(leadDto), HttpStatus.CREATED);
        } catch (LeadNotFoundException e) {
            // If a LeadExist exception is caught, it means a conflict occurred (e.g., duplicate email or mobile)
            // Return an error message with HTTP status 409 (CONFLICT)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/{lid}")
    public ResponseEntity<?> deleteLeadById(@PathVariable String lid) {
        try {
            if (lid != null) {
                leadService.deleteLeadById(lid);
                return new ResponseEntity<>("Lead has been deleted", HttpStatus.OK);
            }
        } catch (LeadNotFoundException e) {
            return new ResponseEntity<>("Invalid request. Please provide a valid lead id.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Invalid request. Please provide a valid lead id.", HttpStatus.BAD_REQUEST);
    }


    //http://localhost:8080/api/leads?pageNo=0&pageSize=3&sortBy=company&sortDir=asc
    @GetMapping
    public LeadResponse getAllLeads(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        return leadService.getAllLeads(pageNo, pageSize, sortBy, sortDir);
    }

    //http://localhost:8080/api/leads/excelReports
    @GetMapping("/excelReports")
    public ResponseEntity<Resource> getLeadsExcelReports() {
        List<Lead> leads = leadService.getLeadsExcelReports();
        ByteArrayInputStream leadReports = ExcelHelperService.leadsToExcel(leads);
        String fileName = "leads.xlsx";
        InputStreamResource file = new InputStreamResource(leadReports);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }


    //http://localhost:8080/api/leads/leadPDFReports
    @GetMapping(value = "/leadPDFReports", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> employeeReport()
            throws IOException {
        List<Lead> leads = (List<Lead>) leadService.getLeadsExcelReports();
        ByteArrayInputStream pdfReports = PdfHelperService.employeePDFReport(leads);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; " + "filename=employees.pdf");
        return ResponseEntity.ok().headers(headers).contentType
                        (MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfReports));
    }
}