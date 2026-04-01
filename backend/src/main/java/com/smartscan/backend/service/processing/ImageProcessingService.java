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
        OpenCV.loadLocally();

        Mat img = Imgcodecs.imread(file.getAbsolutePath());

        if (img.empty()) {
            throw new RuntimeException("Failed to load image: " + file.getAbsolutePath());
        }

        Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
        Imgproc.threshold(img, img, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        String outputPath = System.getProperty("user.dir") + "/uploads/processed_" + System.currentTimeMillis() + ".png";
        File output = new File(outputPath);

        Imgcodecs.imwrite(output.getAbsolutePath(), img);

        return output;
    }
}
