package application;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfCreator {

	private String filename;
	private Document document;
	
	public PdfCreator (String filename, boolean landscape) {
		this.document = new Document(landscape? PageSize.A4.rotate() : PageSize.A4);
		this.filename = filename;
		document.addTitle("Giunico");
		document.addSubject("Giunico");
		
		try {
			PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filename));
			HeaderFooterPageEvent header_footer_event = new HeaderFooterPageEvent();
			pdfWriter.setPageEvent(header_footer_event);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
		document.open();
	}
	
	public Document getDocument() {
		return this.document;
	}
	
	public void closeDocument() {
		this.document.close();
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public PdfPCell formatHeaderCell(PdfPCell cell) {
		cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setPadding(5f);
		cell.setBackgroundColor(new BaseColor(247, 6, 177));
		return cell;
	}
	
	public Paragraph createHeader(String title) {
		Paragraph header = new Paragraph();
		Image image = null;
		try {
			image = Image.getInstance(getClass().getResource("/resources/images/logo_giunico.png"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		image.setAlt("Logo Giunico");
		image.scaleToFit(150, 150);
		image.setAlignment(Image.ALIGN_CENTER);
		
		Paragraph text_title = new Paragraph(title, getPoppinsFont(30, true, BaseColor.BLACK));
		text_title.setAlignment(Paragraph.ALIGN_CENTER);
		
		header.setAlignment(Paragraph.ALIGN_CENTER);
		header.add(image);
		header.add(text_title);
		header.add(emptyLines(1));
		
		return header;
	}
	
	public Font getPoppinsFont(float size, boolean bold, BaseColor color) {
		Font font = null;
		try {
			BaseFont baseFont;
			
			if (bold) baseFont = BaseFont.createFont(getClass().getResource("/resources/fonts/Poppins-Bold.ttf").toString(), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); 
			else baseFont = BaseFont.createFont(getClass().getResource("/resources/fonts/Poppins-Regular.ttf").toString(), BaseFont.WINANSI, BaseFont.NOT_EMBEDDED); 
			
			font = new Font(baseFont, size, Font.NORMAL, color);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return font;
	}
	
	public Paragraph emptyLines(int number) {
		Paragraph paragraph = new Paragraph();
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
        return paragraph;
    }
	
	public PdfPCell createNewCell(String text, int alignment, boolean bold, boolean background_color) {
		Phrase phrase = new Phrase(text, getPoppinsFont(12, bold, BaseColor.BLACK));
		
	    PdfPCell cell = new PdfPCell(phrase);
	    cell.setPadding(5);
	    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.BOX);
	    cell.setBackgroundColor(background_color ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);
	    return cell;
	}
	
	public PdfPCell createNewCell(String text, int size, int alignment, boolean bold, boolean background_color) {
		Phrase phrase = new Phrase(text, getPoppinsFont(size, bold, BaseColor.BLACK));
		
	    PdfPCell cell = new PdfPCell(phrase);
	    cell.setPadding(5);
	    cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
	    cell.setHorizontalAlignment(alignment);
	    cell.setBorder(PdfPCell.BOX);
	    cell.setBackgroundColor(background_color ? BaseColor.LIGHT_GRAY : BaseColor.WHITE);
	    return cell;
	}
	
	class HeaderFooterPageEvent extends PdfPageEventHelper {
		@Override
		public void onOpenDocument(PdfWriter writer, Document document) {
			//addFooter(writer);
	    }
		
		@Override
	    public void onEndPage(PdfWriter writer, Document document) {
	        addFooter(writer);
	    }
		
		private void addFooter(PdfWriter writer) {
			PdfPTable footer = new PdfPTable(3);
			int[] columnWidths = new int[]{100, 500, 200};
			try {
				footer.setWidths(columnWidths);
			}
			catch (DocumentException e1) {
				e1.printStackTrace();
			}
			footer.setTotalWidth(writer.getPageSize().getWidth());
            footer.setLockedWidth(true);
			footer.getDefaultCell().setFixedHeight(30);
			
			// 1st cell
			Image image = null;
			try {
				image = Image.getInstance(getClass().getResource("/resources/images/logo_giunico.png"));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			image.setAlt("Logo Giunico");
			image.scaleToFit(80, 80);
			image.setAlignment(Image.ALIGN_CENTER);
			PdfPCell cell1 = new PdfPCell(image);
			cell1.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell1.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell1.setBorder(PdfPCell.NO_BORDER);
			
			// 2nd cell
			Phrase credits = new Phrase("GIUNICO srl\nLight & Furniture Consulting", getPoppinsFont(16, false, BaseColor.BLACK));
			credits.getFont().setStyle(Font.ITALIC);
			PdfPCell cell2 = new PdfPCell(credits);
			cell2.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell2.setBorder(PdfPCell.NO_BORDER);
			
			// 3rd cell
			Phrase pages = new Phrase(String.format("Pagina %d di %d", writer.getCurrentPageNumber(), writer.getPageNumber()), getPoppinsFont(12, false, BaseColor.BLACK));
		    PdfPCell cell3 = new PdfPCell(pages);
		    cell3.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		    cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		    cell3.setBorder(PdfPCell.NO_BORDER);
		    
		    footer.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
			footer.addCell(cell1);
			footer.addCell(cell2);
			footer.addCell(cell3);
			
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ARTIFACT);
			footer.writeSelectedRows(0, -1, 34, 50, canvas);
			canvas.endMarkedContentSequence();
		}
	}
}
