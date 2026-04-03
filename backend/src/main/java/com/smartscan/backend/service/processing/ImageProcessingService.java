package com.smartscan.backend.service.processing;

import nu.pattern.OpenCV;
import org.opencv.core.Mat;
import org.opencv.core.Size;
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

        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
        }

        // STEP 1: Convert to Grayscale
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        // STEP 2: Noise Reduction (Blur)
        Imgproc.GaussianBlur(img, img, new Size(3, 3), 0);

        // STEP 3: Save processed image
        String outputPath = System.getProperty("user.dir") +
                "/uploads/processed_" + System.currentTimeMillis() + ".png";

        File output = new File(outputPath);

        Imgcodecs.imwrite(output.getAbsolutePath(), img);

        return output;
    }
}