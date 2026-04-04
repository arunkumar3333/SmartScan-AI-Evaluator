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

        // ✅ Load OpenCV
        OpenCV.loadLocally();

        // ✅ Read image
        Mat img = Imgcodecs.imread(file.getAbsolutePath());

        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
        }

        // 1️⃣ Convert to grayscale
        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);

        // 2️⃣ Resize (VERY IMPORTANT for handwritten text)
        Imgproc.resize(img, img, new Size(img.width() * 2, img.height() * 2));

        // 3️⃣ Light blur (reduce noise)
        Imgproc.GaussianBlur(img, img, new Size(3, 3), 0);

        // 4️⃣ Adaptive threshold (BEST for handwriting)
        Imgproc.adaptiveThreshold(
                img,
                img,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                11,
                2
        );

        // ✅ Save processed image
        String outputPath = System.getProperty("user.dir") + "/uploads/processed_" + System.currentTimeMillis() + ".png";
        File output = new File(outputPath);

        Imgcodecs.imwrite(output.getAbsolutePath(), img);

        return output;
    }
}