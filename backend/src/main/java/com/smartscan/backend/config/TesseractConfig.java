package com.smartscan.backend.config;

import net.sourceforge.tess4j.Tesseract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesseractConfig {

    @Bean
    public Tesseract tesseract() {

        Tesseract tesseract = new Tesseract();

        //IMPORTANT: Set correct path
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");

        tesseract.setLanguage("eng");

        return tesseract;
    }
}