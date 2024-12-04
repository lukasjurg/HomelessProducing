package team15.homelessproducing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team15.homelessproducing.model.AppointmentBooking;
import team15.homelessproducing.repository.AppointmentBookingRepository;

import java.util.List;

@Service
public class AppointmentBookingService {

    @Autowired
    private AppointmentBookingRepository appointmentBookingRepository;

    public List<AppointmentBooking> getAllAppointments() {
        return appointmentBookingRepository.findAll();
    }

    public AppointmentBooking getAppointmentById(Long id) {
        return appointmentBookingRepository.findById(id).orElseThrow(() -> new RuntimeException("AppointmentBooking not found"));
    }

    public AppointmentBooking createAppointment(AppointmentBooking appointment) {
        return appointmentBookingRepository.save(appointment);
    }

    public AppointmentBooking updateAppointment(Long id, AppointmentBooking appointmentDetails) {
        AppointmentBooking appointment = appointmentBookingRepository.findById(id).orElseThrow(() -> new RuntimeException("AppointmentBooking not found"));
        appointment.setBookingDate(appointmentDetails.getBookingDate());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setUser(appointmentDetails.getUser());
        appointment.setService(appointmentDetails.getService());
        return appointmentBookingRepository.save(appointment);
    }

    public void deleteAppointment(Long id) {
        appointmentBookingRepository.deleteById(id);
    }
}
