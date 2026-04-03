package com.smartscan.backend.service.processing;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageProcessingService {public File preprocess(File file) {
    OpenCV.loadLocally();

    Mat img = Imgcodecs.imread(file.getAbsolutePath());

    if (img.empty()) {
        throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
    }

    // 1. Grayscale
    Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

    // 2. VERY LIGHT blur (reduce noise only)
    Imgproc.GaussianBlur(img, img, new Size(3, 3), 0);

    //3. Resize (MOST IMPORTANT)
    Imgproc.resize(img, img, new Size(img.width() * 2, img.height() * 2));

    //  REMOVE threshold completely

    // Save image
    String outputPath = System.getProperty("user.dir") + "/uploads/processed_" + System.currentTimeMillis() + ".png";
    File output = new File(outputPath);

    Imgcodecs.imwrite(output.getAbsolutePath(), img);

    return output;
}
    }