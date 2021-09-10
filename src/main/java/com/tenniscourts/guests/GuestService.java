package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public List<GuestDTO> getAllGuests() {
        return guestRepository.findAll().stream()
                .map(guestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public GuestDTO getGuestById(Long id) {
        return guestMapper.toDTO(findGuestById(id));
    }

    public List<GuestDTO> getGuestsByName(String name) {
        return guestRepository.findByName(name).stream()
                .map(guestMapper::toDTO)
                .collect(Collectors.toList());
    }

    private Guest findGuestById(Long id) {
        return guestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Guest with id " + id + " not found."));
    }

    public GuestDTO createGuest(GuestRequestDTO request) {
        return guestMapper.toDTO(guestRepository.saveAndFlush(guestMapper.toGuest(request)));
    }

    public GuestDTO updateGuest(Long id, GuestRequestDTO request) {
        Guest guest = findGuestById(id);
        guest.setName(request.getName());
        return guestMapper.toDTO(guestRepository.saveAndFlush(guest));
    }

    public void deleteGuest(Long id) {
        findGuestById(id);
        guestRepository.deleteById(id);
    }
}
