package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Doctor;
import com.mediapp.juanb.juanm.mediapp.entities.DoctorSpeciality;
import com.mediapp.juanb.juanm.mediapp.entities.Speciality;
import com.mediapp.juanb.juanm.mediapp.repositories.DoctorSpecialityRepository;

@Service
public class DoctorSpecialityService {

    private DoctorSpecialityRepository doctorSpecialityRepository;

    public DoctorSpecialityService(DoctorSpecialityRepository doctorSpecialityRepository) {
        this.doctorSpecialityRepository = doctorSpecialityRepository;
    }

    public List<DoctorSpeciality> findAll() {
        return doctorSpecialityRepository.findAll();
    }

    public Optional<DoctorSpeciality> findById(String id) {
        return doctorSpecialityRepository.findById(id);
    }

    public DoctorSpeciality save(DoctorSpeciality doctorSpeciality) {
        return doctorSpecialityRepository.save(doctorSpeciality);
    }

    public void deleteByDoctorId(String doctorId) {
        List<DoctorSpeciality> list = doctorSpecialityRepository.findAll();
        for (DoctorSpeciality ds : list) {
            if (ds.getDoctor() != null && doctorId.equals(ds.getDoctor().getCedula())) {
                doctorSpecialityRepository.delete(ds);
            }
        }
    }

    public void deleteBySpecialityId(UUID specialityId) {
        List<DoctorSpeciality> list = doctorSpecialityRepository.findAll();
        for (DoctorSpeciality ds : list) {
            if (ds.getSpeciality() != null && specialityId.equals(ds.getSpeciality().getUuid())) {
                doctorSpecialityRepository.delete(ds);
            }
        }
    }

    public DoctorSpeciality updateDoctor(String doctorSpecialityId, Doctor doctor) {
        DoctorSpeciality ds = doctorSpecialityRepository.findById(doctorSpecialityId).orElse(null);
        if (ds != null) {
            ds.setDoctor(doctor);
            return doctorSpecialityRepository.save(ds);
        }
        return null;
    }

    public DoctorSpeciality updateSpeciality(String doctorSpecialityId, Speciality speciality) {
        DoctorSpeciality ds = doctorSpecialityRepository.findById(doctorSpecialityId).orElse(null);
        if (ds != null) {
            ds.setSpeciality(speciality);
            return doctorSpecialityRepository.save(ds);
        }
        return null;
    }
}
