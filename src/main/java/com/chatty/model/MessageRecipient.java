package com.chatty.model;

import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class MessageRecipient {
	
	private String recipientName; // name of user or group to send the message to
	
	private RecipientType recipientType;
	
	public enum RecipientType {
		USER, GROUP
	}
}
