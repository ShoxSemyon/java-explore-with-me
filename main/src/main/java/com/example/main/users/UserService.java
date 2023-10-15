package com.example.main.users;

import com.example.main.exception.NotFoundException;
import com.example.main.utils.OffsetBasedPageRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Transactional
    public UserDto create(UserDto userDto) {

        return userMapper.convertToUserDto(userRepository
                .save(userMapper
                        .convertToUser(userDto)));
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> createNotFoundException(userId));

        userRepository.deleteById(userId);
    }

    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        if (!CollectionUtils.isEmpty(ids)) {
            return userRepository.findAll(QUser.user.id.in(ids),
                            new OffsetBasedPageRequest(from, size))
                    .stream()
                    .map(userMapper::convertToUserDto)
                    .collect(Collectors.toList());
        } else {
            return userRepository.findAll(new OffsetBasedPageRequest(from, size))
                    .stream()
                    .map(userMapper::convertToUserDto)
                    .collect(Collectors.toList());
        }

    }

    public static NotFoundException createNotFoundException(Long userId) {
        return new NotFoundException(String
                .format("User with id=%s was not found",
                        userId));
    }
}
