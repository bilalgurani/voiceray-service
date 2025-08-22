package com.voiceray.Voiceray;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class VoicerayApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoicerayApplication.class, args);
	}

}
