package me.christ9979.demorestapi.event;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            /**
             * reject() : Errors 객체에 GlobalError를 넣는다.
             */
            errors.reject("wrongPrices", "Values to are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
        || endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
        || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
            /**
             * rejectValue() : Errors 객체에 FieldError를 넣는다.
             */
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }

        // TODO BeginEventDateTime
        // TODO CloseEnrollmentDateTime
    }
}
