package com.example.stats.mapper;

import com.example.stats.model.ViewStats;
import com.example.stats.dto.ViewStatsDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ViewStatMapper {

   public ViewStatsDto convertToViewStatsDto(ViewStats viewStats) {
        return new ViewStatsDto(viewStats.getApp(),
                viewStats.getUri(),
                viewStats.getHints());
    }
}
