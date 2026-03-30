package com.smartscan.backend.service;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageProcessingService {

    public File preprocess(File file) {

        // Load OpenCV
        OpenCV.loadLocally();

        // Read image
        Mat img = Imgcodecs.imread(file.getAbsolutePath());

        // Safety check 
        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
        }

        // Convert to grayscale
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        // Noise reduction
        Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);

        // Thresholding
        Imgproc.threshold(img, img, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        String outputPath = System.getProperty("user.dir") + "/uploads/processed_" + System.currentTimeMillis() + ".png";
        File output = new File(outputPath);

        Imgcodecs.imwrite(output.getAbsolutePath(), img);

        System.out.println("Processed image saved at: " + output.getAbsolutePath());

        return output;
    }
}