package com.example.main.compilations;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation convertToCompilation(NewCompilationDto newCompilationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "events", ignore = true)
    void updateCompilationFromCompilationDto(NewCompilationDto newCompilationDto, @MappingTarget Compilation compilation);

    CompilationDto convertToCompilationDto(Compilation compilation);
}
