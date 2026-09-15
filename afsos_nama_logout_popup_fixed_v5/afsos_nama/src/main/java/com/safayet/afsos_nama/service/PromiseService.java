package com.safayet.afsos_nama.service;

import com.safayet.afsos_nama.dto.PromiseDTO;
import com.safayet.afsos_nama.model.StudentPromise;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.PromiseStatus;
import com.safayet.afsos_nama.repository.StudentPromiseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromiseService {

    private final StudentPromiseRepository promiseRepository;

    public void savePromise(PromiseDTO dto, User user) {
        StudentPromise promise = dto.getId() == null
                ? new StudentPromise()
                : getPromiseById(dto.getId(), user);

        if (promise == null) {
            throw new IllegalArgumentException("Promise not found");
        }

        boolean newPromise = promise.getId() == null;

        BeanUtils.copyProperties(dto, promise, "id", "user", "brokenCount");

        if (newPromise) {
            promise.setUser(user);
        }

        if (promise.getStatus() == null) {
            promise.setStatus(PromiseStatus.ACTIVE);
        }

        promiseRepository.save(promise);
    }

    public List<StudentPromise> getAllPromises(User user) {
        return promiseRepository.findByUserOrderByTargetDateAsc(user);
    }

    public StudentPromise getPromiseById(Integer id, User user) {
        StudentPromise promise = promiseRepository.findById(id).orElse(null);

        if (promise == null || !promise.getUser().getId().equals(user.getId())) {
            return null;
        }

        return promise;
    }

    public void markCompleted(Integer id, User user) {
        StudentPromise promise = getPromiseById(id, user);

        if (promise != null) {
            promise.setStatus(PromiseStatus.COMPLETED);
            promiseRepository.save(promise);
        }
    }

    public void markBroken(Integer id, User user) {
        StudentPromise promise = getPromiseById(id, user);

        if (promise != null) {
            promise.setStatus(PromiseStatus.BROKEN);
            promise.setBrokenCount(promise.getBrokenCount() + 1);
            promiseRepository.save(promise);
        }
    }

    public void deletePromise(Integer id, User user) {
        StudentPromise promise = getPromiseById(id, user);

        if (promise != null) {
            promiseRepository.delete(promise);
        }
    }
}
