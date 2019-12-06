package irt.stock.web.controllers.engineering.eco;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.eco.Eco;
import irt.stock.data.jpa.beans.engineering.eco.EcoCategory;
import irt.stock.data.jpa.beans.engineering.eco.EcoCategoryRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoFile;
import irt.stock.data.jpa.beans.engineering.eco.EcoFileRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoRelatedCategory;
import irt.stock.data.jpa.beans.engineering.eco.EcoRelatedTo;
import irt.stock.data.jpa.beans.engineering.eco.EcoRelatedToRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoStatus;
import irt.stock.data.jpa.repositories.PartNumberRepository;
import irt.stock.data.jpa.repositories.UserRepository;
import irt.stock.data.jpa.services.UserPrincipal;
import irt.stock.data.jpa.services.storage.StorageService;
import irt.stock.mail.MailWorker;

@RestController
@RequestMapping("engineering/eco")
public class EcoRestController {
	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.files.default.storage.location}") private String rootLocation;
	@Value("${irt.mail.subject.length}") private int subjectLength;

	@Autowired private EcoRepository ecoRepository;
	@Autowired private EcoRelatedToRepository ecoRelatedToRepository;
	@Autowired private EcoCategoryRepository ecoCategoryRepository;
	@Autowired private EcoFileRepository ecoFileRepository;
	@Autowired private UserRepository userRepository;
	@Autowired private PartNumberRepository partNumberRepository;

	@Autowired private TemplateEngine templateEngine;
	@Autowired private MailWorker mailWorker;

	@Autowired private StorageService storageService;

	private User user;
	private String host;

	@ModelAttribute
	public void modelAttribute(Authentication principal, @RequestHeader String host) {

		user = ((UserPrincipal) principal.getPrincipal()).getUser();
		this.host = host;
	}

	@PostMapping("new")
	public String postNewECO(@RequestParam MultiValueMap<String,String> requestParams, @RequestParam("eco_file_upload") List<MultipartFile> files) {

		String message = "";
		try {

		// Create ECO
		Eco eco = new Eco();
		eco.setCreatedBy(user);
		eco.setStatus(EcoStatus.CREATED);
		eco.setReason(requestParams.get("eco_reason").get(0));
		eco.setDescription(requestParams.get("eco_description").get(0));

		long idSendTo = requestParams.get("eco_send_to").stream().mapToLong(Long::parseLong).findAny().getAsLong();
		User sendTo = userRepository.findById(idSendTo).get();
		eco.setApprovedBy(sendTo);

		Eco savedEco = ecoRepository.save(eco);

		// Save Projects ECO related to
		List<Long> relates = requestParams.get("eco_component_id").parallelStream().map(Long::parseLong).collect(Collectors.toList());
		Iterable<PartNumber> partNumbers = partNumberRepository.findAllById(relates);

		List<EcoRelatedTo> ecoRelateTo = StreamSupport.stream(partNumbers.spliterator(), false).map(partNumber->new EcoRelatedTo().setEco(savedEco).setPartNumber(partNumber)).collect(Collectors.toList());
		List<EcoRelatedTo> relatedTo = (List<EcoRelatedTo>) ecoRelatedToRepository.saveAll(ecoRelateTo);
		eco.setEcoRelatedTo(relatedTo);

		// Save Categories of change
		List<Integer> categories = requestParams.get("eco_change_category").parallelStream().map(Integer::parseInt).collect(Collectors.toList());

		EcoCategory[] values = EcoCategory.values();
		List<EcoRelatedCategory> relatedCategories = categories.stream().filter(categoryIndex->categoryIndex>0 && categoryIndex<values.length)
														.map(index->values[index])
														.map(c->new EcoRelatedCategory().setCategory(c).setEco(savedEco))
														.collect(Collectors.toList());

		List<EcoRelatedCategory> related = (List<EcoRelatedCategory>) ecoCategoryRepository.saveAll(relatedCategories);
		eco.setEcoRelatedCategories(related);

		// Upload files
		List<EcoCategory> fileCategories = requestParams.get("eco_file_relation").stream().filter(fr->!fr.isEmpty()).map(Integer::parseInt).map(index->values[index]).collect(Collectors.toList());

		IntStream.range(0, fileCategories.size())
		.forEach(
				index->{
					Path path = Paths.get(rootLocation, "eco", Long.toString(eco.getNumber()));
					Path storedPath = storageService.store(files.get(index), path);
					EcoFile ecoFile = new EcoFile(savedEco, fileCategories.get(index), storedPath.getFileName().toString());
					ecoFileRepository.save(ecoFile);
					eco.addEcoFile(ecoFile);
				});

		mailWorker.sendEmail(user.getEmail(), getSubject(eco), getEmailText(user, eco, host), sendTo.getEmail());

		}catch(Exception e) {
			logger.catching(e);
			message = e.getMessage();
		}

		return message;
	}

	@PostMapping("reject")
	public String rejectECO(long ecoNumber, String comments) {

		String message = "";

		return message;
	}

	@PostMapping("approve")
	public String approveECO(long ecoNumber, String comments) {

		String message = "";

		return message;
	}

	private String getSubject(Eco eco) {
		String ecoNumber = "ECO #" + eco.getNumber() + "; ";
		return ecoNumber + eco.getShortReason(subjectLength - ecoNumber.length());
	}

	private String getEmailText(User user, Eco eco, String host) {
		Context context = new Context();
		context.setVariable("host"	, host);
		context.setVariable("eco"	, eco);
		context.setVariable("user"	, user);
		return templateEngine.process("engineering/eco/eco_email_template", context);
	}
}
