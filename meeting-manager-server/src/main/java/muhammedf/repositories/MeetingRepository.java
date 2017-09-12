package muhammedf.repositories;

import muhammedf.model.Meeting;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface MeetingRepository
		extends
			PagingAndSortingRepository<Meeting, Long> {
}