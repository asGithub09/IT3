import java.io.*;
import java.util.stream.Collectors;

class InvalidCompressionFormatException extends Exception {
    public InvalidCompressionFormatException(String message) {
        super(message);
    }
}

public class FileCompression {

    public static void compressTextFile(String inputFile, String compressedFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(compressedFile))) {

            StringBuilder compressedContent = new StringBuilder();
            String line;

            // Read the file line by line and compress using RLE
            while ((line = reader.readLine()) != null) {
                compressedContent.append(compressLine(line)).append("\n");
            }

            writer.write(compressedContent.toString());
            System.out.println("File compressed successfully. Compressed file: " + compressedFile);

        } catch (IOException e) {
            System.err.println("Error during compression: " + e.getMessage());
        } finally {
            System.out.println("Compression operation completed.");
        }
    }

    public static void decompressTextFile(String compressedFile, String decompressedFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(compressedFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(decompressedFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                writer.write(decompressLine(line) + "\n");
            }

            System.out.println("File decompressed successfully. Decompressed file: " + decompressedFile);

        } catch (InvalidCompressionFormatException e) {
            System.err.println("Invalid compression format: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error during decompression: " + e.getMessage());
        } finally {
            System.out.println("Decompression operation completed.");
        }
    }

    private static String compressLine(String line) {
        StringBuilder compressed = new StringBuilder();
        int count = 1;

        for (int i = 1; i <= line.length(); i++) {
            if (i == line.length() || line.charAt(i) != line.charAt(i - 1)) {
                compressed.append(line.charAt(i - 1)).append(count);
                count = 1;
            } else {
                count++;
            }
        }
        return compressed.toString();
    }

    private static String decompressLine(String line) throws InvalidCompressionFormatException {
        StringBuilder decompressed = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);

            StringBuilder countBuilder = new StringBuilder();
            while (i + 1 < line.length() && Character.isDigit(line.charAt(i + 1))) {
                countBuilder.append(line.charAt(++i));
            }

            if (countBuilder.length() == 0) {
                throw new InvalidCompressionFormatException("Invalid format at position " + i + " in line: " + line);
            }

            int count = Integer.parseInt(countBuilder.toString());
            decompressed.append(String.valueOf(character).repeat(count));
        }

        return decompressed.toString();
    }

    // Utility method for file size logging
    private static void logFileSize(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("File: " + fileName + " | Size: " + file.length() + " bytes");
        } else {
            System.err.println("File not found: " + fileName);
        }
    }

    // Polymorphic method for binary file compression
    public static void compressBinaryFile(String inputFile, String compressedFile) {
        try (FileInputStream fis = new FileInputStream(inputFile);
             FileOutputStream fos = new FileOutputStream(compressedFile)) {

            int prev = -1, count = 0;
            while (true) {
                int curr = fis.read();
                if (curr == prev) {
                    count++;
                } else {
                    if (prev != -1) {
                        fos.write(prev);
                        fos.write(count);
                    }
                    prev = curr;
                    count = 1;
                }

                if (curr == -1) break;
            }

            System.out.println("Binary file compressed successfully. Compressed file: " + compressedFile);

        } catch (IOException e) {
            System.err.println("Error during binary compression: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
  
        String inputFile = "input.txt";
        String compressedFile = "compressed.txt";
        String decompressedFile = "decompressed.txt";

        System.out.println("Before Compression:");
        logFileSize(inputFile);

        compressTextFile(inputFile, compressedFile);
        System.out.println("After Compression:");
        logFileSize(compressedFile);

        decompressTextFile(compressedFile, decompressedFile);
        System.out.println("After Decompression:");
        logFileSize(decompressedFile);

        
        try {
            if (new BufferedReader(new FileReader(inputFile))
                    .lines()
                    .collect(Collectors.joining())
                    .equals(new BufferedReader(new FileReader(decompressedFile))
                            .lines()
                            .collect(Collectors.joining()))) {
                System.out.println("Decompression verified: Files are identical.");
            } else {
                System.err.println("Decompression failed: Files do not match.");
            }
        } catch (IOException e) {
            System.err.println("Error verifying files: " + e.getMessage());
        }
    }
}
