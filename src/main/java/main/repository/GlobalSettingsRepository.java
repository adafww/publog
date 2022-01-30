package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {

    @Query("select " +
            "case " +
            "when count (g) > 0 " +
            "then true else false end " +
            "from GlobalSettings g " +
            "where g.code like '%STATISTICS_IS_PUBLIC%' and g.value like 'YES'")
    boolean statisticsIsAccepted();

    @Query("select " +
            "case " +
            "when count (g) > 0 " +
            "then true else false end " +
            "from GlobalSettings g " +
            "where g.code like '%POST_PREMODERATION%' and g.value like 'YES'")
    boolean isPostPremoderation();

    @Modifying
    @Transactional
    @Query("update GlobalSettings g " +
            "set g.value = :multiuserMode " +
            "where g.id = 1 ")
    void updateMultiuserMode(@Param("multiuserMode") String multiuserMode);

    @Modifying
    @Transactional
    @Query("update GlobalSettings g " +
            "set g.value = :postPremoderation " +
            "where g.id = 2 ")
    void updatePostPremoderation(@Param("postPremoderation") String postPremoderation);

    @Modifying
    @Transactional
    @Query("update GlobalSettings g " +
            "set g.value = :statisticsIsPublic " +
            "where g.id = 3 ")
    void updateStatisticsIsPublic(@Param("statisticsIsPublic") String statisticsIsPublic);
}
