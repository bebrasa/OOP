package nsu.kochanov.task241.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CheckpointTest {

  @Test
  void testCreateCheckpoint() {
    Checkpoint checkpoint = new Checkpoint();
    assertNotNull(checkpoint);
  }

  @Test
  void testSetCheckpointFields() {
    Checkpoint checkpoint = new Checkpoint();

    checkpoint.name = "Контрольная точка 1";
    checkpoint.date = LocalDate.of(2023, 11, 15);

    assertEquals("Контрольная точка 1", checkpoint.name);
    assertEquals(LocalDate.of(2023, 11, 15), checkpoint.date);
  }

  @Test
  void testCheckpointFieldsInitiallyNull() {
    Checkpoint checkpoint = new Checkpoint();

    assertNull(checkpoint.name);
    assertNull(checkpoint.date);
  }
}
