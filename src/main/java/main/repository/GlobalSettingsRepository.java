package main.repository;

import main.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {

    @Query("select " +
            "case " +
            "when count (g) > 0 " +
            "then true else false end " +
            "from GlobalSettings g " +
            "where g.code like '%STATISTICS_IS_PUBLIC%' and g.value like 'YES'")
    boolean statisticsIsAccepted();
}
