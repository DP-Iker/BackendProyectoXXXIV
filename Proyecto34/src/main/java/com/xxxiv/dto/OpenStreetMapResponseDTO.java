package com.xxxiv.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OpenStreetMapResponseDTO {
	    private Address address;

	    @Data
	    @NoArgsConstructor
	    public static class Address {
	        private String city;
	        private String town;
	        private String village;
	    }
}
