package com.zohocrm.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.zohocrm.entity.Lead;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class PdfHelperService {
    public static ByteArrayInputStream employeePDFReport
            (List<Lead> leads) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            // Add Text to PDF file ->
            Font font = FontFactory
                    .getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
            Paragraph para = new Paragraph("Employee Table", font);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(10);
            // Add PDF Table Header ->
            Stream.of("Lead Id", "First Name", "Last Name", "Email", "Mobile",
                            "Lead Type", "Address", "Designation", "Company", "Note")
                    .forEach(headerTitle ->
                    {
                        PdfPCell header = new PdfPCell();
                        Font headFont = FontFactory.
                                getFont(FontFactory.HELVETICA_BOLD);
                        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        header.setHorizontalAlignment(Element.ALIGN_CENTER);
                        header.setBorderWidth(2);
                        header.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(header);
                    });
            for (Lead lead : leads) {
                PdfPCell idCell = new PdfPCell(new Phrase(lead.getLid().toString()));
                idCell.setPaddingLeft(4);
                idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell firstNameCell = new PdfPCell(new Phrase
                        (lead.getFirstName()));
                firstNameCell.setPaddingLeft(4);
                firstNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(firstNameCell);

                PdfPCell lastNameCell = new PdfPCell(new Phrase
                        (String.valueOf(lead.getLastName())));
                lastNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                lastNameCell.setPaddingRight(4);
                table.addCell(lastNameCell);

                PdfPCell emailCell = new PdfPCell(new Phrase(lead.getEmail()));
                emailCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                emailCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(emailCell);

                PdfPCell mobileCell = new PdfPCell(new Phrase(lead.getMobile()));
                mobileCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                mobileCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(mobileCell);

                PdfPCell leadTypeCell = new PdfPCell(new Phrase(lead.getLeadType()));
                leadTypeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                leadTypeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(leadTypeCell);

                PdfPCell addressCell = new PdfPCell(new Phrase(lead.getAddress()));
                addressCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                addressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(addressCell);

                PdfPCell designationCell = new PdfPCell(new Phrase(lead.getDesignation()));
                designationCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                designationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(designationCell);

                PdfPCell companyCell = new PdfPCell(new Phrase(lead.getCompany()));
                companyCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                companyCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(companyCell);

                PdfPCell noteCell = new PdfPCell(new Phrase(lead.getNote()));
                noteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                lastNameCell.setPaddingRight(4);
                table.addCell(noteCell);
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            System.out.println(e.toString());
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}