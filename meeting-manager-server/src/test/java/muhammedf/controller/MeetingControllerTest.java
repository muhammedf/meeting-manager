package muhammedf.controller;

import muhammedf.model.Meeting;
import muhammedf.repositories.MeetingRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetingControllerTest extends AbstractCRUDControllerTest<Meeting, Long> {

    @InjectMocks
    private MeetingController mc;

    @Mock
    private MeetingRepository mr;

    @Override
    public AbstractCRUDController getController() {
        return mc;
    }

    @Override
    public CrudRepository getRepository() {
        return mr;
    }

    @Override
    public Class<Meeting> getTClass() {
        return Meeting.class;
    }

    @Override
    public Meeting getNewTInstance() {
        return new Meeting();
    }

    private Long lastInstance = 0l;

    @Override
    public Long getNewIDInstance() {
        lastInstance+=1;
        return lastInstance;
    }
}
