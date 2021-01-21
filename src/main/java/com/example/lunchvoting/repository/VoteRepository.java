package com.example.lunchvoting.repository;

import com.example.lunchvoting.entity.User;
import com.example.lunchvoting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    @Transactional
    @Override
    <T extends Vote> T save(T item);

    @Transactional
    @Override
    void deleteById(Integer id);

    @Transactional
    void deleteByUser(User user);

    @Transactional
    void deleteByUserAndDate(User user, LocalDate date);

    @Query("select count(v) from Vote v where v.restaurant.id = :restaurantId and v.date = :date")
    int getCountVotesByDate(@Param("restaurantId") Integer restaurantId, @Param("date") LocalDate date);

    @Query("select v from Vote v join fetch v.restaurant where v.date = :date and v.user.id = :userId")
    Optional<Vote> findByUserAndDate(@Param("userId") Integer userId, @Param("date") LocalDate date);

//    @Query(value = "select t.id, t.name, t.votes " +
//            "from ( " +
//            "       select r.id, r.name, count(v.id_user) as votes " +
//            "       from restaurants r, " +
//            "            user_votes v " +
//            "       where v.id_restaurant = r.id " +
//            "         and v.date = ?1 " +
//            "       group by r.id, r.name " +
//            "     ) t " +
//            "where t.votes = " +
//            "      (select max(votes) " +
//            "       from (select count(v2.id_user) as votes " +
//            "             from user_votes v2 " +
//            "             where v2.date = ?1 " +
//            "             group by v2.id_restaurant)) ", nativeQuery = true)
//    List<Votes> getVotingWinners(LocalDate date);

}
