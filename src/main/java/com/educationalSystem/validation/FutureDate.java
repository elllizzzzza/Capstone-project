//package com.educationalSystem.validation;
//
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import javax.validation.Constraint;
//import java.lang.annotation.*;
//
///**
// * Custom validation annotation to ensure a date is in the future.
// * Business case: Room bookings and course enrollment dates must be in the future.
// */
//@Target({ElementType.FIELD, ElementType.PARAMETER})
//@Retention(RetentionPolicy.RUNTIME)
//@Constraint(validatedBy = FutureDateValidator.class)
//@Documented
//public @interface FutureDate {
//
//    String message() default "Date must be in the future";
//
//    Class<?>[] groups() default {};
//
//    Class<? extends Payload>[] payload() default {};
//
//    /**
//     * Allow booking on the same day (today)?
//     * Default is false (must be strictly future)
//     */
//    boolean allowToday() default false;
//}