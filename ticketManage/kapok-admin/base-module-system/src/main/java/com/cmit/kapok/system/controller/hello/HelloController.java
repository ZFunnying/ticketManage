package com.cmit.kapok.system.controller.hello;

import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping("/helloPage1")
    public StreamingResponseBody downloadFile1(HttpServletResponse response) {

        response.setContentType("text/html; charset=utf-8");

        return outputStream -> {
            int bytesRead;
            byte[] buffer = new byte[8192];
            InputStream inputStream = new ClassPathResource("/hello.html").getInputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        };
    }

    @GetMapping("/helloPage2")
    public ResponseEntity<InputStreamResource> downloadFile2() {
        return ResponseEntity.ok()
          .contentType(MediaType.TEXT_HTML)
          .body(new InputStreamResource(getClass().getResourceAsStream("/hello.html")));
    }

    @GetMapping("/createException")
    @SuppressWarnings("unused")
    public ResponseEntity<String> createException() {
        if (true) {
            throw new RuntimeException("exception");
        }

        return ResponseEntity.ok()
          .contentType(MediaType.TEXT_PLAIN)
          .body("this should not happen");
    }

}
