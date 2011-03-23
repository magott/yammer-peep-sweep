package no.magott.yammer.peep.sweep.batch.reader;

import java.util.List;

import no.magott.yammer.peep.sweep.client.YammerOperations;
import no.magott.yammer.peep.sweep.domain.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.AbstractPagingItemReader;

/**
 * Yammer API supports listing a mximum number of 50 users per page. Will read
 * one page at the time caching the users from that page, returning one at the
 * time in {@link #read()}
 * 
 * NOT thread safe, but can easily be made threadsafe. See pattern used in
 * {@link AbstractPagingItemReader}
 * 
 * @author morten.andersen-gott
 * 
 */
public class YammerUserItemReader implements ItemReader<User> {

	private final Log logger = LogFactory.getLog(getClass());
	
	private static final int YAMMER_MAX_PAGE_SIZE = 50;
	private static final int YAMMER_FIRST_PAGE = 1;

	private YammerOperations yammerOperations;

	private int pageSize=YAMMER_MAX_PAGE_SIZE;
	private int page = YAMMER_FIRST_PAGE;
	private int current = 0;
	private List<User> results;
	
	/**
	 * Code is heavily inspired by {@link AbstractPagingItemReader#read()}
	 * minus the synchronization which isn't needed in this use case
	 */
	@Override
	public User read() {

		if (results == null || current >= pageSize) {

			if (logger.isDebugEnabled()) {
				logger.debug("Reading page " + page);
			}

			doReadPage();
			page++;
			if (current >= pageSize) {
				current = 0;
			}

		}

		int next = current++;
		if (next < results.size()) {
			return results.get(next);
		} else {
			return null;
		}

	}

	public void doReadPage() {
		results = yammerOperations.listAllUsers(page);
	}

}
