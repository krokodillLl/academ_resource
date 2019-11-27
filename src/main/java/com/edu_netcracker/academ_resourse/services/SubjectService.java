package com.edu_netcracker.academ_resourse.services;

import com.edu_netcracker.academ_resourse.domain.Group;
import com.edu_netcracker.academ_resourse.domain.Subject;
import com.edu_netcracker.academ_resourse.repos.IGroupRepo;
import com.edu_netcracker.academ_resourse.repos.ISubjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SubjectService {
    @Autowired
    private ISubjectRepo subjectRepo;
    @Autowired
    private IGroupRepo groupRepo;

    private HashSet<Subject> subjects;

    public Set<Subject> addSubject(String[] sub){
        if(subjects == null)
            subjects = new HashSet<>();
        for (String s : sub ){
            if (subjectRepo.findSubjectByName(s) != null){
                subjects.add(subjectRepo.findSubjectByName(s));
            } else {
                Subject subject = new Subject(s);
                subjectRepo.save(subject);
                subjects.add(subject);
            }
        }
        return subjects;
    }

    public HashSet<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(HashSet<Subject> subjects) {
        this.subjects = subjects;
    }

    public Subject saveSubject(final Subject subject, final Group group) {
        Subject subjectFromBD = subjectRepo.findSubjectByName(subject.getName());

        if(subjectFromBD != null) {
            return subjectFromBD;
        }
        else {
            subject.addGroup(group);
            subjectRepo.save(subject);
        }
        return subject;
    }
}