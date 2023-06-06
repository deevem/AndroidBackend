package com.thss.androidbackend.service.user;

import com.thss.androidbackend.model.document.User;
import com.thss.androidbackend.model.dto.register.EmailRegisterDto;
import com.thss.androidbackend.model.dto.register.PhoneRegisterDto;
import com.thss.androidbackend.model.dto.register.UsernameRegisterDto;
import com.thss.androidbackend.model.dto.user.UpdatePasswordDto;
import com.thss.androidbackend.model.vo.user.UserDetail;
import com.thss.androidbackend.model.vo.user.UserMeta;
import com.thss.androidbackend.repository.UserRepository;
import com.thss.androidbackend.service.security.SecurityService;
import com.thss.androidbackend.service.user.CounterService;
import com.thss.androidbackend.service.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CounterService counterService;
    private final SecurityService securityService;

    @Override
    public User create(@NotNull UsernameRegisterDto dto){
        if (userRepo.findByUsername(dto.username()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setId(counterService.getNextSequence("customSequences"));
        user.setNickname("thss" + user.getId().toString());
        return userRepo.insert(user);
    }
    @Override
    public User create(@NotNull EmailRegisterDto dto){
        if (userRepo.findByEmail(dto.email()) != null) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User();
        user.setUsername(dto.email());
        user.setPassword(dto.password());
        user.setNickname("thss" + user.getId().toString());
        return userRepo.insert(user);
    }
    @Override
    public User create(@NotNull PhoneRegisterDto dto){
//        if (userRepo.findByPhone(dto.phoneNumber()) != null) {
//            throw new RuntimeException("Username already exists");
//        }
//        User user = new User();
//        user.setUsername(dto.username());
//        user.setNickname("thss" + user.getId().toString());
//        return userRepo.insert(user);
        return null;
    }
    public void ban(@NotNull String userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setBanned(true);
        userRepo.save(user);
    }
    public void updateAvatar(@NotNull String avatar){
        User user = securityService.getCurrentUser();
        user.setAvatarUrl(avatar);
        userRepo.save(user);
    }

    public void subscribe(@NotNull String userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User self = securityService.getCurrentUser();
        user.getSubscriberList().add(self);
        self.getFollowList().add(user);
        userRepo.save(self);
        userRepo.save(user);
    }

    public void unsubscribe(String userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        User self = securityService.getCurrentUser();
        user.getSubscriberList().remove(self);
        self.getFollowList().remove(user);
        userRepo.save(self);
        userRepo.save(user);
    }
    public void updateDescription(String description){
        User user = securityService.getCurrentUser();
        user.setDescription(description);
        userRepo.save(user);
    }
    public void updateNickname(String nickname) {
        User user = securityService.getCurrentUser();
        user.setNickname(nickname);
        userRepo.save(user);
    }
    public void updatePassword(UpdatePasswordDto dto) {
        User user = securityService.getCurrentUser();
        if(!passwordEncoder.matches(dto.oldPassword(), user.getPassword())){
            throw new BadCredentialsException("Wrong password");
        }
        user.setPassword(passwordEncoder.encode(dto.oldPassword()));
        userRepo.save(user);
    }
    public UserMeta getUserMeta(String userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getUserMeta(user);
    }
    public UserMeta getUserMeta(User user){
        UserMeta meta = new UserMeta(
                user.getId(),
                user.getAvatarUrl(),
                user.getNickname()
        );
        return meta;
    }
    public UserDetail getUserDetail(String userId){
        User user = userRepo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return getUserDetail(user);
    }
    public UserDetail getUserDetail(User user){
        UserDetail detail;
        if(securityService.isAnonymous()){
            detail = new UserDetail(
                    user.getId(),
                    user.getAvatarUrl(),
                    user.getNickname(),
                    user.getDescription(),
                    user.getFollowList().size(),
                    user.getSubscriberList().size(),
                    false
                    );
        } else {
            boolean liked = user.getSubscriberList().stream().anyMatch(
                    u -> u.getId().equals(securityService.getCurrentUser().getId()));
            detail = new UserDetail(
                    user.getId(),
                    user.getNickname(),
                    user.getAvatarUrl(),
                    user.getDescription(),
                    user.getFollowList().size(),
                    user.getSubscriberList().size(),
                    liked
            );
        }

        return detail;
    }
}
