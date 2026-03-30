package com.smartscan.backend.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PDFUtil {

    public static File convertPdfToImage(File file) throws Exception {

        // Load PDF
        PDDocument doc = PDDocument.load(file);
        PDFRenderer renderer = new PDFRenderer(doc);

        // Convert first page to image
        BufferedImage image = renderer.renderImageWithDPI(0, 300);

        String uploadPath = System.getProperty("user.dir") + "/uploads/";
        File uploadDir = new File(uploadPath);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // ensure folder exists
        }

        File out = new File(uploadDir, "converted_" + System.currentTimeMillis() + ".png");

        // Save image
        ImageIO.write(image, "png", out);

        doc.close();

        System.out.println("PDF converted to image: " + out.getAbsolutePath());

        return out;
    }
}