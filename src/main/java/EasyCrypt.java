/*
 *  EASY CRYPT
 *  BY: HOUSEOFKRAFT
 *
 *  LAST MODIFIED: 1/10/2021
 *
 *  Copyright (c) 2021 houseofkraft
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
 *  to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EasyCrypt {
    private String[] args;
    private RSA rsaHandler, rsaKeyGen;

    private String[] help = {
            "easycrypt -new <rsa_size> <aes_size> (output_file)",
            "easycrypt -encrypt <file_location> <encrypted_path> <key_file>",
            "easycrypt -decrypt <encrypted_path> <decrypted_path> <key_file>"
    };

    public void printHelp() {
        for (int i = 0; i < help.length; i++) {
            System.out.println(help[i]);
        }
    }

    public void newKeyFile(String rsaSize, String aesSize, String outputFile) throws NoSuchAlgorithmException, FileNotFoundException {
        RSA rsaKeyGen = new RSA(true, Integer.parseInt(rsaSize), Integer.parseInt(aesSize));

        rsaKeyGen.generateKeyFile(outputFile);
    }

    public void getKeyFile(String keyFilePath) throws NoSuchAlgorithmException, FileNotFoundException, InvalidKeySpecException {
        RSA rsaHandler = new RSA(false, 1,1);
        rsaHandler.loadKeyFile(keyFilePath);

        this.rsaHandler = rsaHandler;
    }

    private String readFile(String fileName, boolean singleLine) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        StringBuilder builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            if (singleLine) {
                builder.append(line);
            } else {
                builder.append(line + "\n");
            }
        }
        reader.close();

        return builder.toString();
    }


    private void writeFile(String fileInput, String fileOutput) throws IOException {
        File file = new File(fileOutput);
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileOutput));

        if (!file.exists()) { file.createNewFile(); }

        // split the string by new line, because \n might not be detected in all Operating Systems.
        String[] splitString = fileInput.split("\\r?\\n");;

        for (String line : splitString) {
            writer.append(line);
            writer.newLine();
        }

        writer.close();
    }

    public void encryptData(String fileInput, String fileOutput, String keyFileName) throws Exception {
        getKeyFile(args[3]);
        writeFile(rsaHandler.encrypt(readFile(fileInput, false)), fileOutput);
    }

    public void decryptData(String fileInput, String fileOutput, String keyFileName) throws Exception {
        getKeyFile(args[3]);
        writeFile(rsaHandler.decrypt(readFile(fileInput, true)), fileOutput);
    }


    public EasyCrypt(String[] _args) throws Exception {
        args = _args;
        if (args.length < 1) { printHelp(); }

        switch(args[0]) {
            case "-new": newKeyFile(args[1], args[2], args[3]); break;
            case "-encrypt": encryptData(args[1], args[2], args[3]); break;
            case "-decrypt": decryptData(args[1], args[2], args[3]); break;
            case "-version": System.out.println("EasyCrypt 1.0.0\nCopyright (c) 2021 houseofkraft\nThis software is licensed under the MIT license.");
        }
    }
}
