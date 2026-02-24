package com.educationalSystem.filter;

import com.educationalSystem.entity.parts.RoomBooking;
import com.educationalSystem.enums.BookingStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

public class RoomBookingSpecification {

    private RoomBookingSpecification() {}

    public static Specification<RoomBooking> statusIn(List<BookingStatus> statuses) {
        return (root, query, cb) ->
                CollectionUtils.isEmpty(statuses) ? null
                        : root.get("status").in(statuses);
    }

    public static Specification<RoomBooking> roomIdEquals(Long roomId) {
        return (root, query, cb) ->
                roomId == null ? null
                        : cb.equal(root.get("room").get("roomId"), roomId);
    }

    public static Specification<RoomBooking> studentIdEquals(Long studentId) {
        return (root, query, cb) ->
                studentId == null ? null
                        : cb.equal(root.get("student").get("id"), studentId);
    }

    public static Specification<RoomBooking> instructorIdEquals(Long instructorId) {
        return (root, query, cb) ->
                instructorId == null ? null
                        : cb.equal(root.get("instructor").get("id"), instructorId);
    }

    public static Specification<RoomBooking> dateFrom(LocalDate from) {
        return (root, query, cb) ->
                from == null ? null
                        : cb.greaterThanOrEqualTo(root.get("date"), from);
    }

    public static Specification<RoomBooking> dateTo(LocalDate to) {
        return (root, query, cb) ->
                to == null ? null
                        : cb.lessThanOrEqualTo(root.get("date"), to);
    }

    public static Specification<RoomBooking> fromFilter(RoomBookingFilter filter) {
        return Specification.allOf(
                statusIn(filter.getStatus()),
                roomIdEquals(filter.getRoomId()),
                studentIdEquals(filter.getStudentId()),
                instructorIdEquals(filter.getInstructorId()),
                dateFrom(filter.getDateFrom()),
                dateTo(filter.getDateTo())
        );
    }
}