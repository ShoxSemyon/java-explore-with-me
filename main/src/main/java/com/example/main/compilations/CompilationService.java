package com.example.main.compilations;

import com.example.demo.client.StatClient;
import com.example.main.events.EventRepository;
import com.example.main.events.model.Event;
import com.example.main.exception.NotFoundException;
import com.example.main.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@Import(StatClient.class)
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationMapper.convertToCompilation(newCompilationDto);

        setDefault(compilation);
        setEvents(compilation, newCompilationDto);

        return compilationMapper.convertToCompilationDto(
                compilationRepository.save(compilation));
    }

    @Transactional
    public CompilationDto update(NewCompilationDto newCompilationDto, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> createNotFoundException(compId));

        compilationMapper.updateCompilationFromCompilationDto(newCompilationDto, compilation);

        setEvents(compilation, newCompilationDto);

        return compilationMapper.convertToCompilationDto(
                compilationRepository.save(compilation));
    }

    @Transactional
    public void delete(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> createNotFoundException(compId));

        compilationRepository.deleteById(compId);
    }

    public CompilationDto get(Long compId) {


        return compilationMapper.convertToCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(() -> createNotFoundException(compId)));
    }

    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = pinned != null ?
                compilationRepository.findAllByPinned(pinned,
                        new OffsetBasedPageRequest(from, size))
                :
                compilationRepository.findAll(new OffsetBasedPageRequest(from, size))
                        .toList();

        return compilations.stream()
                .map(compilationMapper::convertToCompilationDto)
                .collect(Collectors.toList());
    }

    private void setEvents(Compilation compilation, NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() == null) return;
        List<Event> eventList = eventRepository.findAllByIdIn(newCompilationDto.getEvents());

        compilation.setEvents(eventList);
    }

    private void setDefault(Compilation compilation) {
        if (compilation.getPinned() == null) compilation.setPinned(false);
    }

    public static NotFoundException createNotFoundException(Long compId) {
        return new NotFoundException(String
                .format("Compilation with id=%s was not found",
                        compId));
    }
}
