package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final TennisCourtRepository tennisCourtRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        return scheduleMapper.map(scheduleRepository.saveAndFlush(Schedule.builder()
                        .startDateTime(createScheduleRequestDTO.getStartDateTime())
                        .tennisCourt(tennisCourtRepository.findById(tennisCourtId)
                                .orElseThrow(() -> new EntityNotFoundException("Tennis Court with id " + tennisCourtId + " not found.")))
                .build()));

    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByStartDateTimeBeforeAndEndDateTimeAfter(startDate, endDate).stream()
                .map(scheduleMapper::map)
                .collect(Collectors.toList());
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleMapper.map(scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new EntityNotFoundException("Schedule with id " + scheduleId + " not found.")));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }
}
