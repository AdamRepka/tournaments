package com.repasoft.tournaments.print;

import com.repasoft.tournaments.model.MatchPaper;
import com.repasoft.tournaments.model.Player;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MatchPaperPdfGenerator {

    private static final PDRectangle PAGE = PDRectangle.A4;

    // ROZLOŽENÍ STRÁNKY – TO JE OK
    private static final int PAPERS_PER_ROW = 3;
    private static final int PAPERS_PER_COL = 6;

    private static final float MARGIN = 18;
    private static final float GAP = 8;

    private static final float FONT = 8;

    // ✅ SPRÁVNÝ GRID LÍSTKU – 4 SLOUPCE
    private static final float[] COL_RATIOS =
            {0.20f, 0.20f, 0.20f, 0.20f};

    public byte[] generate(List<MatchPaper> papers) throws IOException {

        try (PDDocument doc = new PDDocument()) {

            PDType0Font regular = loadFont(doc, "/fonts/DejaVuSans.ttf");
            PDType0Font bold = loadFont(doc, "/fonts/DejaVuSans-Bold.ttf");

            PDPage page = new PDPage(PAGE);
            doc.addPage(page);
            PDPageContentStream cs = new PDPageContentStream(doc, page);

            float paperWidth =
                    (PAGE.getWidth() - 2 * MARGIN - (PAPERS_PER_ROW - 1) * GAP)
                            / PAPERS_PER_ROW;

            float paperHeight =
                    (PAGE.getHeight() - 2 * MARGIN - (PAPERS_PER_COL - 1) * GAP)
                            / PAPERS_PER_COL;

            int index = 0;
            int perPage = PAPERS_PER_ROW * PAPERS_PER_COL;

            for (MatchPaper paper : papers) {

                if (index > 0 && index % perPage == 0) {
                    cs.close();
                    page = new PDPage(PAGE);
                    doc.addPage(page);
                    cs = new PDPageContentStream(doc, page);
                }

                int col = index % PAPERS_PER_ROW;
                int row = (index / PAPERS_PER_ROW) % PAPERS_PER_COL;

                float x = MARGIN + col * (paperWidth + GAP);
                float y = PAGE.getHeight()
                        - MARGIN
                        - (row + 1) * paperHeight
                        - row * GAP;

                drawPaper(cs, regular, bold, paper, x, y, paperWidth, paperHeight);
                index++;
            }

            cs.close();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            doc.save(out);
            return out.toByteArray();
        }
    }

    /* ================== FORMÁT LÍSTKU ================== */

    private void drawPaper(
            PDPageContentStream cs,
            PDType0Font regular,
            PDType0Font bold,
            MatchPaper p,
            float x,
            float y,
            float w,
            float h
    ) throws IOException {

        cs.addRect(x, y, w, h);
        cs.stroke();

        // výpočet 4 sloupců
        float[] colX = new float[5];
        colX[0] = x;
        for (int i = 0; i < 4; i++) {
            colX[i + 1] = colX[i] + w * COL_RATIOS[i];
        }

        float rowY = y + h - 10;

        // 1️⃣ Stůl
        writeCentered(cs, regular, FONT,
                center(colX[0], colX[1]), rowY, "Stůl: _");
        rowY -= 10;

        // 2️⃣ Skupina (sloupce 2+3)
        writeCentered(cs, regular, FONT,
                center(colX[1], colX[3]), rowY,
                "Skupina " + p.getGroup().getName());
        rowY -= 12;

        // 3️⃣ Jména
        writeCentered(cs, bold, FONT,
                center(colX[1], colX[2]), rowY, shortName(p.getFirstPlayer()));
        writeCentered(cs, bold, FONT,
                center(colX[2], colX[3]), rowY, ":");
        writeCentered(cs, bold, FONT,
                center(colX[3], colX[4]), rowY, shortName(p.getSecondPlayer()));
        rowY -= 12;

        // 4️⃣ Sety
        for (int i = 1; i <= p.getNumberOfSets(); i++) {
            writeCentered(cs, regular, FONT,
                    center(colX[0], colX[1]), rowY, "Set " + i);
            writeCentered(cs, regular, FONT,
                    center(colX[1], colX[2]), rowY, "____");
            writeCentered(cs, regular, FONT,
                    center(colX[2], colX[3]), rowY, ":");
            writeCentered(cs, regular, FONT,
                    center(colX[3], colX[4]), rowY, "____");
            rowY -= 10;
        }

        // Výsledek
        writeCentered(cs, bold, FONT,
                center(colX[0], colX[1]), rowY, "Konec");
        writeCentered(cs, bold, FONT,
                center(colX[1], colX[2]), rowY, "____");
        writeCentered(cs, bold, FONT,
                center(colX[2], colX[3]), rowY, ":");
        writeCentered(cs, bold, FONT,
                center(colX[3], colX[4]), rowY, "____");
    }

    /* ================== HELPERY ================== */

    private float center(float left, float right) {
        return left + (right - left) / 2;
    }

    private void writeCentered(
            PDPageContentStream cs,
            PDType0Font font,
            float size,
            float centerX,
            float y,
            String text
    ) throws IOException {

        float w = font.getStringWidth(text) / 1000 * size;
        cs.setFont(font, size);
        cs.beginText();
        cs.newLineAtOffset(centerX - w / 2, y);
        cs.showText(text);
        cs.endText();
    }

    private String shortName(Player p) {
        String first = p.getFirstName();
        String shortFirst = first.length() <= 3 ? first : first.substring(0, 3)+".";
        return shortFirst + " " + p.getLastName();
    }

    private PDType0Font loadFont(PDDocument doc, String path) throws IOException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            throw new IllegalStateException("Font not found: " + path);
        }
        return PDType0Font.load(doc, is, true);
    }
}
