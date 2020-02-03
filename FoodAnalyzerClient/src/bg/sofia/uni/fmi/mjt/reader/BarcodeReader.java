package bg.sofia.uni.fmi.mjt.reader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class BarcodeReader {
    private static final String FILE_IS_NOT_AN_IMAGE = "The given file is not a image file.";

    private MultiFormatReader decoder;

    public BarcodeReader() {
        decoder = new MultiFormatReader();
    }

    public String decodeBarcode(String fileName) throws InvalidBarcodeImageException {
        File file = new File(fileName);
        validateFile(file);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            valideteBufferedImage(bufferedImage);
            LuminanceSource sourse = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmapImage = new BinaryBitmap(new HybridBinarizer(sourse));

            Result result = decoder.decode(bitmapImage);
            return result.getText();
        } catch (NotFoundException e) {
            throw new InvalidBarcodeImageException("There is no barcode in the image", e);
        } catch (IOException e) {
            throw new InvalidBarcodeImageException("Could not open the image file", e);
        }
    }

    private void validateFile(File file) throws InvalidBarcodeImageException {
        if (!file.isFile()) {
            throw new InvalidBarcodeImageException(FILE_IS_NOT_AN_IMAGE);
        }
    }

    private void valideteBufferedImage(BufferedImage bufferedImage)
            throws InvalidBarcodeImageException {
        if (bufferedImage == null) {
            throw new InvalidBarcodeImageException(FILE_IS_NOT_AN_IMAGE);
        }
    }

}
