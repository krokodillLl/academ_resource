package com.edu_netcracker.academ_resourse.schedule.logic;

import com.edu_netcracker.academ_resourse.schedule.model.MongoGroup;
import com.edu_netcracker.academ_resourse.schedule.repositories.IItmoRepository;
import com.edu_netcracker.academ_resourse.schedule.repositories.INsuRepository;
import com.edu_netcracker.academ_resourse.schedule.repositories.ISmtuRepository;
import com.edu_netcracker.academ_resourse.schedule.universities.Itmo;
import com.edu_netcracker.academ_resourse.schedule.universities.Nsu;
import com.edu_netcracker.academ_resourse.schedule.universities.Smtu;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;

@Component
public class Schedule {

    private final String ITMO_TABLE_BEFORE = "border=\"0\"";
    private final String ITMO_TABLE_AFTER = "border=\"1\"";
    private final String SMTU_REGEX = "<table cellpadding=\"5\" cellspacing=\"5\" border=\"1\" width=\"100%\">";
    private final String NSU_TABLE = " border=\"1\"> \n";
    private final String END_TABLE = "</table>";
    private final String END_LINE = "\n";

    @Autowired
    IItmoRepository IItmoRepository;
    @Autowired
    ISmtuRepository ISmtuRepository;
    @Autowired
    INsuRepository INsuRepository;

    public Boolean existGroup(final MongoGroup mongoGroup) {
        if(mongoGroup.getUniversity() instanceof Itmo) {
            if(IItmoRepository.findAllByGroup(mongoGroup.getGroup()).size() != 0) {
                return true;
            }
        }
        else if (mongoGroup.getUniversity() instanceof Smtu) {
            if(ISmtuRepository.findAllByGroup(mongoGroup.getGroup()).size() != 0) {
                return true;
            }
        }
        else if (mongoGroup.getUniversity() instanceof Nsu) {
            if(INsuRepository.findAllByGroup(mongoGroup.getGroup()).size() != 0) {
                return true;
            }
        }
        return false;
    }

    public Model getSchedule(final String univ, final String group, final Model model) {
        StringBuilder week = new StringBuilder();
        String tomorrow = new String();

        if (univ.equals("SMTU")) {
            List<Smtu> smtus = ISmtuRepository.findAllByGroup(group);
            for (Smtu smtu : smtus) {
                week.append(smtu.getSchedule());
                tomorrow = smtu.getTomorrowSchedule();
            }
        } else if (univ.equals("ITMO")) {
            List<Itmo> itmos = IItmoRepository.findAllByGroup(group);
            for (Itmo itmo : itmos) {
                week.append(itmo.getSchedule());
                tomorrow = itmo.getTomorrowSchedule();
            }
        } else if (univ.equals("NSU")) {
            List<Nsu> nsus = INsuRepository.findAllByGroup(group);
            for (Nsu nsu : nsus) {
                week.append(nsu.getSchedule());
                tomorrow = nsu.getTomorrowSchedule();
            }
        }
        model.addAttribute("tomorrow_schedule", tomorrow);
        model.addAttribute("schedule", week);
        return model;
    }

    public void save(final MongoGroup mongoGroup, final Elements elements) {
        if(mongoGroup.getUniversity() instanceof Itmo) {
            StringBuilder sb = new StringBuilder(elements.toString().replaceAll(ITMO_TABLE_BEFORE, ITMO_TABLE_AFTER));
            mongoGroup.getUniversity().setSchedule(sb.toString());

            Itmo itmo = (Itmo)mongoGroup.getUniversity();
            IItmoRepository.save(itmo);
        }
        else if(mongoGroup.getUniversity() instanceof Smtu) {
            boolean a = false;
            String[] str = elements.toString().split(END_LINE);
            StringBuilder sb = new StringBuilder();
            for (String s : str) {
                if(s.contains(SMTU_REGEX)) {
                    a = true;
                }
                if(s.contains(END_TABLE)) {
                    a = false;
                }
                if(a) {
                    sb.append(s + "\n");
                }
            }
            mongoGroup.getUniversity().setSchedule(sb.toString());
            Smtu smtu = (Smtu) mongoGroup.getUniversity();
            ISmtuRepository.save(smtu);
        }
        else if(mongoGroup.getUniversity() instanceof Nsu) {
            StringBuilder sb = new StringBuilder();
            String[] str = elements.toString().split(END_LINE);
            for (int i = 0; i < str.length; i++) {
                if (i == 0) {
                    sb.append(str[0].substring(0, str[0].length() - 2) + NSU_TABLE);
                } else {
                    sb.append(str[i] + "\n");
                }
            }
            mongoGroup.getUniversity().setSchedule(sb.toString());

            Nsu nsu = (Nsu) mongoGroup.getUniversity();
            INsuRepository.save(nsu);
        }
    }

}
