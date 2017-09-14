package muhammedf.meetingmanager.repositories;

import muhammedf.meetingmanager.model.Meeting;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MeetingRepository
		extends
			PagingAndSortingRepository<Meeting, Long> {
}