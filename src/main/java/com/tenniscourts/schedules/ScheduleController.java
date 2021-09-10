package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.exceptions.ErrorDetails;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/schedules")
@Validated
public class ScheduleController extends BaseRestController {
    private final ScheduleService scheduleService;

    @ApiOperation(value = "Created schedule to given tennis court", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created schedule to tennis court"),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 400, message = "Tennis Court not found", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody @Valid CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(), createScheduleRequestDTO).getId())).build();
    }

    @ApiOperation(value = "Return the list of scheduled courts between dates",
            produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully obtains schedules"),
            @ApiResponse(code = 204, message = "No schedules between this dates"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 406, message = "Not Acceptable"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@RequestParam(value = "startDate") @Valid LocalDate startDate,
                                                                  @RequestParam(value = "endDate") @Valid LocalDate endDate) {
        List<ScheduleDTO> schedules = scheduleService.findSchedulesByDates(
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59)));
        return schedules.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(schedules);
    }

    @ApiOperation(value = "Return the list of scheduled courts between dates",
            produces = "application/json", consumes = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully get the schedule", response = ScheduleDTO.class),
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDetails.class),
            @ApiResponse(code = 404, message = "No schedule found with this id", response = ErrorDetails.class),
            @ApiResponse(code = 406, message = "Not Acceptable", response = ErrorDetails.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorDetails.class)
    })
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable(value = "id") @Valid Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
