package com.test;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TestsFiles {

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
    }

    @Test
    public void DownloadFileTest() throws Exception {
        String result;
        open("https://github.com/selenide/selenide/blob/master/README.md");
        File download = $("#raw-url").download();
        try (InputStream is = new FileInputStream(download)) {                                                          // read downloaded file
            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            assertThat(result)
                    .contains("Selenide = UI Testing Framework powered by Selenium WebDriver");
        }


    }

    @Test
    public void UploadFileTest() {
        open("http://www.csm-testcenter.org/test?do=show&subdo=common&test=file_upload");
        $("#item input[type='file']").uploadFromClasspath("txtData.txt");
        // $("#item input[type='file']").uploadFile(new File("src/test/resources/txtData.txt"));                        // second way to upload
        $("#item  #button").click();
        $("#content").shouldHave(text("File Upload Finished"));
    }

    @Test
    public void parseTxtTest() throws Exception {
        String result;
            try (InputStream stream = getClass().getClassLoader().getResourceAsStream("txtData.txt")) {            // read .txt file
                result = new String(stream.readAllBytes(), StandardCharsets.UTF_8);

                assertThat(result)
                        .startsWith("H")
                        .contains("Don't give up!");
            }
    }

    @Test
    public void parsePdfTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("pdfData.pdf")) {                // read .pdf file
            PDF parsed = new PDF(stream);

            assertThat(parsed.author)
                    .contains("sammy_lee12");
            assertThat(parsed.title)
                    .contains("Sam Connelly Tester Profile");
        }
    }

    @Test
    public void parseXlsxTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("excelData.xlsx")) {             // read .xlsx file
            XLS parsed = new XLS(stream);

            assertThat(parsed.excel.getSheetAt(0).getRow(5).getCell(1).getStringCellValue())
                    .isEqualTo("West");
            assertThat(parsed.excel.getSheetAt(0).getRow(5).getCell(4).getNumericCellValue())
                    .isEqualTo(56.0);
        }
    }

    @Test
    public void parseZipTest() throws Exception {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("zipData.zip")) {                // read .zip file
            ZipInputStream zipStream = new ZipInputStream(stream);
            zipStream.getNextEntry();
            Scanner scan = new Scanner(zipStream);

            while (scan.hasNext()) {
                System.out.println(scan.nextLine());
            }
        }
    }

}
