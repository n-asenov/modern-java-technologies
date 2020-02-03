package bg.sofia.uni.mjt.client.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

import bg.sofia.uni.mjt.input.parser.InputParser;
import bg.sofia.uni.mjt.reader.BarcodeReader;
import bg.sofia.uni.mjt.reader.InvalidBarcodeImageException;

public class ClientRequestHandler implements Runnable {
    private InputStream input;
    private InputParser inputParser;
    private Writer output;
    private BarcodeReader barcodeReader;

    public ClientRequestHandler(InputStream input, Writer output) {
        this.input = input;
        this.output = output;
        inputParser = new InputParser();
        barcodeReader = new BarcodeReader();
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
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    private String getServerRequestMessage(String clientRequest) throws InvalidBarcodeImageException {
        final String barcodeCommand = "get-food-by-barcode";
        String clientCommand = inputParser.getCommand(clientRequest);

        if (clientCommand.equals(barcodeCommand)) {
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
    
    private String getBarcodeByImageOption(List<String> arguments) throws InvalidBarcodeImageException {
        String imageOption = "--img=";
        
        for (String argument : arguments) {
            int imageOptionIndex = argument.indexOf(imageOption);

            if (imageOptionIndex != -1) {
                int beginIndex = imageOptionIndex + imageOption.length();
                String fileName = argument.substring(beginIndex);
                return barcodeReader.decodeBarcode(fileName);
            }
        }
        
        throw new InvalidBarcodeImageException("No barcode image provided");
    }

}
