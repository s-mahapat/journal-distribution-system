/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Class;

import IAS.Bean.Invoice.InvoiceFormBean;
import IAS.Model.Invoice.InvoiceModel;
import IAS.Model.Subscription.SubscriptionModel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

/**
 *
 * @author Newton
 */
public class PlReferListPDF extends JDSPDF {

    private Connection conn = null;
    private InvoiceModel _InvoiceModel = null;
    private SubscriptionModel _SubscriptionModel = null;
    private static final Logger logger = JDSLogger.getJDSLogger(PlReferListPDF.class.getName());
    private String ctext;

    public PlReferListPDF() throws SQLException {
        super();
        conn = Database.getConnection();
        _InvoiceModel = new InvoiceModel();
        _SubscriptionModel = new SubscriptionModel();
        String sql = "select ctext from prl where year=?";
        try(PreparedStatement st = conn.prepareStatement(sql)){
            st.setInt(1, Calendar.getInstance().get(Calendar.YEAR));
            try(ResultSet rs = st.executeQuery()){
                if(rs.first()){
                    ctext = rs.getString("ctext");
                }
            }
        }
    }

    public ByteArrayOutputStream getPDF(int medium) throws DocumentException,
            MalformedURLException,
            IOException,
            SQLException,
            ParseException,
            ParserConfigurationException,
            TransformerException,
            ClassNotFoundException {

        com.itextpdf.text.Document document = this.getPDFDocument();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();

        String search_string = null;
        String sql = Queries.getQuery("get_pl_refer_list_for_email_and_all");
        //Connection _conn = Database.getConnection();

        if (medium == 2) {
            search_string = "";  // not valid email ids
            sql = Queries.getQuery("get_pl_refer_list_for_print_only");
        } else if (medium == 3) {
            search_string = "%"; // search for all
        }
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, search_string);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String invoice_no = rs.getString("invoiceNumber");
                    //String ctext = rs.getString("ctext");
                    document.newPage();
                    document.add(this.getLetterHead());
                    Paragraph _page = this.getPlReferListLetterBody(invoice_no, ctext);
                    document.add(_page);
                    document.add(this.getLetterFooter());
                    //document.add(this.getPaymentFooter());
                }
            } catch (Exception ex) {
                logger.fatal(ex);
                throw ex;
            }
        } catch (Exception ex) {
            logger.fatal(ex);
            throw ex;
        }
        document.close();
        return outputStream;

    }

    public ByteArrayOutputStream getPlReferListPage(String invoice_no) throws SQLException,
            ParseException,
            ParserConfigurationException,
            TransformerException,
            ClassNotFoundException,
            IOException,
            DocumentException {

        com.itextpdf.text.Document document = this.getPDFDocument();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
        document.open();
        document.add(this.getLetterHead());
        Paragraph _page = this.getPlReferListLetterBody(invoice_no, ctext);
        document.add(_page);
        document.add(this.getLetterFooter());
        document.close();
        return outputStream;

    }

    private Paragraph getPlReferListLetterBody(String invoice_no, String ctext) throws SQLException,
            ParseException,
            ParserConfigurationException,
            TransformerException,
            ClassNotFoundException,
            IOException {

        InvoiceFormBean _invoiceBean = _InvoiceModel.getInvoiceInfoPRL(invoice_no);
        if (_invoiceBean == null) {
            logger.error(String.format("Invoice Bean is null for %s", invoice_no));
            return null;
        }
        Paragraph paragraphOuter;
        paragraphOuter = new Paragraph();
        Paragraph addressParagraph = new Paragraph();
        PdfPTable addressTable = new PdfPTable(2);
        PdfPTable InvoiceInfoTable = new PdfPTable(2);
        Paragraph paragraphInvoiceInfo = new Paragraph();
        Paragraph paragraphBody = new Paragraph();
        Paragraph paragraphctext = new Paragraph(ctext, JDS_FONT_BODY);
        Paragraph paragraphInvoiceAddress = new Paragraph();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        paragraphOuter.setSpacingBefore(JDSPDF.LESS_OUTER_PARAGRAPH_SPACE);
        paragraphOuter.setIndentationLeft(JDSPDF.LEFT_INDENTATION_LESS);
        paragraphOuter.setAlignment(Element.ALIGN_LEFT);

        InvoiceInfoTable.setWidthPercentage(100);
        Paragraph invoiceNumber = new Paragraph(new Chunk("Invoice No: " + invoice_no, JDS_FONT_BODY));
        Paragraph subscriberNumber = new Paragraph(new Chunk("Sub No: " + _invoiceBean.getSubscriberNumber(), JDS_FONT_BODY));
        Paragraph invoiceHeader = new Paragraph("INVOICE");
        invoiceHeader.setAlignment(Element.ALIGN_CENTER);

        PdfPCell subscriberNumberCell = new PdfPCell(subscriberNumber);
        PdfPCell invoiceNumberCell = new PdfPCell(invoiceNumber);
        //PdfPCell invoiceHeaderCell = new PdfPCell(invoiceHeader);

        subscriberNumberCell.setBorder(Rectangle.NO_BORDER);
        subscriberNumberCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        subscriberNumberCell.setVerticalAlignment(Element.ALIGN_TOP);
        subscriberNumberCell.setPaddingLeft(JDSPDF.LEFT_INDENTATION_LESS);

        /*invoiceHeaderCell.setBorder(Rectangle.NO_BORDER);
         invoiceHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
         invoiceHeaderCell.setVerticalAlignment(Element.ALIGN_TOP);*/

        invoiceNumberCell.setBorder(Rectangle.NO_BORDER);
        invoiceNumberCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        invoiceNumberCell.setVerticalAlignment(Element.ALIGN_TOP);

        InvoiceInfoTable.addCell(subscriberNumberCell);
        //InvoiceInfoTable.addCell(invoiceHeaderCell);
        InvoiceInfoTable.addCell(invoiceNumberCell);

        paragraphInvoiceInfo.setSpacingBefore(JDSPDF.OUTER_PARAGRAPH_SPACE);
        addressParagraph.add(addressTable);
        addressParagraph.setIndentationLeft(JDSPDF.LEFT_INDENTATION_LESS);
        addressParagraph.setSpacingBefore(JDSPDF.LESS_OUTER_PARAGRAPH_SPACE);
        addressTable.setWidthPercentage(100);
        Chunk invoiceAddressHeader = new Chunk("INVOICE ADDRESS", JDS_FONT_BODY);
        invoiceAddressHeader.setTextRise(2);
        invoiceAddressHeader.setUnderline(1, 0);

        paragraphInvoiceAddress.add(invoiceAddressHeader);
        paragraphInvoiceAddress.add(Chunk.NEWLINE);
        paragraphInvoiceAddress.add(new Phrase(_invoiceBean.getSubscriberName(), JDS_FONT_BODY));
        paragraphInvoiceAddress.add(Chunk.NEWLINE);
        paragraphInvoiceAddress.add(new Phrase(_invoiceBean.getInvoiceAddress(), JDS_FONT_BODY));

        PdfPCell ctextCell = new PdfPCell(paragraphctext);
        ctextCell.setBorder(Rectangle.NO_BORDER);
        ctextCell.setPaddingLeft(JDSPDF.LEFT_INDENTATION_MORE * 2);
        ctextCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        ctextCell.setVerticalAlignment(Element.ALIGN_TOP);

        PdfPCell invoiceAddressCell = new PdfPCell(paragraphInvoiceAddress);
        invoiceAddressCell.setBorder(Rectangle.NO_BORDER);
        invoiceAddressCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        invoiceAddressCell.setVerticalAlignment(Element.ALIGN_TOP);
        addressTable.addCell(invoiceAddressCell);
        addressTable.addCell(ctextCell);
        paragraphBody.setIndentationLeft(JDSPDF.LEFT_INDENTATION_LESS);
        paragraphBody.setSpacingBefore(JDSPDF.INNER_PARAGRAPH_SPACE);

        // get subscription info for subscription id
        String sql = Queries.getQuery("get_subscription_details_prl");
        //Connection _conn = Database.getConnection();

        PdfPTable table;
        table = new PdfPTable(4);
        table.setWidthPercentage(98);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Journal Name", JDS_FONT_BODY));
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell1.setColspan(2);

        PdfPCell cell2 = new PdfPCell(new Paragraph("No. of Years", JDS_FONT_BODY));
        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell3 = new PdfPCell(new Paragraph("Rs.", JDS_FONT_BODY));
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);

        float total = 0;

        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, _invoiceBean.getSubscriptionID());
            pst.setInt(2, currentYear);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    String journalName = rs.getString("journalGroupName");
                    String years = String.valueOf(rs.getInt("period"));
                    float _rate = rs.getFloat("rate");

                    //total += _rate;
                    String rate = String.valueOf(_rate);

                    PdfPCell c1 = new PdfPCell(new Phrase(journalName, JDSPDF.JDS_FONT_NORMAL_SMALL));
                    c1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    c1.setColspan(2);

                    PdfPCell c2 = new PdfPCell(new Phrase(years, JDSPDF.JDS_FONT_NORMAL_SMALL));
                    c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    PdfPCell c3 = new PdfPCell(new Paragraph(rate, JDSPDF.JDS_FONT_NORMAL_SMALL));
                    c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                    c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    table.addCell(c1);
                    table.addCell(c2);
                    table.addCell(c3);
                    //table.addCell(c4);
                }
            }
        }

        PdfPCell blankCell = new PdfPCell(new Phrase("", JDSPDF.JDS_FONT_NORMAL_SMALL));
        blankCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        blankCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        blankCell.setColspan(2);

        PdfPCell totalCell = new PdfPCell(new Phrase("Total", JDSPDF.JDS_FONT_NORMAL_SMALL));
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell totalValue = new PdfPCell(new Phrase(String.valueOf(_invoiceBean.getAmount()), JDSPDF.JDS_FONT_NORMAL_SMALL));
        totalValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalValue.setVerticalAlignment(Element.ALIGN_MIDDLE);

        table.addCell(blankCell);
        table.addCell(totalCell);
        table.addCell(totalValue);

        // add the subscription table to letter body
        paragraphBody.add(table);

        // amount in words
        EnglishNumberToWords _EnglishNumberToWords = new EnglishNumberToWords();
        paragraphBody.add(Chunk.NEWLINE);
        paragraphBody.add(new Phrase(_EnglishNumberToWords.convertDouble(_invoiceBean.getAmount()).toUpperCase(), JDSPDF.JDS_FONT_NORMAL_SMALL));//Convert total value in words


        // add the invoice header, subscriber number and invoice number
        paragraphOuter.add(invoiceHeader);
        paragraphOuter.add(InvoiceInfoTable);

        // add the invoice address
        paragraphOuter.add(addressParagraph);

        // add the letter body to the outer paragraph
        paragraphOuter.add(paragraphBody);
        return paragraphOuter;
    }
}