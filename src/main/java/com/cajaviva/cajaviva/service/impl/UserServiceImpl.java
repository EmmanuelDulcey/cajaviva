package com.cajaviva.cajaviva.service.impl;

import com.cajaviva.cajaviva.dao.UserDao;
import com.cajaviva.cajaviva.entity.User;
import com.cajaviva.cajaviva.exception.BusinessValidationException;
import com.cajaviva.cajaviva.exception.ConflictException;
import com.cajaviva.cajaviva.exception.ResourceNotFoundException;
import com.cajaviva.cajaviva.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    @Override
    public User findById(UUID id) {
        return getExistingUser(id);
    }

    @Override
    public User create(User user) {
        validateUserPayload(user, true);
        validateEmailAvailability(user.getEmail(), null);

        LocalDateTime now = LocalDateTime.now();
        user.setId(null);
        user.setActive(user.getActive() != null ? user.getActive() : true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return userDao.save(user);
    }

    @Override
    public User update(UUID id, User user) {
        User existingUser = getExistingUser(id);
        validateUserPayload(user, false);

        String emailToPersist = isBlank(user.getEmail()) ? existingUser.getEmail() : user.getEmail();
        validateEmailAvailability(emailToPersist, id);

        user.setId(id);
        user.setName(isBlank(user.getName()) ? existingUser.getName() : user.getName());
        user.setLastName(isBlank(user.getLastName()) ? existingUser.getLastName() : user.getLastName());
        user.setEmail(emailToPersist);
        user.setCreatedAt(existingUser.getCreatedAt());
        user.setActive(user.getActive() != null ? user.getActive() : existingUser.getActive());
        user.setPasswordDigest(isBlank(user.getPasswordDigest()) ? existingUser.getPasswordDigest() : user.getPasswordDigest());
        user.setUpdatedAt(LocalDateTime.now());

        return userDao.save(user);
    }

    @Override
    public void delete(UUID id) {
        getExistingUser(id);
        userDao.deleteById(id);
    }

    private User getExistingUser(UUID id) {
        return userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario solicitado."));
    }

    private void validateEmailAvailability(String email, UUID currentUserId) {
        userDao.findByEmail(email)
                .filter(existingUser -> !existingUser.getId().equals(currentUserId))
                .ifPresent(existingUser -> {
                    throw new ConflictException("Este email ya se encuentra registrado.");
                });
    }

    private void validateUserPayload(User user, boolean requirePasswordDigest) {
        if (user == null) {
            throw new BusinessValidationException("El cuerpo de la solicitud del usuario es obligatorio.");
        }

        if (requirePasswordDigest && isBlank(user.getName())) {
            throw new BusinessValidationException("El nombre del usuario es obligatorio.");
        }

        if (requirePasswordDigest && isBlank(user.getLastName())) {
            throw new BusinessValidationException("El apellido del usuario es obligatorio.");
        }

        if (requirePasswordDigest && isBlank(user.getEmail())) {
            throw new BusinessValidationException("El email del usuario es obligatorio.");
        }

        if (requirePasswordDigest && isBlank(user.getPasswordDigest())) {
            throw new BusinessValidationException("La contrasena del usuario es obligatoria.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
