import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class TestClass {
	private final static Logger logger = LogManager.getLogger();

	@Test
	public void test() {

		final Stream<Optional<Integer>> streamOfInts = Stream.of(Optional.of(1), Optional.of(2), Optional.of(3), Optional.of(4), Optional.of(5), Optional.empty());

		// false - list of Optional.empty(); true -> list of Optional.of(Integer)
		final Map<Boolean, List<Optional<Integer>>> collect = streamOfInts.collect(Collectors.partitioningBy(Optional::isPresent));

		final Function<List<Optional<Integer>>, Stream<Integer>> mapToStream = List->List.stream().filter(o->o.isPresent()).map(o->o.get());

		 Optional<Stream<Integer>> result = Optional
				 .of(Optional.of(collect.get(false)).filter(list->list.size()>0).orElse(collect.get(true)))
				 .filter(list->list.size()>0)
				 .filter(list->list.get(0).isPresent())
				 .map(mapToStream)
				 .map(Optional::of)
				 .orElse(Optional.empty());

		logger.error(result);
		assertFalse(result.isPresent());
	}

}
