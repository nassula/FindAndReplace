package br.com.diegonassula.findandreplace.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileService {

    Logger log = LoggerFactory.getLogger(FileService.class);

    public void listFilesForFolder(File folder) throws IOException {
        /*
         * Percorre um array de pastas e arquivos
         * */
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {

                Charset charsetOfFile = StandardCharsets.UTF_8;
                String contentOfFile = new String(Files.readAllBytes(fileEntry.toPath()), charsetOfFile);

                String regexFolderName = "\\\\vertical\\\\(.+?)\\\\";
                Pattern patternFolderName = Pattern.compile(regexFolderName);
                Matcher matcherFolderName = patternFolderName.matcher(fileEntry.getAbsolutePath());
                if (matcherFolderName.find()) {
                    String folderName = matcherFolderName.group(1);
                    /*
                     * Regex para encontrar o padrão {{ asset('gateways/([^/]+)/assets/img/([^/]+)') }} dentro das views
                     * */
                    String regex = "\\{\\{ asset\\('gateways\\/([^/]+)\\/assets\\/img\\/([^/]+)'\\) }}";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(contentOfFile);
                    if (matcher.find()) {
                        contentOfFile = contentOfFile.replaceAll("\\{\\{ asset\\('gateways\\/([^/]+)\\/assets\\/img\\/([^/]+)'\\) }}", "https://cdn-gateways2.actualsales.com/vertical/$1/assets/img/$2");
                    }
                    /*
                     * Regex para encontrar o padrão ("../../img/[^/]+") dentro dos arquivos .css e .js
                     **/
                    String regex02 = "\\(\"(\\.\\.\\/img\\/[^/]+)\"\\)";
                    Pattern pattern02 = Pattern.compile(regex02);
                    Matcher matcher02 = pattern02.matcher(contentOfFile);
                    if (matcher02.find()) {
                        contentOfFile = contentOfFile.replaceAll("\\(\"\\.\\.\\/img\\/([^/]+)\"\\)", "(\"https://cdn-gateways2.actualsales.com/vertical/" + folderName + "/assets/img/$1\")");
                    }
                    /*
                     * Regex para encontrar o padrão svg-icon("../img/it/internalPages/windtre/icon-03.svg" dentro dos arquivos .css e .js
                     **/
                    contentOfFile = contentOfFile.replace("svg-icon(\"../img", "svg-icon(\"https://cdn-gateways2.actualsales.com/vertical/" + folderName + "/assets/img");
                    /*
                     * Regex para encontrar o padrão {{ asset('gateways/homesecurity/assets/img/us/internalPages/adt/img-01.jpg') }}
 dentro dos arquivos .css e .js
                     **/
                    contentOfFile = contentOfFile.replace("{{ asset('gateways", "svg-icon(\"https://cdn-gateways2.actualsales.com/vertical");
                    contentOfFile = contentOfFile.replace(".jpg') }}", "");
                    contentOfFile = contentOfFile.replace(".png') }}", "");
                    contentOfFile = contentOfFile.replace(".svg') }}", "");


                    contentOfFile = contentOfFile.replace(".imgs({", "/*.imgs({");
                    contentOfFile = contentOfFile.replace("version(pathPublic + \"/img\")", "version(pathPublic + \"/img\")*/");
                    contentOfFile = contentOfFile.replace("version(pathPublic + \"/img\")*/*/", "version(pathPublic + \"/img\")*/");

                    Files.write(fileEntry.toPath(), contentOfFile.getBytes(charsetOfFile));
                }
            }
        }
    }

    public void call() throws IOException {
        final File folder = new File("src/main/resources/static/top");
        log.info("Listing files in directory: " + folder.getAbsolutePath());
        listFilesForFolder(folder);
        log.info("Finished");
    }


}
