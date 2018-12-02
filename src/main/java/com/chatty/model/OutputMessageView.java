package com.chatty.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class OutputMessageView {

    private String sender;

    private String text;

    private LocalDate date;
}
