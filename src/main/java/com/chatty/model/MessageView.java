package com.chatty.model;

import lombok.*;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class MessageView {

    private String username;
    
    private String recipients;

    private String text;

    private LocalDate date;
}
