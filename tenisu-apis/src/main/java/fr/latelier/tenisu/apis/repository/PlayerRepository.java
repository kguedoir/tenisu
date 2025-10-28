package fr.latelier.tenisu.apis.repository;

import fr.latelier.tenisu.apis.domain.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Override
    @EntityGraph(attributePaths = {"country", "data", "last"})
    Optional<Player> findById(Long id);

    @EntityGraph(attributePaths = {"country", "data"})
    List<Player> findByFirstnameContainingIgnoreCaseOrLastnameContainingIgnoreCaseOrShortnameContainingIgnoreCaseOrderByDataRankAsc(
            String firstname,
            String lastname,
            String shortname
    );

    @EntityGraph(attributePaths = {"country", "data"})
    List<Player> findAllByOrderByDataRankAsc();

    @Override
    @EntityGraph(attributePaths = {"country", "data"})
    List<Player> findAll();

    // Fetch all players with country, data, and last for stats computations
    @EntityGraph(attributePaths = {"country", "data", "last"})
    @org.springframework.data.jpa.repository.Query("select p from Player p")
    List<Player> findAllWithDetails();
}
