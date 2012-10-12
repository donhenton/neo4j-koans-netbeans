package org.neo4j.tutorial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.neo4j.tutorial.matchers.ContainsOnlyHumanCompanions.containsOnlyHumanCompanions;
import static org.neo4j.tutorial.matchers.ContainsOnlySpecificTitles.containsOnlyTitles;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In this Koan we start to mix indexing and core API to perform more targeted
 * graph operations. We'll mix indexes and core graph operations to explore the
 * Doctor's universe.
 */
public class Koan05 {

    private static EmbeddedDoctorWhoUniverse universe;
    private final Logger logger = LoggerFactory.getLogger(Koan05.class);

    @BeforeClass
    public static void createDatabase() throws Exception {
        universe = new EmbeddedDoctorWhoUniverse(new DoctorWhoUniverseGenerator());
    }

    @AfterClass
    public static void closeTheDatabase() {
        universe.stop();
    }

    @Test
    public void shouldCountTheNumberOfDoctorsRegeneratedForms() {

        Index<Node> actorsIndex = universe.getDatabase().index().forNodes("actors");
        int numberOfRegenerations = 1;

        // YOUR CODE GOES HERE
        // SNIPPET_START
        Node firstDoctor = actorsIndex.get("actor", "William Hartnell").getSingle();

        Relationship regeneratedTo = firstDoctor.getSingleRelationship(DoctorWhoRelationships.REGENERATED_TO,
                Direction.OUTGOING);
        String test = "\n";
        while (regeneratedTo != null) {
            numberOfRegenerations++;
            regeneratedTo = regeneratedTo.getEndNode().getSingleRelationship(DoctorWhoRelationships.REGENERATED_TO,
                    Direction.OUTGOING);
            if (regeneratedTo != null) {
                // String k = regeneratedTo.getEndNode().getPropertyKeys().iterator().next();
                String t = (String) regeneratedTo.getEndNode().getProperty("actor", null);
                test += "\tactor --> " + t + "\n";
            }



        }

        //logger.debug(test);

        // SNIPPET_END

        assertEquals(11, numberOfRegenerations);
    }

    
    @Test 
    public void testDalekRelations()
    {
         Node dalek = universe.getDatabase().index().forNodes("species").get("species", "Dalek").getSingle();
        Iterable<Relationship> relationships = dalek.getRelationships(Direction.OUTGOING);
    }
    
    
    
    
    @Test
    public void countAllHumans() {
        Node human = universe.getDatabase().index().forNodes("species").get("species", "Human").getSingle();
     
        Iterable<Relationship> relationships = human.getRelationships(Direction.INCOMING);
        
        
        
        

        String test = "\n";
        for (Relationship rel : relationships) {
            Node humanNode = rel.getStartNode();
            String k = humanNode.getPropertyKeys().iterator().next();
            String t = (String) humanNode.getProperty(k, null);
            test += "\t" + k + " --> " + t + "\n";
        }


        logger.debug(test);

    }

    @Test
    public void shouldFindHumanCompanionsUsingCoreApi() {
        HashSet<Node> humanCompanions = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Node human = universe.getDatabase().index().forNodes("species").get("species", "Human").getSingle();

        Iterable<Relationship> relationships = universe.theDoctor().getRelationships(Direction.INCOMING,
                DoctorWhoRelationships.COMPANION_OF);
        for (Relationship rel : relationships) {
            Node companionNode = rel.getStartNode();
            // this finds that the companion IS_A HUMAN because it points to the HUMAN node
            if (companionNode.hasRelationship(Direction.OUTGOING, DoctorWhoRelationships.IS_A)) {
                Relationship singleRelationship = companionNode.getSingleRelationship(DoctorWhoRelationships.IS_A,
                        Direction.OUTGOING);
                Node endNode = singleRelationship.getEndNode();
                // if the endnode of the relationship is the HUMAN NODE
                // this allows for grouping because all humans point to the human Node
                if (endNode.equals(human)) {
                    humanCompanions.add(companionNode);
                }
            }
        }

        // SNIPPET_END

        int numberOfKnownHumanCompanions = 39;
        assertEquals(numberOfKnownHumanCompanions, humanCompanions.size());
        assertThat(humanCompanions, containsOnlyHumanCompanions());
    }

    @Test
    public void shouldFindAllEpisodesWhereRoseTylerFoughtTheDaleks() {
        Index<Node> friendliesIndex = universe.getDatabase().index().forNodes("characters");
        Index<Node> speciesIndex = universe.getDatabase().index().forNodes("species");
        HashSet<Node> episodesWhereRoseFightsTheDaleks = new HashSet<Node>();

        // YOUR CODE GOES HERE
        // SNIPPET_START

        Node roseTyler = friendliesIndex.get("character", "Rose Tyler").getSingle();
        Node daleks = speciesIndex.get("species", "Dalek").getSingle();

        for (Relationship r1 : roseTyler.getRelationships(DoctorWhoRelationships.APPEARED_IN, Direction.OUTGOING)) {
            Node episode = r1.getEndNode();

            for (Relationship r2 : episode.getRelationships(DoctorWhoRelationships.APPEARED_IN, Direction.INCOMING)) {
                if (r2.getStartNode().equals(daleks)) {
                    episodesWhereRoseFightsTheDaleks.add(episode);
                }
            }
        }

        // SNIPPET_END

        assertThat(
                episodesWhereRoseFightsTheDaleks,
                containsOnlyTitles("Army of Ghosts", "The Stolen Earth", "Doomsday", "Journey's End", "Bad Wolf",
                "The Parting of the Ways", "Dalek"));
    }
}
