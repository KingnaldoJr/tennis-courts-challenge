package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.exceptions.ErrorDetails;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/guests")
@Validated
public class GuestController extends BaseRestController {
    private final GuestService guestService;

    @ApiOperation(value = "Get all Guests if no name given, or all guests your specified name",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully obtained the guests", response = List.class),
            @ApiResponse(code = 204, message = "No guests found"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @GetMapping
    public ResponseEntity<List<GuestDTO>> getAll(@RequestParam(value = "name", required = false) @Valid String name) {
        List<GuestDTO> guests = name != null ? guestService.getGuestsByName(name) : guestService.getAllGuests();
        return guests.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(guests);
    }

    @ApiOperation(value = "Get a guest by given id",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully obtained the guest", response = GuestDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Guest not found", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<GuestDTO> getById(@PathVariable(value = "id") @Valid Long id) {
        return ResponseEntity.ok(guestService.getGuestById(id));
    }

    @ApiOperation(value = "Create a guest",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created the guest", response = GuestDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<GuestDTO> create(@RequestBody @Valid GuestRequestDTO request) {
        GuestDTO guest = guestService.createGuest(request);
        return ResponseEntity.created(locationByEntity(guest.getId())).body(guest);
    }

    @ApiOperation(value = "Update a guest",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated the guest", response = GuestDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Guest not found", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @PutMapping("/{id}")
    public ResponseEntity<GuestDTO> update(@PathVariable(value = "id") @Valid Long id,
                                           @RequestBody @Valid GuestRequestDTO request) {
        return ResponseEntity.ok(guestService.updateGuest(id, request));
    }

    @ApiOperation(value = "Update a guest",
            consumes = "application/json", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted the guest"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "Guest not found", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") @Valid Long id) {
        guestService.deleteGuest(id);
        return ResponseEntity.ok().build();
    }
}
