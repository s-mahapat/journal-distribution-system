/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Class;

import com.itextpdf.text.*;
import java.io.IOException;
import javax.servlet.ServletContext;

public class JDSPDF {
    
    public static int OUTER_PARAGRAPH_SPACE  = 10;
    public static int INNER_PARAGRAPH_SPACE  = 30;
    public static int LEFT_INDENTATION_LESS = 25;
    public static int LEFT_INDENTATION_MORE = 50;
    public static Font JDS_BOLD_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
    public static Font JDS_FONT_NORMAL_SMALL = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    
    
    public Document getPDFDocument(){
        com.itextpdf.text.Document document = new com.itextpdf.text.Document(PageSize.A4);        
        document.addAuthor("Indian Academy Of Sciences");
        document.addCreator("Indian Academy Of Sciences");
        document.addSubject("Indian Academy Of Sciences");
        document.addCreationDate();
        document.addTitle("Indian Academy Of Sciences");
        return document;
    }
    
    public Paragraph getLetterHead() throws BadElementException, java.net.MalformedURLException, IOException{
        Paragraph paragraph = new Paragraph();
        Paragraph paragraphDate = new Paragraph();
        
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraphDate.setAlignment(Element.ALIGN_RIGHT);
        
        ServletContext context = ServletContextInfo.getServletContext();
        String logo = context.getRealPath("/images/pdflogo.jpg");        
        
        Image _logo = Image.getInstance(logo);
        _logo.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(_logo);
        
        Font _IASFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);        
        Chunk HeaderIAS = new Chunk(JDSConstants.IAS_LETTERHEAD);
        HeaderIAS.setFont(_IASFont);
        
        Chunk HeaderIASAddress = new Chunk(JDSConstants.IAS_LETTERHEAD_ADDRESS);
        Chunk HeaderIASTel = new Chunk(JDSConstants.IAS_LETTERHEAD_TELEPHONE);
        Chunk LetterDate = new Chunk("Date: " + util.getDateString());
        paragraphDate.add(LetterDate);
        
        paragraph.add(HeaderIAS);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(HeaderIASAddress);
        paragraph.add(Chunk.NEWLINE);
        paragraph.add(HeaderIASTel);
        
        paragraphDate.setSpacingBefore(INNER_PARAGRAPH_SPACE);
        paragraph.add(paragraphDate);
        
        return paragraph;
    }
    
    public Paragraph getSalutation(){
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(40);
        paragraph.setIndentationLeft(25);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(new Chunk(JDSConstants.IAS_LETTERHEAD_SALUTATION));
        return paragraph;
        
    }
    
    public Paragraph getLetterFooter(){
        
        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_RIGHT);
        paragraph.setSpacingBefore(70);
        
        paragraph.add(new Chunk(JDSConstants.IAS_LETTERFOOT_CLOSING));
        paragraph.add(Chunk.NEWLINE);
        
        paragraph.add(new Chunk(JDSConstants.IAS_LETTERFOOT_SIGNATURE));
        paragraph.add(Chunk.NEWLINE);
        
        return paragraph;
        
    }
    
}
