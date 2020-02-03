package bg.sofia.uni.fmi.mjt.client.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import bg.sofia.uni.fmi.mjt.input.parser.InputParser;
import bg.sofia.uni.fmi.mjt.reader.BarcodeReader;
import bg.sofia.uni.fmi.mjt.reader.InvalidBarcodeImageException;

public class ClientRequestHandler implements Runnable {
    private static final String BARCODE_COMMAND = "get-food-by-barcode";
    
    private InputStream input;
    private InputParser inputParser;
    private Writer output;
    private BarcodeReader barcodeReader;

    public ClientRequestHandler(InputStream input, Writer output, BarcodeReader barcodeReader) {
        this.input = input;
        this.output = output;
        this.barcodeReader = barcodeReader;
        inputParser = new InputParser();
    }

    @Override
    public void run() {
        try (var reader = new BufferedReader(new InputStreamReader(input));
                var writer = new PrintWriter(output, true)) {
            while (true) {
                String clientRequest = reader.readLine();

                try {
                    String serverRequestMessage = getServerRequestMessage(clientRequest);
                    writer.println(serverRequestMessage);
                } catch (InvalidBarcodeImageException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerRequestMessage(String clientRequest)
            throws InvalidBarcodeImageException {
        String clientCommand = inputParser.getCommand(clientRequest);

        if (clientCommand.equals(BARCODE_COMMAND)) {
            String barcode = getBarcode(inputParser.getArguments(clientRequest));
            return clientCommand + " " + barcode;
        }

        return clientRequest;
    }

    private String getBarcode(List<String> arguments) throws InvalidBarcodeImageException {
        String codeOption = "--code=";

        for (String argument : arguments) {
            int codeOptionIndex = argument.indexOf(codeOption);

            if (codeOptionIndex != -1) {
                int beginIndex = codeOptionIndex + codeOption.length();
                return argument.substring(beginIndex);
            }
        }

        return getBarcodeByImageOption(arguments);
    }

    private String getBarcodeByImageOption(List<String> arguments)
            throws InvalidBarcodeImageException {
        String imageOption = "--img=";

        for (String argument : arguments) {
            int imageOptionIndex = argument.indexOf(imageOption);

            if (imageOptionIndex != -1) {
                int beginIndex = imageOptionIndex + imageOption.length();
                String imageName = argument.substring(beginIndex);
                return barcodeReader.decodeBarcode(imageName);
            }
        }

        throw new InvalidBarcodeImageException("No barcode image provided");
    }

}
