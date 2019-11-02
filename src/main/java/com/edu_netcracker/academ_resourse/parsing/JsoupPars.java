package com.edu_netcracker.academ_resourse.parsing;

import com.edu_netcracker.academ_resourse.domain.User;
import com.edu_netcracker.academ_resourse.domain.universities.Itmo;
import com.edu_netcracker.academ_resourse.domain.universities.Nsu;
import com.edu_netcracker.academ_resourse.domain.universities.Smtu;
import com.edu_netcracker.academ_resourse.repositories.ItmoRepository;
import com.edu_netcracker.academ_resourse.repositories.NsuRepository;
import com.edu_netcracker.academ_resourse.repositories.SmtuRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class JsoupPars {
    private final static Logger logger = Logger.getLogger(JsoupPars.class);

    @Autowired
    ItmoRepository itmoRepository;
    @Autowired
    SmtuRepository smtuRepository;
    @Autowired
    NsuRepository nsuRepository;

public void addSchedule(final User user) throws IOException {

    if(user.getUniversity() instanceof Itmo) {
        if(itmoRepository.findAllByGroup(user.getGroup()).size() != 0) {
            logger.info("this group already exist");
            return;
        }
    }
    else if (user.getUniversity() instanceof Smtu) {
        if(smtuRepository.findAllByGroup(user.getGroup()).size() != 0) {
            logger.info("this group already exist");
            return;
        }
    }
    else if (user.getUniversity() instanceof Nsu) {
        if(nsuRepository.findAllByGroup(user.getGroup()).size() != 0) {
            logger.info("this group already exist");
            return;
        }
    }

    Document document = Jsoup.connect(user.getUniversity().getUrl())
            .userAgent("Chrome/4.0.249.0 Safari/532.5")
            .referrer("http://www.google.com")
            .get();

    Elements elements = document.select(user.getUniversity().getQuery());

    save(user, elements);
}

    private void save(User user, Elements elements) {
        if(user.getUniversity() instanceof Itmo) {
            StringBuilder sb = new StringBuilder(elements.toString().replaceAll("border=\"0\"", "border=\"1\""));
            user.getUniversity().setSchedule(sb.toString());

            Itmo itmo = (Itmo)user.getUniversity();
            itmoRepository.save(itmo);
        }
        else if(user.getUniversity() instanceof Smtu) {
            boolean a = false;
            String[] str = elements.toString().split("\n");
            StringBuilder sb = new StringBuilder();
            for (String s : str) {
                if(s.contains("<table cellpadding=\"5\" cellspacing=\"5\" border=\"1\" width=\"100%\">")) {
                    a = true;
                }
                if(s.contains("</table>")) {
                    a = false;
                }
                if(a) {
                    sb.append(s + "\n");
                }
            }
            user.getUniversity().setSchedule(sb.toString());
            Smtu smtu = (Smtu) user.getUniversity();
            smtuRepository.save(smtu);
        }
        else if(user.getUniversity() instanceof Nsu) {
            StringBuilder sb = new StringBuilder();
            String[] str = elements.toString().split("\n");
            for (int i = 0; i < str.length; i++) {
                if (i == 0) {
                    sb.append(str[0].substring(0, str[0].length() - 2) + " border=\"1\"> \n");
                } else {
                    sb.append(str[i] + "\n");
                }
            }
                user.getUniversity().setSchedule(sb.toString());

                Nsu nsu = (Nsu) user.getUniversity();
                nsuRepository.save(nsu);
            }
    }
}
