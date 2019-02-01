package irt.stock.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.activation.FileTypeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import irt.stock.data.jpa.beans.Component;
import irt.stock.data.jpa.beans.ComponentImage;
import irt.stock.data.jpa.beans.ImageLink;
import irt.stock.data.jpa.repositories.ComponentImageRepository;
import irt.stock.data.jpa.repositories.ComponentRepository;
import irt.stock.data.jpa.repositories.ImageLinkRepository;

@RestController
@RequestMapping("/component")
public class ComponentImageRestController {
	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.default.component.image}") 			private String defaultImage;
	@Value("${irt.default.component.images.folder}") 	private String defaultImagesFolder;

	@Autowired private ComponentImageRepository componentImageRepository;
	@Autowired private ImageLinkRepository imageRepository;
	@Autowired private ComponentRepository componentRepository;
	
	@GetMapping("image/{componentId}")
	public ResponseEntity<byte[]> getImage(@PathVariable(required=false) Long componentId) throws IOException {
		logger.entry(componentId);

		Optional<ImageLink> oLink = Optional.ofNullable(componentId)
				.flatMap(componentImageRepository::findById)
				.map(ComponentImage::getImageLink);

		Optional<Path> oFile = linkToPath(oLink);
		Optional<InputStream> oInputStream = fileToStream(oFile);
		InputStream is = oInputStream.orElse(getClass().getResourceAsStream(defaultImage));

		File f = oFile.orElse(Paths.get(defaultImage)).toFile();

		return ResponseEntity.ok()
	            .header("Content-Disposition", "attachment; filename=" + f.getName())
	            .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(f)))
	            .body(IOUtils.toByteArray(is));
	}

	@GetMapping("image/byname")
	public ResponseEntity<byte[]> getImage(@RequestParam String imageName) {
		logger.entry(imageName);

		Optional<ImageLink> oLink = imageRepository.findByLinkEndsWith(imageName);

		Optional<Path> oFile = linkToPath(oLink);
		Optional<InputStream> oInputStream = fileToStream(oFile);

		HttpStatus status = oLink
				.map(
						link->
						oFile.map(file->HttpStatus.OK)
						.orElse(HttpStatus.INTERNAL_SERVER_ERROR)
					)
					.orElse(HttpStatus.SERVICE_UNAVAILABLE);

		logger.error("{}\n {}\n {}", status, oFile, oInputStream);
		BodyBuilder bodyBuilder = ResponseEntity.status(status);

		oFile
		.map(Path::toFile)
		.ifPresent(
				file->{

					bodyBuilder
					.header("Content-Disposition", "attachment; filename=" + file.getName())
					.contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(file)));
				});

		return bodyBuilder
				.body(
						oInputStream.map(
								is -> {
									try {
										return IOUtils.toByteArray(is);
									} catch (IOException e) {
										logger.catching(e);
									}
									return "IOUtils.toByteArray error".getBytes();
								})
						.orElse("File not found".getBytes()));	//Text 'File not found' is used in the home.html file in the 'sourceURL=drop_image.js' JavaScript
	}

	private Optional<InputStream> fileToStream(Optional<Path> oFile) {
		return oFile
				.map(p -> {
					try {
						logger.trace("File'{}' to sream.", p);
						return new FileInputStream(p.toFile());
					} catch (FileNotFoundException e) {
						logger.catching(e);
					}
					return null;
				})
				.filter(f->f!=null)
				.map(InputStream.class::cast);
	}

	private Optional<Path> linkToPath(Optional<ImageLink> oLink) {
		logger.entry(oLink);
		return oLink
				.map(ImageLink::getLink)
				.map(
						link->{
							logger.error(link);
							return Paths.get(defaultImagesFolder, link);
						})
				.filter(Files::exists);
	}

	@PostMapping(value="image/upload/{componentId}", consumes = {"multipart/form-data"})
	public ResponseEntity<byte[]> uploadImage(@PathVariable Long componentId, @RequestParam("imageFile") MultipartFile imageFile) {
		logger.entry(componentId, imageFile, imageFile.getSize());

		if (!imageFile.isEmpty()) {
			try {
				Optional<Component> oComponent = componentRepository.findById(componentId);

				if(oComponent.isPresent()) {

					ResponseEntity<byte[]> entity = getImage(imageFile.getOriginalFilename());
					HttpStatus statusCode = entity.getStatusCode();

					logger.debug(statusCode);

					if(statusCode!=HttpStatus.OK) {
						ImageLink imageLink = saveImage(imageFile);
						componentImageRepository.save(new ComponentImage(componentId, imageLink));
					}

				}

			} catch (IOException e) {
				logger.catching(e);
			}
		}
		return null;
	}

	private ImageLink saveImage(MultipartFile imageFile) throws IOException {
		new File(defaultImagesFolder).mkdirs();	//create a directory if there is none
		String originalFilename = imageFile.getOriginalFilename();
		Path path = Paths.get(defaultImagesFolder, originalFilename);
		imageFile.transferTo(path);
		return imageRepository.save(new ImageLink(originalFilename));
	}
}
