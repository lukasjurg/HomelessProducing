package team15.homelessproducing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team15.homelessproducing.model.AppointmentBooking;
import team15.homelessproducing.repository.AppointmentBookingRepository;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentBookingController {

    @Autowired
    private AppointmentBookingRepository appointmentBookingRepository;

    @GetMapping
    public List<AppointmentBooking> getAllAppointments() {
        return appointmentBookingRepository.findAll();
    }

    @GetMapping("/{id}")
    public AppointmentBooking getAppointmentById(@PathVariable Long id) {
        return appointmentBookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @PostMapping
    public AppointmentBooking createAppointment(@RequestBody AppointmentBooking appointment) {
        return appointmentBookingRepository.save(appointment);
    }

    @PutMapping("/{id}")
    public AppointmentBooking updateAppointment(@PathVariable Long id, @RequestBody AppointmentBooking appointmentDetails) {
        AppointmentBooking appointment = appointmentBookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found"));
        appointment.setBookingDate(appointmentDetails.getBookingDate());
        appointment.setStatus(appointmentDetails.getStatus());
        appointment.setService(appointmentDetails.getService());
        appointment.setUser(appointmentDetails.getUser());
        return appointmentBookingRepository.save(appointment);
    }

    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        appointmentBookingRepository.deleteById(id);
    }
}
