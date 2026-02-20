//package com.educationalSystem.validation;
//
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//
//import java.time.LocalDate;
//
//public class FutureDateValidator implements ConstraintValidator<FutureDate, LocalDate> {
//
//    private boolean allowToday;
//
//    @Override
//    public void initialize(FutureDate constraintAnnotation) {
//        this.allowToday = constraintAnnotation.allowToday();
//    }
//
//    @Override
//    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
//        if (value == null) {
//            return true; // Use @NotNull for null checks
//        }
//
//        LocalDate today = LocalDate.now();
//
//        if (allowToday) {
//            return !value.isBefore(today);
//        } else {
//            return value.isAfter(today);
//        }
//    }
//}
