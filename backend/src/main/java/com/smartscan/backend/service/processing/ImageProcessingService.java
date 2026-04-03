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



    public File preprocess(File file) {

        OpenCV.loadLocally();

        Mat img = Imgcodecs.imread(file.getAbsolutePath());

        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
        }

        //Step 1: Convert to Grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);

        //Step 2: Noise Removal
        Mat denoised = new Mat();
        Imgproc.medianBlur(gray, denoised, 3);

        // Step 3: Sharpen Image
        Mat sharpened = new Mat();
        Mat kernel = new Mat(3, 3, CvType.CV_32F);
        float[] data = {
                0, -1, 0,
                -1, 5, -1,
                0, -1, 0
        };
        kernel.put(0, 0, data);
        Imgproc.filter2D(denoised, sharpened, -1, kernel);

        //Step 4: Threshold (important for OCR)
        Mat threshold = new Mat();
        Imgproc.threshold(sharpened, threshold, 0, 255,
                Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        //Step 5: Resize (increase clarity)
        Mat resized = new Mat();
        Imgproc.resize(threshold, resized, new Size(), 2, 2, Imgproc.INTER_LINEAR);

        // Save processed image
        String outputPath = System.getProperty("user.dir") +
                "/uploads/processed_" + System.currentTimeMillis() + ".png";

        File output = new File(outputPath);
        Imgcodecs.imwrite(output.getAbsolutePath(), resized);

        return output;
    }
}

