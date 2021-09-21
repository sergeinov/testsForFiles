package com.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TestFiles {

    @BeforeAll
    static void setup() {
        Configuration.browser = "chrome";
        Configuration.startMaximized = true;
    }

    @Test
    public void testForDownloadFile() throws Exception {
        String result;
        
        open("https://github.com/selenide/selenide/blob/master/README.md");
        File download = $("#raw-url").download();
        try (InputStream is = new FileInputStream(download)) {                                  // read file
            result = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            is.close();
        }
        assertThat(result).contains("Selenide = UI Testing Framework powered by Selenium WebDriver");
    }

    @Test
    public void testForUploadFile() {
        open("http://www.csm-testcenter.org/test?do=show&subdo=common&test=file_upload");
        $("#item input[type='file']").uploadFromClasspath("example.txt");
        // $("#item input[type='file']").uploadFile(new File("src/test/resources/example.txt"));       // second way
        $("#item  #button").click();
        $("#content").shouldHave(text("File Upload Finished"));
    }

}
