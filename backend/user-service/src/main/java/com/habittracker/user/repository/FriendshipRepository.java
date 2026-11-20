package com.habittracker.user.repository;

import com.habittracker.user.domain.Friendship;
import com.habittracker.user.domain.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    // Найти заявку между двумя юзерами (в любую сторону)
    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.requesterId = :id1 AND f.addresseeId = :id2) OR " +
            "(f.requesterId = :id2 AND f.addresseeId = :id1)")
    Optional<Friendship> findBetweenUsers(@Param("id1") Long id1, @Param("id2") Long id2);

    // Получить все принятые дружбы для юзера
    @Query("SELECT f FROM Friendship f WHERE " +
            "(f.requesterId = :userId OR f.addresseeId = :userId) " +
            "AND f.status = :status")
    List<Friendship> findAllByUserIdAndStatus(@Param("userId") Long userId,
                                              @Param("status") FriendshipStatus status);

    // Получить входящие заявки (кого нужно принять)
    List<Friendship> findAllByAddresseeIdAndStatus(Long addresseeId, FriendshipStatus status);
}