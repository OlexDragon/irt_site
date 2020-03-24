package irt.stock.web.controllers.engineering.eco;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import irt.stock.data.jpa.beans.PartNumber;
import irt.stock.data.jpa.beans.User;
import irt.stock.data.jpa.beans.engineering.EngineeringChange;
import irt.stock.data.jpa.beans.engineering.eco.Eco;
import irt.stock.data.jpa.beans.engineering.eco.EcoFile;
import irt.stock.data.jpa.beans.engineering.eco.EcoFileRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoRepository;
import irt.stock.data.jpa.beans.engineering.eco.EcoStatus;
import irt.stock.data.jpa.beans.engineering.eco.EcoStatusRepository;
import irt.stock.data.jpa.beans.engineering.ecr.Ecr;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategory.Category;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCategoryRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrComment;
import irt.stock.data.jpa.beans.engineering.ecr.EcrCommentsRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrFile;
import irt.stock.data.jpa.beans.engineering.ecr.EcrFileRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrForwardTo;
import irt.stock.data.jpa.beans.engineering.ecr.EcrForwardToRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrRelatedTo;
import irt.stock.data.jpa.beans.engineering.ecr.EcrRelatedToRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrRepository;
import irt.stock.data.jpa.beans.engineering.ecr.EcrStatus;
import irt.stock.data.jpa.beans.engineering.ecr.EcrStatus.Status;
import irt.stock.data.jpa.beans.engineering.ecr.EcrStatusRepository;
import irt.stock.data.jpa.repositories.PartNumberRepository;
import irt.stock.data.jpa.services.UserPrincipal;
import irt.stock.data.jpa.services.storage.StorageService;
import irt.stock.mail.MailWorker;

@RestController
@RequestMapping("engineering")
public class EngineeringRestController {
	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.files.default.storage.location}") private String rootLocation;

	@Autowired private EcrRepository ecrRepository;
	@Autowired private EcoRepository ecoRepository;
	@Autowired private EcrStatusRepository ecrStatusRepository;
	@Autowired private EcoStatusRepository ecoStatusRepository;
	@Autowired private EcrCommentsRepository ecrCommentsRepository;
	@Autowired private EcrRelatedToRepository ecrRelatedToRepository;
	@Autowired private EcrCategoryRepository ecrCategoryRepository;
	@Autowired private EcrFileRepository ecrFileRepository;
	@Autowired private EcoFileRepository ecoFileRepository;
//	@Autowired private UserRepository userRepository;
	@Autowired private PartNumberRepository partNumberRepository;
	@Autowired private EcrForwardToRepository forwardToRepository;

	@Autowired private MailWorker mailWorker;

	@Autowired private StorageService storageService;

	private User user;

	@ModelAttribute
	public void modelAttribute(Authentication principal) {
		user = ((UserPrincipal) principal.getPrincipal()).getUser();
	}

	@PostMapping("ecr/request")
	@Transactional
	public String postNewECR(
			@RequestParam("component_id")	Set<Long> compomemtIDs,
			@RequestParam("category")		Set<Category> ecoCategory,
			@RequestParam("text_area")	 	String ecrReason,
			@RequestParam("file_category")	List<Category> ecrFileCategory,
			@RequestParam("file_upload")	List<MultipartFile> files,
			HttpServletRequest request) {


		String message = "";
		try {

		// Create ECR
		Ecr savedEcr = ecrRepository.save(new Ecr(ecrReason));
		message = "ecr/" + savedEcr.getNumber();

		// Save ECR Status as CREATED
		EcrStatus savedStatus = ecrStatusRepository.save(new EcrStatus(Status.CREATED, savedEcr, user));
		savedEcr.addStatus(savedStatus);

		// Save Projects ECO related to
		Iterable<PartNumber> partNumbers = partNumberRepository.findAllById(compomemtIDs);
		List<EcrRelatedTo> ecoRelateTo = StreamSupport.stream(partNumbers.spliterator(), false).map(partNumber->new EcrRelatedTo(partNumber, savedEcr)).collect(Collectors.toList());
		List<EcrRelatedTo> savedRelatedTo = (List<EcrRelatedTo>) ecrRelatedToRepository.saveAll(ecoRelateTo);
		savedEcr.setRelatedTos(savedRelatedTo);

		// Save Categories of change
		List<EcrCategory> relatedCategories = ecoCategory.stream().map(c->new EcrCategory(c, savedEcr)).collect(Collectors.toList());
		List<EcrCategory> savedCategories = (List<EcrCategory>) ecrCategoryRepository.saveAll(relatedCategories);
		savedEcr.setCategories(savedCategories);

		uploadEcrFiles(ecrFileCategory, files, savedEcr, savedStatus);

		URL url = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
		mailWorker.sendEcr(savedEcr, url);

		}catch(Exception e) {
			logger.catching(e);
			message = e.getMessage();
		}

		return message;
	}

	@PostMapping("eco/status")
	public String statusECO(	@RequestParam("eco_number")								Long	 			ecoNumber,
								@RequestParam("new_status")								EcoStatus.Status	newStatus,
								@RequestParam("text_area")								String	 			comments,
								@RequestParam("file_category")							List<Category>	 	fileCategory,
								@RequestParam("file_upload")							List<MultipartFile>	files,
																						HttpServletRequest	request) {

		final Message message = new Message("eco/" + ecoNumber);

		return message.message;
	}

	@PostMapping("ecr/status")
	@Transactional
	public String statusChange(	@RequestParam("ecr_number")								Long	 			ecrNumber,
								@RequestParam("new_status")								EcrStatus.Status	newStatus,
								@RequestParam("text_area")								String	 			comments,
								@RequestParam("file_category")							List<Category>	 	fileCategory,
								@RequestParam("file_upload")							List<MultipartFile>	files,
								@RequestParam(name="forvard_to", required = false)		User				forwardTo,
																						HttpServletRequest	request) {

//		logger.error("ecrNumber={}; newStatus={}; comments={}; fileCategory{}; forwardTo={}", ecrNumber, newStatus, comments, fileCategory, forwardTo);

		final Message message = new Message("ecr/" + ecrNumber);

		Optional.ofNullable(ecrNumber)
		.ifPresent(
				number->{

					ecrRepository.findById(number)
					.ifPresent(
							ecr->{

								// Save Status
								EcrStatus ecrStatus = ecrStatusRepository.save( new EcrStatus(newStatus, ecr, user));

								// If forward to user
								if(newStatus == Status.FORWARDED)
									Optional.of(forwardTo)
									.ifPresent(
											u->{
												EcrForwardTo ft = forwardToRepository.save(new EcrForwardTo(ecrStatus, u));
												ecrStatus.setForwardTo(ft);
											});

								// Create ECO
								EngineeringChange engineeringChange;

								String c = comments.trim();

								if(newStatus == Status.LINKED) {

									Eco eco = new Eco(ecr);
									// Set Description if exists
									Optional.of(c).filter(description->!description.isEmpty()).ifPresent(eco::setDescription);

									Eco savedEco = ecoRepository.save(eco);
									EcoStatus ecoStatus = ecoStatusRepository.save(new EcoStatus(EcoStatus.Status.CREATED, savedEco, user));
									savedEco.addStatus(ecoStatus);
									message.message = "eco/" + ecrNumber;

									engineeringChange = savedEco;

									// Upload ECO files
									uploadEcoFiles(fileCategory, files, savedEco, ecoStatus);

								}else {

									engineeringChange = ecr;

									// Save ECR Comments
									if(!c.isEmpty()) {
										EcrComment comment = ecrCommentsRepository.save(new EcrComment(ecrStatus, comments));
										ecrStatus.setComment(comment);
										ecr.addStatus(ecrStatus);
									}

									// Upload ECR files
									uploadEcrFiles(fileCategory, files, ecr, ecrStatus);
								}

								try {

									URL url = new URL(request.getScheme(), request.getServerName(), request.getServerPort(), "");
									mailWorker.sendEcr(engineeringChange, url);

								} catch (MalformedURLException e) {
									logger.catching(e);
								}
			});
		});

		return message.message;
	}

	private void uploadEcrFiles(List<Category> ecrFileCategory, List<MultipartFile> files, Ecr savedEcr, EcrStatus savedStatus) {

		final Path path = Paths.get(rootLocation, "ecr", Long.toString(savedEcr.getNumber()), Long.toString(savedStatus.getId()));

		// Upload ECR files
		IntStream.range(0, ecrFileCategory.size())
		.mapToObj(
				index->{
					Path storedPath = storageService.store(files.get(index), path);
					return new EcrFile(savedStatus, ecrFileCategory.get(index), storedPath.getFileName().toString());
				})
		.map(ecrFileRepository::save)
		.forEach(savedStatus::addFile);
	}

	private void uploadEcoFiles(List<EcrCategory.Category> ecrFileCategory, List<MultipartFile> files, Eco savedEco, EcoStatus ecoStatus) {

		final Path path = Paths.get(rootLocation, "eco", Long.toString(savedEco.getNumber()), Long.toString(ecoStatus.getId()));

		// Upload ECR files
		IntStream.range(0, ecrFileCategory.size())
		.mapToObj(
				index->{
					Path storedPath = storageService.store(files.get(index), path);
					return new EcoFile(ecoStatus, ecrFileCategory.get(index), storedPath.getFileName().toString());
				})
		.map(ecoFileRepository::save)
		.forEach(ecoStatus::addFile);
	}

	private class Message{

		public Message(String string) {
			message = string;
		}

		private String message;
	}
}
