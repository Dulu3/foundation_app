package com.piotrdulewski.foundationapp.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.piotrdulewski.foundationapp.entity.AccessToken;
import com.piotrdulewski.foundationapp.entity.Request;
import com.piotrdulewski.foundationapp.entity.RequestUser;
import com.piotrdulewski.foundationapp.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Service
@AllArgsConstructor
public class PDFGeneratorService {

    private AccessTokenService accessTokenService;
    private RequestService requestService;

    public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        AccessToken accessToken = accessTokenService.findByToken(request.getHeader("refreshToken"))
                .orElseThrow(() -> new RuntimeException("Header or token not found - 'refreshToken' "));
        Request request_entity = requestService.findById(Long.parseLong(request.getHeader("RequestId")));

        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setSize(18);

        Paragraph paragraph = new Paragraph("Zaswiadczenie", font);
        paragraph.setAlignment(1);

        document.add(paragraph);

        if (isRequestFromUser(accessToken, request_entity)) {

            Paragraph paragraph00 = new Paragraph("Nazwa", font);
            paragraph00.setAlignment(1);

            Paragraph paragraph0 = new Paragraph(request_entity.getName());
            paragraph0.setAlignment(1);

            Paragraph paragraph111 = new Paragraph("Opis", font);
            paragraph111.setAlignment(1);

            Paragraph paragraph11 = new Paragraph(request_entity.getDescription());
            paragraph11.setAlignment(1);

            Paragraph paragraph333 = new Paragraph("Tworca", font);
            paragraph333.setAlignment(1);

            Paragraph paragraph33 = new Paragraph(request_entity.getUser().getFirstName() + " "
                    + request_entity.getUser().getLastName());
            paragraph33.setAlignment(1);

            Paragraph paragraph44 = new Paragraph("Wykonawca", font);
            paragraph44.setAlignment(1);

            User user = userWhoDoneThis(request_entity, accessToken).getUser();
            Paragraph paragraph4 = new Paragraph(user.getFirstName() + " " + user.getLastName());
            paragraph4.setAlignment(1);

            Paragraph paragraph55 = new Paragraph("Data ukonczenia", font);
            paragraph55.setAlignment(1);

            Paragraph paragraph5 = new Paragraph(userWhoDoneThis(request_entity, accessToken).getCompletionTime().toString());
            paragraph5.setAlignment(1);

            Paragraph paragraph22 = new Paragraph("Data utworzenia:", font);
            paragraph22.setAlignment(1);

            Paragraph paragraph2 = new Paragraph(request_entity.getCreationTime().toString());

            paragraph2.setAlignment(1);

            document.add(paragraph00);
            document.add(paragraph0);
            document.add(paragraph111);
            document.add(paragraph11);
            document.add(paragraph333);
            document.add(paragraph33);
            document.add(paragraph44);
            document.add(paragraph4);
            document.add(paragraph55);
            document.add(paragraph5);
            document.add(paragraph22);
            document.add(paragraph2);
        }

        document.close();

    }

    private static boolean isRequestFromUser(AccessToken accessToken, Request request) {
        for (RequestUser u : request.getUsers()) {
            if (u.getUser().getUsername().equals(accessToken.getUserDetails().getUsername()) && u.getVerified() == 1) {
                return true;
            }
        }
        return false;
    }

    private static RequestUser userWhoDoneThis(Request request, AccessToken accessToken) {
        String email = accessToken.getUserDetails().getUsername();
        for (RequestUser u : request.getUsers()) {
            if (u.getUser().getUsername().equals(email)) {
                return u;
            }
        }
        return null;
    }
}
