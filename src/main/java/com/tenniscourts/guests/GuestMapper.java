package com.tenniscourts.guests;

import org.springframework.stereotype.Component;

@Component
public class GuestMapper {

    public Guest toGuest(GuestRequestDTO request) {
        return Guest.builder()
                .name(request.getName())
                .build();
    }

    public GuestDTO toDTO(Guest guest) {
        return GuestDTO.builder()
                .id(guest.getId())
                .name(guest.getName())
                .build();
    }
}
