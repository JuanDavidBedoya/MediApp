package com.mediapp.juanb.juanm.mediapp.entities;

import java.util.UUID;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles_disponibilidad")
public class AvailabilityDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid_availability_detail")
    private UUID uuidAvailabilityDetail;

    @ManyToOne
    @JoinColumn(name = "id_availability", referencedColumnName = "uuid_doctor_availability")
    private DoctorAvailability doctorAvailability;

    @Column(name = "weekday", nullable = false)
    private String weekDay;

    @Column(name = "start_time", nullable = false)
    private String startTime;

    @Column(name = "end_time", nullable = false)
    private String endTime;

    public AvailabilityDetail() {
    }

    public AvailabilityDetail(UUID uuidAvailabilityDetail, DoctorAvailability doctorAvailability, String weekDay, String startTime,
            String endTime) {
        this.uuidAvailabilityDetail = uuidAvailabilityDetail;
        this.doctorAvailability = doctorAvailability;
        this.weekDay = weekDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public UUID getUuidAvailabilityDetail() {
        return uuidAvailabilityDetail;
    }

    public void setUuidAvailabilityDetail(UUID uuidAvailabilityDetail) {
        this.uuidAvailabilityDetail = uuidAvailabilityDetail;
    }

    public DoctorAvailability getDoctorAvailability() {
        return doctorAvailability;
    }

    public void setDoctorAvailability(DoctorAvailability doctorAvailability) {
        this.doctorAvailability = doctorAvailability;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
