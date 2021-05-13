package app.pinjamruang.room.repository;

import app.pinjamruang.room.model.Room;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static app.pinjamruang.TestUtils.createDummyRoom;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RoomRepositoryTests {
    @Autowired
    RoomRepository repository;

    @Test
    public void jpaRepositoryBasicTest() {
        Room room = repository.save(createDummyRoom());

        assertEquals(repository.getOne(room.getId()), room);
    }
}
